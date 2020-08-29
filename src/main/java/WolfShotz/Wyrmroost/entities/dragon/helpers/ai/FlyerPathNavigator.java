package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.math.BlockPos;

public class FlyerPathNavigator extends FlyingPathNavigator
{
    public FlyerPathNavigator(MobEntity entity)
    {
        super(entity, entity.world);
    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos) { return true; }
}
