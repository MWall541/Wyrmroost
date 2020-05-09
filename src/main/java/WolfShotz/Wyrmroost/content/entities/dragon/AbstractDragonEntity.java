package WolfShotz.Wyrmroost.content.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.client.animation.IAnimatedObject;
import WolfShotz.Wyrmroost.client.render.RenderEvents;
import WolfShotz.Wyrmroost.client.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.content.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.DragonBodyController;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.content.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.NetworkUtils;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.EntityDataEntry;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.QuikMaths;
import com.google.common.collect.Lists;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatedObject
{
    // Common Data Parameters
    public static final DataParameter<Boolean> GENDER = createKey(DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> FLYING = createKey(DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLEEPING = createKey(DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> VARIANT = createKey(DataSerializers.VARINT);
    public static final DataParameter<Optional<BlockPos>> HOME_POS = createKey(DataSerializers.OPTIONAL_BLOCK_POS);

    // Dragon Entity Animations
    public int animationTick;
    public Animation animation = NO_ANIMATION;
    public static Animation SLEEP_ANIMATION;
    public static Animation WAKE_ANIMATION;

    // other delegates
    public final LazyOptional<DragonInvHandler> invHandler;
    public final List<String> immunes = Lists.newArrayList();
    public final List<EntityDataEntry<?>> dataEntries = Lists.newArrayList();
    public DragonEggProperties eggProperties;
    public int shouldFlyThreshold = 3;
    public int sleepCooldown;

    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        setTamed(false);

        invHandler = createInv();
        eggProperties = createEggProperties();
        stepHeight = 1;

        addDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        addDataEntry("HomePos", EntityDataEntry.BLOCK_POS, HOME_POS, Optional.empty());
        invHandler.ifPresent(i -> addDataEntry("Inv", EntityDataEntry.COMPOUND, i::serializeNBT, i::deserializeNBT));
    }

    /**
     * Register the AI Goals
     */
    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, sitGoal = new SitGoal(this));
    }

    /**
     * Needed because the field is private >.>
     */
    @Override
    protected BodyController createBodyController() { return new DragonBodyController(this); }

    // ================================
    //           Entity Data
    // ================================

    /**
     * Register DataManager Entries
     */
    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(FLYING, false);
    }

    /**
     * Sava data to world
     */
    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
        dataEntries.forEach(e -> e.write(nbt));
    }

    /**
     * Load data from world
     */
    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
        dataEntries.forEach(e -> e.read(nbt));
    }

    /**
     * Add a data entry helper
     */
    public <T> void addDataEntry(String key, EntityDataEntry.SerializerType<T> type, Supplier<T> write, Consumer<T> read)
    {
        dataEntries.add(new EntityDataEntry<>(key, type, write, read));
    }

    /**
     * Add a data entry synced to clients using the data manager
     */
    public <T> void addDataEntry(String key, EntityDataEntry.SerializerType<T> type, DataParameter<T> param, T value)
    {
        dataManager.register(param, value);
        addDataEntry(key, type, () -> dataManager.get(param), v -> dataManager.set(param, v));
    }

    /**
     * Add a dragon variant data entry.
     *
     * @param variants   the amount of variants this dragon has
     * @param hasSpecial can be "special"
     */
    public void addVariantData(int variants, boolean hasSpecial)
    {
        int chance = getSpecialChances();
        if (hasSpecial && chance != 0 && getRNG().nextInt(chance) == 0) variants = -1;
        else if (variants != 0) variants = getRNG().nextInt(variants);
        addDataEntry("Variant", EntityDataEntry.INT, VARIANT, variants);
    }

    /**
     * Get the variant of the dragon (if it has them)
     */
    public int getVariant()
    {
        try
        {
            return dataManager.get(VARIANT);
        }
        catch (NullPointerException ignore)
        {
            return 0;
        }
    }

    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }

    /**
     * Whether or not this dragonEntity is albino. true == isSpecial, false == is not
     */
    public boolean isSpecial() { return getVariant() == -1; }

    public void setSpecial() { setVariant(-1); }

    public int getSpecialChances() { return rand.nextInt(400) + 100; }

    /**
     * Gets the Gender of the dragonEntity. <P>
     * true = Male | false = Female. Anything else is an abomination.
     */
    public boolean isMale()
    {
        try { return dataManager.get(GENDER); }
        catch (NullPointerException ignore) { return true; }
    }

    public void setGender(boolean sex) { dataManager.set(GENDER, sex); }

    /**
     * Whether or not the dragon is flying
     */
    public boolean isFlying() { return dataManager.get(FLYING); }

    public void setFlying(boolean fly)
    {
        if (isFlying() == fly) return;
//        getNavigator().clearPath();
        if (canFly() && fly && liftOff())
        {
            navigator = new FlyingPathNavigator(this, world);
            dataManager.set(FLYING, true);
        }
        else
        {
//            getMoveHelper().setMoveTo(posX, posY, posZ, 1);
            navigator = new GroundPathNavigator(this, world);
            dataManager.set(FLYING, false);
        }
    }

    /**
     * Whether or not the dragon is sleeping.
     */
    @Override
    public boolean isSleeping()
    {
        return dataManager.get(SLEEPING);
    }

    public void setSleeping(boolean sleep) // If we have a sleep animation, then play it.
    {
        if (isSleeping() == sleep || world.isRemote) return;

        dataManager.set(SLEEPING, sleep);
        clearAI();
        if (!sleep) sleepCooldown = 350;
        if (SLEEP_ANIMATION != null && WAKE_ANIMATION != null && noActiveAnimation())
            NetworkUtils.sendAnimationPacket(this, sleep? SLEEP_ANIMATION : WAKE_ANIMATION);

        recalculateSize(); // Change the hitbox for sitting / sleeping
    }

    /**
     * Setter for dragon sitting
     */
    public void setSit(boolean sitting)
    {
        if (isSitting() == sitting) return;
        setSleeping(false);
        if (!world.isRemote)
        {
            sitGoal.setSitting(sitting);
            clearAI();
        }

        super.setSitting(sitting);

        recalculateSize(); // Change the hitbox for sitting / sleeping
    }

    /**
     * Whether or not the dragonEntity is pissed or not.
     */
    public boolean isAngry() { return (dataManager.get(TAMED) & 2) != 0; }

    public void setAngry(boolean angry)
    {
        if (isAngry() == angry) return;

        byte b0 = dataManager.get(TAMED);

        if (angry) dataManager.set(TAMED, (byte) (b0 | 2));
        else dataManager.set(TAMED, (byte) (b0 & -3));
    }

    public void setHomePos(Optional<BlockPos> homePos) { dataManager.set(HOME_POS, homePos); }

    public Optional<BlockPos> getHomePos() { return dataManager.get(HOME_POS); }

    public void setHomePos(BlockPos homePos) { setHomePos(Optional.of(homePos)); }

    /**
     * Get the inventory (IItemHandler) if it exists
     */
    public DragonInvHandler getInvHandler()
    {
        return invHandler.orElseThrow(() -> new NoSuchElementException("Inventory Handler is not Present!"));
    }

    /**
     * Create an inventory (ItemStackHandler)
     */
    public LazyOptional<DragonInvHandler> createInv() { return LazyOptional.empty(); }

    // ================================

    /**
     * Called to update the entity's position/logic.
     */
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

    /**
     * Called frequently so the entity can update its state every tick as required.
     */
    @Override
    public void livingTick()
    {
        super.livingTick();

        if (isServerWorld())
        {
            // uhh so were falling, we should probably start flying
            boolean flying = canFly() && getAltitude(true) > shouldFlyThreshold && getRidingEntity() == null;
            if (flying != isFlying()) setFlying(flying);

            handleSleep();
            if (isSleeping() && getHomePos().isPresent() && isWithinHomeDistanceCurrentPosition() && getRNG().nextInt(25) == 0)
                heal(0.5f);
        }
        else
        {
            if (isSpecial()) doSpecialEffects();
        }
    }

    /**
     * Called when this dragon is riding another entity
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

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            int index = player.getPassengers().indexOf(this);
            if ((player.isSneaking() && !player.abilities.isFlying) || player.getSubmergedHeight() > 1.25 || index > 2)
            {
                stopRiding();
                return;
            }

            prevRotationPitch = rotationPitch = player.rotationPitch / 2;
            rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
            setRotation(player.rotationYawHead, rotationPitch);

            double xOffset = index == 1? (getWidth() * 0.8) : index == 2? -(getWidth() * 0.8) : 0;
            double yOffset = index == 0? 1.85d : 1.38d;
            double zOffset = 0d;

            if (player.isElytraFlying())
            {
                if (!canFly())
                {
                    stopRiding();
                    return;
                }

                xOffset *= 4d;
//                yOffset *= 0.1d;
//                zOffset = -2;
                setFlying(true);
            }

            Vec3d pos = QuikMaths.calculateYawAngle(player.renderYawOffset, xOffset, zOffset)
                    .add(player.getPosX(), player.getPosY() + yOffset, player.getPosZ());
            setPosition(pos.x, pos.y, pos.z);
        }
    }

    /**
     * Called when the player interacts with this dragon
     *
     * @param player - The player interacting
     * @param hand   - hand they used to interact
     * @param stack  - the itemstack used when interacted
     */
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.interactWithEntity(player, this, hand)) return true;

        if (getGrowingAge() == 0 && canBreed() && isBreedingItem(stack) && isOwner(player) && !player.isSneaking())
        {
            eat(stack);
            setInLove(player);

            return true;
        }

        if (isFoodItem(stack) && isTamed())
        {

            if (getHealth() < getMaxHealth())
            {
                eat(stack);

                return true;
            }

            if (isChild())
            {
                ageUp((int) ((float) (-getGrowingAge() / 20) * 0.1F), true);
                eat(stack);

                return true;
            }
        }

        return false;
    }

    // Overload to make processInteract way less annoying
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        if (!processInteract(player, hand, player.getHeldItem(hand))) return false;

        setSleeping(false);
        return true;
    }

    /**
     * Get an item stack from {@link #invHandler}
     *
     * @param slot the slot
     */
    public ItemStack getStackInSlot(int slot)
    {
        return invHandler.map(i -> i.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    /**
     * Set a stack in a particular slot in the {@link #invHandler}
     *
     * @param slot
     * @param stack
     */
    public void setStackInSlot(int slot, ItemStack stack) { invHandler.ifPresent(i -> i.setStackInSlot(slot, stack)); }

    /**
     * Get all entities in a given range in front of this entity and damage all within it
     */
    public void attackInFront(int radius)
    {
        AxisAlignedBB size = getBoundingBox();
        AxisAlignedBB aabb = size.offset(QuikMaths.calculateYawAngle(renderYawOffset, 0, size.getXSize())).grow(radius);
        attackInAABB(aabb);
    }

    /**
     * Beat up probably innocent creatures in a given aabb
     *
     * @param aabb good example: getBoundingBox().offset(QuikMaths.calculateYawAngle(renderYawOffset, 0, getBounding().getXSize() / 2));
     */
    public void attackInAABB(AxisAlignedBB aabb)
    {
        List<LivingEntity> livingEntities = world.getEntitiesWithinAABB(LivingEntity.class, aabb, found -> found != this && getPassengers().stream().noneMatch(found::equals));

        if (WRConfig.debugMode && world.isRemote) RenderEvents.queueRenderBox(aabb);
        if (livingEntities.isEmpty()) return;
        livingEntities.forEach(this::attackEntityAsMob);
    }

    /**
     * Called to damage entites
     */
    @Override // Dont damage owners other pets!
    public boolean attackEntityAsMob(Entity entity)
    {
        if (entity == getOwner()) return false;
        if (entity instanceof TameableEntity && ((TameableEntity) entity).getOwner() == getOwner()) return false;

        return super.attackEntityAsMob(entity);
    }

    /**
     * Whether the dragon should attack or not
     */
    @Override // We shouldnt be targetting pets...
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner)
    {
        if (!isTamed()) return true;
        if (target instanceof TameableEntity)
        {
            TameableEntity tamable = (TameableEntity) target;
            return tamable.getOwner() == null || tamable.getOwner() != owner;
        }

        return true;
    }

    /**
     * Called when the entity is attacked by something
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        setSleeping(false);
        setSit(false);

        return super.attackEntityFrom(source, amount);
    }

    /**
     * Effects to play when the dragon is 'Special'
     * Default to white sparkles around the body.
     * Can be overriden to do custom effects.
     */
    public void doSpecialEffects()
    {
        if (ticksExisted % 25 == 0)
        {
            double x = getPosX() + getWidth() * (getRNG().nextGaussian() * 0.5d);
            double y = getPosY() + getHeight() * (getRNG().nextDouble());
            double z = getPosZ() + getWidth() * (getRNG().nextGaussian() * 0.5d);
            world.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public boolean isGlowing() // todo: handle this more elegantly: 1.15
    {
        return super.isGlowing();
//        if (!world.isRemote) return super.isGlowing();
//        PlayerEntity player = Minecraft.getInstance().player;
//        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
//        return stack.getItem() instanceof DragonStaffItem && Objects.equals(DragonStaffItem.getDragon(stack, ModUtils.getServerWorld(player)), this) || super.isGlowing();
    }

    /**
     * Spawn drops that may not be able to be covered in the loot table
     */
    @Override
    protected void spawnDrops(DamageSource src)
    {
        invHandler.ifPresent(i ->
        {
            for (int index = 0; index < i.getSlots(); ++index) entityDropItem(i.getStackInSlot(index));
        });
        super.spawnDrops(src);
    }

    /**
     * Try to teleport to this owners position after a search for a safe location
     */
    public void tryTeleportToOwner()
    {
        if (getOwner() == null) return;
        final int CONSTRAINT = (int) (getWidth() * 0.5) + 2;
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

    /**
     * Try teleporting to a pos after a search for a safe location
     */
    public boolean trySafeTeleport(BlockPos pos)
    {
        BlockPos blockpos = pos.subtract(getPosition());
        if (world.hasNoCollisions(this, getBoundingBox().offset(blockpos)))
        {
            setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rotationYaw, rotationPitch);
            return true;
        }
        return false;
    }

    @Override
    public boolean isWithinHomeDistanceCurrentPosition() { return isWithinHomeDistanceFromPosition(getPosition()); }

    @Override
    public boolean isWithinHomeDistanceFromPosition(BlockPos pos)
    {
        return getHomePos().map(home -> home.distanceSq(pos) <= WRConfig.homeRadius * WRConfig.homeRadius).orElse(true);
    }

    /**
     * Public access version of {@link Entity#setRotation}
     */
    public void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    /**
     * Get the altitude of the dragon. (height from the dragon to the solid ground)
     *
     * @param capThreshold - Cap the lookup to the {@link AbstractDragonEntity#shouldFlyThreshold} for faster results
     * @return distance from the ground
     */
    public double getAltitude(boolean capThreshold)
    {
        BlockPos.Mutable pos = new BlockPos.Mutable(getPosition());

        if (capThreshold)
        {
            for (int i = 0; i <= shouldFlyThreshold + 1 && !world.getBlockState(pos).getMaterial().isSolid(); ++i)
                pos.move(0, -1, 0);
        }
        else
        { // cap to the world void (y = 0)
            while (pos.getY() > 0 && !world.getBlockState(pos).getMaterial().isSolid()) pos.move(0, -1, 0);
        }
        return getPosY() - pos.getY();
    }

    /**
     * Handle eating. (Effects, healing, breeding etc)
     */
    public void eat(@Nullable ItemStack stack)
    {
        if (stack != null && !stack.isEmpty())
        {
            stack.shrink(1);
            if (getHealth() < getMaxHealth()) heal(Math.max((int) getMaxHealth() / 5, 6));
            if (stack.getItem().isFood())
            {
                try
                { // Surrounding in try catch block. checking for null doesnt seem to work...
                    List<Pair<EffectInstance, Float>> effects = Objects.requireNonNull(stack.getItem().getFood()).getEffects();
                    if (!effects.isEmpty() && effects.stream().noneMatch(e -> e.getLeft() == null)) // Apply food effects if it has any
                        effects.forEach(e -> addPotionEffect(e.getLeft()));
                }
                catch (Exception ignore) {}
            }
            playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 1f);
            if (world.isRemote)
            {
                Vec3d mouth = getApproximateMouthPos();

                for (int i = 0; i < 11; ++i)
                {
                    Vec3d vec3d1 = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) rand.nextFloat() - 0.5D) * 0.1D);
                    vec3d1 = vec3d1.rotatePitch(-rotationPitch * (QuikMaths.PI / 180f));
                    vec3d1 = vec3d1.rotateYaw(-rotationYaw * (QuikMaths.PI / 180f));
                    world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), mouth.x, mouth.y, mouth.z, vec3d1.x, vec3d1.y, vec3d1.z);
                }
            }
        }
    }

    /**
     * Tame the dragon to the tamer if true
     * else, play the failed tame effects
     */
    public boolean tame(boolean tame, @Nullable PlayerEntity tamer)
    {
        if (isTamed()) return true;
        boolean flag = tame && tamer != null && !ForgeEventFactory.onAnimalTame(this, tamer);
        if (world.isRemote) playTameEffect(flag);
        if (flag)
        {
            setTamedBy(tamer);
            navigator.clearPath();
            setAttackTarget(null);
            setHealth(getMaxHealth());
            world.setEntityState(this, (byte) 7);
            return true;
        }
        else world.setEntityState(this, (byte) 6);

        return false;
    }

    /**
     * Add health to the current amount
     */
    @Override
    public void heal(float healAmount)
    {
        super.heal(healAmount);

        if (world.isRemote)
        {
            for (int i = 0; i < getWidth() * 5; ++i)
            {
                double x = getPosX() + (getRNG().nextGaussian() * getWidth()) / 1.5d;
                double y = getPosY() + getRNG().nextDouble() * (getRNG().nextDouble() + 2d);
                double z = getPosZ() + (getRNG().nextGaussian() * getWidth()) / 1.5d;
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
    }

    @Override
    public int getHorizontalFaceSpeed()
    {
        return isFlying()? 10 : super.getHorizontalFaceSpeed();
    }

    public boolean isRiding() { return getRidingEntity() != null; }


    /**
     * Children are actually eggs. So create an egg item of this dragon type and yeet it into the air
     */
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        CompoundNBT tag = new CompoundNBT();
        ItemStack eggStack = new ItemStack(WRItems.DRAGON_EGG.get());

        tag.putString("dragonType", EntityType.getKey(getType()).toString());
        tag.putInt("hatchTime", getEggProperties().getHatchTime());
        eggStack.setTag(tag);

        ItemEntity eggItem = new ItemEntity(world, getPosX(), getPosY(), getPosZ(), eggStack);

        eggItem.setMotion(0, getHeight() / 3, 0);
        world.addEntity(eggItem);

        return null;
    }

    /**
     * Handle Sleeping of this dragons
     * Override for different sleep patterns of different dragons
     */
    public void handleSleep()
    {
        if (!isSleeping()
                && --sleepCooldown <= 0
                && !world.isDaytime()
                && (!isTamed() || (isSitting() && isWithinHomeDistanceCurrentPosition()))
                && isIdling()
                && getRNG().nextInt(300) == 0) setSleeping(true);
        else if (isSleeping() && world.isDaytime() && getRNG().nextInt(150) == 0) setSleeping(false);
    }

    @Override
    protected void addPassenger(Entity passenger)
    {
        super.addPassenger(passenger);
        if (getControllingPassenger() == passenger) clearAI();
    }

    /**
     * Get the player potentially controlling this dragon
     * {@code null} if its not a player or no controller at all.
     *
     * @return The Player controlling this dragon, else null
     */
    @Nullable
    public PlayerEntity getControllingPlayer()
    {
        Entity passenger = getControllingPassenger();
        if (passenger instanceof PlayerEntity) return (PlayerEntity) passenger;
        return null;
    }

    /**
     * Clear any AI tasks that might still be running
     */
    public void clearAI()
    {
        isJumping = false;
        navigator.clearPath();
        setAttackTarget(null);
    }

    /**
     * is big boi dragon doing anything?
     */
    public boolean isIdling()
    {
        return getNavigator().noPath()
                && getAttackTarget() == null
                && !isBeingRidden()
                && !isAngry()
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
        return QuikMaths.calculateYawAngle(renderYawOffset, 0, (getWidth() / 2) + 0.5d).add(getPosX(), getPosY() + getEyeHeight() - 0.15d, getPosZ());
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        Optional<CustomSpawnEggItem> egg = CustomSpawnEggItem.EGG_TYPES.stream().filter(e -> e.type.get() == getType()).findFirst();
        return egg.map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    /**
     * Get all entities in this entities bounding box increased by a range and filtered
     */
    public List<Entity> getEntitiesNearby(double radius, Predicate<Entity> filter)
    {
        return world.getEntitiesInAABBexcluding(this, getBoundingBox().grow(radius), e -> getPassengers().stream().noneMatch(e::equals) && filter.test(e));
    }

    /**
     * Get all entities in this entities bounding box increased by a range
     */
    public List<Entity> getEntitiesNearby(double radius)
    {
        return world.getEntitiesInAABBexcluding(this, getBoundingBox().grow(radius), e -> getPassengers().stream().noneMatch(e::equals));
    }

    /**
     * Add additional motion to current velocity
     */
    public void addMotion(Vec3d vec3d) { setMotion(getMotion().add(vec3d)); }

    /**
     * Add additional motion to current velocity
     */
    public void addMotion(double x, double y, double z) { setMotion(getMotion().add(x, y, z)); }

    /**
     * If I need to explain this im breaking your legs
     */
    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) { playSound(soundIn, volume, pitch, false); }

    /**
     * Please stop
     *
     * @param sound  the sound
     * @param volume the volume
     * @param pitch  the pitch
     * @param local  client only. Completely disrespects location tho... >.>
     */
    public void playSound(SoundEvent sound, float volume, float pitch, boolean local)
    {
        if (isSilent()) return;

        volume *= getSoundVolume();
        pitch *= getSoundPitch();

        if (local) world.playSound(getPosX(), getPosY(), getPosZ(), sound, getSoundCategory(), volume, pitch, false);
        else world.playSound(null, getPosX(), getPosY(), getPosZ(), sound, getSoundCategory(), volume, pitch);
    }

    /**
     * Ambient sound. duh.
     */
    @Override
    public void playAmbientSound() { if (!isSleeping()) super.playAmbientSound(); }

    /**
     * Set a damage source immunity
     */
    public void setImmune(DamageSource source) { immunes.add(source.getDamageType()); }

    /**
     * Are we immune to this damage source?
     */
    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || (!immunes.isEmpty() && immunes.contains(source.getDamageType()));
    }

    /**
     * Can the rider "steer" or control this entity?
     */
    @Override
    public boolean canPassengerSteer()
    {
        return getControllingPassenger() != null && isOwner((LivingEntity) getControllingPassenger());
    }

    /**
     * Get the passenger controlling this entity
     */
    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
        return this.getPassengers().isEmpty()? null : this.getPassengers().get(0);
    }

    @Override
    public boolean isOnLadder() { return false; }

    /**
     * Perform a one-shot attack
     * TODO this SHOULD be for recieving requests from the player owner, so make it a single method with a byte
     * parameter to allow stuff like this:
     * if (byte == HORN_ATTACK) {...}
     * plus, it allows for more actions and honestly, looks sexier in the packet
     */
    public void performGenericAttack()
    {
    }

    /**
     * Perform a continuous special attack, e.g. Fire breathing
     *
     * @param shouldContinue True = continue attacking | False = interrupt / stop attack
     * @deprecated will be merged with {@link #performGenericAttack()}
     */
    public void performAltAttack(boolean shouldContinue)
    {
    }

    /**
     * Sort of misleading name. if this is true, then nothing else is ticked (goals, look, etc)
     */
    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || isSleeping(); }

    /**
     * Whether or not the dragon can fly.
     * For ground entities, return false
     */
    public boolean canFly() { return !isChild() && !getLeashed(); }

    /**
     * Get the motion this entity performs when jumping
     */
    @Override
    protected float getJumpUpwardsMotion()
    {
        return canFly()? 1.25f : super.getJumpUpwardsMotion();
    }

    /**
     * Called to "liftoff" the dragon. (Shoots it up in the air for flight)
     */
    public boolean liftOff()
    {
        if (!canFly()) return false;

        for (int i = 1; i < (shouldFlyThreshold / 2.5f) + 1; ++i)
        {
            if (world.getBlockState(getPosition().up((int) getHeight() + i)).getMaterial().blocksMovement())
                return false;
        }
        setSit(false);
        setSleeping(false);
        jump();
//        clearAI();

        return true;
    }

    /**
     * Do the thing break legs thing when you hit the ground from jumping off the empire state building
     */
    @Override // Disable fall calculations if we can fly (fall damage etc.)
    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        if (canFly()) return false;
        return super.onLivingFall(distance, damageMultiplier);
    }

    /**
     * Set the view angles of the game camera while riding this dragon in 3rd person
     */
    public void setMountCameraAngles(boolean backView) {}

    /**
     * Add Information or widgets to the Staff Screen when it is opened by a player
     */
    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.HOME_POS);
        screen.addAction(StaffAction.SIT);

        screen.toolTip.add("Owner: " + getOwner().getName().getUnformattedComponentText());
        screen.toolTip.add(String.format("Health: %s / %s", (int) getHealth(), (int) getMaxHealth()));
    }

    /**
     * Add Container info
     */
    public void addContainerInfo(DragonInvContainer container)
    {
        container.buildPlayerSlots(container.playerInv, 17, 136);
    }

    /**
     * Called when the inventory is updated
     */
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad) {}

    /**
     * Is the passed stack considered a breeding item?
     * Default return {@link #isFoodItem(ItemStack)} - Intended to be overrided if applicable
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) { return isFoodItem(stack); }

    /**
     * Is the passed stack considered a food item defined in {@link #getFoodItems()}
     */
    public boolean isFoodItem(ItemStack stack)
    {
        if (getFoodItems() == null || getFoodItems().size() == 0) return false;
        if (stack == ItemStack.EMPTY) return false;
        return getFoodItems().contains(stack.getItem());
    }

    /**
     * Array Containing all of the dragons food items
     */
    public abstract Collection<Item> getFoodItems();

    public DragonEggProperties getEggProperties()
    {
        if (eggProperties == null) // This shouldn't happen, lazily fix it if it does tho.
        {
            ModUtils.L.warn("{} is missing dragon egg properties! Using default values...", getType().getName().getUnformattedComponentText());
            eggProperties = new DragonEggProperties(2f, 2f, 12000);
        }
        return eggProperties;
    }

    /**
     * The egg properties this dragon's eggs have
     */
    public abstract DragonEggProperties createEggProperties();

    // ================================
    //        Entity Animation
    // ================================

    /**
     * Get the current tick time for the playing animation
     */
    @Override
    public int getAnimationTick()
    {
        return animationTick;
    }

    /**
     * Set the animation tick for the playing animation
     */
    @Override
    public void setAnimationTick(int tick)
    {
        animationTick = tick;
    }

    /**
     * Get the current playing animation
     */
    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    /**
     * Set an animation
     */
    @Override
    public void setAnimation(Animation animation)
    {
        if (animation == null) animation = NO_ANIMATION;
        setAnimationTick(0);
        this.animation = animation;
    }

    /**
     * Is no active animation playing currently?
     */
    public boolean noActiveAnimation()
    {
        return getAnimation() == NO_ANIMATION || getAnimationTick() == 0;
    }

    // ================================

    /**
     * Create a boolean data Parameter
     */
    public static <T> DataParameter<T> createKey(IDataSerializer<T> serializer)
    {
        return EntityDataManager.createKey(AbstractDragonEntity.class, serializer);
    }
}
