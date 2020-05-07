package WolfShotz.Wyrmroost.client.render.entity.owdrake;

import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.client.animation.ModelAnimator;
import WolfShotz.Wyrmroost.client.model.AdvancedLivingEntityModel;
import WolfShotz.Wyrmroost.client.model.AdvancedRendererModel;
import WolfShotz.Wyrmroost.content.entities.dragon.OWDrakeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

/**
 * WR Overworld Drake - Ukan
 * Created using Tabula 7.0.1
 */
public class OWDrakeModel extends AdvancedLivingEntityModel<OWDrakeEntity>
{
    public AdvancedRendererModel body1;
    public AdvancedRendererModel body2;
    public AdvancedRendererModel neck1;
    public AdvancedRendererModel arm1L;
    public AdvancedRendererModel arm1R;
    public AdvancedRendererModel tail1;
    public AdvancedRendererModel leg1L;
    public AdvancedRendererModel leg1R;
    public AdvancedRendererModel tail2;
    public AdvancedRendererModel tail3;
    public AdvancedRendererModel tail4;
    public AdvancedRendererModel tail5;
    public AdvancedRendererModel leg2L;
    public AdvancedRendererModel leg3L;
    public AdvancedRendererModel footL;
    public AdvancedRendererModel toe2L;
    public AdvancedRendererModel toe1L;
    public AdvancedRendererModel toe1L_1;
    public AdvancedRendererModel leg2R;
    public AdvancedRendererModel leg3R;
    public AdvancedRendererModel footR;
    public AdvancedRendererModel toe2R;
    public AdvancedRendererModel toe1R;
    public AdvancedRendererModel toe1R_1;
    public AdvancedRendererModel neck2;
    public AdvancedRendererModel head;
    public AdvancedRendererModel snout;
    public AdvancedRendererModel jaw;
    public AdvancedRendererModel eyeL;
    public AdvancedRendererModel eyeR;
    public AdvancedRendererModel horn11L;
    public AdvancedRendererModel horn11R;
    public AdvancedRendererModel horn31L;
    public AdvancedRendererModel horn31R;
    public AdvancedRendererModel horn51;
    public AdvancedRendererModel horn52;
    public AdvancedRendererModel horn53;
    public AdvancedRendererModel horn12L;
    public AdvancedRendererModel horn12R;
    public AdvancedRendererModel horn32L;
    public AdvancedRendererModel horn33L;
    public AdvancedRendererModel horn32R;
    public AdvancedRendererModel horn33R;
    public AdvancedRendererModel arm2L;
    public AdvancedRendererModel palmL;
    public AdvancedRendererModel claw21L;
    public AdvancedRendererModel claw11L;
    public AdvancedRendererModel claw22L;
    public AdvancedRendererModel claw12L;
    public AdvancedRendererModel arm2R;
    public AdvancedRendererModel palmR;
    public AdvancedRendererModel claw21R;
    public AdvancedRendererModel claw11R;
    public AdvancedRendererModel claw22R;
    public AdvancedRendererModel claw12R;
    
    private AdvancedRendererModel[] headArray, tailArray, toeArray;
    
    private ModelAnimator animator;
    
    public OWDrakeModel()
    {
        textureWidth = 200;
        textureHeight = 200;
        claw12L = new AdvancedRendererModel(this, 129, 79);
        claw12L.setRotationPoint(-0.05F, -0.4F, -2.2F);
        claw12L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw12L, 0.9105382707654417F, 0.0F, 0.0F);
        arm2L = new AdvancedRendererModel(this, 111, 76);
        arm2L.setRotationPoint(1.45F, 5.5F, 0.5F);
        arm2L.addBox(-1.5F, 0.0F, -2.0F, 3, 7, 3, 0.0F);
        setRotateAngle(arm2L, -0.36425021489121656F, 0.0F, 0.0F);
        leg3R = new AdvancedRendererModel(this, 60, 94);
        leg3R.setRotationPoint(-0.05F, 4.0F, 1.8F);
        leg3R.addBox(-1.5F, 0.0F, -2.0F, 3, 6, 3, 0.0F);
        setRotateAngle(leg3R, -0.6829473363053812F, 0.0F, 0.0F);
        eyeR = new AdvancedRendererModel(this, 67, 40);
        eyeR.mirror = true;
        eyeR.setRotationPoint(-3.5F, -2.0F, -4.0F);
        eyeR.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        setRotateAngle(eyeR, 0.0F, 0.8651597102135892F, 0.0F);
        claw22R = new AdvancedRendererModel(this, 129, 79);
        claw22R.setRotationPoint(-0.05F, -0.4F, -2.2F);
        claw22R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw22R, 0.9105382707654417F, 0.0F, 0.0F);
        arm1R = new AdvancedRendererModel(this, 111, 61);
        arm1R.mirror = true;
        arm1R.setRotationPoint(-2.0F, 1.3F, -2.0F);
        arm1R.addBox(-3.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
        setRotateAngle(arm1R, -0.22759093446006054F, 0.31869712141416456F, 0.0F);
        tail2 = new AdvancedRendererModel(this, 0, 55);
        tail2.setRotationPoint(0.0F, 0.15F, 6.0F);
        tail2.addBox(-3.0F, -3.0F, 0.0F, 6, 5, 8, 0.0F);
        setRotateAngle(tail2, -0.22759093446006054F, 0.0F, 0.0F);
        tail4 = new AdvancedRendererModel(this, 0, 85);
        tail4.setRotationPoint(0.0F, -0.25F, 6.0F);
        tail4.addBox(-2.0F, -1.5F, 0.0F, 4, 3, 8, 0.0F);
        setRotateAngle(tail4, 0.18203784098300857F, 0.0F, 0.0F);
        toe1R_1 = new AdvancedRendererModel(this, 77, 85);
        toe1R_1.setRotationPoint(-1.4F, -0.5F, -2.5F);
        toe1R_1.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        setRotateAngle(toe1R_1, -0.8726646259971648F, 0.31869712141416456F, 0.0F);
        horn12R = new AdvancedRendererModel(this, 95, 32);
        horn12R.setRotationPoint(0.0F, 0.0F, 3.5F);
        horn12R.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 6, 0.0F);
        setRotateAngle(horn12R, -0.18203784098300857F, 0.22759093446006054F, 0.0F);
        toe2L = new AdvancedRendererModel(this, 77, 85);
        toe2L.setRotationPoint(0.0F, -0.5F, -2.5F);
        toe2L.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        setRotateAngle(toe2L, -0.8726646259971648F, 0.0F, 0.0F);
        toe1L_1 = new AdvancedRendererModel(this, 77, 85);
        toe1L_1.setRotationPoint(-1.4F, -0.5F, -2.5F);
        toe1L_1.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        setRotateAngle(toe1L_1, -0.8726646259971648F, 0.31869712141416456F, 0.0F);
        footR = new AdvancedRendererModel(this, 60, 85);
        footR.setRotationPoint(-0.0F, 5.5F, -0.3F);
        footR.addBox(-2.0F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        setRotateAngle(footR, 0.8651597102135892F, 0.0F, 0.0F);
        claw12R = new AdvancedRendererModel(this, 129, 79);
        claw12R.setRotationPoint(-0.05F, -0.4F, -2.2F);
        claw12R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw12R, 0.9105382707654417F, 0.0F, 0.0F);
        leg1L = new AdvancedRendererModel(this, 42, 79);
        leg1L.mirror = true;
        leg1L.setRotationPoint(3.0F, 1.8F, 6.0F);
        leg1L.addBox(0.2F, -1.2F, -2.5F, 3, 7, 5, 0.0F);
        toe2R = new AdvancedRendererModel(this, 77, 85);
        toe2R.setRotationPoint(0.0F, -0.5F, -2.5F);
        toe2R.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        setRotateAngle(toe2R, -0.8726646259971648F, 0.0F, 0.0F);
        horn32R = new AdvancedRendererModel(this, 95, 24);
        horn32R.setRotationPoint(0.0F, 0.0F, 3.2F);
        horn32R.addBox(-1.0F, -0.5F, 0.0F, 2, 1, 4, 0.0F);
        setRotateAngle(horn32R, 0.0F, -0.7740535232594852F, -0.18203784098300857F);
        horn52 = new AdvancedRendererModel(this, 97, 10);
        horn52.setRotationPoint(0.05F, -3.6F, -0.2F);
        horn52.addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2, 0.0F);
        setRotateAngle(horn52, -0.4553564018453205F, 0.0F, 0.0F);
        palmR = new AdvancedRendererModel(this, 129, 61);
        palmR.setRotationPoint(-0.5F, 6.5F, 0.0F);
        palmR.addBox(-1.5F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        setRotateAngle(palmR, 0.3490658503988659F, 0.0F, 0.0F);
        leg2L = new AdvancedRendererModel(this, 42, 94);
        leg2L.mirror = true;
        leg2L.setRotationPoint(1.65F, 4.5F, -1.3F);
        leg2L.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 4, 0.0F);
        setRotateAngle(leg2L, 0.6373942428283291F, 0.0F, 0.0F);
        claw21R = new AdvancedRendererModel(this, 129, 71);
        claw21R.setRotationPoint(-0.3F, 0.0F, -2.0F);
        claw21R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw21R, -0.31869712141416456F, 0.27314402793711257F, 0.0F);
        horn11R = new AdvancedRendererModel(this, 79, 32);
        horn11R.setRotationPoint(-3.0F, -2.0F, -0.5F);
        horn11R.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        setRotateAngle(horn11R, 0.40980330836826856F, -0.27314402793711257F, -0.27314402793711257F);
        horn12L = new AdvancedRendererModel(this, 95, 32);
        horn12L.setRotationPoint(0.0F, 0.0F, 3.5F);
        horn12L.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 6, 0.0F);
        setRotateAngle(horn12L, -0.18203784098300857F, -0.22759093446006054F, 0.0F);
        neck1 = new AdvancedRendererModel(this, 40, 0);
        neck1.setRotationPoint(0.0F, -0.5F, -3.0F);
        neck1.addBox(-3.5F, -3.0F, -5.0F, 7, 6, 5, 0.0F);
        setRotateAngle(neck1, -0.8196066167365371F, 0.0F, 0.0F);
        horn31L = new AdvancedRendererModel(this, 79, 22);
        horn31L.setRotationPoint(2.6F, 0.5F, -0.9F);
        horn31L.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        setRotateAngle(horn31L, -0.36425021489121656F, 2.0032889154390916F, 0.091106186954104F);
        horn51 = new AdvancedRendererModel(this, 80, 7);
        horn51.setRotationPoint(0.0F, 0.0F, -2.6F);
        horn51.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        setRotateAngle(horn51, 1.1383037381507017F, 0.0F, 0.0F);
        horn32L = new AdvancedRendererModel(this, 95, 24);
        horn32L.setRotationPoint(0.0F, 0.0F, 3.2F);
        horn32L.addBox(-1.0F, -0.5F, 0.0F, 2, 1, 4, 0.0F);
        setRotateAngle(horn32L, 0.0F, 0.7740535232594852F, 0.18203784098300857F);
        claw22L = new AdvancedRendererModel(this, 129, 79);
        claw22L.setRotationPoint(-0.05F, -0.4F, -2.2F);
        claw22L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw22L, 0.9105382707654417F, 0.0F, 0.0F);
        eyeL = new AdvancedRendererModel(this, 67, 40);
        eyeL.setRotationPoint(3.5F, -2.0F, -4.0F);
        eyeL.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        setRotateAngle(eyeL, 0.0F, -0.8651597102135892F, 0.0F);
        body2 = new AdvancedRendererModel(this, 0, 20);
        body2.setRotationPoint(0.0F, -1.55F, 4.0F);
        body2.addBox(-4.5F, -2.0F, 0.0F, 9, 7, 10, 0.0F);
        setRotateAngle(body2, -0.18203784098300857F, 0.0F, 0.0F);
        horn31R = new AdvancedRendererModel(this, 79, 22);
        horn31R.setRotationPoint(-2.6F, 0.5F, -0.9F);
        horn31R.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        setRotateAngle(horn31R, -0.36425021489121656F, -2.0032889154390916F, -0.091106186954104F);
        horn11L = new AdvancedRendererModel(this, 79, 32);
        horn11L.setRotationPoint(3.0F, -2.0F, -0.5F);
        horn11L.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        setRotateAngle(horn11L, 0.40980330836826856F, 0.27314402793711257F, 0.27314402793711257F);
        tail5 = new AdvancedRendererModel(this, 0, 102);
        tail5.setRotationPoint(0.0F, -0.25F, 6.0F);
        tail5.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 8, 0.0F);
        setRotateAngle(tail5, 0.27314402793711257F, 0.0F, 0.0F);
        jaw = new AdvancedRendererModel(this, 72, 46);
        jaw.setRotationPoint(0.0F, 1.35F, -4.0F);
        jaw.addBox(-3.0F, -1.0F, -5.0F, 6, 2, 6, 0.0F);
        setRotateAngle(jaw, 0.2F, 0.0F, 0.0F);
        head = new AdvancedRendererModel(this, 40, 29);
        head.setRotationPoint(-0.05F, 0.05F, -4.0F);
        head.addBox(-4.0F, -3.0F, -4.0F, 8, 6, 4, 0.0F);
        setRotateAngle(head, 0.31869712141416456F, 0.0F, 0.0F);
        tail3 = new AdvancedRendererModel(this, 0, 70);
        tail3.setRotationPoint(0.0F, -0.85F, 6.0F);
        tail3.addBox(-2.5F, -2.0F, 0.0F, 5, 4, 8, 0.0F);
        setRotateAngle(tail3, -0.136659280431156F, 0.0F, 0.0F);
        leg1R = new AdvancedRendererModel(this, 42, 79);
        leg1R.setRotationPoint(-3.0F, 1.8F, 6.0F);
        leg1R.addBox(-3.2F, -1.2F, -2.5F, 3, 7, 5, 0.0F);
        arm2R = new AdvancedRendererModel(this, 111, 76);
        arm2R.mirror = true;
        arm2R.setRotationPoint(-1.45F, 5.5F, 0.5F);
        arm2R.addBox(-1.5F, 0.0F, -2.0F, 3, 7, 3, 0.0F);
        setRotateAngle(arm2R, -0.36425021489121656F, 0.0F, 0.0F);
        toe1L = new AdvancedRendererModel(this, 77, 85);
        toe1L.setRotationPoint(1.4F, -0.5F, -2.5F);
        toe1L.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        setRotateAngle(toe1L, -0.8726646259971648F, -0.31869712141416456F, 0.0F);
        claw21L = new AdvancedRendererModel(this, 129, 71);
        claw21L.setRotationPoint(-0.3F, 0.0F, -2.0F);
        claw21L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw21L, -0.31869712141416456F, 0.27314402793711257F, 0.0F);
        horn33R = new AdvancedRendererModel(this, 105, 24);
        horn33R.mirror = true;
        horn33R.setRotationPoint(0.5F, 0.0F, 3.5F);
        horn33R.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3, 0.0F);
        setRotateAngle(horn33R, 0.0F, -0.7740535232594852F, 0.18203784098300857F);
        body1 = new AdvancedRendererModel(this, 0, 0);
        body1.setRotationPoint(0.0F, -2.2F, 0.0F);
        body1.addBox(-4.0F, -3.5F, -5.0F, 8, 8, 10, 0.0F);
        setRotateAngle(body1, 0.22759093446006054F, 0.0F, 0.0F);
        footL = new AdvancedRendererModel(this, 60, 85);
        footL.setRotationPoint(-0.0F, 5.5F, -0.3F);
        footL.addBox(-2.0F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        setRotateAngle(footL, 0.8651597102135892F, 0.0F, 0.0F);
        leg3L = new AdvancedRendererModel(this, 60, 94);
        leg3L.setRotationPoint(0.05F, 4.0F, 1.8F);
        leg3L.addBox(-1.5F, 0.0F, -2.0F, 3, 6, 3, 0.0F);
        setRotateAngle(leg3L, -0.6829473363053812F, 0.0F, 0.0F);
        toe1R = new AdvancedRendererModel(this, 77, 85);
        toe1R.setRotationPoint(1.4F, -0.5F, -2.5F);
        toe1R.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        setRotateAngle(toe1R, -0.8726646259971648F, -0.31869712141416456F, 0.0F);
        palmL = new AdvancedRendererModel(this, 129, 61);
        palmL.setRotationPoint(-0.5F, 6.5F, 0.0F);
        palmL.addBox(-1.5F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        setRotateAngle(palmL, 0.3490658503988659F, 0.0F, 0.0F);
        horn33L = new AdvancedRendererModel(this, 105, 24);
        horn33L.setRotationPoint(-0.5F, 0.0F, 3.5F);
        horn33L.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3, 0.0F);
        setRotateAngle(horn33L, 0.0F, 0.7740535232594852F, -0.18203784098300857F);
        claw11R = new AdvancedRendererModel(this, 129, 71);
        claw11R.setRotationPoint(1.3F, 0.0F, -2.0F);
        claw11R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw11R, -0.31869712141416456F, -0.27314402793711257F, 0.0F);
        neck2 = new AdvancedRendererModel(this, 40, 15);
        neck2.setRotationPoint(0.05F, 0.05F, -4.0F);
        neck2.addBox(-3.5F, -3.0F, -5.0F, 7, 6, 5, 0.0F);
        setRotateAngle(neck2, 0.31869712141416456F, 0.0F, 0.0F);
        snout = new AdvancedRendererModel(this, 40, 44);
        snout.setRotationPoint(-0.05F, -1.05F, -4.0F);
        snout.addBox(-3.5F, -1.5F, -6.0F, 7, 3, 7, 0.0F);
        setRotateAngle(snout, 0.18203784098300857F, 0.0F, 0.0F);
        arm1L = new AdvancedRendererModel(this, 111, 61);
        arm1L.setRotationPoint(2.0F, 1.3F, -2.0F);
        arm1L.addBox(0.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
        setRotateAngle(arm1L, -0.22759093446006054F, -0.31869712141416456F, 0.0F);
        leg2R = new AdvancedRendererModel(this, 42, 94);
        leg2R.setRotationPoint(-1.65F, 4.5F, -1.3F);
        leg2R.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 4, 0.0F);
        setRotateAngle(leg2R, 0.6373942428283291F, 0.0F, 0.0F);
        tail1 = new AdvancedRendererModel(this, 0, 39);
        tail1.setRotationPoint(0.0F, 1.2F, 8.0F);
        tail1.addBox(-3.5F, -3.0F, 0.0F, 7, 6, 8, 0.0F);
        setRotateAngle(tail1, -0.136659280431156F, 0.0F, 0.0F);
        horn53 = new AdvancedRendererModel(this, 107, 10);
        horn53.setRotationPoint(0.05F, -2.6F, -0.5F);
        horn53.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        setRotateAngle(horn53, -0.6829473363053812F, 0.0F, 0.0F);
        claw11L = new AdvancedRendererModel(this, 129, 71);
        claw11L.setRotationPoint(1.3F, 0.0F, -2.0F);
        claw11L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        setRotateAngle(claw11L, -0.31869712141416456F, -0.27314402793711257F, 0.0F);
        claw11L.addChild(claw12L);
        arm1L.addChild(arm2L);
        leg2R.addChild(leg3R);
        head.addChild(eyeR);
        claw21R.addChild(claw22R);
        body1.addChild(arm1R);
        tail1.addChild(tail2);
        tail3.addChild(tail4);
        footR.addChild(toe1R_1);
        horn11R.addChild(horn12R);
        footL.addChild(toe2L);
        footL.addChild(toe1L_1);
        leg3R.addChild(footR);
        claw11R.addChild(claw12R);
        body2.addChild(leg1L);
        footR.addChild(toe2R);
        horn31R.addChild(horn32R);
        horn51.addChild(horn52);
        arm2R.addChild(palmR);
        leg1L.addChild(leg2L);
        palmR.addChild(claw21R);
        head.addChild(horn11R);
        horn11L.addChild(horn12L);
        body1.addChild(neck1);
        head.addChild(horn31L);
        snout.addChild(horn51);
        horn31L.addChild(horn32L);
        claw21L.addChild(claw22L);
        head.addChild(eyeL);
        body1.addChild(body2);
        head.addChild(horn31R);
        head.addChild(horn11L);
        tail4.addChild(tail5);
        head.addChild(jaw);
        neck2.addChild(head);
        tail2.addChild(tail3);
        body2.addChild(leg1R);
        arm1R.addChild(arm2R);
        footL.addChild(toe1L);
        palmL.addChild(claw21L);
        horn32R.addChild(horn33R);
        leg3L.addChild(footL);
        leg2L.addChild(leg3L);
        footR.addChild(toe1R);
        arm2L.addChild(palmL);
        horn32L.addChild(horn33L);
        palmR.addChild(claw11R);
        neck1.addChild(neck2);
        head.addChild(snout);
        body1.addChild(arm1L);
        leg1R.addChild(leg2R);
        body2.addChild(tail1);
        horn52.addChild(horn53);
        palmL.addChild(claw11L);
        
        updateDefaultPose();
        
        headArray = new AdvancedRendererModel[]{neck1, neck2, head};
        tailArray = new AdvancedRendererModel[]{tail1, tail2, tail3, tail4, tail5};
        toeArray = new AdvancedRendererModel[]{toe1L, toe1L_1, toe1R, toe1R_1, toe2L, toe2R};
        
        animator = ModelAnimator.create();
    }
    
    private final float f = 0.5f;

    @Override
    public void render(MatrixStack ms, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        ms.push();
        ms.scale(2, 2, 2);
        body1.render(ms, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        ms.pop();
    }

    @Override
    public void setRotationAngles(OWDrakeEntity drake, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if (netHeadYaw < -180) netHeadYaw += 360;
        else if (netHeadYaw > 180) netHeadYaw -= 360;
        if (drake.getAnimation() != OWDrakeEntity.ROAR_ANIMATION && !drake.isSleeping())
            faceTarget(netHeadYaw, headPitch, 1, neck1, head);
    }

    @Override
    public void setLivingAnimations(OWDrakeEntity drake, float limbSwing, float limbSwingAmount, float partialTick)
    {
        float frame = drake.ticksExisted;
        Animation currentAnim = drake.getAnimation();
        
        resetToDefaultPose();
        animator.update(drake);
        
        if (!drake.isSitting() && !drake.isSleeping())
        {
            
            // Body bob
            bob(body1, globalSpeed * 2, 0.3f, false, limbSwing, 0.5f);
            
            // Left Arm
            arm1L.walk(globalSpeed, f, true, 0, 0, limbSwing, limbSwingAmount);
            palmL.walk(globalSpeed, f, true, 2.5f, 0, limbSwing, limbSwingAmount);
            
            // Right Arm
            arm1R.walk(globalSpeed, f, false, 0, 0, limbSwing, limbSwingAmount);
            palmR.walk(globalSpeed, f, false, 2.5f, 0, limbSwing, limbSwingAmount);
            
            // Left Leg
            leg1L.walk(globalSpeed, f, false, 0, 0, limbSwing, limbSwingAmount);
            footL.walk(globalSpeed, 0.2f, false, 2f, 0, limbSwing, limbSwingAmount);
            
            // Right Leg
            leg1R.walk(globalSpeed, f, true, 0, 0, limbSwing, limbSwingAmount);
            footR.walk(globalSpeed, 0.2f, true, 2f, 0, limbSwing, limbSwingAmount);
        }
        
        if (drake.isSitting() && currentAnim != OWDrakeEntity.SIT_ANIMATION)
            staySitting();
        
        if (drake.isSleeping() && currentAnim != OWDrakeEntity.SLEEP_ANIMATION)
            staySleeping();
        
        if (currentAnim == OWDrakeEntity.TALK_ANIMATION) talkAnim();
        
        if (currentAnim == OWDrakeEntity.SIT_ANIMATION) sitAnim();
        
        if (currentAnim == OWDrakeEntity.STAND_ANIMATION) standAnim();
        
        if (currentAnim == OWDrakeEntity.SLEEP_ANIMATION) sleepAnim(drake.isSitting());
        
        if (currentAnim == OWDrakeEntity.WAKE_ANIMATION) wakeAnim(drake.isSitting());
        
        if (currentAnim == OWDrakeEntity.GRAZE_ANIMATION) grazeAnim(drake.getAnimationTick(), frame);
        
        if (currentAnim == OWDrakeEntity.HORN_ATTACK_ANIMATION)
        {
            hornAttackAnim();
            
            return;
        }
        
        if (currentAnim == OWDrakeEntity.ROAR_ANIMATION)
        {
            roarAnim(drake, frame);
            
            return;
        }
        
        animatieIdle(frame);
    }
    
    public void animatieIdle(float frame)
    {
        chainWave(headArray, 0.45f - globalSpeed, 0.05f, 0d, frame, f);
        walk(head, 0.45f - globalSpeed, 0.08f, false, 2.5f, 0f, frame, f);
        
        walk(jaw, 0.45f - globalSpeed, 0.15f, false, 0f, 0.15f, frame, f);
        chainWave(tailArray, 0.45f - globalSpeed, 0.043f, 0d, frame, f);
        chainSwing(tailArray, globalSpeed - 0.45f, 0.043f, 2d, frame, f);
    }
    
    /**
     * Sitting <i><b>Position</b></i>
     * <p>
     * Called every tick when drake is sitting and no other animation is playing.
     * Called in {@link #standAnim()} for the initial position
     */
    private void staySitting()
    {
        body1.rotationPointY = -0.35f;
        
        // Front Right
        arm2R.rotateAngleX = -1.5f;
        palmR.rotateAngleX = 1.4f;
        
        // Front Left
        arm2L.rotateAngleX = -1.5f;
        palmL.rotateAngleX = 1.4f;
        
        // Back Right
        leg2R.rotateAngleX = 1f;
        leg2R.rotateAngleY = 0.4f;
        leg3R.setRotationPoint(-0.05F, 4.0F, -1.8F);
        leg3R.rotateAngleX = -2.6f;
        footR.rotateAngleX = 1.6f;
        
        // Back Left
        leg2L.rotateAngleX = 1f;
        leg2L.rotateAngleY = -0.4f;
        leg3L.setRotationPoint(-0.05F, 4.0F, -1.8F);
        leg3L.rotateAngleX = -2.6f;
        footL.rotateAngleX = 1.6f;
        
        //Toes
        for (AdvancedRendererModel toeSegment : toeArray) toeSegment.rotateAngleX = -0.1f;
        
        //Tail
        for (AdvancedRendererModel tailSegment : tailArray) tailSegment.rotateAngleY = -0.6f;
        tail1.rotateAngleX = -0.2f;
        tail3.rotateAngleZ = -0.2f;
        tail4.rotateAngleZ = -0.4f;
        tail5.rotateAngleZ = -0.3f;
        tail5.rotateAngleY += 0.1f;
    }
    
    /**
     * Sitting Animation
     */
    private void sitAnim()
    {
        animator.setAnimation(OWDrakeEntity.SIT_ANIMATION);
        
        animator.startKeyframe(15);
        animator.move(body1, 0, 5.5f, 0);
        //Front Right
        animator.rotate(arm2R, -1.1f, 0, 0);
        animator.rotate(palmR, 1f, 0, 0);
        //Front Left
        animator.rotate(arm2L, -1.1f, 0, 0);
        animator.rotate(palmL, 1f, 0, 0);
        //Back Right
        animator.rotate(leg2R, 0.35f, 0.4f, 0);
        leg3R.setRotationPoint(-0.05F, 4.0F, -1.8F);
        animator.rotate(leg3R, -1.9f, 0, 0);
        animator.rotate(footR, 0.7f, 0, 0);
        //Back Left
        animator.rotate(leg2L, 0.35f, -0.4f, 0);
        leg3L.setRotationPoint(-0.05F, 4.0F, -1.8F);
        animator.rotate(leg3L, -1.9f, 0, 0);
        animator.rotate(footL, 0.7f, 0, 0);
        //Toes
        for (AdvancedRendererModel toeSegment : toeArray) animator.rotate(toeSegment, 0.8f, 0, 0);
        //Tail
        for (AdvancedRendererModel tailSegment : tailArray) animator.rotate(tailSegment, 0, -0.6f, 0);
        tail1.rotateAngleX = -0.2f;
        tail3.rotateAngleZ = -0.2f;
        tail4.rotateAngleZ = -0.4f;
        tail5.rotateAngleZ = -0.3f;
        tail5.rotateAngleY += 0.1f;
        animator.endKeyframe();
    }
    
    /**
     * Standing up anim for "un-sitting".
     * Call {@link #staySitting()} for initial position
     */
    private void standAnim()
    {
        staySitting();
        
        animator.setAnimation(OWDrakeEntity.STAND_ANIMATION);
        
        animator.startKeyframe(15);
        animator.move(body1, 0, -5.5f, 0);
        //Front Right
        animator.rotate(arm2R, 1.1f, 0, 0);
        animator.rotate(palmR, -1f, 0, 0);
        //Front Left
        animator.rotate(arm2L, 1.1f, 0, 0);
        animator.rotate(palmL, -1f, 0, 0);
        //Back Right
        animator.rotate(leg2R, -0.35f, -0.4f, 0);
        leg3R.setRotationPoint(-0.05F, 4.0F, 1.8F);
        animator.rotate(leg3R, 1.9f, 0, 0);
        animator.rotate(footR, -0.7f, 0, 0);
        //Back Left
        animator.rotate(leg2L, -0.35f, 0.4f, 0);
        leg3L.setRotationPoint(-0.05F, 4.0F, 1.8F);
        animator.rotate(leg3L, 1.9f, 0, 0);
        animator.rotate(footL, -0.7f, 0, 0);
        //Toes
        for (AdvancedRendererModel toeSegment : toeArray) animator.rotate(toeSegment, -0.8f, 0, 0);
        //Tail
        for (AdvancedRendererModel tailSegment : tailArray) animator.rotate(tailSegment, 0, 0.6f, 0);
        animator.endKeyframe();
        
        tail1.rotateAngleX = tail1.defaultRotationX;
        tail3.rotateAngleZ = tail3.defaultRotationZ;
        tail4.rotateAngleZ = tail4.defaultRotationZ;
        tail5.rotateAngleZ = tail5.defaultRotationZ;
        tail5.rotateAngleY = tail5.defaultRotationY;
    }
    
    private void staySleeping()
    {
        staySitting();
        
        neck1.rotateAngleX = 0.4f;
        neck1.rotateAngleY = 0.4f;
        neck2.rotateAngleX = -0.2f;
        neck2.rotateAngleY = 0.6f;
        head.rotateAngleX = -0.2f;
        head.rotateAngleY = 0.4f;
        head.rotateAngleZ = -0.4f;
        eyeL.rotateAngleY = 1f;
        eyeR.rotateAngleY = -1f;
    }
    
    private void sleepAnim(boolean isSitting)
    {
        animator.setAnimation(OWDrakeEntity.SLEEP_ANIMATION);
        
        animator.startKeyframe(20);
        
        if (!isSitting)
        {
            animator.move(body1, 0, 5.5f, 0);
            //Front Right
            animator.rotate(arm2R, -1.1f, 0, 0);
            animator.rotate(palmR, 1f, 0, 0);
            //Front Left
            animator.rotate(arm2L, -1.1f, 0, 0);
            animator.rotate(palmL, 1f, 0, 0);
            //Back Right
            animator.rotate(leg2R, 0.35f, 0.4f, 0);
            leg3R.setRotationPoint(-0.05F, 4.0F, -1.8F);
            animator.rotate(leg3R, -1.9f, 0, 0);
            animator.rotate(footR, 0.7f, 0, 0);
            //Back Left
            animator.rotate(leg2L, 0.35f, -0.4f, 0);
            leg3L.setRotationPoint(-0.05F, 4.0F, -1.8F);
            animator.rotate(leg3L, -1.9f, 0, 0);
            animator.rotate(footL, 0.7f, 0, 0);
            //Toes
            for (AdvancedRendererModel toeSegment : toeArray) animator.rotate(toeSegment, 0.8f, 0, 0);
            //Tail
            for (AdvancedRendererModel tailSegment : tailArray) animator.rotate(tailSegment, 0, -0.6f, 0);
            tail1.rotateAngleX = -0.2f;
            tail3.rotateAngleZ = -0.2f;
            tail4.rotateAngleZ = -0.4f;
            tail5.rotateAngleZ = -0.3f;
            tail5.rotateAngleY += 0.1f;
        } else staySitting();
        
        animator.rotate(neck1, 1.2f, 0.4f, 0);
        animator.rotate(neck2, -0.5f, 0.6f, 0);
        animator.rotate(head, -0.52f, 0.4f, -0.4f);
        animator.endKeyframe();
        
        
    }
    
    private void wakeAnim(boolean isSitting)
    {
        animator.setAnimation(OWDrakeEntity.WAKE_ANIMATION);
        
        staySleeping();
        
        animator.startKeyframe(15);
        
        if (!isSitting)
        {
            animator.move(body1, 0, -5.5f, 0);
            //Front Right
            animator.rotate(arm2R, 1.1f, 0, 0);
            animator.rotate(palmR, -1f, 0, 0);
            //Front Left
            animator.rotate(arm2L, 1.1f, 0, 0);
            animator.rotate(palmL, -1f, 0, 0);
            //Back Right
            animator.rotate(leg2R, -0.35f, -0.4f, 0);
            leg3R.setRotationPoint(-0.05F, 4.0F, 1.8F);
            animator.rotate(leg3R, 1.9f, 0, 0);
            animator.rotate(footR, -0.7f, 0, 0);
            //Back Left
            animator.rotate(leg2L, -0.35f, 0.4f, 0);
            leg3L.setRotationPoint(-0.05F, 4.0F, 1.8F);
            animator.rotate(leg3L, 1.9f, 0, 0);
            animator.rotate(footL, -0.7f, 0, 0);
            //Toes
            for (AdvancedRendererModel toeSegment : toeArray) animator.rotate(toeSegment, -0.8f, 0, 0);
            //Tail
            for (AdvancedRendererModel tailSegment : tailArray) animator.rotate(tailSegment, 0, 0.6f, 0);
            
            tail1.rotateAngleX = tail1.defaultRotationX;
            tail3.rotateAngleZ = tail3.defaultRotationZ;
            tail4.rotateAngleZ = tail4.defaultRotationZ;
            tail5.rotateAngleZ = tail5.defaultRotationZ;
            tail5.rotateAngleY = tail5.defaultRotationY;
        }
        eyeL.rotateAngleY = eyeL.defaultRotationY;
        eyeR.rotateAngleY = eyeR.defaultRotationY;
        
        animator.rotate(neck1, -1.2f, -0.4f, 0);
        animator.rotate(neck2, 0.5f, -0.6f, 0);
        animator.rotate(head, 0.52f, -0.4f, 0.4f);
        animator.endKeyframe();
        
    }
    
    /**
     * Horn Attack Anim
     */
    private void hornAttackAnim()
    {
        animator.setAnimation(OWDrakeEntity.HORN_ATTACK_ANIMATION);
        
        animator.startKeyframe(5);
        animator.move(body1, 0, 2f, 1);
        animator.rotate(body1, 0.3f, 0, 0);
        animator.rotate(body2, -0.3f, 0, 0);
        animator.rotate(neck1, -0.4f, 0, 0);
        animator.rotate(head, 0.8f, 0, 0);
        animator.rotate(arm1L, 0.25f, 0, 0);
        animator.rotate(arm2L, -0.75f, 0, 0);
        animator.rotate(palmL, 0.25f, 0, 0);
        animator.rotate(arm1R, 0.25f, 0, 0);
        animator.rotate(arm2R, -0.75f, 0, 0);
        animator.rotate(palmR, 0.25f, 0, 0);
        animator.rotate(leg2L, 0.25f, 0, 0);
        animator.rotate(leg3L, -0.25f, 0, 0);
        animator.rotate(leg2R, 0.25f, 0, 0);
        animator.rotate(leg3R, -0.25f, 0, 0);
        for (AdvancedRendererModel segment : tailArray) animator.rotate(segment, -0.05f, 0, 0);
        animator.endKeyframe();

//        animator.setStaticKeyframe(1);
        
        animator.startKeyframe(3);
        animator.move(body1, 0, 2f, 1);
        animator.rotate(body1, 0.3f, 0, 0);
        animator.rotate(body2, -0.3f, 0, 0);
        animator.rotate(arm1L, 0.25f, 0, 0);
        animator.rotate(arm2L, -0.75f, 0, 0);
        animator.rotate(palmL, 0.25f, 0, 0);
        animator.rotate(arm1R, 0.25f, 0, 0);
        animator.rotate(arm2R, -0.75f, 0, 0);
        animator.rotate(palmR, 0.25f, 0, 0);
        animator.rotate(leg2L, 0.25f, 0, 0);
        animator.rotate(leg3L, -0.25f, 0, 0);
        animator.rotate(leg2R, 0.25f, 0, 0);
        animator.rotate(leg3R, -0.25f, 0, 0);
        for (AdvancedRendererModel segment : tailArray) animator.rotate(segment, -0.05f, 0, 0);
        animator.rotate(neck1, 0.3f, 0, 0);
        animator.endKeyframe();
        
        animator.startKeyframe(2);
        animator.rotate(neck1, -0.2f, 0, 0);
        for (AdvancedRendererModel segment : tailArray) animator.rotate(segment, 0.025f, 0, 0);
        animator.endKeyframe();
        
        animator.resetKeyframe(5);
    }
    
    /**
     * Grass Eating Animation
     * Rotate neck down and then rotate the mouth "eat"
     */
    private void grazeAnim(int animationTick, float frame)
    {
        animator.setAnimation(OWDrakeEntity.GRAZE_ANIMATION);
        
        animator.startKeyframe(12);
        animator.rotate(neck1, 1, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(15);
        animator.resetKeyframe(8);
        
        if (animationTick >= 8 && animationTick <= 27)
        {
            jaw.rotateAngleX -= (6 + Math.sin(frame / 2) * 0.25);
        }
    }
    
    /**
     * Roar Animation
     * played before dashing at the player to attack
     */
    private void roarAnim(OWDrakeEntity entity, float frame)
    {
        animator.setAnimation(OWDrakeEntity.ROAR_ANIMATION);
        
        animator.startKeyframe(14);
        animator.move(body1, 0, 0.8f, 0);
        animator.rotate(leg2L, 0.2f, 0, 0);
        animator.rotate(leg3L, -0.2f, 0, 0);
        animator.rotate(leg2R, 0.2f, 0, 0);
        animator.rotate(leg3R, -0.2f, 0, 0);
        animator.rotate(neck1, -0.6f, -0.2f, 0);
        animator.rotate(neck2, 0.5f, 0, 0);
        animator.rotate(head, 0.6f, -0.2f, 0);
        animator.rotate(arm1R, 0.4f, 0, 0);
        animator.rotate(arm2R, -0.4f, 0, 0);
        animator.rotate(arm1L, 0.4f, 0, 0);
        animator.rotate(arm2L, -0.4f, 0, 0);
        animator.endKeyframe();
        
        animator.startKeyframe(8);
        animator.rotate(neck1, 0.4f, 0, 0);
        animator.rotate(neck2, -0.4f, 0, 0);
        animator.rotate(jaw, 0.9f, 0, 0);
        for (AdvancedRendererModel tailSegment : tailArray) animator.rotate(tailSegment, 0.1f, 0, 0);
        animator.endKeyframe();
        
        animator.setStaticKeyframe(60);
        
        animator.resetKeyframe(4);
        
        if (entity.getAnimationTick() > 10)
        {
            walk(jaw, globalSpeed + 1.5f, 0.02f, false, 0, 0, frame, f);
            swing(head, globalSpeed + 1.5f, 0.02f, false, 0, 0, frame, f);
            
            chainWave(tailArray, globalSpeed + 1.5f, 0.007f, 0, frame, f);
        }
    }
    
    private void talkAnim()
    {
        animator.setAnimation(OWDrakeEntity.TALK_ANIMATION);
        
        animator.startKeyframe(4);
        animator.rotate(jaw, 0.5f, 0, 0);
        animator.endKeyframe();
        
        animator.setStaticKeyframe(11);
        
        animator.resetKeyframe(4);
    }
}
