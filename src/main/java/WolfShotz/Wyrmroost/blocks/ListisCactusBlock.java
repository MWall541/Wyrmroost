package WolfShotz.Wyrmroost.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;

public class ListisCactusBlock extends CactusBlock
{
    public ListisCactusBlock(Block.Properties props) { super(props); }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
//        return plantable.getPlant(world, pos).getBlock() == WRBlocks.LISTIS_CACTUS.get();
        return true;
    }
}
