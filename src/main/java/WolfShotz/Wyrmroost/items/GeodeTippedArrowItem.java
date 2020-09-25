package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GeodeTippedArrowItem extends ArrowItem
{
    private final double damage;

    public GeodeTippedArrowItem(double damage)
    {
        super(WRItems.builder());
        this.damage = damage;
    }

    @Override
    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter)
    {
        GeodeTippedArrowEntity arrow = new GeodeTippedArrowEntity(world, this);
        arrow.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1d, shooter.getPosZ());
        arrow.setShooter(shooter);
        arrow.setDamage(damage);
        return arrow;
    }
}
