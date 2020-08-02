package WolfShotz.Wyrmroost.client.render.entity.alpine;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.AlpineEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class AlpineRenderer extends AbstractDragonRenderer<AlpineEntity, AlpineModel>
{
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[6];

    public AlpineRenderer(EntityRendererManager manager) { super(manager, new AlpineModel(), 2.5f); }

    @Override
    public ResourceLocation getEntityTexture(AlpineEntity entity)
    {
        int variant = entity.getVariant();
        if (TEXTURES[variant] == null)
            return TEXTURES[variant] = Wyrmroost.rl(BASE_PATH + "alpine/body_" + variant + ".png");
        return TEXTURES[variant];
    }
}
