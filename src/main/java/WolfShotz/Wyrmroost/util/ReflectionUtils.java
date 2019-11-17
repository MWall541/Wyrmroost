package WolfShotz.Wyrmroost.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.tileentity.ChestTileEntity;

import javax.annotation.Nonnull;
import java.util.Set;

import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.getPrivateValue;
import static net.minecraftforge.fml.common.ObfuscationReflectionHelper.setPrivateValue;

/**
 * Utility Class to help grab the private values of minecraft.
 * WARNING: these are rather expensive operations and shouldnt be called frequently!
 */
public class ReflectionUtils
{
    /**
     * @return the protected boolean value of "isJumping" of LivingEntity Class
     */
    public static boolean isEntityJumping(@Nonnull LivingEntity entity) {
        return getPrivateValue(LivingEntity.class, entity, "field_70703_bu");
    }
    
    /**
     * Get the amount of players using a chest
     */
    public static int getChestPlayersUsing(@Nonnull ChestTileEntity instance) {
        return getPrivateValue(ChestTileEntity.class, instance, "field_145987_o");
    }
    
    /**
     * Set the amount of players using a chest.
     */
    public static void setChestPlayersUsing(@Nonnull ChestTileEntity instance, int amount, boolean add) {
        if (add) amount += getChestPlayersUsing(instance);
        if (amount < 0) amount = 0;
        setPrivateValue(ChestTileEntity.class, instance, amount, "field_145987_o");
    }
    
    /**
     * Get the goals set of this mob
     */
    public static Set<PrioritizedGoal> getGoalsSet(@Nonnull GoalSelector instance) {
        return getPrivateValue(GoalSelector.class, instance, "field_220892_d");
    }
}
