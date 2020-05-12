package WolfShotz.Wyrmroost.client.render.entity.butterfly;

import WolfShotz.Wyrmroost.content.entities.dragon.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.util.QuikMaths;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ConduitRenderer
{
    public static final Material CAGE_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/cage"));
    public static final Material WIND_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/wind"));
    public static final Material VERTICAL_WIND_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/wind_vertical"));
    public static final Material OPEN_EYE_TEXTURE = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/conduit/open_eye"));

    private static final ModelRenderer CAGE_MODEL = new ModelRenderer(16, 16, 0, 0);
    private static final ModelRenderer WIND_MODEL = new ModelRenderer(64, 32, 0, 0);
    private static final ModelRenderer EYE = new ModelRenderer(32, 16, 0, 0);

    static
    {
        CAGE_MODEL.addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.01F);
        WIND_MODEL.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
        EYE.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }

    public static void render(ButterflyLeviathanEntity entity, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
    {
        float f = (float) entity.ticksExisted + partialTicks;
        float f1 = (f * -0.0375f) * (180f / QuikMaths.PI);
        float f2 = MathHelper.sin(f * 0.1F) / 2.0F + 0.5F;
        f2 = f2 * f2 + f2;

        // Eye
        ms.push();
        ms.translate(0, (0.3F + f2 * 0.2F), 0);
        Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
        vector3f.normalize();
        ms.rotate(new Quaternion(vector3f, f1, true));
        EYE.render(ms, CAGE_TEXTURE.getBuffer(buffer, RenderType::getEntityCutoutNoCull), combinedLightIn, combinedOverlayIn);
        ms.pop();

        // Wind
        int i = entity.ticksExisted / 66 % 3;
        ms.push();
        ms.translate(0, 0.5D, 0);
        if (i == 1) ms.rotate(Vector3f.XP.rotationDegrees(90.0F));
        else if (i == 2) ms.rotate(Vector3f.ZP.rotationDegrees(90.0F));
        IVertexBuilder builder = (i == 1? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).getBuffer(buffer, RenderType::getEntityCutoutNoCull);
        WIND_MODEL.render(ms, builder, combinedLightIn, combinedOverlayIn);
        ms.pop();

        // Wind but its the second time
        ms.push();
        ms.scale(0.875F, 0.875F, 0.875F);
        ms.rotate(Vector3f.XP.rotationDegrees(180.0F));
        ms.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        WIND_MODEL.render(ms, builder, combinedLightIn, combinedOverlayIn);
        ms.pop();

        // The cage thingy
        ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        ms.push();
        ms.translate(0, (0.3F + f2 * 0.2F), 0);
        ms.scale(0.5F, 0.5F, 0.5F);
        float f3 = -activerenderinfo.getYaw();
        ms.rotate(Vector3f.YP.rotationDegrees(f3));
        ms.rotate(Vector3f.XP.rotationDegrees(activerenderinfo.getPitch()));
        ms.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        float f4 = 1.3333334F;
        ms.scale(f4, f4, f4);
        CAGE_MODEL.render(ms, OPEN_EYE_TEXTURE.getBuffer(buffer, RenderType::getEntityCutoutNoCull), combinedLightIn, combinedOverlayIn);
        ms.pop();
    }
}
