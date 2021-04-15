package com.github.wolfshotz.wyrmroost.client.render.entity.coin_dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.CoinDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CoinDragonRenderer extends MobRenderer<CoinDragonEntity, CoinDragonModel>
{
    private static final ResourceLocation CHRISTMAS = Wyrmroost.id("textures/entity/dragon/coin_dragon/christmas.png");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[5];

    static
    {
        for (int i = 0; i < TEXTURES.length; i++)
            TEXTURES[i] = Wyrmroost.id("textures/entity/dragon/coin_dragon/body_" + i + ".png");
    }

    public CoinDragonRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn, new CoinDragonModel(), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(CoinDragonEntity entity)
    {
        return WRConfig.deckTheHalls()? CHRISTMAS : TEXTURES[entity.getVariant()];
    }
}
