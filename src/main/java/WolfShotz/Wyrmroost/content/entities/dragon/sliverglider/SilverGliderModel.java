package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.util.utils.MathUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import static WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity.SLEEP_ANIMATION;
import static WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity.WAKE_ANIMATION;
import static WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity.*;

/**
 * WRSilverGlider - Kingdomall
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class SilverGliderModel extends AdvancedEntityModel {
    private SilverGliderEntity glider;
    
    public AdvancedRendererModel mainbody;
    public AdvancedRendererModel secondarybody;
    public AdvancedRendererModel neck1;
    public AdvancedRendererModel backFin;
    public AdvancedRendererModel bottomFin;
    public AdvancedRendererModel wingphalange1L;
    public AdvancedRendererModel wingphalange1R;
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
    public AdvancedRendererModel wingphalange2L;
    public AdvancedRendererModel membrane1L;
    public AdvancedRendererModel wingphalange3L;
    public AdvancedRendererModel wingphalangemiddle1L;
    public AdvancedRendererModel membrane2L;
    public AdvancedRendererModel wingphalange4L;
    public AdvancedRendererModel membrane4L;
    public AdvancedRendererModel wingphalangetipL;
    public AdvancedRendererModel membranetipL;
    public AdvancedRendererModel phalangeL22;
    public AdvancedRendererModel membrane3L;
    public AdvancedRendererModel wingphalange2R;
    public AdvancedRendererModel membrane1R;
    public AdvancedRendererModel wingphalange3R;
    public AdvancedRendererModel wingphalangemiddle1R;
    public AdvancedRendererModel membrane2R;
    public AdvancedRendererModel wingphalange4R;
    public AdvancedRendererModel membrane4R;
    public AdvancedRendererModel wingphalangetipR;
    public AdvancedRendererModel membranetipR;
    public AdvancedRendererModel wingphalangemiddle2R;
    public AdvancedRendererModel membrane3R;
    
    private AdvancedRendererModel[] toeArray;
    private AdvancedRendererModel[] headArray;
    private AdvancedRendererModel[] neckArray;
    private AdvancedRendererModel[] neckArray2;
    private AdvancedRendererModel[] tailArray;
    
    private ModelAnimator animator;
    
    private boolean isSleeping, isFlying;
    private float globalSpeed, frame;
    private Animation currentAnim;
    
    public SilverGliderModel() {
        textureWidth = 160;
        textureHeight = 125;
        legR3 = new AdvancedRendererModel(this, 58, 14);
        legR3.setRotationPoint(-0.02F, 2.5F, 0.1F);
        legR3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        setRotateAngle(legR3, -1.3632766787327708F, 0.0F, 0.0F);
        backFin = new AdvancedRendererModel(this, 0, -14);
        backFin.setRotationPoint(0.0F, -1.8F, -2.8F);
        backFin.addBox(0.0F, -7.6F, -2.3F, 0, 8, 14, 0.0F);
        setRotateAngle(backFin, 3.875409442231813E-18F, 0.0F, 0.0F);
        legL3 = new AdvancedRendererModel(this, 58, 14);
        legL3.setRotationPoint(0.02F, 2.5F, 0.1F);
        legL3.addBox(-1.1F, -0.7F, -0.2F, 2, 4, 1, 0.0F);
        setRotateAngle(legL3, -1.3632766787327708F, 0.0F, 0.0F);
        tail2 = new AdvancedRendererModel(this, 88, 53);
        tail2.setRotationPoint(-0.02F, -0.02F, 2.8F);
        tail2.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        membrane2L = new AdvancedRendererModel(this, 63, 100);
        membrane2L.setRotationPoint(1.3F, 0.0F, -1.4F);
        membrane2L.addBox(0.0F, 0.0F, -0.5F, 12, 0, 18, 0.0F);
        setRotateAngle(membrane2L, 0.0F, -0.17453292519943295F, 0.0F);
        legL1 = new AdvancedRendererModel(this, 56, 0);
        legL1.setRotationPoint(1.6F, 0.9F, 5.9F);
        legL1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        setRotateAngle(legL1, -0.5918411493512771F, 0.0F, 0.0F);
        neck1 = new AdvancedRendererModel(this, 88, 17);
        neck1.setRotationPoint(0.0F, 0.0F, -4.0F);
        neck1.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        mainbody = new AdvancedRendererModel(this, 70, 24);
        mainbody.setRotationPoint(0.0F, 16.0F, 0.0F);
        mainbody.addBox(-2.5F, -2.5F, -5.0F, 5, 5, 10, 0.0F);
        wingphalange4L = new AdvancedRendererModel(this, 33, 65);
        wingphalange4L.setRotationPoint(15.0F, 0.0F, 0.0F);
        wingphalange4L.addBox(0.0F, -0.5F, -0.5F, 25, 1, 1, 0.0F);
        setRotateAngle(wingphalange4L, 0.0F, -0.17453292519943295F, 0.0F);
        toe2L = new AdvancedRendererModel(this, 60, 20);
        toe2L.setRotationPoint(0.0F, 3.0F, 0.2F);
        toe2L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toe2L, -0.9560913642424937F, 0.0F, 0.0F);
        membrane4R = new AdvancedRendererModel(this, -16, 78);
        membrane4R.mirror = true;
        membrane4R.setRotationPoint(0.0F, -0.1F, 0.0F);
        membrane4R.addBox(-40.0F, 0.0F, -0.5F, 40, 0, 18, 0.0F);
        toe2R = new AdvancedRendererModel(this, 60, 20);
        toe2R.setRotationPoint(0.0F, 3.0F, 0.2F);
        toe2R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toe2R, -0.9560913642424937F, 0.0F, 0.0F);
        tailfin = new AdvancedRendererModel(this, 18, 30);
        tailfin.setRotationPoint(0.0F, 0.0F, 1.6F);
        tailfin.addBox(-5.0F, 0.0F, -1.6F, 10, 0, 12, 0.0F);
        membrane1R = new AdvancedRendererModel(this, 38, 100);
        membrane1R.mirror = true;
        membrane1R.setRotationPoint(0.0F, 0.1F, 0.0F);
        membrane1R.addBox(-11.0F, 0.0F, -0.5F, 11, 0, 18, 0.0F);
        setRotateAngle(membrane1R, 0.0F, -0.17453292519943295F, 0.0F);
        wingphalangetipR = new AdvancedRendererModel(this, 33, 65);
        wingphalangetipR.setRotationPoint(-24.5F, 0.0F, 0.0F);
        wingphalangetipR.addBox(-11.0F, -0.5F, -0.5F, 11, 1, 1, 0.0F);
        setRotateAngle(wingphalangetipR, 0.0F, 0.034033920413889425F, 0.0F);
        membrane3R = new AdvancedRendererModel(this, -16, 100);
        membrane3R.mirror = true;
        membrane3R.setRotationPoint(1.0F, 0.0F, 0.0F);
        membrane3R.addBox(-20.0F, 0.0F, -0.5F, 20, 0, 18, 0.0F);
        setRotateAngle(membrane3R, 0.0F, 0.074176493209759F, 0.0F);
        toe1R = new AdvancedRendererModel(this, 60, 20);
        toe1R.setRotationPoint(0.5F, 3.0F, 0.2F);
        toe1R.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toe1R, -0.9560913642424937F, -0.6373942428283291F, 0.0F);
        eyeL = new AdvancedRendererModel(this, 79, 2);
        eyeL.setRotationPoint(1.7F, -0.5F, -3.9F);
        eyeL.addBox(0.0F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        setRotateAngle(eyeL, 0.0F, 0.17453292519943295F, 0.0F);
        wingphalange3R = new AdvancedRendererModel(this, 0, 65);
        wingphalange3R.setRotationPoint(-14.0F, -0.3F, -0.5F);
        wingphalange3R.addBox(-15.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        setRotateAngle(wingphalange3R, 0.0F, 0.17453292519943295F, 0.0F);
        tail10 = new AdvancedRendererModel(this, 90, 60);
        tail10.setRotationPoint(0.02F, 0.02F, 2.0F);
        tail10.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        toe1L = new AdvancedRendererModel(this, 60, 20);
        toe1L.setRotationPoint(0.5F, 3.0F, 0.2F);
        toe1L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toe1L, -0.9560913642424937F, -0.6071201090352446F, 0.0F);
        wingphalange2L = new AdvancedRendererModel(this, 31, 58);
        wingphalange2L.setRotationPoint(9.0F, 0.01F, 0.0F);
        wingphalange2L.addBox(0.0F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
        setRotateAngle(wingphalange2L, 0.0F, 0.3490658503988659F, 0.0F);
        bottomFin = new AdvancedRendererModel(this, 0, -3);
        bottomFin.setRotationPoint(0.0F, 1.4F, 2.1F);
        bottomFin.addBox(0.0F, -0.1F, -6.4F, 0, 5, 12, 0.0F);
        mouthBottom = new AdvancedRendererModel(this, 84, 10);
        mouthBottom.setRotationPoint(0.1F, 1.0F, -4.5F);
        mouthBottom.addBox(-1.5F, -0.5F, -4.0F, 3, 1, 4, 0.0F);
        legR2 = new AdvancedRendererModel(this, 56, 8);
        legR2.setRotationPoint(0.42F, 3.1F, -0.1F);
        legR2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        setRotateAngle(legR2, 1.360135086079181F, 0.0F, 0.0F);
        wingphalangemiddle2R = new AdvancedRendererModel(this, 33, 69);
        wingphalangemiddle2R.setRotationPoint(-10.0F, 0.0F, 0.0F);
        wingphalangemiddle2R.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(wingphalangemiddle2R, 0.0F, 0.17453292519943295F, 0.0F);
        phalangeL22 = new AdvancedRendererModel(this, 33, 69);
        phalangeL22.setRotationPoint(10.0F, 0.0F, 0.0F);
        phalangeL22.addBox(0.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(phalangeL22, 0.0F, -0.17453292519943295F, 0.0F);
        tail7 = new AdvancedRendererModel(this, 90, 60);
        tail7.setRotationPoint(-0.02F, -0.02F, 2.0F);
        tail7.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        head = new AdvancedRendererModel(this, 80, 0);
        head.setRotationPoint(-0.1F, -0.1F, -2.0F);
        head.addBox(-2.4F, -2.0F, -4.8F, 5, 4, 5, 0.0F);
        neck3 = new AdvancedRendererModel(this, 88, 17);
        neck3.setRotationPoint(-0.02F, -0.02F, -2.0F);
        neck3.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        membranetipR = new AdvancedRendererModel(this, 74, 78);
        membranetipR.setRotationPoint(-5.2F, 0.2F, 0.0F);
        membranetipR.addBox(-5.7F, -0.5F, -0.5F, 11, 0, 10, 0.0F);
        setRotateAngle(membranetipR, 0.0F, -0.01291543646475804F, 0.0F);
        wingphalange2R = new AdvancedRendererModel(this, 31, 58);
        wingphalange2R.setRotationPoint(-9.0F, 0.01F, 0.0F);
        wingphalange2R.addBox(-15.0F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
        setRotateAngle(wingphalange2R, 0.0F, -0.3490658503988659F, 0.0F);
        toe3L = new AdvancedRendererModel(this, 60, 20);
        toe3L.setRotationPoint(-0.5F, 3.0F, 0.2F);
        toe3L.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toe3L, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        secondarybody = new AdvancedRendererModel(this, 76, 40);
        secondarybody.setRotationPoint(0.0F, -0.3F, 4.9F);
        secondarybody.addBox(-2.1F, -2.1F, -0.5F, 4, 4, 8, 0.0F);
        wingphalange1R = new AdvancedRendererModel(this, 0, 58);
        wingphalange1R.setRotationPoint(-1.5F, -1.0F, -3.0F);
        wingphalange1R.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        setRotateAngle(wingphalange1R, 0.0F, 0.17453292519943295F, 0.0F);
        tail4 = new AdvancedRendererModel(this, 88, 53);
        tail4.setRotationPoint(-0.02F, -0.02F, 2.8F);
        tail4.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        membrane2R = new AdvancedRendererModel(this, 63, 100);
        membrane2R.mirror = true;
        membrane2R.setRotationPoint(-1.3F, 0.0F, -1.4F);
        membrane2R.addBox(-12.0F, 0.0F, -0.5F, 12, 0, 18, 0.0F);
        setRotateAngle(membrane2R, 0.0F, 0.17453292519943295F, 0.0F);
        neck2 = new AdvancedRendererModel(this, 88, 17);
        neck2.setRotationPoint(0.02F, 0.02F, -2.0F);
        neck2.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        wingphalangemiddle1R = new AdvancedRendererModel(this, 0, 70);
        wingphalangemiddle1R.setRotationPoint(-14.0F, -0.2F, 0.5F);
        wingphalangemiddle1R.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(wingphalangemiddle1R, 0.0F, 0.7740535232594852F, 0.0F);
        tail3 = new AdvancedRendererModel(this, 88, 53);
        tail3.setRotationPoint(0.02F, 0.02F, 2.8F);
        tail3.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        mouthTop = new AdvancedRendererModel(this, 67, 10);
        mouthTop.setRotationPoint(0.1F, -0.7F, -4.5F);
        mouthTop.addBox(-2.0F, -1.0F, -5.0F, 4, 2, 5, 0.0F);
        setRotateAngle(mouthTop, 0.31869712141416456F, 0.0F, 0.0F);
        neck4 = new AdvancedRendererModel(this, 88, 17);
        neck4.setRotationPoint(0.02F, 0.02F, -2.0F);
        neck4.addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3, 0.0F);
        tail8 = new AdvancedRendererModel(this, 90, 60);
        tail8.setRotationPoint(0.02F, 0.02F, 2.0F);
        tail8.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        eyeR = new AdvancedRendererModel(this, 79, 2);
        eyeR.setRotationPoint(-1.8F, -0.5F, -3.9F);
        eyeR.addBox(-0.7F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        setRotateAngle(eyeR, 0.0F, -0.17453292519943295F, 0.0F);
        toe1R_1 = new AdvancedRendererModel(this, 60, 20);
        toe1R_1.setRotationPoint(-0.5F, 3.0F, 0.2F);
        toe1R_1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        setRotateAngle(toe1R_1, -0.9560913642424937F, 0.6373942428283291F, 0.0F);
        membranetipL = new AdvancedRendererModel(this, 98, 78);
        membranetipL.setRotationPoint(4.7F, 0.2F, 0.0F);
        membranetipL.addBox(-5.4F, -0.4F, -0.5F, 11, 0, 10, 0.0F);
        setRotateAngle(membranetipL, 0.0F, -0.01291543646475804F, 0.0F);
        tail6 = new AdvancedRendererModel(this, 90, 60);
        tail6.setRotationPoint(0.02F, 0.02F, 2.0F);
        tail6.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        wingphalangetipL = new AdvancedRendererModel(this, 33, 65);
        wingphalangetipL.setRotationPoint(24.7F, 0.0F, 0.0F);
        wingphalangetipL.addBox(-0.5F, -0.5F, -0.5F, 11, 1, 1, 0.0F);
        setRotateAngle(wingphalangetipL, 0.0F, 0.030543261909900768F, 0.0F);
        wingphalange3L = new AdvancedRendererModel(this, 0, 65);
        wingphalange3L.setRotationPoint(14.0F, -0.3F, -0.5F);
        wingphalange3L.addBox(0.0F, -0.5F, -0.5F, 15, 1, 1, 0.0F);
        setRotateAngle(wingphalange3L, 0.0F, -0.17453292519943295F, 0.0F);
        tail1 = new AdvancedRendererModel(this, 88, 53);
        tail1.setRotationPoint(0.02F, 0.02F, 7.0F);
        tail1.addBox(-1.6F, -1.7F, -0.4F, 3, 3, 3, 0.0F);
        membrane4L = new AdvancedRendererModel(this, -16, 78);
        membrane4L.setRotationPoint(0.0F, -0.1F, 0.0F);
        membrane4L.addBox(0.0F, 0.0F, -0.5F, 40, 0, 18, 0.0F);
        tail9 = new AdvancedRendererModel(this, 90, 60);
        tail9.setRotationPoint(-0.02F, -0.02F, 2.0F);
        tail9.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        membrane3L = new AdvancedRendererModel(this, -16, 100);
        membrane3L.setRotationPoint(-1.0F, 0.0F, 0.0F);
        membrane3L.addBox(0.0F, 0.0F, -0.5F, 20, 0, 18, 0.0F);
        setRotateAngle(membrane3L, 0.0F, -0.11117747335203879F, 0.0F);
        legR1 = new AdvancedRendererModel(this, 56, 0);
        legR1.setRotationPoint(-2.3F, 0.9F, 5.9F);
        legR1.addBox(-0.7F, -0.9F, -0.8F, 2, 5, 2, 0.0F);
        setRotateAngle(legR1, -0.5918411493512771F, 0.0F, 0.0F);
        wingphalange1L = new AdvancedRendererModel(this, 0, 58);
        wingphalange1L.setRotationPoint(1.5F, -1.0F, -3.0F);
        wingphalange1L.addBox(0.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        setRotateAngle(wingphalange1L, 0.0F, -0.17453292519943295F, 0.0F);
        membrane1L = new AdvancedRendererModel(this, 38, 100);
        membrane1L.setRotationPoint(0.0F, 0.1F, 0.0F);
        membrane1L.addBox(0.0F, 0.0F, -0.5F, 11, 0, 18, 0.0F);
        setRotateAngle(membrane1L, 0.0F, 0.17453292519943295F, 0.0F);
        wingphalange4R = new AdvancedRendererModel(this, 33, 65);
        wingphalange4R.setRotationPoint(-15.0F, 0.0F, 0.0F);
        wingphalange4R.addBox(-25.0F, -0.5F, -0.5F, 25, 1, 1, 0.0F);
        setRotateAngle(wingphalange4R, 0.0F, 0.17453292519943295F, 0.0F);
        tail5 = new AdvancedRendererModel(this, 90, 60);
        tail5.setRotationPoint(-0.1F, -0.1F, 2.0F);
        tail5.addBox(-1.0F, -1.1F, 0.0F, 2, 2, 3, 0.0F);
        wingphalangemiddle1L = new AdvancedRendererModel(this, 0, 70);
        wingphalangemiddle1L.setRotationPoint(14.0F, -0.2F, 0.5F);
        wingphalangemiddle1L.addBox(0.0F, -0.5F, -0.5F, 10, 1, 1, 0.0F);
        setRotateAngle(wingphalangemiddle1L, 0.0F, -0.7740535232594852F, 0.0F);
        legL2 = new AdvancedRendererModel(this, 56, 8);
        legL2.setRotationPoint(0.38F, 3.1F, -0.1F);
        legL2.addBox(-1.1F, -0.2F, -1.0F, 2, 3, 2, 0.0F);
        setRotateAngle(legL2, 1.360135086079181F, 0.0F, 0.0F);
        legR2.addChild(legR3);
        mainbody.addChild(backFin);
        legL2.addChild(legL3);
        tail1.addChild(tail2);
        wingphalange2L.addChild(membrane2L);
        secondarybody.addChild(legL1);
        mainbody.addChild(neck1);
        wingphalange3L.addChild(wingphalange4L);
        legL3.addChild(toe2L);
        wingphalange3R.addChild(membrane4R);
        legR3.addChild(toe2R);
        tail10.addChild(tailfin);
        wingphalange1R.addChild(membrane1R);
        wingphalange4R.addChild(wingphalangetipR);
        wingphalangemiddle1R.addChild(membrane3R);
        legR3.addChild(toe1R);
        head.addChild(eyeL);
        wingphalange2R.addChild(wingphalange3R);
        tail9.addChild(tail10);
        legL3.addChild(toe1L);
        wingphalange1L.addChild(wingphalange2L);
        mainbody.addChild(bottomFin);
        head.addChild(mouthBottom);
        legR1.addChild(legR2);
        wingphalangemiddle1R.addChild(wingphalangemiddle2R);
        wingphalangemiddle1L.addChild(phalangeL22);
        tail6.addChild(tail7);
        neck4.addChild(head);
        neck2.addChild(neck3);
        wingphalangetipR.addChild(membranetipR);
        wingphalange1R.addChild(wingphalange2R);
        legL3.addChild(toe3L);
        mainbody.addChild(secondarybody);
        mainbody.addChild(wingphalange1R);
        tail3.addChild(tail4);
        wingphalange2R.addChild(membrane2R);
        neck1.addChild(neck2);
        wingphalange2R.addChild(wingphalangemiddle1R);
        tail2.addChild(tail3);
        head.addChild(mouthTop);
        neck3.addChild(neck4);
        tail7.addChild(tail8);
        head.addChild(eyeR);
        legR3.addChild(toe1R_1);
        wingphalangetipL.addChild(membranetipL);
        tail5.addChild(tail6);
        wingphalange4L.addChild(wingphalangetipL);
        wingphalange2L.addChild(wingphalange3L);
        secondarybody.addChild(tail1);
        wingphalange3L.addChild(membrane4L);
        tail8.addChild(tail9);
        wingphalangemiddle1L.addChild(membrane3L);
        secondarybody.addChild(legR1);
        mainbody.addChild(wingphalange1L);
        wingphalange1L.addChild(membrane1L);
        wingphalange3R.addChild(wingphalange4R);
        tail4.addChild(tail5);
        wingphalange2L.addChild(wingphalangemiddle1L);
        legL1.addChild(legL2);
    
        toeArray = new AdvancedRendererModel[] {toe1L, toe2L, toe3L, toe1R_1, toe1R, toe2R};
        headArray = new AdvancedRendererModel[] {neck1, neck2, neck3, neck4, head};
        neckArray = new AdvancedRendererModel[] {neck1, neck2, neck3, neck4};
        neckArray2 = new AdvancedRendererModel[] {neck1, neck2};
        tailArray = new AdvancedRendererModel[] {tail1, tail2, tail3, tail4, tail5, tail6, tail7, tail8, tail9, tail10};
        
        updateDefaultPose();
        animator = ModelAnimator.create();
    }
    
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        SilverGliderEntity gliderEntity = (SilverGliderEntity) entity;
    
        GlStateManager.pushMatrix();
        if (gliderEntity.isChild()) {
            GlStateManager.scaled(0.35d, 0.35d, 0.35d);
            GlStateManager.translated(0, 2.75d, 0);
        }
    
        mainbody.render(scale);
    
        GlStateManager.popMatrix();
    }
    
    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (!isSleeping) faceTarget(netHeadYaw, headPitch, 1, headArray);
    }
    
    @Override
    public void setLivingAnimations(Entity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.glider = (SilverGliderEntity) entityIn;
        this.isSitting = glider.isSitting();
        this.isSleeping = glider.isSleeping();
        this.isFlying = glider.isFlying();
        this.frame = entityIn.ticksExisted;
        this.globalSpeed = 0.05f;
        Animation current = this.currentAnim = glider.getAnimation();

        resetToDefaultPose();
        animator.update(glider);
        setPositions(isFlying && (current != TAKE_OFF_ANIMATION && glider.isFlying()) || (current == TAKE_OFF_ANIMATION && !glider.isFlying()));
    
        if (current == TAKE_OFF_ANIMATION) takeOffAnim();
        
        if (isFlying) { // Flight Only Anims
            globalSpeed = 0.3f;
            
            Random rand = new Random();
            swing(wingphalangetipL, globalSpeed * rand.nextFloat() + 1f, Math.min(rand.nextFloat(), 0.15f), false, 0, 0, frame, 0.5f);
            swing(wingphalangetipR, globalSpeed * rand.nextFloat() + 1f, Math.min(rand.nextFloat(), 0.15f), false, 0, 0, frame, 0.5f);
            
        } else { // Ground only Anims
            if (!glider.hasActiveAnimation() && !isSleeping && !isSitting) { // Walk Cycle
                float walkSpeed = globalSpeed * 9;
                
                bob(mainbody, walkSpeed + 0.1f, 0.4f, false, limbSwing, 0.5f);
    
                walk(wingphalange1L, walkSpeed, 0.4f, false, 0, 0, limbSwing, limbSwingAmount);
                swing(wingphalange2L, walkSpeed, 0.3f, true, -1.2f, 0, limbSwing, limbSwingAmount);
                swing(wingphalange3L, walkSpeed, 0.1f, true, -0.4f, 0, limbSwing, limbSwingAmount);
                swing(wingphalangemiddle1L, walkSpeed, 0.1f, true, -0.4f, 0, limbSwing, limbSwingAmount);
                swing(membrane2L, walkSpeed, 0.25f, false, -1.2f, 0, limbSwing, limbSwingAmount);
    
                walk(wingphalange1R, walkSpeed, -0.4f, false, 0, 0, limbSwing, limbSwingAmount);
                swing(wingphalange2R, walkSpeed, 0.3f, true, -1.2f, 0, limbSwing, limbSwingAmount);
                swing(wingphalange3R, walkSpeed, 0.1f, true, -0.4f, 0, limbSwing, limbSwingAmount);
                swing(wingphalangemiddle1R, walkSpeed, 0.1f, true, -0.4f, 0, limbSwing, limbSwingAmount);
                swing(membrane2R, walkSpeed, 0.25f, false, -1.2f, 0, limbSwing, limbSwingAmount);
    
                walk(legL1, walkSpeed, 0.4f, false, 0, 0.1f, limbSwing, limbSwingAmount);
                walk(legR1, walkSpeed, 0.4f, true, 0, 0.1f, limbSwing, limbSwingAmount);
                for (AdvancedRendererModel toe : new AdvancedRendererModel[] {toe1L, toe2L, toe3L})
                    walk(toe, walkSpeed, 0.4f, true, -0.6f, 0, limbSwing, limbSwingAmount);
                for (AdvancedRendererModel toe : new AdvancedRendererModel[] {toe1R, toe2R, toe1R_1})
                    walk(toe, walkSpeed, 0.4f, false, -0.6f, 0, limbSwing, limbSwingAmount);
            }
            else if (current == SIT_ANIMATION) {
                animator.setAnimation(SIT_ANIMATION);
                animator.startKeyframe(10);
                restKeyFrame(true);
                animator.endKeyframe();
            }
            else if (current == STAND_ANIMATION) {
                animator.setAnimation(STAND_ANIMATION);
                animator.startKeyframe(10);
                restKeyFrame(false);
                animator.endKeyframe();
            }
            else if (current == SLEEP_ANIMATION) sleepAnim();
            else if (current == WAKE_ANIMATION) wakeAnim();
        }
    
        idleAnim();
    }
    
    private void setPositions(boolean flight) {
        boolean flag = (isSitting || isSleeping) && (currentAnim != SIT_ANIMATION && currentAnim != SLEEP_ANIMATION) || (currentAnim == STAND_ANIMATION || currentAnim == WAKE_ANIMATION);
        
        // Neck
        for (AdvancedRendererModel box : neckArray2) box.rotateAngleX = -0.4f;
        neck3.rotateAngleX = 0.5f;
        neck4.rotateAngleX = 0.25f;
    
        // Pull the legs in for sitting or sleeping
        if (flag || glider.isRiding() || isFlying) {
            legL1.rotateAngleX = -1.2f;
            legL2.rotateAngleX = 2.3f;
            legL3.rotateAngleX = -2.5f;
            legR1.rotateAngleX = -1.2f;
            legR2.rotateAngleX = 2.3f;
            legR3.rotateAngleX = -2.5f;
        }
        
        if (flight) {
            Vec3d lookVec = glider.getLookVec();
            float yVec = (float) lookVec.y;
            
            mainbody.rotateAngleX = Math.max(-yVec * 1.2f, 0);
            wingphalangetipL.rotateAngleZ = -(Math.max(Math.min(yVec, 0.2f), 0) + 0.25f);
            wingphalangetipR.rotateAngleZ = Math.max(Math.min(yVec, 0.2f), 0) + 0.25f;
            if (lookVec.y < 0) {
                wingphalange1L.rotateAngleY = Math.min(yVec * 1.3f, wingphalange1L.defaultRotationY);
                wingphalange3L.rotateAngleY = Math.min(yVec / 2.5f, wingphalange3L.defaultRotationY);
                wingphalangemiddle1L.rotateAngleY += yVec / 3f;
                wingphalangetipL.rotateAngleY = Math.min(yVec, wingphalangetipL.defaultRotationY);
                
                wingphalange1R.rotateAngleY = -Math.min(yVec * 1.3f, -wingphalange1R.defaultRotationY);
                wingphalange3R.rotateAngleY = -Math.min(yVec / 2.5f, -wingphalange3R.defaultRotationY);
                wingphalangemiddle1R.rotateAngleY += -yVec / 3f;
                wingphalangetipR.rotateAngleY = -Math.min(yVec, -wingphalangetipR.defaultRotationY);
            } else {
                float max = 0.5f;
                wingphalange1L.rotateAngleX = -Math.min(yVec, max);
                wingphalange1R.rotateAngleX = -Math.min(yVec, max);
            }
        } else {
            if (!glider.isRiding()) { // Default ground pose
                // Left Wing
                wingphalange1L.rotateAngleX = 0.6f;
                wingphalange1L.rotateAngleY = -0.1f;
                wingphalange2L.rotateAngleY = 1.3f;
                wingphalange3L.rotateAngleY = -1.8f;
                wingphalangemiddle1L.rotateAngleY = -2.0f;
                wingphalangetipL.rotateAngleY = -0.3f;
                membrane2L.rotateAngleY = -0.9f;
                membrane2L.rotateAngleZ = 0.06f;
                membrane3L.rotateAngleY = 0.1f;
    
                // Right wing
                wingphalange1R.rotateAngleX = 0.6f;
                wingphalange1R.rotateAngleY = 0.1f;
                wingphalange2R.rotateAngleY = -1.3f;
                wingphalange3R.rotateAngleY = 1.8f;
                wingphalangemiddle1R.rotateAngleY = 2.0f;
                wingphalangetipR.rotateAngleY = 0.3f;
                membrane2R.rotateAngleY = 0.9f;
                membrane3R.rotateAngleY = -0.1f;
            } else {
                // Wing Poses for riding
                // Left Wing
                wingphalange1L.rotateAngleY = -1.2f;
                wingphalange1L.rotateAngleX = 2.3f;
                wingphalange1L.rotateAngleZ = -1f;
                wingphalange2L.rotateAngleY = 1.7f;
                membrane1L.rotateAngleY = 0.9f;
                wingphalange3L.rotateAngleY = -1.6f;
                wingphalangemiddle1L.rotateAngleY = -2f;
                wingphalangetipL.rotateAngleY = -0.5f;
    
                // Right Wing
                wingphalange1R.rotateAngleY = 1.2f;
                wingphalange1R.rotateAngleX = 2.3f;
                wingphalange1R.rotateAngleZ = 1f;
                wingphalange2R.rotateAngleY = -1.7f;
                membrane1R.rotateAngleY = -0.9f;
                wingphalange3R.rotateAngleY = 1.6f;
                wingphalangemiddle1R.rotateAngleY = 2f;
                wingphalangetipR.rotateAngleY = 0.5f;
            }
            if ((currentAnim == SLEEP_ANIMATION || currentAnim == WAKE_ANIMATION) && isSitting) mainbody.offsetY = 0.25f;
            
            // Sitting Position / Sleep Position
            if (flag) {
                // Body
                mainbody.offsetY = 0.25f;
    
                // Left Wing
                wingphalange1L.rotateAngleX = 0.3f;
    
                // Right Wing
                wingphalange1R.rotateAngleX = 0.3f;
    
                // Toes
                for (AdvancedRendererModel toe : toeArray) toe.rotateAngleX = -0.2f;
                for (AdvancedRendererModel segment : tailArray) {
                    segment.rotateAngleX = -0.035f;
                    segment.rotateAngleY = -0.35f;
                }
    
                if (isSleeping || currentAnim == WAKE_ANIMATION) { // Sleeping position (Also controlled in sitting position)
                    wingphalange1R.rotateAngleX = 0.1f;
                    wingphalange1L.rotateAngleY = -0.6f;
                    wingphalange2L.rotateAngleY = 2.2f;
                    wingphalange3L.rotateAngleY = -2.1f;
                    wingphalangemiddle1L.rotateAngleY = -2.3f;
                    membrane2L.rotateAngleY = -1.75f;
                    membrane3L.rotateAngleY = 0.1f;
                    
                    for (AdvancedRendererModel segment : neckArray) {
                        segment.rotateAngleX = 0.1f;
                        segment.rotateAngleY = 0.5f;
                    }
                    head.rotateAngleX = -0.2f;
                    for (AdvancedRendererModel tailSegment : tailArray) {
                        tailSegment.rotateAngleY = -0.35f;
                        tailSegment.rotateAngleX = -0.035f;
                    }
        
                    globalSpeed = 0.03f;
                }
            }
        }
    }
    
    // animate the head and tail according to glider's state (flying or on ground)
    private void idleAnim() {
        if (isFlying) {
            chainWave(headArray, globalSpeed + 0.05f, 0.05f, 2.8, frame, 0.5f);
            chainWave(tailArray, globalSpeed, 0.05f, 2, frame, 0.5f);
        } else {
            // Neck
            chainWave(headArray, globalSpeed + 0.01f, 0.02f, -0.5d, frame, 0.5f);
    
            // Tail
            chainSwing(tailArray, globalSpeed, 0.03f, 0, frame, 0.5f);
            chainWave(tailArray, globalSpeed - 0.01f, isFlying? 0.05f : 0.06f, isFlying? 2 : 0, frame, 0.5f);
    
            // Wings
            swing(wingphalange3L, globalSpeed, 0.03f, false, 0, 0, frame, 0.5f);
            swing(wingphalangemiddle1L, globalSpeed, 0.03f, false, 0, 0, frame, 0.5f);
            swing(wingphalange3R, globalSpeed, 0.03f, true, 0, 0, frame, 0.5f);
            swing(wingphalangemiddle1R, globalSpeed, 0.03f, true, 0, 0, frame, 0.5f);
        }
    }
    
    private void restKeyFrame(boolean sit) {
        int i = sit? 1 : -1;
        
        animator.move(mainbody, 0, i * 4f, 0);
        animator.rotate(wingphalange1L, i * -0.3f, 0, 0);
        animator.rotate(wingphalange1R, i * -0.3f, 0, 0);
        animator.rotate(legR1, i * -0.6f, 0, 0);
        animator.rotate(legR2, i * 0.95f, 0, 0);
        animator.rotate(legR3, i * -1.15f, 0, 0);
        animator.rotate(legL1, i * -0.6f, 0, 0);
        animator.rotate(legL2, i * 0.95f, 0, 0);
        animator.rotate(legL3, i * -1.15f, 0, 0);
        for (AdvancedRendererModel toe : toeArray) animator.rotate(toe, i * 0.75f, 0, 0);
        for(AdvancedRendererModel segment : tailArray) animator.rotate(segment, i * -0.035f, i * -0.35f, 0);
    }
    
    private void sleepAnim() {
        animator.setAnimation(SLEEP_ANIMATION);
    
        animator.startKeyframe(20);
    
        if (!isSitting) restKeyFrame(true);
        
        animator.rotate(wingphalange1L, 0, -0.5f, 0);
        animator.rotate(wingphalange2L, 0, 0.9f, 0);
        animator.rotate(membrane2L, 0, -0.85f, 0);
        animator.rotate(wingphalange3L, 0, -0.35f, 0);
        animator.rotate(wingphalangemiddle1L, 0, -0.35f, 0);
    
        animator.rotate(wingphalange1R, -0.2f, 0, 0);
        animator.rotate(neck3, -0.5f, 0, 0);
        animator.rotate(neck4, -0.2f, 0, 0);
        animator.rotate(head, -0.3f, 0, 0);
        for (AdvancedRendererModel neckSegment : neckArray2) animator.rotate(neckSegment, 0.4f, 0, 0);
        for (AdvancedRendererModel neck : neckArray) animator.rotate(neck, 0.1f, 0.5f, 0);
        animator.endKeyframe();
    }
    
    private void wakeAnim() {
        animator.setAnimation(WAKE_ANIMATION);
    
        animator.startKeyframe(15);
    
        if (!glider.isSitting()) restKeyFrame(false);
        
        animator.rotate(wingphalange1L, 0.2f, 0, 0);
        animator.rotate(neck3, 0.5f, 0, 0);
        animator.rotate(neck4, 0.2f, 0, 0);
        animator.rotate(head, 0.3f, 0, 0);
        for (AdvancedRendererModel neckSegment : neckArray2) animator.rotate(neckSegment, -0.4f, 0, 0);
        for (AdvancedRendererModel neck : neckArray) animator.rotate(neck, -0.1f, -0.5f, 0);
        animator.endKeyframe();
    }
    
    
    
    private void takeOffAnim() {
        int i = isFlying? 1 : -1;
        animator.setAnimation(TAKE_OFF_ANIMATION);
        
        animator.startKeyframe(10);
        
        
        if (glider.isRiding()) {
            animator.rotate(wingphalange1L, i * -2.3f, i * (isFlying? 1.2f : 1.1f), i);
            animator.rotate(wingphalange2L, 0, i * (isFlying? -1.7f : -1.45f), 0);
            animator.rotate(wingphalange3L, 0, i * 1.6f, 0);
            animator.rotate(wingphalangemiddle1L, 0, i * 1.4f, 0);
            animator.rotate(wingphalangetipL, 0, i * 0.5f, 0);
            animator.rotate(membrane1L, 0, i * -0.9f, 0);
    
            animator.rotate(wingphalange1R, i * -2.3f, i * (isFlying? -1.2f : -1.1f), i * -1);
            animator.rotate(wingphalange2R, 0, i * (isFlying? 1.7f : 1.45f), 0);
            animator.rotate(wingphalange3R, 0, i * -1.6f, 0);
            animator.rotate(wingphalangemiddle1R, 0, i * -1.4f, 0);
            animator.rotate(wingphalangetipR, 0, i * -0.5f, 0);
            animator.rotate(membrane1R, 0, i * 0.9f, 0);
        } else {
            animator.rotate(wingphalange1L, i * -0.6f, i * -0.2f, 0);
            animator.rotate(wingphalange2L, 0, i * -0.9f, 0);
            animator.rotate(wingphalange3L, 0, i * 1.8f, 0);
            animator.rotate(wingphalangemiddle1L, 0, i * 1.5f, 0);
            animator.rotate(wingphalangetipL, 0, i * 0.3f, 0);
            animator.rotate(membrane2L, 0, i * 0.7f, i * -0.06f);
            animator.rotate(membrane3L, 0, i * -0.3f, 0);
    
            animator.rotate(wingphalange1R, i * -0.6f, i * 0.2f, 0);
            animator.rotate(wingphalange2R, 0, i * 0.9f, 0);
            animator.rotate(wingphalange3R, 0, i * -1.8f, 0);
            animator.rotate(wingphalangemiddle1R, 0, i * -1.5f, 0);
            animator.rotate(wingphalangetipR, 0, i * -0.3f, 0);
            animator.rotate(membrane2R, 0, i * -0.7f, i * -0.06f);
            animator.rotate(membrane3R, 0, i * 0.3f, 0);
        }
        animator.endKeyframe();
    }
}
