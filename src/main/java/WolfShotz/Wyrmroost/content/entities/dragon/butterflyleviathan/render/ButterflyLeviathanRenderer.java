package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
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
        addLayer(new GlowLayer(d -> GLOW, ButterflyLeviathanEntity::hasConduit));
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

    @Override
    public void doRender(ButterflyLeviathanEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if (entity.hasConduit())
        {
            Vec3d vec3d = QuikMaths.calculateYawAngle(entityYaw, 0, 4.2).add(x, y + entity.getEyeHeight() + 2, z);
            ConduitRenderer.render(renderManager.textureManager, entity.ticksExisted, vec3d.x, vec3d.y, vec3d.z, partialTicks);
        }
    }
}
