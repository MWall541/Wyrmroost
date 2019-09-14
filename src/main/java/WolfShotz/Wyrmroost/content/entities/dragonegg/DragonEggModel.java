package WolfShotz.Wyrmroost.content.entities.dragonegg;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;

/**
 * WREggTemplate - Ukan
 * Created using Tabula 7.0.1
 */
public class DragonEggModel extends Model {
    public RendererModel base;
    public RendererModel two;
    public RendererModel three;
    public RendererModel four;
    
    public DragonEggModel() {
        textureWidth = 64;
        textureHeight = 32;
        four = new RendererModel(this, 0, 19);
        four.setRotationPoint(0.0F, -1.3F, 0.0F);
        four.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        two = new RendererModel(this, 17, 0);
        two.setRotationPoint(0.0F, -1.5F, 0.0F);
        two.addBox(-2.5F, -3.0F, -2.5F, 5, 6, 5, 0.0F);
        three = new RendererModel(this, 0, 9);
        three.setRotationPoint(0.0F, -2.0F, 0.0F);
        three.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        base = new RendererModel(this, 0, 0);
        base.setRotationPoint(0.0F, 22.0F, 0.0F);
        base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        three.addChild(four);
        base.addChild(two);
        two.addChild(three);
    }
}
