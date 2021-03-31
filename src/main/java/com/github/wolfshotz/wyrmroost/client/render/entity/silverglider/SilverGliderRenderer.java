package com.github.wolfshotz.wyrmroost.client.render.entity.silverglider;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.entity.AbstractDragonRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.SilverGliderEntity;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity, SilverGliderModel>
{
    public static final ResourceLocation[] MALE_TEXTURES = new ResourceLocation[6]; // includes glow
    // Constant textures
    public static final ResourceLocation FEMALE = resource("female.png");
    public static final ResourceLocation FEMALE_GLOW = resource("female_glow.png");
    public static final ResourceLocation SPECIAL = resource("spe.png");
    public static final ResourceLocation SPECIAL_GLOW = resource("spe_glow.png");
    public static final ResourceLocation CHRISTMAS = resource("christmas.png");
    public static final ResourceLocation CHRISTMAS_MALE_LAYER = resource("christmas_male_layer.png");
    public static final ResourceLocation SLEEP = resource("sleep.png");

    public SilverGliderRenderer(EntityRendererManager manager)
    {
        super(manager, new SilverGliderModel(), 1f);

        addLayer(new GlowLayer(this::getGlowTexture));
        addLayer(new ConditionalLayer(TameableDragonEntity::isSleeping, d -> RenderType.entityCutoutNoCull(SLEEP)));
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(SilverGliderEntity sg)
    {
        if (sg.getVariant() == -1) return SPECIAL;
        if (WRConfig.DECK_THE_HALLS.get()) return CHRISTMAS;
        if (!sg.isMale()) return FEMALE;
        int index = sg.getVariant();
        if (MALE_TEXTURES[index] == null) return MALE_TEXTURES[index] = resource("male_" + index + ".png");
        return MALE_TEXTURES[index];
    }

    private ResourceLocation getGlowTexture(SilverGliderEntity sg)
    {
        if (sg.getVariant() == -1) return SPECIAL_GLOW;
        if (WRConfig.DECK_THE_HALLS.get()) return sg.isMale()? CHRISTMAS_MALE_LAYER : null;
        if (!sg.isMale()) return FEMALE_GLOW;
        int index = sg.getVariant() + 3;
        if (MALE_TEXTURES[index] == null)
            return MALE_TEXTURES[index] = resource("male_" + sg.getVariant() + "_glow.png");
        return MALE_TEXTURES[index];
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.id(BASE_PATH + "silver_glider/" + png);
    }
}
