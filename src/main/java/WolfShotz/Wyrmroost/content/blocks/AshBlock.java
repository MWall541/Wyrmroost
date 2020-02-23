package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class AshBlock extends SandBlock
{
    public AshBlock()
    {
        super(0x575757, ModUtils.blockBuilder(Material.SAND).hardnessAndResistance(0.5f));
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return plantable.getPlantType(world, pos).equals(PlantType.Desert);
    }
}
