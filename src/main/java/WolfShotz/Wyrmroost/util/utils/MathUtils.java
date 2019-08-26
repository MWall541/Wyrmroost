package WolfShotz.Wyrmroost.util.utils;

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
     * Subtract 1 from posZ as a workaround for a vanilla bug using the wrong pos when posZ is negative...
     */
    public static double getAltitude(Entity entity) { return entity.posY - entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) entity.posX, (int) entity.posZ - (entity.posZ < 0? 1 : 0)); }
    
    /**
     * Get the altitude of a world position from the world surface
     * Subtract 1 from posZ as a workaround for a vanilla bug using the wrong pos when posZ is negative...
     */
    public static double getAltitude(World world, int x, double y, int z) { return y - world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z - (z < 0? 1 : 0)); }
    
    public static double calcDistance(double z, double z2, double x, double x2) {
        return Math.sqrt((z - z2) * (z - z2) + (x - x2) * (x - x2));
    }
    
    public static float calcDistance(float z, float z2, float x, float x2) {
        return (float) Math.sqrt((z - z2) * (z - z2) + (x - x2) * (x - x2));
    
    }
}
