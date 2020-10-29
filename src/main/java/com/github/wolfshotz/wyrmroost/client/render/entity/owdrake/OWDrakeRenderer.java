package com.github.wolfshotz.wyrmroost.client.render.entity.owdrake;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.entity.AbstractDragonRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.OWDrakeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity, OWDrakeModel>
{
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[64]; // some indexes will be left unused

    // Easter Egg
    public static final ResourceLocation DAISY = resource("daisy.png");
    public static final ResourceLocation JEB_ = resource("jeb.png");
    // Saddle
    public static final ResourceLocation SADDLE_LAYER = resource("accessories/saddle.png");

    public OWDrakeRenderer(EntityRendererManager manager)
    {
        super(manager, new OWDrakeModel(), 1.6f);
        addLayer(new ArmorLayer(OWDrakeEntity.ARMOR_SLOT));
        addLayer(new ConditionalLayer(OWDrakeEntity::isSaddled, d -> RenderType.getEntityCutoutNoCull(SADDLE_LAYER)));
    }

    public static ResourceLocation resource(String png) { return Wyrmroost.rl(BASE_PATH + "overworld_drake/" + png); }

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

        int index = 0;
        if (drake.isChild()) index |= 1;
        else if (!drake.isMale()) index |= 2;
        if (drake.getVariant() == -1) index |= 4;
        else if (drake.getVariant() == 1) index |= 8;

        if (TEXTURES[index] == null)
        {
            String path = (index & 1) != 0? "child" : (index & 2) != 0? "female" : "male";
            if ((index & 4) != 0) path += "_spe";
            else if ((index & 8) != 0) path += "_sav";
            return TEXTURES[index] = resource(path + ".png");
        }
        return TEXTURES[index];
    }
}
