package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import net.minecraft.entity.Entity;

import java.util.Random;

/**
 * WRSilverGlider - Kingdomall
 * Created using Tabula 7.0.1
 */
public class SilverGliderModel extends AdvancedEntityModel {
    public AdvancedRendererModel MainBody;
    public AdvancedRendererModel SecondaryBody;
    public AdvancedRendererModel Neck1;
    public AdvancedRendererModel BackFin;
    public AdvancedRendererModel BottomFin;
    public AdvancedRendererModel WingSegment1L;
    public AdvancedRendererModel WingSegment1R;
    public AdvancedRendererModel Tail1;
    public AdvancedRendererModel LegL1;
    public AdvancedRendererModel LegR1;
    public AdvancedRendererModel Tail2;
    public AdvancedRendererModel Tail3;
    public AdvancedRendererModel Tail4;
    public AdvancedRendererModel Tail5;
    public AdvancedRendererModel Tail6;
    public AdvancedRendererModel Tail7;
    public AdvancedRendererModel Tail8;
    public AdvancedRendererModel Tail9;
    public AdvancedRendererModel Tail10;
    public AdvancedRendererModel TailFin;
    public AdvancedRendererModel LegL2;
    public AdvancedRendererModel LegL3;
    public AdvancedRendererModel toe2L;
    public AdvancedRendererModel toe1L;
    public AdvancedRendererModel toe3L;
    public AdvancedRendererModel LegR2;
    public AdvancedRendererModel LegR3;
    public AdvancedRendererModel toe2R;
    public AdvancedRendererModel toe1L_1;
    public AdvancedRendererModel toe1L_2;
    public AdvancedRendererModel Neck2;
    public AdvancedRendererModel Neck3;
    public AdvancedRendererModel Neck4;
    public AdvancedRendererModel Head;
    public AdvancedRendererModel MouthBottom;
    public AdvancedRendererModel MouthTop;
    public AdvancedRendererModel EyeR;
    public AdvancedRendererModel EyeL;
    public AdvancedRendererModel WingSegment2L;
    public AdvancedRendererModel Membrane1L;
    public AdvancedRendererModel Membrane2L;
    public AdvancedRendererModel WingSegment3L;
    public AdvancedRendererModel Membrane3L;
    public AdvancedRendererModel WingSegment2R;
    public AdvancedRendererModel Membrane1R;
    public AdvancedRendererModel Membrane2R;
    public AdvancedRendererModel WingSegment3R;
    public AdvancedRendererModel Membrane3R;

    public AdvancedRendererModel[] tailArray;
    public AdvancedRendererModel[] neckArray;
    public AdvancedRendererModel[] neck2Array;
    public AdvancedRendererModel[] toes;

    private float netHeadYaw = 0;
    private float headPitch = 0;

    public SilverGliderModel() {
        this.textureWidth = 160;
        this.textureHeight = 125;
        this.Membrane1L = new AdvancedRendererModel(this, -20, 80);
        this.Membrane1L.setRotationPoint(0.0F, 0.7F, 0.1F);
        this.Membrane1L.addBox(0.0F, -0.9F, -0.5F, 20, 0, 20, 0.0F);
        this.MouthBottom = new AdvancedRendererModel(this, 84, 10);
        this.MouthBottom.setRotationPoint(0.0F, 1.0F, -4.5F);
        this.MouthBottom.addBox(-1.5F, -0.5F, -4.0F, 3, 1, 4, 0.0F);
        this.Tail2 = new AdvancedRendererModel(this, 88, 53);
        this.Tail2.setRotationPoint(-0.02F, -0.02F, 2.8F);
        this.Tail2.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.toe1L = new AdvancedRendererModel(this, 60, 20);
        this.toe1L.setRotationPoint(0.5F, 3.0F, 0.2F);
        this.toe1L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe1L, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        this.LegL2 = new AdvancedRendererModel(this, 56, 8);
        this.LegL2.setRotationPoint(0.38F, 3.1F, -0.1F);
        this.LegL2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(LegL2, 1.360135086079181F, 0.0F, 0.0F);
        this.LegR2 = new AdvancedRendererModel(this, 56, 8);
        this.LegR2.setRotationPoint(0.42F, 3.1F, -0.1F);
        this.LegR2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(LegR2, 1.360135086079181F, 0.0F, 0.0F);
        this.Neck2 = new AdvancedRendererModel(this, 88, 17);
        this.Neck2.setRotationPoint(0.02F, 0.02F, -2.0F);
        this.Neck2.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.Membrane1R = new AdvancedRendererModel(this, -20, 101);
        this.Membrane1R.mirror = true;
        this.Membrane1R.setRotationPoint(0.0F, 0.7F, 0.1F);
        this.Membrane1R.addBox(-20.0F, -0.9F, -0.5F, 20, 0, 20, 0.0F);
        this.WingSegment2L = new AdvancedRendererModel(this, 0, 55);
        this.WingSegment2L.setRotationPoint(17.3F, -0.2F, 0.1F);
        this.WingSegment2L.addBox(0.0F, -0.9F, -0.9F, 30, 2, 2, 0.0F);
        this.TailFin = new AdvancedRendererModel(this, 18, 30);
        this.TailFin.setRotationPoint(0.0F, 0.0F, 1.6F);
        this.TailFin.addBox(-5.0F, 0.0F, -1.6F, 10, 0, 12, 0.0F);
        this.toe3L = new AdvancedRendererModel(this, 60, 20);
        this.toe3L.setRotationPoint(-0.5F, 3.0F, 0.2F);
        this.toe3L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe3L, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        this.SecondaryBody = new AdvancedRendererModel(this, 76, 40);
        this.SecondaryBody.setRotationPoint(0.0F, -0.3F, 4.9F);
        this.SecondaryBody.addBox(-2.1F, -2.1F, -0.5F, 4, 4, 8, 0.0F);
        this.LegL3 = new AdvancedRendererModel(this, 58, 14);
        this.LegL3.setRotationPoint(0.02F, 2.5F, 0.1F);
        this.LegL3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        this.setRotateAngle(LegL3, -1.3632766787327708F, 0.0F, 0.0F);
        this.Neck1 = new AdvancedRendererModel(this, 88, 17);
        this.Neck1.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.Neck1.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.Tail10 = new AdvancedRendererModel(this, 90, 60);
        this.Tail10.setRotationPoint(0.02F, 0.02F, 2.0F);
        this.Tail10.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.Tail6 = new AdvancedRendererModel(this, 90, 60);
        this.Tail6.setRotationPoint(0.02F, 0.02F, 2.0F);
        this.Tail6.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.Tail8 = new AdvancedRendererModel(this, 90, 60);
        this.Tail8.setRotationPoint(0.02F, 0.02F, 2.0F);
        this.Tail8.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.MainBody = new AdvancedRendererModel(this, 70, 24);
        this.MainBody.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.MainBody.addBox(-2.5F, -2.5F, -5.0F, 5, 5, 10, 0.0F);
        this.Membrane2L = new AdvancedRendererModel(this, 19, 80);
        this.Membrane2L.setRotationPoint(0.0F, 0.8F, 0.1F);
        this.Membrane2L.addBox(0.0F, -0.9F, -0.5F, 30, 0, 20, 0.0F);
        this.toe1L_2 = new AdvancedRendererModel(this, 60, 20);
        this.toe1L_2.setRotationPoint(-0.5F, 3.0F, 0.2F);
        this.toe1L_2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe1L_2, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        this.WingSegment1R = new AdvancedRendererModel(this, 0, 60);
        this.WingSegment1R.setRotationPoint(-1.5F, -0.7F, -3.3F);
        this.WingSegment1R.addBox(-18.0F, -1.5F, -1.3F, 18, 3, 3, 0.0F);
        this.LegL1 = new AdvancedRendererModel(this, 56, 0);
        this.LegL1.setRotationPoint(1.6F, 0.9F, 5.9F);
        this.LegL1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        this.setRotateAngle(LegL1, -0.5918411493512771F, 0.0F, 0.0F);
        this.Tail1 = new AdvancedRendererModel(this, 88, 53);
        this.Tail1.setRotationPoint(0.02F, 0.02F, 7.0F);
        this.Tail1.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.Tail3 = new AdvancedRendererModel(this, 88, 53);
        this.Tail3.setRotationPoint(0.02F, 0.02F, 2.8F);
        this.Tail3.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.EyeR = new AdvancedRendererModel(this, 79, 2);
        this.EyeR.setRotationPoint(1.7F, -0.5F, -3.9F);
        this.EyeR.addBox(0.0F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(EyeR, 0.0F, 0.17453292519943295F, 0.0F);
        this.Head = new AdvancedRendererModel(this, 80, 0);
        this.Head.setRotationPoint(0.0F, -0.1F, -2.0F);
        this.Head.addBox(-2.4F, -2.0F, -4.8F, 5, 4, 5, 0.0F);
        this.Membrane2R = new AdvancedRendererModel(this, 19, 101);
        this.Membrane2R.mirror = true;
        this.Membrane2R.setRotationPoint(0.0F, 0.8F, 0.1F);
        this.Membrane2R.addBox(-30.0F, -0.9F, -0.5F, 30, 0, 20, 0.0F);
        this.Tail4 = new AdvancedRendererModel(this, 88, 53);
        this.Tail4.setRotationPoint(-0.02F, -0.02F, 2.8F);
        this.Tail4.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.WingSegment1L = new AdvancedRendererModel(this, 0, 60);
        this.WingSegment1L.setRotationPoint(1.5F, -0.7F, -3.3F);
        this.WingSegment1L.addBox(0.0F, -1.5F, -1.3F, 18, 3, 3, 0.0F);
        this.Neck4 = new AdvancedRendererModel(this, 88, 17);
        this.Neck4.setRotationPoint(0.02F, 0.02F, -2.0F);
        this.Neck4.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.BottomFin = new AdvancedRendererModel(this, 0, -3);
        this.BottomFin.setRotationPoint(0.0F, 1.4F, 2.1F);
        this.BottomFin.addBox(0.0F, -0.1F, -6.4F, 0, 5, 12, 0.0F);
        this.Tail5 = new AdvancedRendererModel(this, 90, 60);
        this.Tail5.setRotationPoint(-0.1F, -0.1F, 2.0F);
        this.Tail5.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.MouthTop = new AdvancedRendererModel(this, 67, 10);
        this.MouthTop.setRotationPoint(0.0F, -0.7F, -4.5F);
        this.MouthTop.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0.0F);
        this.setRotateAngle(MouthTop, 0.31869712141416456F, 0.0F, 0.0F);
        this.LegR3 = new AdvancedRendererModel(this, 58, 14);
        this.LegR3.setRotationPoint(-0.02F, 2.5F, 0.1F);
        this.LegR3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        this.setRotateAngle(LegR3, -1.3632766787327708F, 0.0F, 0.0F);
        this.WingSegment3L = new AdvancedRendererModel(this, 0, 51);
        this.WingSegment3L.setRotationPoint(29.5F, 0.1F, 0.1F);
        this.WingSegment3L.addBox(0.0F, -0.6F, -0.9F, 31, 1, 2, 0.0F);
        this.setRotateAngle(WingSegment3L, 0.0F, -0.08726646259971647F, 0.0F);
        this.WingSegment3R = new AdvancedRendererModel(this, 0, 51);
        this.WingSegment3R.setRotationPoint(-29.5F, 0.1F, 0.1F);
        this.WingSegment3R.addBox(-31.0F, -0.6F, -0.9F, 31, 1, 2, 0.0F);
        this.setRotateAngle(WingSegment3R, 0.0F, 0.08726646259971647F, 0.0F);
        this.BackFin = new AdvancedRendererModel(this, 0, -14);
        this.BackFin.setRotationPoint(0.0F, -1.8F, 2.2F);
        this.BackFin.addBox(0.0F, -7.6F, -6.9F, 0, 8, 14, 0.0F);
        this.Membrane3L = new AdvancedRendererModel(this, 80, 80);
        this.Membrane3L.setRotationPoint(15.5F, -0.2F, 0.2F);
        this.Membrane3L.addBox(-15.0F, -0.1F, -0.7F, 30, 0, 20, 0.0F);
        this.Membrane3R = new AdvancedRendererModel(this, 80, 101);
        this.Membrane3R.mirror = true;
        this.Membrane3R.setRotationPoint(-15.5F, -0.2F, 0.2F);
        this.Membrane3R.addBox(-15.0F, -0.1F, -0.7F, 30, 0, 20, 0.0F);
        this.Neck3 = new AdvancedRendererModel(this, 88, 17);
        this.Neck3.setRotationPoint(-0.02F, -0.02F, -2.0F);
        this.Neck3.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.WingSegment2R = new AdvancedRendererModel(this, 0, 55);
        this.WingSegment2R.setRotationPoint(-17.3F, -0.2F, 0.1F);
        this.WingSegment2R.addBox(-30.0F, -0.9F, -0.9F, 30, 2, 2, 0.0F);
        this.toe2L = new AdvancedRendererModel(this, 60, 20);
        this.toe2L.setRotationPoint(0.0F, 3.0F, 0.2F);
        this.toe2L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe2L, -0.9560913642424937F, 0.0F, 0.0F);
        this.toe1L_1 = new AdvancedRendererModel(this, 60, 20);
        this.toe1L_1.setRotationPoint(0.5F, 3.0F, 0.2F);
        this.toe1L_1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe1L_1, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        this.Tail7 = new AdvancedRendererModel(this, 90, 60);
        this.Tail7.setRotationPoint(-0.02F, -0.02F, 2.0F);
        this.Tail7.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.Tail9 = new AdvancedRendererModel(this, 90, 60);
        this.Tail9.setRotationPoint(-0.02F, -0.02F, 2.0F);
        this.Tail9.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.LegR1 = new AdvancedRendererModel(this, 56, 0);
        this.LegR1.setRotationPoint(-2.3F, 0.9F, 5.9F);
        this.LegR1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        this.setRotateAngle(LegR1, -0.5918411493512771F, 0.0F, 0.0F);
        this.toe2R = new AdvancedRendererModel(this, 60, 20);
        this.toe2R.setRotationPoint(0.0F, 3.0F, 0.2F);
        this.toe2R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe2R, -0.9560913642424937F, 0.0F, 0.0F);
        this.EyeL = new AdvancedRendererModel(this, 79, 2);
        this.EyeL.setRotationPoint(-1.8F, -0.5F, -3.9F);
        this.EyeL.addBox(-0.7F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(EyeL, 0.0F, -0.17453292519943295F, 0.0F);
        this.WingSegment1L.addChild(this.Membrane1L);
        this.Head.addChild(this.MouthBottom);
        this.Tail1.addChild(this.Tail2);
        this.LegL3.addChild(this.toe1L);
        this.LegL1.addChild(this.LegL2);
        this.LegR1.addChild(this.LegR2);
        this.Neck1.addChild(this.Neck2);
        this.WingSegment1R.addChild(this.Membrane1R);
        this.WingSegment1L.addChild(this.WingSegment2L);
        this.Tail10.addChild(this.TailFin);
        this.LegL3.addChild(this.toe3L);
        this.MainBody.addChild(this.SecondaryBody);
        this.LegL2.addChild(this.LegL3);
        this.MainBody.addChild(this.Neck1);
        this.Tail9.addChild(this.Tail10);
        this.Tail5.addChild(this.Tail6);
        this.Tail7.addChild(this.Tail8);
        this.WingSegment2L.addChild(this.Membrane2L);
        this.LegR3.addChild(this.toe1L_2);
        this.MainBody.addChild(this.WingSegment1R);
        this.SecondaryBody.addChild(this.LegL1);
        this.SecondaryBody.addChild(this.Tail1);
        this.Tail2.addChild(this.Tail3);
        this.Head.addChild(this.EyeR);
        this.Neck4.addChild(this.Head);
        this.WingSegment2R.addChild(this.Membrane2R);
        this.Tail3.addChild(this.Tail4);
        this.MainBody.addChild(this.WingSegment1L);
        this.Neck3.addChild(this.Neck4);
        this.MainBody.addChild(this.BottomFin);
        this.Tail4.addChild(this.Tail5);
        this.Head.addChild(this.MouthTop);
        this.LegR2.addChild(this.LegR3);
        this.WingSegment2L.addChild(this.WingSegment3L);
        this.WingSegment2R.addChild(this.WingSegment3R);
        this.MainBody.addChild(this.BackFin);
        this.WingSegment3L.addChild(this.Membrane3L);
        this.WingSegment3R.addChild(this.Membrane3R);
        this.Neck2.addChild(this.Neck3);
        this.WingSegment1R.addChild(this.WingSegment2R);
        this.LegL3.addChild(this.toe2L);
        this.LegR3.addChild(this.toe1L_1);
        this.Tail6.addChild(this.Tail7);
        this.Tail8.addChild(this.Tail9);
        this.SecondaryBody.addChild(this.LegR1);
        this.LegR3.addChild(this.toe2R);
        this.Head.addChild(this.EyeL);

        tailArray = new AdvancedRendererModel[] {Tail1, Tail2, Tail3, Tail4, Tail5, Tail6, Tail7, Tail8, Tail9, Tail10};
        neckArray = new AdvancedRendererModel[] {Neck1, Neck2, Neck3, Neck4, Head};
        neck2Array = new AdvancedRendererModel[] {Neck1, Neck2};
        toes = new AdvancedRendererModel[] {toe1L, toe1L_1, toe1L_2, toe2L, toe2R, toe3L};

        updateDefaultPose();
    }


    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.MainBody.render(scale);
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
    }

    @Override
    public void setLivingAnimations(Entity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        SilverGliderEntity entity = (SilverGliderEntity) entityIn;
        float frame = entity.ticksExisted;
        float globalSpeed = 0.5f;
        float f = 0.5f;

        resetToDefaultPose();
        faceTarget(netHeadYaw, headPitch, 1f, neckArray);

        if (entity.isFlying() && !entity.isGliding && entity.getAnimation() == AbstractDragonEntity.NO_ANIMATION) {
            LegL1.rotateAngleX = 0.5f;
            LegL3.rotateAngleX = -0.7f;

//            LegR1.rotateAngleX = 0.5f;

            walk(WingSegment1L, globalSpeed, -0.25f, false, 0, -0.25f, frame, f);
            flap(WingSegment1L, globalSpeed, 1.3f, false, 0, 0, frame, f);
            flap(WingSegment2L, globalSpeed, 1.1f, false, -0.5f, 0.2f, frame, f);
            flap(WingSegment3L, globalSpeed, 0.6f, false, -0.8f, 0, frame, f);

            walk(WingSegment1R, globalSpeed, -0.25f, false, 0, -0.25f, frame, f);
            flap(WingSegment1R, globalSpeed, 1.3f, true, 0, 0, frame, f);
            flap(WingSegment2R, globalSpeed, 1.1f, true, -0.5f, 0.2f, frame, f);
            flap(WingSegment3R, globalSpeed, 0.6f, true, -0.8f, 0, frame, f);

            chainWave(tailArray, globalSpeed - 0.25f, 0.06f, 2.5, frame, f);

            chainWave(neck2Array, globalSpeed - 0.2f, 0.02f, 0, frame, f);
            walk(Head, globalSpeed - 0.2f, 0.05f, false, 0.9f, 0, frame, f);
            walk(MouthBottom, globalSpeed - 0.2f, 0.3f, false, 0, 0.5f, frame, f);

            return;
        }
        if (entity.isGliding) {
            Random rand = new Random();

            MainBody.rotateAngleX = (float) -entity.getLookVec().y;

            if (entity.isDiving) {
/*                WingSegment1L.rotateAngleX = -0.2f;
                WingSegment1L.rotateAngleY = -0.4f;
                WingSegment1L.rotateAngleZ = -0.1f;
                WingSegment2L.rotateAngleY = -0.4f;
                WingSegment3L.rotateAngleY = -0.4f;*/

/*                progressRotation(WingSegment1L, 0.5f, -0.2f, -0.4f, -0.1f, 2f);
                progressRotation(WingSegment2L, 0.5f, 0, -0.4f, 0, 1);
                progressRotation(WingSegment3L, 0.5f, 0, -0.4f, 0, 1);*/

                WingSegment1R.rotateAngleX = -0.2f;
                WingSegment1R.rotateAngleY = 0.4f;
                WingSegment1R.rotateAngleZ = 0.1f;
                WingSegment2R.rotateAngleY = 0.4f;
                WingSegment3R.rotateAngleY = 0.4f;
            }

            walk(Membrane1L, globalSpeed + rand.nextFloat(), rand.nextFloat(), false, 0, 0, frame, 0.013f);
            walk(Membrane2L, globalSpeed + rand.nextFloat(), rand.nextFloat(), false, 0, 0, frame, 0.013f);
            walk(Membrane3L, globalSpeed + rand.nextFloat(), rand.nextFloat(), false, 0, 0, frame, 0.013f);

            walk(Membrane1R, globalSpeed + rand.nextFloat(), rand.nextFloat(), false, 0, 0, frame, 0.013f);
            walk(Membrane2R, globalSpeed + rand.nextFloat(), rand.nextFloat(), false, 0, 0, frame, 0.013f);
            walk(Membrane3R, globalSpeed + rand.nextFloat(), rand.nextFloat(), false, 0, 0, frame, 0.013f);

            chainWave(neck2Array, globalSpeed - 0.2f, 0.02f, 0, frame, f);
            walk(Head, globalSpeed - 0.2f, 0.05f, false, 0.9f, 0, frame, f);
            walk(MouthBottom, globalSpeed - 0.2f, 0.3f, false, 0, 0.5f, frame, f);

            chainWave(tailArray, globalSpeed - 0.25f, 0.06f, 2.5, frame, f);

            return;
        }

        // IDLE:
        chainWave(neck2Array, globalSpeed - 0.4f, 0.02f, 0, frame, f);
        walk(Head, globalSpeed - 0.4f, 0.05f, false, 0.9f, 0, frame, f);
        walk(MouthBottom, globalSpeed - 0.4f, 0.3f, false, 0, 0.5f, frame, f);
        chainSwing(tailArray, globalSpeed - 0.45f, 0.03f, 0, frame, f);
        chainWave(tailArray, globalSpeed - 0.46f, 0.06f, 0, frame, f);
    }
}
