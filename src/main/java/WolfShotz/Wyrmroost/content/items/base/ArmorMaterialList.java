package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ArmorMaterialList implements IArmorMaterial
{
    GEODE("geode", new int[]{4, 7, 9, 4}, 2.8f, 48, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.GEODE_BLUE.get()),
    GEODERED("geode_red", new int[]{4, 8, 9, 5}, 3f, 52, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.GEODE_RED.get()),
    GEODEPURPLE("geode_purple", new int[]{6, 10, 12, 8}, 4f, 60, 28, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, WRItems.GEODE_RED.get()),
    PLATINUM("platinum", new int[]{2, 5, 7, 2}, 0.2f, 20, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, WRItems.PLATINUM_INGOT.get());
    
    private int[] durabilityArray = new int[]{13, 15, 16, 11};
    private int durability, enchantability;
    private int[] dmgReduction;
    private float toughness;
    private String name;
    private SoundEvent sound;
    private Item repairMaterial;
    
    ArmorMaterialList(String name, int[] dmgReduction, float toughness, int durability, int enchantability, SoundEvent sound, Item repairMaterial)
    {
        this.durability = durability;
        this.dmgReduction = dmgReduction;
        this.enchantability = enchantability;
        this.toughness = toughness;
        this.name = name;
        this.sound = sound;
        this.repairMaterial = repairMaterial;
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
        return Ingredient.fromItems(repairMaterial);
    }
    
    @Override
    public float getToughness()
    {
        return toughness;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName()
    {
        return name;
    }
}
