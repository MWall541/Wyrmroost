package WolfShotz.Wyrmroost.util.entityutils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class PlayerMount
{
    /**
     * Interface to define entities that have the capabilities of mounting a players head
     */
    public interface IHeadMount
    {
    }
    
    /**
     * Interface to define entities that have the capabilities of mounting a players shoulders
     */
    public interface IShoulderMount
    {
        /**
         * Checks if one entity is riding this players shoulder, if it is, check if its index is greater than the other
         * Should be used to differentiate which entity should be riding each shoulder
         */
        default boolean checkShoulderOccupants(PlayerEntity mount)
        {
            return PlayerMount.checkShoulderOccupants(mount, (LivingEntity) this);
        }
    }
    
    public interface IMountable extends IHeadMount, IShoulderMount
    {
    }
    
    /**
     * Checks to see if the player head is occupied by another {@link IHeadMount} entity
     */
    public static boolean hasHeadOccupant(PlayerEntity entity)
    {
        return entity.getPassengers().stream().anyMatch(IHeadMount.class::isInstance);
    }
    
    /**
     * Checks if one {@link IShoulderMount} entity is riding this players shoulder, if it is, check if its index is greater than the other
     * Should be used to differentiate which entity should be riding each shoulder
     */
    public static boolean checkShoulderOccupants(PlayerEntity mount, LivingEntity rider)
    {
        Optional<Entity> other = mount.getPassengers().stream().filter(e -> e != rider && e instanceof IShoulderMount).findFirst();
        return other.isPresent() && mount.getPassengers().indexOf(rider) > mount.getPassengers().indexOf(other.get());
    }
    
    /**
     * Get the amount of entities riding this player's shoulders
     */
    public static long getShoulderEntityCount(PlayerEntity entity)
    {
        return entity.getPassengers().stream().filter(IShoulderMount.class::isInstance).count();
    }
}