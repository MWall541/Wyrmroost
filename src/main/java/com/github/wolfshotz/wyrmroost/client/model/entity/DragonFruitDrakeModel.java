package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.DragonFruitDrakeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class DragonFruitDrakeModel extends DragonEntityModel<DragonFruitDrakeEntity>
{
    public static final ResourceLocation GRAY_SCALE = Wyrmroost.id(FOLDER + "dragon_fruit_drake/body.png");
    public static final ResourceLocation GRAY_SCALE_CHILD = Wyrmroost.id(FOLDER + "dragon_fruit_drake/body_child.png");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[20];

    public WRModelRenderer Body1;
    public WRModelRenderer Body2;
    public WRModelRenderer neck2;
    public WRModelRenderer LegfrontR1;
    public WRModelRenderer BackLeafL;
    public WRModelRenderer BackLeafR;
    public WRModelRenderer SideLeaf1R;
    public WRModelRenderer SideLeaf2R;
    public WRModelRenderer SideLeaf1L;
    public WRModelRenderer SideLeaf2L;
    public WRModelRenderer LegfrontR1_1;
    public WRModelRenderer Tail1;
    public WRModelRenderer LegThighR;
    public WRModelRenderer BackLeafL_1;
    public WRModelRenderer BackLeafR_1;
    public WRModelRenderer LegThighR_1;
    public WRModelRenderer Tail2;
    public WRModelRenderer Tail3;
    public WRModelRenderer Tail4;
    public WRModelRenderer TailLeaf3;
    public WRModelRenderer Tail5;
    public WRModelRenderer TailLeaf4;
    public WRModelRenderer Tail6;
    public WRModelRenderer BackLeaf1;
    public WRModelRenderer Tail7;
    public WRModelRenderer TailLeafEND;
    public WRModelRenderer LegSegmentR1;
    public WRModelRenderer LegSegmentR2;
    public WRModelRenderer LegSegmentR3;
    public WRModelRenderer backfootR;
    public WRModelRenderer LegSegmentR1_1;
    public WRModelRenderer LegSegmentR2_1;
    public WRModelRenderer LegSegmentR3_1;
    public WRModelRenderer backfootR_1;
    public WRModelRenderer neck3;
    public WRModelRenderer Neck1Leaf;
    public WRModelRenderer neck4;
    public WRModelRenderer Neck2Leaf;
    public WRModelRenderer Head;
    public WRModelRenderer NeckLeaf3;
    public WRModelRenderer mouthtop;
    public WRModelRenderer mouthbottom;
    public WRModelRenderer EyeR;
    public WRModelRenderer shape71;
    public WRModelRenderer shape71_1;
    public WRModelRenderer EyeR_1;
    public WRModelRenderer LegfrontR2;
    public WRModelRenderer LegfrontR3;
    public WRModelRenderer frontfootR;
    public WRModelRenderer LegfrontR2_1;
    public WRModelRenderer LegfrontR3_1;
    public WRModelRenderer frontfootR_1;

    public DragonFruitDrakeModel()
    {
        this.texWidth = 100;
        this.texHeight = 70;
        this.LegSegmentR1 = new WRModelRenderer(this, 57, 31);
        this.LegSegmentR1.setPos(0.3F, 4.3F, 0.0F);
        this.LegSegmentR1.addBox(-1.5F, -1.3F, -0.6F, 3.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegSegmentR1, -0.30543261909900765F, 0.0F, 0.007504915908411292F);
        this.LegSegmentR2_1 = new WRModelRenderer(this, 55, 41);
        this.LegSegmentR2_1.setPos(0.1F, 1.0F, 3.5F);
        this.LegSegmentR2_1.addBox(-1.5F, -1.9F, -0.3F, 3.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegSegmentR2_1, -1.4456562513848363F, 0.0F, 0.0F);
        this.LegSegmentR1_1 = new WRModelRenderer(this, 57, 31);
        this.LegSegmentR1_1.mirror = true;
        this.LegSegmentR1_1.setPos(-0.2F, 4.3F, 0.0F);
        this.LegSegmentR1_1.addBox(-1.5F, -1.3F, -0.6F, 3.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegSegmentR1_1, -0.30543261909900765F, 0.0F, 0.0F);
        this.LegfrontR3_1 = new WRModelRenderer(this, 40, 39);
        this.LegfrontR3_1.mirror = true;
        this.LegfrontR3_1.setPos(0.01F, 0.1F, 5.0F);
        this.LegfrontR3_1.addBox(-1.5F, -1.5F, -0.7F, 3.0F, 3.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegfrontR3_1, -0.3490658503988659F, 0.0F, 0.0F);
        this.LegfrontR1 = new WRModelRenderer(this, 34, 18);
        this.LegfrontR1.mirror = true;
        this.LegfrontR1.setPos(3.3F, 1.2F, -1.8F);
        this.LegfrontR1.addBox(-1.5F, -2.0F, -0.6F, 3.0F, 4.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegfrontR1, -0.9873327525268701F, 0.0F, 0.0F);
        this.SideLeaf1R = new WRModelRenderer(this, 0, 29);
        this.SideLeaf1R.setPos(4.1F, -1.1F, -3.0F);
        this.SideLeaf1R.addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(SideLeaf1R, 0.0F, 0.18552850148436806F, -0.0F);
        this.Body1 = new WRModelRenderer(this, 33, 1);
        this.Body1.setPos(0.0F, 9.5F, -2.5F);
        this.Body1.addBox(-4.0F, -3.1F, -4.0F, 8.0F, 8.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.neck2 = new WRModelRenderer(this, 23, 0);
        this.neck2.setPos(0.0F, -0.2F, -2.2F);
        this.neck2.addBox(-2.5F, -1.9F, -3.7F, 5.0F, 5.0F, 4.0F, 0.0F, 0.2F, 0.0F);
        this.setRotateAngle(neck2, -0.5326744956981526F, 0.0F, 0.0F);
        this.SideLeaf2L = new WRModelRenderer(this, 0, 29);
        this.SideLeaf2L.setPos(-4.0F, 0.4F, 1.4F);
        this.SideLeaf2L.addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(SideLeaf2L, 0.0F, -0.26668630504420165F, -0.0F);
        this.mouthtop = new WRModelRenderer(this, 15, 15);
        this.mouthtop.setPos(0.0F, 2.05F, -3.95F);
        this.mouthtop.addBox(-2.0F, -2.6F, -5.0F, 4.0F, 2.5F, 5.0F, 0.01F, 0.0F, 0.0F);
        this.LegThighR = new WRModelRenderer(this, 59, 18);
        this.LegThighR.setPos(3.3F, 0.4F, 2.3F);
        this.LegThighR.addBox(-1.1F, -1.3F, -1.9F, 3.0F, 8.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.LegSegmentR2 = new WRModelRenderer(this, 55, 41);
        this.LegSegmentR2.mirror = true;
        this.LegSegmentR2.setPos(-0.1F, 1.0F, 3.5F);
        this.LegSegmentR2.addBox(-1.5F, -1.9F, -0.3F, 3.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegSegmentR2, -1.4456562513848363F, 0.0F, 0.0F);
        this.LegfrontR3 = new WRModelRenderer(this, 40, 39);
        this.LegfrontR3.setPos(-0.01F, 0.1F, 5.0F);
        this.LegfrontR3.addBox(-1.5F, -1.5F, -0.7F, 3.0F, 3.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegfrontR3, -0.3490658503988659F, 0.0F, 0.0F);
        this.Tail3 = new WRModelRenderer(this, 82, 24);
        this.Tail3.setPos(0.0F, 0.3F, 4.4F);
        this.Tail3.addBox(-2.0F, -1.8F, -1.3F, 4.0F, 4.0F, 5.0F, 0.1F, 0.1F, 0.0F);
        this.setRotateAngle(Tail3, -0.1222409665697785F, 0.0F, 0.0F);
        this.frontfootR = new WRModelRenderer(this, 40, 46);
        this.frontfootR.setPos(-0.01F, -0.5F, 2.8F);
        this.frontfootR.addBox(-1.5F, -1.0F, -0.4F, 3.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(frontfootR, -0.9948376736367678F, 0.0F, 0.0F);
        this.Tail2 = new WRModelRenderer(this, 80, 13);
        this.Tail2.setPos(0.0F, 0.1F, 3.7F);
        this.Tail2.addBox(-2.5F, -1.9F, -0.4F, 5.0F, 5.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Tail2, -0.20367992503931878F, 0.0F, 0.0F);
        this.EyeR_1 = new WRModelRenderer(this, 16, 0);
        this.EyeR_1.mirror = true;
        this.EyeR_1.setPos(-2.5F, -0.1F, -4.5F);
        this.EyeR_1.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(EyeR_1, 0.0F, -4.082150576836893F, 0.0F);
        this.BackLeafR_1 = new WRModelRenderer(this, 0, 23);
        this.BackLeafR_1.mirror = true;
        this.BackLeafR_1.setPos(2.0F, -2.9F, 3.7F);
        this.BackLeafR_1.addBox(-1.0F, -4.9F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(BackLeafR_1, -1.3683381601951652F, 0.0F, 0.0F);
        this.Body2 = new WRModelRenderer(this, 68, 0);
        this.Body2.setPos(0.0F, 0.0F, 5.4F);
        this.Body2.addBox(-3.5F, -3.0F, -1.3F, 7.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.BackLeafR = new WRModelRenderer(this, 0, 23);
        this.BackLeafR.mirror = true;
        this.BackLeafR.setPos(2.5F, -3.0F, 0.4F);
        this.BackLeafR.addBox(-1.0F, -4.9F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(BackLeafR, -1.2524483085153861F, 0.0F, 0.0F);
        this.frontfootR_1 = new WRModelRenderer(this, 40, 46);
        this.frontfootR_1.mirror = true;
        this.frontfootR_1.setPos(0.01F, -0.5F, 2.8F);
        this.frontfootR_1.addBox(-1.5F, -1.0F, -0.4F, 3.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(frontfootR_1, -0.9948376736367678F, 0.0F, 0.0F);
        this.Tail1 = new WRModelRenderer(this, 80, 13);
        this.Tail1.setPos(0.0F, -0.3F, 4.8F);
        this.Tail1.addBox(-2.5F, -1.9F, -0.8F, 5.0F, 5.0F, 5.0F, 0.1F, 0.2F, 0.0F);
        this.setRotateAngle(Tail1, -0.29304079738881444F, 0.0F, 0.0F);
        this.Tail5 = new WRModelRenderer(this, 86, 34);
        this.Tail5.setPos(0.0F, 0.4F, 3.3F);
        this.Tail5.addBox(-1.5F, -1.3F, 0.4F, 3.0F, 3.0F, 4.0F, 0.1F, 0.1F, 0.0F);
        this.setRotateAngle(Tail5, 0.38030725998853093F, 0.0F, 0.0F);
        this.shape71_1 = new WRModelRenderer(this, 6, 26);
        this.shape71_1.setPos(2.5F, -1.1F, 0.0F);
        this.shape71_1.addBox(-0.5F, -0.6F, -0.6F, 1.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(shape71_1, 0.39426988068868013F, 0.2724458902442024F, -0.0F);
        this.LegfrontR2 = new WRModelRenderer(this, 34, 29);
        this.LegfrontR2.mirror = true;
        this.LegfrontR2.setPos(-0.1F, 0.0F, 5.9F);
        this.LegfrontR2.addBox(-1.5F, -1.5F, -0.9F, 3.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegfrontR2, -0.8052949062175392F, 0.0F, 0.0F);
        this.mouthbottom = new WRModelRenderer(this, 15, 23);
        this.mouthbottom.setPos(0.0F, 2.8F, -3.9F);
        this.mouthbottom.addBox(-2.0F, -0.85F, -5.0F, 4.0F, 1.5F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.LegfrontR2_1 = new WRModelRenderer(this, 34, 29);
        this.LegfrontR2_1.setPos(0.3F, 0.0F, 5.9F);
        this.LegfrontR2_1.addBox(-1.5F, -1.5F, -0.9F, 3.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegfrontR2_1, -0.8052949062175392F, 0.0F, 0.0F);
        this.LegSegmentR3 = new WRModelRenderer(this, 61, 50);
        this.LegSegmentR3.setPos(-0.01F, -0.4F, 5.7F);
        this.LegSegmentR3.addBox(-1.5F, -1.1F, -0.9F, 3.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegSegmentR3, -0.6944665253475103F, 0.0F, 0.0F);
        this.LegfrontR1_1 = new WRModelRenderer(this, 34, 18);
        this.LegfrontR1_1.setPos(-3.3F, 1.2F, -1.8F);
        this.LegfrontR1_1.addBox(-1.3F, -2.0F, -0.6F, 3.0F, 4.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegfrontR1_1, -0.9873327525268701F, 0.0F, 0.0F);
        this.BackLeaf1 = new WRModelRenderer(this, 0, 29);
        this.BackLeaf1.setPos(-0.3F, -1.1F, 1.8F);
        this.BackLeaf1.addBox(-1.0F, -3.9F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(BackLeaf1, -1.3800318342255942F, 0.0F, 0.0F);
        this.LegThighR_1 = new WRModelRenderer(this, 59, 18);
        this.LegThighR_1.mirror = true;
        this.LegThighR_1.setPos(-3.3F, 0.4F, 2.3F);
        this.LegThighR_1.addBox(-1.8F, -1.4F, -1.9F, 3.0F, 8.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.BackLeafL_1 = new WRModelRenderer(this, 0, 23);
        this.BackLeafL_1.setPos(-2.0F, -2.9F, 3.7F);
        this.BackLeafL_1.addBox(-1.0F, -4.9F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(BackLeafL_1, -1.3800318342255942F, 0.0F, 0.0F);
        this.Neck2Leaf = new WRModelRenderer(this, 5, 15);
        this.Neck2Leaf.setPos(0.0F, -1.4F, -2.5F);
        this.Neck2Leaf.addBox(0.0F, -5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Neck2Leaf, 0.05619960241356012F, 0.0F, 0.0F);
        this.SideLeaf2R = new WRModelRenderer(this, 0, 29);
        this.SideLeaf2R.setPos(4.0F, 0.4F, 1.4F);
        this.SideLeaf2R.addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(SideLeaf2R, 0.0F, 0.26668630504420165F, -0.0F);
        this.LegSegmentR3_1 = new WRModelRenderer(this, 61, 50);
        this.LegSegmentR3_1.mirror = true;
        this.LegSegmentR3_1.setPos(0.01F, -0.4F, 5.7F);
        this.LegSegmentR3_1.addBox(-1.5F, -1.1F, -0.9F, 3.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(LegSegmentR3_1, -0.6944665253475103F, 0.0F, 0.0F);
        this.Head = new WRModelRenderer(this, 0, 4);
        this.Head.setPos(0.0F, 0.0F, -4.2F);
        this.Head.addBox(-3.0F, -1.5F, -4.7F, 6.0F, 5.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Head, 0.48712139156845624F, 0.0F, 0.0F);
        this.backfootR_1 = new WRModelRenderer(this, 61, 55);
        this.backfootR_1.setPos(-0.01F, 0.0F, 1.6F);
        this.backfootR_1.addBox(-1.5F, -1.2F, -0.3F, 3.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(backfootR_1, -0.7014478024080711F, 0.0F, 0.0F);
        this.Tail6 = new WRModelRenderer(this, 86, 34);
        this.Tail6.setPos(0.0F, -0.1F, 4.0F);
        this.Tail6.addBox(-1.5F, -1.4F, -0.8F, 3.0F, 3.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Tail6, 0.2988003825888152F, 0.0F, 0.0F);
        this.Tail4 = new WRModelRenderer(this, 82, 24);
        this.Tail4.setPos(0.0F, -0.7F, 2.9F);
        this.Tail4.addBox(-2.0F, -1.2F, -0.5F, 4.0F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Tail4, 0.2851868024390345F, 0.0F, 0.0F);
        this.Tail7 = new WRModelRenderer(this, 88, 42);
        this.Tail7.setPos(0.0F, 0.0F, 3.4F);
        this.Tail7.addBox(-1.0F, -1.1F, -0.5F, 2.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Tail7, 0.2309070520493665F, 0.0F, 0.0F);
        this.shape71 = new WRModelRenderer(this, 6, 26);
        this.shape71.mirror = true;
        this.shape71.setPos(-2.5F, -1.1F, 0.0F);
        this.shape71.addBox(-0.5F, -0.6F, -0.6F, 1.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(shape71, 0.39426988068868013F, -0.2724458902442024F, 0.0F);
        this.neck4 = new WRModelRenderer(this, 23, 0);
        this.neck4.setPos(0.0F, -0.05F, -3.2F);
        this.neck4.addBox(-2.5F, -1.9F, -3.8F, 5.0F, 5.0F, 4.0F, -0.2F, 0.0F, 0.0F);
        this.setRotateAngle(neck4, 0.4300491170387584F, 0.0F, 0.0F);
        this.EyeR = new WRModelRenderer(this, 16, 0);
        this.EyeR.setPos(2.5F, -0.1F, -4.5F);
        this.EyeR.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(EyeR, 0.0F, 4.082150576836893F, 0.0F);
        this.TailLeaf4 = new WRModelRenderer(this, 0, 29);
        this.TailLeaf4.setPos(-0.2F, -1.1F, 1.8F);
        this.TailLeaf4.addBox(-1.0F, -3.9F, 0.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(TailLeaf4, -1.3800318342255942F, 0.0F, 0.0F);
        this.backfootR = new WRModelRenderer(this, 61, 55);
        this.backfootR.mirror = true;
        this.backfootR.setPos(0.01F, 0.0F, 1.6F);
        this.backfootR.addBox(-1.5F, -1.2F, -0.3F, 3.0F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(backfootR, -0.7014478024080711F, 0.0F, 0.0F);
        this.NeckLeaf3 = new WRModelRenderer(this, 0, 14);
        this.NeckLeaf3.setPos(0.0F, -1.2F, -2.3F);
        this.NeckLeaf3.addBox(0.0F, -6.0F, -1.0F, 0.0F, 6.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(NeckLeaf3, -0.25516713464420016F, 0.0F, 0.0F);
        this.SideLeaf1L = new WRModelRenderer(this, 0, 29);
        this.SideLeaf1L.setPos(-4.0F, -1.1F, -3.0F);
        this.SideLeaf1L.addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(SideLeaf1L, 0.0F, -0.162315623764424F, -0.0F);
        this.BackLeafL = new WRModelRenderer(this, 0, 23);
        this.BackLeafL.setPos(-2.5F, -3.0F, 0.4F);
        this.BackLeafL.addBox(-1.0F, -4.9F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(BackLeafL, -1.2524483085153861F, 0.0F, 0.0F);
        this.Neck1Leaf = new WRModelRenderer(this, 10, 16);
        this.Neck1Leaf.setPos(0.0F, -1.7F, -2.8F);
        this.Neck1Leaf.addBox(0.0F, -4.0F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(Neck1Leaf, -0.048171087188595925F, 0.0F, 0.0F);
        this.neck3 = new WRModelRenderer(this, 23, 0);
        this.neck3.setPos(0.0F, 0.1F, -3.1F);
        this.neck3.addBox(-2.5F, -1.9F, -3.8F, 5.0F, 5.0F, 4.0F, -0.1F, 0.1F, 0.0F);
        this.setRotateAngle(neck3, -0.25115288535409647F, 0.0F, 0.0F);
        this.TailLeaf3 = new WRModelRenderer(this, 0, 23);
        this.TailLeaf3.setPos(-0.1F, -1.7F, 1.8F);
        this.TailLeaf3.addBox(-1.0F, -4.9F, 0.0F, 2.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(TailLeaf3, -1.3800318342255942F, 0.0F, 0.0F);
        this.TailLeafEND = new WRModelRenderer(this, 82, 40);
        this.TailLeafEND.setPos(0.1F, 0.0F, 3.0F);
        this.TailLeafEND.addBox(-0.1F, -3.1F, -0.3F, 0.0F, 6.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.LegThighR.addChild(this.LegSegmentR1);
        this.LegSegmentR1_1.addChild(this.LegSegmentR2_1);
        this.LegThighR_1.addChild(this.LegSegmentR1_1);
        this.LegfrontR2_1.addChild(this.LegfrontR3_1);
        this.Body1.addChild(this.LegfrontR1);
        this.Body1.addChild(this.SideLeaf1R);
        this.Body1.addChild(this.neck2);
        this.Body1.addChild(this.SideLeaf2L);
        this.Head.addChild(this.mouthtop);
        this.Body2.addChild(this.LegThighR);
        this.LegSegmentR1.addChild(this.LegSegmentR2);
        this.LegfrontR2.addChild(this.LegfrontR3);
        this.Tail2.addChild(this.Tail3);
        this.LegfrontR3.addChild(this.frontfootR);
        this.Tail1.addChild(this.Tail2);
        this.Head.addChild(this.EyeR_1);
        this.Body2.addChild(this.BackLeafR_1);
        this.Body1.addChild(this.Body2);
        this.Body1.addChild(this.BackLeafR);
        this.LegfrontR3_1.addChild(this.frontfootR_1);
        this.Body2.addChild(this.Tail1);
        this.Tail4.addChild(this.Tail5);
        this.Head.addChild(this.shape71_1);
        this.LegfrontR1.addChild(this.LegfrontR2);
        this.Head.addChild(this.mouthbottom);
        this.LegfrontR1_1.addChild(this.LegfrontR2_1);
        this.LegSegmentR2.addChild(this.LegSegmentR3);
        this.Body1.addChild(this.LegfrontR1_1);
        this.Tail5.addChild(this.BackLeaf1);
        this.Body2.addChild(this.LegThighR_1);
        this.Body2.addChild(this.BackLeafL_1);
        this.neck3.addChild(this.Neck2Leaf);
        this.Body1.addChild(this.SideLeaf2R);
        this.LegSegmentR2_1.addChild(this.LegSegmentR3_1);
        this.neck4.addChild(this.Head);
        this.LegSegmentR3_1.addChild(this.backfootR_1);
        this.Tail5.addChild(this.Tail6);
        this.Tail3.addChild(this.Tail4);
        this.Tail6.addChild(this.Tail7);
        this.Head.addChild(this.shape71);
        this.neck3.addChild(this.neck4);
        this.Head.addChild(this.EyeR);
        this.Tail4.addChild(this.TailLeaf4);
        this.LegSegmentR3.addChild(this.backfootR);
        this.neck4.addChild(this.NeckLeaf3);
        this.Body1.addChild(this.SideLeaf1L);
        this.Body1.addChild(this.BackLeafL);
        this.neck2.addChild(this.Neck1Leaf);
        this.neck2.addChild(this.neck3);
        this.Tail3.addChild(this.TailLeaf3);
        this.Tail7.addChild(this.TailLeafEND);

        setDefaultPose();
    }

    @Override
    public void renderToBuffer(MatrixStack ms, IVertexBuilder vertex, int light, int overlay, float red, float green, float blue, float alpha)
    {
        Body1.render(ms, vertex, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void scale(DragonFruitDrakeEntity entity, MatrixStack ms, float partialTicks)
    {
        super.scale(entity, ms, partialTicks);
        ms.scale(1.5f, 1.5f, 1.5f);
    }

    @Override
    public void postProcess(DragonFruitDrakeEntity entity, MatrixStack ms, IRenderTypeBuffer buffer, int light, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float partialTicks)
    {
        int color = getColorByAge(entity);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        renderToBuffer(ms, buffer.getBuffer(RenderType.entityTranslucent(getBodyTexture(entity))), light, LivingRenderer.getOverlayCoords(entity, 0), r, g, b, 1f);
    }

    @Override
    public ResourceLocation getTexture(DragonFruitDrakeEntity entity)
    {
        int index = entity.isHatchling()? 2 : entity.isMale()? 0 : 1;
        if (entity.getVariant() == -1) index |= 4;
        if (TEXTURES[index] == null)
        {
            String path = FOLDER + "dragon_fruit_drake/";
            path += ((index & 2) != 0)? "child" : ((index & 1) != 0)? "female" : "male";
            if ((index & 4) != 0) path += "_spe";
            return TEXTURES[index] = Wyrmroost.id(path + "_leaves.png");
        }
        return TEXTURES[index];
    }

    public ResourceLocation getBodyTexture(DragonFruitDrakeEntity entity)
    {
        return entity.isHatchling()? GRAY_SCALE_CHILD : GRAY_SCALE;
    }

    public static int getColorByAge(DragonFruitDrakeEntity entity)
    {
        int to = entity.getVariant() == -1? 0xD08C21 : 0xFF0054;
        return lerp(entity.ageProgress(), 0x23731C, to);
    }

    private static int lerp(float amount, int a, int b)
    {
        int ar = a >> 16;
        int ag = a >> 8 & 0xff;
        int ab = a & 0xff;

        int br = b >> 16;
        int bg = b >> 8 & 0xff;
        int bb = b & 0xff;

        int rr = (int) (ar + amount * (br - ar));
        int rg = (int) (ag + amount * (bg - ag));
        int rb = (int) (ab + amount * (bb - ab));

        return (rr << 16) + (rg << 8) + (rb);
    }

    @Override
    public float getShadowRadius(DragonFruitDrakeEntity entity)
    {
        return 1.15f;
    }

    @Override
    public void setupAnim(DragonFruitDrakeEntity entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_)
    {
        reset();
        animator().tick(entity, this, partialTicks);
    }

    public void biteAnimation()
    {

    }
}
