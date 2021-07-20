package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.CanariWyvernEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

/**
 * WRCanariWyvern - Ukan
 * Created using Tabula 7.0.1
 */
public class CanariWyvernModel extends DragonEntityModel<CanariWyvernEntity>
{
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[10];

    // Easter egg
    private static final ResourceLocation LADY = texture("lady.png");
    private static final ResourceLocation RUDY = texture("rudy.png");

    public WRModelRenderer body1;
    public WRModelRenderer body2;
    public WRModelRenderer neck1;
    public WRModelRenderer wing1L;
    public WRModelRenderer wing1R;
    public WRModelRenderer tail1;
    public WRModelRenderer leg1L;
    public WRModelRenderer leg1R;
    public WRModelRenderer tail2;
    public WRModelRenderer tailflap1;
    public WRModelRenderer tail3;
    public WRModelRenderer tailflap2;
    public WRModelRenderer tail4;
    public WRModelRenderer tailflap3;
    public WRModelRenderer stinger11;
    public WRModelRenderer tailflap4;
    public WRModelRenderer stinger12;
    public WRModelRenderer stinger13;
    public WRModelRenderer leg2L;
    public WRModelRenderer leg3L;
    public WRModelRenderer footL;
    public WRModelRenderer claw2L;
    public WRModelRenderer claw1L;
    public WRModelRenderer claw3L;
    public WRModelRenderer claw4L;
    public WRModelRenderer leg2R;
    public WRModelRenderer leg3R;
    public WRModelRenderer footL_1;
    public WRModelRenderer claw2R;
    public WRModelRenderer claw1R;
    public WRModelRenderer claw3R;
    public WRModelRenderer claw4R;
    public WRModelRenderer neck2;
    public WRModelRenderer head;
    public WRModelRenderer snout;
    public WRModelRenderer jaw;
    public WRModelRenderer head_1;
    public WRModelRenderer beak;
    public WRModelRenderer wing2L;
    public WRModelRenderer membrane3L;
    public WRModelRenderer feathers1L;
    public WRModelRenderer palmL;
    public WRModelRenderer feathers2L;
    public WRModelRenderer phalang1L;
    public WRModelRenderer phalang2L;
    public WRModelRenderer phalang3L;
    public WRModelRenderer membrane1L;
    public WRModelRenderer feathers3L;
    public WRModelRenderer membrane2L;
    public WRModelRenderer membrane3L_1;
    public WRModelRenderer wing2R;
    public WRModelRenderer membrane3R;
    public WRModelRenderer feathers1R;
    public WRModelRenderer palmR;
    public WRModelRenderer feathers2R;
    public WRModelRenderer phalang1R;
    public WRModelRenderer phalang2R;
    public WRModelRenderer phalang3R;
    public WRModelRenderer membrane1R;
    public WRModelRenderer feathers3R;
    public WRModelRenderer membrane2R;
    public WRModelRenderer membrane3R_1;

    public WRModelRenderer[] headArray;
    public WRModelRenderer[] tailArray;

    // false == left, true == right
    public boolean preenSide = false;

    public CanariWyvernModel()
    {
        texWidth = 150;
        texHeight = 150;
        feathers3L = new WRModelRenderer(this, 87, 85);
        feathers3L.setPos(-1.0F, -0.9F, -0.6F);
        feathers3L.addBox(0.0F, 0.0F, 0.0F, 12, 0, 10, 0.0F);
        claw4L = new WRModelRenderer(this, 34, 56);
        claw4L.setPos(0.0F, 0.5F, 0.7F);
        claw4L.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        feathers2L = new WRModelRenderer(this, 33, 85);
        feathers2L.setPos(5.0F, -1.12F, -1.4F);
        feathers2L.addBox(-5.0F, 0.0F, 0.0F, 10, 0, 10, 0.0F);
        claw4R = new WRModelRenderer(this, 34, 56);
        claw4R.setPos(0.0F, 0.5F, 0.7F);
        claw4R.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        stinger12 = new WRModelRenderer(this, 114, 10);
        stinger12.setPos(0.0F, -0.3F, 1.5F);
        stinger12.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        setRotateAngle(stinger12, -0.4553564018453205F, 0.0F, 0.0F);
        claw1R = new WRModelRenderer(this, 26, 56);
        claw1R.setPos(0.5F, 0.5F, -1.5F);
        claw1R.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
        setRotateAngle(claw1R, 0.0F, -0.7740535232594852F, 0.0F);
        tail3 = new WRModelRenderer(this, 91, 0);
        tail3.setPos(0.0F, -0.6F, 7.0F);
        tail3.addBox(-1.5F, -1.0F, 0.0F, 3, 2, 8, 0.0F);
        setRotateAngle(tail3, 0.31869712141416456F, 0.0F, 0.0F);
        wing1R = new WRModelRenderer(this, 24, 45);
        wing1R.setPos(-2.5F, -1.0F, -2.5F);
        wing1R.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        body2 = new WRModelRenderer(this, 33, 0);
        body2.setPos(0.05F, -0.9F, 2.0F);
        body2.addBox(-3.0F, -2.0F, 0.0F, 6, 5, 8, 0.0F);
        setRotateAngle(body2, -0.045553093477052F, 0.0F, 0.0F);
        claw3L = new WRModelRenderer(this, 26, 56);
        claw3L.setPos(-0.5F, 0.5F, -1.5F);
        claw3L.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
        setRotateAngle(claw3L, 0.0F, 0.7740535232594852F, 0.0F);
        claw2L = new WRModelRenderer(this, 26, 56);
        claw2L.setPos(0.0F, 0.5F, -1.6F);
        claw2L.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
        tailflap1 = new WRModelRenderer(this, 106, 17);
        tailflap1.setPos(0.0F, -1.0F, 1.0F);
        tailflap1.addBox(-4.5F, 0.0F, 0.0F, 9, 0, 7, 0.0F);
        phalang1L = new WRModelRenderer(this, 86, 38);
        phalang1L.setPos(2.0F, -0.2F, -0.8F);
        phalang1L.addBox(0.0F, -0.5F, -0.5F, 23, 1, 1, 0.0F);
        setRotateAngle(phalang1L, 0.0F, -0.36425021489121656F, 0.0F);
        membrane2L = new WRModelRenderer(this, 34, 68);
        membrane2L.setPos(0.0F, -0.02F, 0.0F);
        membrane2L.addBox(0.0F, 0.0F, 0.0F, 20, 0, 12, 0.0F);
        feathers1L = new WRModelRenderer(this, 60, 84);
        feathers1L.setPos(0.0F, -1.1F, 0.02F);
        feathers1L.addBox(0.0F, 0.0F, -1.5F, 10, 0, 10, 0.0F);
        stinger11 = new WRModelRenderer(this, 119, 4);
        stinger11.setPos(0.01F, 0.2F, 7.0F);
        stinger11.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 2, 0.0F);
        setRotateAngle(stinger11, -0.136659280431156F, 0.0F, 0.0F);
        membrane3L_1 = new WRModelRenderer(this, -6, 68);
        membrane3L_1.setPos(0.0F, -0.04F, 0.0F);
        membrane3L_1.addBox(0.0F, 0.0F, 0.0F, 17, 0, 12, 0.0F);
        phalang3R = new WRModelRenderer(this, 86, 62);
        phalang3R.setPos(-1.0F, -0.2F, 0.8F);
        phalang3R.addBox(-17.0F, -0.5F, -0.5F, 17, 1, 1, 0.0F);
        setRotateAngle(phalang3R, 0.0F, 1.6845917940249266F, 0.0F);
        membrane3L = new WRModelRenderer(this, -9, 83);
        membrane3L.setPos(10.0F, 0.0F, 0.0F);
        membrane3L.addBox(-10.0F, 0.0F, 0.0F, 10, 0, 16, 0.0F);
        leg3L = new WRModelRenderer(this, 14, 53);
        leg3L.setPos(0.02F, 4.0F, 0.0F);
        leg3L.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(leg3L, -1.0471975511965976F, 0.0F, 0.0F);
        phalang2L = new WRModelRenderer(this, 86, 43);
        phalang2L.setPos(2.0F, -0.2F, 0.8F);
        phalang2L.addBox(0.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        setRotateAngle(phalang2L, 0.0F, -0.9105382707654417F, 0.0F);
        feathers3R = new WRModelRenderer(this, 87, 100);
        feathers3R.setPos(1.0F, -0.9F, -0.6F);
        feathers3R.addBox(-12.0F, 0.0F, 0.0F, 12, 0, 10, 0.0F);
        leg2R = new WRModelRenderer(this, 14, 45);
        leg2R.setPos(-0.98F, 4.0F, -1.0F);
        leg2R.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(leg2R, 1.0927506446736497F, 0.0F, 0.0F);
        phalang1R = new WRModelRenderer(this, 86, 52);
        phalang1R.setPos(-2.0F, -0.2F, -0.8F);
        phalang1R.addBox(-23.0F, -0.5F, -0.5F, 23, 1, 1, 0.0F);
        setRotateAngle(phalang1R, 0.0F, 0.36425021489121656F, 0.0F);
        membrane1L = new WRModelRenderer(this, 78, 68);
        membrane1L.setPos(0.0F, 0.0F, 0.0F);
        membrane1L.addBox(0.0F, 0.0F, 0.0F, 23, 0, 12, 0.0F);
        wing1L = new WRModelRenderer(this, 24, 45);
        wing1L.mirror = true;
        wing1L.setPos(2.5F, -1.0F, -2.5F);
        wing1L.addBox(0.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        footL = new WRModelRenderer(this, 0, 57);
        footL.setPos(0.02F, 4.0F, -0.5F);
        footL.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 3, 0.0F);
        setRotateAngle(footL, 0.22759093446006054F, 0.0F, 0.0F);
        head_1 = new WRModelRenderer(this, 53, 29);
        head_1.setPos(0.0F, -1.52F, -0.5F);
        head_1.addBox(-2.5F, 0.0F, -0.2F, 5, 0, 4, 0.0F);
        setRotateAngle(head_1, 0.8196066167365371F, 0.0F, 0.0F);
        feathers2R = new WRModelRenderer(this, 33, 100);
        feathers2R.setPos(-5.0F, -1.12F, -1.4F);
        feathers2R.addBox(-5.0F, 0.0F, 0.0F, 10, 0, 10, 0.0F);
        feathers1R = new WRModelRenderer(this, 60, 100);
        feathers1R.setPos(0.0F, -1.1F, 0.02F);
        feathers1R.addBox(-10.0F, 0.0F, -1.5F, 10, 0, 10, 0.0F);
        phalang3L = new WRModelRenderer(this, 86, 47);
        phalang3L.setPos(1.0F, -0.2F, 0.8F);
        phalang3L.addBox(0.0F, -0.5F, -0.5F, 17, 1, 1, 0.0F);
        setRotateAngle(phalang3L, 0.0F, -1.6845917940249266F, 0.0F);
        claw1L = new WRModelRenderer(this, 26, 56);
        claw1L.setPos(0.5F, 0.5F, -1.5F);
        claw1L.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
        setRotateAngle(claw1L, 0.0F, -0.7740535232594852F, 0.0F);
        footL_1 = new WRModelRenderer(this, 0, 57);
        footL_1.setPos(-0.02F, 4.0F, -0.5F);
        footL_1.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 3, 0.0F);
        setRotateAngle(footL_1, 0.22759093446006054F, 0.0F, 0.0F);
        tailflap2 = new WRModelRenderer(this, 63, 27);
        tailflap2.setPos(0.0F, -0.98F, 1.0F);
        tailflap2.addBox(-4.5F, 0.0F, 0.0F, 9, 0, 7, 0.0F);
        head = new WRModelRenderer(this, 45, 18);
        head.setPos(0.0F, -0.52F, -5.0F);
        head.addBox(-2.0F, -1.5F, -3.0F, 4, 3, 3, 0.0F);
        setRotateAngle(head, 0.7740535232594852F, 0.0F, 0.0F);
        stinger13 = new WRModelRenderer(this, 122, 10);
        stinger13.setPos(0.0F, -0.1F, 1.6F);
        stinger13.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2, 0.0F);
        setRotateAngle(stinger13, -0.6829473363053812F, 0.0F, 0.0F);
        membrane3R = new WRModelRenderer(this, -9, 102);
        membrane3R.setPos(-10.0F, 0.0F, 0.0F);
        membrane3R.addBox(0.0F, 0.0F, 0.0F, 10, 0, 16, 0.0F);
        leg1R = new WRModelRenderer(this, 0, 45);
        leg1R.setPos(-2.5F, 1.1F, 3.4F);
        leg1R.addBox(-2.0F, -1.0F, -2.0F, 2, 6, 4, 0.0F);
        setRotateAngle(leg1R, 0.27314402793711257F, 0.0F, 0.0F);
        neck2 = new WRModelRenderer(this, 22, 18);
        neck2.setPos(0.0F, 0.0F, -5.1F);
        neck2.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        setRotateAngle(neck2, 0.40980330836826856F, 0.0F, 0.0F);
        phalang2R = new WRModelRenderer(this, 86, 57);
        phalang2R.setPos(-2.0F, -0.2F, 0.8F);
        phalang2R.addBox(-20.0F, -0.5F, -0.5F, 20, 1, 1, 0.0F);
        setRotateAngle(phalang2R, 0.0F, 0.9105382707654417F, 0.0F);
        tail1 = new WRModelRenderer(this, 66, 0);
        tail1.setPos(0.0F, 0.3F, 7.0F);
        tail1.addBox(-2.0F, -2.0F, 0.0F, 4, 3, 8, 0.0F);
        setRotateAngle(tail1, 0.27314402793711257F, 0.0F, 0.0F);
        palmR = new WRModelRenderer(this, 52, 52);
        palmR.setPos(-9.0F, -0.02F, 0.02F);
        palmR.addBox(-3.0F, -1.0F, -1.5F, 3, 2, 3, 0.0F);
        beak = new WRModelRenderer(this, 7, 32);
        beak.setPos(0.0F, -0.2F, -3.4F);
        beak.addBox(-1.0F, -1.0F, -1.0F, 2, 3, 1, 0.0F);
        setRotateAngle(beak, -0.6373942428283291F, 0.0F, 0.0F);
        membrane1R = new WRModelRenderer(this, 78, 121);
        membrane1R.setPos(0.0F, 0.0F, 0.0F);
        membrane1R.addBox(-23.0F, 0.0F, 0.0F, 23, 0, 12, 0.0F);
        wing2R = new WRModelRenderer(this, 52, 45);
        wing2R.setPos(-9.0F, -0.02F, 0.02F);
        wing2R.addBox(-10.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        setRotateAngle(wing2R, 0.0F, -0.18203784098300857F, 0.0F);
        tail2 = new WRModelRenderer(this, 66, 14);
        tail2.setPos(0.02F, 0.02F, 7.0F);
        tail2.addBox(-2.0F, -2.0F, 0.0F, 4, 3, 8, 0.0F);
        setRotateAngle(tail2, 0.27314402793711257F, 0.0F, 0.0F);
        tailflap3 = new WRModelRenderer(this, 86, 27);
        tailflap3.setPos(0.0F, -0.38F, 1.0F);
        tailflap3.addBox(-4.0F, 0.0F, 0.0F, 8, 0, 7, 0.0F);
        jaw = new WRModelRenderer(this, 21, 30);
        jaw.setPos(0.0F, 1.0F, -3.0F);
        jaw.addBox(-1.0F, -0.5F, -4.0F, 2, 1, 4, 0.0F);
        leg1L = new WRModelRenderer(this, 0, 45);
        leg1L.setPos(2.5F, 1.1F, 3.4F);
        leg1L.addBox(0.0F, -1.0F, -2.0F, 2, 6, 4, 0.0F);
        setRotateAngle(leg1L, 0.27314402793711257F, 0.0F, 0.0F);
        membrane3R_1 = new WRModelRenderer(this, -6, 121);
        membrane3R_1.setPos(0.0F, -0.04F, 0.0F);
        membrane3R_1.addBox(-17.0F, 0.0F, 0.0F, 17, 0, 12, 0.0F);
        neck1 = new WRModelRenderer(this, 0, 18);
        neck1.setPos(0.0F, -0.6F, -3.5F);
        neck1.addBox(-2.0F, -2.0F, -6.0F, 4, 4, 6, 0.0F);
        setRotateAngle(neck1, -0.40980330836826856F, 0.0F, 0.0F);
        claw2R = new WRModelRenderer(this, 26, 56);
        claw2R.setPos(0.0F, 0.5F, -1.6F);
        claw2R.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
        tailflap4 = new WRModelRenderer(this, 107, 27);
        tailflap4.setPos(0.0F, -0.36F, 1.0F);
        tailflap4.addBox(-3.5F, 0.0F, 0.0F, 7, 0, 7, 0.0F);
        wing2L = new WRModelRenderer(this, 52, 45);
        wing2L.mirror = true;
        wing2L.setPos(9.0F, -0.02F, 0.02F);
        wing2L.addBox(0.0F, -1.0F, -1.5F, 10, 2, 3, 0.0F);
        setRotateAngle(wing2L, 0.0F, 0.18203784098300857F, 0.0F);
        claw3R = new WRModelRenderer(this, 26, 56);
        claw3R.setPos(-0.5F, 0.5F, -1.5F);
        claw3R.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 2, 0.0F);
        setRotateAngle(claw3R, 0.0F, 0.7740535232594852F, 0.0F);
        leg3R = new WRModelRenderer(this, 14, 53);
        leg3R.setPos(-0.02F, 4.0F, 0.0F);
        leg3R.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(leg3R, -1.0471975511965976F, 0.0F, 0.0F);
        membrane2R = new WRModelRenderer(this, 34, 121);
        membrane2R.setPos(0.0F, -0.02F, 0.0F);
        membrane2R.addBox(-20.0F, 0.0F, 0.0F, 20, 0, 12, 0.0F);
        body1 = new WRModelRenderer(this, 0, 0);
        body1.setPos(0.0F, 17.0F, 0.0F);
        body1.addBox(-3.5F, -3.0F, -5.0F, 7, 5, 8, 0.0F);
        setRotateAngle(body1, -0.5009094953223726F, 0.0F, 0.0F);
        snout = new WRModelRenderer(this, 38, 29);
        snout.setPos(0.0F, -0.2F, -3.0F);
        snout.addBox(-1.5F, -1.0F, -4.0F, 3, 2, 5, 0.0F);
        tail4 = new WRModelRenderer(this, 91, 15);
        tail4.setPos(0.0F, 0.0F, 7.0F);
        tail4.addBox(-1.0F, -0.5F, 0.0F, 2, 1, 8, 0.0F);
        setRotateAngle(tail4, 0.18203784098300857F, 0.0F, 0.0F);
        leg2L = new WRModelRenderer(this, 14, 45);
        leg2L.setPos(0.98F, 4.0F, -1.0F);
        leg2L.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(leg2L, 1.0927506446736497F, 0.0F, 0.0F);
        palmL = new WRModelRenderer(this, 52, 52);
        palmL.mirror = true;
        palmL.setPos(9.0F, -0.02F, 0.02F);
        palmL.addBox(0.0F, -1.0F, -1.5F, 3, 2, 3, 0.0F);
        phalang1L.addChild(feathers3L);
        footL.addChild(claw4L);
        wing2L.addChild(feathers2L);
        footL_1.addChild(claw4R);
        stinger11.addChild(stinger12);
        footL_1.addChild(claw1R);
        tail2.addChild(tail3);
        body1.addChild(wing1R);
        body1.addChild(body2);
        footL.addChild(claw3L);
        footL.addChild(claw2L);
        tail1.addChild(tailflap1);
        palmL.addChild(phalang1L);
        phalang2L.addChild(membrane2L);
        wing1L.addChild(feathers1L);
        tail4.addChild(stinger11);
        phalang3L.addChild(membrane3L_1);
        palmR.addChild(phalang3R);
        wing1L.addChild(membrane3L);
        leg2L.addChild(leg3L);
        palmL.addChild(phalang2L);
        phalang1R.addChild(feathers3R);
        leg1R.addChild(leg2R);
        palmR.addChild(phalang1R);
        phalang1L.addChild(membrane1L);
        body1.addChild(wing1L);
        leg3L.addChild(footL);
        head.addChild(head_1);
        wing2R.addChild(feathers2R);
        wing1R.addChild(feathers1R);
        palmL.addChild(phalang3L);
        footL.addChild(claw1L);
        leg3R.addChild(footL_1);
        tail2.addChild(tailflap2);
        neck2.addChild(head);
        stinger12.addChild(stinger13);
        wing1R.addChild(membrane3R);
        body2.addChild(leg1R);
        neck1.addChild(neck2);
        palmR.addChild(phalang2R);
        body2.addChild(tail1);
        wing2R.addChild(palmR);
        snout.addChild(beak);
        phalang1R.addChild(membrane1R);
        wing1R.addChild(wing2R);
        tail1.addChild(tail2);
        tail3.addChild(tailflap3);
        head.addChild(jaw);
        body2.addChild(leg1L);
        phalang3R.addChild(membrane3R_1);
        body1.addChild(neck1);
        footL_1.addChild(claw2R);
        tail4.addChild(tailflap4);
        wing1L.addChild(wing2L);
        footL_1.addChild(claw3R);
        leg2R.addChild(leg3R);
        phalang2R.addChild(membrane2R);
        head.addChild(snout);
        tail3.addChild(tail4);
        leg1L.addChild(leg2L);
        wing2L.addChild(palmL);

        setDefaultPose();

        headArray = new WRModelRenderer[] {head_1, neck1, neck2};
        tailArray = new WRModelRenderer[] {tail1, tail2, tail3, tail4};
    }

    @Override
    public ResourceLocation getTexture(CanariWyvernEntity canari)
    {
        if (canari.hasCustomName())
        {
            String name = canari.getCustomName().getString();
            if (name.equals("Rudy")) return RUDY;
            else if (name.equals("Lady Everlyn Winklestein") && !canari.isMale()) return LADY;
        }

        int texture = canari.isMale()? 0 : 5 + canari.getVariant();
        if (TEXTURES[texture] == null)
            return TEXTURES[texture] = texture("body_" + canari.getVariant() + (canari.isMale()? "m" : "f") + ".png");
        return TEXTURES[texture];
    }

    @Override
    public float getShadowRadius(CanariWyvernEntity entity)
    {
        return 0.5f;
    }

    @Override
    public void scale(CanariWyvernEntity entity, MatrixStack ms, float partialTicks)
    {
        super.scale(entity, ms, partialTicks);
        ms.scale(0.5f, 0.5f, 0.5f);
        ms.translate(0, 1, 0);
    }

    @Override
    public void renderToBuffer(MatrixStack ms, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        body1.render(ms, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(CanariWyvernEntity entity, float limbSwing, float limbSwingAmount, float bob, float yaw, float pitch)
    {
        reset();
        animator().tick(entity, this, partialTicks);
        setInitialPositions();

        this.bob = bob;

        if (entity.isFlying())
        {
            flap(wing1L, globalSpeed, 1f, false, 0, 0, limbSwing, limbSwingAmount);
            flap(wing2L, globalSpeed, 0.75f, false, -1.5f, 0, limbSwing, limbSwingAmount);
            flap(wing1R, globalSpeed, 1f, true, 0, 0, limbSwing, limbSwingAmount);
            flap(wing2R, globalSpeed, 0.75f, true, -1.5f, 0, limbSwing, limbSwingAmount);
        }
        else
        {
            wing1L.zRot -= limbSwingAmount * 0.35;
            wing1R.zRot += limbSwingAmount * 0.35;

            body1.y += bob(1f, 0.25f, true, limbSwing, limbSwingAmount);

            swing(wing1L, globalSpeed + 0.5f, 0.25f, false, 0, 0, limbSwing, limbSwingAmount);
            flap(wing1L, globalSpeed + 0.5f, 0.25f, false, 0, 0, limbSwing, limbSwingAmount);

            swing(wing1R, globalSpeed + 0.5f, 0.25f, false, 0, 0, limbSwing, limbSwingAmount);
            flap(wing1R, globalSpeed + 0.5f, 0.25f, false, 0, 0, limbSwing, limbSwingAmount);

            walk(leg1L, globalSpeed + 0.5f, 1, false, 0, 0, limbSwing, limbSwingAmount);
            walk(leg2L, globalSpeed + 0.5f, 0.8f, false, -1.8f, 0.5f, limbSwing, limbSwingAmount);
            walk(leg3L, globalSpeed + 0.5f, -1.2f, false, -1.8f, -0.5f, limbSwing, limbSwingAmount);
            walk(footL, globalSpeed + 0.5f, 2f, true, 0.75f, -1, limbSwing, limbSwingAmount);

            walk(leg1R, globalSpeed + 0.5f, 1, true, 0, 0, limbSwing, limbSwingAmount);
            walk(leg2R, globalSpeed + 0.5f, -0.8f, false, -1.8f, 0.5f, limbSwing, limbSwingAmount);
            walk(leg3R, globalSpeed + 0.5f, 1.2f, false, -1.8f, -0.5f, limbSwing, limbSwingAmount);
            walk(footL_1, globalSpeed + 0.5f, -2f, true, 0.75f, -1, limbSwing, limbSwingAmount);
        }

        if (entity.isInSittingPose() && !entity.isFlying()) sitPose();
        if (entity.isSleeping()) sleepPose();

        idle(bob);

        if (this.entity.isFlying() && entity.getAnimation() != CanariWyvernEntity.ATTACK_ANIMATION)
            body1.xRot = pitch * (Mafs.PI / 180f);
        faceTarget(yaw, pitch, 1, neck1, neck2, head);
    }

    // Standing pose without the t-pose wings shit
    public void setInitialPositions()
    {
        if (entity.isFlying())
        {
            body2.xRot = 0f;

            for (ModelRenderer model : headArray) model.xRot = 0f;
            head.xRot = 0f;
            head_1.xRot = 0.5f;

            for (ModelRenderer model : tailArray) model.xRot = 0f;

            leg1L.xRot = -1f;
            leg2L.xRot = 2.7f;
            leg3L.xRot = -2f;
            footL.xRot = 1f;
            claw1L.xRot = 0.2f;
            claw2L.xRot = 0.2f;
            claw3L.xRot = 0.2f;

            leg1R.xRot = -1f;
            leg2R.xRot = 2.7f;
            leg3R.xRot = -2f;
            footL_1.xRot = 1f;
            claw1R.xRot = 0.2f;
            claw2R.xRot = 0.2f;
            claw3R.xRot = 0.2f;
        }
        else
        {
            // left wing
            wing1L.xRot = 1f;
            wing1L.yRot = -0.8f;
            wing2L.yRot = 2.5f;
            palmL.yRot = -0.8f;
            phalang1L.yRot = -1.4f;
            phalang2L.yRot = -1.85f;
            phalang3L.yRot = -2.15f;
            feathers1L.xRot = 0.05f;
            feathers1L.yRot = 0.3f;
            feathers2L.yRot = -1.2f;
            feathers3L.xRot = 0.001f;
            membrane3L.yRot = 3f;
            membrane3L_1.yRot = 0.8f;

            // right wing
            wing1R.xRot = 1f;
            wing1R.yRot = 0.8f;
            wing2R.yRot = -2.5f;
            palmR.yRot = 0.8f;
            phalang1R.yRot = 1.4f;
            phalang2R.yRot = 1.85f;
            phalang3R.yRot = 2.15f;
            feathers1R.xRot = 0.05f;
            feathers1R.yRot = -0.3f;
            feathers2R.yRot = 1.2f;
            feathers3R.xRot = 0.001f;
            membrane3R.yRot = -3f;
            membrane3R_1.yRot = -0.8f;
        }
    }

    public void sitPose()
    {
        boolean shouldSwap = entity.getVehicle() instanceof PlayerEntity && entity.getVehicle().getPassengers().indexOf(entity) == 1;
        float tailRot = shouldSwap? 0.5f : -0.5f;

        body1.y = 24f;

        wing1L.xRot = 0.6f;
        wing1R.xRot = 0.6f;

        if (entity.getVehicle() instanceof PlayerEntity)
        {
            wing1L.yRot = -1.85f;
            wing1R.yRot = 1.85f;
            tailRot *= 0.7f;
        }

        leg1L.xRot = -0.75f;
        leg2L.xRot = 2.3f;
        leg3L.xRot = -2f;
        footL.xRot = 1f;

        leg1R.xRot = -0.75f;
        leg2R.xRot = 2.3f;
        leg3R.xRot = -2f;
        footL_1.xRot = 1f;

        tail1.xRot = -0.1f;
        tail2.xRot = -0.3f;
        tail3.xRot = -0.2f;
        tail4.xRot = -0.1f;

        for (WRModelRenderer tail : tailArray)
            tail.yRot = tailRot;
    }


    public void sleepPose()
    {
        sitPose();

        neck1.xRot = -0.8f;
        neck2.xRot = 0.6f;
        head.xRot = 1.8f;
    }

    public void idle(float frame)
    {
        if (entity.isFlying())
        {
            chainWave(headArray, globalSpeed - 0.1f, 0.05f, 2.5, frame, 1f);
            chainWave(tailArray, globalSpeed - 0.1f, 0.025f, -2.5, frame, 1f);
            walk(leg3L, globalSpeed - 0.1f, 0.01f, false, 0, 0, frame, 1f);
            walk(leg3R, globalSpeed - 0.1f, 0.01f, false, 0, 0, frame, 1f);
        }
        else
        {
            body1.xRot += Math.cos(frame * (globalSpeed - 0.45f)) * 0.08f;
            body2.xRot -= Math.cos(frame * (globalSpeed - 0.45f)) * 0.08f;

            chainWave(headArray, 0.45f - globalSpeed, 0.1f, 2.5, frame, 0.5f);
            chainWave(tailArray, 0.44f - globalSpeed, 0.05f, 2.5, frame, 0.5f);
            flap(wing1L, 0.45f - globalSpeed, 0.2f, false, 0, 0, frame, 0.5f);
            swing(wing1L, 0.46f - globalSpeed, 0.09f, false, 0.3f, 0, frame, 0.5f);
            swing(palmL, 0.46f - globalSpeed, 0.1f, false, 0.3f, 0, frame, 0.5f);
            flap(wing1R, 0.45f - globalSpeed, 0.2f, true, 0, 0, frame, 0.5f);
            swing(wing1R, 0.46f - globalSpeed, 0.09f, true, 0.3f, 0, frame, 0.5f);
            swing(palmR, 0.46f - globalSpeed, 0.1f, true, 0.3f, 0, frame, 0.5f);
        }
    }

    public void flapWingsAnimation()
    {
        // extend wings
        animator().startKeyframe(5);

        animator().rotate(body1, -0.5f, 0, 0);
        animator().rotate(neck1, 0.5f, 0, 0);
        animator().rotate(tail1, 1f, 0, 0);
        animator().rotate(leg2L, 0.5f, 0, 0);
        animator().rotate(leg2R, 0.5f, 0, 0);

        animator().rotate(wing1L, -1, 0.8f, 0);
        animator().rotate(wing2L, 0, -1.7f, 0);
        animator().rotate(palmL, 0, 0.4f, 0);
        animator().rotate(phalang1L, 0, 1f, 0);
        animator().rotate(phalang2L, 0, 1.25f, 0);
        animator().rotate(phalang3L, 0, 1.15f, 0);
        animator().rotate(feathers1L, -0.05f, -0.3f, 0);
        animator().rotate(feathers2L, 0, 1.2f, 0);
        animator().rotate(feathers3L, -0.001f, 0, 0);
        animator().rotate(membrane3L, 0, -3f, 0);
        animator().rotate(membrane3L_1, 0, -0.8f, 0);

        animator().rotate(wing1R, -1, -0.8f, 0);
        animator().rotate(wing2R, 0, 1.7f, 0);
        animator().rotate(palmR, 0, -0.4f, 0);
        animator().rotate(phalang1R, 0, -1f, 0);
        animator().rotate(phalang2R, 0, -1.25f, 0);
        animator().rotate(phalang3R, 0, -1.15f, 0);
        animator().rotate(feathers1R, -0.05f, 0.3f, 0);
        animator().rotate(feathers2R, 0, -1.2f, 0);
        animator().rotate(feathers3R, -0.001f, 0, 0);
        animator().rotate(membrane3R, 0, 3f, 0);
        animator().rotate(membrane3R_1, 0, 0.8f, 0);

        animator().endKeyframe();

        // flap forward
        animator().startKeyframe(3);

        animator().rotate(body1, -0.5f, 0, 0);
        animator().rotate(neck1, 0.5f, 0, 0);
        animator().rotate(tail1, 1f, 0, 0);
        animator().rotate(leg2L, 0.5f, 0, 0);
        animator().rotate(leg2R, 0.5f, 0, 0);

        animator().rotate(wing1L, -1, 0.8f, 0);
        animator().rotate(wing2L, 0, -1.7f, 0);
        animator().rotate(palmL, 0, 0.4f, 0);
        animator().rotate(phalang1L, 0, 1f, 0);
        animator().rotate(phalang2L, 0, 1.25f, 0);
        animator().rotate(phalang3L, 0, 1.15f, 0);
        animator().rotate(feathers1L, -0.05f, -0.3f, 0);
        animator().rotate(feathers2L, 0, 1.2f, 0);
        animator().rotate(feathers3L, -0.001f, 0, 0);
        animator().rotate(membrane3L, 0, -3f, 0);
        animator().rotate(membrane3L_1, 0, -0.8f, 0);

        animator().rotate(wing1R, -1, -0.8f, 0);
        animator().rotate(wing2R, 0, 1.7f, 0);
        animator().rotate(palmR, 0, -0.4f, 0);
        animator().rotate(phalang1R, 0, -1f, 0);
        animator().rotate(phalang2R, 0, -1.25f, 0);
        animator().rotate(phalang3R, 0, -1.15f, 0);
        animator().rotate(feathers1R, -0.05f, 0.3f, 0);
        animator().rotate(feathers2R, 0, -1.2f, 0);
        animator().rotate(feathers3R, -0.001f, 0, 0);
        animator().rotate(membrane3R, 0, 3f, 0);
        animator().rotate(membrane3R_1, 0, 0.8f, 0);

        animator().rotate(wing1L, 0, 1, 1); // *
        animator().rotate(wing1R, 0, -1, -1); // *
        animator().endKeyframe();

        // flap back
        animator().startKeyframe(3);
        animator().rotate(body1, -0.5f, 0, 0);
        animator().rotate(neck1, 0.5f, 0, 0);
        animator().rotate(tail1, 1f, 0, 0);
        animator().rotate(leg2L, 0.5f, 0, 0);
        animator().rotate(leg2R, 0.5f, 0, 0);

        animator().rotate(wing1L, -1, 0.8f, 0);
        animator().rotate(wing2L, 0, -1.7f, 0);
        animator().rotate(palmL, 0, 0.4f, 0);
        animator().rotate(phalang1L, 0, 1f, 0);
        animator().rotate(phalang2L, 0, 1.25f, 0);
        animator().rotate(phalang3L, 0, 1.15f, 0);
        animator().rotate(feathers1L, -0.05f, -0.3f, 0);
        animator().rotate(feathers2L, 0, 1.2f, 0);
        animator().rotate(feathers3L, -0.001f, 0, 0);
        animator().rotate(membrane3L, 0, -3f, 0);
        animator().rotate(membrane3L_1, 0, -0.8f, 0);

        animator().rotate(wing1R, -1, -0.8f, 0);
        animator().rotate(wing2R, 0, 1.7f, 0);
        animator().rotate(palmR, 0, -0.4f, 0);
        animator().rotate(phalang1R, 0, -1f, 0);
        animator().rotate(phalang2R, 0, -1.25f, 0);
        animator().rotate(phalang3R, 0, -1.15f, 0);
        animator().rotate(feathers1R, -0.05f, 0.3f, 0);
        animator().rotate(feathers2R, 0, -1.2f, 0);
        animator().rotate(feathers3R, -0.001f, 0, 0);
        animator().rotate(membrane3R, 0, 3f, 0);
        animator().rotate(membrane3R_1, 0, 0.8f, 0);

        animator().endKeyframe();

        // flap forward
        animator().startKeyframe(3);

        animator().rotate(body1, -0.5f, 0, 0);
        animator().rotate(neck1, 0.5f, 0, 0);
        animator().rotate(tail1, 1f, 0, 0);
        animator().rotate(leg2L, 0.5f, 0, 0);
        animator().rotate(leg2R, 0.5f, 0, 0);

        animator().rotate(wing1L, -1, 0.8f, 0);
        animator().rotate(wing2L, 0, -1.7f, 0);
        animator().rotate(palmL, 0, 0.4f, 0);
        animator().rotate(phalang1L, 0, 1f, 0);
        animator().rotate(phalang2L, 0, 1.25f, 0);
        animator().rotate(phalang3L, 0, 1.15f, 0);
        animator().rotate(feathers1L, -0.05f, -0.3f, 0);
        animator().rotate(feathers2L, 0, 1.2f, 0);
        animator().rotate(feathers3L, -0.001f, 0, 0);
        animator().rotate(membrane3L, 0, -3f, 0);
        animator().rotate(membrane3L_1, 0, -0.8f, 0);

        animator().rotate(wing1R, -1, -0.8f, 0);
        animator().rotate(wing2R, 0, 1.7f, 0);
        animator().rotate(palmR, 0, -0.4f, 0);
        animator().rotate(phalang1R, 0, -1f, 0);
        animator().rotate(phalang2R, 0, -1.25f, 0);
        animator().rotate(phalang3R, 0, -1.15f, 0);
        animator().rotate(feathers1R, -0.05f, 0.3f, 0);
        animator().rotate(feathers2R, 0, -1.2f, 0);
        animator().rotate(feathers3R, -0.001f, 0, 0);
        animator().rotate(membrane3R, 0, 3f, 0);
        animator().rotate(membrane3R_1, 0, 0.8f, 0);

        animator().rotate(wing1L, 0, 1, 1); // *
        animator().rotate(wing1R, 0, -1, -1); // *
        animator().endKeyframe();

        // flap back
        animator().startKeyframe(3);
        animator().rotate(body1, -0.5f, 0, 0);
        animator().rotate(neck1, 0.5f, 0, 0);
        animator().rotate(tail1, 1f, 0, 0);
        animator().rotate(leg2L, 0.5f, 0, 0);
        animator().rotate(leg2R, 0.5f, 0, 0);

        animator().rotate(wing1L, -1, 0.8f, 0);
        animator().rotate(wing2L, 0, -1.7f, 0);
        animator().rotate(palmL, 0, 0.4f, 0);
        animator().rotate(phalang1L, 0, 1f, 0);
        animator().rotate(phalang2L, 0, 1.25f, 0);
        animator().rotate(phalang3L, 0, 1.15f, 0);
        animator().rotate(feathers1L, -0.05f, -0.3f, 0);
        animator().rotate(feathers2L, 0, 1.2f, 0);
        animator().rotate(feathers3L, -0.001f, 0, 0);
        animator().rotate(membrane3L, 0, -3f, 0);
        animator().rotate(membrane3L_1, 0, -0.8f, 0);

        animator().rotate(wing1R, -1, -0.8f, 0);
        animator().rotate(wing2R, 0, 1.7f, 0);
        animator().rotate(palmR, 0, -0.4f, 0);
        animator().rotate(phalang1R, 0, -1f, 0);
        animator().rotate(phalang2R, 0, -1.25f, 0);
        animator().rotate(phalang3R, 0, -1.15f, 0);
        animator().rotate(feathers1R, -0.05f, 0.3f, 0);
        animator().rotate(feathers2R, 0, -1.2f, 0);
        animator().rotate(feathers3R, -0.001f, 0, 0);
        animator().rotate(membrane3R, 0, 3f, 0);
        animator().rotate(membrane3R_1, 0, 0.8f, 0);

        animator().endKeyframe();

        animator().resetKeyframe(5);
    }

    public void preenAnimation()
    {
        int tick = animator().getEntity().getAnimationTick();
        if (tick == 0) preenSide = new Random().nextBoolean();

        animator().startKeyframe(8);

        if (preenSide) // right side
        {
            for (WRModelRenderer box : headArray)
                animator().rotate(box, 0.5f, 0.7f, 0);
            animator().rotate(head, -1f, 2, 0);
            animator().rotate(wing1R, -0.5f, 0.2f, 0);
        }
        else // left side
        {
            for (WRModelRenderer box : headArray)
                animator().rotate(box, 0.5f, -0.7f, 0);
            animator().rotate(head, -1f, -2, 0);
            animator().rotate(wing1L, -0.5f, 0.2f, 0);
        }

        animator().endKeyframe();
        animator().setStaticKeyframe(20);
        animator().resetKeyframe(8);

        if (tick > 8 && tick < 28) flap(head, globalSpeed + 0.5f, 0.25f, false, 0, 0, bob, 0.5f);
    }

    public void threatAnimation()
    {
        int tick = animator().getEntity().getAnimationTick();
        animator().startKeyframe(8);
        for (WRModelRenderer box : tailArray)
        {
            animator().rotate(box, 0.6f, 0, 0);
            if (tick < 20) walk(box, globalSpeed + 3f, 0.025f, false, 0, 0, bob, 0.5f);
        }
        animator().rotate(body1, 0.5f, 0, 0);
        animator().move(body1, 0, 1f, 0);
        animator().rotate(neck1, 1.5f, 0, 0);
        animator().rotate(neck2, -1.5f, 0, 0);
        animator().rotate(head, -1.5f, 0, 0);
        animator().rotate(head_1, 1f, 0, 0);
        animator().rotate(jaw, 0.75f, 0, 0);
        animator().rotate(wing1L, 0, 1, -1);
        animator().rotate(wing1R, 0, -1, 1);
        animator().rotate(leg1L, -0.5f, 0, 0);
        animator().rotate(leg1R, -0.5f, 0, 0);
        animator().endKeyframe();
        animator().setStaticKeyframe(13);
        animator().resetKeyframe(8);
    }

    public void attackAnimation()
    {
        if (entity.isFlying())
        {
            animator().startKeyframe(5);
            animator().rotate(body1, -0.35f, 0, 0);
            animator().rotate(neck1, 0.5f, 0, 0);
            animator().rotate(neck2, 0.5f, 0, 0);
            animator().rotate(head, 0.65f, 0, 0);
            animator().rotate(leg1L, 0.5f, 0, 0);
            animator().rotate(leg2L, -1, 0, 0);
            animator().rotate(leg3L, 0.5f, 0, 0);
            animator().rotate(footL, -1f, 0, 0);
            animator().rotate(leg1R, 0.5f, 0, 0);
            animator().rotate(leg2R, -1, 0, 0);
            animator().rotate(leg3R, 0.5f, 0, 0);
            animator().rotate(footL_1, -1f, 0, 0);
            for (WRModelRenderer box : tailArray) animator().rotate(box, -0.7f, 0, 0);
//            animator.rotate(tail4, -0.8f, 0, 0);
            animator().endKeyframe();
            animator().setStaticKeyframe(5);
            animator().resetKeyframe(5);
        }
        else
        {
            animator().startKeyframe(5);
            animator().rotate(body1, 0.5f, 0, 0);
            animator().move(body1, 0, 1.25f, 0);
            animator().rotate(leg1L, -0.5f, 0, 0);
            animator().rotate(leg1R, -0.5f, 0, 0);

            animator().rotate(neck1, 1.5f, 0, 0);
            animator().rotate(neck2, -1.5f, 0, 0);
            animator().rotate(head, -1.5f, 0, 0);
            animator().rotate(jaw, 0.2f, 0, 0);
            animator().endKeyframe();
            animator().startKeyframe(2);
            animator().rotate(body1, 0.5f, 0, 0);
            animator().move(body1, 0, 1.25f, 0);
            animator().rotate(leg1L, -0.5f, 0, 0);
            animator().rotate(leg1R, -0.5f, 0, 0);

            animator().rotate(neck1, 1.5f, 0, 0);
            animator().rotate(neck2, -1.5f, 0, 0);
            animator().rotate(head, -1.5f, 0, 0);
            animator().rotate(jaw, 0.2f, 0, 0);
            for (WRModelRenderer box : tailArray) animator().rotate(box, 0.8f, 0, 0);
            animator().rotate(tail4, -0.8f, 0, 0);
            animator().endKeyframe();
            animator().resetKeyframe(10);
        }
    }

    private static ResourceLocation texture(String png)
    {
        return Wyrmroost.id(FOLDER + "canari_wyvern/" + png);
    }
}
