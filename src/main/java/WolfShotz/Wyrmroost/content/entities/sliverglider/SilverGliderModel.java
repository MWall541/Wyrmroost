package WolfShotz.Wyrmroost.content.entities.sliverglider;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * WRSilverGlider - Kingdomall
 * Created using Tabula 7.0.1
 */
public class SilverGliderModel extends AdvancedEntityModel {
    public AdvancedRendererModel mainbody;
    public AdvancedRendererModel secondarybody;
    public AdvancedRendererModel neck1;
    public AdvancedRendererModel backFin;
    public AdvancedRendererModel bottomFin;
    public AdvancedRendererModel wing1L;
    public AdvancedRendererModel wing1R;
    public AdvancedRendererModel tail1;
    public AdvancedRendererModel legL1;
    public AdvancedRendererModel legR1;
    public AdvancedRendererModel tail2;
    public AdvancedRendererModel tail3;
    public AdvancedRendererModel tail4;
    public AdvancedRendererModel tail5;
    public AdvancedRendererModel tail6;
    public AdvancedRendererModel tail7;
    public AdvancedRendererModel tail8;
    public AdvancedRendererModel tail9;
    public AdvancedRendererModel tail10;
    public AdvancedRendererModel tailfin;
    public AdvancedRendererModel legL2;
    public AdvancedRendererModel legL3;
    public AdvancedRendererModel toe2L;
    public AdvancedRendererModel toe1L;
    public AdvancedRendererModel toe3L;
    public AdvancedRendererModel legR2;
    public AdvancedRendererModel legR3;
    public AdvancedRendererModel toe2R;
    public AdvancedRendererModel toe1R;
    public AdvancedRendererModel toe1R_1;
    public AdvancedRendererModel neck2;
    public AdvancedRendererModel neck3;
    public AdvancedRendererModel neck4;
    public AdvancedRendererModel head;
    public AdvancedRendererModel mouthBottom;
    public AdvancedRendererModel mouthTop;
    public AdvancedRendererModel eyeL;
    public AdvancedRendererModel eyeR;
    public AdvancedRendererModel wing2L;
    public AdvancedRendererModel membrane3L;
    public AdvancedRendererModel phalangeL11;
    public AdvancedRendererModel phalangeL21;
    public AdvancedRendererModel phalangeL12;
    public AdvancedRendererModel membrane1L;
    public AdvancedRendererModel phalangeL22;
    public AdvancedRendererModel membrane2L;
    public AdvancedRendererModel wing2R;
    public AdvancedRendererModel membrane3R;
    public AdvancedRendererModel phalangeR11;
    public AdvancedRendererModel phalangeR21;
    public AdvancedRendererModel phalangeR12;
    public AdvancedRendererModel membrane1R;
    public AdvancedRendererModel phalangeR22;
    public AdvancedRendererModel membrane2R;
    
    private ModelAnimator animator;
    
    private final AdvancedRendererModel[] neckArray;
    private final AdvancedRendererModel[] neckArray2;
    private final AdvancedRendererModel[] tailArray;
    private final AdvancedRendererModel[] toeArray;
    
    private int glideFlapTicks = 0;
    private Random rand = new Random();

    public SilverGliderModel() {
        this.textureWidth = 160;
        this.textureHeight = 125;
        this.membrane2L = new AdvancedRendererModel(this, -16, 100);
        this.membrane2L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.membrane2L.addBox(0.0F, 0.0F, -0.5F, 20, 0, 18, 0.0F);
        this.wing1R = new AdvancedRendererModel(this, 0, 58);
        this.wing1R.setRotationPoint(-1.5F, -1.0F, -3.0F);
        this.wing1R.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        this.setRotateAngle(wing1R, 0.0F, 0.17453292519943295F, 0.0F);
        this.phalangeR22 = new AdvancedRendererModel(this, 33, 69);
        this.phalangeR22.setRotationPoint(-10.0F, 0.0F, 0.0F);
        this.phalangeR22.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(phalangeR22, 0.0F, 0.17453292519943295F, 0.0F);
        this.eyeR = new AdvancedRendererModel(this, 79, 2);
        this.eyeR.setRotationPoint(-1.8F, -0.5F, -3.9F);
        this.eyeR.addBox(-0.7F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(eyeR, 0.0F, -0.17453292519943295F, 0.0F);
        this.phalangeR12 = new AdvancedRendererModel(this, 33, 65);
        this.phalangeR12.setRotationPoint(-15.0F, 0.0F, 0.0F);
        this.phalangeR12.addBox(-25.0F, -0.5F, -0.5F, 25, 1, 1, 0.0F);
        this.setRotateAngle(phalangeR12, 0.0F, 0.17453292519943295F, 0.0F);
        this.tail9 = new AdvancedRendererModel(this, 90, 60);
        this.tail9.setRotationPoint(-0.02F, -0.02F, 2.0F);
        this.tail9.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.legL1 = new AdvancedRendererModel(this, 56, 0);
        this.legL1.setRotationPoint(1.6F, 0.9F, 5.9F);
        this.legL1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        this.setRotateAngle(legL1, -0.5918411493512771F, 0.0F, 0.0F);
        this.neck3 = new AdvancedRendererModel(this, 88, 17);
        this.neck3.setRotationPoint(-0.02F, -0.02F, -2.0F);
        this.neck3.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.legR2 = new AdvancedRendererModel(this, 56, 8);
        this.legR2.setRotationPoint(0.42F, 3.1F, -0.1F);
        this.legR2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(legR2, 1.360135086079181F, 0.0F, 0.0F);
        this.wing1L = new AdvancedRendererModel(this, 0, 58);
        this.wing1L.setRotationPoint(1.5F, -1.0F, -3.0F);
        this.wing1L.addBox(0.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        this.setRotateAngle(wing1L, 0.0F, -0.17453292519943295F, 0.0F);
        this.toe2L = new AdvancedRendererModel(this, 60, 20);
        this.toe2L.setRotationPoint(0.0F, 3.0F, 0.2F);
        this.toe2L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe2L, -0.9560913642424937F, 0.0F, 0.0F);
        this.phalangeL22 = new AdvancedRendererModel(this, 33, 69);
        this.phalangeL22.setRotationPoint(10.0F, 0.0F, 0.0F);
        this.phalangeL22.addBox(0.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(phalangeL22, 0.0F, -0.17453292519943295F, 0.0F);
        this.phalangeL12 = new AdvancedRendererModel(this, 33, 65);
        this.phalangeL12.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.phalangeL12.addBox(0.0F, -0.5F, -0.5F, 25, 1, 1, 0.0F);
        this.setRotateAngle(phalangeL12, 0.0F, -0.17453292519943295F, 0.0F);
        this.neck1 = new AdvancedRendererModel(this, 88, 17);
        this.neck1.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.neck1.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.neck2 = new AdvancedRendererModel(this, 88, 17);
        this.neck2.setRotationPoint(0.02F, 0.02F, -2.0F);
        this.neck2.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.mouthTop = new AdvancedRendererModel(this, 67, 10);
        this.mouthTop.setRotationPoint(0.0F, -0.7F, -4.5F);
        this.mouthTop.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0.0F);
        this.setRotateAngle(mouthTop, 0.31869712141416456F, 0.0F, 0.0F);
        this.secondarybody = new AdvancedRendererModel(this, 76, 40);
        this.secondarybody.setRotationPoint(0.0F, -0.3F, 4.9F);
        this.secondarybody.addBox(-2.1F, -2.1F, -0.5F, 4, 4, 8, 0.0F);
        this.tail5 = new AdvancedRendererModel(this, 90, 60);
        this.tail5.setRotationPoint(-0.1F, -0.1F, 2.0F);
        this.tail5.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.phalangeL11 = new AdvancedRendererModel(this, 0, 65);
        this.phalangeL11.setRotationPoint(14.0F, -0.3F, -0.5F);
        this.phalangeL11.addBox(0.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        this.setRotateAngle(phalangeL11, 0.0F, -0.17453292519943295F, 0.0F);
        this.legL2 = new AdvancedRendererModel(this, 56, 8);
        this.legL2.setRotationPoint(0.38F, 3.1F, -0.1F);
        this.legL2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(legL2, 1.360135086079181F, 0.0F, 0.0F);
        this.wing2R = new AdvancedRendererModel(this, 31, 58);
        this.wing2R.setRotationPoint(-9.0F, 0.01F, 0.0F);
        this.wing2R.addBox(-15.0F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
        this.setRotateAngle(wing2R, 0.0F, -0.3490658503988659F, 0.0F);
        this.backFin = new AdvancedRendererModel(this, 0, -14);
        this.backFin.setRotationPoint(0.0F, -1.8F, 2.2F);
        this.backFin.addBox(0.0F, -7.6F, -6.9F, 0, 8, 14, 0.0F);
        this.tail6 = new AdvancedRendererModel(this, 90, 60);
        this.tail6.setRotationPoint(0.02F, 0.02F, 2.0F);
        this.tail6.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.toe3L = new AdvancedRendererModel(this, 60, 20);
        this.toe3L.setRotationPoint(-0.5F, 3.0F, 0.2F);
        this.toe3L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe3L, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        this.phalangeL21 = new AdvancedRendererModel(this, 0, 70);
        this.phalangeL21.setRotationPoint(14.0F, -0.3F, 0.5F);
        this.phalangeL21.addBox(0.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(phalangeL21, 0.0F, -0.7740535232594852F, 0.0F);
        this.tail1 = new AdvancedRendererModel(this, 88, 53);
        this.tail1.setRotationPoint(0.02F, 0.02F, 7.0F);
        this.tail1.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.bottomFin = new AdvancedRendererModel(this, 0, -3);
        this.bottomFin.setRotationPoint(0.0F, 1.4F, 2.1F);
        this.bottomFin.addBox(0.0F, -0.1F, -6.4F, 0, 5, 12, 0.0F);
        this.tail4 = new AdvancedRendererModel(this, 88, 53);
        this.tail4.setRotationPoint(-0.02F, -0.02F, 2.8F);
        this.tail4.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.tailfin = new AdvancedRendererModel(this, 18, 30);
        this.tailfin.setRotationPoint(0.0F, 0.0F, 1.6F);
        this.tailfin.addBox(-5.0F, 0.0F, -1.6F, 10, 0, 12, 0.0F);
        this.tail10 = new AdvancedRendererModel(this, 90, 60);
        this.tail10.setRotationPoint(0.02F, 0.02F, 2.0F);
        this.tail10.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.membrane1L = new AdvancedRendererModel(this, -16, 78);
        this.membrane1L.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.membrane1L.addBox(0.0F, 0.0F, -0.5F, 40, 0, 18, 0.0F);
        this.phalangeR11 = new AdvancedRendererModel(this, 0, 65);
        this.phalangeR11.setRotationPoint(-14.0F, -0.3F, -0.5F);
        this.phalangeR11.addBox(-15.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        this.setRotateAngle(phalangeR11, 0.0F, 0.17453292519943295F, 0.0F);
        this.mainbody = new AdvancedRendererModel(this, 70, 24);
        this.mainbody.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.mainbody.addBox(-2.5F, -2.5F, -5.0F, 5, 5, 10, 0.0F);
        this.neck4 = new AdvancedRendererModel(this, 88, 17);
        this.neck4.setRotationPoint(0.02F, 0.02F, -2.0F);
        this.neck4.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        this.wing2L = new AdvancedRendererModel(this, 31, 58);
        this.wing2L.setRotationPoint(9.0F, 0.01F, 0.0F);
        this.wing2L.addBox(0.0F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
        this.setRotateAngle(wing2L, 0.0F, 0.3490658503988659F, 0.0F);
        this.tail7 = new AdvancedRendererModel(this, 90, 60);
        this.tail7.setRotationPoint(-0.02F, -0.02F, 2.0F);
        this.tail7.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.toe1R = new AdvancedRendererModel(this, 60, 20);
        this.toe1R.setRotationPoint(0.5F, 3.0F, 0.2F);
        this.toe1R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe1R, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        this.membrane2R = new AdvancedRendererModel(this, -16, 100);
        this.membrane2R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.membrane2R.addBox(-20.0F, 0.0F, -0.5F, 20, 0, 18, 0.0F);
        this.legR3 = new AdvancedRendererModel(this, 58, 14);
        this.legR3.setRotationPoint(-0.02F, 2.5F, 0.1F);
        this.legR3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        this.setRotateAngle(legR3, -1.3632766787327708F, 0.0F, 0.0F);
        this.tail8 = new AdvancedRendererModel(this, 90, 60);
        this.tail8.setRotationPoint(0.02F, 0.02F, 2.0F);
        this.tail8.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        this.legR1 = new AdvancedRendererModel(this, 56, 0);
        this.legR1.setRotationPoint(-2.3F, 0.9F, 5.9F);
        this.legR1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        this.setRotateAngle(legR1, -0.5918411493512771F, 0.0F, 0.0F);
        this.toe1R_1 = new AdvancedRendererModel(this, 60, 20);
        this.toe1R_1.setRotationPoint(-0.5F, 3.0F, 0.2F);
        this.toe1R_1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe1R_1, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        this.mouthBottom = new AdvancedRendererModel(this, 84, 10);
        this.mouthBottom.setRotationPoint(0.0F, 1.0F, -4.5F);
        this.mouthBottom.addBox(-1.5F, -0.5F, -4.0F, 3, 1, 4, 0.0F);
        this.tail3 = new AdvancedRendererModel(this, 88, 53);
        this.tail3.setRotationPoint(0.02F, 0.02F, 2.8F);
        this.tail3.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.toe2R = new AdvancedRendererModel(this, 60, 20);
        this.toe2R.setRotationPoint(0.0F, 3.0F, 0.2F);
        this.toe2R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe2R, -0.9560913642424937F, 0.0F, 0.0F);
        this.toe1L = new AdvancedRendererModel(this, 60, 20);
        this.toe1L.setRotationPoint(0.5F, 3.0F, 0.2F);
        this.toe1L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(toe1L, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        this.phalangeR21 = new AdvancedRendererModel(this, 0, 70);
        this.phalangeR21.setRotationPoint(-14.0F, -0.3F, 0.5F);
        this.phalangeR21.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        this.setRotateAngle(phalangeR21, 0.0F, 0.7740535232594852F, 0.0F);
        this.eyeL = new AdvancedRendererModel(this, 79, 2);
        this.eyeL.setRotationPoint(1.7F, -0.5F, -3.9F);
        this.eyeL.addBox(0.0F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(eyeL, 0.0F, 0.17453292519943295F, 0.0F);
        this.membrane1R = new AdvancedRendererModel(this, -16, 78);
        this.membrane1R.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.membrane1R.addBox(-40.0F, 0.0F, -0.5F, 40, 0, 18, 0.0F);
        this.membrane3R = new AdvancedRendererModel(this, 38, 100);
        this.membrane3R.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.membrane3R.addBox(-23.0F, 0.0F, -0.5F, 23, 0, 18, 0.0F);
        this.setRotateAngle(membrane3R, 0.0F, -0.17453292519943295F, 0.0F);
        this.legL3 = new AdvancedRendererModel(this, 58, 14);
        this.legL3.setRotationPoint(0.02F, 2.5F, 0.1F);
        this.legL3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        this.setRotateAngle(legL3, -1.3632766787327708F, 0.0F, 0.0F);
        this.head = new AdvancedRendererModel(this, 80, 0);
        this.head.setRotationPoint(0.0F, -0.1F, -2.0F);
        this.head.addBox(-2.4F, -2.0F, -4.8F, 5, 4, 5, 0.0F);
        this.membrane3L = new AdvancedRendererModel(this, 38, 100);
        this.membrane3L.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.membrane3L.addBox(0.0F, 0.0F, -0.5F, 23, 0, 18, 0.0F);
        this.setRotateAngle(membrane3L, 0.0F, 0.17453292519943295F, 0.0F);
        this.tail2 = new AdvancedRendererModel(this, 88, 53);
        this.tail2.setRotationPoint(-0.02F, -0.02F, 2.8F);
        this.tail2.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        this.phalangeL21.addChild(this.membrane2L);
        this.mainbody.addChild(this.wing1R);
        this.phalangeR21.addChild(this.phalangeR22);
        this.head.addChild(this.eyeR);
        this.phalangeR11.addChild(this.phalangeR12);
        this.tail8.addChild(this.tail9);
        this.secondarybody.addChild(this.legL1);
        this.neck2.addChild(this.neck3);
        this.legR1.addChild(this.legR2);
        this.mainbody.addChild(this.wing1L);
        this.legL3.addChild(this.toe2L);
        this.phalangeL21.addChild(this.phalangeL22);
        this.phalangeL11.addChild(this.phalangeL12);
        this.mainbody.addChild(this.neck1);
        this.neck1.addChild(this.neck2);
        this.head.addChild(this.mouthTop);
        this.mainbody.addChild(this.secondarybody);
        this.tail4.addChild(this.tail5);
        this.wing2L.addChild(this.phalangeL11);
        this.legL1.addChild(this.legL2);
        this.wing1R.addChild(this.wing2R);
        this.mainbody.addChild(this.backFin);
        this.tail5.addChild(this.tail6);
        this.legL3.addChild(this.toe3L);
        this.wing2L.addChild(this.phalangeL21);
        this.secondarybody.addChild(this.tail1);
        this.mainbody.addChild(this.bottomFin);
        this.tail3.addChild(this.tail4);
        this.tail10.addChild(this.tailfin);
        this.tail9.addChild(this.tail10);
        this.phalangeL11.addChild(this.membrane1L);
        this.wing2R.addChild(this.phalangeR11);
        this.neck3.addChild(this.neck4);
        this.wing1L.addChild(this.wing2L);
        this.tail6.addChild(this.tail7);
        this.legR3.addChild(this.toe1R);
        this.phalangeR21.addChild(this.membrane2R);
        this.legR2.addChild(this.legR3);
        this.tail7.addChild(this.tail8);
        this.secondarybody.addChild(this.legR1);
        this.legR3.addChild(this.toe1R_1);
        this.head.addChild(this.mouthBottom);
        this.tail2.addChild(this.tail3);
        this.legR3.addChild(this.toe2R);
        this.legL3.addChild(this.toe1L);
        this.wing2R.addChild(this.phalangeR21);
        this.head.addChild(this.eyeL);
        this.phalangeR11.addChild(this.membrane1R);
        this.wing1R.addChild(this.membrane3R);
        this.legL2.addChild(this.legL3);
        this.neck4.addChild(this.head);
        this.wing1L.addChild(this.membrane3L);
        this.tail1.addChild(this.tail2);
    
        neckArray = new AdvancedRendererModel[] {neck1, neck2, neck3, neck4, head};
        neckArray2 = new AdvancedRendererModel[] {neck1, neck2};
        tailArray = new AdvancedRendererModel[] {tail1, tail2, tail3, tail4, tail6, tail7, tail8, tail9, tail10};
        toeArray = new AdvancedRendererModel[] {toe1L, toe1R, toe2L, toe2R, toe1R_1, toe3L};
    
        animator = ModelAnimator.create();
    
        updateDefaultPose();
    }
    
    private float globalSpeed;
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        mainbody.render(scale);
        
        globalSpeed = 0.5f;
    }
    
    @Override
    public void setLivingAnimations(Entity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        SilverGliderEntity glider = (SilverGliderEntity) entityIn;
        Vec3d look = glider.getLookVec();
        boolean shouldFlap = glider.isFlying() && look.y > -0.13f && look.y < 0.13f;
        float frame = entityIn.ticksExisted;
        
        resetToDefaultPose();
        animator.update(glider);
        
        if (glider.isFlying()) {
            // do idle first, so we can override anything we need
            idleAnim(glider, frame);
            
            // Rotate the body according to look vector
            mainbody.rotateAngleX = (float) -look.y;
            if (glider.isRiding()) { // legs need to stay on the player head...
                mainbody.offsetZ = -0.6f;
                legL1.rotateAngleX = (float) look.y;
                legR1.rotateAngleX = (float) look.y;
            }
            if (look.y < 0) {
                wing1L.rotateAngleY = (float) Math.max(look.y / 2, -0.5f);
                
            }
            
            if (glideFlapTicks <= 0) {
                if (glider.posY > glider.prevPosY && !glider.hasActiveAnimation()) {
                    ascendAnim();
                    return;
                }
                if (shouldFlap && rand.nextInt(100) == 0) glideFlapTicks = 50;
                
            } else --glideFlapTicks;
            
            return;
        }
        
        
        if (glider.isSitting() && !glider.hasActiveAnimation()) {
            staySitting();
            idleAnim(glider, frame);
            
            return;
        }
        
        idleAnim(glider, frame);
    }
    
    // animate the head and tail according to glider's state (flying or on ground)
    private void idleAnim(SilverGliderEntity glider, float frame) {
        if (glider.isFlying()) {
            if (!glider.isRiding()) {
                // Left Leg
                legL1.rotateAngleX = 0.8f;
                legL2.rotateAngleX = 0.8f;
                legL3.rotateAngleX = -0.6f;
                // Right leg
                legR1.rotateAngleX = 0.8f;
                legR2.rotateAngleX = 0.8f;
                legR3.rotateAngleX = -0.6f;
                // Toes
                for (AdvancedRendererModel toe : toeArray) toe.rotateAngleX = 0.5f;
            }
            
            // Neck + head
            chainWave(neckArray, globalSpeed - 0.2f, 0.05f, 3f, frame, 0.5f);
            
            // Legs
            walk(legL1, globalSpeed + 0.2f, 0.03f, false, 0, 0, frame, 0.5f);
            walk(legR1, globalSpeed + 0.2f, 0.03f, true, 0, 0, frame, 0.5f);
            
            // Tail
            chainWave(tailArray, globalSpeed - 0.25f, 0.06f, 2.5, frame, 0.5f);
        } else {
            // Neck
            chainWave(neckArray2, globalSpeed - 0.4f, 0.02f, 0, frame, 0.5f);
            
            // Tail
            chainSwing(tailArray, globalSpeed - 0.45f, 0.03f, 0, frame, 0.5f);
            chainWave(tailArray, globalSpeed - 0.46f, 0.06f, 0, frame, 0.5f);
        }
    }
    
    // Keep the sit position
    private void staySitting() {
    
    }
    
    private void ascendAnim() {
    
    }
}
