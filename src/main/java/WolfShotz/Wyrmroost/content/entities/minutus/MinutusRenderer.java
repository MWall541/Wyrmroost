package WolfShotz.Wyrmroost.content.entities.minutus;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MinutusRenderer extends AbstractDragonRenderer<MinutusEntity>
{
    private String loc = DEF_LOC + "minutus/";
    private ResourceLocation minutus = ModUtils.location(loc + "body.png");

    public MinutusRenderer(EntityRendererManager manager) { super(manager, new MinutusModel(), 0); }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(MinutusEntity entity) { return minutus; }
}
