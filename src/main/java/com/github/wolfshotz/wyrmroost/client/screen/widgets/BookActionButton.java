package com.github.wolfshotz.wyrmroost.client.screen.widgets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.screen.DragonControlScreen;
import com.github.wolfshotz.wyrmroost.items.book.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.items.book.action.BookAction;
import com.github.wolfshotz.wyrmroost.network.packets.BookActionPacket;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class BookActionButton extends AbstractButton
{
    public final DragonControlScreen screen;
    public final BookAction action;
    public final LerpedFloat focusTime = LerpedFloat.unit();
    public boolean wasHovered = false;

    public BookActionButton(DragonControlScreen screen, BookAction action, int xIn, int yIn, ITextComponent msg)
    {
        super(xIn, yIn, 100, 20, msg);
        this.screen = screen;
        this.action = action;
    }

    @Override
    public void onPress()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.TARRAGON_TOME.get());
        if (stack != null)
        {
            TarragonTomeItem.setAction(action, player, stack);
            Wyrmroost.NETWORK.sendToServer(new BookActionPacket(action));
        }
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_)
    {
        if (visible = !screen.showAccessories())
            super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        if (wasHovered != isHovered) onFocusedChanged(wasHovered = isHovered);

        float time = 0.5f * partialTicks; // adjust speed for framerate
        focusTime.add(isHovered? time : -time);
        float amount = focusTime.get(partialTicks) * 6;
        drawCenteredString(ms,
                Minecraft.getInstance().font,
                getMessage().getString(),
                x + width / 2,
                (y + (height - 8) / 2) - (int) amount,
                (int) MathHelper.lerp(amount, 0xffffff, 0xfffd8a));
    }

    @Override
    protected void onFocusedChanged(boolean focusing)
    {
        if (focusing)
            Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.NOTE_BLOCK_BASS, -1f));
    }

    @Override
    public void playDownSound(SoundHandler sounds)
    {
        sounds.play(SimpleSound.forUI(SoundEvents.BOOK_PAGE_TURN, 1f, 1f));
    }
}
