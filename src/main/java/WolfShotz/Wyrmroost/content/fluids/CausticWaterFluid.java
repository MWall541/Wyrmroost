package WolfShotz.Wyrmroost.content.fluids;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.function.Supplier;

public class CausticWaterFluid extends WaterFluid
{
    public static final DamageSource CAUSTIC_WATER = new DamageSource("causticWater").setDamageBypassesArmor();

    // this is how it shouldve been done...
    public final boolean source;

    public CausticWaterFluid(boolean source)
    {
        this.source = source;
    }

    public static Supplier<CausticWaterFluid> create(boolean source) { return () -> new CausticWaterFluid(source); }

    @Override
    protected FluidAttributes createAttributes()
    {
        return FluidAttributes
                .builder(new ResourceLocation("block/water_still"), new ResourceLocation("block/water_flow"))
                .overlay(new ResourceLocation("block/water_overlay"))
                .translationKey("block.wyrmroost.caustic_water")
                .color(0xFF3F4FE4)
                .build(this);
    }

//    @Override
//    public Fluid getStillFluid() { return WRFluids.CAUSTIC_WATER.getSource(); }
//
//    @Override
//    public Fluid getFlowingFluid() { return WRFluids.CAUSTIC_WATER.getFlow(); }

    @Override
    public boolean isSource(IFluidState state) { return source; }

    @Override
    public int getLevel(IFluidState state) { return isSource(state)? 8 : state.get(LEVEL_1_8); }

//    public BlockState getBlockState(IFluidState state)
//    {
//        return WRFluids.CAUSTIC_WATER.getBlock().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
//    }

//    @Override
//    public boolean isEquivalentTo(Fluid fluidIn)
//    {
//        return fluidIn == WRFluids.CAUSTIC_WATER.getSource() || fluidIn == WRFluids.CAUSTIC_WATER.getFlow();
//    }

//    @Override
//    public Item getFilledBucket() { return WRFluids.CAUSTIC_WATER.getBucketItem(); }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
    {
        super.fillStateContainer(builder);
        if (!source) builder.add(LEVEL_1_8);
    }

//    public static class Block extends FlowingFluidBlock
//    {
//        public Block()
//        {
//            super(WRFluids.CAUSTIC_WATER.source, ModUtils.blockBuilder(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100).noDrops());
//        }
//
//        @Override
//        public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
//        {
//            if (entityIn instanceof LivingEntity)
//            {
//                if (Streams.stream(entityIn.getArmorInventoryList()).map(ItemStack::getItem).allMatch(CanariArmorItem.class::isInstance))
//                    return;
//                entityIn.attackEntityFrom(CAUSTIC_WATER, 1);
//            }
//        }
//    }
}
