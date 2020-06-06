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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoyalRedEntity extends AbstractDragonEntity
{
    public final TickFloat flightTime;
    public final TickFloat sitTime;

    public RoyalRedEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        this.flightTime = new TickFloat().setLimit(0, 1);
        this.sitTime = new TickFloat(isSitting()? 1 : 0).setLimit(0, 1);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
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
        flightTime.add(isFlying()? 0.1f : -0.05f);
        sitTime.add(isSitting()? 0.1f : -0.1f);
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        float speed = isFlying()? (float) getAttribute(FLYING_SPEED).getValue() : (float) getAttribute(MOVEMENT_SPEED).getValue() * 0.5f;

        if (canPassengerSteer())
        {
            LivingEntity entity = (LivingEntity) getControllingPassenger();
            rotationYawHead = entity.rotationYawHead;
            rotationPitch = entity.rotationPitch * 0.5f;
            if (entity.isJumping) setFlying(true);
            double yMot = vec3d.y;
            if (isFlying())
            {
                if (entity.moveForward != 0) yMot = entity.getLookVec().y * speed * 3.5;
                else yMot = MathHelper.cos(ticksExisted * 0.25f) * 0.25f;
            }
            setAIMoveSpeed(speed);
            vec3d = new Vec3d(vec3d.x, yMot, entity.moveForward);
        }

        if (isFlying())
        {
            moveRelative(speed, vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.91f));

            return;
        }

        super.travel(vec3d);
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        if (isOwner(player))
        {
            if (player.isSneaking())
            {
                setSit(!isSitting());
                return true;
            }

            player.startRiding(this);
            return true;
        }

        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) { return isTamed(); }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return getHeight() * 0.8f; }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = super.getSize(poseIn);
        if (isSitting() || isSleeping()) size = size.scale(1, 0.5f);
        return size;
    }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 8; }

    @Override
    public Collection<Item> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }

    @Override
    public DragonEggProperties createEggProperties() { return new DragonEggProperties(0.6f, 1f, 72000); }
}
