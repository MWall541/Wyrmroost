package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class CanariLeavesBlock extends LeavesBlock
{
    public static final BooleanProperty BERRIES = BooleanProperty.create("berries");

    public CanariLeavesBlock()
    {
        super(ModUtils.blockBuilder(Material.LEAVES).hardnessAndResistance(0.2f).sound(SoundType.PLANT).tickRandomly());
    }

    @Override
    public boolean ticksRandomly(BlockState state) { return true; }

    @Override
    public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        super.randomTick(state, worldIn, pos, random);

        if (!state.get(BERRIES) && state.get(DISTANCE) < 7)
            worldIn.setBlockState(pos, state.cycle(BERRIES));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!state.get(BERRIES)) return false;
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(WRItems.CANARI_CHERRY.get()));
        worldIn.setBlockState(pos, state.cycle(BERRIES));
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(BERRIES);
    }
}
