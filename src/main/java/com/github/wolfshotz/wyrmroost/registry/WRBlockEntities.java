package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.tile.WRSignBlockEntity;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class WRBlockEntities<T extends TileEntity> extends TileEntityType<T>
{
    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Wyrmroost.MOD_ID);

    public static final RegistryObject<TileEntityType<?>> CUSTOM_SIGN = register("sign", WRSignBlockEntity::new, () -> SignTileEntityRenderer::new);

    @Nullable private final Supplier<Function<TileEntityRendererDispatcher, TileEntityRenderer<T>>> renderer;

    public WRBlockEntities(Supplier<? extends T> factory, Supplier<Function<TileEntityRendererDispatcher, TileEntityRenderer<T>>> renderer)
    {
        super(factory, null, null);
        this.renderer = renderer;
    }

    @Override
    public boolean isValid(Block block)
    {
        return block instanceof Validator && ((Validator) block).isValidEntity(this);
    }

    public void callBack()
    {
        if (ModUtils.isClient() && renderer != null)
            ClientRegistry.bindTileEntityRenderer(this, renderer.get());
    }

    public static <T extends TileEntity> RegistryObject<TileEntityType<?>> register(String name, Supplier<T> factory, @Nullable Supplier<Function<TileEntityRendererDispatcher, TileEntityRenderer<T>>> renderer)
    {
        return REGISTRY.register(name, () -> new WRBlockEntities<>(factory, renderer));
    }

    public interface Validator
    {
        boolean isValidEntity(TileEntityType<?> type);
    }
}
