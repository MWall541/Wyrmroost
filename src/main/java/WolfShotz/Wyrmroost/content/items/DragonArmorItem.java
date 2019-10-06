package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
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
    public boolean isEnchantable(ItemStack stack) { return true; }
    
    public DragonArmorType getType() { return type; }
    
    public int getDmgReduction() { return type.getDmgReduction(); }
    
    public int getID() { return type.getId(); }
    
    public enum DragonArmorType
    {
        IRON(0, 5),
        GOLD(1, 7),
        DIAMOND(2, 11),
        PLATINUM(3, 6),
        BLUE_GEODE(4, 11);
        
        private int dmgReduction;
        private int id;
        
        DragonArmorType(int id, int dmgReduction) {
            this.dmgReduction = dmgReduction;
            this.id = id;
        }
    
        public int getDmgReduction() { return dmgReduction; }
        public int getId()           { return id; }
        
        public static DragonArmorType getTypeByID(int id) {
            for (DragonArmorType type : values()) if (type.id == id) return type;
            return null;
        }
    }
}
