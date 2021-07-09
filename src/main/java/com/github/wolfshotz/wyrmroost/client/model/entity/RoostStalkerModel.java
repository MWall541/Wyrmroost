package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.RoostStalkerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Roost stalker - nova
 * Created using Tabula 7.0.1
 */
public class RoostStalkerModel extends DragonEntityModel<RoostStalkerEntity>
{
    public static final ResourceLocation BODY = texture("body.png");
    public static final ResourceLocation ALBINO = texture("body_spe.png");

    public static final ResourceLocation EYES = texture("body_glow.png");
    public static final ResourceLocation ALBINO_EYES = texture("body_spe_glow.png");

    public WRModelRenderer torso;
    public WRModelRenderer tail1;
    public WRModelRenderer legl1;
    public WRModelRenderer legl2;
    public WRModelRenderer legl3;
    public WRModelRenderer legr1;
    public WRModelRenderer legr2;
    public WRModelRenderer legr3;
    public WRModelRenderer neck;
    public WRModelRenderer tail2;
    public WRModelRenderer tail3;
    public WRModelRenderer footl1;
    public WRModelRenderer footl2;
    public WRModelRenderer footl3;
    public WRModelRenderer footl1_1;
    public WRModelRenderer footl2_1;
    public WRModelRenderer footl3_1;
    public WRModelRenderer head;
    public WRModelRenderer jaw;
    public WRModelRenderer hornl;
    public WRModelRenderer hornl_1;

    public WRModelRenderer[] tailSegments;

    private final float globalSpeed = 0.5f;

    public RoostStalkerModel()
    {
        this.texWidth = 80;
        this.texHeight = 90;
        this.legl1 = new WRModelRenderer(this, 20, 72);
        this.legl1.setPos(-2.5F, 0.0F, -5.0F);
        this.legl1.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl1, 0.091106186954104F, -0.31869712141416456F, -0.33946653951289707F);
        this.tail3 = new WRModelRenderer(this, 0, 55);
        this.tail3.setPos(0.0F, 0.0F, 6.5F);
        this.tail3.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 9, 0.0F);
        this.setRotateAngle(tail3, -0.091106186954104F, 0.0F, 0.0F);
        this.torso = new WRModelRenderer(this, 0, 0);
        this.torso.setPos(0.0F, 31.0F, 0.0F);
        this.torso.addBox(-3.5F, -3.5F, -7.5F, 7, 7, 15, 0.0F);
        this.hornl = new WRModelRenderer(this, 40, 25);
        this.hornl.mirror = true;
        this.hornl.setPos(1.5F, -2.0F, -1.5F);
        this.hornl.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(hornl, 0.6829473363053812F, 0.27314402793711257F, 0.18203784098300857F);
        this.footl1_1 = new WRModelRenderer(this, 20, 80);
        this.footl1_1.mirror = true;
        this.footl1_1.setPos(4.0F, 0.0F, 0.0F);
        this.footl1_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl1_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.legl3 = new WRModelRenderer(this, 20, 72);
        this.legl3.setPos(-2.5F, 0.0F, 5.0F);
        this.legl3.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl3, -0.091106186954104F, 0.31869712141416456F, -0.31869712141416456F);
        this.footl2 = new WRModelRenderer(this, 20, 80);
        this.footl2.setPos(-4.0F, 0.0F, 0.0F);
        this.footl2.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl2, 0.0F, 0.0F, 0.31869712141416456F);
        this.footl3_1 = new WRModelRenderer(this, 20, 80);
        this.footl3_1.mirror = true;
        this.footl3_1.setPos(4.0F, 0.0F, 0.0F);
        this.footl3_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl3_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.tail2 = new WRModelRenderer(this, 0, 40);
        this.tail2.setPos(0.0F, 0.0F, 6.5F);
        this.tail2.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 7, 0.0F);
        this.setRotateAngle(tail2, -0.091106186954104F, 0.0F, 0.0F);
        this.legr2 = new WRModelRenderer(this, 20, 72);
        this.legr2.mirror = true;
        this.legr2.setPos(3.0F, 0.0F, 0.0F);
        this.legr2.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr2, 0.0F, 0.0F, 0.31869712141416456F);
        this.neck = new WRModelRenderer(this, 45, 0);
        this.neck.setPos(0.0F, -0.5F, -6.5F);
        this.neck.addBox(-2.0F, -2.5F, -4.0F, 4, 5, 4, 0.0F);
        this.setRotateAngle(neck, 0, 0, 0);
        this.jaw = new WRModelRenderer(this, 35, 60);
        this.jaw.setPos(0.0F, 1.0F, 0.0F);
        this.jaw.addBox(-2.51F, 0.0F, -10.0F, 5, 2, 10, 0.0F);
        this.footl1 = new WRModelRenderer(this, 20, 80);
        this.footl1.setPos(-4.0F, 0.0F, 0.0F);
        this.footl1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl1, 0.0F, 0.0F, 0.31869712141416456F);
        this.head = new WRModelRenderer(this, 35, 40);
        this.head.setPos(0.0F, -0.5F, -3.0F);
        this.head.addBox(-2.5F, -3.0F, -10.0F, 5, 4, 10, 0.0F);
        this.setRotateAngle(head, 0, 0, 0);
        this.legl2 = new WRModelRenderer(this, 20, 72);
        this.legl2.setPos(-3.0F, 0.0F, 0.0F);
        this.legl2.addBox(-5.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legl2, 0.0F, 0.0F, -0.31869712141416456F);
        this.footl3 = new WRModelRenderer(this, 20, 80);
        this.footl3.setPos(-4.0F, 0.0F, 0.0F);
        this.footl3.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl3, 0.0F, 0.0F, 0.31869712141416456F);
        this.tail1 = new WRModelRenderer(this, 0, 25);
        this.tail1.setPos(0.0F, -0.5F, 7.0F);
        this.tail1.addBox(-2.5F, -2.4F, 0.0F, 5, 5, 7, 0.0F);
        this.setRotateAngle(tail1, -0.091106186954104F, 0.0F, 0.0F);
        this.hornl_1 = new WRModelRenderer(this, 40, 25);
        this.hornl_1.setPos(-1.5F, -2.0F, -1.5F);
        this.hornl_1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 6, 0.0F);
        this.setRotateAngle(hornl_1, 0.6829473363053812F, -0.27314402793711257F, -0.18203784098300857F);
        this.footl2_1 = new WRModelRenderer(this, 20, 80);
        this.footl2_1.mirror = true;
        this.footl2_1.setPos(4.0F, 0.0F, 0.0F);
        this.footl2_1.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(footl2_1, 0.0F, 0.0F, -0.31869712141416456F);
        this.legr3 = new WRModelRenderer(this, 20, 72);
        this.legr3.mirror = true;
        this.legr3.setPos(2.5F, 0.0F, 5.0F);
        this.legr3.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr3, -0.091106186954104F, -0.31869712141416456F, 0.31869712141416456F);
        this.legr1 = new WRModelRenderer(this, 20, 72);
        this.legr1.mirror = true;
        this.legr1.setPos(2.5F, 0.0F, -5.0F);
        this.legr1.addBox(0.0F, -1.5F, -1.5F, 5, 3, 3, 0.0F);
        this.setRotateAngle(legr1, 0.091106186954104F, 0.31869712141416456F, 0.31869712141416456F);
        this.torso.addChild(legl1);
        this.tail2.addChild(tail3);
        this.head.addChild(hornl);
        this.legr1.addChild(footl1_1);
        this.torso.addChild(legl3);
        this.legl2.addChild(footl2);
        this.legr3.addChild(footl3_1);
        this.tail1.addChild(tail2);
        this.torso.addChild(legr2);
        this.torso.addChild(neck);
        this.head.addChild(jaw);
        this.legl1.addChild(footl1);
        this.neck.addChild(head);
        this.torso.addChild(legl2);
        this.legl3.addChild(footl3);
        this.torso.addChild(tail1);
        this.head.addChild(hornl_1);
        this.legr2.addChild(footl2_1);
        this.torso.addChild(legr3);
        this.torso.addChild(legr1);
        tailSegments = new WRModelRenderer[]{tail1, tail2, tail3};
        setDefaultPose();
    }

    @Override
    public ResourceLocation getTexture(RoostStalkerEntity entity)
    {
        return entity.getVariant() == -1? ALBINO : BODY;
    }

    @Override
    public float getShadowRadius(RoostStalkerEntity entity)
    {
        return 0.5f;
    }

    @Override
    public void scale(RoostStalkerEntity entity, MatrixStack ms, float partialTicks)
    {
        super.scale(entity, ms, partialTicks);
        ms.scale(0.625f, 0.625f, 0.625f);
        ms.translate(0, -0.875f, 0);
    }

    @Override
    public void renderToBuffer(MatrixStack ms, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        torso.render(ms, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void postProcess(RoostStalkerEntity entity, MatrixStack ms, IRenderTypeBuffer buffer, int light, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float partialTicks)
    {
        if (!entity.isSleeping()) renderGlowOverlay(entity.getVariant() == -1? ALBINO_EYES : EYES, ms, buffer);
        renderMouthItem(entity, ms, buffer, light, yaw, pitch);
    }

    private void renderMouthItem(RoostStalkerEntity entity, MatrixStack ms, IRenderTypeBuffer buffer, int light, float yaw, float pitch)
    {
        ItemStack stack = entity.getItem();

        if (!stack.isEmpty())
        {
            ms.pushPose();

            if (entity.isSleeping())
            {
                // just set the item on the ground
                ms.translate(-0.4, 1.47, 0.1);
                ms.mulPose(Vector3f.YP.rotationDegrees(135));
            }
            else
            {
                ms.translate(head.x / 8, -(head.y * 2.4), head.z / 8); // translate to heads rotation point (rough estimate) to allow for the same rotations while rotating; fixes connection issues
                ms.mulPose(Vector3f.YP.rotationDegrees(yaw)); // rotate to match head rotations
                ms.mulPose(Vector3f.XP.rotationDegrees(pitch));
                ms.translate(0, entity.isInSittingPose()? 0.11 : 0.03, -0.4); // offset
                Item item = stack.getItem();
                if (item instanceof TieredItem) // offsets for tools, looks way fucking better
                {
                    ms.translate(0.1, 0, 0);
                    ms.mulPose(Vector3f.YP.rotationDegrees(45));
                }
                else if (item instanceof BlockItem)
                {
                    ms.scale(0.8f, 0.8f, 0.8f);
                    ms.mulPose(Vector3f.XP.rotationDegrees(90f));
                    ms.translate(0, 0.075f, -0.175f);
                }
            }

            ms.mulPose(Vector3f.XP.rotationDegrees(90)); // flip the item

            Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.GROUND, false, ms, buffer, light);
            ms.popPose();
        }
    }

    @Override
    public void setupAnim(RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float bob, float netHeadYaw, float headPitch)
    {
        resetToDefaultPose();

        if (!stalker.isInSittingPose())
        {
            swing(legl1, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
            swing(legl2, globalSpeed, 0.7f, true, 0, 0, limbSwing, limbSwingAmount);
            swing(legl3, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);

            swing(legr1, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
            swing(legr2, globalSpeed, 0.7f, true, 0, 0, limbSwing, limbSwingAmount);
            swing(legr3, globalSpeed, 0.7f, false, 0, 0, limbSwing, limbSwingAmount);
        }

        if (stalker.isScavenging())
            walk(neck, globalSpeed + 0.5f, 0.4f, false, 0, 1.5f, bob, 0.5f);


        if (stalker.isInSittingPose() && !stalker.isSleeping()) sit();
        sleep(stalker.sleepTimer.get(partialTicks));

        boolean flag = stalker.getItem().isEmpty() || stalker.isSleeping();
        idle(bob, flag);

        if (!flag)
        {
            if (stalker.getItem().getItem() instanceof BlockItem)
            {
                head.xRot = -0.25f;
                jaw.xRot = 0.6f;
            }
            else jaw.xRot = 0.15f;
        }

        netHeadYaw = MathHelper.wrapDegrees(netHeadYaw);
        if (!stalker.isSleeping())
        {
            head.xRot += headPitch * ((float) Math.PI / 180F);
            head.yRot += netHeadYaw * ((float) Math.PI / 180F);
        }
    }

    public void idle(float frame, boolean head)
    {
        chainWave(tailSegments, globalSpeed - 0.44f, 0.08f, 2, frame, 0.5f);
        chainSwing(tailSegments, globalSpeed - 0.45f, 0.08f, 0, frame, 0.5f);
        if (head)
        {
            walk(jaw, globalSpeed - 0.4f, 0.1f, false, 0, 0.1f, frame, 0.5f);
            chainWave(new WRModelRenderer[]{this.head, this.neck}, globalSpeed - 0.4f, 0.05f, 2, frame, 0.5f);
        }
    }

    public void sleep(float v)
    {
        setTime(v);

        rotate(torso, 0, 0, 1.7f);
        move(torso, 0, 3.4f, 0);
        rotate(head, 1f, -0.2f, 0);

        rotate(legl1, 0, 0, -0.35f);
        rotate(footl1, 0, 0, -0.61f);
        rotate(legl2, 0, 0, -0.37f);
        rotate(footl2, 0, 0, -0.62f);
        rotate(legl3, 0, 0, -0.37f);
        rotate(footl3, 0, 0, -0.61f);

        rotate(legr1, 0, 0, 0.37f);
        rotate(footl1_1, 0, 0, -0.35f);
        rotate(legr2, 0, 0, 0.48f);
        rotate(footl2_1, 0, 0, -0.38f);
        rotate(legr3, 0, 0, 0.4f);
        rotate(footl3_1, 0, 0, -0.38f);

        for (WRModelRenderer segment : tailSegments) rotate(segment, -0.7f, 0, -0.1f);
    }

    public void sit()
    {
        torso.y = 33f;

        tail1.xRot = 0.01f;

        float legAngle = 1.4f;
        footl1.zRot = legAngle;
        footl2.zRot = legAngle;
        footl3.zRot = legAngle;

        footl1_1.zRot = -legAngle;
        footl2_1.zRot = -legAngle;
        footl3_1.zRot = -legAngle;
    }

    public static ResourceLocation texture(String png)
    {
        return Wyrmroost.id(FOLDER + "roost_stalker/" + png);
    }
}