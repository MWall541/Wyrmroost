package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;

import java.util.Collection;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class FogWraithEntity extends AbstractDragonEntity
{
    private static final DataParameter<Boolean> STEALTH = EntityDataManager.createKey(FogWraithEntity.class, DataSerializers.BOOLEAN);

    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat stealthTimer = new TickFloat().setLimit(0, 1);

    public FogWraithEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("IsStealth", EntityDataEntry.BOOLEAN, STEALTH, false);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(9, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));
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
        flightTimer.add(isFlying()? 0.1f : -0.1f);
        stealthTimer.add(isInvisible()? 0.1f : -0.1f);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * 1.3f;
    }

    @Override
    public Collection<? extends IItemProvider> getFoodItems()
    {
        return null;
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 100)
                .createMutableAttribute(MOVEMENT_SPEED, 0.22)
                .createMutableAttribute(KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(FOLLOW_RANGE, 60)
                .createMutableAttribute(ATTACK_KNOCKBACK, 2.25)
                .createMutableAttribute(ATTACK_DAMAGE, 10)
                .createMutableAttribute(FLYING_SPEED, 0.27)
                .createMutableAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 4);
    }
}
