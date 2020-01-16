package WolfShotz.Wyrmroost.util.entityutils.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlightPathNavigator extends FlyingPathNavigator
{
    public FlightPathNavigator(AbstractDragonEntity dragon, World world)
    {
        super(dragon, world);
    }
    
    @Override
    public boolean canEntityStandOnPos(BlockPos pos)
    {
        return world.isAirBlock(pos.down());
    }
}
