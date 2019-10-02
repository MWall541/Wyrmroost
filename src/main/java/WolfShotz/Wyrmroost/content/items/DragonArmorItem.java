package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
            case GEODE: return SetupItems.ArmorMaterialList.GEODE.getEnchantability();
        }
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }
    
    public DragonArmorType getType() { return type; }
    
    public float getDmgReduction() { return type.getDmgReduction(); }
    
    public enum DragonArmorType
    {
        IRON(0.8f),
        GOLD(0.87f),
        DIAMOND(0.75f),
        PLATINUM(0.78f),
        GEODE(0.68f);
        
        private float dmgReduction;
        
        DragonArmorType(float dmgReduction) { this.dmgReduction = dmgReduction; }
    
        public float getDmgReduction() { return dmgReduction; }
    }
}
