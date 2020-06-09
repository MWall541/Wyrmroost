package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.ControlledAttackGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoyalRedEntity extends AbstractDragonEntity
{
    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);

    public RoyalRedEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());

        // because itll act like its doing squats when we re-render if we didnt.
        sitTimer.set(isSitting()? 1 : 0);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(100d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.22d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(10);
        getAttribute(FOLLOW_RANGE).setBaseValue(20d);
        getAttribute(ATTACK_KNOCKBACK).setBaseValue(2.25d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(10d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.115d);
        getAttributes().registerAttribute(PROJECTILE_DAMAGE).setBaseValue(3d);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new ControlledAttackGoal(this, 1, true, 2.1, d ->
        {}));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.2d, 12f, 3f));
        goalSelector.addGoal(7, new DragonBreedGoal(this, true));
        goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(10, CommonGoalWrappers.lookAt(this, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, LivingEntity.class, false, true, e -> e instanceof PlayerEntity || e instanceof AnimalEntity));
        targetSelector.addGoal(4, new HurtByTargetGoal(this)
        {
            @Override
            public boolean shouldExecute() { return super.shouldExecute() && !isChild(); }
        });
    }

    @Override
    public void livingTick()
    {
        super.livingTick();
        flightTimer.add(isFlying()? 0.1f : -0.05f);
        sitTimer.add(isSitting()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.035f : -0.1f);
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        if (isOwner(player) && !isChild() && player.startRiding(this))
        {
            setSit(false);
            return true;
        }

        return false;
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSleeping()) return size.scale(1, 0.5f);
        if (isSitting()) return size.scale(1, 0.9f);
        return size;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) { return isTamed(); }

    @Override
    protected boolean canFitPassenger(Entity passenger) { return getPassengers().size() < 2; }

    @Override
    public Vec3d getPassengerPosOffset(Entity entity, int index) { return new Vec3d(0, getHeight() * 0.75f, index == 0? 0 : -1); }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return getHeight() * 0.8f; }

    @Override
    public float getRenderScale() { return isChild()? 0.5f : isMale()? 0.8f : 1f; }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 8; }

    @Override
    public int getSpecialChances() { return 0; }

    @Override
    public Collection<Item> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }

    @Override
    public DragonEggProperties createEggProperties() { return new DragonEggProperties(0.6f, 1f, 72000); }
}
