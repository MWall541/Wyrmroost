package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.tile.WRSignBlockEntity;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class WRBlockEntities
{
    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Wyrmroost.MOD_ID);

    public static final RegistryObject<TileEntityType<SignTileEntity>> CUSTOM_SIGN = register("sign", WRSignBlockEntity::new, () -> SignTileEntityRenderer::new, () -> setOf(WRBlocks.OSERI_WOOD.getSign(), WRBlocks.OSERI_WOOD.getWallSign()));

    public static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, Supplier<Set<Block>> blocks)
    {
        return register(name, factory, null, blocks);
    }

    public static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, @Nullable Supplier<Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>>> renderer, Supplier<Set<Block>> blocks)
    {
        RegistryObject<TileEntityType<T>> reg = REGISTRY.register(name, () -> new TileEntityType<>(factory, blocks.get(), null));
        if (renderer != null)
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientEvents.CALLBACKS.add(() -> ClientRegistry.bindTileEntityRenderer(reg.get(), renderer.get())));
        return reg;
    }

    private static Set<Block> setOf(Block... blocks)
    {
        return ImmutableSet.copyOf(blocks);
    }
}
