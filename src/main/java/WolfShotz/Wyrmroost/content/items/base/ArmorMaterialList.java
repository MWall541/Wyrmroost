package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

public enum ArmorMaterialList implements IArmorMaterial
{
    GEODE("geode", new int[]{4, 7, 9, 4}, 2.8f, 48, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.BLUE_GEODE),
    GEODERED("geode_red", new int[]{4, 8, 9, 5}, 3f, 52, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.RED_GEODE),
    GEODEPURPLE("geode_purple", new int[]{6, 10, 12, 8}, 4f, 60, 28, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.RED_GEODE, Rarity.RARE),
    PLATINUM("platinum", new int[]{2, 5, 7, 2}, 0.2f, 20, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, WRItems.PLATINUM_INGOT),
    DRAKE("drake", new int[]{3, 6, 8, 3}, 1.2f, 32, 9, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, WRItems.DRAKE_BACKPLATE, Rarity.UNCOMMON);
    
    private final int[] durabilityArray = new int[]{13, 15, 16, 11};
    private final int durability, enchantability;
    private final int[] dmgReduction;
    private final float toughness;
    private final String name;
    private final SoundEvent sound;
    private final Supplier<Item> repairMaterial;
    private Rarity rarity = Rarity.COMMON;
    
    ArmorMaterialList(String name, int[] dmgReduction, float toughness, int durability, int enchantability, SoundEvent sound, Supplier<Item> repairMaterial)
    {
        this.durability = durability;
        this.dmgReduction = dmgReduction;
        this.enchantability = enchantability;
        this.toughness = toughness;
        this.name = name;
        this.sound = sound;
        this.repairMaterial = repairMaterial;
    }

    ArmorMaterialList(String name, int[] dmgReduction, float toughness, int durability, int enchantability, SoundEvent sound, Supplier<Item> repairMaterial, Rarity rarity)
    {
        this(name, dmgReduction, toughness, durability, enchantability, sound, repairMaterial);
        this.rarity = rarity;
    }
    
    @Override
    public int getDurability(EquipmentSlotType slotIn)
    {
        return durabilityArray[slotIn.getIndex()] * this.durability;
    }
    
    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn)
    {
        return dmgReduction[slotIn.getIndex()];
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
    public float getToughness()
    {
        return toughness;
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    public Rarity getRarity()
    {
        return rarity;
    }
}
