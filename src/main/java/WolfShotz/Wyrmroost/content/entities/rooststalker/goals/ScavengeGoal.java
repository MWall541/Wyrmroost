package WolfShotz.Wyrmroost.content.entities.rooststalker.goals;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.NetworkUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

import static com.github.alexthe666.citadel.animation.IAnimatedEntity.NO_ANIMATION;

public class ScavengeGoal extends MoveToBlockGoal
{
    private AbstractDragonEntity dragon;
    private ServerWorld world;
    private Animation animation;
    private IInventory chest;
    private boolean markScavenged;
    
    
    public ScavengeGoal(AbstractDragonEntity dragon, double speed, Animation animation) {
        super(dragon, speed, 8);
        this.dragon = dragon;
        this.world = (ServerWorld) dragon.world;
        this.animation = animation;
    }
    
    public ScavengeGoal(AbstractDragonEntity dragon, double speed) { this(dragon, speed, NO_ANIMATION); }
    
    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !dragon.isTamed() && dragon.getItemStackFromSlot(EquipmentSlotType.MAINHAND) == ItemStack.EMPTY;
    }
    
    @Override
    public void startExecuting() {
        chest = getInventoryAtPosition(world, destinationBlock);
        markScavenged = false;
        
        super.startExecuting();
    }
    
    @Override
    public boolean shouldContinueExecuting() { return super.shouldContinueExecuting() && !markScavenged; }
    
    @Override
    public void tick() {
        super.tick();
        
        if (getIsAboveDestination() && world.getTileEntity(destinationBlock) instanceof ChestTileEntity && !markScavenged) {
            if (dragon.getItemStackFromSlot(EquipmentSlotType.MAINHAND) != ItemStack.EMPTY) return;
            
            if (markScavenged) return;
            
            if (ChestTileEntity.getPlayersUsing(world, destinationBlock) == 0) {
                if (animation != NO_ANIMATION) NetworkUtils.sendAnimationPacket(dragon, animation);
//                chest.openInventory(roostStalker);
            }
            
            if (!chest.isEmpty()) {
                int index = new Random().nextInt(chest.getSizeInventory());
                ItemStack stack = chest.getStackInSlot(index);
    
                if (!stack.isEmpty()) {
                    chest.removeStackFromSlot(index);
                    dragon.setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
                    markScavenged = true;
                }
            }
        }
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        markScavenged = false;
    }
    
    /**
     * Return true to set given position as destination
     *
     * @param worldIn
     * @param pos
     */
    @Override
    protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos.up())) return false;
        
        BlockState blockstate = worldIn.getBlockState(pos);
        Block block = blockstate.getBlock();
        
        if (block instanceof ChestBlock) return ChestTileEntity.getPlayersUsing(worldIn, pos) < 1;
        return false;
    }
    
    /**
     * Returns the IInventory (if applicable) of the TileEntity at the specified position
     */
    @Nullable
    public static IInventory getInventoryAtPosition(World worldIn, BlockPos blockpos) {
        IInventory iinventory = null;
        BlockState blockstate = worldIn.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        if (blockstate.hasTileEntity()) {
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof IInventory) {
                iinventory = (IInventory)tileentity;
                if (iinventory instanceof ChestTileEntity && block instanceof ChestBlock) {
                    iinventory = ChestBlock.getInventory(blockstate, worldIn, blockpos, true);
                }
                else iinventory = null;
            }
        }
    
        return iinventory;
    }
}
