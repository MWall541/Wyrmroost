package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggProperties;
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
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class DragonFruitDrakeEntity extends AbstractDragonEntity implements IForgeShearable
{
    private static final int CROP_GROWTH_RADIUS = 5;
    private static final int CROP_GROWTH_TIME = 1200; // 1 minute
    public static final Animation BITE_ANIMATION = new Animation(15);

    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    private int shearCooldownTime, napTime, growCropsTime;
    private TemptGoal temptGoal;

    public DragonFruitDrakeEntity(EntityType<? extends DragonFruitDrakeEntity> dragon, World world)
    {
        super(dragon, level);

        registerDataEntry("ShearTimer", EntityDataEntry.INTEGER, () -> shearCooldownTime, v -> shearCooldownTime = v);
        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRandom().nextBoolean());
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void initGoals()
    {
        super.initGoals();

        goalSelector.add(3, new MoveToCropsGoal());
        goalSelector.add(4, new MoveToHomeGoal(this));
        goalSelector.add(5, new DragonBreedGoal(this));
        goalSelector.add(6, new MeleeAttackGoal(this, 1.3, false));
        goalSelector.add(8, new WRFollowOwnerGoal(this));
        goalSelector.add(9, new FollowParentGoal(this, 1)
        {
            { setFlags(EnumSet.of(Flag.MOVE)); }

            @Override
            public boolean canUse()
            {
                return !isTamed() && super.canStart();
            }
        });
        goalSelector.add(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.add(11, new LookAtGoal(this, LivingEntity.class, 7f));
        goalSelector.add(12, new LookRandomlyGoal(this));
        goalSelector.add(7, temptGoal = new TemptGoal(this, 1d, false, Ingredient.of(Items.APPLE))
        {
            @Override
            public boolean canUse()
            {
                return !isTamed() && isBaby() && super.canStart();
            }
        });

        targetSelector.add(0, new HurtByTargetGoal(this).setGroupRevenge());
        targetSelector.add(1, new NonTamedTargetGoal<PlayerEntity>(this, PlayerEntity.class, true, EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL::test)
        {
            @Override
            public boolean canUse()
            {
                return !isBaby() && super.canStart();
            }
        });
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SHEARS && isShearable(stack, level, blockPosition()))
            return ActionResultType.success(level.isClientSide);

        if (!isTame() && isBaby() && isFoodItem(stack))
        {
            if (!level.isClientSide && temptGoal.isActive())
            {
                tame(getRandom().nextDouble() <= 0.2d, player);
                eat(stack);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.CONSUME;
        }

        if (isTame() && stack.getItem() == Items.GLISTERING_MELON_SLICE && growCropsTime <= 0)
        {
            eat(stack);
            growCropsTime = CROP_GROWTH_TIME;
            return ActionResultType.success(level.isClientSide);
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void tickMovement()
    {
        super.tickMovement();

        sitTimer.add((isInSittingPose() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.05f : -0.1f);

        if (!level.isClientSide)
        {
            setSprinting(getTarget() != null);
            if (shearCooldownTime > 0) --shearCooldownTime;
            if (napTime > 0) --napTime;

            if (growCropsTime >= 0)
            {
                --growCropsTime;
                if (getRandom().nextBoolean())
                {
                    AxisAlignedBB aabb = getBoundingBox().inflate(CROP_GROWTH_RADIUS);
                    int x = MathHelper.nextInt(getRandom(), (int) aabb.minX, (int) aabb.maxX);
                    int y = MathHelper.nextInt(getRandom(), (int) aabb.minY, (int) aabb.maxY);
                    int z = MathHelper.nextInt(getRandom(), (int) aabb.minZ, (int) aabb.maxZ);
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();
                    if (block instanceof IGrowable && !(block instanceof GrassBlock))
                    {
                        IGrowable plant = (IGrowable) block;
                        if (plant.isFertilizable(level, pos, state, false))
                        {
                            plant.grow((ServerWorld) level, getRandom(), pos, state);
                            level.syncWorldEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
                        }
                    }
                }
            }

            if (!isBaby() && level.isDay() && !isSleeping() && isIdling() && getRandom().nextDouble() < 0.002)
            {
                napTime = 1200;
                setSleeping(true);
            }
        }

        if (getAnimation() == BITE_ANIMATION && getAnimationTick() == 7 && canBeControlledByRider())
        {
            attackInBox(getOffsetBox(getBbWidth()));
            AxisAlignedBB aabb = getBoundingBox().inflate(2).offset(Mafs.getYawVec(headYaw, 0, 2));
            for (BlockPos pos : ModUtils.iterateThrough(aabb))
            {
                if (level.getBlockState(pos).getBlock() instanceof BushBlock)
                    level.breakBlock(pos, true, this);
            }
        }
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().isIn(Tags.Items.CROPS) || ModUtils.equalsAny(stack.getItem(), Items.APPLE, Items.SWEET_BERRIES, Items.MELON, Items.GLISTERING_MELON_SLICE);
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY1 && pressed) setAnimation(BITE_ANIMATION);
    }

    @Override
    protected float getActiveEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return getBbHeight();
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scaled(getScaleFactor());
        if (isInSittingPose() || isSleeping()) size = size.scaled(1, 0.7f);
        return size;
    }

    @Override
    public double getMountedHeightOffset()
    {
        return super.getMountedHeightOffset() + 0.1d;
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, World world, BlockPos pos)
    {
        return shearCooldownTime <= 0;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable PlayerEntity player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune)
    {
        playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1f, 1f);
        shearCooldownTime = 12000;
        return Collections.singletonList(new ItemStack(Items.APPLE, 1 + fortune + getRandom().nextInt(2)));
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().moveBy(-0.25d, 0.5d, 0);
        else event.getInfo().moveBy(-1.5, 0.15, 0);
    }

    @Override
    public void swingHand(Hand hand)
    {
        super.swingHand(hand);
        setAnimation(BITE_ANIMATION);
    }

    @Override
    public boolean canFly()
    {
        return false;
    }

    @Override
    public boolean shouldSleep()
    {
        return napTime <= 0 && super.shouldSleep();
    }

    @Override
    public int determineVariant()
    {
        return getRandom().nextDouble() < 0.01? -1 : 0;
    }

    @Override
    protected boolean canStartRiding(Entity passenger)
    {
        return !isBaby() && passenger instanceof LivingEntity && isOwner((LivingEntity) passenger);
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
    public ILivingEntityData initialize(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT dataTag)
    {
        if (data == null)
        {
            data = new AgeableData(true);
            if (reason == SpawnReason.NATURAL) setBreedingAge(DragonEggProperties.get(getType()).getGrowthTime()); // set the first spawning dfd as a baby. the rest of the group will spawn as an adult.
        }

        return super.initialize(level, difficulty, reason, data, dataTag);
    }

    public static <F extends MobEntity> boolean getSpawnPlacement(EntityType<F> fEntityType, IServerWorld world, SpawnReason spawnReason, BlockPos pos, Random random)
    {
        BlockState state = level.getBlockState(pos.below());
        return state.isOf(Blocks.GRASS_BLOCK) || (state.isIn(BlockTags.LEAVES) && pos.getY() < level.getSeaLevel() + 13) && level.getBaseLightLevel(pos, 0) > 8;
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.JUNGLE)
            event.getSpawns().spawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.DRAGON_FRUIT_DRAKE.get(), 23, 4, 5));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(GENERIC_MAX_HEALTH, 15)
                .add(GENERIC_MOVEMENT_SPEED, 0.23)
                .add(GENERIC_ATTACK_DAMAGE, 3);
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
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean canUse()
        {
            return growCropsTime >= 0 && findTargetPos();
        }

        @Override
        protected int getInterval(CreatureEntity creature)
        {
            return 100;
        }

        @Override
        public boolean canContinueToUse()
        {
            return growCropsTime >= 0;
        }

        @Override
        public void tick()
        {
            super.tick();
            getLookControl().setLookAt(targetPos.getX(), targetPos.getY(), targetPos.getY());
            if (tryingTime >= 200 && getRandom().nextInt(tryingTime) >= 100)
            {
                tryingTime = 0;
                findTargetPos();
            }
        }

        @Override
        protected boolean isTargetPos(IWorldReader world, BlockPos pos)
        {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            return !pos.equals(targetPos) && isCrop(block) && ((IGrowable) block).isFertilizable(level, pos, state, false);
        }
    }
}
