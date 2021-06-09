package com.github.wolfshotz.wyrmroost.client.screen.widgets;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.containers.util.DynamicSlot;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class CollapsibleWidget extends Widget
{
    public static final byte TOP = 1;
    public static final byte BOTTOM = 2;
    public static final byte LEFT = 4;
    public static final byte RIGHT = 8;

    private final int u0;
    private final int v0;
    private final byte direction;
    private final ResourceLocation spriteSheet;
    public final List<DynamicSlot> slots = new ArrayList<>();
    private BooleanSupplier test = () -> true;

    public CollapsibleWidget(int u0, int v0, int width, int height, byte direction, ResourceLocation spriteSheet)
    {
        super(0, 0, width, height, StringTextComponent.EMPTY);
        this.u0 = u0;
        this.v0 = v0;
        this.direction = direction;
        this.spriteSheet = spriteSheet;
    }

    @Override
    public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        ClientEvents.getClient().getTextureManager().bind(spriteSheet);
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        blit(ms, x, y, u0, v0, width, height);
    }

    public CollapsibleWidget addSlot(DynamicSlot slot)
    {
        slot.condition(test);
        slots.add(slot);
        return this;
    }

    public CollapsibleWidget condition(BooleanSupplier test)
    {
        this.test = test;
        return this;
    }

    public boolean visible()
    {
        return this.visible = test.getAsBoolean();
    }

    public boolean collapses()
    {
        return isHovered();
    }

    public void move(float amount, int maxWidth, int maxHeight)
    {
        double xMult = ((direction & LEFT) != 0? 0 : (direction & RIGHT) != 0? 1 : 0.5);
        double yMult = ((direction & TOP) != 0? 0 : (direction & BOTTOM) != 0? 1 : 0.5);

        double x = xMult * maxWidth;
        double y = yMult * maxHeight;
        x -= xMult * width;
        y -= yMult * height;
        x -= Math.signum(0.5 - xMult) * (amount * (width - 15));
        y -= Math.signum(0.5 - yMult) * (amount * (height - 15));

        this.x = (int) x;
        this.y = (int) y;

        for (DynamicSlot slot : slots) slot.move(this.x - maxWidth / 2, this.y - maxHeight / 2);
    }

    @Override
    protected boolean isValidClickButton(int p_230987_1_)
    {
        return false;
    }
}
