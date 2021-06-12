package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.entities.projectile.SoulCrystalEntity;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.wolfshotz.wyrmroost.entities.projectile.SoulCrystalEntity.*;

@SuppressWarnings("ConstantConditions")
public class SoulCrystalItem extends Item
{
    public SoulCrystalItem()
    {
        super(WRItems.builder().durability(10));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        boolean more = stack.getCount() > 1;
        ItemStack split = (more? stack.split(1) : stack);
        ActionResultType result = captureDragon(player, player.level, split, target);
        if (more && !player.inventory.add(split)) player.drop(split, true);
        return result;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        return releaseDragon(context.getLevel(), context.getPlayer(), context.getItemInHand(), context.getClickedPos(), context.getClickedFace());
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f));
        if (!level.isClientSide)
        {
            SoulCrystalEntity entity = new SoulCrystalEntity(stack.split(1), player, level);
            entity.shootFromRotation(player, player.xRot, player.yRot, 0, 1.5f, 1f);
            level.addFreshEntity(entity);
        }
        return new ActionResult<>(ActionResultType.sidedSuccess(level.isClientSide), stack);
    }

    @Override
    public ITextComponent getName(ItemStack stack)
    {
        TranslationTextComponent name = (TranslationTextComponent) super.getName(stack);
        if (containsDragon(stack)) name.withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
        return name;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (containsDragon(stack))
        {
            CompoundNBT tag = stack.getTag().getCompound(DATA_DRAGON);
            ITextComponent name;

            if (tag.contains("CustomName"))
                name = ITextComponent.Serializer.fromJson(tag.getString("CustomName"));
            else name = EntityType.byString(tag.getString("id")).orElse(null).getDescription();

            tooltip.add(name.copy().withStyle(TextFormatting.BOLD));
            INBT nameData = tag.get("OwnerName");
            if (nameData != null)
            {
                tooltip.add(new StringTextComponent("Tamed by ")
                        .append(new StringTextComponent(nameData.getAsString()).withStyle(TextFormatting.ITALIC)));
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return super.isFoil(stack) || containsDragon(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 16;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue()
    {
        return 1;
    }
}
