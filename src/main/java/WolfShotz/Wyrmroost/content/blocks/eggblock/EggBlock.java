package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
import java.util.Map;
import java.util.Random;

public class EggBlock extends Block implements IWaterLoggable
{
    private static final Map<String, Integer> DRAGONTYPES = ImmutableMap.of(
            "overworld_drake", 0,
            "silver_glider", 1,
            "roost_stalker", 2
    );
    
    private static final VoxelShape[] HITBOX = new VoxelShape[] {
            Block.makeCuboidShape(3, 0, 3, 13, 16, 13), // 0: Drake Egg
            Block.makeCuboidShape(4.3d, 0, 4.3d, 11.3d, 10, 11.3d), // 1: Glider Egg
            Block.makeCuboidShape(6d, 0, 6d, 10d, 6, 10d) // 2: Stalker Egg
    };
    
    public static final IntegerProperty DRAGONTYPE = IntegerProperty.create("eggtype", 0, HITBOX.length - 1);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    public EggBlock() {
        super(ModUtils.blockBuilder(Material.DRAGON_EGG).hardnessAndResistance(0, 3).sound(SoundType.METAL));
        setRegistryName("egg");
        
        setDefaultState(getStateContainer().getBaseState().with(DRAGONTYPE, 0).with(WATERLOGGED, false));
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
        if (player.getHeldItem(handIn).getItem() instanceof BucketItem) return false;
        
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
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        IFluidState ifluidstate = world.getFluidState(pos);
        CompoundNBT tag = context.getItem().getTag();
        
        if (tag != null && tag.contains("dragonType")) {
            int index = byEntityType(tag.getString("dragonType"));
            
            return getDefaultState().with(DRAGONTYPE, index).with(WATERLOGGED, ifluidstate.getFluid() != Fluids.EMPTY);
        }
        
        return getDefaultState();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        int index = state.get(DRAGONTYPE);
        
        if (index < 0 || index > HITBOX.length) {
            ModUtils.L.warn("Could not get shape for egg!");
            return HITBOX[0];
        }
        
        return HITBOX[index];
    }
    
    @SuppressWarnings("deprecation")
    public IFluidState getFluidState(BlockState state) { return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state); }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DRAGONTYPE, WATERLOGGED);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) { return true; }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new EggTileEntity(); }
    
    /**
     * Get the registry name minus the
     * @param key
     * @return
     */
    private static int byEntityType(String key) {
        EntityType type = EntityType.byKey(key).orElse(null);
        if (type == null) return 0;
        String registryName = type.getRegistryName().toString().replace("wyrmroost:", "");
        
        return DRAGONTYPES.get(registryName);
    }
    
}