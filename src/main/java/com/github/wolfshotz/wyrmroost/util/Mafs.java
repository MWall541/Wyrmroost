package com.github.wolfshotz.wyrmroost.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
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
    private Mafs()
    {/* good try */}

    /**
     * Float Version of PI.
     * Why? so we don't have to cast the fucking official one 314159265358979323846 (heh) times
     */
    public static final float PI = (float) Math.PI;

    /**
     * Returns a new pseudo random double value constrained to the values of {@code (-1.0d)} and {@code (1.0d)}
     */
    public static double nextDouble(Random rand)
    {
        return 2 * rand.nextDouble() - 1;
    }

    /**
     * Returns a new pseudo random inteeger value constrained to the values of the negation of {@code bounds}
     * and postive {@code bounds}
     */
    public static int nextInt(Random rand, int bounds)
    {
        bounds = bounds * 2 - 1;
        return rand.nextInt(bounds) - (bounds / 2);
    }

    /**
     * A good way to get a position offset by the direction of a yaw angle.
     */
    public static Vector3d getYawVec(float yaw, double xOffset, double zOffset)
    {
        return new Vector3d(xOffset, 0, zOffset).yRot(-yaw * (PI / 180f));
    }

    /**
     * Get the angle between 2 sources
     * <p>
     * TODO: Adjust so that the angle is closest to 0 in the SOUTH direction!, currently it is only doing it for east!
     */
    public static double getAngle(double sourceX, double sourceZ, double targetX, double targetZ)
    {
        return MathHelper.atan2(targetZ - sourceZ, targetX - sourceX) * 180 / Math.PI + 180;
    }

    public static double getAngle(Entity source, Entity target)
    {
        return MathHelper.atan2(target.getZ() - source.getZ(), target.getX() - source.getX()) * (180 / Math.PI) + 180;
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

    @Nullable
    public static EntityRayTraceResult clipEntities(Entity shooter, double range, @Nullable Predicate<Entity> filter)
    {
        return clipEntities(shooter, range, 0, filter);
    }

    @Nullable
    public static EntityRayTraceResult clipEntities(Entity shooter, double range, double hitRadius, @Nullable Predicate<Entity> filter)
    {
        Vector3d eyes = shooter.getEyePosition(1f);
        Vector3d end = eyes.add(shooter.getLookAngle().multiply(range, range, range));

        Entity result = null;
        double distance = range * range;
        for (Entity entity : shooter.level.getEntities(shooter, shooter.getBoundingBox().inflate(range), filter))
        {
            Optional<Vector3d> opt = entity.getBoundingBox().inflate(hitRadius).clip(eyes, end);
            if (opt.isPresent())
            {
                double dist = eyes.distanceToSqr(opt.get());
                if (dist < distance)
                {
                    result = entity;
                    distance = dist;
                }
            }
        }

        return result == null? null : new EntityRayTraceResult(result);
    }
}
