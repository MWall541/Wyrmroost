package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
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
        super(ModUtils.itemBuilder());
        this.damage = damage;
    }

    @Override
    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter)
    {
        GeodeTippedArrowEntity arrow = new GeodeTippedArrowEntity(world, damage, this);
        arrow.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1d, shooter.getPosZ());
        arrow.setShooter(shooter);
        return arrow;
    }
}
