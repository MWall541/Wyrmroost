package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by WolfShotz 7/10/19 - 22:18
 */
public class OWDrakeEntity extends AbstractDragonEntity
{
    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world) {
        super(drake, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!isSleeping() && !world.isDaytime() && !isAngry() && !hasPath()) setSleeping(true);
        else if (isSleeping() && world.isDaytime()) setSleeping(false);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) { return null; }



}
