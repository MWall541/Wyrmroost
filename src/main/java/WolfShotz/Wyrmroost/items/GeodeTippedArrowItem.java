package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class GeodeTippedArrowItem extends ArrowItem
{
    private final Supplier<EntityType<GeodeTippedArrowEntity>> arrowType;

    public GeodeTippedArrowItem(RegistryObject<EntityType<GeodeTippedArrowEntity>> arrowType)
    {
        super(ModUtils.itemBuilder());
        this.arrowType = arrowType;
    }

    @Override
    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter)
    {
        GeodeTippedArrowEntity arrow = arrowType.get().create(world);
        arrow.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1d, shooter.getPosZ());
        arrow.setShooter(shooter);
        return arrow;
    }
}
