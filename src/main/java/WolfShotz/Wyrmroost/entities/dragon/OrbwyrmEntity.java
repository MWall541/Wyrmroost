package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.registry.WREntities;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;

import java.util.Collection;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class OrbwyrmEntity extends AbstractDragonEntity
{
    public OrbwyrmEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, false);
    }

    @Override
    public Collection<? extends IItemProvider> getFoodItems()
    {
        return null;
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        return EntitySize.flexible(2.8f, 3.2f);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * 1.17f;
    }

    @Override
    public float getRenderScale() { return isChild()? 0.3f : isMale()? 0.75f : 1f; }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 40)
                .createMutableAttribute(MOVEMENT_SPEED, 0.22)
                .createMutableAttribute(KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(ATTACK_DAMAGE, 3)
                .createMutableAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 1);
    }
}
