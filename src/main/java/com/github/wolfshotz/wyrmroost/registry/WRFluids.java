package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class WRFluids
{
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, Wyrmroost.MOD_ID);

    public static final RegistryObject<FlowingFluid> BRINE = register("brine", b -> b.color(0xCCDFDF).density(1500).viscosity(1500), b -> b.canMultiply());

    public static RegistryObject<FlowingFluid> register(String name, Function<Supplier<FlowingFluid>, FlowingFluidBlock> block, Consumer<FluidAttributes.Builder> builder, Consumer<ForgeFlowingFluid.Properties> propsBuilder)
    {
        AtomicReference<ForgeFlowingFluid.Properties> ref = new AtomicReference<>(); // caution on memory overhead
        RegistryObject<FlowingFluid> source = REGISTRY.register(name, () -> new ForgeFlowingFluid.Source(ref.get()));
        RegistryObject<Fluid> flow = REGISTRY.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(ref.get()));
        FluidAttributes.Builder atts = FluidAttributes.builder(Wyrmroost.id(String.format("block/%s_still", name)), Wyrmroost.id(String.format("block/%s_flow", name)))
                .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY);
        builder.accept(atts);
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(source, flow, atts)
                .block(WRBlocks.REGISTRY.register(name, () -> block.apply(source)))
                .bucket(WRItems.register(name + "_bucket", () -> new BucketItem(source, new Item.Properties().stacksTo(1).tab(WRBlocks.BLOCKS_ITEM_GROUP))));
        propsBuilder.accept(properties);
        ref.set(properties);
        return source;
    }

    public static RegistryObject<FlowingFluid> register(String name, Consumer<FluidAttributes.Builder> builder, Consumer<ForgeFlowingFluid.Properties> propsBuilder)
    {
        return register(name, s -> new FlowingFluidBlock(s, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100).noDrops()), builder, propsBuilder);
    }
}
