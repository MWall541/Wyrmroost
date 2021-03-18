package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
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
        GeodeTippedArrowEntity arrow = new GeodeTippedArrowEntity(level, this);
        arrow.updatePosition(shooter.getX(), shooter.getEyeY() - 0.1d, shooter.getZ());
        arrow.setDamage(damage);
        return arrow;
    }
}
