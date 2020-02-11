package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.WalkAndSwimNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ButterflyNavigator extends SwimmerPathNavigator
{
    public ButterflyNavigator(MobEntity entitylivingIn, World worldIn)
    {
        super(entitylivingIn, worldIn);
    }
    
    @Override
    protected PathFinder getPathFinder(int pathSearchRange)
    {
        return new PathFinder(new WalkAndSwimNodeProcessor(), pathSearchRange);
    }
    
    @Override
    public boolean canEntityStandOnPos(BlockPos pos) { return !world.getBlockState(pos).isAir(world, pos); }

    @Override
    protected boolean canNavigate() { return true; }
}
