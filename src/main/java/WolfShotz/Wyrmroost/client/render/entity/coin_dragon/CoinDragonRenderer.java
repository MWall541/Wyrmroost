package WolfShotz.Wyrmroost.client.render.entity.coin_dragon;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.CoinDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CoinDragonRenderer extends MobRenderer<CoinDragonEntity, CoinDragonModel>
{
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[5];

    static
    {
        for (int i = 0; i < TEXTURES.length; i++)
            TEXTURES[i] = Wyrmroost.rl("textures/entity/dragon/coin_dragon/body_" + i + ".png");
    }

    public CoinDragonRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn, new CoinDragonModel(), 0.25f);
    }

    @Override
    public ResourceLocation getEntityTexture(CoinDragonEntity entity) { return TEXTURES[entity.getVariant()]; }
}
