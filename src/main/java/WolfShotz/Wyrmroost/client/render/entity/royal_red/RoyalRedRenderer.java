package WolfShotz.Wyrmroost.client.render.entity.royal_red;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.RoyalRedEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RoyalRedRenderer extends AbstractDragonRenderer<RoyalRedEntity, RoyalRedModel>
{
    public static final ResourceLocation TEXTURE = Wyrmroost.rl("textures/entity/dragon/royal_red/body.png");

    public RoyalRedRenderer(EntityRendererManager manager)
    {
        super(manager, new RoyalRedModel(), 2f);
    }

    @Override
    public ResourceLocation getEntityTexture(RoyalRedEntity entity) { return TEXTURE; }
}
