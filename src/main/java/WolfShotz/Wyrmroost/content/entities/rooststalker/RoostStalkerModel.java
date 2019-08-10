package WolfShotz.Wyrmroost.content.entities.rooststalker;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * Roost stalker - nova
 * Created using Tabula 7.0.1
 */
public class RoostStalkerModel extends AdvancedEntityModel {
    public AdvancedRendererModel torso;
    public AdvancedRendererModel tail1;
    public AdvancedRendererModel legl1;
    public AdvancedRendererModel legl2;
    public AdvancedRendererModel legl3;
    public AdvancedRendererModel legr1;
    public AdvancedRendererModel legr2;
    public AdvancedRendererModel legr3;
    public AdvancedRendererModel neck;
    public AdvancedRendererModel tail2;
    public AdvancedRendererModel tail3;
    public AdvancedRendererModel footl1;
    public AdvancedRendererModel footl2;
    public AdvancedRendererModel footl3;
    public AdvancedRendererModel footl1_1;
    public AdvancedRendererModel footl2_1;
    public AdvancedRendererModel footl3_1;
    public AdvancedRendererModel head;
    public AdvancedRendererModel jaw;
    public AdvancedRendererModel hornl;
    public AdvancedRendererModel hornl_1;

    public RoostStalkerModel() {
        this.textureWidth = 80;
        this.textureHeight = 90;
        this.legl1 = new AdvancedRendererModel(this, 20, 72);
        this.legl1.setRotationPoint(-2.5F, 0.0F, -5.0F);
        this.legl1.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl1, 0.091106186954104F, -0.31869712141416456F, -0.33946653951289707F);
        this.tail3 = new AdvancedRendererModel(this, 0, 55);
        this.tail3.setRotationPoint(0.0F, 0.0F, 6.5F);
        this.tail3.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 9, 0.0F);
        this.setRotateAngle(tail3, -0.091106186954104F, 0.0F, 0.0F);
        this.torso = new AdvancedRendererModel(this, 0, 0);
        this.torso.setRotationPoint(0.0F, 31.0F, 0.0F);
        this.torso.addBox(-3.5F, -3.5F, -7.5F, 7, 7, 15, 0.0F);
        this.hornl = new AdvancedRendererModel(this, 40, 25);
        this.hornl.mirror = true;
        this.hornl.setRotationPoint(1.5F, -2.0F, -1.5F);
        this.hornl.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(hornl, 0.6829473363053812F, 0.27314402793711257F, 0.18203784098300857F);
        this.footl1_1 = new AdvancedRendererModel(this, 20, 80);
        this.footl1_1.mirror = true;
        this.footl1_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.footl1_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl1_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.legl3 = new AdvancedRendererModel(this, 20, 72);
        this.legl3.setRotationPoint(-2.5F, 0.0F, 5.0F);
        this.legl3.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl3, -0.091106186954104F, 0.31869712141416456F, -0.31869712141416456F);
        this.footl2 = new AdvancedRendererModel(this, 20, 80);
        this.footl2.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.footl2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl2, 0.0F, 0.0F, 0.31869712141416456F);
        this.footl3_1 = new AdvancedRendererModel(this, 20, 80);
        this.footl3_1.mirror = true;
        this.footl3_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.footl3_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl3_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.tail2 = new AdvancedRendererModel(this, 0, 40);
        this.tail2.setRotationPoint(0.0F, 0.0F, 6.5F);
        this.tail2.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 7, 0.0F);
        this.setRotateAngle(tail2, -0.091106186954104F, 0.0F, 0.0F);
        this.legr2 = new AdvancedRendererModel(this, 20, 72);
        this.legr2.mirror = true;
        this.legr2.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.legr2.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr2, 0.0F, 0.0F, 0.31869712141416456F);
        this.neck = new AdvancedRendererModel(this, 45, 0);
        this.neck.setRotationPoint(0.0F, -0.5F, -6.5F);
        this.neck.addBox(-2.0F, -2.5F, -4.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(neck, -0.18203784098300857F, 0.0F, 0.0F);
        this.jaw = new AdvancedRendererModel(this, 35, 60);
        this.jaw.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.jaw.addBox(-2.51F, 0.0F, -10.0F, 5, 2, 10, 0.0F);
        this.setRotateAngle(jaw, 0.31869712141416456F, 0.0F, 0.0F);
        this.footl1 = new AdvancedRendererModel(this, 20, 80);
        this.footl1.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.footl1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl1, 0.0F, 0.0F, 0.31869712141416456F);
        this.head = new AdvancedRendererModel(this, 35, 40);
        this.head.setRotationPoint(0.0F, -0.5F, -3.0F);
        this.head.addBox(-2.5F, -3.0F, -10.0F, 5, 4, 10, 0.0F);
        this.setRotateAngle(head, 0.18203784098300857F, 0.0F, 0.0F);
        this.legl2 = new AdvancedRendererModel(this, 20, 72);
        this.legl2.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.legl2.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl2, 0.0F, 0.0F, -0.31869712141416456F);
        this.footl3 = new AdvancedRendererModel(this, 20, 80);
        this.footl3.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.footl3.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl3, 0.0F, 0.0F, 0.31869712141416456F);
        this.tail1 = new AdvancedRendererModel(this, 0, 25);
        this.tail1.setRotationPoint(0.0F, -0.5F, 7.0F);
        this.tail1.addBox(-2.5F, -2.4F, 0.0F, 5, 5, 7, 0.0F);
        this.setRotateAngle(tail1, -0.091106186954104F, 0.0F, 0.0F);
        this.hornl_1 = new AdvancedRendererModel(this, 40, 25);
        this.hornl_1.setRotationPoint(-1.5F, -2.0F, -1.5F);
        this.hornl_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(hornl_1, 0.6829473363053812F, -0.27314402793711257F, -0.18203784098300857F);
        this.footl2_1 = new AdvancedRendererModel(this, 20, 80);
        this.footl2_1.mirror = true;
        this.footl2_1.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.footl2_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl2_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.legr3 = new AdvancedRendererModel(this, 20, 72);
        this.legr3.mirror = true;
        this.legr3.setRotationPoint(2.5F, 0.0F, 5.0F);
        this.legr3.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr3, -0.091106186954104F, -0.31869712141416456F, 0.31869712141416456F);
        this.legr1 = new AdvancedRendererModel(this, 20, 72);
        this.legr1.mirror = true;
        this.legr1.setRotationPoint(2.5F, 0.0F, -5.0F);
        this.legr1.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr1, 0.091106186954104F, 0.31869712141416456F, 0.31869712141416456F);
        this.torso.addChild(this.legl1);
        this.tail2.addChild(this.tail3);
        this.head.addChild(this.hornl);
        this.legr1.addChild(this.footl1_1);
        this.torso.addChild(this.legl3);
        this.legl2.addChild(this.footl2);
        this.legr3.addChild(this.footl3_1);
        this.tail1.addChild(this.tail2);
        this.torso.addChild(this.legr2);
        this.torso.addChild(this.neck);
        this.head.addChild(this.jaw);
        this.legl1.addChild(this.footl1);
        this.neck.addChild(this.head);
        this.torso.addChild(this.legl2);
        this.legl3.addChild(this.footl3);
        this.torso.addChild(this.tail1);
        this.head.addChild(this.hornl_1);
        this.legr2.addChild(this.footl2_1);
        this.torso.addChild(this.legr3);
        this.torso.addChild(this.legr1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.625d, 0.625d, 0.625d);
        this.torso.render(f5);
        GlStateManager.popMatrix();
    }
}
