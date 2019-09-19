package WolfShotz.Wyrmroost.content.blocks.base;

import WolfShotz.Wyrmroost.event.SetupBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

/**
 * Blockbase - Helper Class allowing for easier block registration
 */
public class BlockBase extends Block
{
    private boolean isBeaconBase;
    private IXPSupplier xpAmount;
    
    
    public BlockBase(String name, Block.Properties properties) {
        super(properties);

        setRegistryName(name);
        SetupBlocks.BLOCKS.add(this);
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) { return isBeaconBase; }
    
    public BlockBase setBeaconBase(boolean flag) {
        this.isBeaconBase = flag;
        
        return this;
    }
    
    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return xpAmount == null? 0 : xpAmount.xpAmount(state, world, pos, fortune, silktouch);
    }
    
    public BlockBase setXPDrops(IXPSupplier supplier) {
        this.xpAmount = supplier;
        
        return this;
    }
    
    public BlockBase setXPDrops(int amount) {
        this.xpAmount = (s, w, p, f, si) -> amount;
        
        return this;
    }
    
    @FunctionalInterface
    public interface IXPSupplier
    {
        int xpAmount(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch);
    }
}