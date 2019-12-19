package WolfShotz.Wyrmroost.content.entities.dragon.owdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity>
{
    public static final ResourceLocation MALE_COM           = resource("male_com.png");
    public static final ResourceLocation FEMALE_COM         = resource("female_com.png");
    public static final ResourceLocation MALE_SAV           = resource("male_sav.png");
    public static final ResourceLocation FEMALE_SAV         = resource("female_sav.png");
    public static final ResourceLocation MALE_SPE           = resource("male_spe.png");
    public static final ResourceLocation FEMALE_SPE         = resource("female_spe.png");
    public static final ResourceLocation CHILD_COM          = resource("child_com.png");
    public static final ResourceLocation CHILD_SAV          = resource("child_sav.png");
    public static final ResourceLocation CHILD_SPE          = resource("child_spe.png");
    // Easter Egg
    public static final ResourceLocation DAISY              = resource("dasy.png");
    public static final ResourceLocation JEB_               = resource("jeb.png");
    // Saddle
    // Saddle
    public static final ResourceLocation SADDLE_LAYER       = resource("accessories/saddle.png");
    // Armor
    public static final ResourceLocation ARMOR_IRON         = resource("accessories/armor_iron.png");
    public static final ResourceLocation ARMOR_GOLD         = resource("accessories/armor_gold.png");
    public static final ResourceLocation ARMOR_DIAMOND      = resource("accessories/armor_diamond.png");
    public static final ResourceLocation ARMOR_PLATINUM     = resource("accessories/armor_platinum.png");
    public static final ResourceLocation ARMOR_GEODE_BLUE   = resource("accessories/armor_geode_blue.png");
    public static final ResourceLocation ARMOR_GEODE_RED    = resource("accessories/armor_geode_red.png");
    public static final ResourceLocation ARMOR_GEODE_PURPLE = resource("accessories/armor_geode_purple.png");

    public OWDrakeRenderer(EntityRendererManager manager) {
        super(manager, new OWDrakeModel(), 1.6f);
        addLayer(new ConditionalLayer(d -> getArmorTexture(d.getArmor().getType()), AbstractDragonEntity::hasArmor));
        addLayer(new ConditionalLayer(SADDLE_LAYER, OWDrakeEntity::isSaddled));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OWDrakeEntity drake) {
        if (drake.hasCustomName()) {
            String name = drake.getCustomName().getUnformattedComponentText();
            if (name.equals("Daisy")) return DAISY;
            if (name.equalsIgnoreCase("Jeb_")) return JEB_;
        }
        boolean isSavannah = drake.getDrakeVariant();
        boolean isSpecial = drake.isSpecial();
        boolean gender = drake.getGender();
    
        if (drake.isChild()) {
            if (isSpecial)  return CHILD_SPE;
            if (isSavannah) return CHILD_SAV;
            return CHILD_COM;
        }
        if (isSpecial)  return gender? MALE_SPE : FEMALE_SPE;
        if (isSavannah) return gender? MALE_SAV : FEMALE_SAV;
        return gender? MALE_COM : FEMALE_COM;
    }
    
    private ResourceLocation getArmorTexture(DragonArmorItem.DragonArmorType type) {
        switch (type) {
            default:
            case IRON:          return ARMOR_IRON;
            case GOLD:          return ARMOR_GOLD;
            case DIAMOND:       return ARMOR_DIAMOND;
            case PLATINUM:      return ARMOR_PLATINUM;
            case BLUE_GEODE:    return ARMOR_GEODE_BLUE;
            case RED_GEODE:     return ARMOR_GEODE_RED;
            case PURPLE_GEODE:  return ARMOR_GEODE_PURPLE;
        }
    }
    
    public static ResourceLocation resource(String png) { return ModUtils.resource(DEF_LOC + "owdrake/" + png); }
}
