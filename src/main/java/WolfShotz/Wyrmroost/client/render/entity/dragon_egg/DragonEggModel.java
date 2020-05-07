package WolfShotz.Wyrmroost.client.render.entity.dragon_egg;

import WolfShotz.Wyrmroost.client.animation.ModelAnimator;
import WolfShotz.Wyrmroost.client.model.AdvancedLivingEntityModel;
import WolfShotz.Wyrmroost.client.model.AdvancedRendererModel;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;

/**
 * WREggTemplate - Ukan
 * Created using Tabula 7.0.1
 */
public class DragonEggModel extends AdvancedLivingEntityModel<DragonEggEntity>
{
    private ModelAnimator animator;
    public AdvancedRendererModel base;
    public AdvancedRendererModel two;
    public AdvancedRendererModel three;
    public AdvancedRendererModel four;
    
    public DragonEggModel()
    {
        super(RenderType::getEntitySolid);
        textureWidth = 64;
        textureHeight = 32;
        four = new AdvancedRendererModel(this, 0, 19);
        four.setRotationPoint(0.0F, -1.3F, 0.0F);
        four.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        two = new AdvancedRendererModel(this, 17, 0);
        two.setRotationPoint(0.0F, -1.5F, 0.0F);
        two.addBox(-2.5F, -3.0F, -2.5F, 5, 6, 5, 0.0F);
        three = new AdvancedRendererModel(this, 0, 9);
        three.setRotationPoint(0.0F, -2.0F, 0.0F);
        three.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        base = new AdvancedRendererModel(this, 0, 0);
        base.setRotationPoint(0.0F, 22.0F, 0.0F);
        base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        three.addChild(four);
        base.addChild(two);
        two.addChild(three);

        updateDefaultPose();
        animator = ModelAnimator.create();
    }

    public void animate(DragonEggEntity entity)
    {
        resetToDefaultPose();
        animator.update(entity);

        if (animator.setAnimation(DragonEggEntity.WIGGLE_ANIMATION))
        {
            float angle = entity.wiggleInvert? -0.55f : 0.55f;
            boolean xz = entity.wiggleInvert2;
            int speed = 3;

            animator.startKeyframe(speed);
            if (xz) animator.rotate(base, angle, 0, 0);
            else animator.rotate(base, 0, 0, angle);
            animator.endKeyframe();

            animator.startKeyframe(speed);
            if (xz) animator.rotate(base, -angle, 0, 0);
            else animator.rotate(base, 0, 0, -angle);
            animator.endKeyframe();

            animator.resetKeyframe(speed + 1);
        }
    }

    @Override
    public void setRotationAngles(DragonEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void render(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        base.render(ms, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
