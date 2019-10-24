package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DragonArmorItem extends Item
{
    private DragonArmorType type;
    
    public DragonArmorItem(String name, DragonArmorType type) {
        super(ModUtils.itemBuilder().maxStackSize(1));
        setRegistryName(name);
        this.type = type;
    }
    
    @Override
    public int getItemEnchantability() {
        switch(type) {
            default:
            case IRON: return ArmorMaterial.IRON.getEnchantability();
            case GOLD: return ArmorMaterial.GOLD.getEnchantability();
            case DIAMOND: return ArmorMaterial.DIAMOND.getEnchantability();
            case PLATINUM: return SetupItems.ArmorMaterialList.PLATINUM.getEnchantability();
            case BLUE_GEODE: return SetupItems.ArmorMaterialList.GEODE.getEnchantability();
        }
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.PROTECTION && enchantment.getMaxLevel() == 2;
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }
    
    public DragonArmorType getType() { return type; }
    
    public int getDmgReduction() {
        return type.getDmgReduction();
    }
    
    public enum DragonArmorType
    {
        IRON(5),
        GOLD(7),
        DIAMOND(11),
        PLATINUM(6),
        BLUE_GEODE(11),
        RED_GEODE(12),
        PURPLE_GEODE(13);
        
        private int dmgReduction;
        
        DragonArmorType(int dmgReduction) {
            this.dmgReduction = dmgReduction;
        }
    
        public int getDmgReduction() { return dmgReduction; }
    }
}
