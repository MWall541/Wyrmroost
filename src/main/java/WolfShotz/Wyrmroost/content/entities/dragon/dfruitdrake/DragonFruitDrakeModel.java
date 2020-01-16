package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake;

import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedLivingEntityModel;
import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedRendererModel;
import com.mojang.blaze3d.platform.GlStateManager;

/**
 * DragonFruitDrakeModel - Kingdomall
 * Created using Tabula 7.0.1
 */
public class DragonFruitDrakeModel extends AdvancedLivingEntityModel<DragonFruitDrakeEntity>
{
    public AdvancedRendererModel Body1;
    public AdvancedRendererModel Body2;
    public AdvancedRendererModel neck2;
    public AdvancedRendererModel LegfrontR1;
    public AdvancedRendererModel BackLeafMiddle;
    public AdvancedRendererModel BackLeafL;
    public AdvancedRendererModel BackLeafR;
    public AdvancedRendererModel SideLeaf1R;
    public AdvancedRendererModel SideLeaf2R;
    public AdvancedRendererModel SideLeaf1L;
    public AdvancedRendererModel SideLeaf2L;
    public AdvancedRendererModel LegfrontL1;
    public AdvancedRendererModel Tail1;
    public AdvancedRendererModel LegThighR;
    public AdvancedRendererModel BackLeaf2Middle;
    public AdvancedRendererModel BackLeafL_1;
    public AdvancedRendererModel BackLeafR_1;
    public AdvancedRendererModel LegThighR_1;
    public AdvancedRendererModel Tail2;
    public AdvancedRendererModel TailLeaf1;
    public AdvancedRendererModel Tail3;
    public AdvancedRendererModel TailLeaf2;
    public AdvancedRendererModel Tail4;
    public AdvancedRendererModel TailLeaf3;
    public AdvancedRendererModel Tail5;
    public AdvancedRendererModel TailLeaf4;
    public AdvancedRendererModel Tail6;
    public AdvancedRendererModel BackLeaf1;
    public AdvancedRendererModel Tail7;
    public AdvancedRendererModel TailLeafEND;
    public AdvancedRendererModel LegSegmentR1;
    public AdvancedRendererModel LegSegmentR2;
    public AdvancedRendererModel LegSegmentR3;
    public AdvancedRendererModel backfootR;
    public AdvancedRendererModel LegSegmentL1;
    public AdvancedRendererModel LegSegmentL2;
    public AdvancedRendererModel LegSegmentL3;
    public AdvancedRendererModel backfootR_1;
    public AdvancedRendererModel neck3;
    public AdvancedRendererModel Neck1Leaf;
    public AdvancedRendererModel neck4;
    public AdvancedRendererModel Neck2Leaf;
    public AdvancedRendererModel Head;
    public AdvancedRendererModel NeckLeaf3;
    public AdvancedRendererModel mouthtop;
    public AdvancedRendererModel mouthbottom;
    public AdvancedRendererModel EyeR;
    public AdvancedRendererModel shape71;
    public AdvancedRendererModel shape71_1;
    public AdvancedRendererModel EyeR_1;
    public AdvancedRendererModel LegfrontR2;
    public AdvancedRendererModel LegfrontR3;
    public AdvancedRendererModel frontfootR;
    public AdvancedRendererModel LegfrontL2;
    public AdvancedRendererModel LegfrontR3_1;
    public AdvancedRendererModel frontfootL;
    
    private final AdvancedRendererModel[] headArray;
    private final AdvancedRendererModel[] tailArray;
    
    public DragonFruitDrakeModel()
    {
        textureWidth = 100;
        textureHeight = 70;
        Tail1 = new AdvancedRendererModel(this, 80, 13);
        Tail1.setRotationPoint(0.1F, -0.5F, 4.8F);
        Tail1.addBox(-2.5F, -1.9F, -0.8F, 5, 5, 5, 0.0F);
        setRotateAngle(Tail1, -0.2930407814098479F, 0.0F, 0.0F);
        SideLeaf1R = new AdvancedRendererModel(this, 0, 29);
        SideLeaf1R.setRotationPoint(3.9F, -1.1F, -3.0F);
        SideLeaf1R.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
        setRotateAngle(SideLeaf1R, 0.0F, 0.18552849948699726F, -0.0F);
        Tail7 = new AdvancedRendererModel(this, 88, 42);
        Tail7.setRotationPoint(0.0F, 0.0F, 3.4F);
        Tail7.addBox(-1.0F, -1.1F, -0.5F, 2, 2, 4, 0.0F);
        setRotateAngle(Tail7, 0.23090706003884978F, 0.0F, 0.0F);
        LegfrontR3_1 = new AdvancedRendererModel(this, 40, 39);
        LegfrontR3_1.mirror = true;
        LegfrontR3_1.setRotationPoint(0.01F, 0.1F, 5.0F);
        LegfrontR3_1.addBox(-1.5F, -1.5F, -0.7F, 3, 3, 4, 0.0F);
        setRotateAngle(LegfrontR3_1, -0.4988151002199793F, 0.0F, 0.0F);
        TailLeafEND = new AdvancedRendererModel(this, 82, 40);
        TailLeafEND.setRotationPoint(0.1F, 0.0F, 3.1F);
        TailLeafEND.addBox(-0.1F, -3.1F, -0.3F, 0, 6, 9, 0.0F);
        SideLeaf2R = new AdvancedRendererModel(this, 0, 29);
        SideLeaf2R.setRotationPoint(3.9F, 0.4F, 1.7F);
        SideLeaf2R.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
        setRotateAngle(SideLeaf2R, 0.0F, 0.3014183618194207F, -0.0F);
        Neck2Leaf = new AdvancedRendererModel(this, 5, 15);
        Neck2Leaf.setRotationPoint(0.0F, -1.1F, -2.5F);
        Neck2Leaf.addBox(0.0F, -5.0F, -1.0F, 0, 5, 2, 0.0F);
        setRotateAngle(Neck2Leaf, -0.1392772743091475F, 0.0F, 0.0F);
        LegThighR = new AdvancedRendererModel(this, 59, 18);
        LegThighR.setRotationPoint(3.3F, 0.6F, 2.3F);
        LegThighR.addBox(-1.1F, -1.3F, -1.9F, 3, 8, 5, 0.0F);
        EyeR = new AdvancedRendererModel(this, 16, 0);
        EyeR.setRotationPoint(2.5F, 0.0F, -4.6F);
        EyeR.addBox(-0.5F, -0.6F, -0.4F, 1, 1, 1, 0.0F);
        setRotateAngle(EyeR, 0.0F, 4.0821505874895365F, 0.0F);
        BackLeafL = new AdvancedRendererModel(this, 0, 23);
        BackLeafL.setRotationPoint(-2.5F, -3.0F, 0.4F);
        BackLeafL.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(BackLeafL, -1.252448271231131F, 0.0F, 0.0F);
        LegSegmentL3 = new AdvancedRendererModel(this, 61, 50);
        LegSegmentL3.mirror = true;
        LegSegmentL3.setRotationPoint(0.01F, 0.6F, 5.7F);
        LegSegmentL3.addBox(-1.5F, -1.1F, -0.9F, 3, 2, 3, 0.0F);
        setRotateAngle(LegSegmentL3, -0.6944665093685437F, 0.0F, 0.0F);
        Head = new AdvancedRendererModel(this, 0, 4);
        Head.setRotationPoint(0.0F, 0.4F, -4.2F);
        Head.addBox(-3.0F, -1.9F, -4.7F, 6, 5, 6, 0.0F);
        setRotateAngle(Head, 0.4871213942316174F, 0.0F, 0.0F);
        mouthbottom = new AdvancedRendererModel(this, 15, 23);
        mouthbottom.setRotationPoint(0.0F, 2.8F, -3.9F);
        mouthbottom.addBox(-2.0F, -0.8F, -5.0F, 4, 1, 5, 0.0F);
        BackLeaf2Middle = new AdvancedRendererModel(this, 0, 23);
        BackLeaf2Middle.setRotationPoint(0.0F, -2.5F, 0.1F);
        BackLeaf2Middle.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(BackLeaf2Middle, -1.252448271231131F, 0.0F, 0.0F);
        Tail4 = new AdvancedRendererModel(this, 82, 24);
        Tail4.setRotationPoint(0.01F, -0.7F, 2.9F);
        Tail4.addBox(-2.0F, -1.2F, -0.5F, 4, 4, 5, 0.0F);
        setRotateAngle(Tail4, 0.2851867997758734F, 0.0F, 0.0F);
        backfootR_1 = new AdvancedRendererModel(this, 61, 55);
        backfootR_1.setRotationPoint(-0.01F, 0.0F, 1.6F);
        backfootR_1.addBox(-1.5F, -1.2F, -0.3F, 3, 2, 3, 0.0F);
        setRotateAngle(backfootR_1, -0.701447826376521F, 0.0F, 0.0F);
        BackLeafMiddle = new AdvancedRendererModel(this, 0, 23);
        BackLeafMiddle.setRotationPoint(0.0F, -3.0F, -1.6F);
        BackLeafMiddle.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(BackLeafMiddle, -1.252448271231131F, 0.0F, 0.0F);
        SideLeaf1L = new AdvancedRendererModel(this, 0, 29);
        SideLeaf1L.setRotationPoint(-4.0F, -1.1F, -3.0F);
        SideLeaf1L.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
        setRotateAngle(SideLeaf1L, 0.0F, -0.16231562043547265F, -0.0F);
        Tail5 = new AdvancedRendererModel(this, 86, 34);
        Tail5.setRotationPoint(0.0F, 0.4F, 3.3F);
        Tail5.addBox(-1.5F, -1.3F, 0.4F, 3, 3, 4, 0.0F);
        setRotateAngle(Tail5, 0.3803072440095644F, 0.0F, 0.0F);
        Tail3 = new AdvancedRendererModel(this, 82, 24);
        Tail3.setRotationPoint(0.0F, 0.3F, 4.4F);
        Tail3.addBox(-2.0F, -1.8F, -1.3F, 4, 4, 5, 0.0F);
        setRotateAngle(Tail3, -0.12224096245892761F, 0.0F, 0.0F);
        LegfrontL1 = new AdvancedRendererModel(this, 34, 18);
        LegfrontL1.setRotationPoint(-3.3F, 1.2F, -1.8F);
        LegfrontL1.addBox(-1.3F, -2.0F, -0.6F, 3, 4, 7, 0.0F);
        setRotateAngle(LegfrontL1, -0.9873327578531922F, 0.0F, 0.0F);
        mouthtop = new AdvancedRendererModel(this, 15, 15);
        mouthtop.setRotationPoint(0.0F, 2.0F, -4.0F);
        mouthtop.addBox(-2.0F, -3.0F, -5.0F, 4, 3, 5, 0.0F);
        backfootR = new AdvancedRendererModel(this, 61, 55);
        backfootR.mirror = true;
        backfootR.setRotationPoint(0.01F, 0.0F, 1.6F);
        backfootR.addBox(-1.5F, -1.2F, -0.3F, 3, 2, 3, 0.0F);
        setRotateAngle(backfootR, -0.701447826376521F, 0.0F, 0.0F);
        shape71_1 = new AdvancedRendererModel(this, 6, 26);
        shape71_1.setRotationPoint(2.5F, -1.1F, -0.1F);
        shape71_1.addBox(-0.5F, -0.6F, -0.6F, 1, 2, 4, 0.0F);
        setRotateAngle(shape71_1, 0.39426987802551905F, 0.38275070496235647F, -0.0F);
        Body2 = new AdvancedRendererModel(this, 68, 0);
        Body2.setRotationPoint(0.0F, -0.3F, 5.4F);
        Body2.addBox(-3.6F, -2.5F, -1.3F, 7, 6, 6, 0.0F);
        frontfootR = new AdvancedRendererModel(this, 40, 46);
        frontfootR.setRotationPoint(-0.01F, -0.5F, 2.8F);
        frontfootR.addBox(-1.5F, -1.0F, -0.4F, 3, 2, 4, 0.0F);
        setRotateAngle(frontfootR, -0.9948376736367678F, 0.0F, 0.0F);
        TailLeaf1 = new AdvancedRendererModel(this, 0, 23);
        TailLeaf1.setRotationPoint(0.0F, -1.8F, 1.8F);
        TailLeaf1.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(TailLeaf1, -1.3800318395519162F, 0.0F, 0.0F);
        LegThighR_1 = new AdvancedRendererModel(this, 59, 18);
        LegThighR_1.mirror = true;
        LegThighR_1.setRotationPoint(-3.3F, 0.6F, 2.3F);
        LegThighR_1.addBox(-1.8F, -1.4F, -1.9F, 3, 8, 5, 0.0F);
        NeckLeaf3 = new AdvancedRendererModel(this, 0, 14);
        NeckLeaf3.setRotationPoint(0.0F, -1.2F, -2.3F);
        NeckLeaf3.addBox(0.0F, -6.0F, -1.0F, 0, 6, 2, 0.0F);
        setRotateAngle(NeckLeaf3, -0.255167136641571F, 0.0F, 0.0F);
        LegSegmentL2 = new AdvancedRendererModel(this, 55, 41);
        LegSegmentL2.setRotationPoint(0.1F, 1.0F, 4.5F);
        LegSegmentL2.addBox(-1.5F, -0.9F, -0.3F, 3, 3, 6, 0.0F);
        setRotateAngle(LegSegmentL2, -1.4456562194269031F, 0.0F, 0.0F);
        SideLeaf2L = new AdvancedRendererModel(this, 0, 29);
        SideLeaf2L.setRotationPoint(-4.0F, 0.4F, 1.6F);
        SideLeaf2L.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
        setRotateAngle(SideLeaf2L, 0.0F, -0.26668630970473356F, -0.0F);
        Neck1Leaf = new AdvancedRendererModel(this, 10, 16);
        Neck1Leaf.setRotationPoint(0.0F, -1.1F, -2.8F);
        Neck1Leaf.addBox(0.0F, -4.0F, -1.0F, 0, 4, 2, 0.0F);
        setRotateAngle(Neck1Leaf, -0.2436479635784084F, 0.0F, 0.0F);
        BackLeafR_1 = new AdvancedRendererModel(this, 0, 23);
        BackLeafR_1.setRotationPoint(1.9F, -2.5F, 2.6F);
        BackLeafR_1.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(BackLeafR_1, -1.3683381335635545F, 0.0F, 0.0F);
        neck3 = new AdvancedRendererModel(this, 23, 0);
        neck3.setRotationPoint(0.0F, 0.1F, -3.1F);
        neck3.addBox(-2.5F, -1.9F, -3.8F, 5, 5, 4, 0.0F);
        setRotateAngle(neck3, -0.251152879361984F, 0.0F, 0.0F);
        TailLeaf2 = new AdvancedRendererModel(this, 0, 23);
        TailLeaf2.setRotationPoint(0.0F, -1.8F, 1.8F);
        TailLeaf2.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(TailLeaf2, -1.3800318395519162F, 0.0F, 0.0F);
        LegfrontR3 = new AdvancedRendererModel(this, 40, 39);
        LegfrontR3.setRotationPoint(-0.01F, 0.1F, 5.0F);
        LegfrontR3.addBox(-1.5F, -1.5F, -0.7F, 3, 3, 4, 0.0F);
        setRotateAngle(LegfrontR3, -0.3490658503988659F, 0.0F, 0.0F);
        Tail2 = new AdvancedRendererModel(this, 80, 13);
        Tail2.setRotationPoint(0.01F, 0.1F, 3.7F);
        Tail2.addBox(-2.5F, -1.9F, -0.4F, 5, 5, 5, 0.0F);
        setRotateAngle(Tail2, -0.20367992370773824F, 0.0F, 0.0F);
        neck2 = new AdvancedRendererModel(this, 23, 0);
        neck2.setRotationPoint(0.0F, -0.2F, -2.2F);
        neck2.addBox(-2.5F, -1.9F, -3.7F, 5, 5, 4, 0.0F);
        setRotateAngle(neck2, -0.5326744877086693F, 0.0F, 0.0F);
        TailLeaf3 = new AdvancedRendererModel(this, 0, 23);
        TailLeaf3.setRotationPoint(-0.1F, -1.7F, 1.8F);
        TailLeaf3.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(TailLeaf3, -1.3800318395519162F, 0.0F, 0.0F);
        BackLeafL_1 = new AdvancedRendererModel(this, 0, 23);
        BackLeafL_1.setRotationPoint(-2.1F, -2.5F, 2.6F);
        BackLeafL_1.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(BackLeafL_1, -1.3800318395519162F, 0.0F, 0.0F);
        EyeR_1 = new AdvancedRendererModel(this, 16, 0);
        EyeR_1.mirror = true;
        EyeR_1.setRotationPoint(-2.5F, 0.0F, -4.6F);
        EyeR_1.addBox(-0.5F, -0.6F, -0.4F, 1, 1, 1, 0.0F);
        setRotateAngle(EyeR_1, 0.0F, -4.0821505874895365F, 0.0F);
        LegSegmentR3 = new AdvancedRendererModel(this, 61, 50);
        LegSegmentR3.setRotationPoint(-0.01F, 0.6F, 5.7F);
        LegSegmentR3.addBox(-1.5F, -1.1F, -0.9F, 3, 2, 3, 0.0F);
        setRotateAngle(LegSegmentR3, -0.6944665093685437F, 0.0F, 0.0F);
        Body1 = new AdvancedRendererModel(this, 33, 1);
        Body1.setRotationPoint(0.0F, -2.5F, -2.5F);
        Body1.addBox(-4.0F, -3.1F, -4.0F, 8, 8, 9, 0.0F);
        LegfrontR2 = new AdvancedRendererModel(this, 34, 29);
        LegfrontR2.mirror = true;
        LegfrontR2.setRotationPoint(-0.1F, 0.0F, 5.9F);
        LegfrontR2.addBox(-1.5F, -1.5F, -0.9F, 3, 3, 7, 0.0F);
        setRotateAngle(LegfrontR2, -0.8052949168701837F, 0.0F, 0.0F);
        LegSegmentR2 = new AdvancedRendererModel(this, 55, 41);
        LegSegmentR2.mirror = true;
        LegSegmentR2.setRotationPoint(-0.1F, 1.0F, 4.5F);
        LegSegmentR2.addBox(-1.5F, -0.9F, -0.3F, 3, 3, 6, 0.0F);
        setRotateAngle(LegSegmentR2, -1.4456562194269031F, 0.0F, 0.0F);
        Tail6 = new AdvancedRendererModel(this, 86, 34);
        Tail6.setRotationPoint(-0.01F, -0.1F, 4.0F);
        Tail6.addBox(-1.5F, -1.4F, -0.8F, 3, 3, 4, 0.0F);
        setRotateAngle(Tail6, 0.2988003679414292F, 0.0F, 0.0F);
        LegSegmentR1 = new AdvancedRendererModel(this, 57, 31);
        LegSegmentR1.setRotationPoint(0.3F, 4.3F, 0.0F);
        LegSegmentR1.addBox(-1.5F, -1.3F, -0.6F, 3, 4, 6, 0.0F);
        setRotateAngle(LegSegmentR1, -0.30543261909900765F, 0.0F, 0.007504915783575617F);
        LegfrontR1 = new AdvancedRendererModel(this, 34, 18);
        LegfrontR1.mirror = true;
        LegfrontR1.setRotationPoint(3.3F, 1.2F, -1.8F);
        LegfrontR1.addBox(-1.5F, -2.0F, -0.6F, 3, 4, 7, 0.0F);
        setRotateAngle(LegfrontR1, -0.9873327578531922F, 0.0F, 0.0F);
        LegSegmentL1 = new AdvancedRendererModel(this, 57, 31);
        LegSegmentL1.mirror = true;
        LegSegmentL1.setRotationPoint(-0.2F, 4.3F, 0.0F);
        LegSegmentL1.addBox(-1.5F, -1.3F, -0.6F, 3, 4, 6, 0.0F);
        setRotateAngle(LegSegmentL1, -0.30543261909900765F, 0.0F, 0.0F);
        TailLeaf4 = new AdvancedRendererModel(this, 0, 29);
        TailLeaf4.setRotationPoint(-0.2F, -1.1F, 1.8F);
        TailLeaf4.addBox(-1.0F, -3.9F, 0.0F, 2, 4, 0, 0.0F);
        setRotateAngle(TailLeaf4, -1.3800318395519162F, 0.0F, 0.0F);
        LegfrontL2 = new AdvancedRendererModel(this, 34, 29);
        LegfrontL2.setRotationPoint(0.3F, 0.0F, 5.9F);
        LegfrontL2.addBox(-1.5F, -1.5F, -0.9F, 3, 3, 7, 0.0F);
        setRotateAngle(LegfrontL2, -0.8052949168701837F, 0.0F, 0.0F);
        neck4 = new AdvancedRendererModel(this, 23, 0);
        neck4.setRotationPoint(0.0F, -0.05F, -3.2F);
        neck4.addBox(-2.5F, -1.9F, -3.8F, 5, 5, 4, 0.0F);
        setRotateAngle(neck4, 0.4300491276914028F, 0.0F, 0.0F);
        frontfootL = new AdvancedRendererModel(this, 40, 46);
        frontfootL.mirror = true;
        frontfootL.setRotationPoint(0.01F, -0.5F, 2.8F);
        frontfootL.addBox(-1.5F, -1.0F, -0.4F, 3, 2, 4, 0.0F);
        setRotateAngle(frontfootL, -0.8737118235483614F, 0.0F, 0.0F);
        BackLeaf1 = new AdvancedRendererModel(this, 0, 29);
        BackLeaf1.setRotationPoint(-0.3F, -1.1F, 1.8F);
        BackLeaf1.addBox(-1.0F, -3.9F, 0.0F, 2, 4, 0, 0.0F);
        setRotateAngle(BackLeaf1, -1.3800318395519162F, 0.0F, 0.0F);
        shape71 = new AdvancedRendererModel(this, 6, 26);
        shape71.mirror = true;
        shape71.setRotationPoint(-2.5F, -1.1F, -0.1F);
        shape71.addBox(-0.5F, -0.6F, -0.6F, 1, 2, 4, 0.0F);
        setRotateAngle(shape71, 0.39426987802551905F, -0.28989918875625814F, 0.0F);
        BackLeafR = new AdvancedRendererModel(this, 0, 23);
        BackLeafR.setRotationPoint(2.5F, -3.0F, 0.4F);
        BackLeafR.addBox(-1.0F, -4.9F, 0.0F, 2, 5, 0, 0.0F);
        setRotateAngle(BackLeafR, -1.252448271231131F, 0.0F, 0.0F);
        Body2.addChild(Tail1);
        Body1.addChild(SideLeaf1R);
        Tail6.addChild(Tail7);
        LegfrontL2.addChild(LegfrontR3_1);
        Tail7.addChild(TailLeafEND);
        Body1.addChild(SideLeaf2R);
        neck3.addChild(Neck2Leaf);
        Body2.addChild(LegThighR);
        Head.addChild(EyeR);
        Body1.addChild(BackLeafL);
        LegSegmentL2.addChild(LegSegmentL3);
        neck4.addChild(Head);
        Head.addChild(mouthbottom);
        Body2.addChild(BackLeaf2Middle);
        Tail3.addChild(Tail4);
        LegSegmentL3.addChild(backfootR_1);
        Body1.addChild(BackLeafMiddle);
        Body1.addChild(SideLeaf1L);
        Tail4.addChild(Tail5);
        Tail2.addChild(Tail3);
        Body1.addChild(LegfrontL1);
        Head.addChild(mouthtop);
        LegSegmentR3.addChild(backfootR);
        Head.addChild(shape71_1);
        Body1.addChild(Body2);
        LegfrontR3.addChild(frontfootR);
        Tail1.addChild(TailLeaf1);
        Body2.addChild(LegThighR_1);
        neck4.addChild(NeckLeaf3);
        LegSegmentL1.addChild(LegSegmentL2);
        Body1.addChild(SideLeaf2L);
        neck2.addChild(Neck1Leaf);
        Body2.addChild(BackLeafR_1);
        neck2.addChild(neck3);
        Tail2.addChild(TailLeaf2);
        LegfrontR2.addChild(LegfrontR3);
        Tail1.addChild(Tail2);
        Body1.addChild(neck2);
        Tail3.addChild(TailLeaf3);
        Body2.addChild(BackLeafL_1);
        Head.addChild(EyeR_1);
        LegSegmentR2.addChild(LegSegmentR3);
        LegfrontR1.addChild(LegfrontR2);
        LegSegmentR1.addChild(LegSegmentR2);
        Tail5.addChild(Tail6);
        LegThighR.addChild(LegSegmentR1);
        Body1.addChild(LegfrontR1);
        LegThighR_1.addChild(LegSegmentL1);
        Tail4.addChild(TailLeaf4);
        LegfrontL1.addChild(LegfrontL2);
        neck3.addChild(neck4);
        LegfrontR3_1.addChild(frontfootL);
        Tail5.addChild(BackLeaf1);
        Head.addChild(shape71);
        Body1.addChild(BackLeafR);
        
        headArray = new AdvancedRendererModel[]{neck2, neck3, neck4, Head};
        tailArray = new AdvancedRendererModel[]{Tail1, Tail2, Tail3, Tail4, Tail5, Tail6, Tail7};
        
        updateDefaultPose();
    }
    
    @Override
    public void render(DragonFruitDrakeEntity fruitDrake, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scaled(2, 2, 2);
        Body1.render(scale);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(DragonFruitDrakeEntity fruitDrake, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        faceTarget(netHeadYaw, headPitch, 1, headArray);
    }
    
    private float globalSpeed = 0.5f;
    private float globalDegree = 0.5f;
    
    @Override
    public void setLivingAnimations(DragonFruitDrakeEntity fruitDrake, float limbSwing, float limbSwingAmount, float partialTick)
    {
        float frame = partialTick + fruitDrake.ticksExisted;
        
        globalDegree = 0.5f;
        globalSpeed = 0.5f;
        resetToDefaultPose();
        
        if (!fruitDrake.isSleeping())
        {
//            globalDegree = 0.515f;
            
            bob(Body1, 0.6f, 0.2f, false, limbSwing, limbSwingAmount);
            
            // Front
            walk(LegfrontR1, 0.3f, 0.6f, false, 0, 0, limbSwing, limbSwingAmount);
            walk(LegfrontR2, 0.3f, 0.2f, false, 2.5f, 0, limbSwing, limbSwingAmount);
            walk(frontfootR, 0.3f, 0.3f, false, 2.5f, -0.35f, limbSwing, limbSwingAmount);
            
            walk(LegfrontL1, 0.3f, 0.6f, true, 0, 0, limbSwing, limbSwingAmount);
            walk(LegfrontL2, 0.3f, 0.2f, true, 2.5f, 0, limbSwing, limbSwingAmount);
            walk(frontfootL, 0.3f, 0.3f, true, 2.5f, 0.35f, limbSwing, limbSwingAmount);
            
            // Back
            walk(LegThighR, 0.3f, 0.6f, true, 0, 0, limbSwing, limbSwingAmount);
            walk(backfootR, 0.3f, 0.6f, true, 2.5f, 0.35f, limbSwing, limbSwingAmount);
            
            walk(LegThighR_1, 0.3f, 0.6f, false, 0, 0, limbSwing, limbSwingAmount);
            walk(backfootR_1, 0.3f, 0.6f, false, 2.5f, -0.35f, limbSwing, limbSwingAmount);
        }
        if (fruitDrake.isSleeping())
        {
//            globalSpeed = 0.15f;
        }
        
        idleAnim(frame, false);
    }
    
    public void idleAnim(float frame, boolean mouth)
    {
        if (mouth) walk(mouthbottom, globalSpeed - 0.425f, 0.1f, false, 0.5f, 0.1f, frame, 0.5f);
        chainWave(headArray, globalSpeed - 0.425f, globalDegree - 0.47f, 2, frame, 0.5f);
        chainWave(tailArray, globalSpeed - 0.45f, globalDegree - 0.45f, 2, frame, 0.5f);
        chainSwing(tailArray, globalSpeed - 0.465f, globalDegree - 0.45f, 3d, frame, 0.5f);
    }
}
