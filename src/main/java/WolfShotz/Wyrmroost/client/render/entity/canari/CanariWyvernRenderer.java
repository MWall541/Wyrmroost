package WolfShotz.Wyrmroost.client.render.entity.canari;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.CanariWyvernEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class CanariWyvernRenderer extends AbstractDragonRenderer<CanariWyvernEntity, CanariWyvernModel>
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
    public ResourceLocation getEntityTexture(CanariWyvernEntity canari)
    {
        if (canari.hasCustomName())
        {
            String name = canari.getCustomName().getUnformattedComponentText();
            if (name.equals("Rudy")) return EE_RUDY;
            if (name.equals("Lady Everlyn Winklestein") && !canari.isMale()) return EE_LADY;
        }

        String path = "body_" + canari.getVariant() + (canari.isMale()? "m" : "f");
        return resource(path + ".png");
    }

    private static ResourceLocation resource(String png) { return Wyrmroost.rl(BASE_PATH + "canari_wyvern/" + png); }
}
