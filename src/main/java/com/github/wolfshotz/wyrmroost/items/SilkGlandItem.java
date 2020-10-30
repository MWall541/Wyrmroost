package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SilkGlandItem extends Item
{
    private static final int MAX_USE_TIME = 72000;

    public SilkGlandItem()
    {
        super(WRItems.builder().maxStackSize(1));
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft)
    {
        if (!(entity instanceof PlayerEntity)) return;


        int useTime = MAX_USE_TIME - timeLeft;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return MAX_USE_TIME;
    }
}
