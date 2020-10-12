package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;

import java.util.Collection;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class FogWraithEntity extends AbstractDragonEntity
{
    public final TickFloat flightTimer = new TickFloat(1).setLimit(0, 1);

    public FogWraithEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);
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
