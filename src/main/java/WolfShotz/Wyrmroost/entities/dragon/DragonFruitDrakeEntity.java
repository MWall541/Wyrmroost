package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TickFloat;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class DragonFruitDrakeEntity extends AbstractDragonEntity implements IShearable
{
    private static final int CROP_GROWTH_RADIUS = 5;
    private static final int CROP_GROWTH_TIME = 1200; // 1 minute
    public static final Animation BITE_ANIMATION = new Animation(15);

    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    private int shearCooldownTime, napTime, growCropsTime;
    private TemptGoal temptGoal;

    public DragonFruitDrakeEntity(EntityType<? extends DragonFruitDrakeEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("ShearTimer", EntityDataEntry.INTEGER, () -> shearCooldownTime, v -> shearCooldownTime = v);
        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
    }

    @Override
    public Collection<? extends IItemProvider> getFoodItems()
    {
        return ImmutableSet.<Item>builder()
                .addAll(Tags.Items.CROPS.getAllElements())
                .add(Items.APPLE, Items.SWEET_BERRIES, Items.MELON, Items.GLISTERING_MELON_SLICE)
                .build();
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(3, new MoveToCropsGoal());
        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new DragonBreedGoal(this, 0));
        goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.3, false));
        goalSelector.addGoal(8, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(9, CommonGoalWrappers.followParent(this, 1));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 7f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
        goalSelector.addGoal(7, temptGoal = new TemptGoal(this, 1d, false, Ingredient.fromItems(Items.APPLE))
        {
            @Override
            public boolean shouldExecute() { return !isTamed() && isChild() && super.shouldExecute(); }
        });

        targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp(DragonFruitDrakeEntity.class));
        targetSelector.addGoal(2, new NonTamedTargetGoal<>(this, PlayerEntity.class, true, EntityPredicates.CAN_AI_TARGET::test));
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.232524f);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20d);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3d);
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SHEARS && canBeSteered())
            return true; // Shears return false on entity interactions. bad, but workaround for it.

        if (!isTamed() && isChild() && isFoodItem(stack) && temptGoal.isRunning())
        {
            tame(getRNG().nextDouble() <= 0.2d, player);
            eat(stack);
            return true;
        }

        if (isTamed() && stack.getItem() == Items.GLISTERING_MELON_SLICE && growCropsTime <= 0)
        {
            eat(stack);
            growCropsTime = CROP_GROWTH_TIME;
            return true;
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (shearCooldownTime > 0) --shearCooldownTime;
        sitTimer.add((isSitting() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.05f : -0.1f);

        setSprinting(getAttackTarget() != null);

        if (!world.isRemote && growCropsTime >= 0)
        {
            --growCropsTime;
            if (getRNG().nextBoolean())
            {
                AxisAlignedBB aabb = getBoundingBox().grow(CROP_GROWTH_RADIUS);
                int x = MathHelper.nextInt(getRNG(), (int) aabb.minX, (int) aabb.maxX);
                int y = MathHelper.nextInt(getRNG(), (int) aabb.minY, (int) aabb.maxY);
                int z = MathHelper.nextInt(getRNG(), (int) aabb.minZ, (int) aabb.maxZ);
                BlockPos pos = new BlockPos(x, y, z);
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof IGrowable && !(block instanceof GrassBlock))
                {
                    IGrowable plant = (IGrowable) block;
                    if (plant.canGrow(world, pos, state, false))
                    {
                        plant.grow((ServerWorld) world, getRNG(), pos, state);
                        world.playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
                    }
                }
            }
        }

        if (getAnimation() == BITE_ANIMATION && getAnimationTick() == 7 && canPassengerSteer())
        {
            attackInFront(0);
            AxisAlignedBB aabb = getBoundingBox().grow(2).offset(Mafs.getYawVec(rotationYawHead, 0, 2));
            for (BlockPos pos : ModUtils.getBlockPosesInAABB(aabb))
            {
                if (world.getBlockState(pos).getBlock() instanceof BushBlock)
                    world.destroyBlock(pos, true, this);
            }
        }
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY1 && pressed) setAnimation(BITE_ANIMATION);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) { return true; }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) { return true; }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag)
    {
        if (spawnData == null) spawnData = new AgeableData();
        ((AgeableData) spawnData).setBabySpawnProbability((float) WRConfig.dfdBabyChance);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnData, dataTag);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return getHeight(); }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSitting() || isSleeping()) size = size.scale(1, 0.7f);
        return size;
    }

    @Override
    public double getMountedYOffset() { return super.getMountedYOffset() + 0.1d; }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, IWorldReader world, BlockPos pos)
    {
        return shearCooldownTime <= 0;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune)
    {
        playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1f, 1f);
        shearCooldownTime = 12000;
        return Collections.singletonList(new ItemStack(Items.APPLE, 1 + fortune + getRNG().nextInt(2)));
    }

    @Override // These bois are lazy, can sleep during the day
    public void handleSleep()
    {
        if (isSleeping() && --napTime <= 0 && world.isDaytime() && getRNG().nextInt(375) == 0)
        {
            setSleeping(false);
            return;
        }
        if (isSleeping() || (isChild() && world.isDaytime())) return;
        if (--sleepCooldown > 0) return;
        if (isTamed() && !isSitting()) return;
        if (!isIdling()) return;
        int sleepChance = world.isDaytime()? 450 : 300; // neps
        if (getRNG().nextInt(sleepChance) == 0)
        {
            setSleeping(true);
            this.napTime = 150 * getRNG().nextInt(9);
        }
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().movePosition(-0.25d, 0.5d, 0);
        else event.getInfo().movePosition(-1.5, 0.15, 0);
    }

    @Override
    public void swingArm(Hand hand)
    {
        super.swingArm(hand);
        setAnimation(BITE_ANIMATION);
    }

    @Override
    public boolean canFly() { return false; }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(500) == 0? -1 : 0; }

    @Override
    protected boolean canBeRidden(Entity passenger)
    {
        return !isChild() && passenger instanceof LivingEntity && isOwner((LivingEntity) passenger);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_COW_AMBIENT; }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_COW_HURT; }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_COW_DEATH; }

    public static Consumer<EntityType<DragonFruitDrakeEntity>> getSpawnPlacements()
    {
        return t ->
        {
            for (Biome biome : BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE))
                biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(WREntities.DRAGON_FRUIT_DRAKE.get(), 4, 3, 5));

            EntitySpawnPlacementRegistry.register(t,
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING,
                    ((type, world, reason, pos, rand) ->
                    {
                        if (world.getLightSubtracted(pos, 0) <= 8) return false;
                        Block block = world.getBlockState(pos.down()).getBlock();
                        return block instanceof GrassBlock || block instanceof LeavesBlock;
                    }));
        };
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.APPLE; }

    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, BITE_ANIMATION}; }

    public static boolean isCrop(Block block)
    {
        return block instanceof IGrowable && !(block instanceof GrassBlock);
    }

    // todo: completely remake this so it instead looks for random block in range instead of closest,and checks to see if block is in range rather than being directly ontop of it
    private class MoveToCropsGoal extends MoveToBlockGoal
    {
        public MoveToCropsGoal()
        {
            super(DragonFruitDrakeEntity.this, 1, CROP_GROWTH_RADIUS * 2);
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() { return growCropsTime >= 0 && searchForDestination(); }

        @Override
        protected int getRunDelay(CreatureEntity creature) { return 100; }

        @Override
        public boolean shouldContinueExecuting() { return growCropsTime >= 0; }

        @Override
        public void tick()
        {
            super.tick();
            getLookController().setLookPosition(new Vec3d(destinationBlock));
            if (timeoutCounter >= 200 && getRNG().nextInt(timeoutCounter) >= 100)
            {
                timeoutCounter = 0;
                searchForDestination();
            }
        }

        @Override
        protected boolean shouldMoveTo(IWorldReader world, BlockPos pos)
        {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            return !pos.equals(destinationBlock) && isCrop(block) && ((IGrowable) block).canGrow(world, pos, state, false);
        }
    }
}
