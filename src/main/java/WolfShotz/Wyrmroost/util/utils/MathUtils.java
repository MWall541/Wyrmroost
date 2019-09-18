package WolfShotz.Wyrmroost.util.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.List;
import java.util.Optional;

/**
 * Math utility class to make my life like way easier.
 *
 * Half of this shit is just me throwing numbers in and hoping it works,
 * seems to be going well so far!
 */
public class MathUtils
{
    public static final float PI = (float) Math.PI;
    
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
    
    public static Vec3d rotateYaw(float amount, double xOffset, double zOffset) {
        return new Vec3d(xOffset, 0, zOffset).rotateYaw(-amount * (PI / 180f));
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
    public static double getPlaneDistSq(double sourceX, double targetX, double sourceZ, double targetZ) {
        double x = targetX - sourceX;
        double z = targetZ - sourceZ;
        return x * x + z * z;
    }
    
    /**
     * Calculate the euclidean plane distance of two entities.
     * Double
     */
    public static double getPlaneDistSq(Entity source, Entity target) {
        double x = target.posX - source.posX;
        double z = target.posZ - source.posZ;
        return x * x + z * z;
    }
    
    /**
     * Calculate the euclidean plane distance between two points.
     * Float
     */
    public static float getPlaneDistSq(float sourceX, float targetX, float sourceZ, float targetZ) {
        float x = targetX - sourceX;
        float z = targetZ - sourceZ;
        return x * x + z * z;
    }
    
    /**
     * Calculate euclidean space distance
     * Double
     */
    public static double getSpaceDistSq(double sourceX, double targetX, double sourceY, double targetY, double sourceZ, double targetZ) {
        double x = targetX - sourceX;
        double y = targetY - sourceY;
        double z = targetZ - sourceZ;
        return x * x + y * y + z * z;
    }
    
    /**
     * Calculate euclidean space distance
     * Float
     */
    public static float getSpaceDistSq(float sourceX, float targetX, float sourceY, float targetY, float sourceZ, float targetZ) {
        float x = targetX - sourceX;
        float y = targetY - sourceY;
        float z = targetZ - sourceZ;
        return x * x + y * y + z * z;
    }
    
    /**
     * Angle measurement converted to radians as a float value <P>
     * <code>angle / 180.0 * PI</code>
     */
    public static float toRadians(float angle) { return (float) Math.toRadians(angle); }
    
    /**
     * Angle measurement converted to degrees as a float value <P>
     * <code>angle * 180.0 / PI</code>
     */
    public static float toDegrees(float angle) { return (float) Math.toDegrees(angle); }
    
    /**
     * Get the angle between 2 sources
     */
    public static double getAngle(double x1, double x2, double z1, double z2) {
        return Math.atan2(z2 - z1, x2 - x1) * (180 / Math.PI) + 90;
    }
    
    /**
     * Created by TGG on 8/07/2015. Modified by WolfShotz on 9/16/2019 <P>
     * Performs a ray trace of the player's line of sight to see what the player is looking at.
     * Similar to the vanilla getMouseOver, which is client side only.
     *
     * Find what the player is looking at (block or entity), up to a maximum range
     * based on code from EntityRenderer.getMouseOver. <P>
     * <STRIKE>Will not target entities which are tamed by the player</STRIKE> <P>
     * 9/16/2019 - WolfShotz: <P>
     * - Small Cleanup <P>
     * - Includes configurable tamed entity targetting <P>
     * - Changed method name from <code>getMouseOver</code> to <code>rayTrace</code>
     *
     * @return the block or entity that the player is looking at / targeting with their cursor.  null if no collision
     */
    public static RayTraceResult rayTrace(World world, PlayerEntity serverPlayer, double range, boolean targetTamed) {
        final RayTraceContext.FluidMode FLUID_MODE = RayTraceContext.FluidMode.NONE;
        final RayTraceContext.BlockMode BLOCK_MODE = RayTraceContext.BlockMode.COLLIDER;
        final Vec3d EYES_POSITION = serverPlayer.getEyePosition(1f);
        final Vec3d LOOK_DIRECTION = serverPlayer.getLook(1f);
        Vec3d endOfLook = EYES_POSITION.add(LOOK_DIRECTION.x * range, LOOK_DIRECTION.y * range, LOOK_DIRECTION.z * range);
        RayTraceResult targetedBlock = world.rayTraceBlocks(new RayTraceContext(EYES_POSITION, endOfLook, BLOCK_MODE, FLUID_MODE, serverPlayer));
        double collisionDistanceSQ = range * range;
        
        if (targetedBlock.getType() == RayTraceResult.Type.BLOCK) {
            collisionDistanceSQ = targetedBlock.getHitVec().squareDistanceTo(EYES_POSITION);
            endOfLook = targetedBlock.getHitVec();
        }
        
        Vec3d endOfLookDelta = endOfLook.subtract(EYES_POSITION);
        AxisAlignedBB searchBox = serverPlayer.getBoundingBox().expand(endOfLookDelta.x, endOfLookDelta.y, endOfLookDelta.z).grow(1f); //add
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(serverPlayer, searchBox);
        Entity closestEntityHit = null;
        double closestEntityDistanceSQ = Double.MAX_VALUE;
        
        for (Entity entity : nearbyEntities) {
            if (!entity.canBeCollidedWith() || entity == serverPlayer.getRidingEntity())
                continue;
            if (!targetTamed && entity instanceof TameableEntity) {
                TameableEntity tamedEntity = (TameableEntity) entity;
                if (tamedEntity.isOwner(serverPlayer))
                    continue;
            }
            
            float collisionBorderSize = entity.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(collisionBorderSize);
            Optional<Vec3d> movingobjectposition = axisalignedbb.rayTrace(EYES_POSITION, endOfLook);
            
            if (axisalignedbb.contains(endOfLook)) {
                double distanceSQ = (!movingobjectposition.isPresent()) ? EYES_POSITION.squareDistanceTo(endOfLook) : EYES_POSITION.squareDistanceTo(movingobjectposition.get());
                if (distanceSQ <= closestEntityDistanceSQ) {
                    closestEntityDistanceSQ = distanceSQ;
                    closestEntityHit = entity;
                }
            } else if (movingobjectposition.isPresent()) {
                double distanceSQ = EYES_POSITION.squareDistanceTo(movingobjectposition.get());
                if (distanceSQ <= closestEntityDistanceSQ) {
                    closestEntityDistanceSQ = distanceSQ;
                    closestEntityHit = entity;
                }
            }
        }
        
        if (closestEntityDistanceSQ <= collisionDistanceSQ && closestEntityHit != null)
            return new EntityRayTraceResult(closestEntityHit, closestEntityHit.getPositionVec());
        
        return targetedBlock;
    }
}
