package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.content.tileentities.teegg.EggTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEgg extends BlockBase
{
    private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    private EggTileEntity te = new EggTileEntity();
    
    public BlockEgg() {
        super("egg", Block.Properties.create(Material.DRAGON_EGG).hardnessAndResistance(1));
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity te = worldIn.getTileEntity(pos);
        CompoundNBT prevTag = stack.getTag();
        if (te == null || prevTag == null) return;
        CompoundNBT tag = new CompoundNBT();
        te.write(tag);
        tag.putInt("hatchTimer", prevTag.getInt("hatchTimer"));
        tag.putString("dragonType", prevTag.getString("dragonType"));
        te.read(tag);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        te.activated = true;
        return true;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) { return SHAPE; }
    
    @Override
    public boolean hasTileEntity(BlockState state) { return true; }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return te; }
}