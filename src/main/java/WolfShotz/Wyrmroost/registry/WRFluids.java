package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * FLuids are handled like crap so we'll do it way better than mojang nerds
 * On Fluid source creation, also create its flowing fluid, its FlowingFluidBlock, and a Bucket Item to go with this Fluid.
 * These entries are then stored in a {@link FluidRegistry} in which they can be referenced.
 * Done this way, because I feel like having everything for one fluid be under one umbrella. (As it should be anyway...)
 * <p>
 * For differentiating between source and flowing fluid, use simple god damn boolean logic. Why should we have to create
 * new inner classes for that? No. Make a boolean parameter in the fluid constructor and use that to handle the needed methods.
 */
public class WRFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Wyrmroost.MOD_ID);

//    public static final FluidRegistry CAUSTIC_WATER = register("caustic_water", CausticWaterFluid::create, CausticWaterFluid.Block::new);
//    public static final FluidRegistry BRINE = register("brine", BrineFluid::create, BrineFluid.Block::new);

    public static FluidRegistry register(String name, Function<Boolean, Supplier<? extends FlowingFluid>> fluidFunc, Supplier<FlowingFluidBlock> block)
    {
        RegistryObject<FlowingFluid> s = FLUIDS.register(name, fluidFunc.apply(true));
        RegistryObject<FlowingFluid> f = FLUIDS.register("flowing_" + name, fluidFunc.apply(false));
        RegistryObject<FlowingFluidBlock> b = WRBlocks.BLOCKS.register(name, block);
        RegistryObject<BucketItem> bu = WRItems.ITEMS.register(name + "_bucket", () -> new BucketItem(s, ModUtils.itemBuilder().containerItem(Items.BUCKET).maxStackSize(1)));
        return new FluidRegistry(s, f, b, bu);
    }

    /**
     * Fluid Registry Cache: Holds anything related to a fluid
     * This includes the fluid source block, the fluid flowing block,
     * the fluid block itself, and its bucket item
     * <p>
     * I could make a sort of system in fluid classes themselves by im fucking lazy so idgaf
     */
    public static class FluidRegistry
    {
        // use these if supplier is needed
        public final RegistryObject<FlowingFluid> source;
        public final RegistryObject<FlowingFluid> flow;
        public final RegistryObject<FlowingFluidBlock> block;
        public final RegistryObject<BucketItem> bucket;

        public FluidRegistry(RegistryObject<FlowingFluid> source, RegistryObject<FlowingFluid> flow, RegistryObject<FlowingFluidBlock> block, RegistryObject<BucketItem> bucket)
        {
            this.source = source;
            this.flow = flow;
            this.block = block;
            this.bucket = bucket;
        }

        public FlowingFluid getSource() { return source.get(); }

        public FlowingFluid getFlow() { return flow.get(); }

        public FlowingFluidBlock getBlock() { return block.get(); }

        public BucketItem getBucketItem() { return bucket.get(); }
    }
}
