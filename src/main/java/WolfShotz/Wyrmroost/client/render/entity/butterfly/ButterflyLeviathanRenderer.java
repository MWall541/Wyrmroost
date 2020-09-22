package WolfShotz.Wyrmroost.client.render.entity.butterfly;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.RenderHelper;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class ButterflyLeviathanRenderer extends AbstractDragonRenderer<ButterflyLeviathanEntity, ButterflyLeviathanModel>
{
    public static final ResourceLocation BLUE = resource("body_blue.png");
    public static final ResourceLocation PURPLE = resource("body_purple.png");
    // Special
    public static final ResourceLocation ALBINO = resource("body_albino.png");
    // Glow
    public static final ResourceLocation GLOW = resource("activated.png");

    private static final Material CONDUIT_CAGE_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/cage"));
    private static final Material CONDUIT_WIND_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/wind"));
    private static final Material CONDUIT_VERTICAL_WIND_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/wind_vertical"));
    private static final Material CONDUIT_OPEN_EYE_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/open_eye"));

    public ButterflyLeviathanRenderer(EntityRendererManager manager)
    {
        super(manager, new ButterflyLeviathanModel(), 2f);
        addLayer(new LightningLayer());
        addLayer(new ConduitLayer());
    }

    @Override
    protected void preRenderCallback(ButterflyLeviathanEntity entity, MatrixStack ms, float partialTicks)
    {
        super.preRenderCallback(entity, ms, partialTicks);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ButterflyLeviathanEntity entity)
    {
        switch (entity.getVariant())
        {
            case -1:
                return ALBINO;
            default:
            case 0:
                return BLUE;
            case 1:
                return PURPLE;
        }
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(BASE_PATH + "butterfly_leviathan/" + png);
    }

    public class LightningLayer extends LayerRenderer<ButterflyLeviathanEntity, ButterflyLeviathanModel>
    {
        public LightningLayer()
        {
            super(ButterflyLeviathanRenderer.this);
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer buffer, int packedLight, ButterflyLeviathanEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            Animation anim = entity.getAnimation();
            if (anim == ButterflyLeviathanEntity.ROAR_ANIMATION || anim == ButterflyLeviathanEntity.CONDUIT_ANIMATION)
            {
                // alpha calculations for the glow layer.
                float delta = (float) entity.getAnimationTick() / (float) anim.getDuration();
                float alpha = MathHelper.clamp(MathHelper.sin(delta * Mafs.PI) * 1.41f, 0, 1f);

                IVertexBuilder builder = buffer.getBuffer(RenderHelper.getTranslucentGlow(GLOW));
                getEntityModel().render(ms, builder, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
            }
        }
    }

    public class ConduitLayer extends LayerRenderer<ButterflyLeviathanEntity, ButterflyLeviathanModel>
    {
        public ModelRenderer conduitCage;
        public ModelRenderer conduitWind;
        public ModelRenderer conduitEye;

        public ConduitLayer()
        {
            super(ButterflyLeviathanRenderer.this);

            conduitCage = new ModelRenderer(16, 16, 0, 0);
            conduitWind = new ModelRenderer(64, 32, 0, 0);
            conduitEye = new ModelRenderer(32, 16, 0, 0);
            conduitCage.addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.01F);
            conduitWind.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
            conduitEye.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer buffer, int light, ButterflyLeviathanEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float tick, float netHeadYaw, float headPitch)
        {
            if ((entity.getAnimation() == ButterflyLeviathanEntity.CONDUIT_ANIMATION && entity.getAnimationTick() < 15) || !entity.hasConduit())
                return;

            int overlay = getPackedOverlay(entity, getOverlayProgress(entity, partialTicks));
            float rotation = (tick * -0.0375f) * (180f / Mafs.PI);
            float translation = MathHelper.sin(tick * 0.1F) / 2.0F + 0.5F;
            translation = translation * translation + translation;
            if (!entity.canSwim()) headPitch /= 2;

            ms.push();
            ms.translate(entityModel.head.rotationPointX / 16, entityModel.head.rotationPointY / 16, entityModel.head.rotationPointZ / 16);
            ms.rotate(Vector3f.YP.rotationDegrees(netHeadYaw / 2)); // rotate to match head rotations
            ms.rotate(Vector3f.XP.rotationDegrees(headPitch));
            ms.translate(0, -2.25, -3.65);

            // Eye
            ms.push();
            ms.translate(0, (0.3F + translation * 0.2F), 0);
            Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
            vector3f.normalize();
            ms.rotate(new Quaternion(vector3f, rotation, true));
            conduitEye.render(ms, CONDUIT_CAGE_TEXTURE.getBuffer(buffer, RenderType::getEntityCutoutNoCull), light, overlay);
            ms.pop();

            // Wind
            int gen = entity.ticksExisted / 66 % 3;
            ms.push();
            ms.translate(0, 0.5d, 0);
            if (gen == 1) ms.rotate(Vector3f.XP.rotationDegrees(90));
            else if (gen == 2) ms.rotate(Vector3f.ZP.rotationDegrees(90));
            IVertexBuilder builder = (gen == 1? CONDUIT_VERTICAL_WIND_TEXTURE : CONDUIT_WIND_TEXTURE).getBuffer(buffer, RenderType::getEntityCutoutNoCull);
            conduitWind.render(ms, builder, light, overlay);
            ms.pop();

            // Wind but its the second time
            ms.push();
            ms.scale(0.875f, 0.875f, 0.875f);
            ms.rotate(Vector3f.XP.rotationDegrees(180f));
            ms.rotate(Vector3f.ZP.rotationDegrees(180f));
            conduitWind.render(ms, builder, light, overlay);
            ms.pop();

            // The cage thingy
            ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
            ms.push();
            ms.translate(0, (0.3F + translation * 0.2F), 0);
            ms.scale(0.5F, 0.5F, 0.5F);
            float f3 = activerenderinfo.getYaw();
            ms.rotate(Vector3f.YP.rotationDegrees(f3));
            ms.rotate(Vector3f.XP.rotationDegrees(activerenderinfo.getPitch()));
            ms.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            float f4 = 1.3333334f;
            ms.scale(f4, f4, f4);
            conduitCage.render(ms, CONDUIT_OPEN_EYE_TEXTURE.getBuffer(buffer, RenderType::getEntityCutoutNoCull), light, overlay);
            ms.pop();

            ms.pop();
        }
    }
}
