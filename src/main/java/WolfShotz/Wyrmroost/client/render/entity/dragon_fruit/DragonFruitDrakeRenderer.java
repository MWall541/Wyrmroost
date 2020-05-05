package WolfShotz.Wyrmroost.client.render.entity.dragon_fruit;

import WolfShotz.Wyrmroost.*;
import WolfShotz.Wyrmroost.client.render.entity.*;
import WolfShotz.Wyrmroost.content.entities.dragon.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;

import javax.annotation.*;

public class DragonFruitDrakeRenderer extends AbstractDragonRenderer<DragonFruitDrakeEntity>
{
    public static final ResourceLocation BODY_MALE = resource("body_m.png");
    
    public DragonFruitDrakeRenderer(EntityRendererManager manager)
    {
        super(manager, new DragonFruitDrakeModel(), 1.5f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DragonFruitDrakeEntity entity)
    {
        return BODY_MALE;
    }
    
    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(DEF_LOC + "dfruitdrake/" + png);
    }
}
