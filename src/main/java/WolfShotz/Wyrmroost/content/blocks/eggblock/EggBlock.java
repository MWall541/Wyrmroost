package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class EggBlock extends Block
{
    public EggBlock() {
        super(ModUtils.blockBuilder(Material.DRAGON_EGG).hardnessAndResistance(0, 3).sound(SoundType.METAL));
        setRegistryName("egg");
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
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof EggTileEntity)) return false;
        ((EggTileEntity) te).activated = true;
        return true;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof EggTileEntity)) return;
        if (!((EggTileEntity) te).activated) return;
        
        double x = pos.getX() + rand.nextDouble();
        double z = pos.getZ() + rand.nextDouble();
        double y = pos.getY() + rand.nextDouble() + 0.5;
        world.addParticle(new RedstoneParticleData(1, 1, 0, 0.5f), x, y, z, 0, 0, 0);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        
        
        return Block.makeCuboidShape(3, 0, 3, 13, 16, 13);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) { return true; }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new EggTileEntity(); }
}