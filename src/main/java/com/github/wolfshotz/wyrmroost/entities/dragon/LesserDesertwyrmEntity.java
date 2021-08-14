package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.model.entity.LesserDesertwyrmModel;
import com.github.wolfshotz.wyrmroost.items.LDWyrmItem;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import com.github.wolfshotz.wyrmroost.util.animation.LogicalAnimation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.entity.ai.attributes.Attributes.*;

/**
 * Desertwyrm Dragon Entity
 * Seperated from AbstractDragonEntity:
 * This does not need/require much from that class and would instead create redundancies. do this instead.
 */
public class LesserDesertwyrmEntity extends AnimalEntity implements IAnimatable
{
    public static final String DATA_BURROWED = "Burrowed";
    public static final Animation BITE_ANIMATION = LogicalAnimation.create(10, null, () -> LesserDesertwyrmModel::biteAnimation);
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.defineId(LesserDesertwyrmEntity.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> AVOIDING = t -> EntityPredicates.ATTACK_ALLOWED.test(t) && !(t instanceof LesserDesertwyrmEntity);

    public Animation animation = NO_ANIMATION;
    public int animationTick;

    public LesserDesertwyrmEntity(EntityType<? extends LesserDesertwyrmEntity> minutus, World level)
    {
        super(minutus, level);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new BurrowGoal());
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LivingEntity.class, 6f, 0.8d, 1.2d, AVOIDING));
        goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(SpawnEggItem.byId(getType()));
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(BURROWED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound)
    {
        super.addAdditionalSaveData(compound);
        compound.putBoolean(DATA_BURROWED, isBurrowed());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound)
    {
        super.readAdditionalSaveData(compound);
        setBurrowed(compound.getBoolean(DATA_BURROWED));
    }

    /**
     * Whether or not the Minutus is burrowed
     */
    public boolean isBurrowed()
    {
        return entityData.get(BURROWED);
    }

    public void setBurrowed(boolean burrow)
    {
        entityData.set(BURROWED, burrow);
    }

    // ================================

    @Override
    public void aiStep()
    {
        super.aiStep();

        if (isBurrowed())
        {
            if (level.getBlockState(blockPosition().below(1)).getMaterial() != Material.SAND) setBurrowed(false);
            attackAbove();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        super.tick();
        updateAnimations();
    }

    private void attackAbove()
    {
        Predicate<Entity> predicateFilter = filter ->
        {
            if (filter instanceof LesserDesertwyrmEntity) return false;
            return filter instanceof FishingBobberEntity || (filter instanceof LivingEntity && filter.getBbWidth() < 0.9f && filter.getBbHeight() < 0.9f);
        };
        AxisAlignedBB aabb = getBoundingBox().expandTowards(0, 2, 0).inflate(0.5, 0, 0.5);
        List<Entity> entities = level.getEntities(this, aabb, predicateFilter);
        if (entities.isEmpty()) return;

        Optional<Entity> closest = entities.stream().min(Comparator.comparingDouble(entity -> entity.distanceTo(this)));
        Entity entity = closest.get();
        if (entity instanceof FishingBobberEntity)
        {
            entity.remove();
            setDeltaMovement(0, 0.8, 0);
            setBurrowed(false);
        }
        else
        {
            if (getAnimation() != BITE_ANIMATION) setAnimation(BITE_ANIMATION);
            doHurtTarget(entity);
        }
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        if (player.getItemInHand(hand).isEmpty())
        {
            if (!level.isClientSide)
            {
                ItemStack stack = new ItemStack(WRItems.LDWYRM.get());
                CompoundNBT tag = new CompoundNBT();
                CompoundNBT subTag = serializeNBT();
                tag.put(LDWyrmItem.DATA_CONTENTS, subTag);
                if (hasCustomName()) stack.setHoverName(getCustomName());
                stack.setTag(tag);
                InventoryHelper.dropItemStack(level, getX(), getY(), getZ(), stack);
                remove();
            }
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, IWorldReader world) // Attracted to sand
    {
        if (level.getBlockState(pos).getMaterial() == Material.SAND) return 10f;
        return super.getWalkTargetValue(pos, level);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer)
    {
        return !level.isDay();
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld level, AgeableEntity p_241840_2_)
    {
        return null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || source == DamageSource.IN_WALL;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_LDWYRM_IDLE.get();
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.15f;
    }

    @Override
    public boolean isPushable()
    {
        return !isBurrowed();
    }

    @Override
    public boolean isPickable()
    {
        return !isBurrowed();
    }

    @Override
    protected void doPush(Entity entityIn)
    {
        if (!isBurrowed()) super.doPush(entityIn);
    }

    @Override
    protected boolean isImmobile()
    {
        return super.isImmobile() || isBurrowed();
    }

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
    public Animation[] getAnimations()
    {
        return new Animation[] {BITE_ANIMATION};
    }

    @Override
    public void setAnimation(Animation animation)
    {
        this.animation = animation;
        setAnimationTick(0);
    }

    public static <F extends MobEntity> boolean getSpawnPlacement(EntityType<F> fEntityType, IServerWorld level, SpawnReason reason, BlockPos pos, Random random)
    {
        if (reason == SpawnReason.SPAWNER) return true;
        Block block = level.getBlockState(pos.below()).getBlock();
        return block == Blocks.SAND && level.getRawBrightness(pos, 0) > 8;
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.DESERT)
            event.getSpawns().addSpawn(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(WREntities.LESSER_DESERTWYRM.get(), 11, 1, 3));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 4)
                .add(MOVEMENT_SPEED, 0.4)
                .add(ATTACK_DAMAGE, 4);
    }

    class BurrowGoal extends Goal
    {
        private int burrowTicks = 30;

        public BurrowGoal()
        {
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean canUse()
        {
            return !isBurrowed() && belowIsSand();
        }

        @Override
        public boolean canContinueToUse()
        {
            return belowIsSand() && (isBurrowed() || burrowTicks > 0);
        }

        @Override
        public void stop()
        {
            burrowTicks = 30;
            setBurrowed(false);
        }

        @Override
        public void tick()
        {
            if (burrowTicks > 0 && --burrowTicks == 0) setBurrowed(true);
        }

        private boolean belowIsSand()
        {
            return level.getBlockState(blockPosition().below(1)).is(BlockTags.SAND);
        }
    }
}
