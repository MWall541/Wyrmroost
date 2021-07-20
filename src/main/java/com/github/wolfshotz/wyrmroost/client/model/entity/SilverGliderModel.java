package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.SilverGliderEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * WRSilverGlider - Kingdomall
 * Created using Tabula 7.1.0
 */
public class SilverGliderModel extends DragonEntityModel<SilverGliderEntity>
{
    public static final ResourceLocation[] MALE_TEXTURES = new ResourceLocation[6]; // includes glow
    // Constant textures
    public static final ResourceLocation FEMALE = texture("female.png");
    public static final ResourceLocation FEMALE_GLOW = texture("female_glow.png");
    public static final ResourceLocation SPECIAL = texture("spe.png");
    public static final ResourceLocation SPECIAL_GLOW = texture("spe_glow.png");

    public WRModelRenderer neck1;
    public WRModelRenderer backFin;
    public WRModelRenderer bottomFin;
    public WRModelRenderer mainBody;
    public WRModelRenderer secondaryBody;
    public WRModelRenderer tail1;
    public WRModelRenderer legL1;
    public WRModelRenderer legR1;
    public WRModelRenderer tail2;
    public WRModelRenderer tail3;
    public WRModelRenderer tail4;
    public WRModelRenderer tail5;
    public WRModelRenderer tail6;
    public WRModelRenderer tail7;
    public WRModelRenderer tail8;
    public WRModelRenderer tail9;
    public WRModelRenderer tail10;
    public WRModelRenderer wing1R;
    public WRModelRenderer legL2;
    public WRModelRenderer legL3;
    public WRModelRenderer wing1L;
    public WRModelRenderer tailFin;
    public WRModelRenderer toeL2;
    public WRModelRenderer legR2;
    public WRModelRenderer legR3;
    public WRModelRenderer toeL3;
    public WRModelRenderer toeL1;
    public WRModelRenderer toeR2;
    public WRModelRenderer neck2;
    public WRModelRenderer neck3;
    public WRModelRenderer neck4;
    public WRModelRenderer head;
    public WRModelRenderer mouthBottom;
    public WRModelRenderer mouthTop;
    public WRModelRenderer eyeL;
    public WRModelRenderer eyeR;
    public WRModelRenderer toeR3;
    public WRModelRenderer toeR1;
    public WRModelRenderer wing2R;
    public WRModelRenderer wingMembraneR1;
    public WRModelRenderer wingMembraneElbowR;
    public WRModelRenderer phalangeR1;
    public WRModelRenderer wingMembraneR2;
    public WRModelRenderer phalangeR2;
    public WRModelRenderer wingMembraneR3;
    public WRModelRenderer phalangeSegmentR1;
    public WRModelRenderer phalangeR3;
    public WRModelRenderer wingMembraneR4;
    public WRModelRenderer wingMembraneR5;
    public WRModelRenderer phalangeSegmentR2;
    public WRModelRenderer wing2L;
    public WRModelRenderer wingMembraneL1;
    public WRModelRenderer wingMembraneElbowL;
    public WRModelRenderer phalangeL1;
    public WRModelRenderer wingMembraneL2;
    public WRModelRenderer phalangeL2;
    public WRModelRenderer wingMembraneL3;
    public WRModelRenderer phalangeSegmentL1;
    public WRModelRenderer phalangeL3;
    public WRModelRenderer wingMembraneL4;
    public WRModelRenderer wingMembraneL4_1;
    public WRModelRenderer phalangeSegmentL2;

    private final WRModelRenderer[] headArray;
    private final WRModelRenderer[] tailArray;
    private final WRModelRenderer[] toeArray;

    public SilverGliderModel()
    {
        texWidth = 160;
        texHeight = 125;
        toeL1 = new WRModelRenderer(this, 60, 20);
        toeL1.setPos(-0.5F, 3.0F, 0.2F);
        toeL1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toeL1, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        mouthTop = new WRModelRenderer(this, 67, 10);
        mouthTop.setPos(0.0F, -0.7F, -4.5F);
        mouthTop.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0.0F);
        setRotateAngle(mouthTop, 0.31869712141416456F, 0.0F, 0.0F);
        head = new WRModelRenderer(this, 80, 0);
        head.setPos(0.0F, -0.1F, -2.0F);
        head.addBox(-2.4F, -2.0F, -4.8F, 5, 4, 5, 0.0F);
        setRotateAngle(head, 0.1f, 0, 0);
        secondaryBody = new WRModelRenderer(this, 76, 40);
        secondaryBody.setPos(0.0F, -0.3F, 4.9F);
        secondaryBody.addBox(-2.1F, -2.1F, -0.5F, 4, 4, 8, 0.0F);
        toeL2 = new WRModelRenderer(this, 60, 20);
        toeL2.setPos(0.0F, 3.0F, 0.2F);
        toeL2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toeL2, -0.9560913642424937F, 0.0F, 0.0F);
        eyeL = new WRModelRenderer(this, 79, 2);
        eyeL.setPos(1.7F, -0.5F, -3.9F);
        eyeL.addBox(0.0F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        setRotateAngle(eyeL, 0.0F, 0.17453292519943295F, 0.0F);
        phalangeR1 = new WRModelRenderer(this, 0, 65);
        phalangeR1.setPos(-14.0F, 0.0F, -0.5F);
        phalangeR1.addBox(-15.0F, -0.3F, -0.5F, 15, 1, 1, 0.0F);
        setRotateAngle(phalangeR1, 0, 1f, 0);
        wingMembraneElbowL = new WRModelRenderer(this, 125, 65);
        wingMembraneElbowL.setPos(-9.3F, -0.37F, -0.1F);
        wingMembraneElbowL.addBox(-4.9F, -0.1F, -13.0F, 10, 0, 14, 0.0F);
        setRotateAngle(wingMembraneElbowL, 0, 0.5f, 0);
        wingMembraneR1 = new WRModelRenderer(this, -14, 83);
        wingMembraneR1.setPos(-5.2F, 0.0F, 0.0F);
        wingMembraneR1.addBox(-4.8F, -0.5F, -0.5F, 10, 0, 14, 0.0F);
        phalangeL2 = new WRModelRenderer(this, 33, 65);
        phalangeL2.setPos(-14.0F, 0.0F, 0.0F);
        phalangeL2.addBox(-25.0F, -0.5F, -0.5F, 25, 1, 1, 0.0F);
        setRotateAngle(phalangeL2, 0.0F, -0.0326405187521112f + -0.76f, -0.01443337511952157f);
        mouthBottom = new WRModelRenderer(this, 84, 10);
        mouthBottom.setPos(0.0F, 1.0F, -4.5F);
        mouthBottom.addBox(-1.5F, -0.5F, -4.0F, 3, 1, 4, 0.0F);
        neck1 = new WRModelRenderer(this, 88, 17);
        neck1.setPos(0.0F, 0.0F, -4.0F);
        neck1.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        wingMembraneElbowR = new WRModelRenderer(this, -13, 100);
        wingMembraneElbowR.setPos(-8.9F, -0.37F, -0.1F);
        wingMembraneElbowR.addBox(-4.9F, -0.1F, -0.5F, 10, 0, 14, 0.0F);
        setRotateAngle(wingMembraneElbowR, 0, -0.5f, 0);
        tail3 = new WRModelRenderer(this, 88, 53);
        tail3.setPos(0.02F, 0.02F, 2.8F);
        tail3.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        tail9 = new WRModelRenderer(this, 90, 60);
        tail9.setPos(-0.02F, -0.02F, 2.0F);
        tail9.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        tail1 = new WRModelRenderer(this, 88, 53);
        tail1.setPos(0.02F, 0.02F, 7.0F);
        tail1.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        wingMembraneL3 = new WRModelRenderer(this, 63, 98);
        wingMembraneL3.setPos(-8.2F, 0.6F, 0.0F);
        wingMembraneL3.addBox(-5.8F, -0.6F, -17.9F, 14, 0, 18, 0.0F);
        mainBody = new WRModelRenderer(this, 70, 24);
        mainBody.setPos(0.0F, 16.4F, 0.0F);
        mainBody.addBox(-2.5F, -2.5F, -5.0F, 5, 5, 10, 0.0F);
        wingMembraneL4 = new WRModelRenderer(this, 13, 99);
        wingMembraneL4.setPos(-12.3F, 0.7F, 0.0F);
        wingMembraneL4.addBox(-12.7F, -0.7F, -17.0F, 25, 0, 17, 0.0F);
        toeR1 = new WRModelRenderer(this, 60, 20);
        toeR1.setPos(-0.5F, 3.0F, 0.2F);
        toeR1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toeR1, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        toeR2 = new WRModelRenderer(this, 60, 20);
        toeR2.setPos(0.0F, 3.0F, 0.2F);
        toeR2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toeR2, -0.9560913642424937F, 0.0F, 0.0F);
        wing2L = new WRModelRenderer(this, 31, 58);
        wing2L.setPos(-10.0F, -0.49F, 0.0F);
        wing2L.addBox(-15.0F, -0.4F, -1.0F, 15, 2, 2, 0.0F);
        setRotateAngle(wing2L, 0, 0.8f, 0);
        tail8 = new WRModelRenderer(this, 90, 60);
        tail8.setPos(0.02F, 0.02F, 2.0F);
        tail8.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        legL1 = new WRModelRenderer(this, 56, 0);
        legL1.setPos(1.6F, 0.3F, 4.9F);
        legL1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        setRotateAngle(legL1, -0.7853981633974483F, 0.0F, 0.0F);
        phalangeL1 = new WRModelRenderer(this, 0, 65);
        phalangeL1.setPos(-14.0F, 0.0F, 0.4F);
        phalangeL1.addBox(-15.0F, -0.3F, -0.5F, 15, 1, 1, 0.0F);
        setRotateAngle(phalangeL1, 0, -1f, 0);
        legR2 = new WRModelRenderer(this, 56, 8);
        legR2.setPos(0.42F, 3.1F, -0.1F);
        legR2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        setRotateAngle(legR2, 1.360135086079181F, 0.0F, 0.0F);
        toeL3 = new WRModelRenderer(this, 60, 20);
        toeL3.setPos(0.5F, 3.0F, 0.2F);
        toeL3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toeL3, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        phalangeSegmentL1 = new WRModelRenderer(this, 0, 70);
        phalangeSegmentL1.setPos(-1.4F, 0.4F, -0.6F);
        phalangeSegmentL1.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(phalangeSegmentL1, 0.0F, -0.923977306005798F, 0.01308996938995747F);
        phalangeSegmentL2 = new WRModelRenderer(this, 33, 69);
        phalangeSegmentL2.setPos(-9.7F, 0.0F, 0.0F);
        phalangeSegmentL2.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(phalangeSegmentL2, 0.0F, -0.17453292519943295F, 0.0F);
        phalangeR2 = new WRModelRenderer(this, 33, 65);
        phalangeR2.setPos(-14.0F, 0.0F, 0.0F);
        phalangeR2.addBox(-25.0F, -0.5F, -0.5F, 25, 1, 1, 0.0F);
        setRotateAngle(phalangeR2, 0, 0.76f, 0);
        tail4 = new WRModelRenderer(this, 88, 53);
        tail4.setPos(-0.02F, -0.02F, 2.8F);
        tail4.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        wingMembraneL4_1 = new WRModelRenderer(this, 2, 115);
        wingMembraneL4_1.setPos(-3.8F, 0.5F, 0.0F);
        wingMembraneL4_1.addBox(-5.2F, -0.5F, -9.0F, 9, 0, 9, 0.0F);
        legL3 = new WRModelRenderer(this, 58, 14);
        legL3.setPos(0.02F, 2.5F, 0.1F);
        legL3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        setRotateAngle(legL3, -1.1647962782235626F, 0.0F, 0.0F);
        legR3 = new WRModelRenderer(this, 58, 14);
        legR3.setPos(-0.02F, 2.5F, 0.1F);
        legR3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        setRotateAngle(legR3, -1.3632766787327708F, 0.0F, 0.0F);
        legR1 = new WRModelRenderer(this, 56, 0);
        legR1.setPos(-2.3F, 0.4F, 4.9F);
        legR1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        setRotateAngle(legR1, -0.6729989595690135F, 0.0F, 0.0F);
        phalangeSegmentR1 = new WRModelRenderer(this, 0, 70);
        phalangeSegmentR1.setPos(-1.4F, 0.4F, 0.5F);
        phalangeSegmentR1.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(phalangeSegmentR1, 0.0F, 0.9546951008408984F, 0.01308996938995747F);
        neck4 = new WRModelRenderer(this, 88, 17);
        neck4.setPos(0.02F, 0.02F, -2.0F);
        neck4.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        setRotateAngle(neck4, 0.4f, 0, 0);
        phalangeR3 = new WRModelRenderer(this, 33, 65);
        phalangeR3.setPos(-25.0F, 0.0F, 0.0F);
        phalangeR3.addBox(-8.4F, -0.5F, -0.5F, 9, 1, 1, 0.0F);
        setRotateAngle(phalangeR3, 0, 0.3f, 0);
        wingMembraneL2 = new WRModelRenderer(this, 94, 98);
        wingMembraneL2.setPos(-7.8F, 0.0F, 0.0F);
        wingMembraneL2.addBox(-6.2F, 0.0F, -15.3F, 14, 0, 16, 0.0F);
        bottomFin = new WRModelRenderer(this, 0, -3);
        bottomFin.setPos(0.0F, 1.4F, -4.1F);
        bottomFin.addBox(0.0F, 0.2F, 0.0F, 0, 5, 12, 0.0F);
        tail10 = new WRModelRenderer(this, 90, 60);
        tail10.setPos(0.02F, 0.02F, 2.0F);
        tail10.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        tail7 = new WRModelRenderer(this, 90, 60);
        tail7.setPos(-0.02F, -0.02F, 2.0F);
        tail7.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        wing1R = new WRModelRenderer(this, 0, 58);
        wing1R.setPos(-1.5F, -1.0F, -3.0F);
        wing1R.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        setRotateAngle(wing1R, 0.7f, -0.2f, -0.1f);
        wingMembraneR3 = new WRModelRenderer(this, 32, 80);
        wingMembraneR3.setPos(-8.2F, 0.6F, 0.0F);
        wingMembraneR3.addBox(-5.8F, -0.6F, -0.1F, 14, 0, 18, 0.0F);
        backFin = new WRModelRenderer(this, 0, -14);
        backFin.setPos(0.0F, -2.4F, -4.1F);
        backFin.addBox(0.0F, -8.0F, -0.1F, 0, 8, 14, 0.0F);
        wingMembraneR5 = new WRModelRenderer(this, 121, 80);
        wingMembraneR5.setPos(-3.8F, 0.5F, 0.0F);
        wingMembraneR5.addBox(-5.2F, -0.5F, -0.2F, 9, 0, 9, 0.0F);
        neck2 = new WRModelRenderer(this, 88, 17);
        neck2.setPos(0.02F, 0.02F, -2.0F);
        neck2.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        setRotateAngle(neck2, -0.5f, 0, 0);
        phalangeSegmentR2 = new WRModelRenderer(this, 33, 69);
        phalangeSegmentR2.setPos(-9.7F, 0.0F, 0.0F);
        phalangeSegmentR2.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(phalangeSegmentR2, 0.0F, 0.17453292519943295F, 0.0F);
        legL2 = new WRModelRenderer(this, 56, 8);
        legL2.setPos(0.38F, 3.1F, -0.1F);
        legL2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        setRotateAngle(legL2, 1.360135086079181F, 0.0F, 0.0F);
        wingMembraneL1 = new WRModelRenderer(this, 125, 98);
        wingMembraneL1.setPos(-5.2F, 0.0F, 0.0F);
        wingMembraneL1.addBox(-4.8F, -0.5F, -13.4F, 10, 0, 14, 0.0F);
        tailFin = new WRModelRenderer(this, 18, 30);
        tailFin.setPos(0.0F, 0.0F, 1.6F);
        tailFin.addBox(-5.0F, 0.0F, -1.6F, 10, 0, 12, 0.0F);
        wingMembraneR2 = new WRModelRenderer(this, 5, 81);
        wingMembraneR2.setPos(-7.8F, 0.0F, 0.0F);
        wingMembraneR2.addBox(-6.2F, 0.0F, -0.5F, 14, 0, 16, 0.0F);
        phalangeL3 = new WRModelRenderer(this, 33, 65);
        phalangeL3.setPos(-25.0F, 0.0F, 0.0F);
        phalangeL3.addBox(-8.4F, -0.5F, -0.5F, 9, 1, 1, 0.0F);
        setRotateAngle(phalangeL3, 0, -0.3f, 0);
        neck3 = new WRModelRenderer(this, 88, 17);
        neck3.setPos(-0.02F, -0.02F, -2.0F);
        neck3.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        wingMembraneR4 = new WRModelRenderer(this, 62, 80);
        wingMembraneR4.setPos(-12.3F, 0.7F, 0.0F);
        wingMembraneR4.addBox(-12.7F, -0.7F, -0.1F, 25, 0, 17, 0.0F);
        eyeR = new WRModelRenderer(this, 79, 2);
        eyeR.setPos(-1.8F, -0.5F, -3.9F);
        eyeR.addBox(-0.7F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        setRotateAngle(eyeR, 0.0F, -0.17453292519943295F, 0.0F);
        tail5 = new WRModelRenderer(this, 90, 60);
        tail5.setPos(-0.1F, -0.1F, 2.0F);
        tail5.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        tail6 = new WRModelRenderer(this, 90, 60);
        tail6.setPos(0.02F, 0.02F, 2.0F);
        tail6.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        toeR3 = new WRModelRenderer(this, 60, 20);
        toeR3.setPos(0.5F, 3.0F, 0.2F);
        toeR3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toeR3, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        wing2R = new WRModelRenderer(this, 31, 58);
        wing2R.setPos(-10.0F, -0.49F, 0.0F);
        wing2R.addBox(-15.0F, -0.5F, -1.0F, 15, 2, 2, 0.0F);
        setRotateAngle(wing2R, 0, -0.8f, 0);
        wing1L = new WRModelRenderer(this, 0, 58);
        wing1L.setPos(1.5F, -1.0F, -3.0F);
        wing1L.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        setRotateAngle(wing1L, -0.7f, -3.141592653589793f + 0.2f, 0.1f);
        tail2 = new WRModelRenderer(this, 88, 53);
        tail2.setPos(-0.02F, -0.02F, 2.8F);
        tail2.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        legL3.addChild(toeL1);
        head.addChild(mouthTop);
        neck4.addChild(head);
        mainBody.addChild(secondaryBody);
        legL3.addChild(toeL2);
        head.addChild(eyeL);
        wing2R.addChild(phalangeR1);
        wing1L.addChild(wingMembraneElbowL);
        wing1R.addChild(wingMembraneR1);
        phalangeL1.addChild(phalangeL2);
        head.addChild(mouthBottom);
        mainBody.addChild(neck1);
        wing1R.addChild(wingMembraneElbowR);
        tail2.addChild(tail3);
        tail8.addChild(tail9);
        secondaryBody.addChild(tail1);
        phalangeL1.addChild(wingMembraneL3);
        phalangeL2.addChild(wingMembraneL4);
        legR3.addChild(toeR1);
        legR3.addChild(toeR2);
        wing1L.addChild(wing2L);
        tail7.addChild(tail8);
        secondaryBody.addChild(legL1);
        wing2L.addChild(phalangeL1);
        legR1.addChild(legR2);
        legL3.addChild(toeL3);
        phalangeL1.addChild(phalangeSegmentL1);
        phalangeSegmentL1.addChild(phalangeSegmentL2);
        phalangeR1.addChild(phalangeR2);
        tail3.addChild(tail4);
        phalangeL3.addChild(wingMembraneL4_1);
        legL2.addChild(legL3);
        legR2.addChild(legR3);
        secondaryBody.addChild(legR1);
        phalangeR1.addChild(phalangeSegmentR1);
        neck3.addChild(neck4);
        phalangeR2.addChild(phalangeR3);
        wing2L.addChild(wingMembraneL2);
        mainBody.addChild(bottomFin);
        tail9.addChild(tail10);
        tail6.addChild(tail7);
        mainBody.addChild(wing1R);
        phalangeR1.addChild(wingMembraneR3);
        mainBody.addChild(backFin);
        phalangeR3.addChild(wingMembraneR5);
        neck1.addChild(neck2);
        phalangeSegmentR1.addChild(phalangeSegmentR2);
        legL1.addChild(legL2);
        wing1L.addChild(wingMembraneL1);
        tail10.addChild(tailFin);
        wing2R.addChild(wingMembraneR2);
        phalangeL2.addChild(phalangeL3);
        neck2.addChild(neck3);
        phalangeR2.addChild(wingMembraneR4);
        head.addChild(eyeR);
        tail4.addChild(tail5);
        tail5.addChild(tail6);
        legR3.addChild(toeR3);
        wing1R.addChild(wing2R);
        mainBody.addChild(wing1L);
        tail1.addChild(tail2);

        headArray = new WRModelRenderer[] {neck1, neck2, neck3, neck4, head};
        tailArray = new WRModelRenderer[] {tail1, tail2, tail3, tail4, tail5, tail6, tail7, tail8, tail9, tail10};
        toeArray = new WRModelRenderer[] {toeR1, toeR2, toeR3, toeL1, toeL2, toeL3};

        wingMembraneR2.xRot = -0.0001f;
        wingMembraneR3.xRot = 0.0001f;
        wingMembraneL2.xRot = -0.0001f;
        wingMembraneL3.xRot = 0.0001f;

        setDefaultPose();
    }

    @Override
    public ResourceLocation getTexture(SilverGliderEntity entity)
    {
        if (entity.getVariant() == -1) return SPECIAL;
        if (!entity.isMale()) return FEMALE;
        int index = entity.getVariant();
        if (MALE_TEXTURES[index] == null) return MALE_TEXTURES[index] = texture("male_" + index + ".png");
        return MALE_TEXTURES[index];
    }

    public ResourceLocation getGlowTexture(SilverGliderEntity entity)
    {
        if (entity.getVariant() == -1) return SPECIAL_GLOW;
        int index = entity.getVariant() + 3;
        if (MALE_TEXTURES[index] == null)
            return MALE_TEXTURES[index] = texture("male_" + entity.getVariant() + "_glow.png");
        return MALE_TEXTURES[index];
    }

    @Override
    public float getShadowRadius(SilverGliderEntity entity)
    {
        return 1f;
    }

    @Override
    public void renderToBuffer(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        mainBody.render(ms, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void postProcess(SilverGliderEntity entity, MatrixStack ms, IRenderTypeBuffer buffer, int light, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float partialTicks)
    {
        if (entity.isMale()) renderGlowOverlay(getGlowTexture(entity), ms, buffer);
    }

    @Override
    public void setupAnim(SilverGliderEntity entityIn, float limbSwing, float limbSwingAmount, float bob, float netHeadYaw, float headPitch)
    {
        reset();

        flight(entity.flightTimer.get(partialTicks));
        sleep(entity.sleepTimer.get(partialTicks));
        sit(entity.sitTimer.get(partialTicks));

        if (entity.isFlying() || entity.isGliding())
        {
            flap(wing1L, globalSpeed - 0.2f, 0.05f, false, 0, 0, bob, 0.5f);
            walk(wing1L, globalSpeed + 0.5f, 0.09f, false, 0, 0, bob, 0.5f);
            flap(wing1R, globalSpeed - 0.2f, 0.05f, true, 0, 0, bob, 0.5f);
            walk(wing1R, globalSpeed + 0.5f, 0.09f, true, 0, 0, bob, 0.5f);
        }
        else if (!entity.isInSittingPose() && !entity.isSleeping())
        {
            limbSwingAmount = Math.min(limbSwingAmount, 0.6f);

            walk(legR1, globalSpeed - 0.15f, 1.5f, false, 0, 0.75f, limbSwing, limbSwingAmount);
            walk(legR2, globalSpeed - 0.15f, 1f, false, -0.7f, 0.3f, limbSwing, limbSwingAmount);
            walk(legR3, globalSpeed - 0.15f, -1f, false, -0.7f, -0.5f, limbSwing, limbSwingAmount);

            walk(legL1, globalSpeed - 0.15f, 1.5f, true, 0, -0.75f, limbSwing, limbSwingAmount);
            walk(legL2, globalSpeed - 0.15f, 1f, true, -0.7f, -0.3f, limbSwing, limbSwingAmount);
            walk(legL3, globalSpeed - 0.15f, -1f, true, -0.7f, 0.5f, limbSwing, limbSwingAmount);

            for (int i = 0; i < toeArray.length; i++)
            {
                boolean invert = i > 2;
                walk(toeArray[i], globalSpeed - 0.15f, 1f, false, -1f, invert? -0.8f : 0.8f, limbSwing, limbSwingAmount);
            }

            swing(wing1L, globalSpeed - 0.15f, 1, false, 0, 0, limbSwing, limbSwingAmount);
            walk(wing1L, globalSpeed - 0.15f, 0.5f, false, 2f, 0.25f,limbSwing, limbSwingAmount);
            swing(wing2L, globalSpeed - 0.15f, 0.25f, false, 1f, 0.25f, limbSwing, limbSwingAmount);

            swing(wing1R, globalSpeed - 0.15f, 1, false, 0, 0, limbSwing, limbSwingAmount);
            walk(wing1R, globalSpeed - 0.15f, 0.5f, false, 2f, -0.25f,limbSwing, limbSwingAmount);
            swing(wing2R, globalSpeed - 0.15f, 0.25f, false, 1f, -0.25f, limbSwing, limbSwingAmount);
        }

        idle(bob);

        if (netHeadYaw < -180) netHeadYaw += 360;
        else if (netHeadYaw > 180) netHeadYaw -= 360;
        if (entity.flightTimer.get() == 1)
        {
            mainBody.zRot = -(netHeadYaw * Mafs.PI / 180f) * 0.5f;
            mainBody.xRot = headPitch * Mafs.PI / 180f;

            float foldAmount = Math.max(headPitch * Mafs.PI / 180f, 0) * 0.75f;
            wing1R.yRot += foldAmount;
            wing2R.yRot += -foldAmount * 1.8;
            phalangeR1.yRot += foldAmount * 2;
            phalangeR2.yRot += foldAmount;
            phalangeR3.yRot += foldAmount;
            wingMembraneR2.x += foldAmount * 4;
            wingMembraneR2.yRot += foldAmount;

            wing1L.yRot -= foldAmount;
            wing2L.yRot -= -foldAmount * 1.8;
            phalangeL1.yRot -= foldAmount * 2;
            phalangeL2.yRot -= foldAmount;
            phalangeL3.yRot -= foldAmount;
            wingMembraneL2.x += foldAmount * 4;
            wingMembraneL2.yRot -= foldAmount;
        }
        else faceTarget(netHeadYaw, entity.isFlying()? 0 : headPitch, 1, headArray);
    }

    public void idle(float frame)
    {
        if (entity.isSleeping()) globalSpeed -= 0.2f;

        chainWave(headArray, globalSpeed * 0.2f, 0.009f, -1, frame, 1);
        chainWave(tailArray, globalSpeed * 0.1f, 0.03f, -1, frame, 1);
        chainSwing(tailArray, globalSpeed * 0.07f, 0.01f, -1, frame, 1);
    }

    public void sit(float amount)
    {
        setTime(amount);

        move(mainBody, 0, 4, 0);

        rotate(wing1R, -0.5f, 0, 0);
        rotate(wing1L, 0.5f, 0, 0);

        rotate(legR1, -0.6f, 0, 0);
        rotate(legR2, 0.95f, 0, 0);
        rotate(legR3, -1.15f, 0, 0);

        rotate(legL1, -0.6f, 0, 0);
        rotate(legL2, 0.95f, 0, 0);
        rotate(legL3, -1.15f, 0, 0);

        for (WRModelRenderer segment : tailArray) rotate(segment, -0.035f, -0.35f, 0);

    }

    public void sleep(float amount)
    {
        setTime(amount);

        rotate(neck2, 0.5f, 0, 0);
        rotate(neck4, -0.4f, 0, 0);
        rotate(head, -0.1f, 0, 0);
        for (WRModelRenderer part : headArray) rotate(part, 0.05f, 0.5f, 0);

        rotate(wing1R, 0, 0.2f, 0.2f);
        rotate(wing2R, 0, 0, -0.2f);
        rotate(wingMembraneElbowR, 0, 0, -0.1f);
    }

    public void flight(float amount)
    {
        setTime(amount);

        rotate(neck2, 0.5f, 0, 0);
        rotate(neck4, -0.4f, 0, 0);
        rotate(head, -0.1f, 0, 0);

        rotate(wing1R, -0.7f, 0.5f, 0.2f);
        rotate(wing2R, 0, 0.3f, -0.3f);
        rotate(phalangeR1, 0, -0.8f, 0.1f);
        rotate(phalangeR2, 0, -0.56f, 0.1f);
        rotate(phalangeR3, 0, -0.3f, 0.2f);
        rotate(wingMembraneElbowR, 0, 0.5f, 0);

        rotate(wing1L, 0.7f, -0.5f, -0.2f);
        rotate(wing2L, 0, -0.3f, -0.3f);
        rotate(phalangeL1, 0, 0.8f, 0.1f);
        rotate(phalangeL2, 0, 0.56f, 0.1f);
        rotate(phalangeL3, 0, 0.3f, 0.2f);
        rotate(wingMembraneElbowR, 0, -0.5f, 0);

        if (!entity.isGliding())
        {
            rotate(legR1, -0.6f, 0, 0);
            rotate(legR2, 1f, 0, 0);
            rotate(legR3, -0.75f, 0, 0);

            rotate(legL1, -0.6f, 0, 0);
            rotate(legL2, 1f, 0, 0);
            rotate(legL3, -0.75f, 0, 0);

            for (WRModelRenderer toe : toeArray) rotate(toe, 1f, 0, 0);
        }
    }

    public static ResourceLocation texture(String png)
    {
        return Wyrmroost.id(FOLDER + "silver_glider/" + png);
    }
}
