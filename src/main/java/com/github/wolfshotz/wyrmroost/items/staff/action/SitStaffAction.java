package com.github.wolfshotz.wyrmroost.items.staff.action;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SitStaffAction implements StaffAction
{
    @Override
    public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        dragon.setOrderedToSit(!dragon.isInSittingPose());
        DragonStaffItem.setAction(StaffActions.DEFAULT, player, stack);
    }

    @Override
    public String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        if (dragon != null && dragon.isInSittingPose()) return TRANSLATE_PATH + "sit.come";
        return TRANSLATE_PATH + "sit.stay";
    }
}
