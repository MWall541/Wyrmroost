package WolfShotz.Wyrmroost.client.render.entity.less_dwyrm;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.MinutusEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class LessDWyrmRenderer extends MobRenderer<MinutusEntity, EntityModel<MinutusEntity>>
{
    private ResourceLocation minutus = Wyrmroost.rl("textures/entity/dragon/minutus/body.png");

    public LessDWyrmRenderer(EntityRendererManager manager)
    {
        super(manager, new LessDWyrmModel(), 0);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(MinutusEntity entity)
    {
        return minutus;
    }
}
