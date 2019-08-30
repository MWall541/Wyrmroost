package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.util.utils.NetworkUtils;
import WolfShotz.Wyrmroost.util.utils.ReflectionUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.LivingEntity;
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
    private RoostStalkerEntity dragon;
    private ServerWorld world;
    private Animation animation;
    private IInventory chest;
    private int searchDelay = 20 + new Random().nextInt(40) + 5;
    
    
    public ScavengeGoal(RoostStalkerEntity dragon, double speed, Animation animation) {
        super(dragon, speed, 16);
        this.dragon = dragon;
        this.world = (ServerWorld) dragon.world;
        this.animation = animation;
    }
    
    public ScavengeGoal(RoostStalkerEntity dragon, double speed) { this(dragon, speed, NO_ANIMATION); }
    
    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !dragon.isTamed() && isHandEmpty(dragon); }
    
    @Override
    public void startExecuting() {
        chest = getInventoryAtPosition(world, destinationBlock);
    
        super.startExecuting();
    }
    
    @Override
    public boolean shouldContinueExecuting() { return super.shouldContinueExecuting() && isHandEmpty(dragon) && !chest.isEmpty(); }
    
    @Override
    public void tick() {
        super.tick();
        
        if (getIsAboveDestination()) {
            if (!isHandEmpty(dragon)) return;
    
            if (dragon.getAnimation() != animation) NetworkUtils.sendAnimationPacket(dragon, animation);
            
            if (chest instanceof ChestTileEntity && ReflectionUtils.getChestPlayersUsing((ChestTileEntity) chest) == 0)
                interactChest(chest, true);
            
            if (!chest.isEmpty() && --searchDelay <= 0) {
                int index = new Random().nextInt(chest.getSizeInventory());
                ItemStack stack = chest.getStackInSlot(index);
    
                if (!stack.isEmpty() && dragon.canPickUpStack(stack)) {
                    chest.removeStackFromSlot(index);
                    dragon.setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
                }
            }
        }
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        interactChest(chest, false);
        searchDelay = 20 + new Random().nextInt(40) + 5;
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
    
    /**
     * Return true to set given position as destination
     */
    @Override
    protected boolean shouldMoveTo(IWorldReader world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof IInventory;
    }
    
    /**
     * Used to handle the chest opening animation when being used by the scavenger
     */
    private void interactChest(IInventory intentory, boolean open) {
        if (!(intentory instanceof ChestTileEntity)) return; // not a chest, ignore it
        ChestTileEntity chest = (ChestTileEntity) intentory;
        
        ReflectionUtils.setChestPlayersUsing(chest, open? 1 : -1, true);
        chest.getWorld().addBlockEvent(chest.getPos(), chest.getBlockState().getBlock(), 1, ReflectionUtils.getChestPlayersUsing(chest));
    }
    
    private boolean isHandEmpty(LivingEntity entity) { return entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND) == ItemStack.EMPTY; }
}
