package com.github.wolfshotz.wyrmroost.items.staff.action;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class InventoryStaffAction implements StaffAction
{
    @Override
    public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        DragonStaffItem.setAction(StaffActions.DEFAULT, player, stack);
        if (!player.level.isClientSide)
            NetworkHooks.openGui((ServerPlayerEntity) player, dragon.getInventory(), b -> b.writeInt(dragon.getId()));
    }

    @Override
    public String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        return TRANSLATE_PATH + "inventory";
    }
}