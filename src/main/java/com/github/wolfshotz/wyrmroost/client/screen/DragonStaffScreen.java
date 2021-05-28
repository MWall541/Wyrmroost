package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.StaffActionButton;
import com.github.wolfshotz.wyrmroost.containers.DragonStaffContainer;
import com.github.wolfshotz.wyrmroost.containers.util.StaffUISlot;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffAction;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class DragonStaffScreen extends ContainerScreen<DragonStaffContainer>
{
    private static final ResourceLocation SPRITES = Wyrmroost.id("textures/gui/container/dragon_inventory.png");

    private final LerpedFloat collapsedTime = LerpedFloat.unit();
    private double dragX;
    private double dragY;

    public DragonStaffScreen(DragonStaffContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
        this.imageWidth = 193;
        this.imageHeight = 97;
        this.dragX = -150;
        this.dragY = 10;
    }

    @Override
    protected void init()
    {
        super.init();
        this.leftPos = (width - imageWidth) / 2;
        this.topPos = height / 2;

        int size = menu.actions.size();
        int xRadius = width / 3;
        int yRadius = height / 3;
        for (int i = 0; i < size; i++)
        {
            StaffAction action = menu.actions.get(i);
            ITextComponent name = action.getTranslation(menu.dragon);
            double deg = 2 * Math.PI * i / size - Math.toRadians(90);
            int x = ((int) (xRadius * Math.cos(deg))) + (width / 2) - 50;
            int y = ((int) (yRadius * Math.sin(deg))) + (height / 2) - 10;
            addButton(new StaffActionButton(this, action, x, y, name));
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        this.dragX = MathHelper.wrapDegrees(this.dragX + dragX);
        this.dragY = MathHelper.wrapDegrees(this.dragY + dragY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(ms);

        renderEntity(ms, mouseX, mouseY, partialTicks);
        super.render(ms, mouseX, mouseY, partialTicks);

        RenderSystem.disableDepthTest();
        TameableDragonEntity dragon = menu.dragon;
        int midX = this.width / 2;
        int midY = this.height / 2;
        if ((mouseX < midX + 50 && mouseX > midX - 50) && (mouseY < midY + 50 && mouseY > midY - 50))
            renderComponentTooltip(ms, menu.toolTips, mouseX, mouseY);
        RenderSystem.enableDepthTest();

        renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY)
    {
        float time = collapsedTime.get(partialTicks);
        float speed = 0.35f * partialTicks;
        int xMid = (width - imageWidth) / 2;
        boolean flag = mouseX > xMid && mouseX < xMid + imageWidth && mouseY > height - (30 + (70 * time));
        int y = height - (20 + (int) (75 * time));

        for (Slot slot : menu.slots)
        {
            if (slot instanceof StaffUISlot)
            {
                try
                {
                    StaffUISlot staffUISlot = (StaffUISlot) slot;
                    Field posY = ObfuscationReflectionHelper.findField(Slot.class, "field_75221_f");
                    Field posX = ObfuscationReflectionHelper.findField(Slot.class, "field_75223_e");
                    posY.setAccessible(true);
                    posX.setAccessible(true);

                    if (staffUISlot.isPlayerSlot)
                    {
                        posY.set(staffUISlot, staffUISlot.sY + y - topPos);
                    }
                    else if (staffUISlot.getItemHandler() instanceof DragonInventory)
                    {
                        TameableDragonEntity dragon = menu.dragon;
                        int rotX = (int) (staffUISlot.sX * Math.cos(Math.toRadians(-dragX + 270)));
                        int rotY = (int) (20 * Math.sin(Math.toRadians(-dragY + 270)));
                        posX.set(staffUISlot, leftPos + rotX - 8);
                        posY.set(staffUISlot, rotY - 4);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        collapsedTime.add(flag? speed : -speed);
        getMinecraft().getTextureManager().bind(SPRITES);
        blit(ms, xMid, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack ms, int mouseX, int mouseY)
    {
    }

    public boolean focusedInventory()
    {
        return collapsedTime.get() > 0;
    }

    public void renderEntity(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        TameableDragonEntity dragon = menu.dragon;
        float x = width / 2f;
        float y = (height / 2f) + (dragon.getBbHeight() * dragon.getBbHeight()) * 4f;
        float scale = height / 14f;
        float yaw = (float) Math.atan((x - mouseX) / 40);
        float pitch = (float) Math.atan((y - mouseY) / 40);
        float oBody = dragon.yBodyRot;
        float oYRot = dragon.yRot;
        float oXRot = dragon.xRot;
        float oOldYHead = dragon.yHeadRotO;
        float oYHead = dragon.yHeadRot;

        RenderSystem.pushMatrix();
        ms.pushPose();
        RenderSystem.translatef(x, y, 1100f);
        RenderSystem.scalef(1f, 1f, -1f);
        ms.translate(0, 0, 1000d);
        ms.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180f);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees((float) -dragY);
        quaternion.mul(quaternion1);
        ms.mulPose(quaternion);
        dragon.yBodyRot = (float) -dragX;
        dragon.yRot = dragon.yBodyRot;
        dragon.xRot = -pitch * 20f;
        dragon.yHeadRot = 180f + yaw * 40f;
        dragon.yHeadRotO = dragon.yHeadRot;
        EntityRendererManager renderer = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        renderer.overrideCameraOrientation(quaternion1);
        renderer.setRenderShadow(false);
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> renderer.render(dragon, 0, 0, 0, 0, 1f, ms, buffer, 15728880));
        buffer.endBatch();
        renderer.setRenderShadow(true);
        dragon.yBodyRot = oBody;
        dragon.yRot = oYRot;
        dragon.xRot = oXRot;
        dragon.yHeadRotO = oOldYHead;
        dragon.yHeadRot = oYHead;
        ms.popPose();
        RenderSystem.popMatrix();
    }
}
