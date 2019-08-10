package WolfShotz.Wyrmroost.content.entities.rooststalker;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RoostStalkerRenderer extends AbstractDragonRenderer<RoostStalkerEntity>
{
    private static final String LOC = DEF_LOC + "stalker/";
    private ResourceLocation male = ModUtils.location(LOC + "male.png");
    private ResourceLocation female = ModUtils.location(LOC + "female.png");
    private ResourceLocation maleAlb = ModUtils.location(LOC + "male_alb.png");
    private ResourceLocation femaleAlb = ModUtils.location(LOC + "female_alb.png");
    
    public RoostStalkerRenderer(EntityRendererManager manager) {
        super(manager, new RooststalkerModel(), 0.5f);
        addLayer(new ItemStackRenderer(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(RoostStalkerEntity stalker) {
        if (stalker.isAlbino()) return stalker.getGender()? maleAlb : femaleAlb;
        return stalker.getGender()? male : female;
    }
    
    class ItemStackRenderer extends AbstractLayerRenderer<RoostStalkerEntity>
    {
        public ItemStackRenderer(IEntityRenderer entity) {
            super(entity);
        }
    
        @Override
        public void render(RoostStalkerEntity stalker, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
            ItemStack stack = stalker.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            AdvancedRendererModel head = (AdvancedRendererModel) getEntityModel().boxList.get(13);
            
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
                
                GlStateManager.translatef(head.rotationPointX / 16f, head.rotationPointY / 16f, head.rotationPointZ / 16f);
                GlStateManager.rotatef(p_212842_6_, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(p_212842_7_, 1.0F, 0.0F, 0.0F);
//                GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
//                GlStateManager.translatef(0, 1.15f, -0.8f);
    
                GlStateManager.translatef(0.06F, 0.27F, -0.5F);
                
                Minecraft.getInstance().getItemRenderer().renderItem(stack, stalker, ItemCameraTransforms.TransformType.GROUND, false);
    
                GlStateManager.popMatrix();
            }
        }
    
        @Override
        public boolean shouldCombineTextures() { return false; }
    }
}
