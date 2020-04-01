package WolfShotz.Wyrmroost.content.fluids;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRFluids;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.function.Supplier;

public class BrineFluid extends WaterFluid
{
    public static final DamageSource BRINE_WATER = new DamageSource("brine_Water").setDamageBypassesArmor();

    public final boolean source;

    public BrineFluid(boolean source)
    {
        this.source = source;
    }

    public static Supplier<BrineFluid> create(boolean source) { return () -> new BrineFluid(source); }

    @Override
    protected FluidAttributes createAttributes()
    {
        return FluidAttributes
                .builder(Wyrmroost.rl("block/brine_still"), Wyrmroost.rl("block/brine_flow"))
                .translationKey("block.wyrmroost.brine")
                .build(this);
    }

    @Override
    public Fluid getStillFluid() { return WRFluids.BRINE.getSource(); }

    @Override
    public Fluid getFlowingFluid() { return WRFluids.BRINE.getFlow(); }

    @Override
    public Item getFilledBucket() { return WRFluids.BRINE.getBucketItem(); }

    public BlockState getBlockState(IFluidState state)
    {
        return WRFluids.BRINE.getBlock().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isSource(IFluidState state) { return source; }

    @Override
    public int getLevel(IFluidState state) { return isSource(state) ? 8 : state.get(LEVEL_1_8); }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn)
    {
        return fluidIn == WRFluids.BRINE.getSource() || fluidIn == WRFluids.BRINE.getFlow();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
    {
        super.fillStateContainer(builder);
        if (!source) builder.add(LEVEL_1_8);
    }

    public static class Block extends FlowingFluidBlock
    {
        public Block()
        {
            super(WRFluids.BRINE.source, ModUtils.blockBuilder(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100).noDrops());
        }

        @Override
        public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
        {
            entityIn.attackEntityFrom(new DamageSource("brine"), 2);
        }
    }
}
