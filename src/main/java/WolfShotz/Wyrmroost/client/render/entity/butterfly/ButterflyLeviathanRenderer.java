package WolfShotz.Wyrmroost.client.render.entity.butterfly;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.ButterflyLeviathanEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ButterflyLeviathanRenderer extends AbstractDragonRenderer<ButterflyLeviathanEntity, ButterflyLeviathanModel>
{
    public static final ResourceLocation BLUE = resource("butterfly_leviathan.png");
    public static final ResourceLocation PURPLE = resource("butterfly_leviathan_purple.png");
    // Special
    public static final ResourceLocation ALBINO = resource("butterfly_leviathan_alb.png");
    // Glow
    public static final ResourceLocation GLOW = resource("butterfly_leviathan_activated.png");

    public ButterflyLeviathanRenderer(EntityRendererManager manager)
    {
        super(manager, new ButterflyLeviathanModel(), 2f);
        addLayer(new GlowLayer(ButterflyLeviathanRenderer::shouldRenderConduit, d -> GLOW));
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(BASE_PATH + "butterflyleviathan/" + png);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ButterflyLeviathanEntity entity)
    {
        if (entity.isSpecial()) return ALBINO;
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
    public void render(ButterflyLeviathanEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

//        if (shouldRenderConduit(entity))
//        {
//            Vec3d vec3d = entity.getConduitPos(new Vec3d(x, y, z));
//            ConduitRenderer.render(renderManager.textureManager, entity.ticksExisted, vec3d.x, vec3d.y, vec3d.z, partialTicks);
//        }
    }
}
