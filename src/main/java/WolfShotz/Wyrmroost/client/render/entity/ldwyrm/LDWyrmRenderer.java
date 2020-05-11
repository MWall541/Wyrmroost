package WolfShotz.Wyrmroost.client.render.entity.ldwyrm;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.LDWyrmEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class LDWyrmRenderer extends MobRenderer<LDWyrmEntity, EntityModel<LDWyrmEntity>>
{
    private ResourceLocation minutus = Wyrmroost.rl("textures/entity/dragon/minutus/body.png");

    public LDWyrmRenderer(EntityRendererManager manager)
    {
        super(manager, new LDWyrmModel(), 0);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(LDWyrmEntity entity)
    {
        return minutus;
    }
}
