package WolfShotz.Wyrmroost.content.entities.dragon.minutus;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class MinutusRenderer extends MobRenderer<MinutusEntity, EntityModel<MinutusEntity>>
{
    private ResourceLocation minutus = Wyrmroost.rl("textures/entity/dragon/minutus/body.png");
    
    public MinutusRenderer(EntityRendererManager manager)
    {
        super(manager, new MinutusModel(), 0);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(MinutusEntity entity)
    {
        return minutus;
    }
}
