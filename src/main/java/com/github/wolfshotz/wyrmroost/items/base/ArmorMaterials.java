package com.github.wolfshotz.wyrmroost.items.base;

import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

/**
 * @see net.minecraft.item.ArmorMaterial
 */
public enum ArmorMaterials implements IArmorMaterial
{
    BLUE_GEODE(new int[] {3, 6, 8, 3}, 1f, 35, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.BLUE_GEODE),
    RED_GEODE(new int[] {3, 6, 9, 4}, 2.5f, 37, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.RED_GEODE),
    PURPLE_GEODE(new int[] {4, 8, 12, 6}, 4f, 45, 28, 0, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.RED_GEODE, Rarity.RARE),
    PLATINUM(new int[] {2, 5, 7, 2}, 0.1f, 20, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, WRItems.PLATINUM_INGOT),
    DRAKE(new int[] {3, 6, 8, 3}, 1.2f, 32, 9, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, WRItems.DRAKE_BACKPLATE, Rarity.UNCOMMON);

    private static final int[] DURABILITY_ARRAY = new int[] {13, 15, 16, 11};
    private final int durability, enchantability, knockbackResistance;
    private final int[] dmgReduction; // boots, legs, chest, helm
    private final float toughness;
    private final SoundEvent sound;
    private final Supplier<Item> repairMaterial;
    private final Rarity rarity;

    ArmorMaterials(int[] dmgReduction, float toughness, int durability, int enchantability, SoundEvent sound, Supplier<Item> repairMaterial)
    {
        this(dmgReduction, toughness, durability, enchantability, 0, sound, repairMaterial, Rarity.COMMON);
    }

    ArmorMaterials(int[] dmgReduction, float toughness, int durability, int enchantability, int knockbackResistance, SoundEvent sound, Supplier<Item> repairMaterial, Rarity rarity)
    {
        this.durability = durability;
        this.dmgReduction = dmgReduction;
        this.enchantability = enchantability;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.sound = sound;
        this.repairMaterial = repairMaterial;
        this.rarity = rarity;
    }

    @Override
    public int getDurability(EquipmentSlotType slotIn)
    {
        return DURABILITY_ARRAY[slotIn.getIndex()] * durability;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot)
    {
        return dmgReduction[slot.getIndex()];
    }

    @Override
    public int getEnchantability()
    {
        return enchantability;
    }

    @Override
    public SoundEvent getSoundEvent()
    {
        return sound;
    }

    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(repairMaterial.get());
    }

    @Override
    public String getName()
    {
        return toString().toLowerCase();
    }

    @Override
    public float getToughness()
    {
        return toughness;
    }

    @Override
    public float getKnockbackResistance()
    {
        return knockbackResistance;
    }

    public Rarity getRarity()
    {
        return rarity;
    }


}
