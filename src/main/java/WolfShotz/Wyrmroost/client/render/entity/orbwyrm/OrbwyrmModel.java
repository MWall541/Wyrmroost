package WolfShotz.Wyrmroost.client.render.entity.orbwyrm;

import WolfShotz.Wyrmroost.client.model.ModelAnimator;
import WolfShotz.Wyrmroost.client.model.WREntityModel;
import WolfShotz.Wyrmroost.client.model.WRModelRenderer;
import WolfShotz.Wyrmroost.entities.dragon.OrbwyrmEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * WROrbwyrm - Ukan
 * Created using Tabula 8.0.0
 */
public class OrbwyrmModel extends WREntityModel<OrbwyrmEntity>
{
    private final WRModelRenderer[] tails;
    private final WRModelRenderer[] necks;
    private final WRModelRenderer[] eyes;
    private final ModelAnimator animator;
    public WRModelRenderer body1;
    public WRModelRenderer body2;
    public WRModelRenderer neck1;
    public WRModelRenderer wing1L;
    public WRModelRenderer wing1R;
    public WRModelRenderer arm1L;
    public WRModelRenderer arm1R;
    public WRModelRenderer quills4;
    public WRModelRenderer tail1;
    public WRModelRenderer quills5;
    public WRModelRenderer tail2;
    public WRModelRenderer quills6;
    public WRModelRenderer tail3;
    public WRModelRenderer quills7;
    public WRModelRenderer tail4;
    public WRModelRenderer tail5;
    public WRModelRenderer tail6;
    public WRModelRenderer tail7;
    public WRModelRenderer tail8;
    public WRModelRenderer spike0L;
    public WRModelRenderer spike0R;
    public WRModelRenderer tail9;
    public WRModelRenderer spike1L;
    public WRModelRenderer spike1R;
    public WRModelRenderer tail10;
    public WRModelRenderer quills8;
    public WRModelRenderer spike2L;
    public WRModelRenderer spike2R;
    public WRModelRenderer tail11;
    public WRModelRenderer quills9;
    public WRModelRenderer spike3L;
    public WRModelRenderer spike3R;
    public WRModelRenderer tail12;
    public WRModelRenderer spike4L;
    public WRModelRenderer spike4R;
    public WRModelRenderer tail13;
    public WRModelRenderer spike5L;
    public WRModelRenderer spike5R;
    public WRModelRenderer tail14;
    public WRModelRenderer spike6L;
    public WRModelRenderer spike6R;
    public WRModelRenderer spike7L;
    public WRModelRenderer spike7R;
    public WRModelRenderer neck2;
    public WRModelRenderer neck3;
    public WRModelRenderer quills3;
    public WRModelRenderer neck4;
    public WRModelRenderer quills2;
    public WRModelRenderer quills1;
    public WRModelRenderer neck5;
    public WRModelRenderer quills1_1;
    public WRModelRenderer neck6;
    public WRModelRenderer head;
    public WRModelRenderer nose;
    public WRModelRenderer jawR;
    public WRModelRenderer horn1L;
    public WRModelRenderer horn1R;
    public WRModelRenderer eye1L;
    public WRModelRenderer eye2L;
    public WRModelRenderer eye3L;
    public WRModelRenderer eye1R;
    public WRModelRenderer eye2R;
    public WRModelRenderer eye3R;
    public WRModelRenderer jawL;
    public WRModelRenderer teeth;
    public WRModelRenderer chinR;
    public WRModelRenderer cheekR;
    public WRModelRenderer teethR;
    public WRModelRenderer horn2L;
    public WRModelRenderer horn2R;
    public WRModelRenderer chinL;
    public WRModelRenderer cheekL;
    public WRModelRenderer teethL;
    public WRModelRenderer wing2L;
    public WRModelRenderer palmL;
    public WRModelRenderer clawL2;
    public WRModelRenderer clawL1;
    public WRModelRenderer clawL3;
    public WRModelRenderer wing2R;
    public WRModelRenderer palmR;
    public WRModelRenderer clawR2;
    public WRModelRenderer clawR3;
    public WRModelRenderer clawR1;
    public WRModelRenderer arm2L;
    public WRModelRenderer spikeL;
    public WRModelRenderer arm2R;
    public WRModelRenderer spikeR;

    public OrbwyrmModel()
    {
        textureWidth = 150;
        textureHeight = 100;
        spike4R = new WRModelRenderer(this, 102, 19);
        spike4R.setRotationPoint(-1.0F, 0.0F, 4.0F);
        spike4R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike4R, 0.0F, 0.7853981633974483F, 0.0F);
        horn2R = new WRModelRenderer(this, 16, 78);
        horn2R.mirror = true;
        horn2R.setRotationPoint(0.0F, -0.2F, 3.5F);
        horn2R.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(horn2R, 0.18203784630933073F, -0.2275909337942703F, 0.0F);
        quills4 = new WRModelRenderer(this, 95, 20);
        quills4.setRotationPoint(0.0F, -4.0F, 0.0F);
        quills4.addBox(0.0F, -5.0F, -6.0F, 0.0F, 5.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills4, -0.08726646259971647F, 0.0F, 0.0F);
        horn1R = new WRModelRenderer(this, 16, 71);
        horn1R.mirror = true;
        horn1R.setRotationPoint(-1.8F, -2.0F, -1.2F);
        horn1R.addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(horn1R, 0.18203784630933073F, -0.18203784630933073F, 0.0F);
        body2 = new WRModelRenderer(this, 45, 0);
        body2.setRotationPoint(0.0F, 0.0F, 4.0F);
        body2.addBox(-4.5F, -4.5F, 0.0F, 9.0F, 9.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(body2, -0.18203784630933073F, 0.0F, 0.0F);
        quills3 = new WRModelRenderer(this, 100, 20);
        quills3.setRotationPoint(0.0F, -2.5F, -2.0F);
        quills3.addBox(0.0F, -4.0F, -3.0F, 0.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills3, -0.08726646259971647F, 0.0F, 0.0F);
        chinR = new WRModelRenderer(this, 13, 58);
        chinR.mirror = true;
        chinR.setRotationPoint(1.5F, 0.8F, -6.0F);
        chinR.addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 2.0F, -0.01F, 0.0F, 0.0F);
        setRotateAngle(chinR, -0.6318092025377527F, 0.0F, 0.0F);
        horn1L = new WRModelRenderer(this, 16, 71);
        horn1L.setRotationPoint(1.8F, -2.0F, -1.2F);
        horn1L.addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(horn1L, 0.18203784630933073F, 0.18203784630933073F, 0.0F);
        spike7R = new WRModelRenderer(this, 102, 19);
        spike7R.setRotationPoint(-0.5F, 0.0F, 4.0F);
        spike7R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike7R, 0.0F, 0.7853981633974483F, 0.0F);
        tail11 = new WRModelRenderer(this, 127, 58);
        tail11.setRotationPoint(0.0F, 0.0F, 7.0F);
        tail11.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        tail9 = new WRModelRenderer(this, 125, 45);
        tail9.setRotationPoint(0.0F, 0.0F, 7.0F);
        tail9.addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        spike0R = new WRModelRenderer(this, 102, 19);
        spike0R.setRotationPoint(-2.0F, 0.0F, 4.0F);
        spike0R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike0R, 0.0F, 0.7853981633974483F, 0.0F);
        cheekR = new WRModelRenderer(this, 0, 73);
        cheekR.setRotationPoint(-0.4F, 1.0F, 0.0F);
        cheekR.addBox(-0.5F, -2.0F, -6.0F, 1.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(cheekR, 0.0F, -0.0911061832922575F, -0.3642502295386026F);
        eye3L = new WRModelRenderer(this, 14, 65);
        eye3L.setRotationPoint(2.2F, -2.2F, -1.1F);
        eye3L.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(eye3L, 0.0F, 0.3186971254089062F, 0.0F);
        eye2L = new WRModelRenderer(this, 14, 65);
        eye2L.setRotationPoint(2.2F, -1.9F, -3.1F);
        eye2L.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(eye2L, 0.0F, 0.3186971254089062F, 0.0F);
        jawR = new WRModelRenderer(this, 0, 64);
        jawR.setRotationPoint(-2.0F, 0.5F, -1.0F);
        jawR.addBox(0.0F, 0.0F, -6.0F, 3.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(jawR, -0.11728612207217244F, 0.11728612207217244F, 0.5473352640780661F);
        spike1R = new WRModelRenderer(this, 102, 19);
        spike1R.setRotationPoint(-2.0F, 0.0F, 4.0F);
        spike1R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike1R, 0.0F, 0.7853981633974483F, 0.0F);
        wing2R = new WRModelRenderer(this, 40, 22);
        wing2R.setRotationPoint(0.0F, 10.5F, 0.0F);
        wing2R.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 22.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wing2R, -0.3490658503988659F, 0.0F, -1.639038764463741F);
        neck6 = new WRModelRenderer(this, 30, 57);
        neck6.setRotationPoint(-0.0F, -0.0F, -3.5F);
        neck6.addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 5.0F, -0.6F, 0.0F, 0.0F);
        setRotateAngle(neck6, 0.27960175415897487F, 0.0F, 0.0F);
        tail4 = new WRModelRenderer(this, 117, 0);
        tail4.mirror = true;
        tail4.setRotationPoint(-0.0F, -0.0F, 6.5F);
        tail4.addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        setRotateAngle(tail4, -0.05235987755982988F, 0.0F, 0.0F);
        wing1L = new WRModelRenderer(this, 27, 22);
        wing1L.setRotationPoint(3.7F, -3.0F, 3.0F);
        wing1L.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wing1L, 0.3490658503988659F, 0.0F, -2.0943951023931953F);
        tail6 = new WRModelRenderer(this, 121, 16);
        tail6.mirror = true;
        tail6.setRotationPoint(-0.0F, -0.0F, 6.5F);
        tail6.addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        setRotateAngle(tail6, 0.22689280275926282F, 0.0F, 0.0F);
        horn2L = new WRModelRenderer(this, 16, 78);
        horn2L.setRotationPoint(0.0F, -0.2F, 3.5F);
        horn2L.addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(horn2L, 0.18203784630933073F, 0.2275909337942703F, 0.0F);
        jawL = new WRModelRenderer(this, 0, 64);
        jawL.mirror = true;
        jawL.setRotationPoint(2.0F, 0.5F, -1.0F);
        jawL.addBox(-3.0F, 0.0F, -6.0F, 3.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(jawL, -0.11728612207217244F, -0.11728612207217244F, -0.5473352640780661F);
        spike1L = new WRModelRenderer(this, 102, 19);
        spike1L.mirror = true;
        spike1L.setRotationPoint(2.0F, 0.0F, 4.0F);
        spike1L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike1L, 0.0F, -0.7853981633974483F, 0.0F);
        palmL = new WRModelRenderer(this, 25, 38);
        palmL.setRotationPoint(0.0F, 21.0F, 0.0F);
        palmL.addBox(-2.0F, 0.0F, -1.5F, 4.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(palmL, -0.3186971254089062F, 0.0F, 0.500909508638178F);
        clawL2 = new WRModelRenderer(this, 57, 23);
        clawL2.setRotationPoint(-0.3F, 6.1F, -0.5F);
        clawL2.addBox(-0.5F, 0.0F, -0.9F, 1.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(clawL2, -0.9105382388075086F, 0.0F, 0.0F);
        quills2 = new WRModelRenderer(this, 100, 20);
        quills2.setRotationPoint(0.01F, -2.5F, -2.0F);
        quills2.addBox(0.0F, -4.0F, -3.0F, 0.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills2, -0.08726646259971647F, 0.0F, 0.0F);
        wing1R = new WRModelRenderer(this, 27, 22);
        wing1R.mirror = true;
        wing1R.setRotationPoint(-3.7F, -3.0F, 3.0F);
        wing1R.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wing1R, 0.3490658503988659F, 0.0F, 2.0943951023931953F);
        spike7L = new WRModelRenderer(this, 102, 19);
        spike7L.mirror = true;
        spike7L.setRotationPoint(0.5F, 0.0F, 4.0F);
        spike7L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike7L, 0.0F, -0.7853981633974483F, 0.0F);
        arm2R = new WRModelRenderer(this, 79, 23);
        arm2R.setRotationPoint(-1.0F, 4.0F, 0.0F);
        arm2R.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, -0.1F, 0.0F, -0.1F);
        setRotateAngle(arm2R, -1.6845917354353828F, -0.4098033003787853F, 0.0F);
        spike2R = new WRModelRenderer(this, 102, 19);
        spike2R.setRotationPoint(-1.5F, 0.0F, 4.0F);
        spike2R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike2R, 0.0F, 0.7853981633974483F, 0.0F);
        teethL = new WRModelRenderer(this, 0, 84);
        teethL.setRotationPoint(-1.5F, 1.0F, -3.0F);
        teethL.addBox(-1.5F, -2.0F, -3.0F, 3.0F, 2.0F, 6.0F, -0.4F, 0.0F, -0.01F);
        spike3R = new WRModelRenderer(this, 102, 19);
        spike3R.setRotationPoint(-1.5F, 0.0F, 4.0F);
        spike3R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike3R, 0.0F, 0.7853981633974483F, 0.0F);
        teeth = new WRModelRenderer(this, 25, 49);
        teeth.setRotationPoint(0.0F, -0.7F, -3.7F);
        teeth.addBox(-2.5F, 0.0F, -3.0F, 5.0F, 2.0F, 6.0F, -0.3F, 0.0F, 0.0F);
        quills1_1 = new WRModelRenderer(this, 100, 20);
        quills1_1.setRotationPoint(0.0F, -2.5F, -2.0F);
        quills1_1.addBox(0.0F, -4.0F, -3.0F, 0.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills1_1, -0.08726646259971647F, 0.0F, 0.0F);
        tail12 = new WRModelRenderer(this, 127, 58);
        tail12.mirror = true;
        tail12.setRotationPoint(-0.0F, -0.0F, 7.0F);
        tail12.addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        clawR2 = new WRModelRenderer(this, 57, 23);
        clawR2.setRotationPoint(0.0F, 6.1F, -0.8F);
        clawR2.addBox(-0.5F, 0.0F, -0.8F, 1.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(clawR2, -0.9105382388075086F, 0.0F, 0.0F);
        quills9 = new WRModelRenderer(this, 57, 25);
        quills9.setRotationPoint(0.01F, -1.5F, 4.5F);
        quills9.addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills9, -0.08726646259971647F, 0.0F, 0.0F);
        tail14 = new WRModelRenderer(this, 129, 70);
        tail14.mirror = true;
        tail14.setRotationPoint(-0.0F, -0.0F, 7.0F);
        tail14.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        tail2 = new WRModelRenderer(this, 84, 0);
        tail2.mirror = true;
        tail2.setRotationPoint(-0.0F, -0.0F, 6.5F);
        tail2.addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        setRotateAngle(tail2, -0.13962634015954636F, 0.0F, 0.0F);
        palmR = new WRModelRenderer(this, 25, 38);
        palmR.setRotationPoint(0.0F, 21.0F, 0.0F);
        palmR.addBox(-2.0F, 0.0F, -1.5F, 4.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(palmR, -0.3186971254089062F, 0.0F, -0.500909508638178F);
        eye2R = new WRModelRenderer(this, 14, 65);
        eye2R.mirror = true;
        eye2R.setRotationPoint(-2.2F, -1.9F, -3.1F);
        eye2R.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(eye2R, 0.0F, -0.3186971254089062F, 0.0F);
        quills6 = new WRModelRenderer(this, 57, 25);
        quills6.setRotationPoint(0.0F, -3.5F, 5.0F);
        quills6.addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills6, -0.08726646259971647F, 0.0F, 0.0F);
        spike2L = new WRModelRenderer(this, 102, 19);
        spike2L.mirror = true;
        spike2L.setRotationPoint(1.5F, 0.0F, 4.0F);
        spike2L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike2L, 0.0F, -0.7853981633974483F, 0.0F);
        clawR3 = new WRModelRenderer(this, 57, 23);
        clawR3.setRotationPoint(-1.2F, 6.1F, -0.5F);
        clawR3.addBox(-0.5F, 0.0F, -0.9F, 1.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(clawR3, -0.9105382388075086F, 0.13665927909957545F, 0.2275909337942703F);
        arm1L = new WRModelRenderer(this, 70, 23);
        arm1L.setRotationPoint(4.0F, 1.7F, -2.0F);
        arm1L.addBox(0.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(arm1L, 0.5462880425584197F, 0.0F, -0.45535640450848164F);
        eye1L = new WRModelRenderer(this, 14, 65);
        eye1L.setRotationPoint(2.2F, -2.4F, -5.0F);
        eye1L.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(eye1L, 0.0F, 0.3186971254089062F, 0.0F);
        spike3L = new WRModelRenderer(this, 102, 19);
        spike3L.mirror = true;
        spike3L.setRotationPoint(1.5F, 0.0F, 4.0F);
        spike3L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike3L, 0.0F, -0.7853981633974483F, 0.0F);
        arm1R = new WRModelRenderer(this, 70, 23);
        arm1R.setRotationPoint(-4.0F, 1.7F, -2.0F);
        arm1R.addBox(-2.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(arm1R, 0.5462880425584197F, 0.0F, 0.45535640450848164F);
        tail10 = new WRModelRenderer(this, 125, 45);
        tail10.mirror = true;
        tail10.setRotationPoint(-0.0F, -0.0F, 7.0F);
        tail10.addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        clawL1 = new WRModelRenderer(this, 57, 23);
        clawL1.setRotationPoint(-1.2F, 6.1F, -0.5F);
        clawL1.addBox(-0.5F, 0.0F, -0.9F, 1.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(clawL1, -0.9105382388075086F, 0.13665927909957545F, 0.2275909337942703F);
        tail13 = new WRModelRenderer(this, 129, 70);
        tail13.setRotationPoint(0.0F, 0.0F, 7.0F);
        tail13.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        spike6L = new WRModelRenderer(this, 102, 19);
        spike6L.mirror = true;
        spike6L.setRotationPoint(0.5F, 0.0F, 4.0F);
        spike6L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike6L, 0.0F, -0.7853981633974483F, 0.0F);
        wing2L = new WRModelRenderer(this, 40, 22);
        wing2L.mirror = true;
        wing2L.setRotationPoint(0.0F, 10.5F, 0.0F);
        wing2L.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 22.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(wing2L, -0.3490658503988659F, 0.0F, 1.639038764463741F);
        clawL3 = new WRModelRenderer(this, 57, 23);
        clawL3.setRotationPoint(1.2F, 6.1F, -0.5F);
        clawL3.addBox(-0.5F, 0.0F, -0.8F, 1.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(clawL3, -0.9105382388075086F, -0.13665927909957545F, -0.2275909337942703F);
        neck2 = new WRModelRenderer(this, 0, 35);
        neck2.mirror = true;
        neck2.setRotationPoint(0.0F, 0.0F, -3.5F);
        neck2.addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 5.0F, -0.2F, 0.0F, 0.0F);
        setRotateAngle(neck2, -0.13665927909957545F, 0.0F, 0.0F);
        quills8 = new WRModelRenderer(this, 57, 25);
        quills8.setRotationPoint(0.0F, -1.5F, 4.5F);
        quills8.addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills8, -0.08726646259971647F, 0.0F, 0.0F);
        spike0L = new WRModelRenderer(this, 102, 19);
        spike0L.mirror = true;
        spike0L.setRotationPoint(2.0F, 0.0F, 4.0F);
        spike0L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike0L, 0.0F, -0.7853981633974483F, 0.0F);
        neck3 = new WRModelRenderer(this, 0, 35);
        neck3.setRotationPoint(0.0F, 0.0F, -3.5F);
        neck3.addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 5.0F, -0.3F, 0.0F, 0.0F);
        setRotateAngle(neck3, -0.3186971254089062F, 0.0F, 0.0F);
        arm2L = new WRModelRenderer(this, 79, 23);
        arm2L.setRotationPoint(1.0F, 4.0F, 0.0F);
        arm2L.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, -0.1F, 0.0F, -0.1F);
        setRotateAngle(arm2L, -1.6845917354353828F, 0.4098033003787853F, 0.0F);
        spike6R = new WRModelRenderer(this, 102, 19);
        spike6R.setRotationPoint(-0.5F, 0.0F, 4.0F);
        spike6R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike6R, 0.0F, 0.7853981633974483F, 0.0F);
        spike5L = new WRModelRenderer(this, 102, 19);
        spike5L.mirror = true;
        spike5L.setRotationPoint(1.0F, 0.0F, 4.0F);
        spike5L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike5L, 0.0F, -0.7853981633974483F, 0.0F);
        nose = new WRModelRenderer(this, 0, 58);
        nose.setRotationPoint(0.0F, -1.0F, -6.4F);
        nose.addBox(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(nose, -0.4363323129985824F, 0.0F, 0.0F);
        eye1R = new WRModelRenderer(this, 14, 65);
        eye1R.mirror = true;
        eye1R.setRotationPoint(-2.2F, -2.4F, -5.0F);
        eye1R.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(eye1R, 0.0F, -0.3186971254089062F, 0.0F);
        chinL = new WRModelRenderer(this, 13, 58);
        chinL.setRotationPoint(-1.5F, 0.8F, -6.0F);
        chinL.addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 2.0F, -0.01F, 0.0F, 0.0F);
        setRotateAngle(chinL, -0.6318092025377527F, 0.0F, 0.0F);
        tail8 = new WRModelRenderer(this, 123, 31);
        tail8.mirror = true;
        tail8.setRotationPoint(-0.0F, -0.0F, 7.0F);
        tail8.addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 8.0F, -0.3F, -0.3F, 0.0F);
        quills1 = new WRModelRenderer(this, 100, 20);
        quills1.setRotationPoint(0.0F, -2.5F, -2.0F);
        quills1.addBox(0.0F, -4.0F, -3.0F, 0.0F, 4.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills1, -0.08726646259971647F, 0.0F, 0.0F);
        tail3 = new WRModelRenderer(this, 117, 0);
        tail3.setRotationPoint(0.0F, 0.0F, 6.5F);
        tail3.addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(tail3, -0.17453292519943295F, 0.0F, 0.0F);
        quills5 = new WRModelRenderer(this, 74, 23);
        quills5.setRotationPoint(0.01F, -4.0F, 5.0F);
        quills5.addBox(0.0F, -5.0F, -5.0F, 0.0F, 5.0F, 10.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills5, -0.08726646259971647F, 0.0F, 0.0F);
        head = new WRModelRenderer(this, 0, 47);
        head.setRotationPoint(0.0F, -0.15F, -4.0F);
        head.addBox(-2.5F, -3.0F, -7.0F, 5.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(head, 0.18203784630933073F, 0.0F, 0.0F);
        quills7 = new WRModelRenderer(this, 57, 25);
        quills7.setRotationPoint(0.01F, -3.5F, 5.0F);
        quills7.addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(quills7, -0.08726646259971647F, 0.0F, 0.0F);
        spikeL = new WRModelRenderer(this, 88, 23);
        spikeL.setRotationPoint(0.0F, 5.5F, 0.0F);
        spikeL.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spikeL, 1.8668041519541685F, -0.4098033003787853F, 0.0F);
        cheekL = new WRModelRenderer(this, 0, 73);
        cheekL.mirror = true;
        cheekL.setRotationPoint(0.4F, 1.0F, 0.0F);
        cheekL.addBox(-0.5F, -2.0F, -6.0F, 1.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(cheekL, 0.0F, 0.0911061832922575F, 0.3642502295386026F);
        spike5R = new WRModelRenderer(this, 102, 19);
        spike5R.setRotationPoint(-1.0F, 0.0F, 4.0F);
        spike5R.addBox(-3.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike5R, 0.0F, 0.7853981633974483F, 0.0F);
        spike4L = new WRModelRenderer(this, 102, 19);
        spike4L.mirror = true;
        spike4L.setRotationPoint(1.0F, 0.0F, 4.0F);
        spike4L.addBox(0.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spike4L, 0.0F, -0.7853981633974483F, 0.0F);
        teethR = new WRModelRenderer(this, 0, 84);
        teethR.setRotationPoint(1.5F, 1.0F, -3.0F);
        teethR.addBox(-1.5F, -2.0F, -3.0F, 3.0F, 2.0F, 6.0F, -0.4F, 0.0F, -0.01F);
        spikeR = new WRModelRenderer(this, 88, 23);
        spikeR.setRotationPoint(0.0F, 5.5F, 0.0F);
        spikeR.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(spikeR, 1.8668041519541685F, 0.4098033003787853F, 0.0F);
        eye3R = new WRModelRenderer(this, 14, 65);
        eye3R.mirror = true;
        eye3R.setRotationPoint(-2.2F, -2.2F, -1.1F);
        eye3R.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(eye3R, 0.0F, -0.3186971254089062F, 0.0F);
        neck5 = new WRModelRenderer(this, 0, 35);
        neck5.setRotationPoint(0.0F, 0.0F, -3.5F);
        neck5.addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 5.0F, -0.5F, 0.0F, 0.0F);
        setRotateAngle(neck5, 0.43598323915870024F, 0.0F, 0.0F);
        neck1 = new WRModelRenderer(this, 0, 22);
        neck1.setRotationPoint(0.0F, -0.3F, -4.0F);
        neck1.addBox(-3.5F, -3.5F, -5.0F, 7.0F, 7.0F, 5.0F, -0.3F, 0.0F, 0.0F);
        setRotateAngle(neck1, -0.500909508638178F, 0.0F, 0.0F);
        tail5 = new WRModelRenderer(this, 121, 16);
        tail5.setRotationPoint(0.0F, 0.0F, 6.5F);
        tail5.addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(tail5, 0.17453292519943295F, 0.0F, 0.0F);
        tail1 = new WRModelRenderer(this, 84, 0);
        tail1.setRotationPoint(0.0F, 0.0F, 8.0F);
        tail1.addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(tail1, -0.10471975511965977F, 0.0F, 0.0F);
        clawR1 = new WRModelRenderer(this, 57, 23);
        clawR1.setRotationPoint(1.2F, 6.1F, -0.5F);
        clawR1.addBox(-0.5F, 0.0F, -0.8F, 1.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(clawR1, -0.9105382388075086F, -0.13665927909957545F, -0.2275909337942703F);
        neck4 = new WRModelRenderer(this, 0, 35);
        neck4.mirror = true;
        neck4.setRotationPoint(0.0F, 0.0F, -3.5F);
        neck4.addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 5.0F, -0.4F, 0.0F, 0.0F);
        setRotateAngle(neck4, 0.27960175415897487F, 0.0F, 0.0F);
        body1 = new WRModelRenderer(this, 0, 0);
        body1.setRotationPoint(0.0F, 9.0F, 0.0F);
        body1.addBox(-5.0F, -4.5F, -6.0F, 10.0F, 9.0F, 12.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(body1, 0.13665927909957545F, 0.0F, 0.0F);
        tail7 = new WRModelRenderer(this, 123, 31);
        tail7.setRotationPoint(0.0F, 0.0F, 7.0F);
        tail7.addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        setRotateAngle(tail7, 0.06981317007977318F, 0.0F, 0.0F);
        tail11.addChild(spike4R);
        horn1R.addChild(horn2R);
        body1.addChild(quills4);
        head.addChild(horn1R);
        body1.addChild(body2);
        neck2.addChild(quills3);
        jawR.addChild(chinR);
        head.addChild(horn1L);
        tail14.addChild(spike7R);
        tail10.addChild(tail11);
        tail8.addChild(tail9);
        tail7.addChild(spike0R);
        jawR.addChild(cheekR);
        head.addChild(eye3L);
        head.addChild(eye2L);
        head.addChild(jawR);
        tail8.addChild(spike1R);
        wing1R.addChild(wing2R);
        neck5.addChild(neck6);
        tail3.addChild(tail4);
        body1.addChild(wing1L);
        tail5.addChild(tail6);
        horn1L.addChild(horn2L);
        head.addChild(jawL);
        tail8.addChild(spike1L);
        wing2L.addChild(palmL);
        palmL.addChild(clawL2);
        neck3.addChild(quills2);
        body1.addChild(wing1R);
        tail14.addChild(spike7L);
        arm1R.addChild(arm2R);
        tail9.addChild(spike2R);
        jawL.addChild(teethL);
        tail10.addChild(spike3R);
        head.addChild(teeth);
        neck5.addChild(quills1_1);
        tail11.addChild(tail12);
        palmR.addChild(clawR2);
        tail10.addChild(quills9);
        tail13.addChild(tail14);
        tail1.addChild(tail2);
        wing2R.addChild(palmR);
        head.addChild(eye2R);
        tail1.addChild(quills6);
        tail9.addChild(spike2L);
        palmR.addChild(clawR3);
        body1.addChild(arm1L);
        head.addChild(eye1L);
        tail10.addChild(spike3L);
        body1.addChild(arm1R);
        tail9.addChild(tail10);
        palmL.addChild(clawL1);
        tail12.addChild(tail13);
        tail13.addChild(spike6L);
        wing1L.addChild(wing2L);
        palmL.addChild(clawL3);
        neck1.addChild(neck2);
        tail9.addChild(quills8);
        tail7.addChild(spike0L);
        neck2.addChild(neck3);
        arm1L.addChild(arm2L);
        tail13.addChild(spike6R);
        tail12.addChild(spike5L);
        head.addChild(nose);
        head.addChild(eye1R);
        jawL.addChild(chinL);
        tail7.addChild(tail8);
        neck4.addChild(quills1);
        tail2.addChild(tail3);
        body2.addChild(quills5);
        neck6.addChild(head);
        tail2.addChild(quills7);
        arm2L.addChild(spikeL);
        jawL.addChild(cheekL);
        tail12.addChild(spike5R);
        tail11.addChild(spike4L);
        jawR.addChild(teethR);
        arm2R.addChild(spikeR);
        head.addChild(eye3R);
        neck4.addChild(neck5);
        body1.addChild(neck1);
        tail4.addChild(tail5);
        body2.addChild(tail1);
        palmR.addChild(clawR1);
        neck3.addChild(neck4);
        tail6.addChild(tail7);

        this.tails = new WRModelRenderer[] {tail1, tail2, tail3, tail4, tail5, tail6, tail7, tail8, tail9, tail10, tail11, tail12, tail13, tail14};
        this.necks = new WRModelRenderer[] {neck1, neck2, neck3, neck4, neck5, neck6, head};
        this.eyes = new WRModelRenderer[] {eye1R, eye2R, eye3R, eye1L, eye2L, eye3L};

        this.animator = ModelAnimator.create();

        setDefaultPose();
    }

    @Override
    public void render(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        body1.render(ms, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setRotationAngles(OrbwyrmEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        netHeadYaw = MathHelper.wrapDegrees(netHeadYaw);
        faceTarget(netHeadYaw, headPitch, 1, necks);
    }

    @Override
    public void setLivingAnimations(OrbwyrmEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick)
    {
        this.entity = entityIn;
        float frame = entityIn.ticksExisted + partialTick;

        resetToDefaultPose();
        animator.update(entity, partialTick);

        if (animator.setAnimation(OrbwyrmEntity.HISS_ANIMATION)) roarAnim(frame, partialTick);
        else if (animator.setAnimation(OrbwyrmEntity.SHOOT_SILK_ANIMATION)) silkAttackAnim(frame, partialTick);
        else if (animator.setAnimation(OrbwyrmEntity.BITE_ANIMATION)) biteAttakAnim();

        sit(entityIn.sitTimer.get(partialTick), frame);
        sleep(entityIn.sleepTimer.get(partialTick), frame);

        idle(frame, partialTick);
    }

    public void idle(float frame, float partialTicks)
    {
        float amount = 0.5f * (-entity.sleepTimer.get(partialTicks) + 1f);
        float sitAmount = entity.sitTimer.get(partialTicks);

        if (amount > 0)
        {
            chainWave(necks, 0.05f, 0.025f, -1, frame, amount);

            walk(body1, 0.05f, 0.075f, false, 0, 0, frame, amount);
            walk(body2, 0.05f, 0.075f, true, 0, 0, frame, amount);
            swing(wing1R, 0.05f, 0.0375f, false, 0, 0, frame, amount);
            flap(wing1R, 0.05f, 0.0375f, true, 0, 0, frame, amount * sitAmount);

            walk(arm1R, 0.06f, 0.15f, false, 0, 0, frame, amount);
            flap(arm1R, 0.075f, 0.15f, false, 0, 0, frame, amount);
            walk(arm2R, 0.06f, 0.25f, false, 0, 0.5f, frame, amount);
            walk(spikeR, 0.06f, 0.1f, false, 0, -0.5f, frame, amount);
        }

        arm1R.copyRotationsTo(arm1L);
        arm2R.copyRotationsTo(arm2L);
        spikeR.copyRotationsTo(spikeL);
        wing1R.copyRotationsTo(wing1L);

        amount *= -sitAmount + 1; // tails movement is different when sitting
        if (amount > 0)
        {
            chainSwing(tails, 0.05f, 0.2f, -2, frame, amount);
            flap(tail5, 0.05f, 0.2f, false, -1.122f, 0, frame, amount);
        }
    }

    private void sit(float v, float frame)
    {
        setTime(v);
        rotate(body1, -1.1f, 0, 0);
        rotate(body2, 0.1f, 0, 0);
        move(body1, 0, -5f, 0);

        rotate(wing1R, -0.4f, -0.8f, -0.1f);
        rotate(wing2R, -0.1f, 0, 0);
        rotate(palmR, 0.2f, 0, 0.3f);

        wing1R.copyRotationsTo(wing1L);
        wing2R.copyRotationsTo(wing2L);
        palmR.copyRotationsTo(palmL);

        rotate(neck1, 0.6f, 0, 0);
        rotate(neck3, 0.7f, 0, 0);

        rotate(tail1, 0.4f, 0, 0);
        rotate(tail2, 0.6f, 0, 0);
        rotate(tail3, 0.4f, -0.6f, -0.4f);
        rotate(tail4, 0.3f, -0.8f, 0);
        rotate(tail5, -0.1f, -0.7f, -0.1f);
        rotate(tail6, -0.2f, -0.8f, 0);
        rotate(tail7, -0.1f, -0.6f, 0);
        rotate(tail8, 0, -0.6f, 0);
        rotate(tail9, 0, -0.6f, 0);
        rotate(tail10, -0.025f, 0.3f, 0);
        rotate(tail11, 0, 0.6f, 0);
        rotate(tail12, 0, 0.9f, 0.075f);
        rotate(tail13, 0, 0.9f, 0);
        rotate(tail14, 0, 1f, 0);

        rotate(arm1R, 0.5f, 0, 0);
        rotate(arm2R, -0.4f, 0, 0);
        rotate(spikeR, 0.4f, 0, 0);

        chainSwing(tails, 0.05f, 0.01f, -2, frame, v * 0.5f);

        walk(arm1R, 0.02f, 0.15f, false, 0, 0, frame, v * 0.5f);
        flap(arm1R, 0.035f, 0.15f, false, 0, 0, frame, v * 0.5f);
        walk(arm2R, 0.02f, 0.25f, false, 0, 0.5f, frame, v * 0.5f);
        walk(spikeR, 0.01f, 0.1f, false, 0, -0.5f, frame, v * 0.5f);
    }


    private void sleep(float v, float frame)
    {
        setTime(v);

        rotate(neck1, -0.875f, 0, 0);
        for (WRModelRenderer part : necks) rotate(part, 0.275f, 0, 0);
        if (v == 1) for (int i = 0; i < eyes.length; i++) eyes[i].rotateAngleY += i < 3? Mafs.PI : -Mafs.PI;
        chainWave(necks, 0.05f, 0.04f, -2, frame, v * 0.5f);
    }

    private void roarAnim(float frame, float partialTicks)
    {
        animator.startKeyframe(7);
        animator.rotate(neck1, -0.2f, 0, 0);
        animator.rotate(neck2, -0.2f, 0, 0);
        animator.rotate(neck3, 0.1f, 0, 0);
        animator.rotate(neck4, 0.3f, 0, 0);
        animator.rotate(neck5, 0.2f, 0, 0);
        animator.rotate(neck6, 0.3f, 0, 0);
        animator.rotate(head, 0.2f, 0, 0);

        animator.rotate(body1, -0.5f, 0, 0);
        animator.move(body1, 0, -5f, 0);
        animator.rotate(wing1R, 0, -0.5f, -0.2f);
        animator.rotate(palmR, 0, -0.2f, 0.2f);
        animator.rotate(wing1L, 0, 0.5f, 0.2f);
        animator.rotate(palmL, 0, 0.2f, -0.2f);
        animator.rotate(tail1, -0.1f, 0, 0);
        animator.rotate(tail2, 0.3f, 0, 0);
        animator.rotate(tail3, 0.4f, 0, 0);
        animator.rotate(tail4, 0.2f, 0, 0);
        animator.rotate(tail6, -0.2f, 0, 0);
        animator.rotate(tail7, -0.1f, 0, 0);

        animator.endKeyframe();
        animator.startKeyframe(10);
        for (WRModelRenderer part : necks) animator.rotate(part, -part.rotateAngleX + 0.075f, 0, 0);
        animator.rotate(jawL, 1f, 0, 0);
        animator.rotate(cheekL, 0, 0, 0.75f);
        animator.rotate(chinL, 0.3f, 0, 0);
        animator.rotate(jawR, 1f, 0, 0);
        animator.rotate(cheekR, 0, 0, -0.75f);
        animator.rotate(chinR, 0.3f, 0, 0);

        animator.rotate(body1, -0.5f, 0, 0);
        animator.move(body1, 0, -5f, 0);
        animator.rotate(wing1R, 0, -0.5f, -0.2f);
        animator.rotate(palmR, 0, -0.2f, 0.2f);
        animator.rotate(wing1L, 0, 0.5f, 0.2f);
        animator.rotate(palmL, 0, 0.2f, -0.2f);
        animator.rotate(tail1, -0.1f, 0, 0);
        animator.rotate(tail2, 0.3f, 0, 0);
        animator.rotate(tail3, 0.4f, 0, 0);
        animator.rotate(tail4, 0.2f, 0, 0);
        animator.rotate(tail6, -0.2f, 0, 0);
        animator.rotate(tail7, -0.1f, 0, 0);
        for (int i = 5; i < tails.length; i++) animator.rotate(tails[i], 0.1f, 0, 0);

        float tick = animator.getEntity().getAnimationTick();
        if (tick > 7 && tick < 57)
        {
            tick -= 7f;
            float prev = tick--;
            tick = Math.min(-0.1f * Math.abs(tick - 25f) + 2.5f, 1f);
            prev = Math.min(-0.1f * Math.abs(prev - 25f) + 2.5f, 1f);
            tick = MathHelper.lerp(partialTicks, tick, prev);
            tick *= 0.5f;

            chainFlap(necks, 0.5f, 0.1f, 2, frame, tick);
            chainSwing(necks, 0.5f, -0.05f, 1, frame, tick);
            walk(jawR, 3, 0.075f, false, 0, 0, frame, tick);
            walk(tail5, 3, 0.02f, false, 0, 0, frame, tick);
            jawL.rotateAngleX += jawR.rotateAngleX;
        }

        animator.endKeyframe();
        animator.setStaticKeyframe(40);
        animator.resetKeyframe(10);
    }

    private void silkAttackAnim(float frame, float partialTicks)
    {
        animator.startKeyframe(7);
        animator.rotate(body1, 0, -1f, 0);
        animator.rotate(jawR, 0.6f, 0, 0);
        animator.rotate(jawL, 0.6f, 0, 0);
        for (WRModelRenderer part : necks) animator.rotate(part, 0, 0.2f, 0);
        float tick = animator.getEntity().getAnimationTick();
        if (tick < 14)
        {
            float prev = tick--;
            tick = MathHelper.clamp(-2f * Math.abs(tick - 7f) + 14, 0, 1);
            prev = MathHelper.clamp(-2f * Math.abs(prev - 7f) + 7, 0, 1);
            tick = MathHelper.lerp(partialTicks, tick, prev);
            tick *= 0.5f;
            chainSwing(tails, 0.4f, 0.5f, -2, frame, tick);
        }
        animator.endKeyframe();
        animator.setStaticKeyframe(7);
    }


    private void biteAttakAnim()
    {
        animator.startKeyframe(6);
        animator.rotate(body1, -0.2f, 0, 0);
        animator.rotate(body2, 0.2f, 0, 0);
        animator.move(body2, 0, -0.8f, 0);
        animator.rotate(wing1L, 0, 0.2f, 0);
        animator.rotate(wing1R, 0, -0.2f, 0);
        animator.rotate(neck1, -0.2f, 0, 0);
        animator.rotate(neck2, -0.2f, 0, 0);
        animator.rotate(neck3, 0.1f, 0, 0);
        animator.rotate(neck4, 0.3f, 0, 0);
        animator.rotate(neck5, 0.2f, 0, 0);
        animator.rotate(neck6, 0.3f, 0, 0);
        animator.rotate(head, 0.2f, 0, 0);
        animator.rotate(jawL, 1f, 0, 0);
        animator.rotate(jawR, 1f, 0, 0);
        for (int i = 5; i < tails.length; i++) animator.rotate(tails[i], 0.15f, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(3);
        animator.rotate(neck1, 0.6f, 0, 0);
        animator.rotate(neck2, -0.2f, 0, 0);
        animator.rotate(neck3, 0.1f, 0, 0);
        animator.rotate(neck4, 0.3f, 0, 0);
        animator.rotate(neck5, 0.2f, 0, 0);
        animator.rotate(neck6, 0.15f, 0, 0);
        animator.rotate(head, -0.1f, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(7);
    }
}
