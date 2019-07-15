package WolfShotz.Wyrmroost.content.entities;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
    protected String DEF_LOC = "textures/entity/dragon/";

    public AbstractDragonRenderer(EntityRendererManager manager, M model, float shadowSize) {
        super(manager, model, shadowSize);
    }

}
