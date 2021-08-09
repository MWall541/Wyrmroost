package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class TarragonTomeScreen extends Screen implements BookScreen
{
    public TarragonTomeScreen()
    {
        super(new TranslationTextComponent("tarragonTome.title"));
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    public static void open(PlayerEntity player, ItemStack stack)
    {
        ClientEvents.getClient().setScreen(new TarragonTomeScreen());
    }
}
