package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.SleepController;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggProperties;
import com.github.wolfshotz.wyrmroost.entities.util.AnonymousGoals;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static net.minecraft.entity.SharedMonsterAttributes.*;

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
    protected SleepController createSleepController()
    {
        return new SleepController(this).addWakeConditions(() -> --napTime <= 0);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(3, new MoveToCropsGoal());
        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new DragonBreedGoal(this));
        goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.3, false));
        goalSelector.addGoal(8, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(9, AnonymousGoals.followParent(this, 1));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 7f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
        goalSelector.addGoal(7, temptGoal = new TemptGoal(this, 1d, false, Ingredient.fromItems(Items.APPLE))
        {
            @Override
            public boolean shouldExecute()
            {
                return !isTamed() && isChild() && super.shouldExecute();
            }
        });

        targetSelector.addGoal(1, AnonymousGoals.nonTamedHurtByTarget(this).setCallsForHelp(DragonFruitDrakeEntity.class));
        targetSelector.addGoal(2, new NonTamedTargetGoal<>(this, PlayerEntity.class, true, EntityPredicates.CAN_AI_TARGET::test));
    }

//    @Override
//    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
//    {
//        if (stack.getItem() == Items.SHEARS && canBeSteered())
//            return ActionResultType.func_233537_a_(world.isRemote);
//
//        if (!isTamed() && isChild() && isFoodItem(stack))
//        {
//            if (!world.isRemote && temptGoal.isRunning())
//            {
//                tame(getRNG().nextDouble() <= 0.2d, player);
//                eat(stack);
//                return ActionResultType.SUCCESS;
//            }
//            return ActionResultType.CONSUME;
//        }
//
//        if (isTamed() && stack.getItem() == Items.GLISTERING_MELON_SLICE && growCropsTime <= 0)
//        {
//            eat(stack);
//            growCropsTime = CROP_GROWTH_TIME;
//            return ActionResultType.func_233537_a_(world.isRemote);
//        }
//
//        return super.playerInteraction(player, hand, stack);
//    }


    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SHEARS && canBeSteered()) return true;

        if (!isTamed() && isChild() && isFoodItem(stack))
        {
            if (!world.isRemote && temptGoal.isRunning())
            {
                tame(getRNG().nextDouble() <= 0.2d, player);
                eat(stack);
            }
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
    public void livingTick()
    {
        super.livingTick();

        sitTimer.add((isSitting() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.05f : -0.1f);

        if (!world.isRemote)
        {
            setSprinting(getAttackTarget() != null);
            if (shearCooldownTime > 0) --shearCooldownTime;

            if (growCropsTime >= 0)
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

            if (!isChild() && world.isDaytime() && !isSleeping() && isIdling() && getRNG().nextDouble() < 0.002)
            {
                napTime = 1200;
                setSleeping(true);
            }
        }

        if (getAnimation() == BITE_ANIMATION && getAnimationTick() == 7 && canPassengerSteer())
        {
            attackInBox(getOffsetBox(getWidth()));
            AxisAlignedBB aabb = getBoundingBox().grow(2).offset(Mafs.getYawVec(rotationYawHead, 0, 2));
            for (BlockPos pos : ModUtils.getBlockPosesInAABB(aabb))
            {
                if (world.getBlockState(pos).getBlock() instanceof BushBlock)
                    world.destroyBlock(pos, true, this);
            }
        }
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().isIn(Tags.Items.CROPS) || ModUtils.isItemIn(stack, Items.APPLE, Items.SWEET_BERRIES, Items.MELON, Items.GLISTERING_MELON_SLICE);
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY1 && pressed) setAnimation(BITE_ANIMATION);
    }

    @Override
    public boolean preventDespawn()
    {
        return isTamed();
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer)
    {
        return ticksExisted > 2400;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return getHeight();
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSitting() || isSleeping()) size = size.scale(1, 0.7f);
        return size;
    }

    @Override
    public double getMountedYOffset()
    {
        return super.getMountedYOffset() + 0.1d;
    }

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
    public boolean canFly()
    {
        return false;
    }

    @Override
    public int determineVariant()
    {
        return getRNG().nextDouble() < 0.01? -1 : 0;
    }

    @Override
    protected boolean canBeRidden(Entity passenger)
    {
        return !isChild() && passenger instanceof LivingEntity && isOwner((LivingEntity) passenger);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_DFD_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_DFD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_DFD_DEATH.get();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.APPLE;
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, BITE_ANIMATION};
    }

    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT dataTag)
    {
        if (data == null)
        {
            data = new AgeableData();
            if (reason == SpawnReason.NATURAL)
                setGrowingAge(DragonEggProperties.get(getType()).getGrowthTime()); // set the first spawning dfd as a baby. the rest of the group will spawn as an adult.
        }

        return super.onInitialSpawn(world, difficulty, reason, data, dataTag);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(15);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.23);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(3);
    }

    public static <F extends MobEntity> boolean getSpawnPlacement(EntityType<F> fEntityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random)
    {
        BlockState state = world.getBlockState(pos.down());
        return (state.getBlock() == Blocks.GRASS_BLOCK || state.isIn(BlockTags.LEAVES)) && world.getLightSubtracted(pos, 0) > 8;
    }

    public static void setSpawnBiomes(Biome biome)
    {
        if (biome.getCategory() == Biome.Category.JUNGLE)
            biome.getSpawns(EntityClassification.AMBIENT).add(new Biome.SpawnListEntry(WREntities.DRAGON_FRUIT_DRAKE.get(), 14, 4, 5));
    }

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
        public boolean shouldExecute()
        {
            return growCropsTime >= 0 && searchForDestination();
        }

        @Override
        protected int getRunDelay(CreatureEntity creature)
        {
            return 100;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return growCropsTime >= 0;
        }

        @Override
        public void tick()
        {
            super.tick();
            getLookController().setLookPosition(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getY());
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
