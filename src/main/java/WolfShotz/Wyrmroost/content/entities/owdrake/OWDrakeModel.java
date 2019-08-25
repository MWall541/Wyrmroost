package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * WR Overworld Drake - Ukan
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class OWDrakeModel extends AdvancedEntityModel
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

    private float netHeadYaw, headPitch;

    public OWDrakeModel() {
        this.textureWidth = 200;
        this.textureHeight = 200;
        this.claw12L = new AdvancedRendererModel(this, 129, 79);
        this.claw12L.setRotationPoint(-0.05F, -0.4F, -2.2F);
        this.claw12L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw12L, 0.9105382707654417F, 0.0F, 0.0F);
        this.arm2L = new AdvancedRendererModel(this, 111, 76);
        this.arm2L.setRotationPoint(1.45F, 5.5F, 0.5F);
        this.arm2L.addBox(-1.5F, 0.0F, -2.0F, 3, 7, 3, 0.0F);
        this.setRotateAngle(arm2L, -0.36425021489121656F, 0.0F, 0.0F);
        this.leg3R = new AdvancedRendererModel(this, 60, 94);
        this.leg3R.setRotationPoint(-0.05F, 4.0F, 1.8F);
        this.leg3R.addBox(-1.5F, 0.0F, -2.0F, 3, 6, 3, 0.0F);
        this.setRotateAngle(leg3R, -0.6829473363053812F, 0.0F, 0.0F);
        this.eyeR = new AdvancedRendererModel(this, 67, 40);
        this.eyeR.mirror = true;
        this.eyeR.setRotationPoint(-3.5F, -2.0F, -4.0F);
        this.eyeR.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(eyeR, 0.0F, 0.8651597102135892F, 0.0F);
        this.claw22R = new AdvancedRendererModel(this, 129, 79);
        this.claw22R.setRotationPoint(-0.05F, -0.4F, -2.2F);
        this.claw22R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw22R, 0.9105382707654417F, 0.0F, 0.0F);
        this.arm1R = new AdvancedRendererModel(this, 111, 61);
        this.arm1R.mirror = true;
        this.arm1R.setRotationPoint(-2.0F, 1.3F, -2.0F);
        this.arm1R.addBox(-3.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
        this.setRotateAngle(arm1R, -0.22759093446006054F, 0.31869712141416456F, 0.0F);
        this.tail2 = new AdvancedRendererModel(this, 0, 55);
        this.tail2.setRotationPoint(0.0F, 0.15F, 6.0F);
        this.tail2.addBox(-3.0F, -3.0F, 0.0F, 6, 5, 8, 0.0F);
        this.setRotateAngle(tail2, -0.22759093446006054F, 0.0F, 0.0F);
        this.tail4 = new AdvancedRendererModel(this, 0, 85);
        this.tail4.setRotationPoint(0.0F, -0.25F, 6.0F);
        this.tail4.addBox(-2.0F, -1.5F, 0.0F, 4, 3, 8, 0.0F);
        this.setRotateAngle(tail4, 0.18203784098300857F, 0.0F, 0.0F);
        this.toe1R_1 = new AdvancedRendererModel(this, 77, 85);
        this.toe1R_1.setRotationPoint(-1.4F, -0.5F, -2.5F);
        this.toe1R_1.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        this.setRotateAngle(toe1R_1, -0.8726646259971648F, 0.31869712141416456F, 0.0F);
        this.horn12R = new AdvancedRendererModel(this, 95, 32);
        this.horn12R.setRotationPoint(0.0F, 0.0F, 3.5F);
        this.horn12R.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 6, 0.0F);
        this.setRotateAngle(horn12R, -0.18203784098300857F, 0.22759093446006054F, 0.0F);
        this.toe2L = new AdvancedRendererModel(this, 77, 85);
        this.toe2L.setRotationPoint(0.0F, -0.5F, -2.5F);
        this.toe2L.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        this.setRotateAngle(toe2L, -0.8726646259971648F, 0.0F, 0.0F);
        this.toe1L_1 = new AdvancedRendererModel(this, 77, 85);
        this.toe1L_1.setRotationPoint(-1.4F, -0.5F, -2.5F);
        this.toe1L_1.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        this.setRotateAngle(toe1L_1, -0.8726646259971648F, 0.31869712141416456F, 0.0F);
        this.footR = new AdvancedRendererModel(this, 60, 85);
        this.footR.setRotationPoint(-0.0F, 5.5F, -0.3F);
        this.footR.addBox(-2.0F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(footR, 0.8651597102135892F, 0.0F, 0.0F);
        this.claw12R = new AdvancedRendererModel(this, 129, 79);
        this.claw12R.setRotationPoint(-0.05F, -0.4F, -2.2F);
        this.claw12R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw12R, 0.9105382707654417F, 0.0F, 0.0F);
        this.leg1L = new AdvancedRendererModel(this, 42, 79);
        this.leg1L.mirror = true;
        this.leg1L.setRotationPoint(3.0F, 1.8F, 6.0F);
        this.leg1L.addBox(0.2F, -1.2F, -2.5F, 3, 7, 5, 0.0F);
        this.toe2R = new AdvancedRendererModel(this, 77, 85);
        this.toe2R.setRotationPoint(0.0F, -0.5F, -2.5F);
        this.toe2R.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        this.setRotateAngle(toe2R, -0.8726646259971648F, 0.0F, 0.0F);
        this.horn32R = new AdvancedRendererModel(this, 95, 24);
        this.horn32R.setRotationPoint(0.0F, 0.0F, 3.2F);
        this.horn32R.addBox(-1.0F, -0.5F, 0.0F, 2, 1, 4, 0.0F);
        this.setRotateAngle(horn32R, 0.0F, -0.7740535232594852F, -0.18203784098300857F);
        this.horn52 = new AdvancedRendererModel(this, 97, 10);
        this.horn52.setRotationPoint(0.05F, -3.6F, -0.2F);
        this.horn52.addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(horn52, -0.4553564018453205F, 0.0F, 0.0F);
        this.palmR = new AdvancedRendererModel(this, 129, 61);
        this.palmR.setRotationPoint(-0.5F, 6.5F, 0.0F);
        this.palmR.addBox(-1.5F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(palmR, 0.3490658503988659F, 0.0F, 0.0F);
        this.leg2L = new AdvancedRendererModel(this, 42, 94);
        this.leg2L.mirror = true;
        this.leg2L.setRotationPoint(1.65F, 4.5F, -1.3F);
        this.leg2L.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 4, 0.0F);
        this.setRotateAngle(leg2L, 0.6373942428283291F, 0.0F, 0.0F);
        this.claw21R = new AdvancedRendererModel(this, 129, 71);
        this.claw21R.setRotationPoint(-0.3F, 0.0F, -2.0F);
        this.claw21R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw21R, -0.31869712141416456F, 0.27314402793711257F, 0.0F);
        this.horn11R = new AdvancedRendererModel(this, 79, 32);
        this.horn11R.setRotationPoint(-3.0F, -2.0F, -0.5F);
        this.horn11R.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(horn11R, 0.40980330836826856F, -0.27314402793711257F, -0.27314402793711257F);
        this.horn12L = new AdvancedRendererModel(this, 95, 32);
        this.horn12L.setRotationPoint(0.0F, 0.0F, 3.5F);
        this.horn12L.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 6, 0.0F);
        this.setRotateAngle(horn12L, -0.18203784098300857F, -0.22759093446006054F, 0.0F);
        this.neck1 = new AdvancedRendererModel(this, 40, 0);
        this.neck1.setRotationPoint(0.0F, -0.5F, -3.0F);
        this.neck1.addBox(-3.5F, -3.0F, -5.0F, 7, 6, 5, 0.0F);
        this.setRotateAngle(neck1, -0.8196066167365371F, 0.0F, 0.0F);
        this.horn31L = new AdvancedRendererModel(this, 79, 22);
        this.horn31L.setRotationPoint(2.6F, 0.5F, -0.9F);
        this.horn31L.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(horn31L, -0.36425021489121656F, 2.0032889154390916F, 0.091106186954104F);
        this.horn51 = new AdvancedRendererModel(this, 80, 7);
        this.horn51.setRotationPoint(0.0F, 0.0F, -2.6F);
        this.horn51.addBox(-1.0F, -4.0F, -1.5F, 2, 4, 3, 0.0F);
        this.setRotateAngle(horn51, 1.1383037381507017F, 0.0F, 0.0F);
        this.horn32L = new AdvancedRendererModel(this, 95, 24);
        this.horn32L.setRotationPoint(0.0F, 0.0F, 3.2F);
        this.horn32L.addBox(-1.0F, -0.5F, 0.0F, 2, 1, 4, 0.0F);
        this.setRotateAngle(horn32L, 0.0F, 0.7740535232594852F, 0.18203784098300857F);
        this.claw22L = new AdvancedRendererModel(this, 129, 79);
        this.claw22L.setRotationPoint(-0.05F, -0.4F, -2.2F);
        this.claw22L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw22L, 0.9105382707654417F, 0.0F, 0.0F);
        this.eyeL = new AdvancedRendererModel(this, 67, 40);
        this.eyeL.setRotationPoint(3.5F, -2.0F, -4.0F);
        this.eyeL.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(eyeL, 0.0F, -0.8651597102135892F, 0.0F);
        this.body2 = new AdvancedRendererModel(this, 0, 20);
        this.body2.setRotationPoint(0.0F, -1.55F, 4.0F);
        this.body2.addBox(-4.5F, -2.0F, 0.0F, 9, 7, 10, 0.0F);
        this.setRotateAngle(body2, -0.18203784098300857F, 0.0F, 0.0F);
        this.horn31R = new AdvancedRendererModel(this, 79, 22);
        this.horn31R.setRotationPoint(-2.6F, 0.5F, -0.9F);
        this.horn31R.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.setRotateAngle(horn31R, -0.36425021489121656F, -2.0032889154390916F, -0.091106186954104F);
        this.horn11L = new AdvancedRendererModel(this, 79, 32);
        this.horn11L.setRotationPoint(3.0F, -2.0F, -0.5F);
        this.horn11L.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.setRotateAngle(horn11L, 0.40980330836826856F, 0.27314402793711257F, 0.27314402793711257F);
        this.tail5 = new AdvancedRendererModel(this, 0, 102);
        this.tail5.setRotationPoint(0.0F, -0.25F, 6.0F);
        this.tail5.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 8, 0.0F);
        this.setRotateAngle(tail5, 0.27314402793711257F, 0.0F, 0.0F);
        this.jaw = new AdvancedRendererModel(this, 72, 46);
        this.jaw.setRotationPoint(0.0F, 1.35F, -4.0F);
        this.jaw.addBox(-3.0F, -1.0F, -5.0F, 6, 2, 6, 0.0F);
        this.setRotateAngle(jaw, 0.2F, 0.0F, 0.0F);
        this.head = new AdvancedRendererModel(this, 40, 29);
        this.head.setRotationPoint(-0.05F, 0.05F, -4.0F);
        this.head.addBox(-4.0F, -3.0F, -4.0F, 8, 6, 4, 0.0F);
        this.setRotateAngle(head, 0.31869712141416456F, 0.0F, 0.0F);
        this.tail3 = new AdvancedRendererModel(this, 0, 70);
        this.tail3.setRotationPoint(0.0F, -0.85F, 6.0F);
        this.tail3.addBox(-2.5F, -2.0F, 0.0F, 5, 4, 8, 0.0F);
        this.setRotateAngle(tail3, -0.136659280431156F, 0.0F, 0.0F);
        this.leg1R = new AdvancedRendererModel(this, 42, 79);
        this.leg1R.setRotationPoint(-3.0F, 1.8F, 6.0F);
        this.leg1R.addBox(-3.2F, -1.2F, -2.5F, 3, 7, 5, 0.0F);
        this.arm2R = new AdvancedRendererModel(this, 111, 76);
        this.arm2R.mirror = true;
        this.arm2R.setRotationPoint(-1.45F, 5.5F, 0.5F);
        this.arm2R.addBox(-1.5F, 0.0F, -2.0F, 3, 7, 3, 0.0F);
        this.setRotateAngle(arm2R, -0.36425021489121656F, 0.0F, 0.0F);
        this.toe1L = new AdvancedRendererModel(this, 77, 85);
        this.toe1L.setRotationPoint(1.4F, -0.5F, -2.5F);
        this.toe1L.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        this.setRotateAngle(toe1L, -0.8726646259971648F, -0.31869712141416456F, 0.0F);
        this.claw21L = new AdvancedRendererModel(this, 129, 71);
        this.claw21L.setRotationPoint(-0.3F, 0.0F, -2.0F);
        this.claw21L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw21L, -0.31869712141416456F, 0.27314402793711257F, 0.0F);
        this.horn33R = new AdvancedRendererModel(this, 105, 24);
        this.horn33R.mirror = true;
        this.horn33R.setRotationPoint(0.5F, 0.0F, 3.5F);
        this.horn33R.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(horn33R, 0.0F, -0.7740535232594852F, 0.18203784098300857F);
        this.body1 = new AdvancedRendererModel(this, 0, 0);
        this.body1.setRotationPoint(0.0F, -2.2F, 0.0F);
        this.body1.addBox(-4.0F, -3.5F, -5.0F, 8, 8, 10, 0.0F);
        this.setRotateAngle(body1, 0.22759093446006054F, 0.0F, 0.0F);
        this.footL = new AdvancedRendererModel(this, 60, 85);
        this.footL.setRotationPoint(-0.0F, 5.5F, -0.3F);
        this.footL.addBox(-2.0F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(footL, 0.8651597102135892F, 0.0F, 0.0F);
        this.leg3L = new AdvancedRendererModel(this, 60, 94);
        this.leg3L.setRotationPoint(0.05F, 4.0F, 1.8F);
        this.leg3L.addBox(-1.5F, 0.0F, -2.0F, 3, 6, 3, 0.0F);
        this.setRotateAngle(leg3L, -0.6829473363053812F, 0.0F, 0.0F);
        this.toe1R = new AdvancedRendererModel(this, 77, 85);
        this.toe1R.setRotationPoint(1.4F, -0.5F, -2.5F);
        this.toe1R.addBox(-0.5F, -0.5F, -3.5F, 1, 2, 4, 0.0F);
        this.setRotateAngle(toe1R, -0.8726646259971648F, -0.31869712141416456F, 0.0F);
        this.palmL = new AdvancedRendererModel(this, 129, 61);
        this.palmL.setRotationPoint(-0.5F, 6.5F, 0.0F);
        this.palmL.addBox(-1.5F, -1.0F, -3.0F, 4, 2, 4, 0.0F);
        this.setRotateAngle(palmL, 0.3490658503988659F, 0.0F, 0.0F);
        this.horn33L = new AdvancedRendererModel(this, 105, 24);
        this.horn33L.setRotationPoint(-0.5F, 0.0F, 3.5F);
        this.horn33L.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(horn33L, 0.0F, 0.7740535232594852F, -0.18203784098300857F);
        this.claw11R = new AdvancedRendererModel(this, 129, 71);
        this.claw11R.setRotationPoint(1.3F, 0.0F, -2.0F);
        this.claw11R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw11R, -0.31869712141416456F, -0.27314402793711257F, 0.0F);
        this.neck2 = new AdvancedRendererModel(this, 40, 15);
        this.neck2.setRotationPoint(0.05F, 0.05F, -4.0F);
        this.neck2.addBox(-3.5F, -3.0F, -5.0F, 7, 6, 5, 0.0F);
        this.setRotateAngle(neck2, 0.31869712141416456F, 0.0F, 0.0F);
        this.snout = new AdvancedRendererModel(this, 40, 44);
        this.snout.setRotationPoint(-0.05F, -1.05F, -4.0F);
        this.snout.addBox(-3.5F, -1.5F, -6.0F, 7, 3, 7, 0.0F);
        this.setRotateAngle(snout, 0.18203784098300857F, 0.0F, 0.0F);
        this.arm1L = new AdvancedRendererModel(this, 111, 61);
        this.arm1L.setRotationPoint(2.0F, 1.3F, -2.0F);
        this.arm1L.addBox(0.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
        this.setRotateAngle(arm1L, -0.22759093446006054F, -0.31869712141416456F, 0.0F);
        this.leg2R = new AdvancedRendererModel(this, 42, 94);
        this.leg2R.setRotationPoint(-1.65F, 4.5F, -1.3F);
        this.leg2R.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 4, 0.0F);
        this.setRotateAngle(leg2R, 0.6373942428283291F, 0.0F, 0.0F);
        this.tail1 = new AdvancedRendererModel(this, 0, 39);
        this.tail1.setRotationPoint(0.0F, 1.2F, 8.0F);
        this.tail1.addBox(-3.5F, -3.0F, 0.0F, 7, 6, 8, 0.0F);
        this.setRotateAngle(tail1, -0.136659280431156F, 0.0F, 0.0F);
        this.horn53 = new AdvancedRendererModel(this, 107, 10);
        this.horn53.setRotationPoint(0.05F, -2.6F, -0.5F);
        this.horn53.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(horn53, -0.6829473363053812F, 0.0F, 0.0F);
        this.claw11L = new AdvancedRendererModel(this, 129, 71);
        this.claw11L.setRotationPoint(1.3F, 0.0F, -2.0F);
        this.claw11L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(claw11L, -0.31869712141416456F, -0.27314402793711257F, 0.0F);
        this.claw11L.addChild(this.claw12L);
        this.arm1L.addChild(this.arm2L);
        this.leg2R.addChild(this.leg3R);
        this.head.addChild(this.eyeR);
        this.claw21R.addChild(this.claw22R);
        this.body1.addChild(this.arm1R);
        this.tail1.addChild(this.tail2);
        this.tail3.addChild(this.tail4);
        this.footR.addChild(this.toe1R_1);
        this.horn11R.addChild(this.horn12R);
        this.footL.addChild(this.toe2L);
        this.footL.addChild(this.toe1L_1);
        this.leg3R.addChild(this.footR);
        this.claw11R.addChild(this.claw12R);
        this.body2.addChild(this.leg1L);
        this.footR.addChild(this.toe2R);
        this.horn31R.addChild(this.horn32R);
        this.horn51.addChild(this.horn52);
        this.arm2R.addChild(this.palmR);
        this.leg1L.addChild(this.leg2L);
        this.palmR.addChild(this.claw21R);
        this.head.addChild(this.horn11R);
        this.horn11L.addChild(this.horn12L);
        this.body1.addChild(this.neck1);
        this.head.addChild(this.horn31L);
        this.snout.addChild(this.horn51);
        this.horn31L.addChild(this.horn32L);
        this.claw21L.addChild(this.claw22L);
        this.head.addChild(this.eyeL);
        this.body1.addChild(this.body2);
        this.head.addChild(this.horn31R);
        this.head.addChild(this.horn11L);
        this.tail4.addChild(this.tail5);
        this.head.addChild(this.jaw);
        this.neck2.addChild(this.head);
        this.tail2.addChild(this.tail3);
        this.body2.addChild(this.leg1R);
        this.arm1R.addChild(this.arm2R);
        this.footL.addChild(this.toe1L);
        this.palmL.addChild(this.claw21L);
        this.horn32R.addChild(this.horn33R);
        this.leg3L.addChild(this.footL);
        this.leg2L.addChild(this.leg3L);
        this.footR.addChild(this.toe1R);
        this.arm2L.addChild(this.palmL);
        this.horn32L.addChild(this.horn33L);
        this.palmR.addChild(this.claw11R);
        this.neck1.addChild(this.neck2);
        this.head.addChild(this.snout);
        this.body1.addChild(this.arm1L);
        this.leg1R.addChild(this.leg2R);
        this.body2.addChild(this.tail1);
        this.horn52.addChild(this.horn53);
        this.palmL.addChild(this.claw11L);

        updateDefaultPose();

        headArray = new AdvancedRendererModel[] {neck1, neck2, head};
        tailArray = new AdvancedRendererModel[] {tail1, tail2, tail3, tail4, tail5};
        toeArray = new AdvancedRendererModel[] {toe1L, toe1L_1, toe1R, toe1R_1, toe2L, toe2R};

        animator = ModelAnimator.create();
    }

    private float globalSpeed = 0.5f;
    private float f = 0.5f;

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        OWDrakeEntity dragon = (OWDrakeEntity) entity;
        
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;

        GlStateManager.pushMatrix();
        
        if (dragon.isChild()) {
            GlStateManager.scaled(1, 1, 1);
            GlStateManager.translated(0, 0.7, 0);
        }
        else GlStateManager.scaled(2, 2, 2);
        
        body1.render(scale);
        
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        OWDrakeEntity entity = (OWDrakeEntity) entityIn;
        
    }
    
    @Override
    public void setLivingAnimations(Entity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        float frame = entityIn.ticksExisted;
        OWDrakeEntity drake = (OWDrakeEntity) entityIn;
        Animation currentAnim = drake.getAnimation();

        resetToDefaultPose();
        animator.update(drake);
    
        if (!drake.isSitting() || drake.getAnimation() != AbstractDragonEntity.NO_ANIMATION) {
        
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
    
        if (currentAnim == OWDrakeEntity.WAKE_ANIMATION) wakeAnim();
    
        if (currentAnim == OWDrakeEntity.GRAZE_ANIMATION) grazeAnim(drake, frame);

        if (currentAnim == OWDrakeEntity.HORN_ATTACK_ANIMATION) {
            hornAttackAnim();
            
            return;
        }
        
        if (currentAnim == OWDrakeEntity.ROAR_ANIMATION) {
            roarAnim(drake, frame);
            
            return;
        }
        
        continueIdle(frame);
        
        if (!drake.hasActiveAnimation()) faceTarget(netHeadYaw, headPitch, 4, neck1, head);
    }
    
    
    private void continueIdle(float frame) {
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
    private void staySitting() {
        body1.offsetY = 0.35f;

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
    private void sitAnim() {
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
    private void standAnim() {
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
    
    private void staySleeping() {
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
    
    private void sleepAnim(boolean isSitting) {
        animator.setAnimation(OWDrakeEntity.SLEEP_ANIMATION);
        
        animator.startKeyframe(20);
        
        if (!isSitting) {
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
    
    private void wakeAnim() {
        animator.setAnimation(OWDrakeEntity.WAKE_ANIMATION);
    
        staySleeping();
        
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
    
        tail1.rotateAngleX = tail1.defaultRotationX;
        tail3.rotateAngleZ = tail3.defaultRotationZ;
        tail4.rotateAngleZ = tail4.defaultRotationZ;
        tail5.rotateAngleZ = tail5.defaultRotationZ;
        tail5.rotateAngleY = tail5.defaultRotationY;
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
    private void hornAttackAnim() {
        animator.setAnimation(OWDrakeEntity.HORN_ATTACK_ANIMATION);

        animator.startKeyframe(7);
        animator.move(body1, 0, 0.5f, 0.2f);
        animator.rotate(neck1, -0.4f, 0, 0);
        animator.rotate(head, 0.8f, 0, 0);
        animator.rotate(arm1L, 0.2f, 0, 0);
        animator.rotate(arm2L, -0.2f, 0, 0);
        animator.rotate(arm1R, 0.2f, 0, 0);
        animator.rotate(arm2R, -0.2f, 0, 0);
        animator.rotate(leg1L, 0.2f, 0, 0);
        animator.rotate(leg1R, 0.2f, 0, 0);
        animator.rotate(leg2L, -0.2f, 0, 0);
        animator.rotate(leg2R, -0.2f, 0, 0);
        animator.rotate(footL, -0.09f, 0, 0);
        animator.rotate(footR, -0.09f, 0, 0);
        animator.endKeyframe();

        animator.startKeyframe(3);
        animator.rotate(neck1, 0.6f, 0, 0);
        animator.endKeyframe();

        animator.startKeyframe(5);
        animator.rotate(head, -0.8f, 0, 0);
        animator.endKeyframe();

        animator.resetKeyframe(7);
    }

    /**
     * Grass Eating Animation
     * Rotate neck down and then rotate the mouth "eat"
     */
    private void grazeAnim(OWDrakeEntity entity, float frame) {
        animator.setAnimation(OWDrakeEntity.GRAZE_ANIMATION);

        animator.startKeyframe(12);
        animator.rotate(neck1, 1, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(15);
        animator.resetKeyframe(8);

        if (entity.getAnimationTick() >= 8 && entity.getAnimationTick() <= 27) {
            jaw.rotateAngleX -= (6 + Math.sin(frame / 2) * 0.25);
        }
    }
    
    /**
     * Roar Animation
     * played before dashing at the player to attack
     */
    private void roarAnim(OWDrakeEntity entity, float frame) {
        animator.setAnimation(OWDrakeEntity.ROAR_ANIMATION);
        
        animator.startKeyframe(5);
        animator.rotate(neck1, -0.4f, 0, 0);
        animator.rotate(neck2, 0.5f, 0, 0);
        animator.endKeyframe();
        
        animator.startKeyframe(8);
        animator.rotate(neck1, 0.4f, 0, 0);
        animator.rotate(neck2, -0.4f, 0, 0);
        animator.rotate(jaw, 0.9f, 0, 0);
        for (AdvancedRendererModel tailSegment : tailArray) animator.rotate(tailSegment, 0.1f, 0, 0);
        animator.endKeyframe();
        
        animator.setStaticKeyframe(18);
        
        animator.resetKeyframe(4);
        
        if (entity.getAnimationTick() > 10) {
            walk(jaw, globalSpeed + 1.5f, 0.02f, false, 0, 0, frame, f);
            swing(head, globalSpeed + 1.5f, 0.02f, false, 0, 0, frame, f);
            
            chainWave(tailArray, globalSpeed + 1.5f, 0.007f, 0, frame, f);
        }
    }
    
    private void talkAnim() {
        animator.setAnimation(OWDrakeEntity.TALK_ANIMATION);
        
        animator.startKeyframe(4);
        animator.rotate(jaw, 0.5f, 0, 0);
        animator.endKeyframe();
        
        animator.setStaticKeyframe(11);
        
        animator.resetKeyframe(4);
    }

}
