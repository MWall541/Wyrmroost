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

    public DragonFruitDrakeEntity(EntityType<? extends DragonFruitDrakeEntity> dragon, World level)
    {
        super(dragon, level);

        registerDataEntry("ShearTimer", EntityDataEntry.INTEGER, () -> shearCooldownTime, v -> shearCooldownTime = v);
        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRandom().nextBoolean());
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
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
        goalSelector.addGoal(9, new FollowParentGoal(this, 1)
        {
            { setFlags(EnumSet.of(Flag.MOVE)); }

            @Override
            public boolean canUse()
            {
                return !isTame() && super.canUse();
            }
        });
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 7f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
        goalSelector.addGoal(7, temptGoal = new TemptGoal(this, 1d, false, Ingredient.of(Items.APPLE))
        {
            @Override
            public boolean canUse()
            {
                return !isTame() && isBaby() && super.canUse();
            }
        });

        targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        targetSelector.addGoal(1, new NonTamedTargetGoal<PlayerEntity>(this, PlayerEntity.class, true, EntityPredicates.ATTACK_ALLOWED::test)
        {
            @Override
            public boolean canUse()
            {
                return !isBaby() && super.canUse();
            }
        });
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SHEARS && isShearable(stack, level, blockPosition()))
            return ActionResultType.sidedSuccess(level.isClientSide);

        if (!isTame() && isBaby() && isFoodItem(stack))
        {
            if (!level.isClientSide && temptGoal.isRunning())
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
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

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
                        if (plant.isValidBonemealTarget(level, pos, state, false))
                        {
                            plant.performBonemeal((ServerWorld) level, getRandom(), pos, state);
                            level.levelEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
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
            AxisAlignedBB aabb = getBoundingBox().inflate(2).move(Mafs.getYawVec(yHeadRot, 0, 2));
            for (BlockPos pos : ModUtils.iterateThrough(aabb))
            {
                if (level.getBlockState(pos).getBlock() instanceof BushBlock)
                    level.destroyBlock(pos, true, this);
            }
        }
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().is(Tags.Items.CROPS) || ModUtils.equalsAny(stack.getItem(), Items.APPLE, Items.SWEET_BERRIES, Items.MELON, Items.GLISTERING_MELON_SLICE);
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY1 && pressed) setAnimation(BITE_ANIMATION);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return getBbHeight();
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scale(getScale());
        if (isInSittingPose() || isSleeping()) size = size.scale(1, 0.7f);
        return size;
    }

    @Override
    public double getPassengersRidingOffset()
    {
        return super.getPassengersRidingOffset() + 0.1d;
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
        playSound(SoundEvents.MOOSHROOM_SHEAR, 1f, 1f);
        shearCooldownTime = 12000;
        return Collections.singletonList(new ItemStack(Items.APPLE, 1 + fortune + getRandom().nextInt(2)));
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().move(-0.25d, 0.5d, 0);
        else event.getInfo().move(-1.5, 0.15, 0);
    }

    @Override
    public void swing(Hand hand)
    {
        super.swing(hand);
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
    protected boolean canRide(Entity passenger)
    {
        return !isBaby() && passenger instanceof LivingEntity && isOwnedBy((LivingEntity) passenger);
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
    public boolean isFood(ItemStack stack)
    {
        return stack.getItem() == Items.APPLE;
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, BITE_ANIMATION};
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld level, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT dataTag)
    {
        if (data == null)
        {
            data = new AgeableData(true);
            if (reason == SpawnReason.NATURAL) setAge(DragonEggProperties.get(getType()).getGrowthTime()); // set the first spawning dfd as a baby. the rest of the group will spawn as an adult.
        }

        return super.finalizeSpawn(level, difficulty, reason, data, dataTag);
    }

    public static <F extends MobEntity> boolean getSpawnPlacement(EntityType<F> fEntityType, IServerWorld level, SpawnReason spawnReason, BlockPos pos, Random random)
    {
        BlockState state = level.getBlockState(pos.below());
        return state.is(Blocks.GRASS_BLOCK) || (state.is(BlockTags.LEAVES) && pos.getY() < level.getSeaLevel() + 13) && level.getRawBrightness(pos, 0) > 8;
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.JUNGLE)
            event.getSpawns().addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.DRAGON_FRUIT_DRAKE.get(), 23, 4, 5));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 15)
                .add(MOVEMENT_SPEED, 0.23)
                .add(ATTACK_DAMAGE, 3);
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
            return growCropsTime >= 0 && findNearestBlock();
        }

        @Override
        protected int nextStartTick(CreatureEntity creature)
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
            getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getY());
            if (tryTicks >= 200 && getRandom().nextInt(tryTicks) >= 100)
            {
                tryTicks = 0;
                findNearestBlock();
            }
        }

        @Override
        protected boolean isValidTarget(IWorldReader level, BlockPos pos)
        {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            return !pos.equals(blockPos) && isCrop(block) && ((IGrowable) block).isValidBonemealTarget(level, pos, state, false);
        }
    }
}
