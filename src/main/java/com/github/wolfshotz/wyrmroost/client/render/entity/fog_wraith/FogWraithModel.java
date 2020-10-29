package com.github.wolfshotz.wyrmroost.client.render.entity.fog_wraith;

import com.github.wolfshotz.wyrmroost.client.model.ModelAnimator;
import com.github.wolfshotz.wyrmroost.client.model.WREntityModel;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.FogWraithEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * WRFogWraith - Ukan
 * Created using Tabula 8.0.0
 */
public class FogWraithModel extends WREntityModel<FogWraithEntity>
{
    public final WRModelRenderer[][] tails;
    public final WRModelRenderer[] headArray;
    public WRModelRenderer body2;
    public WRModelRenderer body1;
    public WRModelRenderer body3;
    public WRModelRenderer neck1;
    public WRModelRenderer wingA1L;
    public WRModelRenderer wingB1L;
    public WRModelRenderer wingA1R;
    public WRModelRenderer wingB1R;
    public WRModelRenderer neck2;
    public WRModelRenderer neck3;
    public WRModelRenderer neck4;
    public WRModelRenderer neck5;
    public WRModelRenderer neck6;
    public WRModelRenderer neck7;
    public WRModelRenderer head;
    public WRModelRenderer jaw;
    public WRModelRenderer teeth1;
    public WRModelRenderer tongue1;
    public WRModelRenderer teeth2;
    public WRModelRenderer tongue2;
    public WRModelRenderer wingA2L;
    public WRModelRenderer membraneA6L;
    public WRModelRenderer membraneA5L;
    public WRModelRenderer wingA3L;
    public WRModelRenderer membraneA4L;
    public WRModelRenderer fingerA1_1L;
    public WRModelRenderer membraneA3L;
    public WRModelRenderer fingerA1_2L;
    public WRModelRenderer membraneA2L;
    public WRModelRenderer membraneA1L;
    public WRModelRenderer wingB2L;
    public WRModelRenderer membraneB6L;
    public WRModelRenderer membraneB5L;
    public WRModelRenderer wingB3L;
    public WRModelRenderer membraneB4L;
    public WRModelRenderer fingerB1_1L;
    public WRModelRenderer membraneB3L;
    public WRModelRenderer fingerB1_2L;
    public WRModelRenderer membraneB2L;
    public WRModelRenderer membraneB1L;
    public WRModelRenderer wingA2R;
    public WRModelRenderer membraneA6R;
    public WRModelRenderer membraneA5R;
    public WRModelRenderer wingA3R;
    public WRModelRenderer membraneA4R;
    public WRModelRenderer fingerA1_1R;
    public WRModelRenderer membraneA3R;
    public WRModelRenderer fingerA1_2R;
    public WRModelRenderer membraneA2R;
    public WRModelRenderer membraneA1R;
    public WRModelRenderer wingB2R;
    public WRModelRenderer membraneB6R;
    public WRModelRenderer membraneB5R;
    public WRModelRenderer wingB3R;
    public WRModelRenderer membraneB4R;
    public WRModelRenderer fingerB1_1R;
    public WRModelRenderer membraneB3R;
    public WRModelRenderer fingerB1_2R;
    public WRModelRenderer membraneB2R;
    public WRModelRenderer membraneB1R;
    public WRModelRenderer tailL1_1;
    public WRModelRenderer tailL1_2;
    public WRModelRenderer tailL1_3;
    public WRModelRenderer tailL1_4;
    public WRModelRenderer wingC1L;
    public WRModelRenderer wingC1R;
    public WRModelRenderer tailL2_1;
    public WRModelRenderer tailL3_1;
    public WRModelRenderer tailL4_1;
    public WRModelRenderer tailL5_1;
    public WRModelRenderer tailL6_1;
    public WRModelRenderer tailL7_1;
    public WRModelRenderer tailL8_1;
    public WRModelRenderer tailL9_1;
    public WRModelRenderer tailL10_1;
    public WRModelRenderer tailL11_1;
    public WRModelRenderer tailL12_1;
    public WRModelRenderer tailL2_2;
    public WRModelRenderer tailL3_2;
    public WRModelRenderer tailL4_2;
    public WRModelRenderer tailL5_2;
    public WRModelRenderer tailL6_2;
    public WRModelRenderer tailL7_2;
    public WRModelRenderer tailL8_2;
    public WRModelRenderer tailL9_2;
    public WRModelRenderer tailL10_2;
    public WRModelRenderer tailL11_2;
    public WRModelRenderer tailL12_2;
    public WRModelRenderer tailL2_3;
    public WRModelRenderer tailL3_3;
    public WRModelRenderer tailL4_3;
    public WRModelRenderer tailL5_3;
    public WRModelRenderer tailL6_3;
    public WRModelRenderer tailL7_3;
    public WRModelRenderer tailL8_3;
    public WRModelRenderer tailL9_3;
    public WRModelRenderer tailL10_3;
    public WRModelRenderer tailL11_3;
    public WRModelRenderer tailL12_3;
    public WRModelRenderer tailL2_4;
    public WRModelRenderer tailL3_4;
    public WRModelRenderer tailL4_4;
    public WRModelRenderer tailL5_4;
    public WRModelRenderer tailL6_4;
    public WRModelRenderer tailL7_4;
    public WRModelRenderer tailL8_4;
    public WRModelRenderer tailL9_4;
    public WRModelRenderer tailL10_4;
    public WRModelRenderer tailL11_4;
    public WRModelRenderer tailL12_4;
    public WRModelRenderer wingC2L;
    public WRModelRenderer membraneC6L;
    public WRModelRenderer membraneC5L;
    public WRModelRenderer wingC3L;
    public WRModelRenderer membraneC4L;
    public WRModelRenderer fingerC1_1L;
    public WRModelRenderer membraneC3L;
    public WRModelRenderer fingerC1_2L;
    public WRModelRenderer membraneC2L;
    public WRModelRenderer membraneC1L;
    public WRModelRenderer wingC2R;
    public WRModelRenderer membraneC6R;
    public WRModelRenderer membraneC5R;
    public WRModelRenderer wingC3R;
    public WRModelRenderer membraneC4R;
    public WRModelRenderer fingerC1_1R;
    public WRModelRenderer membraneC3R;
    public WRModelRenderer fingerC1_2R;
    public WRModelRenderer membraneC2R;
    public WRModelRenderer membraneC1R;
    public ModelAnimator animator;

    public FogWraithModel()
    {
        super(RenderType::getEntityCutout);

        textureWidth = 300;
        textureHeight = 150;
        membraneA3L = new WRModelRenderer(this, 120, 30);
        membraneA3L.mirror = true;
        membraneA3L.setRotationPoint(7.5F, 0.01F, 1.0F);
        membraneA3L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        membraneA3R = new WRModelRenderer(this, 120, 30);
        membraneA3R.setRotationPoint(-7.5F, 0.01F, 1.0F);
        membraneA3R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        membraneB3R = new WRModelRenderer(this, 120, 30);
        membraneB3R.setRotationPoint(-7.5F, 0.01F, 1.0F);
        membraneB3R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        neck1 = new WRModelRenderer(this, 0, 21);
        neck1.setRotationPoint(0.0F, -0.5F, -8.0F);
        neck1.addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(neck1, -0.5544911113480817F, 0.0F, 0.0F);
        fingerA1_2R = new WRModelRenderer(this, 70, 20);
        fingerA1_2R.mirror = true;
        fingerA1_2R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        fingerA1_2R.addBox(-40.0F, -0.5F, -1.0F, 40.0F, 1.0F, 2.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(fingerA1_2R, 0.0F, 0.11728612207217244F, 0.0F);
        tailL9_1 = new WRModelRenderer(this, 145, 0);
        tailL9_1.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL9_1.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        tailL1_2 = new WRModelRenderer(this, 109, 0);
        tailL1_2.setRotationPoint(-1.5F, -2.01F, 7.0F);
        tailL1_2.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        membraneC5R = new WRModelRenderer(this, 55, 80);
        membraneC5R.setRotationPoint(-10.0F, 0.03F, 1.0F);
        membraneC5R.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneC5R, 0.0F, -0.23457224414434488F, 0.0F);
        tailL4_2 = new WRModelRenderer(this, 109, 0);
        tailL4_2.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL4_2.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        wingA3L = new WRModelRenderer(this, 30, 33);
        wingA3L.setRotationPoint(14.0F, 0.0F, 0.0F);
        wingA3L.addBox(0.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.02F, -0.02F);
        setRotateAngle(wingA3L, 0.0F, -2.5830872929516078F, 0.0F);
        fingerB1_2L = new WRModelRenderer(this, 75, 52);
        fingerB1_2L.setRotationPoint(14.0F, 0.0F, 0.0F);
        fingerB1_2L.addBox(0.0F, -0.5F, -1.0F, 50.0F, 1.0F, 2.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(fingerB1_2L, 0.0F, -1.5414747900350696F, 0.0F);
        membraneA4L = new WRModelRenderer(this, 170, 30);
        membraneA4L.mirror = true;
        membraneA4L.setRotationPoint(7.5F, 0.02F, 1.0F);
        membraneA4L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneA4L, 0.0F, -1.9268435208333508F, 0.0F);
        tailL6_4 = new WRModelRenderer(this, 128, 0);
        tailL6_4.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL6_4.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        wingA3R = new WRModelRenderer(this, 30, 33);
        wingA3R.mirror = true;
        wingA3R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        wingA3R.addBox(-15.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.02F, -0.02F);
        setRotateAngle(wingA3R, 0.0F, 2.5830872929516078F, 0.0F);
        tailL9_2 = new WRModelRenderer(this, 145, 0);
        tailL9_2.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL9_2.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        tailL2_3 = new WRModelRenderer(this, 109, 0);
        tailL2_3.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL2_3.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        tailL10_4 = new WRModelRenderer(this, 145, 0);
        tailL10_4.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL10_4.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        neck2 = new WRModelRenderer(this, 0, 35);
        neck2.setRotationPoint(0.0F, 0.0F, -3.5F);
        neck2.addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(neck2, -0.37734019892856F, 0.0F, 0.0F);
        tailL2_4 = new WRModelRenderer(this, 109, 0);
        tailL2_4.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL2_4.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        wingB3R = new WRModelRenderer(this, 30, 33);
        wingB3R.mirror = true;
        wingB3R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        wingB3R.addBox(-15.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.02F, -0.02F);
        setRotateAngle(wingB3R, 0.0F, 0.6143559100178094F, 0.0F);
        tailL9_3 = new WRModelRenderer(this, 145, 0);
        tailL9_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL9_3.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        membraneC1L = new WRModelRenderer(this, 170, 85);
        membraneC1L.setRotationPoint(1.0F, 0.0F, 1.0F);
        membraneC1L.addBox(0.0F, 0.0F, 0.0F, 35.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        fingerC1_2R = new WRModelRenderer(this, 70, 70);
        fingerC1_2R.mirror = true;
        fingerC1_2R.setRotationPoint(-9.0F, 0.0F, 0.0F);
        fingerC1_2R.addBox(-35.0F, -0.5F, -1.0F, 35.0F, 1.0F, 2.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(fingerC1_2R, 0.0F, 1.4995868613555947F, 0.0F);
        tongue1 = new WRModelRenderer(this, 9, 116);
        tongue1.setRotationPoint(0.0F, -0.5F, -5.5F);
        tongue1.addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(tongue1, -3.141592653589793F, 0.0F, 0.0F);
        fingerA1_2L = new WRModelRenderer(this, 70, 20);
        fingerA1_2L.setRotationPoint(14.0F, 0.0F, 0.0F);
        fingerA1_2L.addBox(0.0F, -0.5F, -1.0F, 40.0F, 1.0F, 2.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(fingerA1_2L, 0.0F, -0.11728612207217244F, 0.0F);
        membraneC4L = new WRModelRenderer(this, 170, 110);
        membraneC4L.mirror = true;
        membraneC4L.setRotationPoint(5.0F, 0.02F, 1.0F);
        membraneC4L.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        tailL10_1 = new WRModelRenderer(this, 145, 0);
        tailL10_1.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL10_1.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        tailL2_2 = new WRModelRenderer(this, 109, 0);
        tailL2_2.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL2_2.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        membraneB3L = new WRModelRenderer(this, 120, 30);
        membraneB3L.mirror = true;
        membraneB3L.setRotationPoint(7.5F, 0.01F, 1.0F);
        membraneB3L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        fingerC1_1R = new WRModelRenderer(this, 70, 70);
        fingerC1_1R.setRotationPoint(-9.0F, 0.0F, 0.0F);
        fingerC1_1R.addBox(-10.0F, -0.5F, -1.0F, 10.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(fingerC1_1R, 0.0F, -0.011344639721844222F, 0.0F);
        membraneB4L = new WRModelRenderer(this, 170, 30);
        membraneB4L.mirror = true;
        membraneB4L.setRotationPoint(7.5F, 0.02F, 1.0F);
        membraneB4L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL12_2 = new WRModelRenderer(this, 145, 0);
        tailL12_2.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL12_2.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        tailL5_1 = new WRModelRenderer(this, 128, 0);
        tailL5_1.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL5_1.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        body2 = new WRModelRenderer(this, 38, 0);
        body2.setRotationPoint(0.0F, -14.5F, 0.0F);
        body2.addBox(-4.0F, -4.0F, -4.5F, 8.0F, 7.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(body2, -0.25132740562928074F, 0.0F, 0.0F);
        wingC1R = new WRModelRenderer(this, 30, 70);
        wingC1R.mirror = true;
        wingC1R.setRotationPoint(-2.5F, 1.0F, 4.0F);
        wingC1R.addBox(-10.0F, -1.0F, -1.5F, 10.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wingC1R, 0.20943951023931953F, 0.49148471469844207F, -1.0890854798760725F);
        tailL10_2 = new WRModelRenderer(this, 145, 0);
        tailL10_2.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL10_2.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        wingA2L = new WRModelRenderer(this, 30, 26);
        wingA2L.setRotationPoint(14.0F, 0.0F, 0.0F);
        wingA2L.addBox(0.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(wingA2L, 0.0F, 2.1921335138732667F, 0.0F);
        tailL11_3 = new WRModelRenderer(this, 145, 0);
        tailL11_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL11_3.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        neck4 = new WRModelRenderer(this, 0, 46);
        neck4.setRotationPoint(0.0F, 0.0F, -4.0F);
        neck4.addBox(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 5.0F, 0.2F, 0.2F, 0.0F);
        setRotateAngle(neck4, -0.2275909337942703F, 0.0F, 0.0F);
        membraneC2L = new WRModelRenderer(this, 70, 110);
        membraneC2L.setRotationPoint(0.0F, -0.01F, 1.0F);
        membraneC2L.addBox(0.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        wingC3L = new WRModelRenderer(this, 30, 83);
        wingC3L.setRotationPoint(9.0F, 0.0F, 0.0F);
        wingC3L.addBox(0.0F, -1.0F, -1.5F, 10.0F, 2.0F, 3.0F, 0.0F, -0.02F, -0.02F);
        setRotateAngle(wingC3L, 0.0F, -0.19547687289441354F, 0.0F);
        membraneA1L = new WRModelRenderer(this, 170, 5);
        membraneA1L.setRotationPoint(1.0F, 0.0F, 1.0F);
        membraneA1L.addBox(0.0F, 0.0F, 0.0F, 40.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL4_3 = new WRModelRenderer(this, 109, 0);
        tailL4_3.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL4_3.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        neck5 = new WRModelRenderer(this, 0, 46);
        neck5.setRotationPoint(-0.0F, -0.0F, -4.0F);
        neck5.addBox(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 5.0F, 0.1F, 0.1F, 0.0F);
        setRotateAngle(neck5, 0.5227261242881072F, 0.0F, 0.0F);
        head = new WRModelRenderer(this, 0, 81);
        head.setRotationPoint(0.0F, -0.01F, -3.7F);
        head.addBox(-2.0F, -2.0F, -8.0F, 4.0F, 4.0F, 8.0F, -0.2F, -0.2F, -0.2F);
        setRotateAngle(head, 0.18116517835438223F, 0.0F, 0.0F);
        tailL6_3 = new WRModelRenderer(this, 128, 0);
        tailL6_3.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL6_3.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        membraneA5R = new WRModelRenderer(this, 25, 50);
        membraneA5R.setRotationPoint(-15.0F, 0.03F, 1.0F);
        membraneA5R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneA5R, 0.0F, -0.4858996664183824F, 0.0F);
        neck7 = new WRModelRenderer(this, 0, 69);
        neck7.setRotationPoint(-0.0F, -0.0F, -4.0F);
        neck7.addBox(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 5.0F, -0.1F, -0.1F, 0.0F);
        setRotateAngle(neck7, 0.5714208263977155F, 0.0F, 0.0F);
        tongue2 = new WRModelRenderer(this, 9, 116);
        tongue2.setRotationPoint(0.01F, 0.01F, -3.7F);
        tongue2.addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, -0.1F, -0.1F, 0.0F);
        setRotateAngle(tongue2, -3.141592653589793F, 0.0F, 0.0F);
        membraneA5L = new WRModelRenderer(this, 25, 50);
        membraneA5L.mirror = true;
        membraneA5L.setRotationPoint(15.0F, 0.03F, 1.0F);
        membraneA5L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneA5L, 0.0F, 0.4858996664183824F, 0.0F);
        membraneB5R = new WRModelRenderer(this, 25, 50);
        membraneB5R.setRotationPoint(-15.0F, 0.03F, 1.0F);
        membraneB5R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneB5R, 0.0F, 0.0F, -0.25132740562928074F);
        membraneC5L = new WRModelRenderer(this, 55, 80);
        membraneC5L.mirror = true;
        membraneC5L.setRotationPoint(10.0F, 0.03F, 1.0F);
        membraneC5L.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneC5L, 0.0F, 0.23457224414434488F, 0.0F);
        membraneC3L = new WRModelRenderer(this, 120, 110);
        membraneC3L.mirror = true;
        membraneC3L.setRotationPoint(5.0F, 0.01F, 1.0F);
        membraneC3L.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        membraneA6L = new WRModelRenderer(this, 220, 30);
        membraneA6L.mirror = true;
        membraneA6L.setRotationPoint(7.5F, 0.04F, 1.0F);
        membraneA6L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneA6L, 0.0F, 1.298524990115392F, 0.0F);
        tailL7_3 = new WRModelRenderer(this, 128, 0);
        tailL7_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL7_3.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        membraneC3R = new WRModelRenderer(this, 120, 110);
        membraneC3R.setRotationPoint(-5.0F, 0.01F, 1.0F);
        membraneC3R.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        fingerB1_1R = new WRModelRenderer(this, 70, 20);
        fingerB1_1R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        fingerB1_1R.addBox(-15.0F, -0.5F, -1.0F, 15.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(fingerB1_1R, 0.0F, 1.3711306177561677F, 0.0F);
        membraneA1R = new WRModelRenderer(this, 170, 5);
        membraneA1R.mirror = true;
        membraneA1R.setRotationPoint(1.0F, 0.0F, 1.0F);
        membraneA1R.addBox(-40.0F, 0.0F, 0.0F, 40.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        membraneC6L = new WRModelRenderer(this, 220, 105);
        membraneC6L.mirror = true;
        membraneC6L.setRotationPoint(5.0F, 0.04F, 1.0F);
        membraneC6L.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        tailL11_4 = new WRModelRenderer(this, 145, 0);
        tailL11_4.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL11_4.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        tailL7_4 = new WRModelRenderer(this, 128, 0);
        tailL7_4.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL7_4.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        membraneB1R = new WRModelRenderer(this, 170, 65);
        membraneB1R.mirror = true;
        membraneB1R.setRotationPoint(-1.0F, 0.0F, 1.0F);
        membraneB1R.addBox(-50.0F, 0.0F, 0.0F, 50.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL9_4 = new WRModelRenderer(this, 145, 0);
        tailL9_4.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL9_4.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        membraneB6L = new WRModelRenderer(this, 220, 30);
        membraneB6L.mirror = true;
        membraneB6L.setRotationPoint(7.5F, 0.04F, 1.0F);
        membraneB6L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL12_3 = new WRModelRenderer(this, 145, 0);
        tailL12_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL12_3.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        wingA1R = new WRModelRenderer(this, 30, 20);
        wingA1R.mirror = true;
        wingA1R.setRotationPoint(-3.0F, -1.0F, -7.0F);
        wingA1R.addBox(-15.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wingA1R, 0.6702064593974334F, 0.7119198178674535F, -0.41887902047863906F);
        tailL5_2 = new WRModelRenderer(this, 128, 0);
        tailL5_2.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL5_2.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        membraneB5L = new WRModelRenderer(this, 25, 50);
        membraneB5L.mirror = true;
        membraneB5L.setRotationPoint(15.0F, 0.03F, 1.0F);
        membraneB5L.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneB5L, 0.0F, 0.0F, 0.25132740562928074F);
        neck3 = new WRModelRenderer(this, 0, 35);
        neck3.setRotationPoint(-0.0F, -0.0F, -3.5F);
        neck3.addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F, -0.1F, -0.1F, 0.0F);
        setRotateAngle(neck3, -0.28763025273918225F, 0.0F, 0.0F);
        membraneC6R = new WRModelRenderer(this, 220, 105);
        membraneC6R.setRotationPoint(-5.0F, 0.04F, 1.0F);
        membraneC6R.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        neck6 = new WRModelRenderer(this, 0, 57);
        neck6.setRotationPoint(0.0F, 0.0F, -4.0F);
        neck6.addBox(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(neck6, 0.417133684568742F, 0.0F, 0.0F);
        tailL12_1 = new WRModelRenderer(this, 145, 0);
        tailL12_1.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL12_1.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        membraneC2R = new WRModelRenderer(this, 70, 110);
        membraneC2R.mirror = true;
        membraneC2R.setRotationPoint(0.0F, -0.01F, 1.0F);
        membraneC2R.addBox(-10.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        tailL11_1 = new WRModelRenderer(this, 145, 0);
        tailL11_1.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL11_1.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        tailL12_4 = new WRModelRenderer(this, 145, 0);
        tailL12_4.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL12_4.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        wingB1L = new WRModelRenderer(this, 30, 20);
        wingB1L.setRotationPoint(3.0F, 1.0F, -3.0F);
        wingB1L.addBox(0.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wingB1L, 0.0F, 0.04188790371233959F, 1.0471975511965976F);
        fingerC1_1L = new WRModelRenderer(this, 70, 70);
        fingerC1_1L.mirror = true;
        fingerC1_1L.setRotationPoint(9.0F, 0.0F, 0.0F);
        fingerC1_1L.addBox(0.0F, -0.5F, -1.0F, 10.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(fingerC1_1L, 0.0F, 0.011344639721844222F, 0.0F);
        teeth2 = new WRModelRenderer(this, 30, 117);
        teeth2.setRotationPoint(0.0F, 0.0F, -4.3F);
        teeth2.addBox(-1.5F, -3.5F, -3.5F, 3.0F, 4.0F, 7.0F, 0.0F, -0.4F, -0.2F);
        wingB3L = new WRModelRenderer(this, 30, 33);
        wingB3L.setRotationPoint(14.0F, 0.0F, 0.0F);
        wingB3L.addBox(0.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.02F, -0.02F);
        setRotateAngle(wingB3L, 0.0F, -0.6143559100178094F, 0.0F);
        tailL3_1 = new WRModelRenderer(this, 109, 0);
        tailL3_1.setRotationPoint(0.0F, 0.0F, 5.0F);
        tailL3_1.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        membraneA6R = new WRModelRenderer(this, 220, 30);
        membraneA6R.setRotationPoint(-7.5F, 0.04F, 1.0F);
        membraneA6R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneA6R, 0.0F, -1.298524990115392F, 0.0F);
        tailL1_4 = new WRModelRenderer(this, 109, 0);
        tailL1_4.setRotationPoint(-1.49F, 0.99F, 7.0F);
        tailL1_4.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        jaw = new WRModelRenderer(this, 0, 103);
        jaw.setRotationPoint(0.0F, 1.41F, 0.1F);
        jaw.addBox(-2.0F, -0.9F, -8.0F, 4.0F, 2.0F, 8.0F, -0.29F, -0.2F, -0.2F);
        tailL1_1 = new WRModelRenderer(this, 109, 0);
        tailL1_1.setRotationPoint(1.5F, -2.0F, 7.0F);
        tailL1_1.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        tailL2_1 = new WRModelRenderer(this, 109, 0);
        tailL2_1.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL2_1.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        membraneA2L = new WRModelRenderer(this, 70, 30);
        membraneA2L.setRotationPoint(0.0F, -0.01F, 1.0F);
        membraneA2L.addBox(0.0F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL6_1 = new WRModelRenderer(this, 128, 0);
        tailL6_1.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL6_1.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        tailL8_2 = new WRModelRenderer(this, 128, 0);
        tailL8_2.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL8_2.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        tailL5_4 = new WRModelRenderer(this, 128, 0);
        tailL5_4.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL5_4.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        body1 = new WRModelRenderer(this, 0, 0);
        body1.setRotationPoint(0.0F, -0.3F, -3.0F);
        body1.addBox(-4.0F, -4.0F, -10.0F, 8.0F, 8.0F, 10.0F, 0.1F, 0.0F, 0.0F);
        setRotateAngle(body1, 0.25132740562928074F, 0.0F, 0.0F);
        tailL4_4 = new WRModelRenderer(this, 109, 0);
        tailL4_4.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL4_4.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        fingerB1_2R = new WRModelRenderer(this, 75, 52);
        fingerB1_2R.mirror = true;
        fingerB1_2R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        fingerB1_2R.addBox(-50.0F, -0.5F, -1.0F, 50.0F, 1.0F, 2.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(fingerB1_2R, 0.0F, 1.5414747900350696F, 0.0F);
        membraneC4R = new WRModelRenderer(this, 170, 110);
        membraneC4R.setRotationPoint(-5.0F, 0.02F, 1.0F);
        membraneC4R.addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        fingerB1_1L = new WRModelRenderer(this, 70, 20);
        fingerB1_1L.mirror = true;
        fingerB1_1L.setRotationPoint(14.0F, 0.0F, 0.0F);
        fingerB1_1L.addBox(0.0F, -0.5F, -1.0F, 15.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(fingerB1_1L, 0.0F, -1.3711306177561677F, 0.0F);
        wingA2R = new WRModelRenderer(this, 30, 26);
        wingA2R.mirror = true;
        wingA2R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        wingA2R.addBox(-15.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(wingA2R, 0.0F, -2.1921335138732667F, 0.0F);
        membraneA2R = new WRModelRenderer(this, 70, 30);
        membraneA2R.mirror = true;
        membraneA2R.setRotationPoint(0.0F, -0.01F, 1.0F);
        membraneA2R.addBox(-15.0F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL1_3 = new WRModelRenderer(this, 109, 0);
        tailL1_3.setRotationPoint(1.49F, 1.0F, 7.0F);
        tailL1_3.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        body3 = new WRModelRenderer(this, 77, 0);
        body3.setRotationPoint(0.0F, 0.2F, 4.0F);
        body3.addBox(-3.5F, -4.0F, 0.0F, 7.0F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(body3, -0.0911061832922575F, 0.0F, 0.0F);
        wingB2L = new WRModelRenderer(this, 30, 26);
        wingB2L.setRotationPoint(14.0F, 0.0F, 0.0F);
        wingB2L.addBox(0.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(wingB2L, 0.0F, 0.3490658503988659F, 0.25132740562928074F);
        membraneB1L = new WRModelRenderer(this, 170, 65);
        membraneB1L.setRotationPoint(1.0F, 0.0F, 1.0F);
        membraneB1L.addBox(0.0F, 0.0F, 0.0F, 50.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL7_2 = new WRModelRenderer(this, 128, 0);
        tailL7_2.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL7_2.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        tailL4_1 = new WRModelRenderer(this, 109, 0);
        tailL4_1.setRotationPoint(-0.0F, -0.0F, 5.0F);
        tailL4_1.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        tailL3_2 = new WRModelRenderer(this, 109, 0);
        tailL3_2.setRotationPoint(0.0F, 0.0F, 5.0F);
        tailL3_2.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        membraneC1R = new WRModelRenderer(this, 170, 85);
        membraneC1R.mirror = true;
        membraneC1R.setRotationPoint(-1.0F, 0.0F, 1.0F);
        membraneC1R.addBox(-35.0F, 0.0F, 0.0F, 35.0F, 0.01F, 13.0F, 0.0F, 0.0F, 0.0F);
        membraneB4R = new WRModelRenderer(this, 170, 30);
        membraneB4R.setRotationPoint(-7.5F, 0.02F, 1.0F);
        membraneB4R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL7_1 = new WRModelRenderer(this, 128, 0);
        tailL7_1.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL7_1.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        fingerA1_1L = new WRModelRenderer(this, 70, 20);
        fingerA1_1L.mirror = true;
        fingerA1_1L.setRotationPoint(14.0F, 0.0F, 0.0F);
        fingerA1_1L.addBox(0.0F, -0.5F, -1.0F, 15.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(fingerA1_1L, 0.0F, -0.1563815016444822F, 0.0F);
        tailL8_1 = new WRModelRenderer(this, 128, 0);
        tailL8_1.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL8_1.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        wingB1R = new WRModelRenderer(this, 30, 20);
        wingB1R.mirror = true;
        wingB1R.setRotationPoint(-3.0F, 1.0F, -3.0F);
        wingB1R.addBox(-15.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wingB1R, 0.0F, -0.04188790371233959F, -1.0471975511965976F);
        membraneB2L = new WRModelRenderer(this, 70, 30);
        membraneB2L.setRotationPoint(0.0F, -0.01F, 1.0F);
        membraneB2L.addBox(0.0F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        membraneB6R = new WRModelRenderer(this, 220, 30);
        membraneB6R.setRotationPoint(-7.5F, 0.04F, 1.0F);
        membraneB6R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL11_2 = new WRModelRenderer(this, 145, 0);
        tailL11_2.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL11_2.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        tailL8_3 = new WRModelRenderer(this, 128, 0);
        tailL8_3.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL8_3.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        tailL3_4 = new WRModelRenderer(this, 109, 0);
        tailL3_4.setRotationPoint(0.0F, 0.0F, 5.0F);
        tailL3_4.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        wingC1L = new WRModelRenderer(this, 30, 70);
        wingC1L.setRotationPoint(2.5F, 1.0F, 4.0F);
        wingC1L.addBox(0.0F, -1.0F, -1.5F, 10.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wingC1L, 0.20943951023931953F, -0.49148471469844207F, 1.0890854798760725F);
        wingB2R = new WRModelRenderer(this, 30, 26);
        wingB2R.mirror = true;
        wingB2R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        wingB2R.addBox(-15.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(wingB2R, 0.0F, -0.3490658503988659F, -0.25132740562928074F);
        fingerC1_2L = new WRModelRenderer(this, 70, 70);
        fingerC1_2L.setRotationPoint(9.0F, 0.0F, 0.0F);
        fingerC1_2L.addBox(0.0F, -0.5F, -1.0F, 35.0F, 1.0F, 2.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(fingerC1_2L, 0.0F, -1.4995868613555947F, 0.0F);
        tailL10_3 = new WRModelRenderer(this, 145, 0);
        tailL10_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL10_3.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        teeth1 = new WRModelRenderer(this, 30, 106);
        teeth1.setRotationPoint(0.0F, 1.0F, -4.2F);
        teeth1.addBox(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 7.0F, 0.09F, -0.2F, -0.2F);
        membraneA4R = new WRModelRenderer(this, 170, 30);
        membraneA4R.setRotationPoint(-7.5F, 0.02F, 1.0F);
        membraneA4R.addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(membraneA4R, 0.0F, 1.9268435208333508F, 0.0F);
        tailL3_3 = new WRModelRenderer(this, 109, 0);
        tailL3_3.setRotationPoint(0.0F, 0.0F, 5.0F);
        tailL3_3.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        tailL8_4 = new WRModelRenderer(this, 128, 0);
        tailL8_4.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL8_4.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, -0.1F, -0.1F, 0.0F);
        wingC3R = new WRModelRenderer(this, 30, 83);
        wingC3R.mirror = true;
        wingC3R.setRotationPoint(-9.0F, 0.0F, 0.0F);
        wingC3R.addBox(-10.0F, -1.0F, -1.5F, 10.0F, 2.0F, 3.0F, 0.0F, -0.02F, -0.02F);
        setRotateAngle(wingC3R, 0.0F, 0.19547687289441354F, 0.0F);
        wingC2R = new WRModelRenderer(this, 30, 76);
        wingC2R.mirror = true;
        wingC2R.setRotationPoint(-9.0F, 0.0F, 0.0F);
        wingC2R.addBox(-10.0F, -1.0F, -1.5F, 10.0F, 2.0F, 3.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(wingC2R, 0.0F, -0.3071779550089047F, -0.20943951023931953F);
        wingA1L = new WRModelRenderer(this, 30, 20);
        wingA1L.setRotationPoint(3.0F, -1.0F, -7.0F);
        wingA1L.addBox(0.0F, -1.0F, -1.5F, 15.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wingA1L, 0.6702064593974334F, -0.712094321497881F, 0.41887902047863906F);
        wingC2L = new WRModelRenderer(this, 30, 76);
        wingC2L.setRotationPoint(9.0F, 0.0F, 0.0F);
        wingC2L.addBox(0.0F, -1.0F, -1.5F, 10.0F, 2.0F, 3.0F, 0.0F, -0.01F, -0.01F);
        setRotateAngle(wingC2L, 0.0F, 0.3071779550089047F, 0.20943951023931953F);
        fingerA1_1R = new WRModelRenderer(this, 70, 20);
        fingerA1_1R.setRotationPoint(-14.0F, 0.0F, 0.0F);
        fingerA1_1R.addBox(-15.0F, -0.5F, -1.0F, 15.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(fingerA1_1R, 0.0F, 0.1563815016444822F, 0.0F);
        membraneB2R = new WRModelRenderer(this, 70, 30);
        membraneB2R.mirror = true;
        membraneB2R.setRotationPoint(0.0F, -0.01F, 1.0F);
        membraneB2R.addBox(-15.0F, 0.0F, 0.0F, 15.0F, 0.01F, 15.0F, 0.0F, 0.0F, 0.0F);
        tailL6_2 = new WRModelRenderer(this, 128, 0);
        tailL6_2.setRotationPoint(-0.0F, -0.0F, 5.5F);
        tailL6_2.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.1F, 0.1F, 0.0F);
        tailL5_3 = new WRModelRenderer(this, 128, 0);
        tailL5_3.setRotationPoint(0.0F, 0.0F, 5.5F);
        tailL5_3.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.2F, 0.2F, 0.0F);
        wingA3L.addChild(membraneA3L);
        wingA3R.addChild(membraneA3R);
        wingB3R.addChild(membraneB3R);
        body1.addChild(neck1);
        fingerA1_1R.addChild(fingerA1_2R);
        tailL8_1.addChild(tailL9_1);
        body3.addChild(tailL1_2);
        wingC1R.addChild(membraneC5R);
        tailL3_2.addChild(tailL4_2);
        wingA2L.addChild(wingA3L);
        fingerB1_1L.addChild(fingerB1_2L);
        wingA2L.addChild(membraneA4L);
        tailL5_4.addChild(tailL6_4);
        wingA2R.addChild(wingA3R);
        tailL8_2.addChild(tailL9_2);
        tailL1_3.addChild(tailL2_3);
        tailL9_4.addChild(tailL10_4);
        neck1.addChild(neck2);
        tailL1_4.addChild(tailL2_4);
        wingB2R.addChild(wingB3R);
        tailL8_3.addChild(tailL9_3);
        fingerC1_2L.addChild(membraneC1L);
        fingerC1_1R.addChild(fingerC1_2R);
        jaw.addChild(tongue1);
        fingerA1_1L.addChild(fingerA1_2L);
        wingC2L.addChild(membraneC4L);
        tailL9_1.addChild(tailL10_1);
        tailL1_2.addChild(tailL2_2);
        wingB3L.addChild(membraneB3L);
        wingC3R.addChild(fingerC1_1R);
        wingB2L.addChild(membraneB4L);
        tailL11_2.addChild(tailL12_2);
        tailL4_1.addChild(tailL5_1);
        body3.addChild(wingC1R);
        tailL9_2.addChild(tailL10_2);
        wingA1L.addChild(wingA2L);
        tailL10_3.addChild(tailL11_3);
        neck3.addChild(neck4);
        fingerC1_1L.addChild(membraneC2L);
        wingC2L.addChild(wingC3L);
        fingerA1_2L.addChild(membraneA1L);
        tailL3_3.addChild(tailL4_3);
        neck4.addChild(neck5);
        neck7.addChild(head);
        tailL5_3.addChild(tailL6_3);
        wingA1R.addChild(membraneA5R);
        neck6.addChild(neck7);
        tongue1.addChild(tongue2);
        wingA1L.addChild(membraneA5L);
        wingB1R.addChild(membraneB5R);
        wingC1L.addChild(membraneC5L);
        wingC3L.addChild(membraneC3L);
        wingA1L.addChild(membraneA6L);
        tailL6_3.addChild(tailL7_3);
        wingC3R.addChild(membraneC3R);
        wingB3R.addChild(fingerB1_1R);
        fingerA1_2R.addChild(membraneA1R);
        wingC1L.addChild(membraneC6L);
        tailL10_4.addChild(tailL11_4);
        tailL6_4.addChild(tailL7_4);
        fingerB1_2R.addChild(membraneB1R);
        tailL8_4.addChild(tailL9_4);
        wingB1L.addChild(membraneB6L);
        tailL11_3.addChild(tailL12_3);
        body1.addChild(wingA1R);
        tailL4_2.addChild(tailL5_2);
        wingB1L.addChild(membraneB5L);
        neck2.addChild(neck3);
        wingC1R.addChild(membraneC6R);
        neck5.addChild(neck6);
        tailL11_1.addChild(tailL12_1);
        fingerC1_1R.addChild(membraneC2R);
        tailL10_1.addChild(tailL11_1);
        tailL11_4.addChild(tailL12_4);
        body1.addChild(wingB1L);
        wingC3L.addChild(fingerC1_1L);
        jaw.addChild(teeth2);
        wingB2L.addChild(wingB3L);
        tailL2_1.addChild(tailL3_1);
        wingA1R.addChild(membraneA6R);
        body3.addChild(tailL1_4);
        head.addChild(jaw);
        body3.addChild(tailL1_1);
        tailL1_1.addChild(tailL2_1);
        fingerA1_1L.addChild(membraneA2L);
        tailL5_1.addChild(tailL6_1);
        tailL7_2.addChild(tailL8_2);
        tailL4_4.addChild(tailL5_4);
        body2.addChild(body1);
        tailL3_4.addChild(tailL4_4);
        fingerB1_1R.addChild(fingerB1_2R);
        wingC2R.addChild(membraneC4R);
        wingB3L.addChild(fingerB1_1L);
        wingA1R.addChild(wingA2R);
        fingerA1_1R.addChild(membraneA2R);
        body3.addChild(tailL1_3);
        body2.addChild(body3);
        wingB1L.addChild(wingB2L);
        fingerB1_2L.addChild(membraneB1L);
        tailL6_2.addChild(tailL7_2);
        tailL3_1.addChild(tailL4_1);
        tailL2_2.addChild(tailL3_2);
        fingerC1_2R.addChild(membraneC1R);
        wingB2R.addChild(membraneB4R);
        tailL6_1.addChild(tailL7_1);
        wingA3L.addChild(fingerA1_1L);
        tailL7_1.addChild(tailL8_1);
        body1.addChild(wingB1R);
        fingerB1_1L.addChild(membraneB2L);
        wingB1R.addChild(membraneB6R);
        tailL10_2.addChild(tailL11_2);
        tailL7_3.addChild(tailL8_3);
        tailL2_4.addChild(tailL3_4);
        body3.addChild(wingC1L);
        wingB1R.addChild(wingB2R);
        fingerC1_1L.addChild(fingerC1_2L);
        tailL9_3.addChild(tailL10_3);
        head.addChild(teeth1);
        wingA2R.addChild(membraneA4R);
        tailL2_3.addChild(tailL3_3);
        tailL7_4.addChild(tailL8_4);
        wingC2R.addChild(wingC3R);
        wingC1R.addChild(wingC2R);
        body1.addChild(wingA1L);
        wingC1L.addChild(wingC2L);
        wingA3R.addChild(fingerA1_1R);
        fingerB1_1R.addChild(membraneB2R);
        tailL5_2.addChild(tailL6_2);
        tailL4_3.addChild(tailL5_3);

        tails = new WRModelRenderer[][] {
                {tailL1_1, tailL2_1, tailL3_1, tailL4_1, tailL5_1, tailL6_1, tailL7_1, tailL8_1, tailL9_1, tailL10_1, tailL11_1, tailL12_1}, // top left
                {tailL1_2, tailL2_2, tailL3_2, tailL4_2, tailL5_2, tailL6_2, tailL7_2, tailL8_2, tailL9_2, tailL10_2, tailL11_2, tailL12_2}, // top right
                {tailL1_3, tailL2_3, tailL3_3, tailL4_3, tailL5_3, tailL6_3, tailL7_3, tailL8_3, tailL9_3, tailL10_3, tailL11_3, tailL12_3}, // bottom left
                {tailL1_4, tailL2_4, tailL3_4, tailL4_4, tailL5_4, tailL6_4, tailL7_4, tailL8_4, tailL9_4, tailL10_4, tailL11_4, tailL12_4}, // bottom right
        };

        headArray = new WRModelRenderer[] {neck1, neck2, neck3, neck4, neck5, neck6, neck7, head};

        setRotateAngle(tailL1_1, 0.9f, 0.3f, 0);
        setRotateAngle(tailL1_2, 0.9f, -0.3f, 0);
        setRotateAngle(tailL1_3, 0, 0.3f, 0);
        setRotateAngle(tailL1_4, 0, -0.3f, 0);

        animator = ModelAnimator.create();

        setDefaultPose();
    }

    @Override
    public void render(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        alpha = -entity.stealthTimer.get() + 1;
        body2.render(ms, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setRotationAngles(FogWraithEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void setLivingAnimations(FogWraithEntity entity, float limbSwing, float limbSwingAmount, float partialTick)
    {
        this.entity = entity;
        float frame = entity.ticksExisted + partialTick;
        boolean flying = entity.flightTimer.get() > 0;

        animator.update(entity, partialTick);

        resetToDefaultPose();

        if (flying)
        {
            membraneA5R.showModel = false;
            membraneA5L.showModel = false;
            membraneB5R.showModel = false;
            membraneB5L.showModel = false;

            flap(wingA1R, globalSpeed - 0.2f, 0.85f, false, 0, 0.2f, limbSwing, limbSwingAmount);
            walk(wingA1R, globalSpeed - 0.2f, 0.3f, false, 0.6f, 0, limbSwing, limbSwingAmount);
            flap(wingA2R, globalSpeed - 0.2f, 0.6f, false, -1f, -0.1f, limbSwing, limbSwingAmount);
            flap(wingA3R, globalSpeed - 0.2f, 0.5f, false, -1.5f, 0, limbSwing, limbSwingAmount);
            flap(fingerA1_1R, globalSpeed - 0.2f, 0.3f, false, -1.5f, 0, limbSwing, limbSwingAmount);

            flap(wingB1R, globalSpeed - 0.2f, 0.65f, false, 0, -0.2f, limbSwing, limbSwingAmount);
            walk(wingB1R, globalSpeed - 0.2f, 0.3f, false, 0.6f, 0, limbSwing, limbSwingAmount);
            flap(wingB2R, globalSpeed - 0.2f, 0.55f, false, -1f, -0.1f, limbSwing, limbSwingAmount);
            flap(wingB3R, globalSpeed - 0.2f, 0.45f, false, -1.5f, 0, limbSwing, limbSwingAmount);
            flap(fingerB1_1R, globalSpeed - 0.2f, 0.3f, false, -1.5f, 0, limbSwing, limbSwingAmount);

            flap(wingC1R, globalSpeed - 0.2f, 0.65f, false, 0, -0.4f, limbSwing, limbSwingAmount);
            walk(wingC1R, globalSpeed - 0.2f, 0.3f, false, 0.6f, 0, limbSwing, limbSwingAmount);
            flap(wingC2R, globalSpeed - 0.2f, 0.55f, false, -1f, -0.1f, limbSwing, limbSwingAmount);
            flap(wingC3R, globalSpeed - 0.2f, 0.45f, false, -1.5f, 0, limbSwing, limbSwingAmount);
            flap(fingerC1_1R, globalSpeed - 0.2f, 0.3f, false, -1.5f, 0, limbSwing, limbSwingAmount);

            wingA1R.copyRotationsTo(wingA1L);
            wingA2R.copyRotationsTo(wingA2L);
            wingA3R.copyRotationsTo(wingA3L);
            fingerA1_1R.copyRotationsTo(fingerA1_1L);

            wingB1R.copyRotationsTo(wingB1L);
            wingB2R.copyRotationsTo(wingB2L);
            wingB3R.copyRotationsTo(wingB3L);
            fingerB1_1R.copyRotationsTo(fingerB1_1L);

            wingC1R.copyRotationsTo(wingC1L);
            wingC2R.copyRotationsTo(wingC2L);
            wingC3R.copyRotationsTo(wingC3L);
            fingerC1_1R.copyRotationsTo(fingerC1_1L);
        }
        else
        {
            membraneA5R.showModel = true;
            membraneA5L.showModel = true;
            membraneB5R.showModel = true;
            membraneB5L.showModel = true;
        }

        float swingAmount = 0.5f;

        if (animator.setAnimation(FogWraithEntity.GRAB_AND_ATTACK_ANIMATION)) grabAttackAnim(frame, partialTick);
        if (animator.setAnimation(FogWraithEntity.BITE_ANIMATION)) biteAnim();
        if (animator.setAnimation(FogWraithEntity.SCREECH_ANIMATION))
        {
            screechAnim(frame, swingAmount);
        }

        flight(entity.flightTimer.get(partialTick), frame);

        if (entity.getAnimation() == FogWraithEntity.SCREECH_ANIMATION)
            swingAmount *= getAnimationSwingDelta(10f, entity.getAnimationTick(), partialTick);

        idle(frame, swingAmount, partialTick);
    }

    public void idle(float frame, float swingAmount, float partialTick)
    {
        walk(body1, globalSpeed - 0.43f, 0.1f, false, 0, 0, frame, swingAmount);

        chainWave(headArray, globalSpeed - 0.43f, 0.05f, -2, frame, swingAmount);

        swing(wingB1R, globalSpeed - 0.43f, -0.1f, false, 0, 0, frame, swingAmount);
        swing(wingB2R, globalSpeed - 0.43f, -0.1f, false, 0, 0, frame, swingAmount);
        swing(wingB3R, globalSpeed - 0.43f, 0.2f, false, 0, 0, frame, swingAmount);

        swing(wingB1L, globalSpeed - 0.43f, -0.1f, true, 0, 0, frame, swingAmount);
        swing(wingB2L, globalSpeed - 0.43f, -0.1f, true, 0, 0, frame, swingAmount);
        swing(wingB3L, globalSpeed - 0.43f, 0.2f, true, 0, 0, frame, swingAmount);

        flap(wingA1R, globalSpeed - 0.45f, 0.2f, false, 0, 0, frame, swingAmount);
        swing(wingA2R, globalSpeed - 0.45f, 0.2f, false, 0, 0, frame, swingAmount);
        swing(wingA3R, globalSpeed - 0.45f, -0.09f, false, 0, 0, frame, swingAmount);
        swing(fingerA1_1R, globalSpeed - 0.45f, -0.2f, false, 0.5f, 0, frame, swingAmount);
        swing(fingerA1_2R, globalSpeed - 0.45f, -0.1f, false, 0.5f, 0, frame, swingAmount);

        flap(wingA1L, globalSpeed - 0.45f, 0.1f, false, 0, 0, frame, swingAmount);
        swing(wingA2L, globalSpeed - 0.45f, 0.2f, true, 0, 0, frame, swingAmount);
        swing(wingA3L, globalSpeed - 0.45f, -0.09f, true, 0, 0, frame, swingAmount);
        swing(fingerA1_1L, globalSpeed - 0.45f, -0.2f, true, 0.5f, 0, frame, swingAmount);
        swing(fingerA1_2L, globalSpeed - 0.45f, -0.1f, true, 0.5f, 0, frame, swingAmount);

        // tail
        for (int i = 0; i < tails.length; i++)
        {
            swingAmount *= -entity.flightTimer.get(partialTick) + 1;
            if (i == 0 && entity.getAnimation() == FogWraithEntity.GRAB_AND_ATTACK_ANIMATION)
                swingAmount *= getAnimationSwingDelta(8f, entity.getAnimationTick(), partialTick);

            animateTail(i, frame, swingAmount);
        }
    }

    public void animateTail(int generation, float tick, float swingAmount)
    {
        float waveDegree = 0.4f;
        if (generation > 1) waveDegree = 0.1f;
        if (generation == 1 || generation == 2) waveDegree = -waveDegree;
        chainWave(tails[generation], globalSpeed - 0.4f, waveDegree, -2, tick, swingAmount);
        chainSwing(tails[generation], globalSpeed - (generation > 1? 0.38f : 0.35f), (generation == 1 || generation == 2? -0.4f : 0.4f), -2, tick, swingAmount);
    }

    private void flight(float v, float frame)
    {
        if (v == 0) return;
        setTime(v);

        for (ModelRenderer head : headArray) rotate(head, -head.rotateAngleX, 0, 0);

        rotate(wingA1R, -0.67f, -1f, 0.42f);
        rotate(wingA2R, 0, 1.9f, 0);
        rotate(wingA3R, 0, -2.2f, 0);
        rotate(membraneA4R, 0, -1.8f, 0);
        rotate(membraneA5R, 0, 0.38f, 0);
        rotate(membraneA6R, 0, 1.2f, 0);

        rotate(wingB1R, 0, 0.2f, 1.05f);
        rotate(wingB2R, 0, 0.1f, 0.25f);
        rotate(wingB3R, 0, -0.61f, 0);
        rotate(fingerB1_1R, 0, -1.2f, 0);
        rotate(fingerB1_2R, 0, -1.3f, 0);
        rotate(membraneB5R, 0, 0, 0.25f);

        rotate(wingC1R, 0.15f, -0.1f, 1.1f);
        rotate(wingC2R, 0, -0.3f, 0.21f);
        rotate(wingC3R, 0, 0.1f, 0.21f);
        rotate(fingerC1_1R, 0, 0.1f, 0);
        rotate(fingerC1_2R, 0, -1.2f, 0);


        rotate(wingA1L, -0.67f, 1f, -0.42f);
        rotate(wingA2L, 0, -1.9f, 0);
        rotate(wingA3L, 0, 2.2f, 0);
        rotate(membraneA4L, 0, 1.8f, 0);
        rotate(membraneA5L, 0, -0.38f, 0);
        rotate(membraneA6L, 0, -1.2f, 0);

        rotate(wingB1L, 0, -0.2f, -1.05f);
        rotate(wingB2L, 0, -0.1f, -0.25f);
        rotate(wingB3L, 0, 0.61f, 0);
        rotate(fingerB1_1L, 0, 1.2f, 0);
        rotate(fingerB1_2L, 0, 1.3f, 0);
        rotate(membraneB5L, 0, 0, -0.25f);

        rotate(wingC1L, 0.15f, 0.1f, -1.1f);
        rotate(wingC2L, 0, 0.3f, -0.21f);
        rotate(wingC3L, 0, -0.1f, -0.21f);
        rotate(fingerC1_1L, 0, -0.1f, 0);
        rotate(fingerC1_2L, 0, 1.2f, 0);

        chainWave(tails[0], globalSpeed - 0.2f, 0.15f, -2, frame, v * 0.5f);
        for (int i = 0; i < tails.length; i++)
        {
            rotate(tails[i][0], i < 2? -0.5f : 0.3f, i % 2 == 0? -0.3f : 0.3f, 0);
            if (i > 0)
            {
                for (int j = 0; j < tails[i].length; j++)
                    tails[i][j].rotateAngleX = i % 2 == 0? tails[0][j].rotateAngleX : -tails[0][j].rotateAngleX;
            }
        }

        chainWave(headArray, globalSpeed - 0.2f, 0.1f, 3, frame, v * 0.5f);
    }

    public void grabAttackAnim(float frame, float partialTicks)
    {
        WRModelRenderer[] parts = tails[0];
        animator.startKeyframe(1);
        animator.rotate(parts[0], 0.3f, 0, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(100);
    }

    public void biteAnim()
    {
        animator.startKeyframe(4);
        animator.rotate(neck1, -0.3f, 0, 0);
        animator.rotate(neck2, -0.3f, 0, 0);
        animator.rotate(neck3, -0.2f, 0, 0);
        animator.rotate(neck4, 0.5f, 0, 0);
        animator.rotate(neck5, 0.5f, 0, 0);
        animator.rotate(neck6, 0.4f, 0, 0);
        animator.rotate(jaw, 1f, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(3);
        for (WRModelRenderer head : headArray) animator.rotate(head, -head.rotateAngleX, 0, 0);
        animator.rotate(neck1, 0.85f, 0, 0);
        animator.rotate(head, 0.2f, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(6);
    }

    public void screechAnim(float frame, float swingAmount)
    {
        animator.startKeyframe(10);
        animator.rotate(body1, 0.4f, 0, 0);

        animator.rotate(neck1, 1.2f, 0, 0);
        animator.rotate(neck2, 0.2f, 0, 0);
        animator.rotate(neck3, -0.2f, 0, 0);
        animator.rotate(neck4, -0.15f, 0, 0);
        animator.rotate(neck5, -1.2f, 0, 0);
        animator.rotate(neck6, -0.1f, 0, 0);
        animator.rotate(neck7, -0.1f, 0, 0);
        animator.rotate(jaw, 1.2f, 0, 0);
        animator.rotate(head, -0.5f, 0, 0);

        animator.rotate(wingA1R, -2, -0.4f, 0.7f);
        animator.rotate(wingA2R, 0, 1.8f, 0);
        animator.rotate(membraneA4R, 0, -2.4f, 0);
        animator.rotate(membraneA6R, 0, 1.2f, 0);
        animator.rotate(wingA3R, 0, -3.1f, 0);

        animator.rotate(wingA1L, -2, 0.4f, -0.7f);
        animator.rotate(wingA2L, 0, -1.8f, 0);
        animator.rotate(membraneA4L, 0, 2.4f, 0);
        animator.rotate(membraneA6L, 0, -1.2f, 0);
        animator.rotate(wingA3L, 0, 3.1f, 0);

        animator.rotate(wingB1R, 0, -0.6f, 0);
        animator.rotate(wingB2R, -0.5f, -0.3f, 0.2f);
        animator.rotate(wingB3R, 0, 0.3f, 0);
        animator.rotate(fingerB1_2R, 0, -0.3f, 0);
        animator.rotate(membraneB5R, -0.5f, 0, 0);
        animator.rotate(membraneB6R, -0.5f, 0, 0);

        animator.rotate(wingB1L, 0, 0.6f, 0);
        animator.rotate(wingB2L, -0.5f, 0.3f, -0.2f);
        animator.rotate(wingB3L, 0, -0.3f, 0);
        animator.rotate(fingerB1_2L, 0, 0.3f, 0);
        animator.rotate(membraneB5L, -0.5f, 0, 0);
        animator.rotate(membraneB6L, -0.5f, 0, 0);
        for (int i = 0; i < tails.length; i++)
        {
            for (WRModelRenderer part : tails[i])
            {
                animator.rotate(part, i < 2? 0.1f : -0.1f, i % 2 != 0? -0.3f : 0.3f, 0);
            }
        }
        animator.endKeyframe();
        animator.setStaticKeyframe(50);
        animator.resetKeyframe(10);

        int tick = entity.getAnimationTick();
        if (tick > 5 && tick < 60)
        {
            float delta = (Math.min(MathHelper.sin(((tick - 6) / 54f) * Mafs.PI) * 2, 1) * 0.5f);
            chainFlap(headArray, globalSpeed + 0.5f, 0.05f, -2.5, frame, delta);
            chainSwing(headArray, globalSpeed + 0.5f, 0.03f, -2.5, frame, delta);
        }
    }
}
