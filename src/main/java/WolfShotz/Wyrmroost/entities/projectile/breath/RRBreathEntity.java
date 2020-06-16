package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.world.World;

public class RRBreathEntity extends FireBreathEntity
{
    public RRBreathEntity(EntityType<? extends DamagingProjectileEntity> type, World world) { super(type, world); }

    public RRBreathEntity(AbstractDragonEntity shooter) { super(WREntities.ROYAL_RED_BREATH.get(), shooter); }

//    @Override
//    protected boolean isFireballFiery() { return false; }
}
