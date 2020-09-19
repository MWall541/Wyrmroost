package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.TickFloat;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import javax.annotation.Nullable;
import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class AlpineEntity extends AbstractDragonEntity
{
    public static final Animation ROAR_ANIMATION = new Animation(84);

    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);

    public AlpineEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(40d); // 20 hearts
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.22d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(1); // no knockback
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(3d); // 1.5 hearts
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.15);
        getAttributes().registerAttribute(PROJECTILE_DAMAGE).setBaseValue(1d); // 0.5 hearts
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.1d, true));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.1, 13, 5));
        goalSelector.addGoal(7, new DragonBreedGoal(this, 4));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 10));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, BeeEntity.class, true, false, bee -> ((BeeEntity) bee).hasNectar()));
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(FLYING, false);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        sitTimer.add(isSitting() || isSleeping()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.1f : -0.1f);
        flightTimer.add(isFlying()? 0.1f : -0.05f);

        if (!world.isRemote && noActiveAnimation() && !isSleeping() && !isChild() && getRNG().nextDouble() < 0.005)
            AnimationPacket.send(this, ROAR_ANIMATION);

        if (getAnimation() == ROAR_ANIMATION)
        {
            int tick = getAnimationTick();
            switch (tick)
            {
                case 0:
                    playSound(WRSounds.ENTITY_ALPINE_ROAR.get(), 3f, 1f);
                    break;
                case 25:
                    for (LivingEntity entity : getEntitiesNearby(20, e -> e.getType() == WREntities.ALPINE.get() && ((AlpineEntity) e).noActiveAnimation()))
                        ((AlpineEntity) entity).setAnimation(ROAR_ANIMATION);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public boolean attackEntityAsMob(Entity enemy)
    {
        boolean flag = super.attackEntityAsMob(enemy);

        if (!isTamed() && flag && !enemy.isAlive() && enemy.getType() == EntityType.BEE)
        {
            BeeEntity bee = (BeeEntity) enemy;
            if (bee.hasNectar() && bee.getLeashed())
            {
                Entity holder = bee.getLeashHolder();
                if (holder instanceof PlayerEntity) tame(true, (PlayerEntity) holder);
            }
        }
        return flag;
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().movePosition(-5d, 0.75d, 0);
        else event.getInfo().movePosition(-3, 0.3, 0);
    }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(6); }

    @Override
    protected boolean canBeRidden(Entity entity) { return entity instanceof LivingEntity && isOwner((LivingEntity) entity); }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return ImmutableSet.of(Items.HONEYCOMB, Items.HONEY_BOTTLE); }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return sizeIn.height * 1.25f; }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_ALPINE_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.ENTITY_ALPINE_ROAR.get(); }

    @Override
    public Animation[] getAnimations() { return new Animation[] {ROAR_ANIMATION}; }
}
