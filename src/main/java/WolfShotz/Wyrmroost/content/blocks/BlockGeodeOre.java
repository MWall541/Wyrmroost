package WolfShotz.Wyrmroost.content.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import java.util.Random;

class BlockGeodeOre extends BlockBase
{
    BlockGeodeOre(String name) { super(name, ItemGroup.BUILDING_BLOCKS, Material.ROCK, ToolType.PICKAXE, 2, 3, SoundType.STONE); }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) { return silktouch == 0 ? MathHelper.nextInt(new Random(), 3, 7) : 0; }
}
