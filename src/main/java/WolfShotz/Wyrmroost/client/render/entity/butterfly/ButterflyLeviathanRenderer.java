package WolfShotz.Wyrmroost.client.render.entity.butterfly;

import WolfShotz.Wyrmroost.*;
import WolfShotz.Wyrmroost.client.render.entity.*;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import javax.annotation.*;

public class ButterflyLeviathanRenderer extends AbstractDragonRenderer<ButterflyLeviathanEntity>
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
        addLayer(new GlowLayer(d -> GLOW, ButterflyLeviathanRenderer::shouldRenderConduit));
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(DEF_LOC + "butterflyleviathan/" + png);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ButterflyLeviathanEntity entity)
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
    public void doRender(ButterflyLeviathanEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if (shouldRenderConduit(entity))
        {
            Vec3d vec3d = entity.getConduitPos(new Vec3d(x, y, z));
            ConduitRenderer.render(renderManager.textureManager, entity.ticksExisted, vec3d.x, vec3d.y, vec3d.z, partialTicks);
        }
    }
}
