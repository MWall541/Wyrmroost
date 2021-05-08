package com.github.wolfshotz.wyrmroost.items.staff.action;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public interface StaffAction
{
    String TRANSLATE_PATH = "item.wyrmroost.dragon_staff.action.";

    default boolean clickBlock(TameableDragonEntity dragon, ItemUseContext context)
    {
        return false;
    }

    default boolean rightClick(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        return false;
    }

    default void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
    }

    default void render(TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
    {
    }

    String getTranslateKey(@Nullable TameableDragonEntity dragon);

    default TranslationTextComponent getTranslation(@Nullable TameableDragonEntity dragon)
    {
        return new TranslationTextComponent(getTranslateKey(dragon));
    }
}