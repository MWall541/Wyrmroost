package WolfShotz.Wyrmroost.util;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReflectionUtils
{
    /**
     * @return the protected boolean value of "isJumping" of LivingEntity Class
     */
    public static boolean isEntityJumping(LivingEntity entity) {
        return ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, entity, "field_70703_bu");
    }
}
