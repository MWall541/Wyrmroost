package WolfShotz.Wyrmroost.entities.multipart;

import net.minecraft.entity.LivingEntity;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public interface IMultiPartEntity
{
    MultiPartEntity[] getParts();
    
    /**
     * MultiPartEntity Streamer used to iterate the parts
     */
    default Stream<MultiPartEntity> iterateParts()
    {
        return Arrays.stream(getParts()).filter(Objects::nonNull);
    }
    
    /**
     * Method used to update the parts per tick
     */
    default void tickParts()
    {
        iterateParts().forEach(MultiPartEntity::tick);
    }
    
    /**
     * Create an additional "hitbox" on the passed host. SHOULD BE CREATED ON SERVER!
     *
     * @param host     entity were adding this part to
     * @param radius   how far the part is away from the host's center
     * @param angleYaw angle at which the box is at
     * @param offsetY  how high the part is
     * @param sizeX    the width of the part
     * @param sizeY    the height of the part
     */
    default MultiPartEntity createPart(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY)
    {
        return createPart(host, radius, angleYaw, offsetY, sizeX, sizeY, 1f);
    }
    
    /**
     * Create an additional "hitbox" on the passed host. SHOULD BE CREATED ON SERVER!
     *
     * @param host             entity were adding this part to
     * @param radius           how far the part is away from the host's center
     * @param angleYaw         angle at which the box is at
     * @param offsetY          how high the part is
     * @param sizeX            the width of the part
     * @param sizeY            the height of the part
     * @param damageMultiplier the amount  of damage multiplied applied to the target when this part is damaged
     */
    default MultiPartEntity createPart(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier)
    {
        return new MultiPartEntity(host, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }
    
    default LivingEntity getHost() { return (LivingEntity) this; }
}
