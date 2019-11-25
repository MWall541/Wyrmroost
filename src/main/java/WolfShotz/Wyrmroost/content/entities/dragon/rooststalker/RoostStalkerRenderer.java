package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RoostStalkerRenderer extends AbstractDragonRenderer<RoostStalkerEntity>
{
    public static final ResourceLocation BODY          = resource("body.png");
    public static final ResourceLocation BODY_SPE      = resource("body_spe.png");
    public static final ResourceLocation BODY_XMAS     = resource("body_christmas.png");
    public static final ResourceLocation BODY_GLOW     = resource("body_glow.png");
    public static final ResourceLocation BODY_SPE_GLOW = resource("body_spe_glow.png");
    public static final ResourceLocation SLEEP         = resource("sleep.png");
    
    public RoostStalkerRenderer(EntityRendererManager manager) {
        super(manager, new RoostStalkerModel(), 0.5f);
        addLayer(ITEM_STACK_RENDERER);
        addLayer(new GlowLayer(stalker -> stalker.isSpecial()? BODY_SPE_GLOW : BODY_GLOW));
        addLayer(new SleepLayer(SLEEP));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(RoostStalkerEntity entity) {
        if (isChristmas) return BODY_XMAS;
        return entity.isSpecial()? BODY_SPE : BODY;
    }
    
    public static ResourceLocation resource(String png) { return ModUtils.resource(DEF_LOC + "rooststalker/" + png); }
    
    // Item Stack Layer Renderer
    @SuppressWarnings("deprecation") // This literally never works lmfao
    private final AbstractLayerRenderer ITEM_STACK_RENDERER = new AbstractLayerRenderer() {
        
        @Override
        public void render(RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ItemStack stack = stalker.getStackInSlot(0);
    
            if (!stack.isEmpty()) {
                float i = stalker.isChild()? 1f : 0;
        
                GlStateManager.pushMatrix();
        
                GlStateManager.rotatef(netHeadYaw / 3f, 0, 1f, 0);
                GlStateManager.rotatef(90, 1, 0, 0);
        
                if (stalker.isSleeping() && stalker.getAnimation() != RoostStalkerEntity.WAKE_ANIMATION) {
                    GlStateManager.translatef(-0.5f - (i * 2.8f), -0.6f - (i * 1.8f), -1.49f);
                    GlStateManager.rotatef(240, 0, 0, 1);
                } else {
                    GlStateManager.translatef(0, -0.5f - (i * -0.4f), (stalker.isSitting()? -1.3f : -1.2f) - (i * 0.135f));
                    GlStateManager.rotatef(headPitch / (1.7f - (i * -1f)), 1f, 0, 0);
                    GlStateManager.translatef(0, -0.3f, 0f);
                }
                if (stalker.isChild()) GlStateManager.scalef(0.45f, 0.45f, 0.45f);
        
                Minecraft.getInstance().getItemRenderer().renderItem(stack, stalker, ItemCameraTransforms.TransformType.GROUND, false);
        
                GlStateManager.popMatrix();
            }
        }
    };
}
