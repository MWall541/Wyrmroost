package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

public class DragonArmorItem extends Item
{
    public static final UUID ARMOR_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    private DragonArmorType type;

    public DragonArmorItem(DragonArmorType type)
    {
        super(ModUtils.itemBuilder().maxStackSize(1));
        this.type = type;
    }

    @Override
    public int getItemEnchantability()
    {
        switch (type)
        {
            default:
            case IRON:
                return ArmorMaterial.IRON.getEnchantability();
            case GOLD:
                return ArmorMaterial.GOLD.getEnchantability();
            case DIAMOND:
                return ArmorMaterial.DIAMOND.getEnchantability();
            case PLATINUM:
                return ArmorMaterialList.PLATINUM.getEnchantability();
            case BLUE_GEODE:
                return ArmorMaterialList.GEODE.getEnchantability();
        }
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.PROTECTION;
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return true;
    }

    public DragonArmorType getType()
    {
        return type;
    }

    public int getDmgReduction()
    {
        return type.getDmgReduction();
    }

    public static DragonArmorItem getArmorInInv(ItemStackHandler inv, int slot)
    {
        Item item = inv.getStackInSlot(slot).getItem();
        return item instanceof DragonArmorItem ? (DragonArmorItem) item : null;
    }

    public static void setDragonArmored(AbstractDragonEntity dragon, int slot)
    {
        if (!dragon.world.isRemote)
        {
            IAttributeInstance armorAttribute = dragon.getAttribute(SharedMonsterAttributes.ARMOR);
            DragonArmorItem armor = getArmorInInv(dragon.getInvHandler(), slot);

            armorAttribute.removeModifier(ARMOR_UUID);
            if (armor != null)
                armorAttribute.applyModifier(new AttributeModifier("Armor Modifier", armor.getDmgReduction(), AttributeModifier.Operation.ADDITION).setSaved(true));
        }
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
        
        DragonArmorType(int dmgReduction)
        {
            this.dmgReduction = dmgReduction;
        }

        public int getDmgReduction()
        {
            return dmgReduction;
        }
    }
}
