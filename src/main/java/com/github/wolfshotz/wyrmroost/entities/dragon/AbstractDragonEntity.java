package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.client.sound.FlyingSound;
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

import static net.minecraft.entity.ai.attributes.Attributes.GENERIC_FLYING_SPEED;
import static net.minecraft.entity.ai.attributes.Attributes.GENERIC_MOVEMENT_SPEED;


/**
 * Created by com.github.WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatable
{
    public static final byte HEAL_PARTICLES_DATA_ID = 8;

    // Common Data Parameters
    public static final DataParameter<Boolean> GENDER = EntityDataManager.registerData(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> FLYING = EntityDataManager.registerData(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLEEPING = EntityDataManager.registerData(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> VARIANT = EntityDataManager.registerData(AbstractDragonEntity.class, DataSerializers.INTEGER);
    public static final DataParameter<ItemStack> ARMOR = EntityDataManager.registerData(AbstractDragonEntity.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<Optional<BlockPos>> HOME_POS = EntityDataManager.registerData(AbstractDragonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

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
        super(dragon, level);

        stepHeight = 1;

        DragonInvHandler inv = createInv();
        invHandler = LazyOptional.of(inv == null? null : () -> inv);
        lookControl = new LessShitLookController(this);
        if (hasDataParameter(FLYING)) moveControl = new FlyerMoveController(this);

        registerDataEntry("HomePos", EntityDataEntry.BLOCK_POS.optional(), HOME_POS, Optional.empty());
        registerDataEntry("BreedCount", EntityDataEntry.INTEGER, () -> breedCount, i -> breedCount = i);
        invHandler.ifPresent(i -> registerDataEntry("Inv", EntityDataEntry.COMPOUND, i::serializeNBT, i::deserializeNBT));
    }

    @Override
    protected PathNavigator createNavigation(World worldIn)
    {
        return new BetterPathNavigator(this);
    }

    @Override
    protected BodyController createBodyControl()
    {
        return new DragonBodyController(this);
    }

    @Override
    protected void initGoals()
    {
        goalSelector.add(0, new SwimGoal(this));
        goalSelector.add(1, new WRSitGoal(this));
    }

    @Override
    public void writeCustomDataToTag(CompoundNBT nbt)
    {
        super.writeCustomDataToTag(nbt);
        for (EntityDataEntry<?> entry : dataEntries) entry.write(nbt);
    }

    @Override
    public void readCustomDataFromTag(CompoundNBT nbt)
    {
        super.readCustomDataFromTag(nbt);
        for (EntityDataEntry<?> entry : dataEntries) entry.read(nbt);
        applyAttributes();
    }

    public <T> void registerDataEntry(String key, EntityDataEntry.SerializerType<T> type, Supplier<T> write, Consumer<T> read)
    {
        if (!level.isClientSide) dataEntries.add(new EntityDataEntry<>(key, type, write, read));
    }

    public <T> void registerDataEntry(String key, EntityDataEntry.SerializerType<T> type, DataParameter<T> param, T value)
    {
        dataTracker.startTracking(param, value);
        registerDataEntry(key, type, () -> dataTracker.get(param), v -> dataTracker.set(param, v));
    }

    public boolean hasDataParameter(DataParameter<?> param)
    {
        return dataTracker.entries.containsKey(param.getId());
    }

    public int getVariant()
    {
        return hasDataParameter(VARIANT)? dataTracker.get(VARIANT) : 0;
    }

    public void setVariant(int variant)
    {
        dataTracker.set(VARIANT, variant);
    }

    /**
     * @return true for male, false for female. anything else is a political abomination and needs to be cancelled.
     */
    public boolean isMale()
    {
        return hasDataParameter(GENDER)? dataTracker.get(GENDER) : true;
    }

    public void setGender(boolean sex)
    {
        dataTracker.set(GENDER, sex);
    }

    public boolean isSleeping()
    {
        return hasDataParameter(SLEEPING)? dataTracker.get(SLEEPING) : false;
    }

    public void setSleeping(boolean sleep)
    {
        if (isSleeping() == sleep) return;

        dataTracker.set(SLEEPING, sleep);
        if (!level.isClientSide)
        {
            if (sleep) clearAI();
            else sleepCooldown = 350;
        }
    }

    public boolean shouldSleep()
    {
        if (sleepCooldown > 0) return false;
        if (level.isDay()) return false;
        if (!isIdling()) return false;
        if (isTame())
        {
            if (isAtHome())
            {
                if (defendsHome()) return getHealth() < getMaxHealth() * 0.25;
            }
            else if (!isInSittingPose()) return false;
        }

        return getRandom().nextDouble() < 0.0065;
    }

    public boolean shouldWakeUp()
    {
        return level.isDay() && getRandom().nextDouble() < 0.0065;
    }

    public boolean isFlying()
    {
        return hasDataParameter(FLYING)? dataTracker.get(FLYING) : false;
    }

    public void setFlying(boolean fly)
    {
        if (isFlying() == fly) return;
        dataTracker.set(FLYING, fly);
        if (fly)
        {
            // make sure NOT to switch the navigator if liftoff fails
            if (liftOff()) navigation = new FlyerPathNavigator(this);
        }
        else navigation = new BetterPathNavigator(this);
    }

    public boolean hasArmor()
    {
        return hasDataParameter(ARMOR) && dataTracker.get(ARMOR).getItem() instanceof DragonArmorItem;
    }

    public ItemStack getArmorStack()
    {
        return hasDataParameter(ARMOR)? dataTracker.get(ARMOR) : ItemStack.EMPTY;
    }

    public void setArmor(@Nullable ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof DragonArmorItem)) stack = ItemStack.EMPTY;
        dataTracker.set(ARMOR, stack);
    }

    @Override
    public void setInSittingPose(boolean flag)
    {
        super.setInSittingPose(flag);
        if (flag) clearAI();
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
    public void tickMovement()
    {
        super.tickMovement();

        if (canMoveVoluntarily())
        {
            // uhh so were falling, we should probably start flying
            boolean flying = shouldFly();
            if (flying != isFlying()) setFlying(flying);

            if (sleepCooldown > 0) --sleepCooldown;
            if (isSleeping())
            {
                ((LessShitLookController) getLookControl()).restore();
                if (getHealth() < getMaxHealth() && getRandom().nextDouble() < 0.005) heal(1);

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
            LivingEntity target = getTarget();
            if (target != null && (!target.isAlive() || !canTarget(target) || !canAttackWithOwner(target, getOwner())))
                setTarget(null);
        }
        else
        {
            doSpecialEffects();
        }
    }

    @Override
    public void tickRiding()
    {
        super.tickRiding();

        Entity entity = getVehicle();

        if (entity == null || !entity.isAlive())
        {
            stopRiding();
            return;
        }

        setVelocity(Vector3d.ZERO);
        clearAI();

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            int index = player.getPassengers().indexOf(this);
            if ((player.isSneaking() && !player.abilities.flying) || isInWater() || index > 2)
            {
                stopRiding();
                setSitting(false);
                return;
            }

            pitch = player.pitch / 2;
            headYaw = bodyYaw = prevYaw = yRot = player.yRot;
            setRotation(player.headYaw, player.pitch);

            Vector3d vec3d = getRidingPosOffset(index);
            if (player.isFallFlying())
            {
                if (!canFly())
                {
                    stopRiding();
                    return;
                }

                vec3d = vec3d.multiply(1.5);
                setFlying(true);
            }
            Vector3d pos = Mafs.getYawVec(player.bodyYaw, vec3d.x, vec3d.z).add(player.getX(), player.getY() + vec3d.y, player.getZ());
            setPos(pos.x, pos.y, pos.z);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Vector3d getRidingPosOffset(int passengerIndex)
    {
        double x = getBbWidth() * 0.5d + getVehicle().getBbWidth() * 0.5d;
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

    @Override
    public void updatePassengerPosition(Entity passenger)
    {
        Vector3d offset = getPassengerPosOffset(passenger, getPassengers().indexOf(passenger));
        Vector3d pos = Mafs.getYawVec(bodyYaw, offset.x, offset.z).add(getX(), getY() + offset.y + passenger.getHeightOffset(), getZ());
        passenger.updatePosition(pos.x, pos.y, pos.z);
    }

    public Vector3d getPassengerPosOffset(Entity entity, int index)
    {
        return new Vector3d(0, getMountedHeightOffset(), 0);
    }

    // Ok so some basic notes here:
    // if the action result is a SUCCESS, the player swings its arm.
    // however, itll send that arm swing twice if we aren't careful.
    // essentially, returning SUCCESS on server will send a swing arm packet to notify the client to animate the arm swing
    // client tho, it will just animate it.
    // so if we aren't careful, both will happen. So its important to do the following for common execution:
    // ActionResultType.success(World::isClient)
    // essentially, if the provided boolean is true, it will return SUCCESS, else CONSUME.
    // so since the world is client, it will be SUCCESS on client and CONSUME on server.
    // That way, the server never sends the arm swing packet.
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        final ActionResultType SUCCESS = ActionResultType.success(level.isClientSide);

        if (isOwner(player) && player.isSneaking() && !isFlying())
        {
            setSitting(!isInSittingPose());
            return SUCCESS;
        }

        if (isTame())
        {
            if (isFoodItem(stack))
            {
                boolean flag = getHealth() < getMaxHealth();
                if (isBaby())
                {
                    if (!level.isClientSide) growUp((int) ((-getBreedingAge() / 20) * 0.1F), true);
                    flag = true;
                }

                if (flag)
                {
                    eat(stack);
                    return SUCCESS;
                }
            }

            if (isBreedingItem(stack) && getBreedingAge() == 0)
            {
                if (!level.isClientSide && !isInLove())
                {
                    eat(stack);
                    lovePlayer(player);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
        }

        if (canStartRiding(player) && !player.isSneaking())
        {
            if (!level.isClientSide) player.startRiding(this);
            return SUCCESS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType interactMob(PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getStackInHand(hand);
        ActionResultType result = stack.useOnEntity(player, this, hand);
        if (!result.isAccepted()) result = playerInteraction(player, hand, stack);
        if (result.isAccepted()) setSleeping(false);
        return result;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void travel(Vector3d vec3d)
    {
        float speed = getTravelSpeed();

        if (canBeControlledByRider()) // Were being controlled; override ai movement
        {
            LivingEntity entity = (LivingEntity) getPrimaryPassenger();
            double moveY = vec3d.y;
            double moveX = entity.sidewaysSpeed * 0.5;
            double moveZ = entity.forwardSpeed;

            // rotate head to match driver. yaw is handled relative to this.
            headYaw = entity.headYaw;
            pitch = entity.pitch * 0.5f;

            if (isFlying())
            {
                if (entity.forwardSpeed != 0) moveY = entity.pitch * speed * 18;
                moveX = vec3d.x;

                if (entity instanceof ServerPlayerEntity)
                    ((ServerPlayerEntity) entity).networkHandler.floating = false;
            }
            else
            {
                speed *= 0.35f;
                if (entity.jumping && canFly()) setFlying(true);
            }

            setMovementSpeed(speed);
            vec3d = new Vector3d(moveX, moveY, moveZ);
        }

        if (isFlying())
        {
            // Move relative to yaw - handled in the move controller or by the passenger
            updateVelocity(speed, vec3d);
            move(MoverType.SELF, getDeltaMovement());
            setVelocity(getDeltaMovement().multiply(0.88f));

            // hover in place
            Vector3d motion = getDeltaMovement();
            if (motion.length() < 0.04f) setVelocity(motion.add(0, Math.cos(tickCount * 0.1f) * 0.02f, 0));

            // limb swinging animations
            float limbSpeed = 0.4f;
            float amount = 1f;
            if (getY() - lastRenderY < -0.1f)
            {
                amount = 0f;
                limbSpeed = 0.2f;
            }

            lastLimbDistance = limbDistance;
            limbDistance += (amount - limbDistance) * limbSpeed;
            limbAngle += limbDistance;

            return;
        }

        super.travel(vec3d);
    }

    public float getTravelSpeed()
    {
        //@formatter:off
        return isFlying()? (float) getAttributeValue(GENERIC_FLYING_SPEED)
                         : (float) getAttributeValue(GENERIC_MOVEMENT_SPEED);
        //@formatter:on
    }

    public boolean shouldFly()
    {
        return canFly() && getAltitude() > getFlightThreshold();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onTrackedDataSet(DataParameter<?> key)
    {
        if (key.equals(SLEEPING) || key.equals(FLYING) || key.equals(TAMEABLE_FLAGS))
        {
            calculateDimensions();
            if (level.isClientSide && key == FLYING && isFlying() && canBeControlledByRider()) FlyingSound.play(this);
        }
        else if (key == ARMOR)
        {
            if (!level.isClientSide)
            {
                ModifiableAttributeInstance attribute = getAttributeInstance(Attributes.GENERIC_ARMOR);
                if (attribute.getModifier(DragonArmorItem.ARMOR_UUID) != null)
                    attribute.removeModifier(DragonArmorItem.ARMOR_UUID);
                if (hasArmor())
                {
                    attribute.addTemporaryModifier(new AttributeModifier(DragonArmorItem.ARMOR_UUID, "Armor Modifier", DragonArmorItem.getDmgReduction(getArmorStack()), AttributeModifier.Operation.ADDITION));
                    playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 1, 1, true);
                }
            }
        }
        else super.onTrackedDataSet(key);

    }

    @Override
    public void handleStatus(byte id)
    {
        if (id == HEAL_PARTICLES_DATA_ID)
        {
            for (int i = 0; i < getBbWidth() * getBbHeight(); ++i)
            {
                double x = getX() + Mafs.nextDouble(getRandom()) * getBbWidth() + 0.4d;
                double y = getY() + getRandom().nextDouble() * getBbHeight();
                double z = getZ() + Mafs.nextDouble(getRandom()) * getBbWidth() + 0.4d;
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
        else super.handleStatus(id);
    }

    public ItemStack getStackInSlot(int slot)
    {
        return invHandler.map(i -> i.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    /**
     * It is VERY important to be careful when using this.
     * It is VERY sidedness sensitive. If not done correctly, it can result in the loss of items! <P>
     * {@code if (!world.isClient) setStackInSlot(...)}
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
        List<LivingEntity> attackables = level.getEntitiesOfClass(LivingEntity.class, box, entity -> entity != this && !hasPassenger(entity) && canAttackWithOwner(entity, getOwner()));
        if (WRConfig.debugMode && level.isClientSide) RenderHelper.DebugBox.INSTANCE.queue(box);
        for (LivingEntity attacking : attackables)
        {
            tryAttack(attacking);
            if (disabledShieldTime > 0 && attacking instanceof PlayerEntity)
            {
                PlayerEntity player = ((PlayerEntity) attacking);
                if (player.isUsingItem() && player.getActiveItem().isShield(player))
                {
                    player.getItemCooldownManager().set(Items.SHIELD, disabledShieldTime);
                    player.stopUsingItem();
                    level.sendEntityStatus(player, (byte) 9);
                }
            }
        }
    }

    public AxisAlignedBB getOffsetBox(float offset)
    {
        return getBoundingBox().offset(Vector3d.fromPolar(0, bodyYaw).multiply(offset));
    }

    @Override // Dont damage owners other pets!
    public boolean tryAttack(Entity entity)
    {
        return !isTeammate(entity) && super.tryAttack(entity);
    }

    @Override // We shouldnt be targetting pets...
    public boolean canAttackWithOwner(LivingEntity target, @Nullable LivingEntity owner)
    {
        return !isTeammate(target);
    }

    @Override
    public boolean canTarget(LivingEntity target)
    {
        return !isBaby() && super.canTarget(target);
    }

    @Override
    public boolean damage(DamageSource source, float amount)
    {
        if (isImmuneToArrows() && source.getSource() != null)
        {
            EntityType<?> attackSource = source.getSource().getType();
            if (attackSource == EntityType.ARROW) return false;
            else if (attackSource == WREntities.GEODE_TIPPED_ARROW.get()) amount *= 0.5f;
        }

        setSleeping(false);
        setSitting(false);
        return super.damage(source, amount);
    }

    public void doSpecialEffects()
    {
    }

    public boolean tryTeleportToOwner()
    {
        if (getOwner() == null) return false;
        final int CONSTRAINT = (int) (getBbWidth() * 0.5) + 1;
        BlockPos pos = getOwner().blockPosition();
        BlockPos.Mutable potentialPos = new BlockPos.Mutable();

        for (int x = -CONSTRAINT; x < CONSTRAINT; x++)
            for (int y = 0; y < 4; y++)
                for (int z = -CONSTRAINT; z < CONSTRAINT; z++)
                {
                    potentialPos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (trySafeTeleport(potentialPos)) return true;
                }
        return false;
    }

    public boolean trySafeTeleport(BlockPos pos)
    {
        if (level.isSpaceEmpty(this, getBoundingBox().offset(pos.subtract(blockPosition()))))
        {
            refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yRot, pitch);
            return true;
        }
        return false;
    }

    @Override
    public BlockPos getPositionTarget()
    {
        return getHomePos().orElse(BlockPos.ORIGIN);
    }

    public Optional<BlockPos> getHomePos()
    {
        return dataTracker.get(HOME_POS);
    }

    public void setHomePos(@Nullable BlockPos pos)
    {
        dataTracker.set(HOME_POS, Optional.ofNullable(pos));
    }

    public void clearHome()
    {
        setHomePos(null);
    }

    @Override
    public boolean hasPositionTarget()
    {
        return getHomePos().isPresent();
    }

    @Override
    public float getPositionTargetRange()
    {
        return WRConfig.homeRadius * WRConfig.homeRadius;
    }

    @Override
    public void setPositionTarget(BlockPos pos, int distance)
    {
        setHomePos(pos);
    }

    @Override
    public boolean isInWalkTargetRange()
    {
        return isInWalkTargetRange(blockPosition());
    }

    @Override
    public boolean isInWalkTargetRange(BlockPos pos)
    {
        Optional<BlockPos> home = getHomePos();
        return home.map(h -> h.getSquaredDistance(pos) <= getPositionTargetRange()).orElse(true);
    }

    public boolean isAtHome()
    {
        return hasPositionTarget() && isInWalkTargetRange();
    }

    @Override
    protected void dropInventory()
    {
        invHandler.ifPresent(i -> i.getStacks().forEach(this::dropStack));
    }

    public void setRotation(float yaw, float pitch)
    {
        this.yRot = yRot % 360.0F;
        this.pitch = pitch % 360.0F;
    }

    public double getAltitude()
    {
        BlockPos.Mutable pos = blockPosition().mutable();

        // cap to the world void (y = 0)
        while (pos.getY() > 0 && !level.getBlockState(pos.below()).getMaterial().isSolid()) pos.move(0, -1, 0);
        return getY() - pos.getY();
    }

    // overload because... WHY IS `World` A PARAMETER WTF THE FIELD IS LITERALLY PUBLIC
    public void eat(ItemStack stack)
    {
        eatFood(level, stack);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack eatFood(World world, ItemStack stack)
    {
        Vector3d mouth = getApproximateMouthPos();

        if (level.isClientSide)
        {
            double width = getBbWidth();
            for (int i = 0; i < Math.max(width * width * 2, 12); ++i)
            {
                Vector3d vec3d1 = new Vector3d(((double) getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) getRandom().nextFloat() - 0.5D) * 0.1D);
                vec3d1 = vec3d1.rotateZ(-pitch * (Mafs.PI / 180f));
                vec3d1 = vec3d1.rotateY(-yRot * (Mafs.PI / 180f));
                level.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), mouth.x + Mafs.nextDouble(getRandom()) * (width * 0.2), mouth.y, mouth.z + Mafs.nextDouble(getRandom()) * (width * 0.2), vec3d1.x, vec3d1.y, vec3d1.z);
            }
            level.playSound(null, getX(), getY(), getZ(), getEatSound(stack), SoundCategory.NEUTRAL, 1f, 1f + (getRandom().nextFloat() - getRandom().nextFloat()) * 0.4f);
        }
        else
        {
            final float max = getMaxHealth();
            if (getHealth() < max) heal(Math.max((int) max / 5, 4)); // Base healing on max health, minumum 2 hearts.

            Item item = stack.getItem();
            if (item.isFood())
            {
                for (Pair<EffectInstance, Float> pair : item.getFoodComponent().getStatusEffects())
                    if (!level.isClientSide && pair.getFirst() != null && getRandom().nextFloat() < pair.getSecond())
                        addStatusEffect(new EffectInstance(pair.getFirst()));
            }
            if (item.hasContainerItem(stack))
                dropStack(item.getContainerItem(stack), (float) (mouth.y - getY()));
            stack.decrement(1);
        }

        return stack;
    }

    public boolean tame(boolean tame, @Nullable PlayerEntity tamer)
    {
        if (isTame()) return true;
        if (level.isClientSide) return false;
        if (tame && tamer != null && !ForgeEventFactory.onAnimalTame(this, tamer))
        {
            setOwner(tamer);
            setHealth(getMaxHealth());
            clearAI();
            level.sendEntityStatus(this, (byte) 7); // heart particles
            return true;
        }
        else level.sendEntityStatus(this, (byte) 6); // black particles

        return false;
    }

    @Override
    public void heal(float healAmount)
    {
        super.heal(healAmount);
        level.sendEntityStatus(this, HEAL_PARTICLES_DATA_ID);
    }

    public int getYawRotationSpeed()
    {
        return isFlying()? 6 : 75;
    }

    public boolean isRiding()
    {
        return getVehicle() != null;
    }

    @Override
    public boolean canBreedWith(AnimalEntity mate)
    {
        if (!(mate instanceof AbstractDragonEntity)) return false;
        AbstractDragonEntity dragon = (AbstractDragonEntity) mate;
        if (isInSittingPose() || dragon.isInSittingPose()) return false;
        if (hasDataParameter(GENDER) && isMale() == dragon.isMale()) return false;
        return super.canBreedWith(mate);
    }

    @Override
    public void setBaby(boolean baby)
    {
        setBreedingAge(baby? DragonEggProperties.get(getType()).getGrowthTime() : 0);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld serverWorld, AgeableEntity mate)
    {
        return (AgeableEntity) getType().create(level);
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity mate)
    {
        final BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this, mate, null);
        if (MinecraftForge.EVENT_BUS.post(event)) return; // cancelled

        final AgeableEntity child = event.getChild();
        if (child == null)
        {
            ItemStack eggStack = DragonEggItem.getStack(getType());
            ItemEntity eggItem = new ItemEntity(level, getX(), getY(), getZ(), eggStack);
            eggItem.setVelocity(0, getBbHeight() / 3, 0);
            level.spawnEntity(eggItem);
        }
        else
        {
            child.setBaby(true);
            child.refreshPositionAndAngles(getX(), getY(), getZ(), 0, 0);
            level.spawnEntityAndPassengers(child);
        }

        breedCount++;
        ((AbstractDragonEntity) mate).breedCount++;

        ServerPlayerEntity serverPlayer = getLovingPlayer();

        if (serverPlayer == null && mate.getLovingPlayer() != null)
            serverPlayer = mate.getLovingPlayer();

        if (serverPlayer != null)
        {
            serverPlayer.incrementStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this, mate, child);
        }

        setBreedingAge(6000);
        mate.setBreedingAge(6000);
        resetLoveTicks();
        mate.resetLoveTicks();
        level.sendEntityStatus(this, (byte) 18);
        if (level.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
            level.spawnEntity(new ExperienceOrbEntity(level, getX(), getY(), getZ(), getRandom().nextInt(7) + 1));
    }

    @Override
    protected void addPassenger(Entity passenger)
    {
        super.addPassenger(passenger);
        if (getPrimaryPassenger() == passenger && isOwner((LivingEntity) passenger))
        {
            clearAI();
            setSitting(false);
            clearHome();
            if (isLeashed()) detachLeash(true, true);
        }
    }

    /**
     * Get the player potentially controlling this dragon
     * {@code null} if its not a player or no controller at all.
     */
    @Nullable
    public PlayerEntity getControllingPlayer()
    {
        Entity passenger = getPrimaryPassenger();
        return passenger instanceof PlayerEntity? (PlayerEntity) passenger : null;
    }

    public void clearAI()
    {
        jumping = false;
        navigation.stop();
        setTarget(null);
        setMovementSpeed(0);
        setUpwardSpeed(0);
    }

    public boolean isIdling()
    {
        return getNavigation().isDone() && getTarget() == null && !isVehicle() && !isInsideWaterOrBubbleColumn() && !isFlying();
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
        Vector3d position = getCameraPosVec(1).subtract(0, 0.75d, 0);
        double dist = (getBbWidth() / 2) + 0.75d;
        return position.add(getRotationVector(pitch, headYaw).multiply(dist));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(SpawnEggItem.forEntity(getType()));
    }

    public List<LivingEntity> getEntitiesNearby(double radius, Predicate<LivingEntity> filter)
    {
        return level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius), filter.and(e -> e != this));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isTeammate(Entity entity)
    {
        if (entity == this) return true;
        if (entity instanceof LivingEntity && isOwner(((LivingEntity) entity))) return true;
        if (entity instanceof TameableEntity && getOwner() != null && getOwner().equals(((TameableEntity) entity).getOwner()))
            return true;
        return entity.isTeamPlayer(getScoreboardTeam());
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

        if (local) level.playSound(getX(), getY(), getZ(), sound, getSoundCategory(), volume, pitch, false);
        else level.playSound(null, getX(), getY(), getZ(), sound, getSoundCategory(), volume, pitch);
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
    public ILivingEntityData initialize(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT dataTag)
    {
        if (hasDataParameter(GENDER)) setGender(getRandom().nextBoolean());
        if (hasDataParameter(VARIANT)) setVariant(determineVariant());

        applyAttributes();
        setHealth(getMaxHealth());

        return super.initialize(level, difficulty, reason, data, dataTag);
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
    public boolean collides()
    {
        return super.collides() && !isRiding();
    }

    @Override
    public boolean canBeControlledByRider() // Only OWNERS can control their pets
    {
        Entity entity = getPrimaryPassenger();
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;
            return isOwner(player) && (!level.isClientSide || player.isMainPlayer()); // fix vehicle-desync
        }
        return false;
    }

    @Nullable
    @Override
    public Entity getPrimaryPassenger()
    {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty()? null : passengers.get(0);
    }

    @Override
    protected boolean canStartRiding(Entity entityIn)
    {
        return false;
    }

    @Override
    public boolean isHoldingOntoLadder()
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
     * Sort of misleading name. if this is true, then {@link MobEntity#tickNewAi()} is not ticked:
     * which tl;dr does not update any AI including Goal Selectors, Pathfinding, Moving, etc.
     * Do not perform any AI actions while: Not Sleeping; not being controlled, etc.
     */
    @Override
    public boolean isImmobile()
    {
        return super.isImmobile() || isSleeping() || isRiding();
    }

    public boolean canFly()
    {
        return !isBaby() && !isUnderWater() && !isLeashed();
    }

    /**
     * Get the motion this entity performs when jumping
     */
    @Override
    protected float getJumpVelocity()
    {
        if (canFly()) return (getBbHeight() * getJumpVelocityMultiplier()) * 0.6f;
        else return super.getJumpVelocity();
    }

    public boolean liftOff()
    {
        if (!canFly()) return false;
        if (!onGround) return true; // We can't lift off the ground in the air...

        int heightDiff = level.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) getX(), (int) getZ()) - (int) getY();
        if (heightDiff > 0 && heightDiff <= getFlightThreshold())
            return false; // position has too low of a ceiling, can't fly here.

        setSitting(false);
        setSleeping(false);
        jump();
        return true;
    }

    @Override // Disable fall calculations if we can fly (fall damage etc.)
    public boolean handleFallDamage(float distance, float damageMultiplier)
    {
        if (canFly()) return false;
        return super.handleFallDamage(distance - (int) (getBbHeight() * 0.8), damageMultiplier);
    }

    public int getFlightThreshold()
    {
        return (int) getBbHeight();
    }

    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
    }

    @Override
    public void detachLeash(boolean sendPacket, boolean dropLead)
    {
        super.detachLeash(sendPacket, dropLead);
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
                .formatted(TextFormatting.RED)
                .append(new StringTextComponent(String.format(" %s / %s", (int) (getHealth() / 2), (int) getMaxHealth() / 2)).formatted(TextFormatting.WHITE)));
        if (hasDataParameter(GENDER))
        {
            boolean isMale = isMale();
            screen.addTooltip(new TranslationTextComponent("entity.wyrmroost.dragons.gender." + (isMale? "male" : "female"))
                    .formatted(isMale? TextFormatting.DARK_AQUA : TextFormatting.RED));
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
    public EntitySize getDimensions(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scaled(getScaleFactor());
        if (isInSittingPose() || isSleeping()) size = size.scaled(1, 0.5f);
        return size;
    }

    @Override
    protected int getCurrentExperience(PlayerEntity player)
    {
        return Math.max((int) ((getBbWidth() * getBbHeight()) * 0.25) + getRandom().nextInt(3), super.getCurrentExperience(player));
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
        return level.getBlockState(pos.below()).getFluidState().isEmpty();
    }
}
