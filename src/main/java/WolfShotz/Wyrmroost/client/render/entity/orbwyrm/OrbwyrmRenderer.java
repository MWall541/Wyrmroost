package WolfShotz.Wyrmroost.client.render.entity.orbwyrm;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.OrbwyrmEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class OrbwyrmRenderer extends AbstractDragonRenderer<OrbwyrmEntity, OrbwyrmModel>
{
    private static final ResourceLocation PALE = Wyrmroost.rl(BASE_PATH + "orbwyrm/body.png");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[6];

    public OrbwyrmRenderer(EntityRendererManager manager)
    {
        super(manager, new OrbwyrmModel(), 3.5f);
    }

    @Override
    protected void preRenderCallback(OrbwyrmEntity entity, MatrixStack ms, float partialTicks)
    {
        super.preRenderCallback(entity, ms, partialTicks);
        ms.scale(2.5f, 2.5f, 2.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(OrbwyrmEntity entity)
    {
        if (entity.getVariant() == -1) return PALE;

        int index = (entity.isMale()? 0 : 3) + entity.getVariant();
        if (TEXTURES[index] == null)
        {
            String path = BASE_PATH + "orbwyrm/body_" + entity.getVariant() + (index < 3? "m" : "f") + ".png";
            return TEXTURES[index] = Wyrmroost.rl(path);
        }
        return TEXTURES[index];
    }
}
