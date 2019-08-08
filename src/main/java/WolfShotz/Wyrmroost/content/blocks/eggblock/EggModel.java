package WolfShotz.Wyrmroost.content.blocks.eggblock;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * WREggTemplate - Ukan
 * Created using Tabula 7.0.1
 */
public class EggModel extends AdvancedEntityModel {
    public AdvancedRendererModel base;
    public AdvancedRendererModel two;
    public AdvancedRendererModel three;
    public AdvancedRendererModel four;
    
    public EggModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.four = new AdvancedRendererModel(this, 0, 19);
        this.four.setRotationPoint(0.0F, -1.3F, 0.0F);
        this.four.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        this.two = new AdvancedRendererModel(this, 17, 0);
        this.two.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.two.addBox(-2.5F, -3.0F, -2.5F, 5, 6, 5, 0.0F);
        this.three = new AdvancedRendererModel(this, 0, 9);
        this.three.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.three.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.base = new AdvancedRendererModel(this, 0, 0);
        this.base.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.three.addChild(this.four);
        this.base.addChild(this.two);
        this.two.addChild(this.three);
        
    }

    public void renderAll() { base.render(0.0625f); }
}
