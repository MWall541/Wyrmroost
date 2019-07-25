package WolfShotz.Wyrmroost.content.entities;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity> extends MobRenderer<T, EntityModel<T>>
{
    protected String DEF_LOC = "textures/entity/dragon/";

    public AbstractDragonRenderer(EntityRendererManager manager, EntityModel<T> model, float shadowSize) {
        super(manager, model, shadowSize);
    }
}
