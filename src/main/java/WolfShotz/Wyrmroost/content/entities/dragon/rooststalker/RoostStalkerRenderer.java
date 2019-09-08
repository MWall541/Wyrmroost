package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("deprecation")
public class RoostStalkerRenderer extends AbstractDragonRenderer<RoostStalkerEntity>
{
    private final String loc = DEF_LOC + "rooststalker/";
    private final ResourceLocation BODY = ModUtils.location(loc + "body.png");
    private final ResourceLocation BODY_SPE = ModUtils.location(loc + "body_spe.png");
    private final ResourceLocation BODY_XMAS = ModUtils.location(loc + "body_christmas.png");
    private final ResourceLocation BODY_GLOW = ModUtils.location(loc + "body_glow.png");
    private final ResourceLocation BODY_SPE_GLOW = ModUtils.location(loc + "body_spe_glow.png");
    private final ResourceLocation SLEEP = ModUtils.location(loc + "sleep.png");
    
    public RoostStalkerRenderer(EntityRendererManager manager) {
        super(manager, new RoostStalkerModel(), 0.5f);
        addLayer(new ItemStackRenderer(this));
        addLayer(new GlowLayer(this, stalker -> stalker.isSpecial()? BODY_SPE_GLOW : BODY_GLOW));
        addLayer(new SleepLayer(this, SLEEP));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(RoostStalkerEntity stalker) {
        if (isChristmas) return BODY_XMAS;
        return stalker.isSpecial()? BODY_SPE : BODY;
    }
    
    class ItemStackRenderer extends AbstractLayerRenderer
    {
        ItemStackRenderer(IEntityRenderer entity) { super(entity); }
        
        @Override
        public void render(RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ItemStack stack = stalker.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
    
                GlStateManager.rotatef(netHeadYaw / 3f, 0, 1f, 0);
                GlStateManager.rotatef(90, 1, 0, 0);
                
                if (stalker.isSleeping() && stalker.getAnimation() != RoostStalkerEntity.WAKE_ANIMATION) {
                    GlStateManager.translatef(-0.5f, -0.6f, -1.49f);
                    GlStateManager.rotatef(240, 0, 0, 1);
                } else {
                    GlStateManager.translatef(0, -0.5f, stalker.isSitting()? -1.3f : -1.2f);
                    GlStateManager.rotatef(headPitch / 1.7f, 1f, 0, 0);
                    GlStateManager.translatef(0, -0.3f, 0f);
                }
                
                Minecraft.getInstance().getItemRenderer().renderItem(stack, stalker, ItemCameraTransforms.TransformType.GROUND, false);
                
                GlStateManager.popMatrix();
            }
        }
        
        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
