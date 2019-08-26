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
    private final ResourceLocation body = ModUtils.location(loc + "body.png");
    private final ResourceLocation bodyAlb = ModUtils.location(loc + "body_alb.png");
    private final ResourceLocation bodyChristmas = ModUtils.location(loc + "body_christmas.png");
    private final ResourceLocation bodyGlow = ModUtils.location(loc + "body_glow.png");
    private final ResourceLocation sleep = ModUtils.location(loc + "closed_eyes.png");
    
    public RoostStalkerRenderer(EntityRendererManager manager) {
        super(manager, new RoostStalkerModel(), 0.5f);
        addLayer(new ItemStackRenderer(this));
        addLayer(new GlowLayer(this, bodyGlow));
        addLayer(new SleepLayer(this, sleep));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(RoostStalkerEntity stalker) {
        if (isChristmas) return bodyChristmas;
        return stalker.isAlbino()? bodyAlb : body;
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
                GlStateManager.translatef(0, -0.5f, stalker.isSitting()? -1.3f : -1.2f);
                GlStateManager.rotatef(headPitch / 1.7f, 1f, 0, 0);
                GlStateManager.translatef(0, -0.3f, 0f);
                
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
