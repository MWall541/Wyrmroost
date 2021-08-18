package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.BookActionButton;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.CollapsibleWidget;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.PinButton;
import com.github.wolfshotz.wyrmroost.containers.BookContainer;
import com.github.wolfshotz.wyrmroost.containers.util.Slot3D;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.book.action.BookAction;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DragonControlScreen extends ContainerScreen<BookContainer> implements BookScreen
{
    public static final ResourceLocation SPRITES = Wyrmroost.id("textures/gui/container/dragon_inventory.png");
    public static final Vec2d SADDLE_UV = new Vec2d(194, 18);
    public static final Vec2d ARMOR_UV = new Vec2d(194, 34);
    public static final Vec2d CHEST_UV = new Vec2d(194, 50);
    public static final Vec2d CONDUIT_UV = new Vec2d(194, 66);

    public final LerpedFloat collapsedTime = LerpedFloat.unit();
    public final List<CollapsibleWidget> collapsibles = new ArrayList<>();
    public final PinButton pin = new PinButton(107, 0);
    public int centerX;
    public int centerY;
    public float dragX;
    public float dragY;
    public float scale;

    public DragonControlScreen(BookContainer container, PlayerInventory inventory, ITextComponent title)
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
        this.centerX = width / 2;
        this.centerY = height / 2;
        this.leftPos = centerX;
        this.topPos = centerY;
        this.scale = topPos / 7f;

        pin.x = centerX - 107;

        menu.collapsibles.forEach(this::addWidget);

        initButtons();
    }

    protected void initButtons()
    {
        addButton(pin);

        int size = menu.actions.size();
        int xRadius = width / 3;
        int yRadius = height / 3;
        for (int i = 0; i < size; i++)
        {
            BookAction action = menu.actions.get(i);
            ITextComponent name = action.getTranslation(menu.dragon);
            double deg = 2 * Math.PI * i / size - Math.toRadians(90);
            int x = ((int) (xRadius * Math.cos(deg))) + centerX - 50;
            int y = ((int) (yRadius * Math.sin(deg))) + centerY - 10;
            addButton(new BookActionButton(this, action, x, y, name));
        }
    }

    @Override
    protected <T extends IGuiEventListener> T addWidget(T widget)
    {
        if (widget instanceof CollapsibleWidget) collapsibles.add((CollapsibleWidget) widget);
        return super.addWidget(widget);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        double scale = this.scale * 2;
        boolean showAccessories = showAccessories();

        renderBackground(ms);
        renderEntity(ms, mouseX, mouseY);
        if (showAccessories) fill(ms, 0, 0, width, height, 0xd0101010);
        super.render(ms, mouseX, mouseY, partialTicks);

        renderTooltip(ms, mouseX, mouseY);
        if (!showAccessories && withinBoundary(mouseX, mouseY, centerX, centerY, scale, scale) && minecraft.player.inventory.getCarried().isEmpty() && hoveredSlot == null)
        {
            renderComponentTooltip(ms, menu.toolTips, mouseX, mouseY);
            renderEffects(ms, menu.dragon.getActiveEffects().stream().filter(e -> e.shouldRender() && e.getDuration() > 0).collect(Collectors.toList()), mouseX - 124, mouseY - 16);
        }
    }

    private void renderEffects(MatrixStack ms, List<EffectInstance> effects, int x, int y)
    {
        if (effects.isEmpty()) return;

        ms.pushPose();
        ms.translate(0, 0, 400);

        // multiple for loops to avoid binding textures many more times than needed. annoying af but w/e
        minecraft.getTextureManager().bind(SPRITES);

        // backgrounds and labels
        for (int i = 0; i < effects.size(); i++)
        {
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            int yOff = y + (i * 33);
            blit(ms, x, yOff, 122, 174, 120, 32);
        }

        for (int i = 0; i < effects.size(); i++)
        {
            EffectInstance instance = effects.get(i);
            if (instance.shouldRenderInvText())
            {
                String s = I18n.get(instance.getEffect().getDescriptionId());
                int amp = instance.getAmplifier();
                int yOff = y + (i * 33);
                if (amp >= 1 && amp <= 9) s += ' ' + I18n.get("enchantment.level." + (amp + 1));

                font.drawShadow(ms, s, x + 10 + 18, yOff + 6, 16777215);
                String duration = EffectUtils.formatDuration(instance, 1.0F);
                font.drawShadow(ms, duration, x + 10 + 18, yOff + 6 + 10, 8355711);
            }
        }

        PotionSpriteUploader sheet = minecraft.getMobEffectTextures();

        // icons
        for (int i = 0; i < effects.size(); i++)
        {
            Effect effect = effects.get(i).getEffect();
            TextureAtlasSprite atlas = sheet.get(effect);
            int yOff = y + (i * 32);
            minecraft.getTextureManager().bind(atlas.atlas().location());
            blit(ms, x + 6, yOff + 7, getBlitOffset(), 18, 18, atlas);
        }
        ms.popPose();
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY)
    {
        float time = collapsedTime.get(partialTicks);
        float speed = 0.35f * partialTicks;
        boolean flag = pin.pinned() || pin.isHovered() || hoveringWidget();

        collapsedTime.add(flag? speed : -speed);
        pin.y = (int) (height - (time * 28));

        for (CollapsibleWidget w : collapsibles)
        {
            if (w.visible())
            {
                w.move(1 - time, width, height);
                w.render(ms, mouseX, mouseY, partialTicks);
            }
        }
    }

    private boolean hoveringWidget()
    {
        for (CollapsibleWidget collapsible : collapsibles) if (collapsible.collapses()) return true;
        return false;
    }

    @Override
    protected void renderLabels(MatrixStack ms, int mouseX, int mouseY)
    {
    }

    @Override
    public void renderSlot(MatrixStack ms, Slot slot)
    {
        boolean flag = false;
        if (slot instanceof Slot3D)
        {
            Slot3D uiSlot = (Slot3D) slot;
            double scale = this.scale / 22f;
            float xRot = (dragX + 270f) / 180f * Mafs.PI;
            float yRot = (dragY + 270f) / 180f * Mafs.PI;
            Vector3d vector = new Vector3d(uiSlot.anchorY, uiSlot.anchorZ, uiSlot.anchorX)
                    .scale(scale)
                    .xRot(xRot)
                    .yRot(yRot);
            float colZ = (float) Math.max(-vector.x * 0.15f, 0.4f);

            flag = true;
            RenderSystem.pushMatrix();
            RenderSystem.translated(0, 0, -vector.x);
            uiSlot.setPos((int) vector.y - 8, (int) vector.z - 8);
            if (!slot.hasItem() && uiSlot.iconUV != null)
            {
                RenderSystem.color3f(colZ, colZ, colZ);
                setBlitOffset(250);
                getMinecraft().getTextureManager().bind(SPRITES);
                uiSlot.blitBackgroundIcon(this, ms, uiSlot.x, uiSlot.y);
            }
        }

        super.renderSlot(ms, slot);
        if (flag) RenderSystem.popMatrix();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == GLFW.GLFW_MOUSE_BUTTON_2 && (hoveredSlot == null || !hoveredSlot.hasItem()) && minecraft.player.inventory.getCarried().isEmpty())
        {
            pin.onPress();
            pin.playDownSound(minecraft.getSoundManager());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1)
        {
            this.dragX = MathHelper.wrapDegrees(this.dragX + (float) dragX);
            this.dragY = MathHelper.wrapDegrees(this.dragY + (float) dragY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta)
    {
        this.scale = MathHelper.clamp(this.scale + (float) scrollDelta, 4f, 60f);
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int topPos, int button)
    {
        return !withinBoundary(mouseX, mouseY, centerX, height - (imageHeight / 2f), imageWidth / 2f, imageHeight / 2f) &&
                !withinBoundary(mouseX, mouseY, centerX, centerY, scale * 3, scale * 3);
    }

    public boolean showAccessories()
    {
        return collapsedTime.get() > 0 || !minecraft.player.inventory.getCarried().isEmpty();
    }

    public void renderEntity(MatrixStack ms, int mouseX, int mouseY)
    {
        TameableDragonEntity dragon = menu.dragon;
        float x = centerX;
        float y = centerY + ((dragon.getBbHeight() / 2) * scale) - scale;
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
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(-dragY);
        quaternion.mul(quaternion1);
        ms.mulPose(quaternion);
        dragon.yBodyRot = -dragX;
        dragon.yRot = dragon.yBodyRot;
        dragon.xRot = -pitch * 20f;
        dragon.yHeadRot = 180f + yaw * 40f;
        dragon.yHeadRotO = dragon.yHeadRot;
        EntityRendererManager renderer = minecraft.getEntityRenderDispatcher();
        quaternion1.conj();
        renderer.overrideCameraOrientation(quaternion1);
        renderer.setRenderShadow(false);
        IRenderTypeBuffer.Impl buffer = minecraft.renderBuffers().bufferSource();
        ms.translate(0, -1, 0);
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

    public static boolean withinBoundary(double mouseX, double mouseY, double pointX, double pointY, double boundaryX, double boundaryY)
    {
        return (mouseX < pointX + boundaryX && mouseX > pointX - boundaryX) && (mouseY < pointY + boundaryY && mouseY > pointY - boundaryY);
    }
}
