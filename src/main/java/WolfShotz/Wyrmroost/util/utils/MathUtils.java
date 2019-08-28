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
     * Subtract 1 from plane pose's as a workaround for a vanilla bug using the wrong pos when plane pose's are negative...
     */
    public static double getAltitude(Entity entity) {
        return entity.posY - entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, (int) entity.posX - (entity.posX < 0? 1 : 0), (int) entity.posZ - (entity.posZ < 0? 1 : 0));
    }
    
    /**
     * Get the altitude of a world position from the world surface
     * Subtract 1 from plane pose's as a workaround for a vanilla bug using the wrong pos when plane pose's are negative...
     */
    public static double getAltitude(World world, int x, double y, int z) {
        return y - world.getHeight(Heightmap.Type.WORLD_SURFACE, x - (x < 0? 1 : 0), z - (z < 0? 1 : 0));
    }
    
    /**
     * Calculate the euclidean plane distance of two points.
     * Double
     */
    public static double calcDistance2d(double z, double z2, double x, double x2) {
        return Math.sqrt((z - z2) * (z - z2) + (x - x2) * (x - x2));
    }
    
    /**
     * Calculate the euclidean plane distance between two points.
     * Float
     */
    public static float calcDistance2d(float z, float z2, float x, float x2) {
        return (float) Math.sqrt((z - z2) * (z - z2) + (x - x2) * (x - x2));
    }
    
    /**
     * Calculate euclidean space distance
     * Double
     */
    public static double calcDistance3d(double x, double y, double z) { return x * x + y * y + z * z; }
    
    /**
     * Calculate euclidean space distance
     * Float
     */
    public static float calcDistance3d(float x, float y, float z) { return x * x + y * y + z * z; }
}
