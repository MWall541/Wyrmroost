package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.items.LDWyrmItem;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
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
import net.minecraftforge.event.ForgeEventFactory;
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
public class LDWyrmEntity extends AnimalEntity implements IAnimatable
{
    public static final String DATA_BURROWED = "Burrowed";
    public static final Animation BITE_ANIMATION = new Animation(10);
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.registerData(LDWyrmEntity.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> AVOIDING = t -> EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(t) && !(t instanceof LDWyrmEntity);

    public Animation animation = NO_ANIMATION;
    public int animationTick;

    public LDWyrmEntity(EntityType<? extends LDWyrmEntity> minutus, World world)
    {
        super(minutus, world);
    }

    @Override
    protected void initGoals()
    {
        goalSelector.add(1, new SwimGoal(this));
        goalSelector.add(2, new AvoidEntityGoal<>(this, LivingEntity.class, 6f, 0.8d, 1.2d, AVOIDING));
        goalSelector.add(3, new BurrowGoal());
        goalSelector.add(4, new WaterAvoidingRandomWalkingGoal(this, 1));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(SpawnEggItem.forEntity(getType()));
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        dataTracker.startTracking(BURROWED, false);
    }

    @Override
    public void writeCustomDataToTag(CompoundNBT compound)
    {
        super.writeCustomDataToTag(compound);
        compound.putBoolean(DATA_BURROWED, isBurrowed());
    }

    @Override
    public void readCustomDataFromTag(CompoundNBT compound)
    {
        super.readCustomDataFromTag(compound);
        setBurrowed(compound.getBoolean(DATA_BURROWED));
    }

    /**
     * Whether or not the Minutus is burrowed
     */
    public boolean isBurrowed()
    {
        return dataTracker.get(BURROWED);
    }

    public void setBurrowed(boolean burrow)
    {
        dataTracker.set(BURROWED, burrow);
    }

    // ================================

    @Override
    public void tickMovement()
    {
        super.tickMovement();

        if (isBurrowed())
        {
            if (world.getBlockState(getBlockPos().down(1)).getMaterial() != Material.AGGREGATE) setBurrowed(false);
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

        if (getAnimation() != NO_ANIMATION)
        {
            ++animationTick;
            if (animationTick >= animation.getDuration()) setAnimation(NO_ANIMATION);
        }
    }

    private void attackAbove()
    {
        Predicate<Entity> predicateFilter = filter ->
        {
            if (filter instanceof LDWyrmEntity) return false;
            return filter instanceof FishingBobberEntity || (filter instanceof LivingEntity && filter.getWidth() < 0.9f && filter.getHeight() < 0.9f);
        };
        AxisAlignedBB aabb = getBoundingBox().expand(0, 2, 0).expand(0.5, 0, 0.5);
        List<Entity> entities = world.getOtherEntities(this, aabb, predicateFilter);
        if (entities.isEmpty()) return;

        Optional<Entity> closest = entities.stream().min(Comparator.comparingDouble(entity -> entity.distanceTo(this)));
        Entity entity = closest.get();
        if (entity instanceof FishingBobberEntity)
        {
            entity.remove();
            setVelocity(0, 0.8, 0);
            setBurrowed(false);
        }
        else
        {
            if (getAnimation() != BITE_ANIMATION) setAnimation(BITE_ANIMATION);
            tryAttack(entity);
        }
    }

    @Override
    public ActionResultType interactMob(PlayerEntity player, Hand hand)
    {
        if (player.getStackInHand(hand).isEmpty())
        {
            if (!world.isClientSide)
            {
                ItemStack stack = new ItemStack(WRItems.LDWYRM.get());
                CompoundNBT tag = new CompoundNBT();
                CompoundNBT subTag = serializeNBT();
                tag.put(LDWyrmItem.DATA_CONTENTS, subTag);
                if (hasCustomName()) stack.setCustomName(getCustomName());
                stack.setTag(tag);
                InventoryHelper.spawn(world, getX(), getY(), getZ(), stack);
                remove();
            }
            return ActionResultType.success(world.isClientSide);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, IWorldReader world) // Attracted to sand
    {
        if (world.getBlockState(pos).getMaterial() == Material.AGGREGATE) return 10f;
        return super.getPathfindingFavor(pos, world);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer)
    {
        return true;
    }

    @Override
    public void checkDespawn()
    {
        if (isPersistent())
        {
            despawnCounter = 0;
            return;
        }

        switch (ForgeEventFactory.canEntityDespawn(this))
        {
            case DENY:
                despawnCounter = 0;
                return;
            case ALLOW:
                remove();
                return;
            default:
                break;
        }

        Entity player = world.getClosestPlayer(this, 32);
        if (player == null && (getRandom().nextDouble() < 0.0075 || !world.isDay())) remove();
        else despawnCounter = 0;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
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

//    @Nullable
//    @Override
//    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
//    {
////        return WRSounds.MINUTUS_SCREECH.get();
//    }

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
    public boolean collides()
    {
        return !isBurrowed();
    }

    @Override
    protected void pushAway(Entity entityIn)
    {
        if (!isBurrowed()) super.pushAway(entityIn);
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
        return new Animation[] {NO_ANIMATION, BITE_ANIMATION};
    }

    @Override
    public void setAnimation(Animation animation)
    {
        this.animation = animation;
        setAnimationTick(0);
    }

    public static <F extends MobEntity> boolean getSpawnPlacement(EntityType<F> fEntityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
    {
        if (reason == SpawnReason.SPAWNER) return true;
        Block block = world.getBlockState(pos.down()).getBlock();
        return block == Blocks.SAND && world.getBaseLightLevel(pos, 0) > 8;
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.DESERT)
            event.getSpawns().spawn(EntityClassification.AMBIENT, new MobSpawnInfo.Spawners(WREntities.LESSER_DESERTWYRM.get(), 11, 1, 3));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(GENERIC_MAX_HEALTH, 4)
                .add(GENERIC_MOVEMENT_SPEED, 0.4)
                .add(GENERIC_ATTACK_DAMAGE, 4);
    }

    class BurrowGoal extends Goal
    {
        private int burrowTicks = 30;

        public BurrowGoal()
        {
            setControls(EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean canStart()
        {
            return !isBurrowed() && belowIsSand();
        }

        @Override
        public void stop()
        {
            burrowTicks = 30;
        }

        @Override
        public void tick()
        {
            if (--burrowTicks <= 0)
            {
                setBurrowed(true);
                burrowTicks = 30;
            }
        }

        private boolean belowIsSand()
        {
            return world.getBlockState(getBlockPos().down(1)).getMaterial() == Material.AGGREGATE;
        }
    }
}
