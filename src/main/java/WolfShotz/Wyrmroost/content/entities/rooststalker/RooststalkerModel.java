package WolfShotz.Wyrmroost.content.entities.rooststalker;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * Roost stalker - nova
 * Created using Tabula 7.0.1
 */
public class RooststalkerModel extends AdvancedEntityModel {
    public AdvancedRendererModel torso;
    public AdvancedRendererModel tail1;
    public AdvancedRendererModel legl1;
    public AdvancedRendererModel legl2;
    public AdvancedRendererModel legl3;
    public AdvancedRendererModel legr1;
    public AdvancedRendererModel legr2;
    public AdvancedRendererModel legr3;
    public AdvancedRendererModel neck;
    public AdvancedRendererModel tail2;
    public AdvancedRendererModel tail3;
    public AdvancedRendererModel footl1;
    public AdvancedRendererModel footl2;
    public AdvancedRendererModel footl3;
    public AdvancedRendererModel footl1_1;
    public AdvancedRendererModel footl2_1;
    public AdvancedRendererModel footl3_1;
    public AdvancedRendererModel head;
    public AdvancedRendererModel jaw;
    public AdvancedRendererModel hornl;
    public AdvancedRendererModel hornl_1;
    
    private AdvancedRendererModel[] headArray;
    
    public ModelAnimator animator;

    public RooststalkerModel() {
        textureWidth = 80;
        textureHeight = 90;
        legl1 = new AdvancedRendererModel(this, 0, 72);
        legl1.setRotationPoint(-2.5F, 0.0F, -5.0F);
        legl1.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        setRotateAngle(legl1, 0.091106186954104F, -0.31869712141416456F, -0.33953459193752383F);
        tail3 = new AdvancedRendererModel(this, 0, 55);
        tail3.setRotationPoint(0.0F, 0.0F, 6.5F);
        tail3.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 9, 0.0F);
        torso = new AdvancedRendererModel(this, 0, 0);
        torso.setRotationPoint(0.0F, 31.0F, 0.0F);
        torso.addBox(-3.5F, -3.5F, -7.5F, 7, 7, 15, 0.0F);
        hornl = new AdvancedRendererModel(this, 40, 25);
        hornl.setRotationPoint(1.5F, -2.0F, -1.5F);
        hornl.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        setRotateAngle(hornl, 0.6829473363053812F, 0.27314402793711257F, 0.18203784098300857F);
        footl1_1 = new AdvancedRendererModel(this, 20, 80);
        footl1_1.mirror = true;
        footl1_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        footl1_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(footl1_1, 0.0F, 0.0F, -0.31869712141416456F);
        legl3 = new AdvancedRendererModel(this, 0, 72);
        legl3.setRotationPoint(-2.5F, 0.0F, 5.0F);
        legl3.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        setRotateAngle(legl3, -0.091106186954104F, 0.31869712141416456F, -0.31869712141416456F);
        footl2 = new AdvancedRendererModel(this, 0, 80);
        footl2.setRotationPoint(-4.0F, 0.0F, 0.0F);
        footl2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(footl2, 0.0F, 0.0F, 0.31869712141416456F);
        footl3_1 = new AdvancedRendererModel(this, 20, 80);
        footl3_1.mirror = true;
        footl3_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        footl3_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(footl3_1, 0.0F, 0.0F, -0.31869712141416456F);
        tail2 = new AdvancedRendererModel(this, 0, 40);
        tail2.setRotationPoint(0.0F, 0.0F, 6.5F);
        tail2.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 7, 0.0F);
        setRotateAngle(tail2, -0.136659280431156F, 0.0F, 0.0F);
        legr2 = new AdvancedRendererModel(this, 20, 72);
        legr2.mirror = true;
        legr2.setRotationPoint(3.0F, 0.0F, 0.0F);
        legr2.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        setRotateAngle(legr2, 0.0F, 0.0F, 0.31869712141416456F);
        neck = new AdvancedRendererModel(this, 45, 0);
        neck.setRotationPoint(0.0F, -0.5F, -6.5F);
        neck.addBox(-2.0F, -2.5F, -4.0F, 4, 5, 4, 0.0F);
        setRotateAngle(neck, -0.18203784098300857F, 0.0F, 0.0F);
        jaw = new AdvancedRendererModel(this, 35, 60);
        jaw.setRotationPoint(0.0F, 1.0F, 0.0F);
        jaw.addBox(-2.51F, 0.0F, -10.0F, 5, 2, 10, 0.0F);
        setRotateAngle(jaw, 0.091106186954104F, 0.0F, 0.0F);
        footl1 = new AdvancedRendererModel(this, 0, 80);
        footl1.setRotationPoint(-4.0F, 0.0F, 0.0F);
        footl1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(footl1, 0.0F, 0.0F, 0.31869712141416456F);
        head = new AdvancedRendererModel(this, 35, 40);
        head.setRotationPoint(0.0F, -0.5F, -3.0F);
        head.addBox(-2.5F, -3.0F, -10.0F, 5, 4, 10, 0.0F);
        setRotateAngle(head, 0.27314402793711257F, 0.0F, 0.0F);
        legl2 = new AdvancedRendererModel(this, 0, 72);
        legl2.setRotationPoint(-3.0F, 0.0F, 0.0F);
        legl2.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        setRotateAngle(legl2, 0.0F, 0.0F, -0.31869712141416456F);
        footl3 = new AdvancedRendererModel(this, 0, 80);
        footl3.setRotationPoint(-4.0F, 0.0F, 0.0F);
        footl3.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(footl3, 0.0F, 0.0F, 0.31869712141416456F);
        tail1 = new AdvancedRendererModel(this, 0, 25);
        tail1.setRotationPoint(0.0F, -0.5F, 7.0F);
        tail1.addBox(-2.5F, -2.4F, 0.0F, 5, 5, 7, 0.0F);
        setRotateAngle(tail1, -0.136659280431156F, 0.0F, 0.0F);
        hornl_1 = new AdvancedRendererModel(this, 40, 25);
        hornl_1.setRotationPoint(-1.5F, -2.0F, -1.5F);
        hornl_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        setRotateAngle(hornl_1, 0.6829473363053812F, -0.27314402793711257F, -0.18203784098300857F);
        footl2_1 = new AdvancedRendererModel(this, 20, 80);
        footl2_1.mirror = true;
        footl2_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        footl2_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(footl2_1, 0.0F, 0.0F, -0.31869712141416456F);
        legr3 = new AdvancedRendererModel(this, 20, 72);
        legr3.mirror = true;
        legr3.setRotationPoint(2.5F, 0.0F, 5.0F);
        legr3.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        setRotateAngle(legr3, -0.091106186954104F, -0.31869712141416456F, 0.31869712141416456F);
        legr1 = new AdvancedRendererModel(this, 20, 72);
        legr1.mirror = true;
        legr1.setRotationPoint(2.5F, 0.0F, -5.0F);
        legr1.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        setRotateAngle(legr1, 0.091106186954104F, 0.31869712141416456F, 0.31869712141416456F);
        torso.addChild(legl1);
        tail2.addChild(tail3);
        head.addChild(hornl);
        legr1.addChild(footl1_1);
        torso.addChild(legl3);
        legl2.addChild(footl2);
        legr3.addChild(footl3_1);
        tail1.addChild(tail2);
        torso.addChild(legr2);
        torso.addChild(neck);
        head.addChild(jaw);
        legl1.addChild(footl1);
        neck.addChild(head);
        torso.addChild(legl2);
        legl3.addChild(footl3);
        torso.addChild(tail1);
        head.addChild(hornl_1);
        legr2.addChild(footl2_1);
        torso.addChild(legr3);
        torso.addChild(legr1);
        
        headArray = new AdvancedRendererModel[] {neck, head};
        
        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    private float globalSpeed = 0.5f;
    private float f = 0.5f;
    
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
//        faceTarget(netHeadYaw, headPitch, 1, head);
        head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.625d, 0.625d, 0.625d);
        torso.render(scale);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setLivingAnimations(Entity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
//        resetToDefaultPose();
        RoostStalkerEntity stalker = (RoostStalkerEntity) entityIn;
        
        animator.update(stalker);
    }
}
