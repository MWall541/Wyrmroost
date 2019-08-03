package WolfShotz.Wyrmroost.content.entities.minutus;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.minutus.goals.BurrowGoal;
import WolfShotz.Wyrmroost.content.entities.minutus.goals.RunAwayGoal;
import WolfShotz.Wyrmroost.content.entities.minutus.goals.WalkRandom;
import WolfShotz.Wyrmroost.event.SetupItem;
import WolfShotz.Wyrmroost.event.SetupSound;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class MinutusEntity extends AbstractDragonEntity
{
    public static final Animation BITE_ANIMATION = Animation.create(10);

    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(MinutusEntity.class, DataSerializers.BOOLEAN);


    public MinutusEntity(EntityType<? extends MinutusEntity> minutus, World world) {
        super(minutus, world);

        moveController = new MovementController(this);

        setImmune(DamageSource.IN_WALL);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new RunAwayGoal<>(this, LivingEntity.class));
        goalSelector.addGoal(3, new BurrowGoal(this));
        goalSelector.addGoal(4, new WalkRandom(this));

    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(4d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.4d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4d);

    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(BURROWED, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Burrowed", isBurrowed());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setBurrowed(compound.getBoolean("Burrowed"));
    }

    /**
     * Whether or not the Minutus is burrowed
     */
    public boolean isBurrowed() { return dataManager.get(BURROWED); }
    public void setBurrowed(boolean burrow) { dataManager.set(BURROWED, burrow); }

    @Override
    public boolean canFly() { return false; }

    /**
     * Set The chances this dragon can be an albino.
     * Set it to 0 to have no chance
     */
    @Override
    public int getAlbinoChances() { return 0; }

    // ================================

    @Override
    public void livingTick() {
        super.livingTick();
        if (isBurrowed()) {
            if (world.getBlockState(getPosition().down(1)).getMaterial() != Material.SAND) setBurrowed(false);
            attackAbove();
        }
    }

    private void attackAbove() {
        Predicate<Entity> predicateFilter = filter -> {
            if (filter instanceof MinutusEntity) return false;
            return filter instanceof FishingBobberEntity || (filter instanceof LivingEntity && filter.getSize(filter.getPose()).width < 0.9f && filter.getSize(filter.getPose()).height < 0.9f);
        };
        AxisAlignedBB aabb = getBoundingBox().expand(0, 2, 0).grow(0.5, 0, 0.5);
        List<Entity> entities = world.getEntitiesInAABBexcluding(this, aabb, predicateFilter);
        if (entities.isEmpty()) return;

        Optional<Entity> closest = entities.stream().min((entity1, entity2) -> Float.compare(entity1.getDistance(this), entity2.getDistance(this)));
        Entity entity = closest.get();
        if (entity instanceof FishingBobberEntity) {
            entity.remove();
            setMotion(0, 0.8, 0);
            setBurrowed(false);
        }
        else {
            if (getAnimation()!= MinutusEntity.BITE_ANIMATION) setAnimation(MinutusEntity.BITE_ANIMATION);
            attackEntityAsMob(entity);
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            CompoundNBT nbt = new CompoundNBT();
            ItemStack newDrop = new ItemStack(SetupItem.itemminutus);

            nbt.putBoolean("isalive", true);
            nbt.putString("entitytype", EntityType.getKey(getType()).toString());

            setBurrowed(false);
            writeAdditional(nbt);
            newDrop.setTag(nbt);
            ItemEntity drop = new ItemEntity(world, posX, posY + 0.5d, posZ, newDrop);
            double d0 = player.posX - posX;
            double d1 = player.posY - posY;
            double d2 = player.posZ - posZ;
            drop.setMotion(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);

            world.addEntity(drop);
            remove();

            return true;
        }

        return super.processInteract(player, hand);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SetupSound.MINUTUS_IDLE; }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SetupSound.MINUTUS_SCREECH; }

    /** Array Containing all of the dragons food items */
    @Override
    public Item[] getFoodItems() { return new Item[0]; } // Doesnt eat :P

    @Override
    public boolean canBePushed() { return !isBurrowed(); }

    @Override
    public boolean canBeCollidedWith() { return !isBurrowed(); }

    @Override
    protected void collideWithEntity(Entity entityIn) { if (!isBurrowed()) super.collideWithEntity(entityIn); }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) { return null; }

    // ================================
    //        Entity Animation
    // ================================
    @Override
    public Animation[] getAnimations() {
        return new Animation[] {BITE_ANIMATION};
    }

    // ================================
}
