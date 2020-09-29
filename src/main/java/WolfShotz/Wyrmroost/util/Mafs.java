package WolfShotz.Wyrmroost.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Maf utility class to make my life like way easier.
 * <p>
 * Half of this shit is just me throwing numbers in and hoping it works,
 * seems to be going well so far!
 */
public final class Mafs
{
    private Mafs() {/* good try */}

    /**
     * Float Version of PI.
     * Why? so we don't have to cast the fucking official one 314159265358979323846 (heh) times
     */
    public static final float PI = (float) Math.PI;

    /**
     * Returns a new pseudo random double value constrained to the values of {@code (-1.0d)} and {@code (1.0d)}
     */
    public static double nextDouble(Random rand) { return 2 * rand.nextDouble() - 1; }

    /**
     * A good way to get a position offset by the direction of a yaw angle.
     */
    public static Vec3d getYawVec(float yaw, double xOffset, double zOffset)
    {
        return new Vec3d(xOffset, 0, zOffset).rotateYaw(-yaw * (PI / 180f));
    }

    /**
     * Get the angle between 2 sources
     */
    public static double getAngle(double sourceX, double sourceZ, double targetX, double targetZ)
    {
        return MathHelper.atan2(targetZ - sourceZ, targetX - sourceX) * 180 / Math.PI + 180;
    }

    public static double getAngle(Entity source, Entity target)
    {
        return MathHelper.atan2(target.getPosZ() - source.getPosZ(), target.getPosX() - source.getPosX()) * 180 / Math.PI + 180;
    }

    /**
     * Clamped (0-1) Linear Interpolation (Float version)
     */
    public static float linTerp(float a, float b, float x)
    {
        if (x <= 0) return a;
        if (x >= 1) return b;
        return a + x * (b - a);
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
     * @deprecated use {@link net.minecraft.entity.projectile.ProjectileHelper}
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
        List<Entity> nearbyEntities = world.getEntitiesInAABBexcluding(player, searchBox, e -> EntityPredicates.NOT_SPECTATING.test(e) && !e.equals(player.getRidingEntity()));
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

    @Nullable
    public static EntityRayTraceResult rayTraceEntities(Entity shooter, double range, @Nullable Predicate<Entity> filter)
    {
        Vec3d eyes = shooter.getEyePosition(1f);
        Vec3d end = eyes.add(shooter.getLookVec().mul(range, range, range));
        return ProjectileHelper.rayTraceEntities(shooter.world,
                shooter,
                eyes,
                end,
                shooter.getBoundingBox().grow(range),
                filter,
                range * range);
    }
}
