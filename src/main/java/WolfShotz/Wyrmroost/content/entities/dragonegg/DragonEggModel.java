package WolfShotz.Wyrmroost.content.entities.dragonegg;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

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
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.four = new RendererModel(this, 0, 19);
        this.four.setRotationPoint(0.0F, -1.3F, 0.0F);
        this.four.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.two = new RendererModel(this, 17, 0);
        this.two.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.two.addBox(-2.5F, -3.0F, -2.5F, 5, 6, 5, 0.0F);
        this.three = new RendererModel(this, 0, 9);
        this.three.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.three.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.base = new RendererModel(this, 0, 0);
        this.base.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.three.addChild(this.four);
        this.base.addChild(this.two);
        this.two.addChild(this.three);
    }

    public void renderAll() { base.render(0.0625f); }
}
