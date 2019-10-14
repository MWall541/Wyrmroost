package WolfShotz.Wyrmroost.util.entityhelpers.multipart;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.stream.Stream;

public interface IMultiPartEntity
{
    MultiPartEntity[] getParts();
    
    default Stream<MultiPartEntity> iterateParts() { return Arrays.stream(getParts()); }
    
    default void tickParts() { iterateParts().forEach(MultiPartEntity::tick); }
    
    default MultiPartEntity createPart(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY) {
        return createPart(host, radius, angleYaw, offsetY, sizeX, sizeY, 1f);
    }
    
    default MultiPartEntity createPart(LivingEntity host, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        return new MultiPartEntity(host, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }
    
    default void addPartsToWorld(World world) { iterateParts().forEach(world::addEntity); }
    
    default LivingEntity getHost() { return (LivingEntity) this; }
}
