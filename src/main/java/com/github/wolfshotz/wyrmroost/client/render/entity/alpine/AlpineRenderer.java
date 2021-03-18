package com.github.wolfshotz.wyrmroost.client.render.entity.alpine;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.entity.AbstractDragonRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.AlpineEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class AlpineRenderer extends AbstractDragonRenderer<AlpineEntity, AlpineModel>
{
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[6];

    public AlpineRenderer(EntityRendererManager manager) { super(manager, new AlpineModel(), 2f); }

    @Override
    public ResourceLocation getTextureLocation(AlpineEntity entity)
    {
        int variant = entity.getVariant();
        if (TEXTURES[variant] == null)
        {
            String path = BASE_PATH + "alpine/body_" + variant;
            if (WRConfig.deckTheHalls) path += "_christmas";
            return TEXTURES[variant] = Wyrmroost.id(path + ".png");
        }
        return TEXTURES[variant];
    }
}
