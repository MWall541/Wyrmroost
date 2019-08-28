package WolfShotz.Wyrmroost.content.entities.dragon.minutus;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * WR Lesser Desertwyrm - Ukan
 * Created using Tabula 7.0.1
 */
@OnlyIn(Dist.CLIENT)
public class MinutusModel extends AdvancedEntityModel {
    public AdvancedRendererModel body1;
    public AdvancedRendererModel body2;
    public AdvancedRendererModel neck;
    public AdvancedRendererModel leg1;
    public AdvancedRendererModel leg1_1;
    public AdvancedRendererModel wingL;
    public AdvancedRendererModel wingR;
    public AdvancedRendererModel body3;
    public AdvancedRendererModel body4;
    public AdvancedRendererModel body5;
    public AdvancedRendererModel tail1;
    public AdvancedRendererModel tail2;
    public AdvancedRendererModel tail3;
    public AdvancedRendererModel jaw;
    public AdvancedRendererModel head;

    private AdvancedRendererModel[] body;

    private ModelAnimator animator;

    public MinutusModel() {
        this.textureWidth = 30;
        this.textureHeight = 30;
        this.wingL = new AdvancedRendererModel(this, 0, 22);
        this.wingL.setRotationPoint(0.5F, -0.7F, 2.0F);
        this.wingL.addBox(0.0F, -2.0F, 0.0F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wingL, 0.6829473363053812F, 0.0F, 0.5462880558742251F);
        this.body5 = new AdvancedRendererModel(this, 0, 0);
        this.body5.setRotationPoint(-0.02F, -0.02F, 3.0F);
        this.body5.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.tail3 = new AdvancedRendererModel(this, 0, 17);
        this.tail3.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.tail3.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 4, 0.0F);
        this.leg1_1 = new AdvancedRendererModel(this, 18, 22);
        this.leg1_1.setRotationPoint(-0.7F, 0.0F, 0.5F);
        this.leg1_1.addBox(-2.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(leg1_1, 0.0F, 0.0F, -0.40980330836826856F);
        this.body1 = new AdvancedRendererModel(this, 0, 9);
        this.body1.setRotationPoint(0.02F, 23.0F, -5.5F);
        this.body1.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 4, 0.0F);
        this.body4 = new AdvancedRendererModel(this, 0, 0);
        this.body4.setRotationPoint(0.02F, 0.02F, 3.0F);
        this.body4.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.neck = new AdvancedRendererModel(this, 16, 0);
        this.neck.setRotationPoint(-0.02F, 0.02F, -1.0F);
        this.neck.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 3, 0.0F);
        this.setRotateAngle(neck, 0.0F, 0.0F, 0.0F);
        this.body3 = new AdvancedRendererModel(this, 0, 0);
        this.body3.setRotationPoint(-0.02F, -0.02F, 3.0F);
        this.body3.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.wingR = new AdvancedRendererModel(this, 0, 22);
        this.wingR.setRotationPoint(-0.5F, -0.7F, 2.0F);
        this.wingR.addBox(0.0F, -2.0F, 0.0F, 0, 2, 3, 0.0F);
        this.setRotateAngle(wingR, 0.6829473363053812F, 0.0F, -0.5462880558742251F);
        this.head = new AdvancedRendererModel(this, 18, 14);
        this.head.setRotationPoint(0.02F, -0.6F, -2.5F);
        this.head.addBox(-1.0F, -0.5F, -3.0F, 2, 1, 3, 0.0F);
        this.setRotateAngle(head, 0.0F, 0.0F, 0.0F);
        this.tail1 = new AdvancedRendererModel(this, 0, 9);
        this.tail1.setRotationPoint(0.0F, 0.02F, 3.0F);
        this.tail1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.leg1 = new AdvancedRendererModel(this, 18, 22);
        this.leg1.setRotationPoint(0.7F, 0.0F, 0.5F);
        this.leg1.addBox(0.0F, -0.5F, -0.5F, 2, 1, 1, 0.0F);
        this.setRotateAngle(leg1, 0.0F, 0.0F, 0.40980330836826856F);
        this.body2 = new AdvancedRendererModel(this, 0, 0);
        this.body2.setRotationPoint(0.02F, 0.02F, 1.0F);
        this.body2.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 4, 0.0F);
        this.tail2 = new AdvancedRendererModel(this, 0, 9);
        this.tail2.setRotationPoint(0.02F, 0.02F, 3.0F);
        this.tail2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 4, 0.0F);
        this.jaw = new AdvancedRendererModel(this, 18, 7);
        this.jaw.setRotationPoint(0.02F, 0.6F, -2.2F);
        this.jaw.addBox(-1.0F, -0.5F, -3.0F, 2, 1, 3, 0.0F);
        this.setRotateAngle(jaw, 0.0F, 0.0F, 0.0F);
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

        updateDefaultPose();

        body = new AdvancedRendererModel[] {body1, body2, body3, body4, body5, tail1, tail2, tail3};

        animator = ModelAnimator.create();
    }

    private float globalSpeed = 0.5f;
    private float f = 0.5f;

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        animate((MinutusEntity) entityIn);
        this.body1.render(scale);
    }

    @Override
    public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        chainSwing(body, globalSpeed, 0.3f, 5, -limbSwing, limbSwingAmount);
        
        faceTarget(netHeadYaw, headPitch, 1, head);
    }

    @Override
    public void setLivingAnimations(Entity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        resetToDefaultPose();
        float frame = entity.ticksExisted;
        MinutusEntity minutus = (MinutusEntity) entity;

        if (minutus.isBurrowed()) {
            body1.rotateAngleX = -0.8f;
            body1.offsetY = 0.2f;
            body2.rotateAngleX = 0.8f;
            neck.rotateAngleX = -0.8f;
            jaw.rotateAngleX = 1f;
            head.rotateAngleX = -1f;

            bob(neck, 0.45f - globalSpeed, 0.15f, false, frame, f);
        }

        if (minutus.getAnimation() != MinutusEntity.BITE_ANIMATION) {
            walk(jaw, 0.45f - globalSpeed, 0.1f, false, 0, 0, frame, f);
            walk(head, 0.45f - globalSpeed, 0.1f, true, 0, (minutus.isBurrowed() ? 0f : 0.5f), frame, f);
        }
        flap(wingL, 0.45f - globalSpeed, 0.15f, false, 0, 0, frame, f);
        flap(wingR, 0.45f - globalSpeed, 0.15f, true, 0, 0, frame, f);
        flap(leg1, 0.45f - globalSpeed, 0.15f, true, 0, 0, frame, f);
        flap(leg1_1, 0.45f - globalSpeed, 0.15f, false, 0, 0, frame, f);
    }

    private void animate(MinutusEntity entity) {
        animator.update(entity);

        animator.setAnimation(MinutusEntity.BITE_ANIMATION);
        animator.startKeyframe(4);
        animator.rotate(head, 1f, 0, 0);
        animator.rotate(jaw, -1f, 0, 0);
        animator.move(body1, 0, -1f, 0);
        animator.endKeyframe();
        animator.resetKeyframe(7);
    }
}
