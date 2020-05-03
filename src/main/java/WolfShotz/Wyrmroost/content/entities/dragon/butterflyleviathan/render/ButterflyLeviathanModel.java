package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.ModelAnimator;
import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedLivingEntityModel;
import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedRendererModel;
import WolfShotz.Wyrmroost.util.entityutils.client.model.SocketRendererModel;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.tags.FluidTags;
import org.apache.commons.lang3.ArrayUtils;

/**
 * butterfly leviathan - Kingdomall
 * Created using Tabula 7.0.1
 */
public class ButterflyLeviathanModel extends AdvancedLivingEntityModel<ButterflyLeviathanEntity>
{
    public AdvancedRendererModel body1;
    public AdvancedRendererModel body2;
    public AdvancedRendererModel neck1;
    public AdvancedRendererModel topWingFinPhalangeR1;
    public AdvancedRendererModel bottomWingFinPhalangeR1;
    public AdvancedRendererModel topWingFinPhalangeL1;
    public AdvancedRendererModel bottomWingFinPhalangeL1;
    public SocketRendererModel tail1;
    public AdvancedRendererModel legThighR1;
    public AdvancedRendererModel legThighL1;
    public SocketRendererModel tail2;
    public SocketRendererModel tail3;
    public SocketRendererModel tail4;
    public SocketRendererModel tail5;
    public SocketRendererModel tail6;
    public AdvancedRendererModel tailFinTop;
    public AdvancedRendererModel tailFinBottom;
    public AdvancedRendererModel legSegmentR1;
    public AdvancedRendererModel legSegmentR2;
    public AdvancedRendererModel footR;
    public AdvancedRendererModel legSegmentL1;
    public AdvancedRendererModel legSegmentL2;
    public AdvancedRendererModel footL;
    public AdvancedRendererModel neck2;
    public AdvancedRendererModel neck3;
    public SocketRendererModel head;
    public AdvancedRendererModel mouthTop;
    public AdvancedRendererModel mouthBottom;
    public AdvancedRendererModel headFrillR;
    public AdvancedRendererModel headFrillL;
    public AdvancedRendererModel eyeR;
    public AdvancedRendererModel eyeL;
    public AdvancedRendererModel teethTop;
    public AdvancedRendererModel teethBottom;
    public AdvancedRendererModel topWingFinPhalangeR2;
    public AdvancedRendererModel topWingFinMembraneR1;
    public AdvancedRendererModel topWingFinMembraneR2;
    public AdvancedRendererModel topWingFinMembraneR3;
    public AdvancedRendererModel bottomWingFinPhalangeR2;
    public AdvancedRendererModel bottomWingFinMembraneR1;
    public AdvancedRendererModel bottomWingFinMembraneR2;
    public AdvancedRendererModel bottomWingFinMembraneR3;
    public AdvancedRendererModel topWingFinPhalangeL2;
    public AdvancedRendererModel topWingFinMembraneL1;
    public AdvancedRendererModel topWingFinPhalangeL2_1;
    public AdvancedRendererModel topWingFinMembrane3L;
    public AdvancedRendererModel bottomWingFinPhalangeL2;
    public AdvancedRendererModel bottomWingFinMembraneL1;
    public AdvancedRendererModel bottomWingFinMembraneL2;
    public AdvancedRendererModel bottomWingFinMembraneL3;

    public SocketRendererModel[] tailArray;
    public SocketRendererModel[] dynamicTailArray;
    private final AdvancedRendererModel[] neckArray;
    private final AdvancedRendererModel[] headArray;
    
    public ModelAnimator animator;
    
    public ButterflyLeviathanModel()
    {
        textureWidth = 150;
        textureHeight = 250;
        tail4 = new SocketRendererModel(this, 82, 129);
        tail4.setRotationPoint(0.0F, -0.1F, 10.2F);
        tail4.addBox(-3.0F, -3.7F, -2.2F, 6, 8, 13, 0.0F);
        setRotateAngle(tail4, 0.0F, 0.22235494670407757F, 0.0F);
        tail1 = new SocketRendererModel(this, 106, 57);
        tail1.setRotationPoint(0.0F, -2.0F, 10.2F);
        tail1.addBox(-4.5F, -4.1F, -2.2F, 9, 11, 13, 0.0F);
        setRotateAngle(tail1, 0.0F, 0.08726646259971647F, 0.0F);
        headFrillR = new AdvancedRendererModel(this, 31, 12);
        headFrillR.setRotationPoint(3.2F, -4.0F, -3.1F);
        headFrillR.addBox(0.1F, -4.3F, -4.5F, 0, 5, 9, 0.0F);
        setRotateAngle(headFrillR, 0.0F, 0.0F, 0.875631685725555F);
        headFrillL = new AdvancedRendererModel(this, 31, 12);
        headFrillL.setRotationPoint(-3.4F, -4.0F, -3.1F);
        headFrillL.addBox(0.0F, -4.3F, -4.5F, 0, 5, 9, 0.0F);
        setRotateAngle(headFrillL, 0.0F, 0.0F, -0.875631685725555F);
        legThighR1 = new AdvancedRendererModel(this, 73, 21);
        legThighR1.setRotationPoint(3.1F, 2.1F, 7.7F);
        legThighR1.addBox(0.0F, -1.4F, -2.0F, 3, 7, 4, 0.0F);
        setRotateAngle(legThighR1, 0.4836068585593608F, 0.0F, 0.0F);
        bottomWingFinMembraneR3 = new AdvancedRendererModel(this, 67, 200);
        bottomWingFinMembraneR3.setRotationPoint(11.0F, 0.0F, 0.3F);
        bottomWingFinMembraneR3.addBox(0.0F, 0.0F, -0.2F, 11, 0, 8, 0.0F);
        setRotateAngle(bottomWingFinMembraneR3, 0.0F, 0.0F, -0.17453292519943295F);
        tail5 = new SocketRendererModel(this, 44, 130);
        tail5.setRotationPoint(0.1F, 0.3F, 10.2F);
        tail5.addBox(-2.6F, -3.9F, -2.2F, 5, 7, 13, 0.0F);
        setRotateAngle(tail5, 0.0F, 0.2501056818107874F, 0.0F);
        bottomWingFinPhalangeL2 = new AdvancedRendererModel(this, 32, 187);
        bottomWingFinPhalangeL2.setRotationPoint(-11.0F, -0.1F, -0.4F);
        bottomWingFinPhalangeL2.addBox(-13.8F, -1.1F, -0.9F, 14, 2, 2, 0.0F);
        setRotateAngle(bottomWingFinPhalangeL2, 0.0F, 0.0F, 0.3490658503988659F);
        topWingFinPhalangeL1 = new AdvancedRendererModel(this, 113, 155);
        topWingFinPhalangeL1.setRotationPoint(-5.0F, -3.7F, -1.9F);
        topWingFinPhalangeL1.addBox(0.0F, -1.1F, -1.5F, 14, 2, 3, 0.0F);
        setRotateAngle(topWingFinPhalangeL1, 0.0F, 3.141592653589793F, 0.3490658503988659F);
        topWingFinMembraneR2 = new AdvancedRendererModel(this, 84, 162);
        topWingFinMembraneR2.setRotationPoint(6.0F, 0.0F, 0.4F);
        topWingFinMembraneR2.addBox(-6.0F, 0.0F, -0.8F, 13, 0, 11, 0.0F);
        topWingFinPhalangeL2 = new AdvancedRendererModel(this, 80, 155);
        topWingFinPhalangeL2.setRotationPoint(13.1F, -0.1F, 0.2F);
        topWingFinPhalangeL2.addBox(-0.1F, -1.1F, -0.9F, 14, 2, 2, 0.0F);
        setRotateAngle(topWingFinPhalangeL2, 0.0F, 0.0F, 0.3490658503988659F);
        topWingFinMembrane3L = new AdvancedRendererModel(this, 50, 173);
        topWingFinMembrane3L.setRotationPoint(13.0F, 0.0F, 0.1F);
        topWingFinMembrane3L.addBox(0.0F, 0.0F, -9.4F, 13, 0, 10, 0.0F);
        setRotateAngle(topWingFinMembrane3L, 0.0F, 0.0F, -0.2617993877991494F);
        tail6 = new SocketRendererModel(this, 6, 130);
        tail6.setRotationPoint(0.1F, 0.1F, 10.0F);
        tail6.addBox(-2.2F, -3.9F, -2.2F, 4, 6, 14, 0.0F);
        setRotateAngle(tail6, 0.0F, 0.16667894356545848F, 0.0F);
        footL = new AdvancedRendererModel(this, 74, 56);
        footL.setRotationPoint(0.0F, 3.7F, 0.0F);
        footL.addBox(-1.5F, -0.3F, -1.1F, 3, 3, 2, 0.0F);
        setRotateAngle(footL, 0.5337216852598659F, 0.0F, 0.0F);
        topWingFinMembraneR3 = new AdvancedRendererModel(this, 58, 162);
        topWingFinMembraneR3.setRotationPoint(13.0F, 0.0F, 0.1F);
        topWingFinMembraneR3.addBox(0.0F, 0.0F, -0.5F, 13, 0, 10, 0.0F);
        setRotateAngle(topWingFinMembraneR3, 0.0F, 0.0F, -0.2617993877991494F);
        bottomWingFinPhalangeR1 = new AdvancedRendererModel(this, 0, 187);
        bottomWingFinPhalangeR1.setRotationPoint(5.1F, -0.8F, -1.9F);
        bottomWingFinPhalangeR1.addBox(0.0F, -1.1F, -1.5F, 12, 2, 3, 0.0F);
        setRotateAngle(bottomWingFinPhalangeR1, 0.0F, 0.0F, 0.3490658503988659F);
        legThighL1 = new AdvancedRendererModel(this, 73, 21);
        legThighL1.setRotationPoint(-3.9F, 2.1F, 7.7F);
        legThighL1.addBox(-2.2F, -1.4F, -2.0F, 3, 7, 4, 0.0F);
        setRotateAngle(legThighL1, 0.5687630079445055F, 0.0F, 0.0F);
        neck3 = new AdvancedRendererModel(this, 0, 0);
        neck3.setRotationPoint(0.0F, 0.0F, -4.7F);
        neck3.addBox(-3.0F, -3.9F, -7.1F, 6, 8, 8, 0.0F);
        legSegmentL1 = new AdvancedRendererModel(this, 75, 36);
        legSegmentL1.setRotationPoint(-0.6F, 4.2F, -0.3F);
        legSegmentL1.addBox(-1.5F, 0.1F, -1.6F, 3, 4, 3, 0.0F);
        setRotateAngle(legSegmentL1, 0.9261035373325488F, 0.0F, 0.0F);
        tail3 = new SocketRendererModel(this, 108, 107);
        tail3.setRotationPoint(0.0F, 0.1F, 10.2F);
        tail3.addBox(-3.5F, -3.9F, -2.2F, 7, 9, 13, 0.0F);
        setRotateAngle(tail3, 0.0F, -0.1424188669627373F, 0.0F);
        topWingFinPhalangeL2_1 = new AdvancedRendererModel(this, 21, 173);
        topWingFinPhalangeL2_1.setRotationPoint(5.8F, 0.0F, 0.4F);
        topWingFinPhalangeL2_1.addBox(-5.8F, 0.0F, -10.7F, 13, 0, 11, 0.0F);
        teethBottom = new AdvancedRendererModel(this, 29, 55);
        teethBottom.setRotationPoint(0.0F, -1.6F, -5.9F);
        teethBottom.addBox(-2.5F, -0.7F, -0.7F, 5, 1, 8, 0.0F);
        bottomWingFinMembraneL2 = new AdvancedRendererModel(this, 22, 210);
        bottomWingFinMembraneL2.setRotationPoint(-5.3F, 0.0F, 0.4F);
        bottomWingFinMembraneL2.addBox(-5.7F, 0.0F, -0.8F, 11, 0, 9, 0.0F);
        body2 = new AdvancedRendererModel(this, 102, 30);
        body2.setRotationPoint(0.0F, 0.1F, 10.2F);
        body2.addBox(-5.0F, -6.3F, -2.2F, 10, 12, 13, 0.0F);
        footR = new AdvancedRendererModel(this, 74, 56);
        footR.setRotationPoint(0.0F, 3.7F, 0.0F);
        footR.addBox(-1.5F, -0.3F, -1.1F, 3, 3, 2, 0.0F);
        setRotateAngle(footR, 0.5337216852598659F, 0.0F, 0.0F);
        legSegmentR1 = new AdvancedRendererModel(this, 75, 36);
        legSegmentR1.setRotationPoint(1.4F, 4.2F, -0.3F);
        legSegmentR1.addBox(-1.5F, 0.1F, -1.6F, 3, 4, 3, 0.0F);
        setRotateAngle(legSegmentR1, 1.0112596867176926F, 0.0F, 0.0F);
        topWingFinMembraneL1 = new AdvancedRendererModel(this, -10, 173);
        topWingFinMembraneL1.setRotationPoint(4.8F, -0.1F, -0.4F);
        topWingFinMembraneL1.addBox(-5.7F, 0.0F, -10.3F, 14, 0, 12, 0.0F);
        topWingFinMembraneR1 = new AdvancedRendererModel(this, 110, 162);
        topWingFinMembraneR1.setRotationPoint(4.6F, -0.1F, -0.4F);
        topWingFinMembraneR1.addBox(-5.6F, 0.0F, -0.8F, 14, 0, 12, 0.0F);
        tail2 = new SocketRendererModel(this, 106, 82);
        tail2.setRotationPoint(0.0F, 0.0F, 10.2F);
        tail2.addBox(-4.0F, -3.9F, -2.2F, 8, 10, 13, 0.0F);
        setRotateAngle(tail2, 0.0F, -0.08726646259971647F, 0.0F);
        bottomWingFinMembraneR1 = new AdvancedRendererModel(this, 115, 200);
        bottomWingFinMembraneR1.setRotationPoint(4.7F, -0.1F, -0.4F);
        bottomWingFinMembraneR1.addBox(-5.7F, 0.0F, -0.8F, 12, 0, 10, 0.0F);
        tailFinBottom = new AdvancedRendererModel(this, 0, 108);
        tailFinBottom.setRotationPoint(-0.2F, 1.6F, 5.4F);
        tailFinBottom.addBox(0.0F, -0.5F, -6.2F, 0, 5, 16, 0.0F);
        bottomWingFinPhalangeR2 = new AdvancedRendererModel(this, 32, 187);
        bottomWingFinPhalangeR2.setRotationPoint(11.0F, -0.1F, -0.4F);
        bottomWingFinPhalangeR2.addBox(0.0F, -1.1F, -0.9F, 14, 2, 2, 0.0F);
        setRotateAngle(bottomWingFinPhalangeR2, 0.0F, 0.0F, -0.3490658503988659F);
        legSegmentL2 = new AdvancedRendererModel(this, 76, 46);
        legSegmentL2.setRotationPoint(0.1F, 3.7F, 0.1F);
        legSegmentL2.addBox(-1.5F, -0.8F, -1.0F, 3, 5, 2, 0.0F);
        setRotateAngle(legSegmentL2, -0.5330235535590682F, 0.0F, 0.0F);
        legSegmentR2 = new AdvancedRendererModel(this, 76, 46);
        legSegmentR2.setRotationPoint(-0.1F, 3.7F, 0.1F);
        legSegmentR2.addBox(-1.5F, -0.8F, -1.0F, 3, 5, 2, 0.0F);
        setRotateAngle(legSegmentR2, -0.5330235535590682F, 0.0F, 0.0F);
        neck1 = new AdvancedRendererModel(this, 62, 0);
        neck1.setRotationPoint(0.0F, -1.5F, -2.9F);
        neck1.addBox(-4.0F, -4.1F, -7.1F, 8, 10, 8, 0.0F);
        neck2 = new AdvancedRendererModel(this, 30, 0);
        neck2.setRotationPoint(0.0F, 0.0F, -4.7F);
        neck2.addBox(-3.5F, -4.0F, -7.1F, 7, 9, 8, 0.0F);
        tailFinTop = new AdvancedRendererModel(this, 0, 97);
        tailFinTop.setRotationPoint(-0.2F, -3.4F, 5.4F);
        tailFinTop.addBox(0.0F, -6.8F, -6.2F, 0, 7, 16, 0.0F);
        bottomWingFinMembraneL3 = new AdvancedRendererModel(this, 49, 210);
        bottomWingFinMembraneL3.setRotationPoint(-11.0F, 0.0F, 0.1F);
        bottomWingFinMembraneL3.addBox(-11.0F, 0.0F, 0.0F, 11, 0, 8, 0.0F);
        setRotateAngle(bottomWingFinMembraneL3, 0.0F, 0.0F, 0.17453292519943295F);
        teethTop = new AdvancedRendererModel(this, 29, 40);
        teethTop.setRotationPoint(0.0F, -0.3F, -5.9F);
        teethTop.addBox(-2.5F, 0.3F, -0.7F, 5, 1, 8, 0.0F);
        body1 = new AdvancedRendererModel(this, 96, 0);
        body1.setRotationPoint(0.0F, 4.0F, 0.0F);
        body1.addBox(-5.5F, -6.3F, -4.6F, 11, 13, 15, 0.0F);
        eyeL = new AdvancedRendererModel(this, 1, 19);
        eyeL.setRotationPoint(-2.8F, -1.5F, -5.5F);
        eyeL.addBox(-1.1F, -0.5F, -0.6F, 1, 1, 1, 0.0F);
        mouthTop = new AdvancedRendererModel(this, 0, 40);
        mouthTop.setRotationPoint(0.0F, 0.1F, -6.2F);
        mouthTop.addBox(-3.0F, -4.0F, -7.1F, 6, 4, 8, 0.0F);
        eyeR = new AdvancedRendererModel(this, 1, 19);
        eyeR.setRotationPoint(2.8F, -1.5F, -5.5F);
        eyeR.addBox(0.1F, -0.5F, -0.6F, 1, 1, 1, 0.0F);
        topWingFinPhalangeR1 = new AdvancedRendererModel(this, 113, 155);
        topWingFinPhalangeR1.setRotationPoint(5.0F, -3.7F, -1.9F);
        topWingFinPhalangeR1.addBox(0.0F, -1.1F, -1.5F, 14, 2, 3, 0.0F);
        setRotateAngle(topWingFinPhalangeR1, 0.0F, 0.0F, -0.3490658503988659F);
        topWingFinPhalangeR2 = new AdvancedRendererModel(this, 80, 155);
        topWingFinPhalangeR2.setRotationPoint(13.0F, -0.1F, -0.4F);
        topWingFinPhalangeR2.addBox(0.0F, -1.1F, -0.9F, 14, 2, 2, 0.0F);
        setRotateAngle(topWingFinPhalangeR2, 0.0F, 0.0F, 0.3490658503988659F);
        head = new SocketRendererModel(this, 0, 21);
        head.setRotationPoint(0.0F, 0.0F, -6.2F);
        head.addBox(-3.5F, -4.0F, -7.1F, 7, 8, 8, 0.0F);
        bottomWingFinMembraneR2 = new AdvancedRendererModel(this, 92, 200);
        bottomWingFinMembraneR2.setRotationPoint(5.9F, 0.0F, 0.4F);
        bottomWingFinMembraneR2.addBox(-5.9F, 0.0F, -0.8F, 11, 0, 9, 0.0F);
        mouthBottom = new AdvancedRendererModel(this, 0, 55);
        mouthBottom.setRotationPoint(0.0F, 1.4F, -6.2F);
        mouthBottom.addBox(-3.0F, -1.3F, -7.1F, 6, 4, 8, 0.0F);
        bottomWingFinMembraneL1 = new AdvancedRendererModel(this, -7, 210);
        bottomWingFinMembraneL1.setRotationPoint(-5.9F, -0.1F, -0.4F);
        bottomWingFinMembraneL1.addBox(-5.1F, 0.0F, -0.8F, 12, 0, 10, 0.0F);
        bottomWingFinPhalangeL1 = new AdvancedRendererModel(this, 0, 187);
        bottomWingFinPhalangeL1.setRotationPoint(-5.1F, -0.8F, -1.9F);
        bottomWingFinPhalangeL1.addBox(-11.9F, -1.1F, -1.5F, 12, 2, 3, 0.0F);
        setRotateAngle(bottomWingFinPhalangeL1, 0.0F, 0.0F, -0.3490658503988659F);
        tail3.addChild(tail4);
        body2.addChild(tail1);
        head.addChild(headFrillR);
        head.addChild(headFrillL);
        body2.addChild(legThighR1);
        bottomWingFinPhalangeR2.addChild(bottomWingFinMembraneR3);
        tail4.addChild(tail5);
        bottomWingFinPhalangeL1.addChild(bottomWingFinPhalangeL2);
        body1.addChild(topWingFinPhalangeL1);
        topWingFinPhalangeR2.addChild(topWingFinMembraneR2);
        topWingFinPhalangeL1.addChild(topWingFinPhalangeL2);
        topWingFinPhalangeL2.addChild(topWingFinMembrane3L);
        tail5.addChild(tail6);
        legSegmentL2.addChild(footL);
        topWingFinPhalangeR2.addChild(topWingFinMembraneR3);
        body1.addChild(bottomWingFinPhalangeR1);
        body2.addChild(legThighL1);
        neck2.addChild(neck3);
        legThighL1.addChild(legSegmentL1);
        tail2.addChild(tail3);
        topWingFinPhalangeL2.addChild(topWingFinPhalangeL2_1);
        mouthBottom.addChild(teethBottom);
        bottomWingFinPhalangeL2.addChild(bottomWingFinMembraneL2);
        body1.addChild(body2);
        legSegmentR2.addChild(footR);
        legThighR1.addChild(legSegmentR1);
        topWingFinPhalangeL1.addChild(topWingFinMembraneL1);
        topWingFinPhalangeR1.addChild(topWingFinMembraneR1);
        tail1.addChild(tail2);
        bottomWingFinPhalangeR1.addChild(bottomWingFinMembraneR1);
        tail6.addChild(tailFinBottom);
        bottomWingFinPhalangeR1.addChild(bottomWingFinPhalangeR2);
        legSegmentL1.addChild(legSegmentL2);
        legSegmentR1.addChild(legSegmentR2);
        body1.addChild(neck1);
        neck1.addChild(neck2);
        tail6.addChild(tailFinTop);
        bottomWingFinPhalangeL2.addChild(bottomWingFinMembraneL3);
        mouthTop.addChild(teethTop);
        head.addChild(eyeL);
        head.addChild(mouthTop);
        head.addChild(eyeR);
        body1.addChild(topWingFinPhalangeR1);
        topWingFinPhalangeR1.addChild(topWingFinPhalangeR2);
        neck3.addChild(head);
        bottomWingFinPhalangeR2.addChild(bottomWingFinMembraneR2);
        head.addChild(mouthBottom);
        bottomWingFinPhalangeL1.addChild(bottomWingFinMembraneL1);
        body1.addChild(bottomWingFinPhalangeL1);

        animator = ModelAnimator.create();
        tailArray = new SocketRendererModel[]{tail1, tail2, tail3, tail4, tail5, tail6};
        dynamicTailArray = new SocketRendererModel[tailArray.length];
        neckArray = new AdvancedRendererModel[]{neck1, neck2, neck3};
        headArray = ArrayUtils.add(neckArray, head);
        updateDefaultPose();
    }
    
    @Override
    public void render(ButterflyLeviathanEntity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translated(body1.offsetX, body1.offsetY, body1.offsetZ);
        GlStateManager.translated(body1.rotationPointX * f5, body1.rotationPointY * f5, body1.rotationPointZ * f5);
        GlStateManager.scaled(3d, 3d, 3d);
        GlStateManager.translated(-body1.offsetX, -body1.offsetY, -body1.offsetZ);
        GlStateManager.translated(-body1.rotationPointX * f5, -body1.rotationPointY * f5, -body1.rotationPointZ * f5);
        body1.render(f5);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(ButterflyLeviathanEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        if (netHeadYaw < -180) netHeadYaw += 360;
        else if (netHeadYaw > 180) netHeadYaw -= 360;
        if (entity.areEyesInFluid(FluidTags.WATER) && !entityIn.isSitting())
            body1.rotateAngleX = headPitch * (QuikMaths.PI / 180f);
        else faceTarget(netHeadYaw, headPitch, 1, head, neck3, neck2, neck1);
    }
    
    private float globalSpeed = 0.5f;
    private float globalDegree = 0.5f;
    
    @Override
    public void setLivingAnimations(ButterflyLeviathanEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick)
    {
        entity = entityIn;
        float frame = entity.ticksExisted + partialTick;
        globalDegree = 0.5f;
        globalSpeed = 0.5f;

        resetToDefaultPose();
        animator.update(entityIn);
        setInitialPositions();

        if (!entity.isSitting())
        {
            if (entity.isInWater())
            {
                chainSwing(tailArray, globalSpeed - 0.1f, globalDegree - 0.4f, -2, limbSwing, limbSwingAmount);
                chainSwing(headArray, globalSpeed - 0.2f, globalDegree - 0.4f, 3f, limbSwing, limbSwingAmount);
                walk(legThighL1, globalSpeed - 0.4f, 0.1f, false, 0, 0, limbSwing, limbSwingAmount);
                walk(legThighR1, globalSpeed - 0.4f, 0.1f, false, 0, 0, limbSwing, limbSwingAmount);

                float wingSpeed = globalSpeed - 0.37f;

                flap(topWingFinPhalangeL1, wingSpeed, globalDegree + 0.1f, false, 0, 0.25f, limbSwing, limbSwingAmount);
                flap(topWingFinPhalangeL2, wingSpeed, globalDegree - 0.1f, false, 1, -0.2f, limbSwing, limbSwingAmount);
                flap(topWingFinMembrane3L, wingSpeed, globalDegree - 0.35f, false, 2, 0, limbSwing, limbSwingAmount);

                flap(bottomWingFinPhalangeL1, wingSpeed, globalDegree - 0.1f, false, 0, 0.25f, limbSwing, limbSwingAmount);
                flap(bottomWingFinPhalangeL2, wingSpeed, globalDegree - 0.1f, true, 1, 0.2f, limbSwing, limbSwingAmount);
                flap(bottomWingFinMembraneL3, wingSpeed, globalDegree - 0.35f, true, 2, 0, limbSwing, limbSwingAmount);

                flap(topWingFinPhalangeR1, wingSpeed, globalDegree + 0.1f, true, 0, 0.25f, limbSwing, limbSwingAmount);
                flap(topWingFinPhalangeR2, wingSpeed, globalDegree - 0.1f, false, 1, -0.2f, limbSwing, limbSwingAmount);
                flap(topWingFinMembraneR3, wingSpeed, globalDegree - 0.35f, false, 2, 0, limbSwing, limbSwingAmount);

                flap(bottomWingFinPhalangeR1, wingSpeed, globalDegree - 0.1f, true, 0, 0.25f, limbSwing, limbSwingAmount);
                flap(bottomWingFinPhalangeR2, wingSpeed, globalDegree - 0.1f, false, 1, 0, limbSwing, limbSwingAmount);
                flap(bottomWingFinMembraneR3, wingSpeed, globalDegree - 0.35f, false, 2, 0, limbSwing, limbSwingAmount);
            }
            else if (Math.abs(entity.getMotion().y) < 0.09)
            {
                flap(bottomWingFinPhalangeL1, globalSpeed - 0.2f, 0.5f, true, 0.75f, -0.25f, limbSwing, limbSwingAmount);
                swing(bottomWingFinPhalangeL1, globalSpeed - 0.2f, 0.5f, false, 0, 0, limbSwing, limbSwingAmount);
                flap(bottomWingFinPhalangeL2, globalSpeed - 0.2f, 1f, false, 0.5f, 0, limbSwing, limbSwingAmount);
                walk(bottomWingFinPhalangeL2, globalSpeed - 0.2f, 0, false, 0.5f, 0.5f, limbSwing, limbSwingAmount);

                flap(bottomWingFinPhalangeR1, globalSpeed - 0.2f, 0.5f, true, 0.75f, 0.25f, limbSwing, limbSwingAmount);
                swing(bottomWingFinPhalangeR1, globalSpeed - 0.2f, 0.5f, false, 0, 0, limbSwing, limbSwingAmount);
                flap(bottomWingFinPhalangeR2, globalSpeed - 0.2f, 1f, false, 0.5f, 0, limbSwing, limbSwingAmount);
                walk(bottomWingFinPhalangeR2, globalSpeed - 0.2f, 0, false, 0.5f, 0.5f, limbSwing, limbSwingAmount);
            }
        }

        if (animator.setAnimation(ButterflyLeviathanEntity.CONDUIT_ANIMATION) || animator.setAnimation(ButterflyLeviathanEntity.ROAR_ANIMATION))
            roarAnim();
        if (animator.setAnimation(ButterflyLeviathanEntity.BITE_ANIMATION)) biteAnim();

        idleAnim(frame);
    }
    
    private void setInitialPositions()
    {
        if (!entity.isUnderWater())
        {
            neck1.rotateAngleX = -0.6f;
            neck2.rotateAngleX = 0.15f;
            neck3.rotateAngleX = 0.2f;
            head.rotateAngleX = 0.3f;

            topWingFinPhalangeL1.rotateAngleY = 4.25f;
            topWingFinPhalangeL2.rotateAngleY = 0.5f;
            bottomWingFinPhalangeL1.rotateAngleY = -0.5f;
            bottomWingFinPhalangeL1.rotateAngleZ = -0.6f;
            bottomWingFinPhalangeL2.rotateAngleY = 0.7f;
            bottomWingFinPhalangeL2.rotateAngleZ = 0.6f;
            bottomWingFinPhalangeL2.rotateAngleX = 0.2f;

            topWingFinPhalangeR1.rotateAngleY = -1.05f;
            topWingFinPhalangeR2.rotateAngleY = -0.5f;
            bottomWingFinPhalangeR1.rotateAngleY = 0.5f;
            bottomWingFinPhalangeR1.rotateAngleZ = 0.6f;
            bottomWingFinPhalangeR2.rotateAngleY = -0.7f;
            bottomWingFinPhalangeR2.rotateAngleZ = -0.6f;
            bottomWingFinPhalangeR2.rotateAngleX = 0.2f;
        }

        if (!entity.isInWater())
        {
            legThighL1.rotateAngleX = -1.55f;
            legThighL1.rotateAngleY = 0.5f;
            legSegmentL1.rotateAngleX = 1.6f;
            legSegmentL2.rotateAngleX = -1.6f;
            footL.rotateAngleX = 0f;

            legThighR1.rotateAngleX = -1.55f;
            legThighR1.rotateAngleY = -0.5f;
            legSegmentR1.rotateAngleX = 1.6f;
            legSegmentR2.rotateAngleX = -1.6f;
            footR.rotateAngleX = 0f;

            if (entity.isSitting())
            {
                bottomWingFinPhalangeL1.rotateAngleY = 0.75f;
                bottomWingFinPhalangeL2.rotateAngleX = 0.1f;
                bottomWingFinPhalangeL2.rotateAngleZ = -0.1f;

                bottomWingFinPhalangeR1.rotateAngleY = -0.75f;
                bottomWingFinPhalangeR2.rotateAngleX = 0.1f;
                bottomWingFinPhalangeR2.rotateAngleZ = 0.1f;
            }
        }
    }
    
    @Override
    public void idleAnim(float frame)
    {
        if (!entity.isUnderWater())
        {
            flap(topWingFinPhalangeL1, 0.05f, 0.1f, false, 0, 0, frame, 0.5f);
            swing(topWingFinPhalangeL1, 0.045f, 0.025f, false, 0.5f, 0, frame, 0.5f);
            swing(topWingFinPhalangeL2, 0.045f, 0.05f, false, 0.75f, 0, frame, 0.5f);

            flap(topWingFinPhalangeR1, 0.05f, 0.1f, true, 0, 0, frame, 0.5f);
            swing(topWingFinPhalangeR1, 0.045f, 0.025f, true, 0.5f, 0, frame, 0.5f);
            swing(topWingFinPhalangeR2, 0.045f, 0.05f, true, 0.75f, 0, frame, 0.5f);

            swing(bottomWingFinPhalangeL1, globalSpeed - 0.45f, 0.05f, false, 0, 0, frame, 0.5f);
            swing(bottomWingFinPhalangeL2, globalSpeed - 0.45f, -0.1f, false, 0, 0, frame, 0.5f);
            walk(bottomWingFinPhalangeL2, globalSpeed - 0.45f, -0.05f, false, 0, 0, frame, 0.5f);

            swing(bottomWingFinPhalangeR1, 0.05f, 0.05f, true, 0, 0, frame, 0.5f);
            swing(bottomWingFinPhalangeR2, 0.05f, -0.1f, true, 0, 0, frame, 0.5f);
            walk(bottomWingFinPhalangeR2, 0.05f, -0.05f, false, 0, 0, frame, 0.5f);
        }

        globalDegree = 0.5f;
        chainWave(headArray, globalSpeed - 0.45f, globalDegree - 0.46f, entity.isUnderWater()? 3 : -3, frame, 0.5f);
        walk(mouthBottom, globalSpeed - 0.45f, 0.15f, false, -0.6f, 0.15f, frame, 0.5f);

        float degree = entity.isInWater() ? 0.1f : 0.35f;
        chainSwing(tailArray, globalSpeed - 0.45f, degree, -2, frame, 0.5f);
        chainWave(tailArray, globalSpeed - 0.47f, 0.05f, -2, frame, 0.5f);

    }

    public void roarAnim() // 46
    {
        animator.startKeyframe(8);
        for (AdvancedRendererModel box : headArray)
        {
            animator.rotate(box, -0.15f, 0, 0);
        }
        animator.rotate(mouthBottom, 0.5f, 0, 0);
        animator.rotate(topWingFinPhalangeL1, 0, -0.2f, 0);
        animator.rotate(topWingFinPhalangeL2, 0, 0, -0.5f);
        animator.rotate(topWingFinPhalangeR1, 0, 0.2f, 0);
        animator.rotate(topWingFinPhalangeR2, 0, 0, -0.5f);
        animator.rotate(headFrillL, 0, 0, 0.8f);
        animator.rotate(headFrillR, 0, 0, -0.8f);
        animator.endKeyframe();
        animator.setStaticKeyframe(30);
        animator.resetKeyframe(8);
    }

    public void biteAnim() // 20
    {
        animator.startKeyframe(10);
        animator.rotate(neck1, -0.4f, 0, 0);
        animator.rotate(head, 0.4f, 0, 0);
        animator.rotate(mouthBottom, 0.65f, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(4);
        animator.rotate(neck1, 0.4f, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }
}
