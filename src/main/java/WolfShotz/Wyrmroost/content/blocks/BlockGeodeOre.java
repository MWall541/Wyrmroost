package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockGeodeOre extends BlockBase
{
    public BlockGeodeOre() { super("geode_ore", Material.ROCK, ToolType.PICKAXE, 2, 3, SoundType.STONE); }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) { return silktouch == 0 ? MathHelper.nextInt(new Random(), 3, 7) : 0; }
}
