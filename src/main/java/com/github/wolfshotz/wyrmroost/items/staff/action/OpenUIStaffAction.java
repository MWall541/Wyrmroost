package com.github.wolfshotz.wyrmroost.items.staff.action;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class OpenUIStaffAction implements StaffAction
{
    @Override
    public boolean rightClick(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        if (player.level.isClientSide) StaffScreen.open(dragon, stack);
        return true;
    }

    @Override
    public String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        return TRANSLATE_PATH + "default";
    }
}
