package WolfShotz.Wyrmroost.util.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.ChestTileEntity;

import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.getPrivateValue;
import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.setPrivateValue;

public class ReflectionUtils
{
    /**
     * @return the protected boolean value of "isJumping" of LivingEntity Class
     */
    public static boolean isEntityJumping(LivingEntity entity) {
        return getPrivateValue(LivingEntity.class, entity, "field_70703_bu");
    }
    
    /**
     * Get the amount of players using a chest
     */
    public static int getChestPlayersUsing(ChestTileEntity instance) {
        return getPrivateValue(ChestTileEntity.class, instance, "field_145987_o");
    }
    
    /**
     * Set the amount of players using a chest.
     */
    public static void setChestPlayersUsing(ChestTileEntity instance, int amount, boolean add) {
        if (add) amount += getChestPlayersUsing(instance);
        if (amount < 0) amount = 0;
        setPrivateValue(ChestTileEntity.class, instance, amount, "field_145987_o");
    }
}
