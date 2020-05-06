package WolfShotz.Wyrmroost.client.render.entity.owdrake;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.OWDrakeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity>
{
    // Easter Egg
    public static final ResourceLocation DAISY = resource("dasy.png");
    public static final ResourceLocation JEB_ = resource("jeb.png");
    // Saddle
    public static final ResourceLocation SADDLE_LAYER = resource("accessories/saddle.png");
    // Armor
    public static final ResourceLocation ARMOR_IRON = resource("accessories/armor_iron.png");
    public static final ResourceLocation ARMOR_GOLD = resource("accessories/armor_gold.png");
    public static final ResourceLocation ARMOR_DIAMOND = resource("accessories/armor_diamond.png");
    public static final ResourceLocation ARMOR_PLATINUM = resource("accessories/armor_platinum.png");
    public static final ResourceLocation ARMOR_GEODE_BLUE = resource("accessories/armor_geode_blue.png");
    public static final ResourceLocation ARMOR_GEODE_RED = resource("accessories/armor_geode_red.png");
    public static final ResourceLocation ARMOR_GEODE_PURPLE = resource("accessories/armor_geode_purple.png");

    public OWDrakeRenderer(EntityRendererManager manager)
    {
        super(manager, new OWDrakeModel(), 1.6f);
        addLayer(new ConditionalLayer(OWDrakeEntity::isArmored, d -> RenderType.getEntityCutoutNoCull(getArmorTexture(d))));
        addLayer(new ConditionalLayer(OWDrakeEntity::hasChest, d -> RenderType.getEntityCutoutNoCull(SADDLE_LAYER)));
    }
    
    public static ResourceLocation resource(String png) { return Wyrmroost.rl(DEF_LOC + "owdrake/" + png); }

    private ResourceLocation getArmorTexture(OWDrakeEntity drake)
    {
        switch (drake.getArmor().getType())
        {
            default:
            case IRON:
                return ARMOR_IRON;
            case GOLD:
                return ARMOR_GOLD;
            case DIAMOND:
                return ARMOR_DIAMOND;
            case PLATINUM:
                return ARMOR_PLATINUM;
            case BLUE_GEODE:
                return ARMOR_GEODE_BLUE;
            case RED_GEODE:
                return ARMOR_GEODE_RED;
            case PURPLE_GEODE:
                return ARMOR_GEODE_PURPLE;
        }
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(OWDrakeEntity drake)
    {
        if (drake.hasCustomName())
        {
            String name = drake.getCustomName().getUnformattedComponentText();
            if (name.equals("Daisy")) return DAISY;
            if (name.equalsIgnoreCase("Jeb_")) return JEB_;
        }

        String path = drake.isChild()? "child" : drake.getGender()? "male" : "female";
        if (drake.isSpecial()) return resource(path + "_spe.png");
        if (drake.getDrakeVariant()) return resource(path + "_sav.png");
        return resource(path + "_com.png");
    }
}
