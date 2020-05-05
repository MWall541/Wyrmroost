package WolfShotz.Wyrmroost.content.entities.dragon;

import WolfShotz.Wyrmroost.registry.*;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

import static net.minecraft.entity.SharedMonsterAttributes.*;

/**
 * Desertwyrm Dragon Entity
 * Seperated from AbstractDragonEntity:
 * This does not need/require much from that class and would instead create redundancies. do this instead.
 */
public class MinutusEntity extends AnimalEntity implements IAnimatedObject
{
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(MinutusEntity.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> AVOIDING = t -> EntityPredicates.CAN_AI_TARGET.test(t) && !(t instanceof MinutusEntity);

    public Animation animation = NO_ANIMATION;
    public int animationTick;
    public static final Animation BITE_ANIMATION = new Animation(10);

    public MinutusEntity(EntityType<? extends MinutusEntity> minutus, World world)
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
    
    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(4d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.4d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4d);
        
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
    
    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("burrowed", isBurrowed());
    }
    
    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        setBurrowed(compound.getBoolean("burrowed"));
    }
    
    /**
     * Whether or not the Minutus is burrowed
     */
    public boolean isBurrowed()
    {
        return dataManager.get(BURROWED);
    }
    
    public void setBurrowed(boolean burrow)
    {
        dataManager.set(BURROWED, burrow);
    }
    
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
        Predicate<Entity> predicateFilter = filter -> {
            if (filter instanceof MinutusEntity) return false;
            return filter instanceof FishingBobberEntity || (filter instanceof LivingEntity && filter.getSize(filter.getPose()).width < 0.9f && filter.getSize(filter.getPose()).height < 0.9f);
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
        } else
        {
            if (getAnimation() != BITE_ANIMATION) setAnimation(BITE_ANIMATION);
            attackEntityAsMob(entity);
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        if (hand != Hand.MAIN_HAND) return false;
        
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty())
        {
            CompoundNBT nbt = new CompoundNBT();
            ItemStack newDrop = new ItemStack(WRItems.MINUTUS.get());

            nbt.putBoolean("isalive", true);
            nbt.putString("entitytype", EntityType.getKey(getType()).toString());

            setBurrowed(false);
            writeWithoutTypeId(nbt);
            newDrop.setTag(nbt);
            if (hasCustomName()) newDrop.setDisplayName(getCustomName());
            ItemEntity drop = new ItemEntity(world, getPosX(), getPosY() + 0.5d, getPosZ(), newDrop);
            double d0 = player.getPosX() - getPosX();
            double d1 = player.getPosY() - getPosY();
            double d2 = player.getPosZ() - getPosZ();
            drop.setMotion(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);

            world.addEntity(drop);
            remove();

            return true;
        }
//        // Easter egg ;) // Second note: Other devs got mad at me I cant keep this ;~; cri
//        if (stack.getItem() == Items.BUCKET) {
//            world.playSound(null, player.getPosition(), SoundEvents.ENTITY_COW_MILK, SoundCategory.NEUTRAL, 1f, 1f);
//            stack.shrink(1);
//            player.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET));
//
//            return true;
//        }
        
        return super.processInteract(player, hand);
    }
    
    @Override
    public boolean canDespawn(double distanceToClosestPlayer)
    {
        return true;
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
        return WRSounds.MINUTUS_IDLE.get();
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
        return 0.3f;
    }

    @Override
    public boolean canBePushed()
    {
        return !isBurrowed();
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !isBurrowed();
    }
    
    @Override
    protected void collideWithEntity(Entity entityIn)
    {
        if (!isBurrowed()) super.collideWithEntity(entityIn);
    }
    
    @Override
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked() || isBurrowed();
    }
    
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity)
    {
        return null;
    }
    
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
        return new Animation[]{NO_ANIMATION, BITE_ANIMATION};
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
