package WolfShotz.Wyrmroost.content.entities.minutus;

import WolfShotz.Wyrmroost.util.animtools.BaseModel;
import WolfShotz.Wyrmroost.util.animtools.BaseRenderer;
import net.minecraft.entity.Entity;

/**
 * WR Lesser Desertwyrm - Ukan
 * Created using Tabula 7.0.1
 */
public class MinutusModel<T extends Entity> extends BaseModel<T> {
    public BaseRenderer body1;
    public BaseRenderer body2;
    public BaseRenderer neck;
    public BaseRenderer leg1;
    public BaseRenderer leg1_1;
    public BaseRenderer wingL;
    public BaseRenderer wingR;
    public BaseRenderer body3;
    public BaseRenderer body4;
    public BaseRenderer body5;
    public BaseRenderer tail1;
    public BaseRenderer tail2;
    public BaseRenderer tail3;
    public BaseRenderer jaw;
    public BaseRenderer head;

    public MinutusModel() {
        this.textureWidth = 100;
        this.textureHeight = 100;
        this.wingL = new BaseRenderer(this, 0, 22);
        this.wingL.setRotationPoint(0.5F, -0.7F, 2.0F);
        this.wingL.addBox(0.0F, -2.0F, 0.0F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wingL, 0.6829473363053812F, 0.0F, 0.5462880558742251F);
        this.body5 = new BaseRenderer(this, 0, 0);
        this.body5.setRotationPoint(-0.02F, -0.02F, 3.0F);
        this.body5.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.tail3 = new BaseRenderer(this, 0, 17);
        this.tail3.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.tail3.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 4, 0.0F);
        this.leg1_1 = new BaseRenderer(this, 18, 22);
        this.leg1_1.setRotationPoint(-0.7F, 0.0F, 0.5F);
        this.leg1_1.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(leg1_1, 0.0F, 0.0F, -0.40980330836826856F);
        this.body1 = new BaseRenderer(this, 0, 9);
        this.body1.setRotationPoint(0.02F, 23.0F, -5.5F);
        this.body1.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F);
        this.body4 = new BaseRenderer(this, 0, 0);
        this.body4.setRotationPoint(0.02F, 0.02F, 3.0F);
        this.body4.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.neck = new BaseRenderer(this, 16, 0);
        this.neck.setRotationPoint(-0.02F, 0.02F, -1.0F);
        this.neck.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 3, 0.0F);
        this.setRotateAngle(neck, -0.5918411493512771F, 0.0F, 0.0F);
        this.body3 = new BaseRenderer(this, 0, 0);
        this.body3.setRotationPoint(-0.02F, -0.02F, 3.0F);
        this.body3.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.wingR = new BaseRenderer(this, 0, 22);
        this.wingR.setRotationPoint(-0.5F, -0.7F, 2.0F);
        this.wingR.addBox(0.0F, -2.0F, 0.0F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wingR, 0.6829473363053812F, 0.0F, -0.5462880558742251F);
        this.head = new BaseRenderer(this, 18, 14);
        this.head.setRotationPoint(0.02F, -0.6F, -2.5F);
        this.head.addBox(-1.0F, -0.5F, -3.0F, 2, 1, 3, 0.0F);
        this.setRotateAngle(head, -0.27314402793711257F, 0.0F, 0.0F);
        this.tail1 = new BaseRenderer(this, 0, 9);
        this.tail1.setRotationPoint(0.0F, 0.02F, 3.0F);
        this.tail1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.leg1 = new BaseRenderer(this, 18, 22);
        this.leg1.setRotationPoint(0.7F, 0.0F, 0.5F);
        this.leg1.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(leg1, 0.0F, 0.0F, 0.40980330836826856F);
        this.body2 = new BaseRenderer(this, 0, 0);
        this.body2.setRotationPoint(0.02F, 0.02F, 1.0F);
        this.body2.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.tail2 = new BaseRenderer(this, 0, 9);
        this.tail2.setRotationPoint(0.02F, 0.02F, 3.0F);
        this.tail2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.jaw = new BaseRenderer(this, 18, 7);
        this.jaw.setRotationPoint(0.02F, 0.6F, -2.2F);
        this.jaw.addBox(-1.0F, -0.5F, -3.0F, 2, 1, 3, 0.0F);
        this.setRotateAngle(jaw, 0.5009094953223726F, 0.0F, 0.0F);
        this.body1.addChild(this.wingL);
        this.body4.addChild(this.body5);
        this.tail2.addChild(this.tail3);
        this.body1.addChild(this.leg1_1);
        this.body3.addChild(this.body4);
        this.body1.addChild(this.neck);
        this.body2.addChild(this.body3);
        this.body1.addChild(this.wingR);
        this.neck.addChild(this.head);
        this.body5.addChild(this.tail1);
        this.body1.addChild(this.leg1);
        this.body1.addChild(this.body2);
        this.tail1.addChild(this.tail2);
        this.neck.addChild(this.jaw);
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body1.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(BaseRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
