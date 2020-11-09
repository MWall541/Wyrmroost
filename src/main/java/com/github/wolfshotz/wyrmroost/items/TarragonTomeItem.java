package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TarragonTomeItem extends Item
{
    public TarragonTomeItem()
    {
        super(WRItems.builder().maxStackSize(1));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity)
    {
        if (!(entity instanceof AbstractDragonEntity)) return false;
        if (stack.getTag() == null) stack.setTag(new CompoundNBT());
        CompoundNBT tag = stack.getTag();
        tag.putByte(entity.getType().getRegistryName().getPath(), (byte) 0);
        return true;
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".tooltip")
                            .appendSibling(new StringTextComponent("sgdshdf")
                                                   .applyTextStyle(TextFormatting.OBFUSCATED))
                            .applyTextStyle(TextFormatting.GRAY));
    }
}
