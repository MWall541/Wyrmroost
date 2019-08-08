package WolfShotz.Wyrmroost.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class MathUtils
{
    /**
     * Attempt to rotate the first angle to become the second angle, but only allow overall direction change to at max be
     * third parameter
     */
    public static float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);

        if (f > maximumChange) f = maximumChange;
        if (f < -maximumChange) f = -maximumChange;

        float f1 = sourceAngle + f;

        if (f1 < 0.0F) f1 += 360.0F;
        else if (f1 > 360.0F) f1 -= 360.0F;

        return f1;
    }
    
    /**
     * Get the Altitude of an entity from the world surface
     */
    public static double getAltitude(Entity entity) { return entity.posY - entity.world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) entity.posX, (int) entity.posZ); }
    
    /**
     * Get the altitude of a world position from the world surface
     */
    public static double getAltitude(World world, int x, double y, int z) { return y - world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z); }
}
