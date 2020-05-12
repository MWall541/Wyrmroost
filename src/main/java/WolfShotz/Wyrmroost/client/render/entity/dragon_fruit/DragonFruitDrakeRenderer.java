package WolfShotz.Wyrmroost.client.render.entity.dragon_fruit;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.DragonFruitDrakeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class DragonFruitDrakeRenderer extends AbstractDragonRenderer<DragonFruitDrakeEntity, DragonFruitDrakeModel>
{
    public static final ResourceLocation CHILD = resource("child.png");

    public DragonFruitDrakeRenderer(EntityRendererManager manager)
    {
        super(manager, new DragonFruitDrakeModel(), 1.5f);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(DragonFruitDrakeEntity entity)
    {
        if (entity.isChild()) return CHILD;
        String path = "body_" + (entity.isMale()? "m" : "f");
        if (entity.isSpecial()) path += "_s";
        return resource(path + ".png");
    }
    
    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(BASE_PATH + "dfruitdrake/" + png);
    }
}
