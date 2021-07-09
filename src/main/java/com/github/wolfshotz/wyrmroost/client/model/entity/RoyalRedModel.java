package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.ModelAnimator;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.RoyalRedEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

/**
 * WRRoyalRedReparent - Ukan
 * Created using Tabula 7.1.0
 */
public class RoyalRedModel extends DragonEntityModel<RoyalRedEntity>
{
    public static final ResourceLocation CHILD = Wyrmroost.id(FOLDER + "royal_red/child.png");
    public static final ResourceLocation CHRISTMAS_MALE = Wyrmroost.id(FOLDER + "royal_red/male_christmas.png");
    public static final ResourceLocation CHRISTMAS_FEMALE = Wyrmroost.id(FOLDER + "royal_red/female_christmas.png");
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

    public WRModelRenderer body2;
    public WRModelRenderer tail1;
    public WRModelRenderer leg1R;
    public WRModelRenderer spike4;
    public WRModelRenderer leg1L;
    public WRModelRenderer body1;
    public WRModelRenderer tail2;
    public WRModelRenderer spike5;
    public WRModelRenderer tail3;
    public WRModelRenderer spike6;
    public WRModelRenderer tail4;
    public WRModelRenderer spike7;
    public WRModelRenderer tail5;
    public WRModelRenderer spike8;
    public WRModelRenderer tail6;
    public WRModelRenderer spike9;
    public WRModelRenderer tail7;
    public WRModelRenderer spike10;
    public WRModelRenderer tail8;
    public WRModelRenderer spike11;
    public WRModelRenderer tailspike1part1;
    public WRModelRenderer tailspike1part2;
    public WRModelRenderer tailspike3part1;
    public WRModelRenderer tailspike4part1;
    public WRModelRenderer spike12;
    public WRModelRenderer tailspike2part1;
    public WRModelRenderer tailspike2part2;
    public WRModelRenderer tailspike2part3;
    public WRModelRenderer tailspike2part4;
    public WRModelRenderer leg2R;
    public WRModelRenderer leg3R;
    public WRModelRenderer footR;
    public WRModelRenderer toe1R;
    public WRModelRenderer toe2R;
    public WRModelRenderer toe3R;
    public WRModelRenderer leg2L;
    public WRModelRenderer leg3L;
    public WRModelRenderer footL;
    public WRModelRenderer toe1L;
    public WRModelRenderer toe2L;
    public WRModelRenderer toe3L;
    public WRModelRenderer neck1;
    public WRModelRenderer arm1L;
    public WRModelRenderer arm1R;
    public WRModelRenderer wingR1;
    public WRModelRenderer spike31;
    public WRModelRenderer wingL1;
    public WRModelRenderer neck2;
    public WRModelRenderer neck3;
    public WRModelRenderer spike2;
    public WRModelRenderer head;
    public WRModelRenderer spike1;
    public WRModelRenderer snout;
    public WRModelRenderer jaw;
    public WRModelRenderer frill1;
    public WRModelRenderer frillL;
    public WRModelRenderer frillR;
    public WRModelRenderer eyeL;
    public WRModelRenderer eyeR;
    public WRModelRenderer teeth1;
    public WRModelRenderer teeth2;
    public WRModelRenderer crownHorn1;
    public WRModelRenderer crownHornL1;
    public WRModelRenderer crownHornL2;
    public WRModelRenderer crownHornR2;
    public WRModelRenderer crownHornR1;
    public WRModelRenderer arm2L;
    public WRModelRenderer palmL;
    public WRModelRenderer claw3L;
    public WRModelRenderer claw2L;
    public WRModelRenderer claw1L;
    public WRModelRenderer arm2R;
    public WRModelRenderer palmR;
    public WRModelRenderer claw3R;
    public WRModelRenderer claw2R;
    public WRModelRenderer claw1R;
    public WRModelRenderer wingR2;
    public WRModelRenderer membraneR1;
    public WRModelRenderer membraneR2;
    public WRModelRenderer palmR_1;
    public WRModelRenderer membraneR3;
    public WRModelRenderer fingerR1part1;
    public WRModelRenderer fingerR2part1;
    public WRModelRenderer fingerR4part1;
    public WRModelRenderer fingerR3part1;
    public WRModelRenderer fingerR1part2;
    public WRModelRenderer membraneR6;
    public WRModelRenderer fingerR2part2;
    public WRModelRenderer membraneR5;
    public WRModelRenderer fingerR4part2;
    public WRModelRenderer fingerR3part2;
    public WRModelRenderer membraneR4;
    public WRModelRenderer wingL2;
    public WRModelRenderer membraneL1;
    public WRModelRenderer membraneL2;
    public WRModelRenderer palmL_1;
    public WRModelRenderer membraneL3;
    public WRModelRenderer fingerL1part1;
    public WRModelRenderer fingerL2part1;
    public WRModelRenderer fingerL4part1;
    public WRModelRenderer fingerL3part1;
    public WRModelRenderer fingerL1part2;
    public WRModelRenderer membraneL6;
    public WRModelRenderer fingerL2part2;
    public WRModelRenderer membraneL5;
    public WRModelRenderer fingerL4part2;
    public WRModelRenderer fingerL3part2;
    public WRModelRenderer membraneL4;

    public WRModelRenderer[] tailParts;
    public WRModelRenderer[] headParts;

    public RoyalRedModel()
    {
        this.texWidth = 130;
        this.texHeight = 125;
        this.fingerL2part1 = new WRModelRenderer(this, 0, 58);
        this.fingerL2part1.setPos(1.4F, -0.2F, 0.5F);
        this.fingerL2part1.addBox(0.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerL2part1, 0.0F, -1.2747884856566583F, 0.0F);
        this.fingerR1part2 = new WRModelRenderer(this, 0, 58);
        this.fingerR1part2.setPos(-20.0F, 0.0F, 0.0F);
        this.fingerR1part2.addBox(-20.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerR1part2, 0.0F, 0.22759093446006054F, 0.0F);
        this.fingerL3part2 = new WRModelRenderer(this, 43, 58);
        this.fingerL3part2.setPos(15.0F, 0.0F, 0.0F);
        this.fingerL3part2.addBox(0.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        this.setRotateAngle(fingerL3part2, 0.0F, -0.22759093446006054F, 0.0F);
        this.spike11 = new WRModelRenderer(this, 28, 43);
        this.spike11.setPos(0.0F, -0.5F, 1.5F);
        this.spike11.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike11, 0.5235987755982988F, 0.0F, 0.0F);
        this.toe2R = new WRModelRenderer(this, 41, 36);
        this.toe2R.setPos(0.0F, 0.8F, -1.5F);
        this.toe2R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(toe2R, 0.18203784098300857F, 0.0F, 0.0F);
        this.neck2 = new WRModelRenderer(this, 77, 0);
        this.neck2.setPos(0.01F, 0.01F, -3.0F);
        this.neck2.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(neck2, -0.4553564018453205F, 0.0F, 0.0F);
        this.claw3R = new WRModelRenderer(this, 31, 36);
        this.claw3R.mirror = true;
        this.claw3R.setPos(-0.4F, 1.4F, -0.5F);
        this.claw3R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(claw3R, -0.7285004297824331F, -0.40980330836826856F, -0.5918411493512771F);
        this.palmL_1 = new WRModelRenderer(this, 36, 53);
        this.palmL_1.setPos(14.5F, 0.01F, 0.01F);
        this.palmL_1.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(palmL_1, 0.0F, -1.2292353921796064F, 0.0F);
        this.membraneL6 = new WRModelRenderer(this, -20, 61);
        this.membraneL6.mirror = true;
        this.membraneL6.setPos(0.0F, 0.0F, 0.1F);
        this.membraneL6.addBox(0.0F, 0.0F, 0.0F, 40, 0, 20, 0.0F);
        this.frillL = new WRModelRenderer(this, 34, 22);
        this.frillL.setPos(1.5F, 0.0F, -3.0F);
        this.frillL.addBox(0.0F, -2.0F, -0.5F, 3, 3, 1, 0.0F);
        this.setRotateAngle(frillL, 0.0F, -0.7740535232594852F, -0.5918411493512771F);
        this.palmR = new WRModelRenderer(this, 22, 36);
        this.palmR.setPos(-0.3F, 4.5F, -0.01F);
        this.palmR.addBox(-0.5F, 0.0F, -1.0F, 1, 2, 2, 0.0F);
        this.setRotateAngle(palmR, 0.0F, 0.0F, -0.40980330836826856F);
        this.head = new WRModelRenderer(this, 77, 10);
        this.head.setPos(0.01F, -0.1F, -3.0F);
        this.head.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(head, 0.5009094953223726F, 0.0F, 0.0F);
        this.tailspike2part4 = new WRModelRenderer(this, 25, 16);
        this.tailspike2part4.setPos(-2.5F, -0.01F, 0.0F);
        this.tailspike2part4.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(tailspike2part4, 0.0F, -0.18203784098300857F, 0.0F);
        this.arm1R = new WRModelRenderer(this, 0, 36);
        this.arm1R.mirror = true;
        this.arm1R.setPos(-2.0F, 0.0F, -2.5F);
        this.arm1R.addBox(-2.0F, 0.0F, -1.5F, 2, 5, 3, 0.0F);
        this.setRotateAngle(arm1R, 0.7285004297824331F, -0.5f, 0.0F);
        this.tail7 = new WRModelRenderer(this, 54, 30);
        this.tail7.setPos(0.0F, -0.2F, 4.0F);
        this.tail7.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5, 0.0F);
        this.arm1L = new WRModelRenderer(this, 0, 36);
        this.arm1L.setPos(2.0F, 0.0F, -2.5F);
        this.arm1L.addBox(0.0F, 0.0F, -1.5F, 2, 5, 3, 0.0F);
        this.setRotateAngle(arm1L, 0.7285004297824331F, 0.5f, 0.0F);
        this.tailspike3part1 = new WRModelRenderer(this, 0, 15);
        this.tailspike3part1.setPos(0.0F, 0.0F, 1.0F);
        this.tailspike3part1.addBox(-4.0F, -0.5F, -1.0F, 4, 1, 2, 0.0F);
        this.setRotateAngle(tailspike3part1, 0.0F, 0.8196066167365371F, 0.5462880558742251F);
        this.spike31 = new WRModelRenderer(this, 28, 43);
        this.spike31.setPos(0.0F, -2.5F, 0.3F);
        this.spike31.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike31, 0.5235987755982988F, 0.0F, 0.0F);
        this.leg2L = new WRModelRenderer(this, 15, 27);
        this.leg2L.setPos(1.49F, 4.0F, -2.0F);
        this.leg2L.addBox(-1.5F, 0.0F, 0.0F, 3, 5, 3, 0.0F);
        this.setRotateAngle(leg2L, 0.5009094953223726F, 0.0F, 0.0F);
        this.spike12 = new WRModelRenderer(this, 28, 43);
        this.spike12.setPos(0.0F, -0.5F, 1.5F);
        this.spike12.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike12, 0.5235987755982988F, 0.0F, 0.0F);
        this.neck1 = new WRModelRenderer(this, 77, 0);
        this.neck1.setPos(0.0F, -0.7F, -4.5F);
        this.neck1.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(neck1, -0.18203784098300857F, 0.0F, 0.0F);
        this.leg1R = new WRModelRenderer(this, 0, 25);
        this.leg1R.mirror = true;
        this.leg1R.setPos(-1.4F, 0.0F, 1.0F);
        this.leg1R.addBox(-3.0F, -1.0F, -2.0F, 3, 5, 4, 0.0F);
        this.setRotateAngle(leg1R, 0.045553093477052F, 0.0F, 0.0F);
        this.membraneR1 = new WRModelRenderer(this, 70, 93);
        this.membraneR1.setPos(-10.0F, 0.0F, -0.8F);
        this.membraneR1.addBox(0.0F, 0.0F, 0.0F, 10, 0, 11, 0.0F);
        this.wingR2 = new WRModelRenderer(this, 0, 53);
        this.wingR2.setPos(-9.5F, 0.01F, 0.0F);
        this.wingR2.addBox(-15.0F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
        this.setRotateAngle(wingR2, 0.0F, -2.231054382824351F, 0.0F);
        this.jaw = new WRModelRenderer(this, 77, 29);
        this.jaw.setPos(0.01F, 1.3F, -3.0F);
        this.jaw.addBox(-2.0F, -0.5F, -5.0F, 4, 1, 5, 0.0F);
        this.fingerL1part2 = new WRModelRenderer(this, 0, 58);
        this.fingerL1part2.setPos(20.0F, 0.0F, 0.0F);
        this.fingerL1part2.addBox(0.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerL1part2, 0.0F, -0.22759093446006054F, 0.0F);
        this.frillR = new WRModelRenderer(this, 34, 22);
        this.frillR.mirror = true;
        this.frillR.setPos(-1.5F, 0.0F, -3.0F);
        this.frillR.addBox(-3.0F, -2.0F, -0.5F, 3, 3, 1, 0.0F);
        this.setRotateAngle(frillR, 0.0F, 0.7740535232594852F, 0.5918411493512771F);
        this.fingerR3part1 = new WRModelRenderer(this, 43, 58);
        this.fingerR3part1.setPos(-1.0F, -0.1F, 0.0F);
        this.fingerR3part1.addBox(-15.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        this.setRotateAngle(fingerR3part1, 0.0F, 1.4570008595648662F, 0.0F);
        this.spike4 = new WRModelRenderer(this, 28, 43);
        this.spike4.setPos(0.0F, -0.5F, 2.5F);
        this.spike4.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike4, 0.5235987755982988F, 0.0F, 0.0F);
        this.toe3R = new WRModelRenderer(this, 41, 36);
        this.toe3R.setPos(-1.0F, 0.8F, -1.5F);
        this.toe3R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(toe3R, 0.18203784098300857F, 0.40980330836826856F, 0.0F);
        this.tail1 = new WRModelRenderer(this, 54, 0);
        this.tail1.setPos(0.0F, -0.2F, 2.5F);
        this.tail1.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 5, 0.0F);
        this.footR = new WRModelRenderer(this, 41, 29);
        this.footR.setPos(-0.01F, 4.0F, -1.3F);
        this.footR.addBox(-1.5F, 0.0F, -2.0F, 3, 2, 3, 0.0F);
        this.setRotateAngle(footR, 0.31869712141416456F, 0.0F, 0.0F);
        this.tailspike2part3 = new WRModelRenderer(this, 14, 16);
        this.tailspike2part3.setPos(-3.5F, -0.01F, 0.0F);
        this.tailspike2part3.addBox(-4.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(tailspike2part3, 0.0F, -0.18203784098300857F, 0.0F);
        this.tail3 = new WRModelRenderer(this, 54, 11);
        this.tail3.setPos(0.0F, -0.2F, 4.0F);
        this.tail3.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 5, 0.0F);
        this.membraneL2 = new WRModelRenderer(this, 71, 82);
        this.membraneL2.mirror = true;
        this.membraneL2.setPos(10.0F, 0.1F, -0.8F);
        this.membraneL2.addBox(-5.0F, 0.0F, 0.0F, 10, 0, 10, 0.0F);
        this.setRotateAngle(membraneL2, 0.0F, 0.5009094953223726F, 0.0F);
        this.leg1L = new WRModelRenderer(this, 0, 25);
        this.leg1L.setPos(1.4F, 0.0F, 1.0F);
        this.leg1L.addBox(0.0F, -1.0F, -2.0F, 3, 5, 4, 0.0F);
        this.setRotateAngle(leg1L, 0.045553093477052F, 0.0F, 0.0F);
        this.crownHornL1 = new WRModelRenderer(this, 13, 21);
        this.crownHornL1.setPos(2.0F, -0.5F, 0.0F);
        this.crownHornL1.addBox(-0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(crownHornL1, 0.0F, -0.18203784098300857F, 0.18203784098300857F);
        this.fingerR2part2 = new WRModelRenderer(this, 0, 58);
        this.fingerR2part2.setPos(-20.0F, 0.0F, 0.0F);
        this.fingerR2part2.addBox(-20.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerR2part2, 0.0F, 0.22759093446006054F, 0.0F);
        this.tailspike1part2 = new WRModelRenderer(this, 0, 20);
        this.tailspike1part2.setPos(0.0F, 0.0F, 3.5F);
        this.tailspike1part2.addBox(0.0F, -0.5F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(tailspike1part2, 0.0F, -1.1383037381507017F, -0.27314402793711257F);
        this.crownHornR1 = new WRModelRenderer(this, 13, 21);
        this.crownHornR1.mirror = true;
        this.crownHornR1.setPos(-2.0F, -0.5F, 0.0F);
        this.crownHornR1.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(crownHornR1, 0.0F, 0.18203784098300857F, -0.18203784098300857F);
        this.snout = new WRModelRenderer(this, 77, 20);
        this.snout.setPos(-0.01F, -0.3F, -3.0F);
        this.snout.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0.0F);
        this.wingL1 = new WRModelRenderer(this, 0, 46);
        this.wingL1.setPos(2.0F, -1.8F, -3.3F);
        this.wingL1.addBox(0.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        this.setRotateAngle(wingL1, -0.18203784098300857F, -0.6373942428283291F, -0.7740535232594852F);
        this.toe1R = new WRModelRenderer(this, 41, 36);
        this.toe1R.setPos(1.0F, 0.8F, -1.5F);
        this.toe1R.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(toe1R, 0.18203784098300857F, -0.40980330836826856F, 0.0F);
        this.tailspike2part2 = new WRModelRenderer(this, 25, 16);
        this.tailspike2part2.mirror = true;
        this.tailspike2part2.setPos(2.5F, -0.01F, 0.0F);
        this.tailspike2part2.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(tailspike2part2, 0.0F, 0.18203784098300857F, 0.0F);
        this.body2 = new WRModelRenderer(this, 27, 0);
        this.body2.setPos(0.0F, -18.8F, 6.2F);
        this.body2.addBox(-3.0F, -3.0F, -3.5F, 6, 6, 7, 0.0F);
        this.setRotateAngle(body2, -0.045553093477052F, 0.0F, 0.0F);
        this.teeth2 = new WRModelRenderer(this, 77, 36);
        this.teeth2.setPos(0.0F, -0.5F, -0.7F);
        this.teeth2.addBox(-1.5F, -1.0F, -4.0F, 3, 1, 4, 0.0F);
        this.tailspike2part1 = new WRModelRenderer(this, 14, 16);
        this.tailspike2part1.mirror = true;
        this.tailspike2part1.setPos(3.5F, -0.01F, 0.0F);
        this.tailspike2part1.addBox(0.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(tailspike2part1, 0.0F, 0.18203784098300857F, 0.0F);
        this.spike7 = new WRModelRenderer(this, 28, 43);
        this.spike7.setPos(0.0F, -1.5F, 1.5F);
        this.spike7.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike7, 0.5235987755982988F, 0.0F, 0.0F);
        this.fingerL1part1 = new WRModelRenderer(this, 0, 58);
        this.fingerL1part1.setPos(1.4F, -0.3F, -0.5F);
        this.fingerL1part1.addBox(0.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerL1part1, 0.0F, -1.0927506446736497F, 0.0F);
        this.frill1 = new WRModelRenderer(this, 39, 16);
        this.frill1.setPos(0.01F, -1.5F, -3.0F);
        this.frill1.addBox(-2.0F, -3.0F, -0.5F, 4, 3, 1, 0.0F);
        this.setRotateAngle(frill1, -1.0471975511965976F, 0.0F, 0.0F);
        this.wingL2 = new WRModelRenderer(this, 0, 53);
        this.wingL2.setPos(9.5F, 0.01F, 0.0F);
        this.wingL2.addBox(0.0F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
        this.setRotateAngle(wingL2, 0.0F, 2.231054382824351F, 0.0F);
        this.palmL = new WRModelRenderer(this, 22, 36);
        this.palmL.mirror = true;
        this.palmL.setPos(0.3F, 4.5F, -0.01F);
        this.palmL.addBox(-0.5F, 0.0F, -1.0F, 1, 2, 2, 0.0F);
        this.setRotateAngle(palmL, 0.0F, 0.0F, 0.40980330836826856F);
        this.membraneL3 = new WRModelRenderer(this, 61, 61);
        this.membraneL3.mirror = true;
        this.membraneL3.setPos(0.0F, -0.1F, 0.0F);
        this.membraneL3.addBox(0.0F, 0.0F, 0.0F, 15, 0, 20, 0.0F);
        this.setRotateAngle(membraneL3, 0.0F, -1.3203415791337103F, 0.0F);
        this.arm2R = new WRModelRenderer(this, 12, 36);
        this.arm2R.mirror = true;
        this.arm2R.setPos(-0.99F, 4.0F, 0.0F);
        this.arm2R.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(arm2R, -1.1383037381507017F, 0.0F, 0.0F);
        this.tail8 = new WRModelRenderer(this, 54, 30);
        this.tail8.setPos(0.01F, 0.01F, 4.0F);
        this.tail8.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5, 0.0F);
        this.claw2R = new WRModelRenderer(this, 31, 36);
        this.claw2R.mirror = true;
        this.claw2R.setPos(-0.4F, 1.7F, 0.0F);
        this.claw2R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(claw2R, 0.0F, 0.0F, -0.5462880558742251F);
        this.tail2 = new WRModelRenderer(this, 54, 0);
        this.tail2.setPos(0.01F, 0.01F, 4.0F);
        this.tail2.addBox(-2.5F, -2.5F, 0.0F, 5, 5, 5, 0.0F);
        this.claw1R = new WRModelRenderer(this, 31, 36);
        this.claw1R.mirror = true;
        this.claw1R.setPos(-0.4F, 1.2F, 0.5F);
        this.claw1R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(claw1R, 0.5462880558742251F, 0.0F, -0.5462880558742251F);
        this.membraneR6 = new WRModelRenderer(this, -20, 61);
        this.membraneR6.setPos(0.0F, 0.0F, 0.1F);
        this.membraneR6.addBox(-40.0F, 0.0F, 0.0F, 40, 0, 20, 0.0F);
        this.spike9 = new WRModelRenderer(this, 28, 43);
        this.spike9.setPos(0.0F, -1.0F, 1.5F);
        this.spike9.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike9, 0.5235987755982988F, 0.0F, 0.0F);
        this.tailspike4part1 = new WRModelRenderer(this, 0, 20);
        this.tailspike4part1.setPos(0.0F, 0.0F, 3.5F);
        this.tailspike4part1.addBox(-3.0F, -0.5F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(tailspike4part1, 0.0F, 1.1383037381507017F, 0.27314402793711257F);
        this.spike8 = new WRModelRenderer(this, 28, 43);
        this.spike8.setPos(0.0F, -1.5F, 1.5F);
        this.spike8.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike8, 0.5235987755982988F, 0.0F, 0.0F);
        this.fingerR4part1 = new WRModelRenderer(this, 45, 55);
        this.fingerR4part1.setPos(-0.5F, 0.0F, 0.5F);
        this.fingerR4part1.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(fingerR4part1, 0.0F, 1.6390387005478748F, 0.0F);
        this.leg2R = new WRModelRenderer(this, 15, 27);
        this.leg2R.mirror = true;
        this.leg2R.setPos(-1.49F, 4.0F, -2.0F);
        this.leg2R.addBox(-1.5F, 0.0F, 0.0F, 3, 5, 3, 0.0F);
        this.setRotateAngle(leg2R, 0.5009094953223726F, 0.0F, 0.0F);
        this.fingerR3part2 = new WRModelRenderer(this, 43, 58);
        this.fingerR3part2.setPos(-15.0F, 0.0F, 0.0F);
        this.fingerR3part2.addBox(-15.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        this.setRotateAngle(fingerR3part2, 0.0F, 0.22759093446006054F, 0.0F);
        this.neck3 = new WRModelRenderer(this, 77, 0);
        this.neck3.setPos(-0.01F, -0.01F, -3.0F);
        this.neck3.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(neck3, 0.40980330836826856F, 0.0F, 0.0F);
        this.crownHornR2 = new WRModelRenderer(this, 13, 21);
        this.crownHornR2.mirror = true;
        this.crownHornR2.setPos(-2.0F, -1.7F, 0.0F);
        this.crownHornR2.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(crownHornR2, 0.27314402793711257F, 0.18203784098300857F, 0.22759093446006054F);
        this.spike2 = new WRModelRenderer(this, 28, 43);
        this.spike2.setPos(0.0F, -1.5F, -3.5F);
        this.spike2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike2, 0.5235987755982988F, 0.0F, 0.0F);
        this.crownHornL2 = new WRModelRenderer(this, 13, 21);
        this.crownHornL2.setPos(2.0F, -1.7F, 0.0F);
        this.crownHornL2.addBox(-0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(crownHornL2, 0.27314402793711257F, -0.18203784098300857F, -0.22759093446006054F);
        this.leg3L = new WRModelRenderer(this, 29, 27);
        this.leg3L.setPos(-0.01F, 5.0F, 3.0F);
        this.leg3L.addBox(-1.5F, 0.0F, -2.1F, 3, 5, 2, 0.0F);
        this.setRotateAngle(leg3L, -0.8196066167365371F, 0.0F, 0.0F);
        this.membraneL4 = new WRModelRenderer(this, -15, 103);
        this.membraneL4.mirror = true;
        this.membraneL4.setPos(0.0F, 0.2F, 0.0F);
        this.membraneL4.addBox(0.0F, 0.0F, 0.0F, 30, 0, 15, 0.0F);
        this.arm2L = new WRModelRenderer(this, 12, 36);
        this.arm2L.setPos(0.99F, 4.0F, 0.0F);
        this.arm2L.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
        this.setRotateAngle(arm2L, -1.1383037381507017F, 0.0F, 0.0F);
        this.membraneR2 = new WRModelRenderer(this, 71, 82);
        this.membraneR2.setPos(-10.0F, 0.1F, -0.8F);
        this.membraneR2.addBox(-5.0F, 0.0F, 0.0F, 10, 0, 10, 0.0F);
        this.setRotateAngle(membraneR2, 0.0F, -0.5009094953223726F, 0.0F);
        this.footL = new WRModelRenderer(this, 41, 29);
        this.footL.setPos(0.01F, 4.0F, -1.3F);
        this.footL.addBox(-1.5F, 0.0F, -2.0F, 3, 2, 3, 0.0F);
        this.setRotateAngle(footL, 0.31869712141416456F, 0.0F, 0.0F);
        this.leg3R = new WRModelRenderer(this, 29, 27);
        this.leg3R.setPos(0.01F, 5.0F, 3.0F);
        this.leg3R.addBox(-1.5F, 0.0F, -2.1F, 3, 5, 2, 0.0F);
        this.setRotateAngle(leg3R, -0.8196066167365371F, 0.0F, 0.0F);
        this.membraneL5 = new WRModelRenderer(this, -20, 82);
        this.membraneL5.mirror = true;
        this.membraneL5.setPos(0.0F, 0.0F, 0.0F);
        this.membraneL5.addBox(0.0F, 0.0F, 0.0F, 40, 0, 20, 0.0F);
        this.eyeR = new WRModelRenderer(this, 46, 24);
        this.eyeR.mirror = true;
        this.eyeR.setPos(-1.7F, -1.3F, -3.8F);
        this.eyeR.addBox(-1.5F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(eyeR, 0.0F, 1.1383037381507017F, 0.0F);
        this.palmR_1 = new WRModelRenderer(this, 36, 53);
        this.palmR_1.setPos(-14.5F, 0.01F, 0.01F);
        this.palmR_1.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(palmR_1, 0.0F, 1.2292353921796064F, 0.0F);
        this.claw1L = new WRModelRenderer(this, 31, 36);
        this.claw1L.setPos(0.4F, 1.2F, 0.5F);
        this.claw1L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(claw1L, 0.5462880558742251F, 0.0F, 0.5462880558742251F);
        this.tail5 = new WRModelRenderer(this, 54, 21);
        this.tail5.setPos(0.0F, -0.2F, 4.0F);
        this.tail5.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 5, 0.0F);
        this.claw3L = new WRModelRenderer(this, 31, 36);
        this.claw3L.setPos(0.4F, 1.4F, -0.5F);
        this.claw3L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(claw3L, -0.7285004297824331F, 0.40980330836826856F, 0.5918411493512771F);
        this.tail6 = new WRModelRenderer(this, 54, 21);
        this.tail6.setPos(0.01F, 0.01F, 4.0F);
        this.tail6.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 5, 0.0F);
        this.membraneR3 = new WRModelRenderer(this, 61, 61);
        this.membraneR3.setPos(0.0F, -0.1F, 0.0F);
        this.membraneR3.addBox(-15.0F, 0.0F, 0.0F, 15, 0, 20, 0.0F);
        this.setRotateAngle(membraneR3, 0.0F, 1.3203415791337103F, 0.0F);
        this.fingerL4part1 = new WRModelRenderer(this, 45, 55);
        this.fingerL4part1.setPos(0.5F, 0.0F, 0.5F);
        this.fingerL4part1.addBox(0.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(fingerL4part1, 0.0F, -1.6390387005478748F, 0.0F);
        this.spike5 = new WRModelRenderer(this, 28, 43);
        this.spike5.setPos(0.0F, -2.0F, 1.5F);
        this.spike5.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike5, 0.5235987755982988F, 0.0F, 0.0F);
        this.spike10 = new WRModelRenderer(this, 28, 43);
        this.spike10.setPos(0.0F, -1.0F, 1.5F);
        this.spike10.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike10, 0.5235987755982988F, 0.0F, 0.0F);
        this.eyeL = new WRModelRenderer(this, 46, 24);
        this.eyeL.setPos(1.7F, -1.3F, -3.8F);
        this.eyeL.addBox(-0.5F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(eyeL, 0.0F, -1.1383037381507017F, 0.0F);
        this.fingerR4part2 = new WRModelRenderer(this, 45, 55);
        this.fingerR4part2.setPos(-10.0F, 0.0F, 0.0F);
        this.fingerR4part2.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(fingerR4part2, 0.0F, 0.22759093446006054F, 0.0F);
        this.spike6 = new WRModelRenderer(this, 28, 43);
        this.spike6.setPos(0.0F, -2.0F, 1.5F);
        this.spike6.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike6, 0.5235987755982988F, 0.0F, 0.0F);
        this.fingerL4part2 = new WRModelRenderer(this, 45, 55);
        this.fingerL4part2.setPos(10.0F, 0.0F, 0.0F);
        this.fingerL4part2.addBox(0.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(fingerL4part2, 0.0F, -0.22759093446006054F, 0.0F);
        this.toe1L = new WRModelRenderer(this, 41, 36);
        this.toe1L.setPos(1.0F, 0.8F, -1.5F);
        this.toe1L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(toe1L, 0.18203784098300857F, -0.40980330836826856F, 0.0F);
        this.toe3L = new WRModelRenderer(this, 41, 36);
        this.toe3L.setPos(-1.0F, 0.8F, -1.5F);
        this.toe3L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(toe3L, 0.18203784098300857F, 0.40980330836826856F, 0.0F);
        this.membraneR4 = new WRModelRenderer(this, -15, 103);
        this.membraneR4.setPos(0.0F, 0.2F, 0.0F);
        this.membraneR4.addBox(-30.0F, 0.0F, 0.0F, 30, 0, 15, 0.0F);
        this.crownHorn1 = new WRModelRenderer(this, 22, 21);
        this.crownHorn1.setPos(0.0F, -2.5F, 0.1F);
        this.crownHorn1.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(crownHorn1, 0.22759093446006054F, 0.0F, 0.0F);
        this.toe2L = new WRModelRenderer(this, 41, 36);
        this.toe2L.setPos(0.0F, 0.8F, -1.5F);
        this.toe2L.addBox(-0.5F, -1.0F, -3.0F, 1, 2, 3, 0.0F);
        this.setRotateAngle(toe2L, 0.18203784098300857F, 0.0F, 0.0F);
        this.fingerR1part1 = new WRModelRenderer(this, 0, 58);
        this.fingerR1part1.setPos(-1.4F, -0.3F, -0.5F);
        this.fingerR1part1.addBox(-20.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerR1part1, 0.0F, 1.0927506446736497F, 0.0F);
        this.membraneL1 = new WRModelRenderer(this, 70, 93);
        this.membraneL1.mirror = true;
        this.membraneL1.setPos(0.0F, 0.0F, -0.8F);
        this.membraneL1.addBox(0.0F, 0.0F, 0.0F, 10, 0, 11, 0.0F);
        this.teeth1 = new WRModelRenderer(this, 77, 43);
        this.teeth1.setPos(-0.01F, 2.0F, 0.2F);
        this.teeth1.addBox(-1.5F, -1.0F, -5.0F, 3, 1, 4, 0.0F);
        this.tail4 = new WRModelRenderer(this, 54, 11);
        this.tail4.setPos(0.01F, 0.01F, 4.0F);
        this.tail4.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 5, 0.0F);
        this.spike1 = new WRModelRenderer(this, 28, 43);
        this.spike1.setPos(0.0F, -1.5F, -3.5F);
        this.spike1.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(spike1, 0.5235987755982988F, 0.0F, 0.0F);
        this.membraneR5 = new WRModelRenderer(this, -20, 82);
        this.membraneR5.setPos(0.0F, 0.0F, 0.0F);
        this.membraneR5.addBox(-40.0F, 0.0F, 0.0F, 40, 0, 20, 0.0F);
        this.fingerL2part2 = new WRModelRenderer(this, 0, 58);
        this.fingerL2part2.setPos(20.0F, 0.0F, 0.0F);
        this.fingerL2part2.addBox(0.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerL2part2, 0.0F, -0.22759093446006054F, 0.0F);
        this.fingerR2part1 = new WRModelRenderer(this, 0, 58);
        this.fingerR2part1.setPos(-1.4F, -0.2F, 0.5F);
        this.fingerR2part1.addBox(-20.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        this.setRotateAngle(fingerR2part1, 0.0F, 1.2747884856566583F, 0.0F);
        this.body1 = new WRModelRenderer(this, 0, 0);
        this.body1.setPos(-0.01F, -0.4F, -3.8F);
        this.body1.addBox(-3.0F, -3.0F, -5.5F, 6, 6, 7, 0.0F);
        this.setRotateAngle(body1, -0.14713125594312196F, 0.0F, 0.0F);
        this.tailspike1part1 = new WRModelRenderer(this, 0, 15);
        this.tailspike1part1.setPos(0.0F, 0.0F, 1.0F);
        this.tailspike1part1.addBox(0.0F, -0.5F, -1.0F, 4, 1, 2, 0.0F);
        this.setRotateAngle(tailspike1part1, 0.0F, -0.8196066167365371F, -0.5462880558742251F);
        this.claw2L = new WRModelRenderer(this, 31, 36);
        this.claw2L.setPos(0.4F, 1.7F, 0.0F);
        this.claw2L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(claw2L, 0.0F, 0.0F, 0.5462880558742251F);
        this.wingR1 = new WRModelRenderer(this, 0, 46);
        this.wingR1.mirror = true;
        this.wingR1.setPos(-2.0F, -1.8F, -3.3F);
        this.wingR1.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        this.setRotateAngle(wingR1, -0.18203784098300857F, 0.6373942428283291F, 0.7740535232594852F);
        this.fingerL3part1 = new WRModelRenderer(this, 43, 58);
        this.fingerL3part1.setPos(1.0F, -0.1F, 0.0F);
        this.fingerL3part1.addBox(0.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        this.setRotateAngle(fingerL3part1, 0.0F, -1.4570008595648662F, 0.0F);
        this.palmL_1.addChild(this.fingerL2part1);
        this.fingerR1part1.addChild(this.fingerR1part2);
        this.fingerL3part1.addChild(this.fingerL3part2);
        this.tail7.addChild(this.spike11);
        this.footR.addChild(this.toe2R);
        this.neck1.addChild(this.neck2);
        this.palmR.addChild(this.claw3R);
        this.wingL2.addChild(this.palmL_1);
        this.fingerL1part1.addChild(this.membraneL6);
        this.head.addChild(this.frillL);
        this.arm2R.addChild(this.palmR);
        this.neck3.addChild(this.head);
        this.tailspike4part1.addChild(this.tailspike2part4);
        this.body1.addChild(this.arm1R);
        this.tail6.addChild(this.tail7);
        this.body1.addChild(this.arm1L);
        this.tail8.addChild(this.tailspike3part1);
        this.body1.addChild(this.spike31);
        this.leg1L.addChild(this.leg2L);
        this.tail8.addChild(this.spike12);
        this.body1.addChild(this.neck1);
        this.body2.addChild(this.leg1R);
        this.wingR1.addChild(this.membraneR1);
        this.wingR1.addChild(this.wingR2);
        this.head.addChild(this.jaw);
        this.fingerL1part1.addChild(this.fingerL1part2);
        this.head.addChild(this.frillR);
        this.palmR_1.addChild(this.fingerR3part1);
        this.body2.addChild(this.spike4);
        this.footR.addChild(this.toe3R);
        this.body2.addChild(this.tail1);
        this.leg3R.addChild(this.footR);
        this.tailspike3part1.addChild(this.tailspike2part3);
        this.tail2.addChild(this.tail3);
        this.wingL1.addChild(this.membraneL2);
        this.body2.addChild(this.leg1L);
        this.frillL.addChild(this.crownHornL1);
        this.fingerR2part1.addChild(this.fingerR2part2);
        this.tail8.addChild(this.tailspike1part2);
        this.frillR.addChild(this.crownHornR1);
        this.head.addChild(this.snout);
        this.body1.addChild(this.wingL1);
        this.footR.addChild(this.toe1R);
        this.tailspike1part2.addChild(this.tailspike2part2);
        this.jaw.addChild(this.teeth2);
        this.tailspike1part1.addChild(this.tailspike2part1);
        this.tail3.addChild(this.spike7);
        this.palmL_1.addChild(this.fingerL1part1);
        this.head.addChild(this.frill1);
        this.wingL1.addChild(this.wingL2);
        this.arm2L.addChild(this.palmL);
        this.wingL2.addChild(this.membraneL3);
        this.arm1R.addChild(this.arm2R);
        this.tail7.addChild(this.tail8);
        this.palmR.addChild(this.claw2R);
        this.tail1.addChild(this.tail2);
        this.palmR.addChild(this.claw1R);
        this.fingerR1part1.addChild(this.membraneR6);
        this.tail5.addChild(this.spike9);
        this.tail8.addChild(this.tailspike4part1);
        this.tail4.addChild(this.spike8);
        this.palmR_1.addChild(this.fingerR4part1);
        this.leg1R.addChild(this.leg2R);
        this.fingerR3part1.addChild(this.fingerR3part2);
        this.neck2.addChild(this.neck3);
        this.frillR.addChild(this.crownHornR2);
        this.neck2.addChild(this.spike2);
        this.frillL.addChild(this.crownHornL2);
        this.leg2L.addChild(this.leg3L);
        this.fingerL3part1.addChild(this.membraneL4);
        this.arm1L.addChild(this.arm2L);
        this.wingR1.addChild(this.membraneR2);
        this.leg3L.addChild(this.footL);
        this.leg2R.addChild(this.leg3R);
        this.fingerL2part1.addChild(this.membraneL5);
        this.head.addChild(this.eyeR);
        this.wingR2.addChild(this.palmR_1);
        this.palmL.addChild(this.claw1L);
        this.tail4.addChild(this.tail5);
        this.palmL.addChild(this.claw3L);
        this.tail5.addChild(this.tail6);
        this.wingR2.addChild(this.membraneR3);
        this.palmL_1.addChild(this.fingerL4part1);
        this.tail1.addChild(this.spike5);
        this.tail6.addChild(this.spike10);
        this.head.addChild(this.eyeL);
        this.fingerR4part1.addChild(this.fingerR4part2);
        this.tail2.addChild(this.spike6);
        this.fingerL4part1.addChild(this.fingerL4part2);
        this.footL.addChild(this.toe1L);
        this.footL.addChild(this.toe3L);
        this.fingerR3part1.addChild(this.membraneR4);
        this.frill1.addChild(this.crownHorn1);
        this.footL.addChild(this.toe2L);
        this.palmR_1.addChild(this.fingerR1part1);
        this.wingL1.addChild(this.membraneL1);
        this.snout.addChild(this.teeth1);
        this.tail3.addChild(this.tail4);
        this.neck3.addChild(this.spike1);
        this.fingerR2part1.addChild(this.membraneR5);
        this.fingerL2part1.addChild(this.fingerL2part2);
        this.palmR_1.addChild(this.fingerR2part1);
        this.body2.addChild(this.body1);
        this.tail8.addChild(this.tailspike1part1);
        this.palmL.addChild(this.claw2L);
        this.body1.addChild(this.wingR1);
        this.palmL_1.addChild(this.fingerL3part1);

        this.tailParts = new WRModelRenderer[]{tail1,tail2, tail3, tail4, tail5, tail6, tail7, tail8};
        this.headParts = new WRModelRenderer[]{neck1, neck2, neck3, head};

        setDefaultPose();
    }

    @Override
    public ResourceLocation getTexture(RoyalRedEntity entity)
    {
        boolean shiny = entity.getVariant() == -1;

        if (entity.isBaby()) return CHILD;
        int index = (entity.isMale()? 0 : 1) + (shiny? 2 : 0);
        if (TEXTURES[index] == null)
        {
            String path = FOLDER + "royal_red/" + ((index & 1) != 0? "female" : "male");
            if ((index & 2) != 0) path += "_spe";
            return TEXTURES[index] = Wyrmroost.id(path + ".png");
        }
        return TEXTURES[index];
    }

    @Override
    public float getShadowRadius(RoyalRedEntity entity)
    {
        return 2.5f;
    }

    @Override
    public void scale(RoyalRedEntity entity, MatrixStack ms, float partialTicks)
    {
        super.scale(entity, ms, partialTicks);
        ms.scale(3.5f, 3.5f, 3.5f);
        ms.translate(0, 0.825f, -0.23f);
    }

    @Override
    public void renderToBuffer(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        body2.render(ms, buffer, packedLightIn, packedOverlayIn);
    }

    @Override
    public void postProcess(RoyalRedEntity entity, MatrixStack ms, IRenderTypeBuffer buffer, int light, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float partialTicks)
    {
        renderArmorOverlay(ms, buffer, light);
    }

    @Override
    public void setupAnim(RoyalRedEntity entity, float limbSwing, float limbSwingAmount, float bob, float netHeadYaw, float headPitch)
    {
        boolean flying = entity.flightTimer.get() > 0;
        membraneR2.visible = membraneL2.visible = !flying;
        this.bob = bob;

        resetToDefaultPose();
        animator().tick(entity, this, partialTicks);

        if (!entity.isInSittingPose() && !entity.isSleeping())
        {
            if (entity.isFlying()) // Flight Cycle
            {
                globalSpeed = 0.5f;

                chainWave(headParts, globalSpeed - 0.3f, 0.05f, 3, limbSwing, limbSwingAmount);
                chainWave(tailParts, globalSpeed - 0.3f, -0.05f, -3, limbSwing, limbSwingAmount);

                flap(wingR1, globalSpeed - 0.3f, 0.75f, false, 0, 0, limbSwing, limbSwingAmount);
                walk(wingR1, globalSpeed - 0.3f, 0.3f, false, 0.6f, 0, limbSwing, limbSwingAmount);
                flap(wingR2, globalSpeed - 0.3f, 0.65f, false, -1f, -0.1f, limbSwing, limbSwingAmount);
                walk(wingR2, globalSpeed - 0.3f, 0.05f, false, 0.6f, 0, limbSwing, limbSwingAmount);
                flap(palmR_1, globalSpeed - 0.3f, 0.4f, false, -1.5f, 0, limbSwing, limbSwingAmount);
                flap(wingL1, globalSpeed - 0.3f, 0.75f, true, 0, 0, limbSwing, limbSwingAmount);
                walk(wingL1, globalSpeed - 0.3f, 0.3f, false, 0.6f, 0, limbSwing, limbSwingAmount);
                flap(wingL2, globalSpeed - 0.3f, 0.65f, true, -1f, -0.1f, limbSwing, limbSwingAmount);
                walk(wingL2, globalSpeed - 0.3f, 0.05f, false, 0.6f, 0, limbSwing, limbSwingAmount);
                flap(palmL_1, globalSpeed - 0.3f, 0.4f, true, -1.5f, 0, limbSwing, limbSwingAmount);

                Vector3d motion = entity.getDeltaMovement();
                if (motion.y < 0 && motion.x != 0 && motion.z != 0)
                {
                    flap(wingR1, globalSpeed - 0.3f, 0.15f, false, 0, 0, bob, 0.5f);
                    walk(wingR1, globalSpeed + 0.65f, 0.15f, false, 0, 0, bob, 0.5f);
                    flap(wingL1, globalSpeed - 0.3f, 0.15f, true, 0, 0, bob, 0.5f);
                    walk(wingL1, globalSpeed + 0.65f, 0.15f, false, 0, 0, bob, 0.5f);
                }

                boolean wingsDown = wingR1.zRot < 1.35;
                if (!entity.wingsDown && wingsDown) entity.flapWings();
                entity.wingsDown = wingsDown;
            }
            else // Walk Cycle
            {
                bob(body2, globalSpeed + 0.3f, 0.5f, false, limbSwing, limbSwingAmount);

                flap(palmL, globalSpeed + 0.3f, 0.25f, false, 1, 0, limbSwing, limbSwingAmount);
                flap(palmR, globalSpeed + 0.3f, 0.25f, true, 1, 0, limbSwing, limbSwingAmount);

                walk(arm1R, globalSpeed + 0.3f, 0.1f, false, -1, 0, limbSwing, limbSwingAmount);
                walk(arm2R, globalSpeed + 0.3f, 0.05f, false, -1, 0, limbSwing, limbSwingAmount);
                walk(arm1L, globalSpeed + 0.3f, 0.1f, false, -1, 0, limbSwing, limbSwingAmount);
                walk(arm2L, globalSpeed + 0.3f, 0.05f, false, -1, 0, limbSwing, limbSwingAmount);

                swing(wingR1, globalSpeed + 0.3f, 0.075f, true, 1, 0, limbSwing, limbSwingAmount);
                swing(wingR2, globalSpeed + 0.3f, 0.05f, false, 1, 0, limbSwing, limbSwingAmount);

                swing(wingL1, globalSpeed + 0.3f, 0.075f, false, 1, 0, limbSwing, limbSwingAmount);
                swing(wingL2, globalSpeed + 0.3f, 0.05f, true, 1, 0, limbSwing, limbSwingAmount);

                walk(leg1L, globalSpeed - 0.1f, 1.25f, true, 0, 0, limbSwing, limbSwingAmount);
                walk(leg2L, globalSpeed - 0.1f, 1.25f, false, 0.5f, 0.7f, limbSwing, limbSwingAmount);
                walk(footL, globalSpeed - 0.1f, 0.75f, false, 0.25f, 0.2f, limbSwing, limbSwingAmount);

                walk(leg1R, globalSpeed - 0.1f, 1.25f, false, 0, 0, limbSwing, limbSwingAmount);
                walk(leg2R, globalSpeed - 0.1f, 1.25f, true, 0.5f, -0.7f, limbSwing, limbSwingAmount);
                walk(footR, globalSpeed - 0.1f, 0.75f, true, 0.25f, -0.2f, limbSwing, limbSwingAmount);
            }
        }

        fly(entity.flightTimer.get(partialTicks));
        breath(entity.breathTimer.get(partialTicks));
        sit(entity.sitTimer.get(partialTicks));
        sleep(entity.sleepTimer.get(partialTicks));
        knockout(entity.knockOutTimer.get(partialTicks));
        idle(bob);

        netHeadYaw = MathHelper.wrapDegrees(netHeadYaw);
        if (entity.flightTimer.get() == 1) body2.zRot = -(netHeadYaw * (Mafs.PI / 180F)) * 0.3f;
        if (!entity.isSleeping() && !entity.isKnockedOut())
            faceTarget(netHeadYaw, headPitch, 1, headParts);
    }

    public void idle(float frame)
    {
        if (entity.flightTimer.get() == 0)
        {
            if (entity.isKnockedOut())
            {
                chainWave(headParts, globalSpeed - 0.48f, 0.04f, -1.5f, frame, 0.5f);
                jaw.xRot += MathHelper.cos(frame * (globalSpeed - 0.48f)) * 0.1f + 0.1f;

                flap(wingL1, globalSpeed - 0.45f, 0.1f, false, 0, 0, frame, 0.5f);
                swing(wingL1, globalSpeed - 0.47f, 0.1f, false, 0, 0, frame, 0.5f);
                swing(wingL2, globalSpeed - 0.46f, 0.1f, false, 0, 0, frame, 0.5f);
                swing(palmL_1, globalSpeed - 0.45f, 0.03f, false, 0, 0.25f, frame, 0.5f);

                flap(wingR1, globalSpeed - 0.45f, 0.1f, true, 0, 0, frame, 0.5f);
                swing(wingR1, globalSpeed - 0.47f, 0.1f, true, 0, 0, frame, 0.5f);
                swing(wingR2, globalSpeed - 0.46f, 0.1f, true, 0, 0, frame, 0.5f);
                swing(palmR_1, globalSpeed - 0.45f, 0.03f, true, 0, 0.25f, frame, 0.5f);

                chainSwing(tailParts, globalSpeed - 0.48f, 0.06f, -2f, frame, 0.5f);
            }
            else
            {
                if (entity.isBreathingFire()) chainWave(headParts, 0.3f, 0.4f, 3f, frame, 0.1f);
                else chainWave(headParts, globalSpeed - 0.45f, 0.075f, -1.5f, frame, 0.5f);

                jaw.xRot += MathHelper.cos(frame * (globalSpeed - 0.45f)) * 0.1f + 0.1f;

                flap(wingL1, globalSpeed - 0.42f, 0.1f, false, 0, 0, frame, 0.5f);
                swing(wingL1, globalSpeed - 0.44f, 0.1f, false, 0, 0, frame, 0.5f);
                swing(wingL2, globalSpeed - 0.43f, 0.1f, false, 0, 0, frame, 0.5f);
                swing(palmL_1, globalSpeed - 0.42f, 0.07f, false, 0, 0.25f, frame, 0.5f);

                flap(wingR1, globalSpeed - 0.42f, 0.1f, true, 0, 0, frame, 0.5f);
                swing(wingR1, globalSpeed - 0.44f, 0.1f, true, 0, 0, frame, 0.5f);
                swing(wingR2, globalSpeed - 0.43f, 0.1f, true, 0, 0, frame, 0.5f);
                swing(palmR_1, globalSpeed - 0.42f, 0.07f, true, 0, 0.25f, frame, 0.5f);

                if (!entity.isSleeping())
                {
                    body1.xRot += MathHelper.cos(frame * (globalSpeed - 0.45f)) * 0.075f;
                    walk(arm2R, globalSpeed - 0.45f, 0.25f, false, 0, 0, frame, 0.5f);
                    flap(palmR, globalSpeed - 0.45f, 0.5f, true, 0, 0, frame, 0.5f);
                    walk(arm2L, globalSpeed - 0.45f, 0.25f, false, 0, 0, frame, 0.5f);
                    flap(palmL, globalSpeed - 0.45f, 0.5f, false, 0, 0, frame, 0.5f);
                }

                chainWave(tailParts, globalSpeed - 0.45f, 0.1f, -1.5f, frame, 0.5f);
                chainSwing(tailParts, globalSpeed - 0.46f, 0.075f, -2f, frame, 0.5f);
            }
        }
    }

    public void fly(float amount)
    {
        if (amount == 0) return;
        setTime(amount);

        rotate(neck1, 0.3f, 0, 0);
        rotate(neck2, 0.5f, 0, 0);
        rotate(neck3, -0.5f, 0, 0);
        rotate(head, -0.35f, 0, 0);

        rotate(wingR1, 0.18f, -0.65f, -0.77f);
        rotate(wingR2, 0f, 2.23f, 0);
        rotate(palmR_1, 0, -1.2f, 0);
        rotate(fingerR1part1, 0, -1.4f, 0);
        rotate(fingerR2part1, 0, -1.1f, 0);
        rotate(fingerR3part1, 0, -0.625f, 0);
        rotate(fingerR4part1, 0, -0.1f, 0);
        rotate(membraneR2, 0, 0.6f, 0);
        rotate(membraneR3, 0, -1.35f, 0);

        rotate(wingL1, 0.18f, 0.65f, 0.77f);
        rotate(wingL2, 0f, -2.23f, 0);
        rotate(palmL_1, 0, 1.2f, 0);
        rotate(fingerL1part1, 0, 1.4f, 0);
        rotate(fingerL2part1, 0, 1.1f, 0);
        rotate(fingerL3part1, 0, 0.625f, 0);
        rotate(fingerL4part1, 0, 0.1f, 0);
        rotate(membraneL2, 0, -0.6f, 0);
        rotate(membraneL3, 0, 1.35f, 0);

        rotate(leg1L, 1, 0, 0);
        rotate(leg3L, 0.3f, 0, 0);
        rotate(footL, 0.7f, 0, 0);

        rotate(leg1R, 1, 0, 0);
        rotate(leg3R, 0.3f, 0, 0);
        rotate(footR, 0.7f, 0, 0);
    }

    private void breath(float amount)
    {
        if (amount == 0) return;
        setTime(amount);

        if (!entity.isFlying())
        {
            rotate(neck1, 0.3f, 0, 0);
            rotate(neck2, 0.5f, 0, 0);
            rotate(neck3, -0.5f, 0, 0);
            rotate(head, -0.35f, 0, 0);
        }
        rotate(jaw, 0.65f, 0, 0);
    }

    public void sit(float amount)
    {
        if (amount == 0) return;
        setTime(amount);

        rotate(body2, -0.5f, 0, 0);
        move(body2, 0, 6.9f, 0);

        rotate(neck3, 0.15f, 0, 0);
        rotate(head, 0.32f, 0, 0);

        rotate(arm1L, 0, -0.25f, 0);
        rotate(arm2L, -0.3f, 0, 0);
        rotate(arm1R, 0, 0.25f, 0);
        rotate(arm2R, -0.3f, 0, 0);

        rotate(leg1L, -0.7f, 0, 0);
        rotate(leg2L, 0.5f, 0, 0);
        rotate(leg3L, -0.55f, 0, 0);
        rotate(footL, 1.25f, 0, 0);
        move(footL, 0, 0, -0.8f);

        rotate(leg1R, -0.7f, 0, 0);
        rotate(leg2R, 0.5f, 0, 0);
        rotate(leg3R, -0.55f, 0, 0);
        rotate(footR, 1.25f, 0, 0);
        move(footR, 0, 0, -0.8f);

        rotate(tail1, 0.3f, 0, 0);
        rotate(tail2, 0.2f, 0, 0);
        rotate(tail3, 0.1f, 0, 0);
        rotate(tail4, 0.1f, 0, 0);
        rotate(tail6, 0.05f, 0, 0);
        rotate(tail8, 0.2f, 0, 0);
    }

    public void sleep(float amount)
    {
        if (amount == 0) return;
        setTime(amount);
        toDefaultPose();

        move(body2, 0, 6.9f, 0);
        rotate(body1, 0.3f, 0, 0);

        rotate(neck1, 0.4f, 0.1f, 0);
        rotate(neck2, 0.7f, 0.2f, 0);
        rotate(neck3, -0.7f, 0.2f, 0);
        rotate(head, -0.9f, 0.5f, -0.3f);

        rotate(arm1L, -1.4f, -1f, 0);
        rotate(arm2L, 0.35f, 0.75f, 0);
        rotate(palmL, -0.7f, -0.3f, 0);

        rotate(arm1R, -1.4f, 1f, 0);
        rotate(arm2R, 0.35f, -0.75f, 0);
        rotate(palmR, -0.7f, 0.3f, 0);

        rotate(wingL1, 0.5f, 0, 1f);
        rotate(wingL2, 0.5f, 0, 0.35f);
        rotate(wingR1, 0.5f, 0, -1f);
        rotate(wingR2, 0.5f, 0, -0.35f);

        rotate(leg1L, -0.65f, 0, 0);
        rotate(leg2L, 1.05f, 0, 0);
        rotate(leg3L, -1.6f, 0, 0);
        move(leg3L, 0, 1f, -2.5f);
        rotate(footL, 1.25f, 0, 0);
        move(footL, 0, 0, -0.8f);

        rotate(leg1R, -0.65f, 0, 0);
        rotate(leg2R, 1f, 0, 0);
        rotate(leg3R, -1.6f, 0, 0);
        move(leg3R, 0, 1f, -2.5f);
        rotate(footR, 1.25f, 0, 0);
        move(footR, 0, 0, -0.8f);

        if (amount == 1)
        {
            eyeL.yRot = 2f;
            eyeL.z = -3f;
            eyeL.x = 2f;

            eyeR.yRot = -2f;
            eyeR.z = -3f;
            eyeR.x = -2f;
        }
    }

    private void knockout(float frame)
    {
        setTime(frame);
        toDefaultPose();

        move(body2, 0, 9.8f, 0);
        rotate(body1, 0.2f, 0, 0);

        rotate(neck1, 0.3f, 0, 0);
        rotate(neck2, 0.6f, 0, 0);
        rotate(neck3, -0.6f, 0, 0);
        rotate(head, -0.6f, 0, 0);

        rotate(wingR1, 0.2f, 0, -1.1f);
        rotate(wingR2, 0.3f, 0.4f, 0);

        rotate(wingL1, 0.2f, 0, 1.1f);
        rotate(wingL2, 0.3f, -0.4f, 0);

        rotate(arm1R, -1f, 0.5f, 0.8f);
        rotate(arm2R, 0.2f, -0.2f, 0.8f);
        rotate(claw1R, 0, 0, 0.4f);
        rotate(claw2R, 0, 0, 0.4f);
        rotate(claw3R, 0, 0, 0.4f);

        rotate(arm1L, -1f, -0.5f, -0.8f);
        rotate(arm2L, 0.2f, 0.2f, -0.8f);
        rotate(claw1L, 0, 0, -0.4f);
        rotate(claw2L, 0, 0, -0.4f);
        rotate(claw3L, 0, 0, -0.4f);

        rotate(leg1R, 1.35f, -0.6f, 0);
        rotate(leg2R, -0.25f, 0, -0.2f);
        rotate(leg3R, 0.5f, 0, 0);
        rotate(footR, 1.3f, 0, 0);

        rotate(leg1L, 1.35f, 0.6f, 0);
        rotate(leg2L, -0.25f, 0, 0.2f);
        rotate(leg3L, 0.5f, 0, 0);
        rotate(footL, 1.3f, 0, 0);

        if (frame == 1)
        {
            eyeL.yRot = 2f;
            eyeL.z = -3f;
            eyeL.x = 2f;

            eyeR.yRot = -2f;
            eyeR.z = -3f;
            eyeR.x = -2f;
        }
    }

    public void roarAnimation()
    {
        ModelAnimator animator = animator(); // reduce method calls

        animator.startKeyframe(10);

        if (!entity.isFlying())
        {
            animator.rotate(wingL1, -0.6f, 0.5f, 0.5f)
                    .rotate(wingL2, 0, -1f, 0)
                    .rotate(palmL_1, 0, 0.5f, 0)
                    .rotate(fingerL1part1, 0, 0.3f, 0)
                    .rotate(fingerL3part1, 0, -0.3f, 0)
                    .rotate(fingerL4part1, 0, -0.5f, 0)

                    .rotate(wingR1, -0.6f, -0.5f, -0.5f)
                    .rotate(wingR2, 0, 1f, 0)
                    .rotate(palmR_1, 0, -0.5f, 0)
                    .rotate(fingerR1part1, 0, -0.3f, 0)
                    .rotate(fingerR3part1, 0, 0.3f, 0)
                    .rotate(fingerR4part1, 0, 0.5f, 0)

                    .rotate(body1, 0.3f, 0, 0)

                    .rotate(neck1, 0.3f, 0, 0)
                    .rotate(neck2, 0.2f, 0, 0)
                    .rotate(neck3, -0.5f, 0, 0)
                    .rotate(head, -0.4f, 0, 0);
        }

        animator.rotate(palmL, 0, 0, -0.3f)
                .rotate(palmR, 0, 0, 0.3f)

                .rotate(jaw, 0.8f, 0, 0);

        int tick = entity.getAnimationTick();
        if (tick > 5 && tick < 60)
        {
            float delta = (Math.min(MathHelper.sin(((tick - 6) / 59f) * Mafs.PI) * 2, 1) * 0.5f);
            chainFlap(headParts, globalSpeed, 0.2f, 2.5, bob, delta);
            chainSwing(headParts, globalSpeed, 0.065f, 1, bob, delta);
        }
        for (WRModelRenderer tailPart : tailParts) animator.rotate(tailPart, 0.08f, 0, 0);

        animator.endKeyframe();
        animator.setStaticKeyframe(50)
                .resetKeyframe(10);
    }


    public void biteAttackAnimation()
    {
        animator().startKeyframe(5)
                .rotate(body1, -0.25f, 0, 0)
                .rotate(neck1, -0.3f, 0, 0)
                .rotate(neck2, 0.1f, 0, 0)
                .rotate(neck3, 0.4f, 0, 0)
                .rotate(head, 0.55f, 0, 0)
                .rotate(jaw, 0.8f, 0, 0)
                .endKeyframe();

        animator().startKeyframe(4)
                .rotate(neck1, 0.5f, 0, 0)
                .rotate(neck2, 0.25f, 0, 0)
                .rotate(neck3, 0.25f, 0, 0)
                .rotate(head, 0.35f, 0, 0)
                .endKeyframe();

        animator().resetKeyframe(6);
    }

    public void slapAttackAnimation()
    {
        animator().startKeyframe(7);
        keepSlapStance();
        animator().rotate(arm1R, -0.8f, 0.3f, 1f)
                .rotate(arm2R, 0.5f, 0, 0.5f)
                .rotate(body1, 0, 0, 0.3f)
                .rotate(head, 0, 0, -0.3f)
                .rotate(leg1R, -0.6f, 0, 0)
                .rotate(leg2R, 0.9f, 0, 0)
                .rotate(footR, 0.9f, 0, 0)
                .endKeyframe();

        animator().startKeyframe(3);
        keepSlapStance();
        animator().rotate(body1, 0, 0, -0.3f)
                .rotate(head, 0, 0, 0.3f)
                .rotate(leg1R, -0.1f, 0, 0)
                .rotate(leg2R, 0.3f, 0, 0)
                .rotate(leg3R, 0.3f, 0, 0)
                .endKeyframe();

        animator().startKeyframe(3);
        keepSlapStance();
        animator().rotate(arm1L, -0.8f, -0.3f, -1f)
                .rotate(arm2L, 0.5f, 0, -0.5f)
                .rotate(body1, 0, 0, -0.3f)
                .rotate(head, 0, 0, 0.3f)
                .rotate(leg1R, -0.1f, 0, 0)
                .rotate(leg2R, 0.3f, 0, 0)
                .rotate(leg3R, 0.3f, 0, 0)
                .endKeyframe();

        animator().startKeyframe(3);
        keepSlapStance();
        animator().rotate(arm2L, 0f, 0f, 0.5f)
                .rotate(body1, 0, 0, 0.3f)
                .rotate(head, 0, 0, -0.3f)
                .rotate(leg1R, -0.1f, 0, 0)
                .rotate(leg2R, 0.3f, 0, 0)
                .rotate(leg3R, 0.3f, 0, 0)
                .endKeyframe();

        animator().setStaticKeyframe(1)
                .resetKeyframe(10);
    }

    private void keepSlapStance()
    {
        ModelAnimator animator = animator(); // reduce method calls

        animator.rotate(body2, -0.5f, 0, 0)
                .move(body2, 0, -0.2f, -3f)
                .rotate(leg1L, 1.2f, 0, 0)
                .rotate(leg2L, -0.5f, 0, 0)
                .rotate(footL, -0.2f, 0, 0);

        for (WRModelRenderer tailPart : tailParts) animator.rotate(tailPart, 0.1f, 0, 0);
        for (WRModelRenderer headPart : headParts) animator.rotate(headPart, 0.4f, 0, 0);
    }
}
