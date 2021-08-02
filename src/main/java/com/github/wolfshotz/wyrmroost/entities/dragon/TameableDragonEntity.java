package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.sound.FlyingSound;
import com.github.wolfshotz.wyrmroost.containers.DragonStaffContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.*;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRSitGoal;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggProperties;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializer;
import com.github.wolfshotz.wyrmroost.items.DragonArmorItem;
import com.github.wolfshotz.wyrmroost.items.DragonEggItem;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffActions;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRKeybind;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.DebugRendering;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.Mafs;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import static net.minecraft.entity.ai.attributes.Attributes.FLYING_SPEED;
import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;


/**
 * Created by com.github.WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class TameableDragonEntity extends TameableEntity implements IAnimatable, INamedContainerProvider
{
    public static final EntitySerializer<TameableDragonEntity> SERIALIZER = EntitySerializer.builder(b -> b
            .track(EntitySerializer.POS.optional(), "HomePos", TameableDragonEntity::getHomePos, (d, v) -> d.setHomePos(v.orElse(null)))
            .track(EntitySerializer.INT, "BreedCount", TameableDragonEntity::getBreedCount, TameableDragonEntity::setBreedCount));

    public static final byte HEAL_PARTICLES_EVENT_ID = 8;

    // Common Data Parameters
    public static final DataParameter<Boolean> GENDER = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> FLYING = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLEEPING = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.INT);
    public static final DataParameter<ItemStack> ARMOR = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<Optional<BlockPos>> HOME_POS = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS); // todo for 1.17: remove optional and make this nullable

    @Deprecated // https://github.com/MinecraftForge/MinecraftForge/issues/7622
    public final LazyOptional<DragonInventory> inventory;
    public final LerpedFloat sleepTimer = LerpedFloat.unit();
    private int sleepCooldown;
    public boolean wingsDown;
    public int breedCount;
    private Animation<?, ?> animation = NO_ANIMATION;
    private int animationTick;

    public TameableDragonEntity(EntityType<? extends TameableDragonEntity> dragon, World level)
    {
        super(dragon, level);

        maxUpStep = 1;

        DragonInventory inv = createInv();
        inventory = LazyOptional.of(inv == null? null : () -> inv);
        lookControl = new LessShitLookController(this);
        if (hasDataParameter(FLYING)) moveControl = new FlyerMoveController(this);
    }

    @Override
    protected PathNavigator createNavigation(World levelIn)
    {
        return new BetterPathNavigator(this);
    }

    @Override
    protected BodyController createBodyControl()
    {
        return new DragonBodyController(this);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new WRSitGoal(this));
    }

    public abstract EntitySerializer<? extends TameableDragonEntity> getSerializer();

    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        if (inventory.isPresent()) nbt.put("Inv", inventory.orElse(null).serializeNBT());
        ((EntitySerializer<TameableDragonEntity>) getSerializer()).serialize(this, nbt);
    }

    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        if (inventory.isPresent()) inventory.orElse(null).deserializeNBT(nbt.getCompound("Inv"));
        ((EntitySerializer<TameableDragonEntity>) getSerializer()).deserialize(this, nbt);
        applyAttributes();
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(HOME_POS, Optional.empty());
    }

    public boolean hasDataParameter(DataParameter<?> param)
    {
        return entityData.itemsById.containsKey(param.getId());
    }

    public int getVariant()
    {
        return hasDataParameter(VARIANT)? entityData.get(VARIANT) : 0;
    }

    public void setVariant(int variant)
    {
        entityData.set(VARIANT, variant);
    }

    /**
     * @return true for male, false for female. anything else is a political abomination and needs to be cancelled.
     */
    public boolean isMale()
    {
        return !hasDataParameter(GENDER) || entityData.get(GENDER);
    }

    public void setGender(boolean sex)
    {
        entityData.set(GENDER, sex);
    }

    public boolean isSleeping()
    {
        return hasDataParameter(SLEEPING) && entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleep)
    {
        if (isSleeping() == sleep) return;

        entityData.set(SLEEPING, sleep);
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
        return hasDataParameter(FLYING) && entityData.get(FLYING);
    }

    public void setFlying(boolean fly)
    {
        if (isFlying() == fly) return;
        entityData.set(FLYING, fly);
        Path prev = navigation.getPath();
        if (fly)
        {
            // make sure NOT to switch the navigator if liftoff fails
            if (liftOff()) navigation = new FlyerPathNavigator(this);
            else return;
        }
        else navigation = new BetterPathNavigator(this);
        navigation.moveTo(prev, 1);
    }

    public boolean hasArmor()
    {
        return hasDataParameter(ARMOR) && entityData.get(ARMOR).getItem() instanceof DragonArmorItem;
    }

    public ItemStack getArmorStack()
    {
        return hasDataParameter(ARMOR)? entityData.get(ARMOR) : ItemStack.EMPTY;
    }

    public void setArmor(@Nullable ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof DragonArmorItem)) stack = ItemStack.EMPTY;
        entityData.set(ARMOR, stack);
    }

    @Override
    public void setInSittingPose(boolean flag)
    {
        super.setInSittingPose(flag);
        if (flag) clearAI();
    }

    public DragonInventory getInventory()
    {
        return inventory.orElseThrow(() -> new NoSuchElementException("This boi doesn't have an inventory wtf are u doing"));
    }

    public DragonInventory createInv()
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
    public void aiStep()
    {
        super.aiStep();

        if (isEffectiveAi())
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
            if (target != null && (!target.isAlive() || !canAttack(target) || !wantsToAttack(target, getOwner())))
                setTarget(null);
        }
        else
        {
            doSpecialEffects();
        }
    }

    @Override
    public void rideTick()
    {
        super.rideTick();

        Entity entity = getVehicle();

        if (entity == null || !entity.isAlive())
        {
            stopRiding();
            return;
        }

        setDeltaMovement(Vector3d.ZERO);
        clearAI();

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            int index = player.getPassengers().indexOf(this);
            if ((player.isShiftKeyDown() && !player.abilities.flying) || isInWater() || index > 2)
            {
                stopRiding();
                setOrderedToSit(false);
                return;
            }

            xRot = player.xRot / 2;
            yHeadRot = yBodyRot = yRotO = yRot = player.yRot;
            setRotation(player.yHeadRot, player.xRot);

            Vector3d vec3d = getRidingPosOffset(index);
            if (player.isFallFlying())
            {
                if (!canFly())
                {
                    stopRiding();
                    return;
                }

                vec3d = vec3d.scale(1.5);
                setFlying(true);
            }
            Vector3d pos = Mafs.getYawVec(player.yBodyRot, vec3d.x, vec3d.z).add(player.getX(), player.getY() + vec3d.y, player.getZ());
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
    public void positionRider(Entity passenger)
    {
        Vector3d offset = getPassengerPosOffset(passenger, getPassengers().indexOf(passenger));
        Vector3d pos = Mafs.getYawVec(yBodyRot, offset.x, offset.z).add(getX(), getY() + offset.y + passenger.getMyRidingOffset(), getZ());
        passenger.setPos(pos.x, pos.y, pos.z);
    }

    public Vector3d getPassengerPosOffset(Entity entity, int index)
    {
        return new Vector3d(0, getPassengersRidingOffset(), 0);
    }

    // Ok so some basic notes here:
    // if the action result is a SUCCESS, the player swings its arm.
    // however, itll send that arm swing twice if we aren't careful.
    // essentially, returning SUCCESS on server will send a swing arm packet to notify the client to animate the arm swing
    // client tho, it will just animate it.
    // so if we aren't careful, both will happen. So its important to do the following for common execution:
    // ActionResultType.sidedSuccess(level.isClientSide);
    // essentially, if the provided boolean is true, it will return SUCCESS, else CONSUME.
    // so since the level is client, it will be SUCCESS on client and CONSUME on server.
    // That way, the server never sends the arm swing packet.
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        final ActionResultType SUCCESS = ActionResultType.sidedSuccess(level.isClientSide);

        if (isOwnedBy(player) && player.isShiftKeyDown() && !isFlying())
        {
            setOrderedToSit(!isOrderedToSit());
            return SUCCESS;
        }

        if (isTame())
        {
            if (isFoodItem(stack))
            {
                boolean flag = getHealth() < getMaxHealth();
                if (isBaby())
                {
                    if (!level.isClientSide) ageUp((int) ((-getAge() / 20) * 0.1F), true);
                    flag = true;
                }

                if (flag)
                {
                    eat(stack);
                    return SUCCESS;
                }
            }

            if (isFood(stack) && getAge() == 0)
            {
                if (!level.isClientSide && !isInLove())
                {
                    eat(stack);
                    setInLove(player);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.CONSUME;
            }
        }

        if (canAddPassenger(player) && !player.isShiftKeyDown())
        {
            if (!level.isClientSide) player.startRiding(this);
            return SUCCESS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        ActionResultType result = stack.interactLivingEntity(player, this, hand);
        if (!result.consumesAction()) result = playerInteraction(player, hand, stack);
        if (result.consumesAction()) setSleeping(false);
        return result;
    }

    @Override
    public void travel(Vector3d vec3d)
    {
        float speed = getTravelSpeed();

        if (canBeControlledByRider()) // Were being controlled; override ai movement
        {
            LivingEntity entity = (LivingEntity) getControllingPassenger();
            double moveX = entity.xxa * 0.5;
            double moveY = vec3d.y;
            double moveZ = entity.zza;

            // rotate head to match driver. yaw is handled relative to this.
            yHeadRot = entity.yHeadRot;
            xRot = entity.xRot * 0.65f;

            if (isControlledByLocalInstance())
            {
                if (isFlying())
                {
                    moveX = vec3d.x;
                    moveZ = moveZ > 0? moveZ : 0;
                    if (ClientEvents.keybindFlight)
                        moveY = ClientEvents.getClient().options.keyJump.isDown()? 1f : WRKeybind.FLIGHT_DESCENT.isDown()? -1f : 0;
                    else if (moveZ > 0) moveY = -entity.xRot * (Math.PI / 180);
                }
                else
                {
                    speed *= 0.225f;
                    if (entity.jumping && canFly()) setFlying(true);
                }

                vec3d = new Vector3d(moveX, moveY, moveZ);
                setSpeed(speed);
            }
            else if (entity instanceof PlayerEntity)
            {
                calculateEntityAnimation(this, true);
                setDeltaMovement(Vector3d.ZERO);
                if (!level.isClientSide && isFlying())
                    ((ServerPlayerEntity) entity).connection.aboveGroundVehicleTickCount = 0;
                return;
            }
        }

        if (isFlying())
        {
            // Move relative to yaw - handled in the move controller or by passenger
            moveRelative(speed, vec3d);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9f));
            calculateEntityAnimation(this, true);
        }
        else super.travel(vec3d);
    }

    @Override
    public void calculateEntityAnimation(LivingEntity what, boolean includeY)
    {
        if (isFlying())
        {
            animationSpeedOld = animationSpeed;
            double x = getX() - xo;
            double y = 0;
            double z = getZ() - zo;
            float speed = MathHelper.cos(MathHelper.sqrt(x * x + y * y + z * z) * 4f);
            if (speed > 1f) speed = 1f;
            if (getMoveControl().getWantedY() < getY()) speed = 0f;

            animationSpeed += (speed - animationSpeed) * 0.4F;
            animationPosition += animationSpeed;
        }
        else super.calculateEntityAnimation(what, includeY);
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
    public void onSyncedDataUpdated(DataParameter<?> key)
    {
        if (key.equals(SLEEPING) || key.equals(FLYING) || key.equals(TameableEntity.DATA_FLAGS_ID))
        {
            refreshDimensions();
            if (level.isClientSide && key == FLYING && isFlying() && canBeControlledByRider()) FlyingSound.play(this);
        }
        else if (key == ARMOR)
        {
            if (!level.isClientSide)
            {
                ModifiableAttributeInstance attribute = getAttribute(Attributes.ARMOR);
                if (attribute.getModifier(DragonArmorItem.ARMOR_UUID) != null)
                    attribute.removeModifier(DragonArmorItem.ARMOR_UUID);
                if (hasArmor())
                {
                    attribute.addTransientModifier(new AttributeModifier(DragonArmorItem.ARMOR_UUID, "Armor Modifier", DragonArmorItem.getDmgReduction(getArmorStack()), AttributeModifier.Operation.ADDITION));
                    playSound(SoundEvents.ARMOR_EQUIP_DIAMOND, 1, 1, true);
                }
            }
        }
        else super.onSyncedDataUpdated(key);

    }

    @Override
    public void handleEntityEvent(byte id)
    {
        if (id == HEAL_PARTICLES_EVENT_ID)
        {
            for (int i = 0; i < getBbWidth() * getBbHeight(); ++i)
            {
                double x = getX() + Mafs.nextDouble(getRandom()) * getBbWidth() + 0.4d;
                double y = getY() + getRandom().nextDouble() * getBbHeight();
                double z = getZ() + Mafs.nextDouble(getRandom()) * getBbWidth() + 0.4d;
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
        else super.handleEntityEvent(id);
    }

    public ItemStack getStackInSlot(int slot)
    {
        return inventory.map(i -> i.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    /**
     * It is VERY important to be careful when using this.
     * It is VERY sidedness sensitive. If not done correctly, it can result in the loss of items! <P>
     * {@code if (!level.isClient) setStackInSlot(...)}
     */
    public void setStackInSlot(int slot, ItemStack stack)
    {
        inventory.ifPresent(i -> i.setStackInSlot(slot, stack));
    }

    public void attackInBox(AxisAlignedBB box)
    {
        attackInBox(box, 0);
    }

    public void attackInBox(AxisAlignedBB box, int disabledShieldTime)
    {
        List<LivingEntity> attackables = level.getEntitiesOfClass(LivingEntity.class, box, entity -> entity != this && !hasPassenger(entity) && wantsToAttack(entity, getOwner()));
        if (WRConfig.DEBUG_MODE.get() && level.isClientSide) DebugRendering.box(box, 0x99ff0000, Integer.MAX_VALUE);
        for (LivingEntity attacking : attackables)
        {
            doHurtTarget(attacking);
            if (disabledShieldTime > 0 && attacking instanceof PlayerEntity)
            {
                PlayerEntity player = ((PlayerEntity) attacking);
                if (player.isUsingItem() && player.getUseItem().isShield(player))
                {
                    player.getCooldowns().addCooldown(Items.SHIELD, disabledShieldTime);
                    player.stopUsingItem();
                    level.broadcastEntityEvent(player, (byte) 9);
                }
            }
        }
    }

    public AxisAlignedBB getOffsetBox(float offset)
    {
        return getBoundingBox().move(Vector3d.directionFromRotation(0, yBodyRot).scale(offset));
    }

    @Override // Dont damage owners other pets!
    public boolean doHurtTarget(Entity entity)
    {
        return !isAlliedTo(entity) && super.doHurtTarget(entity);
    }

    @Override // We shouldnt be targetting pets...
    public boolean wantsToAttack(LivingEntity target, @Nullable LivingEntity owner)
    {
        return !isAlliedTo(target);
    }

    @Override
    public boolean canAttack(LivingEntity target)
    {
        return !isBaby() && !canBeControlledByRider() && super.canAttack(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        if (isImmuneToArrows() && source.getDirectEntity() != null)
        {
            EntityType<?> attackSource = source.getDirectEntity().getType();
            if (attackSource == EntityType.ARROW) return false;
            else if (attackSource == WREntities.GEODE_TIPPED_ARROW.get()) amount *= 0.5f;
        }

        setSleeping(false);
        setOrderedToSit(false);
        return super.hurt(source, amount);
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
        if (level.noCollision(this, getBoundingBox().move(pos.subtract(blockPosition()))))
        {
            moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yRot, xRot);
            return true;
        }
        return false;
    }

    @Override
    public BlockPos getRestrictCenter()
    {
        return getHomePos().orElse(BlockPos.ZERO);
    }

    public Optional<BlockPos> getHomePos()
    {
        return entityData.get(HOME_POS);
    }

    public void setHomePos(@Nullable BlockPos pos)
    {
        entityData.set(HOME_POS, Optional.ofNullable(pos));
    }

    public void clearHome()
    {
        setHomePos(null);
    }

    @Override
    public boolean hasRestriction()
    {
        return getHomePos().filter(i -> i != BlockPos.ZERO).isPresent();
    }

    @Override
    public float getRestrictRadius()
    {
        return WRConfig.HOME_RADIUS.get() * WRConfig.HOME_RADIUS.get();
    }

    @Override
    public void restrictTo(BlockPos pos, int distance)
    {
        setHomePos(pos);
    }

    @Override
    public boolean isWithinRestriction()
    {
        return isWithinRestriction(blockPosition());
    }

    @Override
    public boolean isWithinRestriction(BlockPos pos)
    {
        Optional<BlockPos> home = getHomePos();
        return home.map(h -> h.distSqr(pos) <= getRestrictRadius()).orElse(true);
    }

    public boolean isAtHome()
    {
        return hasRestriction() && isWithinRestriction();
    }

    @Override
    protected void dropEquipment()
    {
        inventory.ifPresent(i -> i.getContents().forEach(this::spawnAtLocation));
    }

    public void dropStorage()
    {
    }

    public void setRotation(float yaw, float pitch)
    {
        this.yRot = yaw % 360.0F;
        this.xRot = pitch % 360.0F;
    }

    public double getAltitude()
    {
        BlockPos.Mutable pos = blockPosition().mutable();

        // cap to the level void (y = 0)
        while (pos.getY() > 0 && !level.getBlockState(pos.below()).getMaterial().isSolid()) pos.move(0, -1, 0);
        return getY() - pos.getY();
    }

    // overload because... WHY IS `World` A PARAMETER WTF THE FIELD IS LITERALLY PUBLIC
    public void eat(ItemStack stack)
    {
        eat(level, stack);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack eat(World level, ItemStack stack)
    {
        Vector3d mouth = getApproximateMouthPos();

        if (level.isClientSide)
        {
            double width = getBbWidth();
            for (int i = 0; i < Math.max(width * width * 2, 12); ++i)
            {
                Vector3d vec3d1 = new Vector3d(((double) getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) getRandom().nextFloat() - 0.5D) * 0.1D);
                vec3d1 = vec3d1.zRot(-xRot * (Mafs.PI / 180f));
                vec3d1 = vec3d1.yRot(-yRot * (Mafs.PI / 180f));
                level.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), mouth.x + Mafs.nextDouble(getRandom()) * (width * 0.2), mouth.y, mouth.z + Mafs.nextDouble(getRandom()) * (width * 0.2), vec3d1.x, vec3d1.y, vec3d1.z);
            }
            level.playSound(null, getX(), getY(), getZ(), getEatingSound(stack), SoundCategory.NEUTRAL, 1f, 1f + (getRandom().nextFloat() - getRandom().nextFloat()) * 0.4f);
        }
        else
        {
            final float max = getMaxHealth();
            if (getHealth() < max) heal(Math.max((int) max / 5, 4)); // Base healing on max health, minumum 2 hearts.

            Item item = stack.getItem();
            if (item.isEdible())
            {
                for (Pair<EffectInstance, Float> pair : item.getFoodProperties().getEffects())
                    if (!level.isClientSide && pair.getFirst() != null && getRandom().nextFloat() < pair.getSecond())
                        addEffect(new EffectInstance(pair.getFirst()));
            }
            if (item.hasContainerItem(stack))
                spawnAtLocation(item.getContainerItem(stack), (float) (mouth.y - getY()));
            stack.shrink(1);
        }

        return stack;
    }

    public boolean tame(boolean tame, @Nullable PlayerEntity tamer)
    {
        if (getOwner() == tamer) return true;
        if (level.isClientSide) return false;
        if (tame && tamer != null && !ForgeEventFactory.onAnimalTame(this, tamer))
        {
            tame(tamer);
            setHealth(getMaxHealth());
            clearAI();
            level.broadcastEntityEvent(this, (byte) 7); // heart particles
            return true;
        }
        else level.broadcastEntityEvent(this, (byte) 6); // black particles

        return false;
    }

    @Override
    public void heal(float healAmount)
    {
        super.heal(healAmount);
        level.broadcastEntityEvent(this, HEAL_PARTICLES_EVENT_ID);
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
    public boolean canMate(AnimalEntity mate)
    {
        if (!(mate instanceof TameableDragonEntity)) return false;
        TameableDragonEntity dragon = (TameableDragonEntity) mate;
        if (isInSittingPose() || dragon.isInSittingPose()) return false;
        if (hasDataParameter(GENDER) && isMale() == dragon.isMale()) return false;
        return super.canMate(mate);
    }

    @Override
    public void setBaby(boolean baby)
    {
        setAge(baby? DragonEggProperties.get(getType()).getGrowthTime() : 0);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld level, AgeableEntity mate)
    {
        return (AgeableEntity) getType().create(level);
    }

    @Override
    public void spawnChildFromBreeding(ServerWorld level, AnimalEntity mate)
    {
        final BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(this, mate, null);
        if (MinecraftForge.EVENT_BUS.post(event)) return; // cancelled

        final AgeableEntity child = event.getChild();
        if (child == null)
        {
            ItemStack eggStack = DragonEggItem.getStack(getType());
            ItemEntity eggItem = new ItemEntity(level, getX(), getY(), getZ(), eggStack);
            eggItem.setDeltaMovement(0, getBbHeight() / 3, 0);
            level.addFreshEntity(eggItem);
        }
        else
        {
            child.setBaby(true);
            child.moveTo(getX(), getY(), getZ(), 0, 0);
            level.addFreshEntityWithPassengers(child);
        }

        breedCount++;
        ((TameableDragonEntity) mate).breedCount++;

        ServerPlayerEntity serverPlayer = getLoveCause();

        if (serverPlayer == null && mate.getLoveCause() != null)
            serverPlayer = mate.getLoveCause();

        if (serverPlayer != null)
        {
            serverPlayer.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this, mate, child);
        }

        setAge(6000);
        mate.setAge(6000);
        resetLove();
        mate.resetLove();
        level.broadcastEntityEvent(this, (byte) 18);
        if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
            level.addFreshEntity(new ExperienceOrbEntity(level, getX(), getY(), getZ(), getRandom().nextInt(7) + 1));
    }

    public int getBreedCount()
    {
        return breedCount;
    }

    public void setBreedCount(int i)
    {
        this.breedCount = i;
    }

    @Override
    protected void addPassenger(Entity passenger)
    {
        super.addPassenger(passenger);
        if (getControllingPassenger() == passenger && isOwnedBy((LivingEntity) passenger))
        {
            clearAI();
            setOrderedToSit(false);
            clearHome();
            if (isLeashed()) dropLeash(true, true);
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
        return passenger instanceof PlayerEntity? (PlayerEntity) passenger : null;
    }

    public void clearAI()
    {
        jumping = false;
        navigation.stop();
        setTarget(null);
        setSpeed(0);
        setYya(0);
    }

    public boolean isIdling()
    {
        return getNavigation().isDone() && getTarget() == null && !isVehicle() && !isInWaterOrBubble() && !isFlying();
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
        double dist = (getBbWidth() / 2) + 0.75d;
        return position.add(calculateViewVector(xRot, yHeadRot).scale(dist));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(SpawnEggItem.byId(getType()));
    }

    public List<LivingEntity> getEntitiesNearby(double radius, Predicate<LivingEntity> filter)
    {
        return level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius), filter.and(e -> e != this));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isAlliedTo(Entity entity)
    {
        if (entity == this) return true;
        if (entity instanceof LivingEntity && isOwnedBy(((LivingEntity) entity))) return true;
        if (entity instanceof TameableEntity && getOwner() != null && getOwner().equals(((TameableEntity) entity).getOwner()))
            return true;
        return entity.isAlliedTo(getTeam());
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
        pitch *= getVoicePitch();

        if (local) level.playLocalSound(getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch, false);
        else level.playSound(null, getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch);
    }

    @Override
    public void playAmbientSound()
    {
        if (!isSleeping()) super.playAmbientSound();
    }

    public void flapWings()
    {
        playSound(WRSounds.WING_FLAP.get(), 3, 1, false);
        setDeltaMovement(getDeltaMovement().add(0, 1.285, 0));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        if (isRiding() && source == DamageSource.IN_WALL) return true;
        if (isImmuneToArrows() && source == DamageSource.CACTUS) return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld level, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT dataTag)
    {
        if (hasDataParameter(GENDER)) setGender(getRandom().nextBoolean());
        if (hasDataParameter(VARIANT)) setVariant(determineVariant());

        applyAttributes();
        setHealth(getMaxHealth());

        return super.finalizeSpawn(level, difficulty, reason, data, dataTag);
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
    public boolean isPickable()
    {
        return super.isPickable() && !isRiding();
    }

    @Override
    public boolean canBeControlledByRider() // Only OWNERS can control their pets
    {
        Entity entity = getControllingPassenger();
        return entity instanceof PlayerEntity && isOwnedBy(((PlayerEntity) entity));
    }

    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty()? null : passengers.get(0);
    }

    @Override
    protected boolean canAddPassenger(Entity entityIn)
    {
        return false;
    }

    @Override
    public boolean isSuppressingSlidingDownLadder()
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
     * Sort of misleading name. if this is true, then {@link MobEntity#serverAiStep()} ()} is not ticked:
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
    protected float getJumpPower()
    {
        if (canFly()) return (getBbHeight() * getBlockJumpFactor()) * 0.6f;
        else return super.getJumpPower();
    }

    public boolean liftOff()
    {
        if (!canFly()) return false;
        if (!onGround) return true; // We can't lift off the ground in the air...

        int heightDiff = level.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) getX(), (int) getZ()) - (int) getY();
        if (heightDiff > 0 && heightDiff <= getFlightThreshold())
            return false; // position has too low of a ceiling, can't fly here.

        setOrderedToSit(false);
        setSleeping(false);
        jumpFromGround();
        return true;
    }

    @Override // Disable fall calculations if we can fly (fall damage etc.)
    public boolean causeFallDamage(float distance, float damageMultiplier)
    {
        if (canFly()) return false;
        return super.causeFallDamage(distance - (int) (getBbHeight() * 0.8), damageMultiplier);
    }

    public int getFlightThreshold()
    {
        return (int) getBbHeight();
    }

    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
    }

    @Override
    public void dropLeash(boolean sendPacket, boolean dropLead)
    {
        super.dropLeash(sendPacket, dropLead);
        clearHome();
    }

    public boolean isImmuneToArrows()
    {
        return false;
    }

    public void applyStaffInfo(DragonStaffContainer container)
    {
        container.addStaffActions(StaffActions.HOME, StaffActions.SIT)
                .addTooltip(getName())
                .addTooltip(new StringTextComponent(Character.toString('\u2764'))
                        .withStyle(TextFormatting.RED)
                        .append(new StringTextComponent(String.format(" %s / %s", (int) (getHealth() / 2), (int) getMaxHealth() / 2))
                                .withStyle(TextFormatting.WHITE)));

        if (hasDataParameter(GENDER))
        {
            boolean isMale = isMale();
            container.addTooltip(new TranslationTextComponent("entity.wyrmroost.dragons.gender." + (isMale? "male" : "female"))
                    .withStyle(isMale? TextFormatting.DARK_AQUA : TextFormatting.RED));
        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return super.getDisplayName();
    }

    @Override
    public Container createMenu(int id, PlayerInventory playersInv, PlayerEntity player)
    {
        return new DragonStaffContainer(id, playersInv, this);
    }

    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scale(getScale());
        if (isInSittingPose() || isSleeping()) size = size.scale(1, 0.5f);
        return size;
    }

    @Override
    protected int getExperienceReward(PlayerEntity player)
    {
        return Math.max((int) ((getBbWidth() * getBbHeight()) * 0.25) + getRandom().nextInt(3), super.getExperienceReward(player));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventory.isPresent() && !getInventory().isEmpty())
            return inventory.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean isFood(ItemStack stack)
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

    public static boolean canFlyerSpawn(EntityType<? extends TameableDragonEntity> type, IWorld level, SpawnReason reason, BlockPos pos, Random random)
    {
        return level.getBlockState(pos.below()).getFluidState().isEmpty();
    }
}
