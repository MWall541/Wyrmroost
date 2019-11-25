package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ButterflyLeviathanRenderer extends AbstractDragonRenderer<ButterflyLeviathanEntity>
{
    public static final ResourceLocation BLUE   = resource("butterfly_leviathan.png");
    public static final ResourceLocation PURPLE = resource("butterfly_leviathan_purple.png");
    // Special
    public static final ResourceLocation ALBINO = resource("butterfly_leviathan_alb.png");
    // Glow
    public static final ResourceLocation GLOW   = resource("butterfly_leviathan_activated.png");
    
    public ButterflyLeviathanRenderer(EntityRendererManager manager) {
        super(manager, new ButterflyLeviathanModel(), 2f);
        addLayer(new GlowLayer(d -> GLOW, ButterflyLeviathanEntity::hasConduit));
    }
    
    @Override
    public void doRender(ButterflyLeviathanEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        
        if (entity.hasConduit()) {
            double angle = entity.rotationYawHead * (Math.PI / 180d) + 1.570796d;
            double offsetX = x + 4.2d * Math.cos(angle);
            double offsetY = y - (entity.rotationPitch / 20) + 4;
            double offsetZ = z + 4.2d * Math.sin(angle);
            ConduitRenderer.render(entity, this::bindTexture, offsetX, offsetY, offsetZ, partialTicks);
        }
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ButterflyLeviathanEntity entity) {
        if (entity.isSpecial()) return ALBINO;
        switch (entity.getVariant()) {
            default: // Fall back: WHAT VARIANT IS THIS?!
            case 0: return BLUE;
            case 1: return PURPLE;
        }
    }
    
    public static ResourceLocation resource(String png) { return ModUtils.resource(DEF_LOC + "butterflyleviathan/" + png); }
}
