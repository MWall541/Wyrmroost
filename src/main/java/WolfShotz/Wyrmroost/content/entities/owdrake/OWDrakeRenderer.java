package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends MobRenderer<OWDrakeEntity, OWDrakeModel<OWDrakeEntity>>
{
    private ResourceLocation ComMale = ModUtils.location("textures/entity/dragon/owdrake/owdrake_cm.png");
    private ResourceLocation ComFemale = ModUtils.location("textures/entity/dragon/owdrake/owdrake_cf.png");
    private ResourceLocation SavMale = ModUtils.location("textures/entity/dragon/owdrake/owdrake_sm.png");
    private ResourceLocation SavFemale = ModUtils.location("textures/entity/dragon/owdrake/owdrake_sf.png");

    public OWDrakeRenderer(EntityRendererManager manager) { super(manager, new OWDrakeModel<>(), 1.6f); }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OWDrakeEntity drake) { return drake.getGender() ? ComMale : ComFemale; }
}
