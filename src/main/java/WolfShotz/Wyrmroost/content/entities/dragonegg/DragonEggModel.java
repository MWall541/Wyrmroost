package WolfShotz.Wyrmroost.content.entities.dragonegg;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;

import java.util.Random;

/**
 * WREggTemplate - Ukan
 * Created using Tabula 7.0.1
 */
public class DragonEggModel extends AdvancedEntityModel
{
    private ModelAnimator animator;
    public AdvancedRendererModel base;
    public AdvancedRendererModel two;
    public AdvancedRendererModel three;
    public AdvancedRendererModel four;
    
    public DragonEggModel() {
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
    
    public void render(DragonEggEntity entity) {
        base.render(0.0625f);
        
        resetToDefaultPose();
        animator.update(entity);
        
        if (entity.getAnimation() == DragonEggEntity.WIGGLE_ANIMATION) {
            
            animator.setAnimation(DragonEggEntity.WIGGLE_ANIMATION);
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
}
