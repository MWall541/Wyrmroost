package WolfShotz.Wyrmroost.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.NonNullList;

import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.findField;
import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.getPrivateValue;

public class ReflectionUtils
{
    /**
     * @return the protected boolean value of "isJumping" of LivingEntity Class
     */
    public static boolean isEntityJumping(LivingEntity entity) {
        return getPrivateValue(LivingEntity.class, entity, "field_70703_bu");
    }
    
    /**
     * Gets the chestContents field from ChestTileEntity class
     *
     * Surrounded in a try/catch block for perforamce reasons
     * @param instance
     * @return
     */
    public static NonNullList<ItemStack> getChestItems(ChestTileEntity instance) {
        try {
            return (NonNullList<ItemStack>) findField(ChestTileEntity.class, "field_145985_p").get(instance);
        } catch (Exception e) {
            ModUtils.L.error("Exception trying to get \"chestContents\" field");
            throw new RuntimeException();
        }
    }
    
    public static void setChestItems(ChestTileEntity instance, NonNullList<ItemStack> contents) {
        try {
            findField(ChestTileEntity.class, "field_145985_p").set(instance, contents);
        } catch (Exception e) {
            ModUtils.L.error("Exception trying to set \"chestConents\"");
        }
    }
}
