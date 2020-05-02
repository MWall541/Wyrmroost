package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker;

import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.ModelAnimator;
import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedLivingEntityModel;
import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedRendererModel;
import com.mojang.blaze3d.platform.GlStateManager;

/**
 * Roost stalker - nova
 * Created using Tabula 7.0.1
 */
public class RoostStalkerModel extends AdvancedLivingEntityModel<RoostStalkerEntity>
{
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
    
    public AdvancedRendererModel[] tailSegments;
    
    public ModelAnimator animator;
    
    public RoostStalkerModel()
    {
        this.textureWidth = 80;
        this.textureHeight = 90;
        this.legl1 = new AdvancedRendererModel(this, 20, 72);
        this.legl1.setRotationPoint(-2.5F, 0.0F, -5.0F);
        this.legl1.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl1, 0.091106186954104F, -0.31869712141416456F, -0.33946653951289707F);
        this.tail3 = new AdvancedRendererModel(this, 0, 55);
        this.tail3.setRotationPoint(0.0F, 0.0F, 6.5F);
        this.tail3.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 9, 0.0F);
        this.setRotateAngle(tail3, -0.091106186954104F, 0.0F, 0.0F);
        this.torso = new AdvancedRendererModel(this, 0, 0);
        this.torso.setRotationPoint(0.0F, 31.0F, 0.0F);
        this.torso.addBox(-3.5F, -3.5F, -7.5F, 7, 7, 15, 0.0F);
        this.hornl = new AdvancedRendererModel(this, 40, 25);
        this.hornl.mirror = true;
        this.hornl.setRotationPoint(1.5F, -2.0F, -1.5F);
        this.hornl.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(hornl, 0.6829473363053812F, 0.27314402793711257F, 0.18203784098300857F);
        this.footl1_1 = new AdvancedRendererModel(this, 20, 80);
        this.footl1_1.mirror = true;
        this.footl1_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.footl1_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl1_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.legl3 = new AdvancedRendererModel(this, 20, 72);
        this.legl3.setRotationPoint(-2.5F, 0.0F, 5.0F);
        this.legl3.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl3, -0.091106186954104F, 0.31869712141416456F, -0.31869712141416456F);
        this.footl2 = new AdvancedRendererModel(this, 20, 80);
        this.footl2.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.footl2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl2, 0.0F, 0.0F, 0.31869712141416456F);
        this.footl3_1 = new AdvancedRendererModel(this, 20, 80);
        this.footl3_1.mirror = true;
        this.footl3_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.footl3_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl3_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.tail2 = new AdvancedRendererModel(this, 0, 40);
        this.tail2.setRotationPoint(0.0F, 0.0F, 6.5F);
        this.tail2.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 7, 0.0F);
        this.setRotateAngle(tail2, -0.091106186954104F, 0.0F, 0.0F);
        this.legr2 = new AdvancedRendererModel(this, 20, 72);
        this.legr2.mirror = true;
        this.legr2.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.legr2.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr2, 0.0F, 0.0F, 0.31869712141416456F);
        this.neck = new AdvancedRendererModel(this, 45, 0);
        this.neck.setRotationPoint(0.0F, -0.5F, -6.5F);
        this.neck.addBox(-2.0F, -2.5F, -4.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(neck, -0.18203784098300857F, 0.0F, 0.0F);
        this.jaw = new AdvancedRendererModel(this, 35, 60);
        this.jaw.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.jaw.addBox(-2.51F, 0.0F, -10.0F, 5, 2, 10, 0.0F);
        this.footl1 = new AdvancedRendererModel(this, 20, 80);
        this.footl1.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.footl1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl1, 0.0F, 0.0F, 0.31869712141416456F);
        this.head = new AdvancedRendererModel(this, 35, 40);
        this.head.setRotationPoint(0.0F, -0.5F, -3.0F);
        this.head.addBox(-2.5F, -3.0F, -10.0F, 5, 4, 10, 0.0F);
        this.setRotateAngle(head, 0.18203784098300857F, 0.0F, 0.0F);
        this.legl2 = new AdvancedRendererModel(this, 20, 72);
        this.legl2.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.legl2.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl2, 0.0F, 0.0F, -0.31869712141416456F);
        this.footl3 = new AdvancedRendererModel(this, 20, 80);
        this.footl3.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.footl3.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl3, 0.0F, 0.0F, 0.31869712141416456F);
        this.tail1 = new AdvancedRendererModel(this, 0, 25);
        this.tail1.setRotationPoint(0.0F, -0.5F, 7.0F);
        this.tail1.addBox(-2.5F, -2.4F, 0.0F, 5, 5, 7, 0.0F);
        this.setRotateAngle(tail1, -0.091106186954104F, 0.0F, 0.0F);
        this.hornl_1 = new AdvancedRendererModel(this, 40, 25);
        this.hornl_1.setRotationPoint(-1.5F, -2.0F, -1.5F);
        this.hornl_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(hornl_1, 0.6829473363053812F, -0.27314402793711257F, -0.18203784098300857F);
        this.footl2_1 = new AdvancedRendererModel(this, 20, 80);
        this.footl2_1.mirror = true;
        this.footl2_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.footl2_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl2_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.legr3 = new AdvancedRendererModel(this, 20, 72);
        this.legr3.mirror = true;
        this.legr3.setRotationPoint(2.5F, 0.0F, 5.0F);
        this.legr3.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr3, -0.091106186954104F, -0.31869712141416456F, 0.31869712141416456F);
        this.legr1 = new AdvancedRendererModel(this, 20, 72);
        this.legr1.mirror = true;
        this.legr1.setRotationPoint(2.5F, 0.0F, -5.0F);
        this.legr1.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr1, 0.091106186954104F, 0.31869712141416456F, 0.31869712141416456F);
        this.torso.addChild(legl1);
        this.tail2.addChild(tail3);
        this.head.addChild(hornl);
        this.legr1.addChild(footl1_1);
        this.torso.addChild(legl3);
        this.legl2.addChild(footl2);
        this.legr3.addChild(footl3_1);
        this.tail1.addChild(tail2);
        this.torso.addChild(legr2);
        this.torso.addChild(neck);
        this.head.addChild(jaw);
        this.legl1.addChild(footl1);
        this.neck.addChild(head);
        this.torso.addChild(legl2);
        this.legl3.addChild(footl3);
        this.torso.addChild(tail1);
        this.head.addChild(hornl_1);
        this.legr2.addChild(footl2_1);
        this.torso.addChild(legr3);
        this.torso.addChild(legr1);
        
        tailSegments = new AdvancedRendererModel[]{tail1, tail2, tail3};
        
        animator = ModelAnimator.create();
        
        updateDefaultPose();
    }
    
    private float globalSpeed = 0.5f;
    
    @Override
    public void render(RoostStalkerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        GlStateManager.pushMatrix();

        if (entity.isChild())
        {
            GlStateManager.translatef(0, 0.78f, 0);
            GlStateManager.scaled(0.3d, 0.3d, 0.3d);
        }
        else GlStateManager.scaled(0.625d, 0.625d, 0.625d);
        
        torso.render(scale);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        if (netHeadYaw < -180) netHeadYaw += 360;
        else if (netHeadYaw > 180) netHeadYaw -= 360;
        if (stalker.getAnimation() != RoostStalkerEntity.SCAVENGE_ANIMATION)
            faceTarget(netHeadYaw, headPitch, 2, head);
    }
    
    @Override
    public void setLivingAnimations(RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float partialTick)
    {
        Animation currentAnim = stalker.getAnimation();
        float frame = stalker.ticksExisted;
        globalSpeed = 0.5f;
        
        animator.update(stalker);
        resetToDefaultPose();
        
        if (!stalker.isSitting())
        {
            swing(legl1, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
            swing(legl2, globalSpeed, 0.7f, true, 0, 0, limbSwing, limbSwingAmount);
            swing(legl3, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
            
            swing(legr1, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
            swing(legr2, globalSpeed, 0.7f, true, 0, 0, limbSwing, limbSwingAmount);
            swing(legr3, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
        }
        
        if (stalker.isSleeping() && currentAnim != RoostStalkerEntity.SLEEP_ANIMATION)
            staySleep();
        
        if (stalker.isSitting() && !stalker.isSleeping())
            staySit();
        
        if (currentAnim == RoostStalkerEntity.SLEEP_ANIMATION) sleepAnim();
        
        if (currentAnim == RoostStalkerEntity.WAKE_ANIMATION)
        {
            staySleep();
            wakeAnim();
        }
        
        if (currentAnim == RoostStalkerEntity.SCAVENGE_ANIMATION)
            scavengeAnim(stalker.getAnimationTick(), frame);

        boolean flag = stalker.getItem().isEmpty() || stalker.isSleeping();
        idle(frame, flag);
        
        if (!flag) jaw.rotateAngleX = 0.15f;
    }
    
    public void idle(float frame, boolean head)
    {
        chainWave(tailSegments, globalSpeed - 0.44f, 0.08f, 2, frame, 0.5f);
        chainSwing(tailSegments, globalSpeed - 0.45f, 0.08f, 0, frame, 0.5f);
        if (head)
        {
            walk(jaw, globalSpeed - 0.4f, 0.1f, false, 0, 0.1f, frame, 0.5f);
            chainWave(new AdvancedRendererModel[]{this.head, this.neck}, globalSpeed - 0.4f, 0.05f, 2, frame, 0.5f);
        }
    }
    
    private void staySit()
    {
        torso.offsetY = 0.21f;
        
        tail1.rotateAngleX = 0.01f;
        
        float legAngle = 1.4f;
        footl1.rotateAngleZ = legAngle;
        footl2.rotateAngleZ = legAngle;
        footl3.rotateAngleZ = legAngle;
        
        footl1_1.rotateAngleZ = -legAngle;
        footl2_1.rotateAngleZ = -legAngle;
        footl3_1.rotateAngleZ = -legAngle;
    }
    
    private void staySleep()
    {
        torso.offsetY = 0.21f;
        torso.rotateAngleZ = 1.7f;
        head.rotateAngleX = 1.3f;
        head.rotateAngleY = -0.2f;
        
        for (AdvancedRendererModel segment : tailSegments)
        {
            segment.rotateAngleX = -0.7f;
            segment.rotateAngleZ = -0.1f;
        }
        
        float legAngle = 0.7f;
        legr1.rotateAngleZ = legAngle;
        footl1_1.rotateAngleZ = -legAngle;
        legr2.rotateAngleZ = legAngle + 0.09f;
        footl2_1.rotateAngleZ = -legAngle;
        legr3.rotateAngleZ = legAngle;
        footl3_1.rotateAngleZ = -legAngle;
        
        legl1.rotateAngleZ = -legAngle;
        footl1.rotateAngleZ = legAngle - 1f;
        legl2.rotateAngleZ = -legAngle;
        footl2.rotateAngleZ = legAngle - 1f;
        legl3.rotateAngleZ = -legAngle;
        footl3.rotateAngleZ = legAngle - 1f;
    }
    
    private void sleepAnim()
    {
        animator.setAnimation(RoostStalkerEntity.SLEEP_ANIMATION);
        
        animator.startKeyframe(20);
        animator.rotate(torso, 0, 0, 1.7f);
        animator.move(torso, 0, 3.4f, 0);
        animator.rotate(head, 1f, -0.2f, 0);
        
        animator.rotate(legl1, 0, 0, -0.35f);
        animator.rotate(footl1, 0, 0, -0.61f);
        animator.rotate(legl2, 0, 0, -0.37f);
        animator.rotate(footl2, 0, 0, -0.62f);
        animator.rotate(legl3, 0, 0, -0.37f);
        animator.rotate(footl3, 0, 0, -0.61f);
        
        animator.rotate(legr1, 0, 0, 0.37f);
        animator.rotate(footl1_1, 0, 0, -0.35f);
        animator.rotate(legr2, 0, 0, 0.48f);
        animator.rotate(footl2_1, 0, 0, -0.38f);
        animator.rotate(legr3, 0, 0, 0.4f);
        animator.rotate(footl3_1, 0, 0, -0.38f);
        
        for (AdvancedRendererModel segment : tailSegments) animator.rotate(segment, -0.7f, 0, -0.1f);
        animator.endKeyframe();
    }
    
    private void wakeAnim()
    {
        animator.setAnimation(RoostStalkerEntity.WAKE_ANIMATION);
        
        animator.startKeyframe(15);
        animator.rotate(torso, 0, 0, -1.7f);
        animator.move(torso, 0, -3.4f, 0);
        animator.rotate(head, -1f, 0.2f, 0);
        
        animator.rotate(legl1, 0, 0, 0.35f);
        animator.rotate(footl1, 0, 0, 0.61f);
        animator.rotate(legl2, 0, 0, 0.37f);
        animator.rotate(footl2, 0, 0, 0.62f);
        animator.rotate(legl3, 0, 0, 0.37f);
        animator.rotate(footl3, 0, 0, 0.61f);
        
        animator.rotate(legr1, 0, 0, -0.37f);
        animator.rotate(footl1_1, 0, 0, 0.35f);
        animator.rotate(legr2, 0, 0, -0.48f);
        animator.rotate(footl2_1, 0, 0, -0.38f);
        animator.rotate(legr3, 0, 0, -0.4f);
        animator.rotate(footl3_1, 0, 0, 0.38f);
        
        for (AdvancedRendererModel segment : tailSegments) animator.rotate(segment, 0.7f, 0, 0.1f);
        animator.endKeyframe();
    }
    
    /**
     * Bob the head up and down: makes it look like its "ruffling through a chest"
     */
    private void scavengeAnim(int animationTick, float frame)
    {
        animator.setAnimation(RoostStalkerEntity.SCAVENGE_ANIMATION);
        
        animator.startKeyframe(5);
        animator.rotate(neck, 1.5f, 0, 0);
        animator.endKeyframe();
        
        if (animationTick > 5) walk(neck, globalSpeed + 0.5f, 0.4f, false, 0, 0, frame, 0.5f);
        
        animator.setStaticKeyframe(25);
        
        animator.resetKeyframe(5);
    }
}