package WolfShotz.Wyrmroost.client.render.entity.butterfly;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.ButterflyLeviathanEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class ButterflyLeviathanRenderer extends AbstractDragonRenderer<ButterflyLeviathanEntity, ButterflyLeviathanModel>
{
    public static final ResourceLocation BLUE = resource("body_blue.png");
    public static final ResourceLocation PURPLE = resource("body_purple.png");
    // Special
    public static final ResourceLocation ALBINO = resource("body_albino.png");
    // Glow
    public static final ResourceLocation GLOW = resource("activated.png");

    public ButterflyLeviathanRenderer(EntityRendererManager manager)
    {
        super(manager, new ButterflyLeviathanModel(), 2f);
        addLayer(new GlowLayer(d -> GLOW).addCondition(ButterflyLeviathanRenderer::shouldRenderConduit));
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(BASE_PATH + "butterfly_leviathan/" + png);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ButterflyLeviathanEntity entity)
    {
        if (entity.getVariant() == -1) return ALBINO;
        switch (entity.getVariant())
        {
            default: // Fall back: WHAT VARIANT IS THIS?!
            case 0:
                return BLUE;
            case 1:
                return PURPLE;
        }
    }

    public static boolean shouldRenderConduit(ButterflyLeviathanEntity entity)
    {
        if (!entity.hasConduit()) return false;
        return entity.getAnimation() != ButterflyLeviathanEntity.CONDUIT_ANIMATION || entity.getAnimationTick() >= 15;
    }

    @Override
    public void render(ButterflyLeviathanEntity entity, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        super.render(entity, entityYaw, partialTicks, ms, bufferIn, packedLightIn);

        if (shouldRenderConduit(entity))
        {
            Vec3d vec3d = entity.getConduitPos(0, 0, 0);
            ms.push();
            ms.translate(vec3d.x, vec3d.y, vec3d.z);
            ConduitRenderer.render(entity, partialTicks, ms, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
            ms.pop();
        }
    }
}
