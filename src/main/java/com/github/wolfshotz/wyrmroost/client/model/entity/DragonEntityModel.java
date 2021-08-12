package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WREntityModel;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.DragonArmorItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class DragonEntityModel<T extends TameableDragonEntity> extends WREntityModel<T>
{
    private static final Map<String, ResourceLocation> ARMOR_TEXTURES = new HashMap<>();
    public static final String FOLDER = "textures/entity/dragon/";

    public DragonEntityModel()
    {
    }

    public DragonEntityModel(Function<ResourceLocation, RenderType> type)
    {
        super(type);
    }

    @Override
    public void scale(T entity, MatrixStack ms, float partialTicks)
    {
        float scale = entity.getScale();
        ms.scale(scale, scale, scale);
    }

    public void renderEyes(ResourceLocation texture, MatrixStack ms, IRenderTypeBuffer buffer)
    {
        renderToBuffer(ms, buffer.getBuffer(RenderHelper.getAdditiveGlow(texture)), 15728640, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }

    public void renderArmorOverlay(MatrixStack ms, IRenderTypeBuffer buffer, int light)
    {
        ItemStack armor = entity.getArmorStack();
        if (!armor.isEmpty())
        {
            Item item = armor.getItem();
            float r = 1f, g = 1f, b = 1f;
            if (armor.getItem() instanceof DragonArmorItem.Dyeable)
            {
                int i = ((DragonArmorItem.Dyeable) armor.getItem()).getColor(armor);
                r = (i >> 16 & 255) / 255f;
                g = (i >> 8 & 255) / 255f;
                b = (i & 255) / 255f;
            }
            renderTexturedOverlay(getArmorTexture(entity, item), ms, buffer, light, OverlayTexture.NO_OVERLAY, r, g, b, 1f);
        }
    }

    private static ResourceLocation getArmorTexture(TameableDragonEntity entity, Item armor)
    {
        String path = entity.getType().getRegistryName().getPath() + "/accessories/" + armor.getRegistryName().getPath();
        return ARMOR_TEXTURES.computeIfAbsent(path, p -> Wyrmroost.id(FOLDER + p + ".png"));
    }
}
