package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.event.SetupBlock;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EggBlock extends BlockBase
{
    private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    
    public EggBlock() {
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
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof EggTileEntity)) return false;
        ((EggTileEntity) te).activated = true;
        return true;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof EggTileEntity))  return;
        if (!((EggTileEntity) te).activated) return;
        
        double x = pos.getX() + rand.nextDouble();
        double z = pos.getZ() + rand.nextDouble();
        double y = pos.getY() + rand.nextDouble();
        world.addParticle(new RedstoneParticleData(1, 1, 0, 0.5f), x, y, z, 0, 0, 0);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) { return SHAPE; }
    
    @Override
    public boolean hasTileEntity(BlockState state) { return true; }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new EggTileEntity(); }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            String dragonTranslation = EntityType.byKey(tag.getString("dragonType")).get().getName().getUnformattedComponentText();
            String eggTranslation = SetupBlock.egg.getNameTextComponent().getUnformattedComponentText();
            
            stack.setDisplayName(ModUtils.translation(dragonTranslation +" "+ eggTranslation));
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt("hatchTimer") / 1200).applyTextStyle(TextFormatting.AQUA));
        }
    }
}