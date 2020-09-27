package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.RenderHelper;
import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.client.sounds.FlyingSound;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.DragonBodyController;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.FlyerLookController;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.FlyerMoveController;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.entities.util.animation.IAnimatedEntity;
import WolfShotz.Wyrmroost.items.DragonEggItem;
import WolfShotz.Wyrmroost.items.LazySpawnEggItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.minecraft.entity.SharedMonsterAttributes.FLYING_SPEED;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatedEntity
{
    public static final byte HEAL_PARTICLES_DATA_ID = 8;
    public static final IAttribute PROJECTILE_DAMAGE = new RangedAttribute(null, "generic.projectileDamage", 2d, 0, 2048d);

    // Common Data Parameters
    public static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Optional<BlockPos>> HOME_POS = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

    private final Set<String> immunes = new HashSet<>();
    private final Set<EntityDataEntry<?>> dataEntries = new HashSet<>();
    public final Optional<DragonInvHandler> invHandler;
    public final TickFloat sleepTimer = new TickFloat().setLimit(0, 1);
    public boolean wingsDown;
    public int sleepCooldown;
    public int breedCount;
    private Animation animation = NO_ANIMATION;
    private int animationTick;

    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        invHandler = Optional.ofNullable(createInv());
        stepHeight = 1;

        if (hasDataEntry(FLYING))
        {
            lookController = new FlyerLookController(this);
            moveController = new FlyerMoveController(this);
        }

        registerDataEntry("HomePos", EntityDataEntry.BLOCK_POS.optional(), HOME_POS, Optional.empty());
        registerDataEntry("BreedCount", EntityDataEntry.INTEGER, () -> breedCount, i -> breedCount = i);
        invHandler.ifPresent(i -> registerDataEntry("Inv", EntityDataEntry.COMPOUND, i::serializeNBT, i::deserializeNBT));
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, sitGoal = new SitGoal(this));
    }

    @Override
    protected BodyController createBodyController() { return new DragonBodyController(this); }

    // ================================
    //           Entity Data
    // ================================

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

    public boolean hasDataEntry(DataParameter<?> param) { return dataManager.entries.containsKey(param.getId()); }

    public int getVariant() { return hasDataEntry(VARIANT)? dataManager.get(VARIANT) : 0; }

    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }

    public boolean isMale() { return hasDataEntry(GENDER)? dataManager.get(GENDER) : true; }

    public void setGender(boolean sex) { dataManager.set(GENDER, sex); }

    public boolean isSleeping() { return hasDataEntry(SLEEPING)? dataManager.get(SLEEPING) : false; }

    public void setSleeping(boolean sleep)
    {
        if (isSleeping() == sleep) return;

        dataManager.set(SLEEPING, sleep);
        if (!world.isRemote)
        {
            if (sleep) clearAI();
            else this.sleepCooldown = 350;
        }
    }

    public boolean isFlying() { return hasDataEntry(FLYING)? dataManager.get(FLYING) : false; }

    public void setFlying(boolean fly)
    {
        if (isFlying() == fly) return;
        dataManager.set(FLYING, fly);
        if (fly)
        {
            // make sure NOT to switch the navigator if liftoff fails
            if (liftOff()) navigator = new FlyingPathNavigator(this, world);
        }
        else navigator = new GroundPathNavigator(this, world);
    }

    @Override
    public void setSitting(boolean sitting)
    {
        setSleeping(false);
        if (!world.isRemote)
        {
            sitGoal.setSitting(sitting);
            if (sitting) clearAI();
        }
        super.setSitting(sitting);
    }

    public DragonInvHandler getInvHandler()
    {
        return invHandler.orElseThrow(() -> new NoSuchElementException("This boi doesn't have an inventory wtf are u doing"));
    }

    public DragonInvHandler createInv() { return null; }

    // ================================

    @Override
    public void tick()
    {
        super.tick();

        if (getAnimation() != NO_ANIMATION)
        {
            ++animationTick;
            if (animationTick >= animation.getDuration()) setAnimation(NO_ANIMATION);
        }
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

            if (!isAIDisabled() && hasDataEntry(SLEEPING)) handleSleep();

            if (isSleeping() && getHomePos().isPresent() && isWithinHomeDistanceCurrentPosition() && getRNG().nextInt(200) == 0)
                heal(1);
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

        setMotion(Vec3d.ZERO);
        clearAI();

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            int index = player.getPassengers().indexOf(this);
            if ((player.isSneaking() && !player.abilities.isFlying) || isInWater() || index > 2)
            {
                stopRiding();
                return;
            }

            prevRotationPitch = rotationPitch = player.rotationPitch / 2;
            rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
            setRotation(player.rotationYawHead, rotationPitch);

            Vec3d vec3d = getRidingPosOffset(index);
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
            Vec3d pos = Mafs.getYawVec(player.renderYawOffset, vec3d.x, vec3d.z).add(player.getPosX(), player.getPosY() + vec3d.y, player.getPosZ());
            setPosition(pos.x, pos.y, pos.z);
        }
    }

    public Vec3d getRidingPosOffset(int passengerIndex)
    {
        double x = getWidth() * 0.5d + getRidingEntity().getWidth() * 0.5d;
        switch (passengerIndex)
        {
            default:
            case 0:
                return new Vec3d(0, 1.81, 0);
            case 1:
                return new Vec3d(x, 1.38d, 0);
            case 2:
                return new Vec3d(-x, 1.38d, 0);
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
            Vec3d offset = getPassengerPosOffset(passenger, getPassengers().indexOf(passenger));
            Vec3d pos = Mafs.getYawVec(renderYawOffset, offset.x, offset.z).add(getPosX(), getPosY() + offset.y + passenger.getYOffset(), getPosZ());
            passenger.setPosition(pos.x, pos.y, pos.z);
        }
    }

    public Vec3d getPassengerPosOffset(Entity entity, int index) { return new Vec3d(0, getMountedYOffset(), 0); }

    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (isOwner(player) && player.isSneaking() && !isFlying())
        {
            setSitting(!isSitting());
            return true;
        }

        if (isTamed())
        {
            if (isBreedingItem(stack) && getGrowingAge() == 0)
            {
                if (!world.isRemote && canBreed())
                {
                    eat(stack);
                    setInLove(player);
                }
                return true;
            }

            if (isFoodItem(stack))
            {
                boolean flag = getHealth() < getMaxHealth();
                if (isChild())
                {
                    ageUp((int) ((-getGrowingAge() / 20) * 0.1F), true);
                    flag = true;
                }

                if (flag)
                {
                    eat(stack);
                    return true;
                }
            }
        }

        if (canBeRidden(player))
        {
            if (!world.isRemote) player.startRiding(this);
            return true;
        }

        return false;
    }

    // Override to make processInteract way less annoying
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.interactWithEntity(player, this, hand)) return true;
        if (playerInteraction(player, hand, stack))
        {
            setSleeping(false);
            return true;
        }
        return false;
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        float speed = getTravelSpeed();

        if (canPassengerSteer()) // Were being controlled
        {
            LivingEntity entity = (LivingEntity) getControllingPassenger();
            double moveY = vec3d.y;
            double moveX = entity.moveStrafing;
            double moveZ = entity.moveForward;

            // rotate head to match driver. rotationYaw is handled relative to this.
            rotationYawHead = entity.rotationYawHead;
            rotationPitch = entity.rotationPitch * 0.5f;

            if (isFlying())
            {
                if (entity.moveForward != 0) moveY = entity.getLookVec().y * speed * 18;
                moveX = vec3d.x;
            }
            else if (entity.isJumping)
            {
                if (canFly()) setFlying(true);
                else jumpController.setJumping();
            }

            setAIMoveSpeed(speed);
            vec3d = new Vec3d(moveX, moveY, moveZ);
        }

        if (isFlying())
        {
            // Move relative to rotationYaw
            moveRelative(speed, vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.88f));

            Vec3d motion = getMotion();
            if (motion.length() < 0.04f) // Not Moving, just hover
                setMotion(motion.add(0, Math.cos(ticksExisted * 0.1f) * 0.02f, 0));

            float limbSpeed = 0.4f;
            float amount = 1f;
            if (getPosY() - prevPosY < -0.2f)
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

    protected float getTravelSpeed()
    {
        return isFlying()? (float) getAttribute(FLYING_SPEED).getValue()
                : (float) getAttribute(MOVEMENT_SPEED).getValue() * 0.225f;
    }

    public boolean shouldFly() { return canFly() && getAltitude() > getFlightThreshold(); }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if (key == SLEEPING || key == FLYING || key == TAMED) recalculateSize();
        if (world.isRemote && key == FLYING && isFlying())
        {
            ClientEvents.getClient().getSoundHandler().play(new FlyingSound(this));
        }
    }

    @Override
    public void handleStatusUpdate(byte id)
    {
        switch (id)
        {
            default:
                super.handleStatusUpdate(id);
                break;
            case HEAL_PARTICLES_DATA_ID:
                for (int i = 0; i < getWidth() * getHeight(); ++i)
                {
                    double x = getPosX() + Mafs.nextDouble(getRNG()) * getWidth() + 0.4d;
                    double y = getPosY() + getRNG().nextDouble() * getHeight();
                    double z = getPosZ() + Mafs.nextDouble(getRNG()) * getWidth() + 0.4d;
                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
                }
                break;
        }
    }

    public ItemStack getStackInSlot(int slot)
    {
        return invHandler.map(i -> i.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    /**
     * It is VERY important to be careful when using this.
     * It is VERY sidedness sensitive. If not done correctly, it can result in the loss of items! <P>
     * {@code if (!world.isReomote) setStackInSlot(...)}
     *
     * @param slot
     * @param stack
     */
    public void setStackInSlot(int slot, ItemStack stack) { invHandler.ifPresent(i -> i.setStackInSlot(slot, stack)); }

    public void attackInFront(double radius)
    {
        AxisAlignedBB size = getBoundingBox();
        AxisAlignedBB aabb = size.offset(Mafs.getYawVec(renderYawOffset, 0, size.getXSize())).grow(radius);
        attackInAABB(aabb);
    }

    public void attackInAABB(AxisAlignedBB aabb)
    {
        List<LivingEntity> attackables = world.getEntitiesWithinAABB(LivingEntity.class, aabb, entity -> entity != this && !isPassenger(entity) && shouldAttackEntity(entity, getOwner()));
        if (WRConfig.debugMode && world.isRemote) RenderHelper.DebugBox.INSTANCE.queue(aabb);
        for (LivingEntity attacking : attackables) attackEntityAsMob(attacking);
    }

    @Override // Dont damage owners other pets!
    public boolean attackEntityAsMob(Entity entity)
    {
        if (isOnSameTeam(entity)) return false;
        return super.attackEntityAsMob(entity);
    }

    @Override // We shouldnt be targetting pets...
    public boolean shouldAttackEntity(LivingEntity target, @Nullable LivingEntity owner) { return !isOnSameTeam(target); }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!world.isRemote && isImmuneToArrows())
        {
            EntityType<?> attackSource = source.getImmediateSource().getType();
            if (attackSource == EntityType.ARROW) return false;
            else if (attackSource == WREntities.GEODE_TIPPED_ARROW.get()) amount *= 0.5f;
        }

        setSleeping(false);
        if (getOwner() != null) setSitting(false);
        return super.attackEntityFrom(source, amount);
    }

    public void doSpecialEffects() {}

    public void tryTeleportToOwner()
    {
        if (getOwner() == null) return;
        final int CONSTRAINT = (int) (getWidth() * 0.5) + 1;
        BlockPos pos = getOwner().getPosition();
        BlockPos.Mutable potentialPos = new BlockPos.Mutable();

        for (int x = -CONSTRAINT; x < CONSTRAINT; x++)
            for (int y = -2; y < 2; y++)
                for (int z = -CONSTRAINT; z < CONSTRAINT; z++)
                {
                    potentialPos.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
//                    if (getPosX() - potentialPos.getX() < 2 && getPosZ() - potentialPos.getZ() < 2) continue;
                    if (trySafeTeleport(potentialPos)) return;
                }
    }

    public boolean trySafeTeleport(BlockPos pos)
    {
        if (world.hasNoCollisions(this, getBoundingBox().offset(pos.subtract(getPosition()))))
        {
            setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rotationYaw, rotationPitch);
            return true;
        }
        return false;
    }

    public boolean isOwner(Entity entity) { return entity instanceof LivingEntity && entity == getOwner(); }

    @Override
    public BlockPos getHomePosition() { return getHomePos().orElse(BlockPos.ZERO); }

    public Optional<BlockPos> getHomePos() { return dataManager.get(HOME_POS); }

    public void setHomePos(@Nullable BlockPos pos) { setHomePos(Optional.ofNullable(pos)); }

    public void setHomePos(Optional<BlockPos> pos) { dataManager.set(HOME_POS, pos); }

    public void clearHome() { setHomePos(Optional.empty()); }

    @Override
    public boolean detachHome() { return getHomePos().isPresent(); }

    @Override
    public float getMaximumHomeDistance() { return WRConfig.homeRadius * WRConfig.homeRadius; }

    @Override
    public void setHomePosAndDistance(BlockPos pos, int distance) { setHomePos(pos); }

    @Override
    public boolean isWithinHomeDistanceCurrentPosition() { return isWithinHomeDistanceFromPosition(getPosition()); }

    @Override
    public boolean isWithinHomeDistanceFromPosition(BlockPos pos)
    {
        Optional<BlockPos> home = getHomePos();
        return home.map(h -> h.distanceSq(pos) <= getMaximumHomeDistance()).orElse(true);
    }

    @Override
    protected void dropInventory() { invHandler.ifPresent(i -> i.getStacks().forEach(this::entityDropItem)); }

    public void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    public double getAltitude()
    {
        BlockPos.Mutable pos = new BlockPos.Mutable(getPosition());

        // cap to the world void (y = 0)
        while (pos.getY() > 0 && !world.getBlockState(pos.down()).getMaterial().isSolid()) pos.move(0, -1, 0);
        return getPosY() - pos.getY();
    }

    // overload because... WHY IS `World` A PARAMETER WTF THE FIELD IS LITERALLY PUBLIC
    public void eat(ItemStack stack) { onFoodEaten(world, stack); }

    @Override
    public ItemStack onFoodEaten(World world, ItemStack stack)
    {
        float max = getMaxHealth();
        if (getHealth() < max) heal(Math.max((int) max / 5, 4)); // Base healing on max health, minumum 2 hearts.

        Vec3d mouth = getApproximateMouthPos();

        if (world.isRemote)
        {
            double width = getWidth();
            for (int i = 0; i < Math.max(width * width * 2, 12); ++i)
            {
                Vec3d vec3d1 = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) rand.nextFloat() - 0.5D) * 0.1D);
                vec3d1 = vec3d1.rotatePitch(-rotationPitch * (Mafs.PI / 180f));
                vec3d1 = vec3d1.rotateYaw(-rotationYaw * (Mafs.PI / 180f));
                world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), mouth.x + Mafs.nextDouble(getRNG()) * (width * 0.2), mouth.y, mouth.z + Mafs.nextDouble(getRNG()) * (width * 0.2), vec3d1.x, vec3d1.y, vec3d1.z);
            }
        }

        world.playSound(null, getPosX(), getPosY(), getPosZ(), getEatSound(stack), SoundCategory.NEUTRAL, 1f, 1f + (rand.nextFloat() - rand.nextFloat()) * 0.4f);
        Item item = stack.getItem();
        if (item.isFood())
        {
            for (Pair<EffectInstance, Float> pair : item.getFood().getEffects())
                if (!world.isRemote && pair.getLeft() != null && rand.nextFloat() < pair.getRight())
                    addPotionEffect(new EffectInstance(pair.getLeft()));
        }
        if (item.hasContainerItem(stack)) entityDropItem(item.getContainerItem(stack), (float) mouth.y);
        stack.shrink(1);

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

    @Override
    public boolean canMateWith(AnimalEntity mate)
    {
        if (isSitting() || ((AbstractDragonEntity) mate).isSitting()) return false;
        return super.canMateWith(mate);
    }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 6 : super.getHorizontalFaceSpeed(); }

    public boolean isRiding() { return getRidingEntity() != null; }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        ItemStack eggStack = DragonEggItem.getStack(getType());
        ItemEntity eggItem = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), eggStack);

        eggItem.setMotion(0, getHeight() / 3, 0);
        world.addEntity(eggItem);

        return null;
    }

    public DragonEggProperties getEggProperties() { return DragonEggProperties.get(getType()); }

    public void handleSleep()
    {
        if (isSleeping())
        {
            if (world.isDaytime() && getRNG().nextInt(150) == 0) setSleeping(false);
        }
        else
        {
            if (--sleepCooldown > 0) return;
            if (world.isDaytime()) return;
            if (isTamed() && (!isSitting() || !isWithinHomeDistanceCurrentPosition())) return;
            if (!isIdling()) return;
            if (getRNG().nextInt(300) == 0) setSleeping(true);
        }
    }

    @Override
    protected void addPassenger(Entity passenger)
    {
        super.addPassenger(passenger);
        if (getControllingPassenger() == passenger && isOwner((LivingEntity) passenger))
        {
            clearAI();
            setSitting(false);
            clearHome();
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
        setMoveForward(0);
        setMoveVertical(0);
    }

    public boolean isIdling()
    {
        return getNavigator().noPath()
                && getAttackTarget() == null
                && !isBeingRidden()
                && !isInWaterOrBubbleColumn()
                && !isFlying();
    }

    /**
     * A universal getter for the position of the mouth on the dragon.
     * This is prone to be inaccurate, but can serve good enough for most things
     * If a more accurate position is needed, best to override and adjust accordingly.
     *
     * @return An approximate position of the mouth of the dragon
     */
    public Vec3d getApproximateMouthPos()
    {
        Vec3d position = getEyePosition(1).subtract(0, 0.75d, 0);
        double dist = (getWidth() / 2) + 0.75d;
        return position.add(getVectorForRotation(rotationPitch, rotationYawHead).mul(dist, dist, dist));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) { return new ItemStack(LazySpawnEggItem.getEggFor(getType())); }

    public List<LivingEntity> getEntitiesNearby(double radius, Predicate<LivingEntity> filter)
    {
        return world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(radius), filter.and(e -> e != this));
    }

    public List<LivingEntity> getEntitiesNearby(double radius)
    {
        return getEntitiesNearby(radius, EntityPredicates.notRiding(this)::test);
    }

    @Override
    public boolean isOnSameTeam(Entity entity)
    {
        if (entity == this) return true;
        if (entity instanceof LivingEntity && isOwner(((LivingEntity) entity))) return true;
        if (entity instanceof TameableEntity && ((TameableEntity) entity).getOwner() == getOwner()) return true;
        return entity.isOnScoreboardTeam(getTeam());
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) { playSound(soundIn, volume, pitch, false); }

    public void playSound(SoundEvent sound, float volume, float pitch, boolean local)
    {
        if (isSilent()) return;

        volume *= getSoundVolume();
        pitch *= getSoundPitch();

        if (local) world.playSound(getPosX(), getPosY(), getPosZ(), sound, getSoundCategory(), volume, pitch, false);
        else world.playSound(null, getPosX(), getPosY(), getPosZ(), sound, getSoundCategory(), volume, pitch);
    }

    @Override
    public void playAmbientSound() { if (!isSleeping()) super.playAmbientSound(); }

    public void flapWings() { playSound(WRSounds.WING_FLAP.get(), 3, 1, true); }

    public void setImmune(DamageSource source) { immunes.add(source.getDamageType()); }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        if (isRiding() && source == DamageSource.IN_WALL) return true;
        if (!immunes.isEmpty() && immunes.contains(source.getDamageType())) return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
    {
        if (hasDataEntry(GENDER)) setGender(getRNG().nextBoolean());
        if (hasDataEntry(VARIANT)) setVariant(getVariantForSpawn());

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public int getVariantForSpawn() { return 0; }

    @Override
    public boolean canBeCollidedWith() { return super.canBeCollidedWith() && !isRiding(); }

    @Override
    public boolean canPassengerSteer() // Only OWNERS can control their pets
    {
        Entity entity = getControllingPassenger();
        return entity instanceof LivingEntity && isOwner((LivingEntity) entity);
    }

    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty()? null : passengers.get(0);
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) { return false; }

    @Override
    public boolean isOnLadder() { return false; }

    /**
     * Recieve the keybind message from the current controlling passenger.
     *
     * @param key     shut up
     * @param mods    the modifiers that is pressed when this key was pressed (e.g. shift was held, ctrl etc {@link org.lwjgl.glfw.GLFW})
     * @param pressed true if pressed, false if released. pretty straight forward idk why ur fucking asking.
     */
    public void recievePassengerKeybind(int key, int mods, boolean pressed) {}

    /**
     * Sort of misleading name. if this is true, then {@link MobEntity#updateEntityActionState()} is not ticked:
     * which tl;dr does not update any AI including Goal Selectors, Pathfinding, Moving, etc.
     * Do not perform any AI actions while: Not Sleeping; not being controlled, etc.
     */
    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || isSleeping() || canPassengerSteer(); }

    public boolean canFly() { return hasDataEntry(FLYING) && !isChild() && !getLeashed(); }

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

        setSitting(false);
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

    public int getFlightThreshold() { return (int) getHeight(); }

    /**
     * todo make a forge patch to allow this to actually work
     */
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event) {}

    public boolean isImmuneToArrows() { return false; }

    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.HOME);
        screen.addAction(StaffAction.SIT);

        screen.addTooltip(new StringTextComponent(Character.toString('\u2764'))
                .applyTextStyle(TextFormatting.RED)
                .appendSibling(new StringTextComponent(String.format(" %s / %s", (int) (getHealth() / 2), (int) getMaxHealth() / 2)).applyTextStyle(TextFormatting.WHITE))
                .getFormattedText());
        if (hasDataEntry(GENDER))
        {
            boolean isMale = isMale();
            screen.addTooltip(new TranslationTextComponent("entity.wyrmroost.dragons.gender." + (isMale? "male" : "female"))
                    .applyTextStyle(isMale? TextFormatting.DARK_AQUA : TextFormatting.RED)
                    .getFormattedText());
        }
    }

    public void addContainerInfo(DragonInvContainer container)
    {
        container.makePlayerSlots(container.playerInv, 17, 136);
    }

    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad) {}

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSitting() || isSleeping()) size = size.scale(1, 0.5f);
        return size;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player)
    {
        return Math.max((int) ((getWidth() * getHeight()) * 0.25) + getRNG().nextInt(3), super.getExperiencePoints(player));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return isFoodItem(stack); }

    public boolean isFoodItem(ItemStack stack)
    {
        Item food = stack.getItem();
        for (IItemProvider wrapper : getFoodItems()) if (wrapper.asItem() == food) return true;
        return false;
    }

    public abstract Collection<? extends IItemProvider> getFoodItems();

    // ================================
    //        Entity Animation
    // ================================

    @Override
    public int getAnimationTick() { return animationTick; }

    @Override
    public void setAnimationTick(int tick) { animationTick = tick; }

    @Override
    public Animation getAnimation() { return animation; }

    @Override
    public void setAnimation(Animation animation)
    {
        if (animation == null)
            animation = NO_ANIMATION;
        setAnimationTick(0);
        this.animation = animation;
    }

    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
}
