package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DragonFruitDrakeRenderer extends AbstractDragonRenderer<DragonFruitDrakeEntity>
{
    public final ResourceLocation BODY_MALE = location("body_m.png");
    
    public DragonFruitDrakeRenderer(EntityRendererManager manager) {
        super(manager, new DragonFruitDrakeModel(), 1.5f);
    }
    
    @Override
    public String getResourceDirectory() { return DEF_LOC + "dfruitdrake/"; }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DragonFruitDrakeEntity entity) { return BODY_MALE; }
}
