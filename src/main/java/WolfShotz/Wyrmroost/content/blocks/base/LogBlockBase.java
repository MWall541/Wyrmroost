package WolfShotz.Wyrmroost.content.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class LogBlockBase extends LogBlock
{
    public final Supplier<Block> strippedLog;

    public LogBlockBase(MaterialColor verticalColorIn, Supplier<Block> strippedLog, Properties properties)
    {
        super(verticalColorIn, properties);
        this.strippedLog = strippedLog;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        Item item = player.getHeldItem(handIn).getItem();
        if (item instanceof AxeItem)
        {
            worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1f, 1f);
            if (!worldIn.isRemote)
            {
                worldIn.setBlockState(pos, strippedLog.get().getDefaultState().with(RotatedPillarBlock.AXIS, worldIn.getBlockState(pos).get(RotatedPillarBlock.AXIS)), 11);
                player.getHeldItem(handIn).damageItem(1, player, playerConsumer -> playerConsumer.sendBreakAnimation(handIn));
            }
            return true;
        }

        return false;
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
    {
        super.onBlockClicked(state, worldIn, pos, player);
    }
}
