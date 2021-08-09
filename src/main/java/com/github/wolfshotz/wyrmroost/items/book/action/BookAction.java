package com.github.wolfshotz.wyrmroost.items.book.action;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public interface BookAction
{
    String TRANSLATE_PATH = "item.wyrmroost.dragon_staff.action.";

    default ActionResultType clickBlock(@Nullable TameableDragonEntity dragon, ItemUseContext context)
    {
        return ActionResultType.PASS;
    }

    default ActionResultType rightClick(@Nullable TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        return ActionResultType.PASS;
    }

    default void onSelected(@Nullable TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
    }

    default void render(@Nullable TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
    {
    }

    String getTranslateKey(@Nullable TameableDragonEntity dragon);

    default TranslationTextComponent getTranslation(@Nullable TameableDragonEntity dragon)
    {
        return new TranslationTextComponent(getTranslateKey(dragon));
    }
}