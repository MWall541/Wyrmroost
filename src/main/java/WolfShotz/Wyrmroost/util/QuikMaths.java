package WolfShotz.Wyrmroost.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Math utility class to make my life like way easier.
 * <p>
 * Half of this shit is just me throwing numbers in and hoping it works,
 * seems to be going well so far!
 */
public class QuikMaths
{
    /**
     * Float Version of PI.
     * Why? so we don't have to cast the fucking official one
     */
    public static final float PI = (float) Math.PI;

    /**
     * Returns a new pseudo random double value constrained to the values of {@code (-1.0d)} and {@code (1.0d)}
     */
    public static double nextPseudoDouble(Random rand)
    {
        return 2 * rand.nextDouble() - 1;
    }

    /**
     * A good way to get a position offset by the direction of a yaw angle.
     */
    public static Vec3d calculateYawAngle(float amount, double xOffset, double zOffset)
    {
        return new Vec3d(xOffset, 0, zOffset).rotateYaw(-amount * (PI / 180f));
    }

    /**
     * Calculate euclidean space distance
     * Double
     */
    public static double getSpaceDistSq(double sourceX, double targetX, double sourceY, double targetY, double sourceZ, double targetZ)
    {
        double x = targetX - sourceX;
        double y = targetY - sourceY;
        double z = targetZ - sourceZ;
        return x * x + y * y + z * z;
    }

    /**
     * Calculate euclidean space distance
     * Float
     */
    public static float getSpaceDistSq(float sourceX, float targetX, float sourceY, float targetY, float sourceZ, float targetZ)
    {
        float x = targetX - sourceX;
        float y = targetY - sourceY;
        float z = targetZ - sourceZ;
        return x * x + y * y + z * z;
    }

    /**
     * Get the angle between 2 sources
     */
    public static double getAngle(double x1, double x2, double z1, double z2)
    {
        return Math.atan2(z2 - z1, x2 - x1) * (180 / Math.PI) + 90;
    }

    /**
     * Created by TGG on 8/07/2015. Modified by WolfShotz on 9/16/2019 <P>
     * Performs a ray trace of the player's line of sight to see what the player is looking at.
     * Similar to the vanilla getMouseOver, which is client side only.
     * <p>
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
    public static RayTraceResult rayTrace(World world, PlayerEntity player, double range, boolean targetTamed)
    {
        final RayTraceContext.FluidMode FLUID_MODE = RayTraceContext.FluidMode.NONE;
        final RayTraceContext.BlockMode BLOCK_MODE = RayTraceContext.BlockMode.COLLIDER;
        final Vec3d EYES_POSITION = player.getEyePosition(1f);
        final Vec3d LOOK_DIRECTION = player.getLook(1f);
        Vec3d endOfLook = EYES_POSITION.add(LOOK_DIRECTION.x * range, LOOK_DIRECTION.y * range, LOOK_DIRECTION.z * range);
        RayTraceResult targetedBlock = world.rayTraceBlocks(new RayTraceContext(EYES_POSITION, endOfLook, BLOCK_MODE, FLUID_MODE, player));
        double collisionDistanceSQ = range * range;

        if (targetedBlock.getType() == RayTraceResult.Type.BLOCK)
        {
            collisionDistanceSQ = targetedBlock.getHitVec().squareDistanceTo(EYES_POSITION);
            endOfLook = targetedBlock.getHitVec();
        }

        Vec3d endOfLookDelta = endOfLook.subtract(EYES_POSITION);
        AxisAlignedBB searchBox = player.getBoundingBox().expand(endOfLookDelta.x, endOfLookDelta.y, endOfLookDelta.z).grow(1f); //add
        List<Entity> nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(player, searchBox);
        Entity closestEntityHit = null;
        double closestEntityDistanceSQ = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities)
        {
            if (!entity.canBeCollidedWith() || entity == player.getRidingEntity())
                continue;
            if (!targetTamed && entity instanceof TameableEntity)
            {
                TameableEntity tamedEntity = (TameableEntity) entity;
                if (tamedEntity.isOwner(player))
                    continue;
            }

            float collisionBorderSize = entity.getCollisionBorderSize();
            AxisAlignedBB axisAlignedBB = entity.getBoundingBox().grow(collisionBorderSize);
            Optional<Vec3d> movingObjectPosition = axisAlignedBB.rayTrace(EYES_POSITION, endOfLook);

            if (axisAlignedBB.contains(endOfLook))
            {
                double distanceSQ = (!movingObjectPosition.isPresent()) ? EYES_POSITION.squareDistanceTo(endOfLook) : EYES_POSITION.squareDistanceTo(movingObjectPosition.get());
                if (distanceSQ <= closestEntityDistanceSQ)
                {
                    closestEntityDistanceSQ = distanceSQ;
                    closestEntityHit = entity;
                }
            }
            else if (movingObjectPosition.isPresent())
            {
                double distanceSQ = EYES_POSITION.squareDistanceTo(movingObjectPosition.get());
                if (distanceSQ <= closestEntityDistanceSQ)
                {
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
