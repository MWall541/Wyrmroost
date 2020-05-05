package WolfShotz.Wyrmroost.client.render.entity.canari;

import WolfShotz.Wyrmroost.*;
import WolfShotz.Wyrmroost.client.render.entity.*;
import WolfShotz.Wyrmroost.content.entities.dragon.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;

import javax.annotation.*;

public class CanariWyvernRenderer extends AbstractDragonRenderer<CanariWyvernEntity>
{
    // Easter egg
    private static final ResourceLocation EE_LADY = resource("lady.png");
    private static final ResourceLocation EE_RUDY = resource("rudy.png");

    public CanariWyvernRenderer(EntityRendererManager manager)
    {
        super(manager, new CanariWyvernModel(), 0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(CanariWyvernEntity canari)
    {
        if (canari.hasCustomName())
        {
            String name = canari.getCustomName().getUnformattedComponentText();
            if (name.equals("Rudy")) return EE_RUDY;
            if (name.equals("Lady Everlyn Winklestein") && !canari.getGender()) return EE_LADY;
        }

        String path = "body_" + canari.getVariant();
        if (canari.getGender()) path += "m";
        else path += "f";
        return resource(path + ".png");
    }
    
    private static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(DEF_LOC + "canari/" + png);
    }
}
