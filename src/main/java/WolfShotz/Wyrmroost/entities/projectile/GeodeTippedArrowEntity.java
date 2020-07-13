package WolfShotz.Wyrmroost.entities.projectile;

import WolfShotz.Wyrmroost.items.GeodeTippedArrowItem;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class GeodeTippedArrowEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData
{
    private final GeodeTippedArrowItem item;

    public GeodeTippedArrowEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn)
    {
        super(type, worldIn);
        this.item = (GeodeTippedArrowItem) WRItems.BLUE_GEODE_ARROW.get();
    }

    public GeodeTippedArrowEntity(World worldIn, double damage, Item item)
    {
        super(WREntities.GEODE_TIPPED_ARROW.get(), worldIn);
        setDamage(damage);
        this.item = (GeodeTippedArrowItem) item;
    }

    public GeodeTippedArrowItem getItem() { return item; }

    @Override
    protected ItemStack getArrowStack() { return new ItemStack(item); }

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    public void writeSpawnData(PacketBuffer buf)
    {
        Entity shooter = getShooter();
        buf.writeInt(shooter == null? 0 : shooter.getEntityId());
    }

    @Override
    public void readSpawnData(PacketBuffer buf)
    {
        Entity shooter = world.getEntityByID(buf.readInt());
        if (shooter != null) setShooter(shooter);
    }
}
