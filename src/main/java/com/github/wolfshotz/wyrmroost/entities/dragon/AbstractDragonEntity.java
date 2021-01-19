package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.client.sounds.FlyingSound;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInvHandler;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.*;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRSitGoal;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggProperties;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.items.DragonArmorItem;
import com.github.wolfshotz.wyrmroost.items.DragonEggItem;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.minecraft.entity.ai.attributes.Attributes.FLYING_SPEED;
import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

/**
 * Created by com.github.WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatable
{
    public static final byte HEAL_PARTICLES_DATA_ID = 8;

    // Common Data Parameters
    public static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.VARINT);
    public static final DataParameter<ItemStack> ARMOR = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.ITEMSTACK);
    public static final DataParameter<Optional<BlockPos>> HOME_POS = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

    private final Set<EntityDataEntry<?>> dataEntries = new HashSet<>();
    public final LazyOptional<DragonInvHandler> invHandler;
    public final TickFloat sleepTimer = new TickFloat().setLimit(0, 1);
    private int sleepCooldown;
    public boolean wingsDown;
    public int breedCount;
    private Animation animation = NO_ANIMATION;
    private int animationTick;

    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        stepHeight = 1;

        DragonInvHandler inv = createInv();
        invHandler = LazyOptional.of(inv == null? null : () -> inv);
        lookController = new LessShitLookController(this);
        if (hasDataParameter(FLYING)) moveController = new FlyerMoveController(this);

        registerDataEntry("HomePos", EntityDataEntry.BLOCK_POS.optional(), HOME_POS, Optional.empty());
        registerDataEntry("BreedCount", EntityDataEntry.INTEGER, () -> breedCount, i -> breedCount = i);
        invHandler.ifPresent(i -> registerDataEntry("Inv", EntityDataEntry.COMPOUND, i::serializeNBT, i::deserializeNBT));
    }

    @Override
    protected PathNavigator createNavigator(World worldIn)
    {
        return new BetterPathNavigator(this);
    }

    @Override
    protected BodyController createBodyController()
    {
        return new DragonBodyController(this);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new WRSitGoal(this));
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
        for (EntityDataEntry<?> entry : dataEntries) entry.write(nbt);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
        for (EntityDataEntry<?> entry : dataEntries) entry.read(nbt);
        applyAttributes();
    }

    public <T> void registerDataEntry(String key, EntityDataEntry.SerializerType<T> type, Supplier<T> write, Consumer<T> read)
    {
        if (!world.isRemote) dataEntries.add(new EntityDataEntry<>(key, type, write, read));
    }

    public <T> void registerDataEntry(String key, EntityDataEntry.SerializerType<T> type, DataParameter<T> param, T value)
    {
        dataManager.register(param, value);
        registerDataEntry(key, type, () -> dataManager.get(param), v -> dataManager.set(param, v));
    }

    public boolean hasDataParameter(DataParameter<?> param)
    {
        return dataManager.entries.containsKey(param.getId());
    }

    public int getVariant()
    {
        return hasDataParameter(VARIANT)? dataManager.get(VARIANT) : 0;
    }

    public void setVariant(int variant)
    {
        dataManager.set(VARIANT, variant);
    }

    /**
     * @return true for male, false for female. anything else is a political abomination and needs to be cancelled.
     */
    public boolean isMale()
    {
        return hasDataParameter(GENDER)? dataManager.get(GENDER) : true;
    }

    public void setGender(boolean sex)
    {
        dataManager.set(GENDER, sex);
    }

    public boolean isSleeping()
    {
        return hasDataParameter(SLEEPING)? dataManager.get(SLEEPING) : false;
    }

    public void setSleeping(boolean sleep)
    {
        if (isSleeping() == sleep) return;

        dataManager.set(SLEEPING, sleep);
        if (!world.isRemote)
        {
            if (sleep) clearAI();
            else sleepCooldown = 350;
        }
    }

    public boolean shouldSleep()
    {
        if (sleepCooldown > 0) return false;
        if (world.isDaytime()) return false;
        if (!isIdling()) return false;
        if (isTamed())
        {
            if (isAtHome())
            {
                if (defendsHome()) return getHealth() < getMaxHealth() * 0.25;
            }
            else if (!func_233684_eK_()) return false;
        }

        return getRNG().nextDouble() < 0.0065;
    }

    public boolean shouldWakeUp()
    {
        return world.isDaytime() && getRNG().nextDouble() < 0.0065;
    }

    public boolean isFlying()
    {
        return hasDataParameter(FLYING)? dataManager.get(FLYING) : false;
    }

    public void setFlying(boolean fly)
    {
        if (isFlying() == fly) return;
        dataManager.set(FLYING, fly);
        if (fly)
        {
            // make sure NOT to switch the navigator if liftoff fails
            if (liftOff()) navigator = new FlyerPathNavigator(this);
        }
        else navigator = new BetterPathNavigator(this);
    }

    public boolean hasArmor()
    {
        return hasDataParameter(ARMOR) && dataManager.get(ARMOR).getItem() instanceof DragonArmorItem;
    }

    public ItemStack getArmor()
    {
        return hasDataParameter(ARMOR)? dataManager.get(ARMOR) : ItemStack.EMPTY;
    }

    public void setArmor(@Nullable ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof DragonArmorItem)) stack = ItemStack.EMPTY;
        dataManager.set(ARMOR, stack);
    }

    public void setSit(boolean sitting)
    {
        func_233687_w_(sitting);
    }

    @Override
    public void func_233686_v_(boolean sitting)
    {
        super.func_233686_v_(sitting);
        if (sitting) clearAI();
    }

    public DragonInvHandler getInvHandler()
    {
        return invHandler.orElseThrow(() -> new NoSuchElementException("This boi doesn't have an inventory wtf are u doing"));
    }

    public DragonInvHandler createInv()
    {
        return null;
    }

    @Override
    public void tick()
    {
        super.tick();
        updateAnimations();
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        if (isServerWorld())
        {
            // uhh so were falling, we should probably start flying
            boolean flying = shouldFly();
            if (flying != isFlying()) setFlying(flying);

            if (sleepCooldown > 0) --sleepCooldown;
            if (isSleeping())
            {
                ((LessShitLookController) getLookController()).restore();
                if (getHealth() < getMaxHealth() && getRNG().nextDouble() < 0.005) heal(1);

                if (shouldWakeUp())
                {
                    setSleeping(false);
                }
            }
            else if (shouldSleep())
            {
                setSleeping(true);
            }

            // todo figure out a better target system?
            LivingEntity target = getAttackTarget();
            if (target != null && (!target.isAlive() || !canAttack(target) || !shouldAttackEntity(target, getOwner())))
                setAttackTarget(null);
        }
        else
        {
            doSpecialEffects();
        }
    }

    /**
     * Not to be confused with {@link #updatePassenger(Entity)}, as this is called when were riding something
     */
    @Override
    public void updateRidden()
    {
        super.updateRidden();

        Entity entity = getRidingEntity();

        if (entity == null || !entity.isAlive())
        {
            stopRiding();
            return;
        }

        setMotion(Vector3d.ZERO);
        clearAI();

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            int index = player.getPassengers().indexOf(this);
            if ((player.isSneaking() && !player.abilities.isFlying) || isInWater() || index > 2)
            {
                stopRiding();
                setSit(false);
                return;
            }

            prevRotationPitch = rotationPitch = player.rotationPitch / 2;
            rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
            setRotation(player.rotationYawHead, rotationPitch);

            Vector3d vec3d = getRidingPosOffset(index);
            if (player.isElytraFlying())
            {
                if (!canFly())
                {
                    stopRiding();
                    return;
                }

                vec3d = vec3d.scale(1.5);
                setFlying(true);
            }
            Vector3d pos = Mafs.getYawVec(player.renderYawOffset, vec3d.x, vec3d.z).add(player.getPosX(), player.getPosY() + vec3d.y, player.getPosZ());
            setPosition(pos.x, pos.y, pos.z);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Vector3d getRidingPosOffset(int passengerIndex)
    {
        double x = getWidth() * 0.5d + getRidingEntity().getWidth() * 0.5d;
        switch (passengerIndex)
        {
            default:
            case 0:
                return new Vector3d(0, 1.81, 0);
            case 1:
                return new Vector3d(x, 1.38d, 0);
            case 2:
                return new Vector3d(-x, 1.38d, 0);
        }
    }

    /**
     * Not to be confused with {@link #updateRidden()}, as this is called when were being ridden by something
     */
    @Override
    public void updatePassenger(Entity passenger)
    {
        if (isPassenger(passenger))
        {
            Vector3d offset = getPassengerPosOffset(passenger, getPassengers().indexOf(passenger));
            Vector3d pos = Mafs.getYawVec(renderYawOffset, offset.x, offset.z).add(getPosX(), getPosY() + offset.y + passenger.getYOffset(), getPosZ());
            passenger.setPosition(pos.x, pos.y, pos.z);
        }
    }

    public Vector3d getPassengerPosOffset(Entity entity, int index)
    {
        return new Vector3d(0, getMountedYOffset(), 0);
    }

    // Ok so some basic notes here:
    // if the action result is a SUCCESS, the player swings its arm.
    // however, itll send that arm swing twice if we aren't careful.
    // essentially, returning SUCCESS on server will send a swing arm packet to notify the client to animate the arm swing
    // client tho, it will just animate it.
    // so if we aren't careful, both will happen. So its important to do the following for common execution:
    // ActionResultType.func_233537_a_(World::isRemote)
    // essentially, if the provided boolean is true, it will return SUCCESS, else CONSUME.
    // so since the world is client, it will be SUCCESS on client and CONSUME on server.
    // That way, the server never sends the arm swing packet.
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        final ActionResultType COMMON_SUCCESS = ActionResultType.func_233537_a_(world.isRemote);

        if (isOwner(player) && player.isSneaking() && !isFlying())
        {
            setSit(!func_233684_eK_());
            return COMMON_SUCCESS;
        }

        if (isTamed())
        {
            if (isFoodItem(stack))
            {
                boolean flag = getHealth() < getMaxHealth();
                if (isChild())
                {
                    if (!world.isRemote) ageUp((int) ((-getGrowingAge() / 20) * 0.1F), true);
                    flag = true;
                }

                if (flag)
                {
                    eat(stack);
                    return COMMON_SUCCESS;
                }
            }

            if (isBreedingItem(stack) && getGrowingAge() == 0)
            {
                if (!world.isRemote && !isInLove())
                {
                    eat(stack);
                    setInLove(player);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
        }

        if (canBeRidden(player) && !player.isSneaking())
        {
            if (!world.isRemote) player.startRiding(this);
            return COMMON_SUCCESS;
        }

        return ActionResultType.PASS;
    }

    // Override to make processInteract way less annoying
    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        ActionResultType result = stack.interactWithEntity(player, this, hand);
        if (!result.isSuccessOrConsume()) result = playerInteraction(player, hand, stack);
        if (result.isSuccessOrConsume()) setSleeping(false);
        return result;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void travel(Vector3d vec3d)
    {
        float speed = getTravelSpeed();

        if (canPassengerSteer()) // Were being controlled; override ai movement
        {
            LivingEntity entity = (LivingEntity) getControllingPassenger();
            double moveY = vec3d.y;
            double moveX = entity.moveStrafing * 0.5;
            double moveZ = entity.moveForward;

            // rotate head to match driver. rotationYaw is handled relative to this.
            rotationYawHead = entity.rotationYawHead;
            rotationPitch = entity.rotationPitch * 0.5f;

            if (isFlying())
            {
                if (entity.moveForward != 0) moveY = entity.getLookVec().y * speed * 18;
                moveX = vec3d.x;

                if (entity instanceof ServerPlayerEntity)
                    ((ServerPlayerEntity) entity).connection.vehicleFloating = false;
            }
            else
            {
                speed *= 0.35f;
                if (entity.isJumping && canFly()) setFlying(true);
            }

            setAIMoveSpeed(speed);
            vec3d = new Vector3d(moveX, moveY, moveZ);
        }

        if (isFlying())
        {
            // Move relative to rotationYaw - handled in the move controller or by the passenger
            moveRelative(speed, vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.88f));

            // hover in place
            Vector3d motion = getMotion();
            if (motion.length() < 0.04f) setMotion(motion.add(0, Math.cos(ticksExisted * 0.1f) * 0.02f, 0));

            // limb swinging animations
            float limbSpeed = 0.4f;
            float amount = 1f;
            if (getPosY() - prevPosY < -0.1f)
            {
                amount = 0f;
                limbSpeed = 0.2f;
            }

            prevLimbSwingAmount = limbSwingAmount;
            limbSwingAmount += (amount - limbSwingAmount) * limbSpeed;
            limbSwing += limbSwingAmount;

            return;
        }

        super.travel(vec3d);
    }

    public float getTravelSpeed()
    {
        //@formatter:off
        return isFlying()? (float) getAttributeValue(FLYING_SPEED)
                         : (float) getAttributeValue(MOVEMENT_SPEED);
        //@formatter:on
    }

    public boolean shouldFly()
    {
        return canFly() && getAltitude() > getFlightThreshold();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (key.equals(SLEEPING) || key.equals(FLYING) || key.equals(TAMED))
        {
            recalculateSize();
            if (world.isRemote && key == FLYING && isFlying() && canPassengerSteer())
                FlyingSound.play(this);
        }
        else if (key == ARMOR)
        {
            if (!world.isRemote)
            {
                ModifiableAttributeInstance attribute = getAttribute(Attributes.ARMOR);
                if (attribute.getModifier(DragonArmorItem.ARMOR_UUID) != null)
                    attribute.removeModifier(DragonArmorItem.ARMOR_UUID);
                if (hasArmor())
                {
                    attribute.applyNonPersistentModifier(new AttributeModifier(DragonArmorItem.ARMOR_UUID, "Armor Modifier", DragonArmorItem.getDmgReduction(getArmor()), AttributeModifier.Operation.ADDITION));
                    playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1, 1, true);
                }
            }
        }
        else super.notifyDataManagerChange(key);

    }

    @Override
    public void handleStatusUpdate(byte id)
    {
        if (id == HEAL_PARTICLES_DATA_ID)
        {
            for (int i = 0; i < getWidth() * getHeight(); ++i)
            {
                double x = getPosX() + Mafs.nextDouble(getRNG()) * getWidth() + 0.4d;
                double y = getPosY() + getRNG().nextDouble() * getHeight();
                double z = getPosZ() + Mafs.nextDouble(getRNG()) * getWidth() + 0.4d;
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
        else super.handleStatusUpdate(id);
    }

    public ItemStack getStackInSlot(int slot)
    {
        return invHandler.map(i -> i.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    /**
     * It is VERY important to be careful when using this.
     * It is VERY sidedness sensitive. If not done correctly, it can result in the loss of items! <P>
     * {@code if (!world.isReomote) setStackInSlot(...)}
     */
    public void setStackInSlot(int slot, ItemStack stack)
    {
        invHandler.ifPresent(i -> i.setStackInSlot(slot, stack));
    }

    public void attackInBox(AxisAlignedBB box)
    {
        attackInBox(box, 0);
    }

    public void attackInBox(AxisAlignedBB box, int disabledShieldTime)
    {
        List<LivingEntity> attackables = world.getEntitiesWithinAABB(LivingEntity.class, box, entity -> entity != this && !isPassenger(entity) && shouldAttackEntity(entity, getOwner()));
        if (WRConfig.debugMode && world.isRemote) RenderHelper.DebugBox.INSTANCE.queue(box);
        for (LivingEntity attacking : attackables)
        {
            attackEntityAsMob(attacking);
            if (disabledShieldTime > 0 && attacking instanceof PlayerEntity)
            {
                PlayerEntity player = ((PlayerEntity) attacking);
                if (player.isHandActive() && player.getActiveItemStack().isShield(player))
                {
                    player.getCooldownTracker().setCooldown(Items.SHIELD, disabledShieldTime);
                    player.resetActiveHand();
                    world.setEntityState(player, (byte) 9);
                }
            }
        }
    }

    public AxisAlignedBB getOffsetBox(float offset)
    {
        return getBoundingBox().offset(Vector3d.fromPitchYaw(0, renderYawOffset).scale(offset));
    }

    @Override // Dont damage owners other pets!
    public boolean attackEntityAsMob(Entity entity)
    {
        if (isOnSameTeam(entity)) return false;
        return super.attackEntityAsMob(entity);
    }

    @Override // We shouldnt be targetting pets...
    public boolean shouldAttackEntity(LivingEntity target, @Nullable LivingEntity owner)
    {
        return !isOnSameTeam(target);
    }

    @Override
    public boolean canAttack(LivingEntity target)
    {
        return !isChild() && super.canAttack(target);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (isImmuneToArrows() && source.getImmediateSource() != null)
        {
            EntityType<?> attackSource = source.getImmediateSource().getType();
            if (attackSource == EntityType.ARROW) return false;
            else if (attackSource == WREntities.GEODE_TIPPED_ARROW.get()) amount *= 0.5f;
        }

        setSleeping(false);
        setSit(false);
        return super.attackEntityFrom(source, amount);
    }

    public void doSpecialEffects()
    {
    }

    public boolean tryTeleportToOwner()
    {
        if (getOwner() == null) return false;
        final int CONSTRAINT = (int) (getWidth() * 0.5) + 1;
        BlockPos pos = getOwner().getPosition();
        BlockPos.Mutable potentialPos = new BlockPos.Mutable();

        for (int x = -CONSTRAINT; x < CONSTRAINT; x++)
            for (int y = 0; y < 4; y++)
                for (int z = -CONSTRAINT; z < CONSTRAINT; z++)
                {
                    potentialPos.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (trySafeTeleport(potentialPos)) return true;
                }
        return false;
    }

    public boolean trySafeTeleport(BlockPos pos)
    {
        if (world.hasNoCollisions(this, getBoundingBox().offset(pos.subtract(getPosition()))))
        {
            setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, rotationYaw, rotationPitch);
            return true;
        }
        return false;
    }

    @Override
    public BlockPos getHomePosition()
    {
        return getHomePos().orElse(BlockPos.ZERO);
    }

    public Optional<BlockPos> getHomePos()
    {
        return dataManager.get(HOME_POS);
    }

    public void setHomePos(@Nullable BlockPos pos)
    {
        setHomePos(Optional.ofNullable(pos));
    }

    public void setHomePos(Optional<BlockPos> pos)
    {
        dataManager.set(HOME_POS, pos);
    }

    public void clearHome()
    {
        setHomePos(Optional.empty());
    }

    @Override
    public boolean detachHome()
    {
        return getHomePos().isPresent();
    }

    @Override
    public float getMaximumHomeDistance()
    {
        return WRConfig.homeRadius * WRConfig.homeRadius;
    }

    @Override
    public void setHomePosAndDistance(BlockPos pos, int distance)
    {
        setHomePos(pos);
    }

    @Override
    public boolean isWithinHomeDistanceCurrentPosition()
    {
        return isWithinHomeDistanceFromPosition(getPosition());
    }

    @Override
    public boolean isWithinHomeDistanceFromPosition(BlockPos pos)
    {
        Optional<BlockPos> home = getHomePos();
        return home.map(h -> h.distanceSq(pos) <= getMaximumHomeDistance()).orElse(true);
    }

    public boolean isAtHome()
    {
        return detachHome() && isWithinHomeDistanceCurrentPosition();
    }

    @Override
    protected void dropInventory()
    {
        invHandler.ifPresent(i -> i.getStacks().forEach(this::entityDropItem));
    }

    public void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    public double getAltitude()
    {
        BlockPos.Mutable pos = getPosition().toMutable();

        // cap to the world void (y = 0)
        while (pos.getY() > 0 && !world.getBlockState(pos.down()).getMaterial().isSolid()) pos.move(0, -1, 0);
        return getPosY() - pos.getY();
    }

    // overload because... WHY IS `World` A PARAMETER WTF THE FIELD IS LITERALLY PUBLIC
    public void eat(ItemStack stack)
    {
        onFoodEaten(world, stack);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack onFoodEaten(World world, ItemStack stack)
    {
        Vector3d mouth = getApproximateMouthPos();

        if (world.isRemote)
        {
            double width = getWidth();
            for (int i = 0; i < Math.max(width * width * 2, 12); ++i)
            {
                Vector3d vec3d1 = new Vector3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) rand.nextFloat() - 0.5D) * 0.1D);
                vec3d1 = vec3d1.rotatePitch(-rotationPitch * (Mafs.PI / 180f));
                vec3d1 = vec3d1.rotateYaw(-rotationYaw * (Mafs.PI / 180f));
                world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), mouth.x + Mafs.nextDouble(getRNG()) * (width * 0.2), mouth.y, mouth.z + Mafs.nextDouble(getRNG()) * (width * 0.2), vec3d1.x, vec3d1.y, vec3d1.z);
            }
            world.playSound(null, getPosX(), getPosY(), getPosZ(), getEatSound(stack), SoundCategory.NEUTRAL, 1f, 1f + (rand.nextFloat() - rand.nextFloat()) * 0.4f);
        }
        else
        {
            final float max = getMaxHealth();
            if (getHealth() < max) heal(Math.max((int) max / 5, 4)); // Base healing on max health, minumum 2 hearts.

            Item item = stack.getItem();
            if (item.isFood())
            {
                for (Pair<EffectInstance, Float> pair : item.getFood().getEffects())
                    if (!world.isRemote && pair.getFirst() != null && rand.nextFloat() < pair.getSecond())
                        addPotionEffect(new EffectInstance(pair.getFirst()));
            }
            if (item.hasContainerItem(stack))
                entityDropItem(item.getContainerItem(stack), (float) (mouth.y - getPosY()));
            stack.shrink(1);
        }

        return stack;
    }

    public boolean tame(boolean tame, @Nullable PlayerEntity tamer)
    {
        if (isTamed()) return true;
        if (world.isRemote) return false;
        if (tame && tamer != null && !ForgeEventFactory.onAnimalTame(this, tamer))
        {
            setTamedBy(tamer);
            setHealth(getMaxHealth());
            clearAI();
            world.setEntityState(this, (byte) 7); // heart particles
            return true;
        }
        else world.setEntityState(this, (byte) 6); // black particles

        return false;
    }

    @Override
    public void heal(float healAmount)
    {
        super.heal(healAmount);
        world.setEntityState(this, HEAL_PARTICLES_DATA_ID);
    }

    public int getYawRotationSpeed()
    {
        return isFlying()? 6 : 75;
    }

    public boolean isRiding()
    {
        return getRidingEntity() != null;
    }

    @Override
    public boolean canMateWith(AnimalEntity mate)
    {
        if (!(mate instanceof AbstractDragonEntity)) return false;
        AbstractDragonEntity dragon = (AbstractDragonEntity) mate;
        if (func_233684_eK_() || dragon.func_233684_eK_()) return false;
        if (isMale() == dragon.isMale()) return false;
        return super.canMateWith(mate);
    }

    @Override
    public void setChild(boolean child)
    {
        setGrowingAge(child? DragonEggProperties.get(getType()).getGrowthTime() : 0);
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity mate)
    {
        return (AgeableEntity) getType().create(world);
    }

    @Override
    public void func_234177_a_(ServerWorld world, AnimalEntity mate)
    {
        final BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this, mate, null);
        if (MinecraftForge.EVENT_BUS.post(event)) // cancelled
            return;

        final AgeableEntity child = event.getChild();
        if (child == null)
        {
            ItemStack eggStack = DragonEggItem.getStack(getType());
            ItemEntity eggItem = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), eggStack);
            eggItem.setMotion(0, getHeight() / 3, 0);
            world.addEntity(eggItem);
        }
        else
        {
            child.setChild(true);
            child.setLocationAndAngles(getPosX(), getPosY(), getPosZ(), 0, 0);
            world.func_242417_l(child);
        }

        breedCount++;
        ((AbstractDragonEntity) mate).breedCount++;

        ServerPlayerEntity serverPlayer = getLoveCause();

        if (serverPlayer == null && mate.getLoveCause() != null)
            serverPlayer = mate.getLoveCause();

        if (serverPlayer != null)
        {
            serverPlayer.addStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this, mate, child);
        }

        setGrowingAge(6000);
        mate.setGrowingAge(6000);
        resetInLove();
        mate.resetInLove();
        world.setEntityState(this, (byte) 18);
        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
            world.addEntity(new ExperienceOrbEntity(world, getPosX(), getPosY(), getPosZ(), getRNG().nextInt(7) + 1));
    }

    @Override
    protected void addPassenger(Entity passenger)
    {
        super.addPassenger(passenger);
        if (getControllingPassenger() == passenger && isOwner((LivingEntity) passenger))
        {
            clearAI();
            setSit(false);
            clearHome();
            if (getLeashed()) clearLeashed(true, true);
        }
    }

    /**
     * Get the player potentially controlling this dragon
     * {@code null} if its not a player or no controller at all.
     */
    @Nullable
    public PlayerEntity getControllingPlayer()
    {
        Entity passenger = getControllingPassenger();
        if (passenger instanceof PlayerEntity) return (PlayerEntity) passenger;
        return null;
    }

    public void clearAI()
    {
        isJumping = false;
        navigator.clearPath();
        setAttackTarget(null);
        setRevengeTarget(null);
        setMoveForward(0);
        setMoveVertical(0);
    }

    public boolean isIdling()
    {
        return getNavigator().noPath() && getAttackTarget() == null && !isBeingRidden() && !isInWaterOrBubbleColumn() && !isFlying();
    }

    /**
     * A universal getter for the position of the mouth on the dragon.
     * This is prone to be inaccurate, but can serve good enough for most things
     * If a more accurate position is needed, best to override and adjust accordingly.
     *
     * @return An approximate position of the mouth of the dragon
     */
    public Vector3d getApproximateMouthPos()
    {
        Vector3d position = getEyePosition(1).subtract(0, 0.75d, 0);
        double dist = (getWidth() / 2) + 0.75d;
        return position.add(getVectorForRotation(rotationPitch, rotationYawHead).scale(dist));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(SpawnEggItem.getEgg(getType()));
    }

    public List<LivingEntity> getEntitiesNearby(double radius, Predicate<LivingEntity> filter)
    {
        return world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(radius), filter.and(e -> e != this));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isOnSameTeam(Entity entity)
    {
        if (entity == this) return true;
        if (entity instanceof LivingEntity && isOwner(((LivingEntity) entity))) return true;
        if (entity instanceof TameableEntity && getOwner() != null && getOwner().equals(((TameableEntity) entity).getOwner()))
            return true;
        return entity.isOnScoreboardTeam(getTeam());
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch)
    {
        playSound(soundIn, volume, pitch, false);
    }

    public void playSound(SoundEvent sound, float volume, float pitch, boolean local)
    {
        if (isSilent()) return;

        volume *= getSoundVolume();
        pitch *= getSoundPitch();

        if (local) world.playSound(getPosX(), getPosY(), getPosZ(), sound, getSoundCategory(), volume, pitch, false);
        else world.playSound(null, getPosX(), getPosY(), getPosZ(), sound, getSoundCategory(), volume, pitch);
    }

    @Override
    public void playAmbientSound()
    {
        if (!isSleeping()) super.playAmbientSound();
    }

    public void flapWings()
    {
        playSound(WRSounds.WING_FLAP.get(), 3, 1, true);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        if (isRiding() && source == DamageSource.IN_WALL) return true;
        if (isImmuneToArrows() && source == DamageSource.CACTUS) return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT dataTag)
    {
        if (hasDataParameter(GENDER)) setGender(getRNG().nextBoolean());
        if (hasDataParameter(VARIANT)) setVariant(determineVariant());

        applyAttributes();
        setHealth(getMaxHealth());

        return super.onInitialSpawn(world, difficulty, reason, data, dataTag);
    }

    /**
     * This method is called after the entity is read, and after the entity initally spawns.
     * It is intended to modify the base attributes based on the entity after it has been fully constructed (and guaranteed to spawn)
     */
    public void applyAttributes()
    {
    }

    public int determineVariant()
    {
        return 0;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return super.canBeCollidedWith() && !isRiding();
    }

    @Override
    public boolean canPassengerSteer() // Only OWNERS can control their pets
    {
        Entity entity = getControllingPassenger();
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;
            return isOwner(player) && (!world.isRemote || player.isUser()); // fix vehicle-desync
        }
        return false;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty()? null : passengers.get(0);
    }

    @Override
    protected boolean canBeRidden(Entity entityIn)
    {
        return false;
    }

    @Override
    public boolean isOnLadder()
    {
        return false;
    }

    /**
     * Recieve the keybind message from the current controlling passenger.
     *
     * @param key     shut up
     * @param mods    the modifiers that is pressed when this key was pressed (e.g. shift was held, ctrl etc {@link org.lwjgl.glfw.GLFW})
     * @param pressed true if pressed, false if released. pretty straight forward idk why ur fucking asking.
     */
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider)
    {
        return false;
    }

    public boolean defendsHome()
    {
        return false;
    }

    /**
     * Sort of misleading name. if this is true, then {@link MobEntity#updateEntityActionState()} is not ticked:
     * which tl;dr does not update any AI including Goal Selectors, Pathfinding, Moving, etc.
     * Do not perform any AI actions while: Not Sleeping; not being controlled, etc.
     */
    @Override
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked() || isSleeping() || isRiding();
    }

    public boolean canFly()
    {
        return !isChild() && !canSwim() && !isRiding() && !getLeashed();
    }

    /**
     * Get the motion this entity performs when jumping
     */
    @Override
    protected float getJumpUpwardsMotion()
    {
        if (canFly()) return (getHeight() * getJumpFactor()) * 0.6f;
        else return super.getJumpUpwardsMotion();
    }

    public boolean liftOff()
    {
        if (!canFly()) return false;
        if (!onGround) return true; // We can't lift off the ground in the air...

        int heightDiff = world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) getPosX(), (int) getPosZ()) - (int) getPosY();
        if (heightDiff > 0 && heightDiff <= getFlightThreshold())
            return false; // position has too low of a ceiling, can't fly here.

        setSit(false);
        setSleeping(false);
        jump();
        return true;
    }

    @Override // Disable fall calculations if we can fly (fall damage etc.)
    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        if (canFly()) return false;
        return super.onLivingFall(distance - (int) (getHeight() * 0.8), damageMultiplier);
    }

    public int getFlightThreshold()
    {
        return (int) getHeight();
    }

    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
    }

    @Override
    public void clearLeashed(boolean sendPacket, boolean dropLead)
    {
        super.clearLeashed(sendPacket, dropLead);
        clearHome();
    }

    public boolean isImmuneToArrows()
    {
        return false;
    }

    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.HOME);
        screen.addAction(StaffAction.SIT);

        screen.addTooltip(new StringTextComponent(Character.toString('\u2764'))
                .mergeStyle(TextFormatting.RED)
                .append(new StringTextComponent(String.format(" %s / %s", (int) (getHealth() / 2), (int) getMaxHealth() / 2)).mergeStyle(TextFormatting.WHITE))
                .getString());
        if (hasDataParameter(GENDER))
        {
            boolean isMale = isMale();
            screen.addTooltip(new TranslationTextComponent("entity.wyrmroost.dragons.gender." + (isMale? "male" : "female"))
                    .mergeStyle(isMale? TextFormatting.DARK_AQUA : TextFormatting.RED).getString());
        }
    }

    public void addContainerInfo(DragonInvContainer container)
    {
        container.makePlayerSlots(container.playerInv, 17, 136);
    }

    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (func_233684_eK_() || isSleeping()) size = size.scale(1, 0.5f);
        return size;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player)
    {
        return Math.max((int) ((getWidth() * getHeight()) * 0.25) + getRNG().nextInt(3), super.getExperiencePoints(player));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && invHandler.isPresent())
            return invHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return isFoodItem(stack);
    }

    public abstract boolean isFoodItem(ItemStack stack);

    // ================================
    //        Entity Animation
    // ================================

    @Override
    public int getAnimationTick()
    {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick)
    {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public void setAnimation(Animation animation)
    {
        if (animation == null)
            animation = NO_ANIMATION;
        setAnimationTick(0);
        this.animation = animation;
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[0];
    }

    @SuppressWarnings("unused")
    public static boolean canFlyerSpawn(EntityType<? extends AbstractDragonEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random)
    {
        return world.getBlockState(pos.down()).getFluidState().isEmpty();
    }
}
