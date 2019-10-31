package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DragonFruitDrakeRenderer extends AbstractDragonRenderer<DragonFruitDrakeEntity>
{
    public static final ResourceLocation BODY_MALE = resource("body_m.png");
    
    public DragonFruitDrakeRenderer(EntityRendererManager manager) {
        super(manager, new DragonFruitDrakeModel(), 1.5f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DragonFruitDrakeEntity entity) { return BODY_MALE; }
    
    public static ResourceLocation resource(String png) { return ModUtils.location(DEF_LOC + "dfruitdrake/" + png); }
}
