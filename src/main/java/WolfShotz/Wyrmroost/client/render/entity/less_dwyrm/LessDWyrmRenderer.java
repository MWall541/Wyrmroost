package WolfShotz.Wyrmroost.client.render.entity.less_dwyrm;

import WolfShotz.Wyrmroost.*;
import WolfShotz.Wyrmroost.content.entities.dragon.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.util.*;

import javax.annotation.*;

public class LessDWyrmRenderer extends MobRenderer<MinutusEntity, EntityModel<MinutusEntity>>
{
    private ResourceLocation minutus = Wyrmroost.rl("textures/entity/dragon/minutus/body.png");

    public LessDWyrmRenderer(EntityRendererManager manager)
    {
        super(manager, new LessDWyrmModel(), 0);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(MinutusEntity entity)
    {
        return minutus;
    }
}
