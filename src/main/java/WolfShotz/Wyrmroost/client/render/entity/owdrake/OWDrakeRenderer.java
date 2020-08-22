package WolfShotz.Wyrmroost.client.render.entity.owdrake;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.OWDrakeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
        addLayer(new ArmorLayer());
        addLayer(new ConditionalLayer(OWDrakeEntity::isSaddled, d -> RenderType.getEntityCutoutNoCull(SADDLE_LAYER)));
    }

    public static ResourceLocation resource(String png) { return Wyrmroost.rl(BASE_PATH + "overworld_drake/" + png); }

    private ResourceLocation getArmorTexture(OWDrakeEntity drake)
    {
        String path = drake.getArmor().getRegistryName().getPath().replace("_dragon_armor", "");
        return resource("accessories/armor_" + path + ".png");
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

    class ArmorLayer extends LayerRenderer<OWDrakeEntity, OWDrakeModel>
    {
        public ArmorLayer() { super(OWDrakeRenderer.this); }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer type, int packedLightIn, OWDrakeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            if (entity.isArmored())
            {
                IVertexBuilder builder = type.getBuffer(RenderType.getEntityCutoutNoCull(getArmorTexture(entity)));
                if (entity.getStackInSlot(OWDrakeEntity.ARMOR_SLOT).hasEffect())
                    builder = VertexBuilderUtils.newDelegate(type.getBuffer(RenderType.getEntityGlint()), builder);
                getEntityModel().render(ms, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        }
    }
}
