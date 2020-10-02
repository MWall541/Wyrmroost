package WolfShotz.Wyrmroost.client.render.entity.royal_red;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.RoyalRedEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RoyalRedRenderer extends AbstractDragonRenderer<RoyalRedEntity, RoyalRedModel>
{
    public static final ResourceLocation MALE = Wyrmroost.rl(BASE_PATH + "royal_red/male.png");
    public static final ResourceLocation FEMALE = Wyrmroost.rl(BASE_PATH + "royal_red/female.png");

    public RoyalRedRenderer(EntityRendererManager manager)
    {
        super(manager, new RoyalRedModel(), 2.5f);
        addLayer(new ArmorLayer(RoyalRedEntity.ARMOR_SLOT));
    }

    @Override
    public ResourceLocation getEntityTexture(RoyalRedEntity entity) { return entity.isMale()? MALE : FEMALE; }
}
