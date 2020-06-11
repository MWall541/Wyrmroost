package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.util.Animation;
import WolfShotz.Wyrmroost.entities.util.IAnimatedEntity;
import WolfShotz.Wyrmroost.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.items.LDWyrmItem;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static net.minecraft.entity.SharedMonsterAttributes.*;

/**
 * Desertwyrm Dragon Entity
 * Seperated from AbstractDragonEntity:
 * This does not need/require much from that class and would instead create redundancies. do this instead.
 */
public class LDWyrmEntity extends AnimalEntity implements IAnimatedEntity
{
    public static final String DATA_BURROWED = "Burrowed";
    public static final Animation BITE_ANIMATION = new Animation(10);
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(LDWyrmEntity.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> AVOIDING = t -> EntityPredicates.CAN_AI_TARGET.test(t) && !(t instanceof LDWyrmEntity);

    public Animation animation = NO_ANIMATION;
    public int animationTick;

    public LDWyrmEntity(EntityType<? extends LDWyrmEntity> minutus, World world)
    {
        super(minutus, world);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new AvoidEntityGoal<>(this, LivingEntity.class, 6f, 0.8d, 1.2d, AVOIDING));
        goalSelector.addGoal(3, new BurrowGoal());
        goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1));
    }

    public static void setSpawnConditions()
    {
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY)
                .stream()
                .filter(b -> !BiomeDictionary.getTypes(b).containsAll(ImmutableList.of(BiomeDictionary.Type.MESA, BiomeDictionary.Type.BEACH)))
                .forEach(b -> b.getSpawns(EntityClassification.AMBIENT).add(new Biome.SpawnListEntry(WREntities.LESSER_DESERTWYRM.get(), 14, 3, 6)));
        EntitySpawnPlacementRegistry.register(WREntities.LESSER_DESERTWYRM.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                (wyrm, world, reason, pos, rng) ->
                {
                    if (reason == SpawnReason.SPAWNER) return true;
                    Block block = world.getBlockState(pos.down()).getBlock();
                    return block == Blocks.SAND && world.getLightSubtracted(pos, 0) > 8;
                });
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(BURROWED, false);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(4d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.4d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4d);

    }

    /**
     * Save Game
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean(DATA_BURROWED, isBurrowed());
    }

    /**
     * Load Game
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        setBurrowed(compound.getBoolean(DATA_BURROWED));
    }

    /**
     * Whether or not the Minutus is burrowed
     */
    public boolean isBurrowed() { return dataManager.get(BURROWED); }

    public void setBurrowed(boolean burrow) { dataManager.set(BURROWED, burrow); }

    // ================================

    @Override
    public void livingTick()
    {
        super.livingTick();

        if (isBurrowed())
        {
            if (world.getBlockState(getPosition().down(1)).getMaterial() != Material.SAND) setBurrowed(false);
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
        AxisAlignedBB aabb = getBoundingBox().expand(0, 2, 0).grow(0.5, 0, 0.5);
        List<Entity> entities = world.getEntitiesInAABBexcluding(this, aabb, predicateFilter);
        if (entities.isEmpty()) return;

        Optional<Entity> closest = entities.stream().min((entity1, entity2) -> Float.compare(entity1.getDistance(this), entity2.getDistance(this)));
        Entity entity = closest.get();
        if (entity instanceof FishingBobberEntity)
        {
            entity.remove();
            setMotion(0, 0.8, 0);
            setBurrowed(false);
        }
        else
        {
            if (getAnimation() != BITE_ANIMATION) setAnimation(BITE_ANIMATION);
            attackEntityAsMob(entity);
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        if (player.getHeldItem(hand).isEmpty() && !world.isRemote)
        {
            ItemStack stack = new ItemStack(WRItems.LDWYRM.get());
            CompoundNBT tag = new CompoundNBT();
            CompoundNBT subTag = serializeNBT();
            tag.put(LDWyrmItem.DATA_CONTENTS, subTag);
            if (hasCustomName()) stack.setDisplayName(getCustomName());
            stack.setTag(tag);
            InventoryHelper.spawnItemStack(world, getPosX(), getPosY(), getPosZ(), stack);
            remove();

            return true;
        }

        return super.processInteract(player, hand);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos, IWorldReader world) // Attracted to sand
    {
        if (world.getBlockState(pos).getMaterial() == Material.SAND) return 10f;
        return super.getBlockPathWeight(pos, world);
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) { return !world.isDaytime(); }

    @Override
    public void checkDespawn()
    {
        if (isNoDespawnRequired())
        {
            idleTime = 0;
            return;
        }

        Entity player = world.getClosestPlayer(this, -1d);
        Event.Result result = ForgeEventFactory.canEntityDespawn(this);
        if (result == Event.Result.DENY)
        {
            idleTime = 0;
            return;
        }
        else if (result == Event.Result.ALLOW)
        {
            remove();
            return;
        }

        if (player != null)
        {
            double distanceSq = player.getDistanceSq(this);
            if (distanceSq > 1024 && getRNG().nextInt(500) == 0 && canDespawn(distanceSq)) remove();
            else if (distanceSq < 1024.0D) idleTime = 0;
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        Optional<CustomSpawnEggItem> egg = CustomSpawnEggItem.EGG_TYPES.stream().filter(e -> getType().equals(e.type.get())).findFirst();
        return egg.map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || source == DamageSource.IN_WALL;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_LDWYRM_IDLE.get(); }

//    @Nullable
//    @Override
//    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
//    {
////        return WRSounds.MINUTUS_SCREECH.get();
//    }

    @Override
    protected float getSoundVolume() { return 0.3f; }

    @Override
    public boolean canBePushed() { return !isBurrowed(); }

    @Override
    public boolean canBeCollidedWith() { return !isBurrowed(); }

    @Override
    protected void collideWithEntity(Entity entityIn) { if (!isBurrowed()) super.collideWithEntity(entityIn); }

    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || isBurrowed(); }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) { return null; }

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

    // ================================

    class BurrowGoal extends Goal
    {
        private int burrowTicks = 30;

        public BurrowGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute()
        {
            return !isBurrowed() && belowIsSand();
        }

        @Override
        public void resetTask() { burrowTicks = 30; }

        @Override
        public void tick()
        {
            if (--burrowTicks <= 0)
            {
                setBurrowed(true);
                burrowTicks = 30;
            }
        }

        private boolean belowIsSand() { return world.getBlockState(getPosition().down(1)).getMaterial() == Material.SAND; }
    }
}
