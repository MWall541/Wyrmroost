//package WolfShotz.Wyrmroost.entities.multipart;
//
//import net.minecraft.entity.LivingEntity;
//
//import java.util.Arrays;
//import java.util.Objects;
//import java.util.stream.Stream;
//
//public interface IMultiPartEntity
//{
//    MultiPartEntity[] getParts();
//
//    /**
//     * MultiPartEntity Streamer used to iterate the parts
//     */
//    default Stream<MultiPartEntity> iterateParts()
//    {
//        return Arrays.stream(getParts()).filter(Objects::nonNull);
//    }
//
//    /**
//     * Method used to update the parts per tick
//     */
//    default void tickParts()
//    {
//        iterateParts().forEach(MultiPartEntity::tick);
//    }
//
//    default LivingEntity getHost() { return (LivingEntity) this; }
//}
