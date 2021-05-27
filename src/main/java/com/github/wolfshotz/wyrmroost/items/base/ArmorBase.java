package com.github.wolfshotz.wyrmroost.items.base;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Helper class used to help register playerArmor items
 */
public class ArmorBase extends ArmorItem
{
    public ArmorBase(ArmorMaterials material, EquipmentSlotType equipType)
    {
        super(material, equipType, WRItems.builder().rarity(material.getRarity()));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        int layer = slot == EquipmentSlotType.LEGS? 2 : 1;
        return Wyrmroost.MOD_ID + ":textures/models/armor/" + this.material.getName() + "_layer_" + layer + ".png";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> lines, ITooltipFlag flags)
    {
        super.appendHoverText(stack, level, lines, flags);
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.set", new TranslationTextComponent("item.wyrmroost.armors." + material.getName()).withStyle(((ArmorMaterials) material).getRarity().color)));

        if (hasDescription())
        {
            lines.add(new StringTextComponent(""));
            lines.add(new TranslationTextComponent(String.format("item.wyrmroost.armors.%s.desc", material.getName().toLowerCase())));
        }
    }

    protected boolean hasDescription()
    {
        return false;
    }

    public void applyFullSetBonus(LivingEntity entity, boolean hasFullSet)
    {
    }

    public static boolean hasFullSet(LivingEntity entity)
    {
        IArmorMaterial prev = null;
        for (ItemStack stack : entity.getArmorSlots())
        {
            if (stack.getItem() instanceof ArmorItem)
            {
                IArmorMaterial now = ((ArmorItem) stack.getItem()).getMaterial();
                if (now == prev || prev == null)
                {
                    prev = now;
                    continue;
                }
            }
            return false;
        }
        return true;
    }
}
