package WolfShotz.Wyrmroost.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldCapability
{
    @CapabilityInject(WorldCapability.class)
    public static final Capability<WorldCapability> OW_CAP = null;

    public boolean portalTriggered;

    public static boolean isPortalTriggered(World world)
    {
        return world.getCapability(OW_CAP).map(WorldCapability::isPortalTriggered).orElse(false);
    }

    public static void setPortalTriggered(World world, boolean trigger)
    {
        world.getCapability(OW_CAP).ifPresent(cap -> cap.setPortalTriggered(trigger));
    }

    public boolean isPortalTriggered()
    {
        return portalTriggered;
    }

    public void setPortalTriggered(boolean trigger)
    {
        portalTriggered = trigger;
    }

    public static class PropertiesDispatcher implements ICapabilitySerializable<CompoundNBT>
    {
        private final WorldCapability instance = new WorldCapability();

        /**
         * Retrieves the Optional handler for the capability requested on the specific side.
         * The return value <strong>CAN</strong> be the same for multiple faces.
         * Modders are encouraged to cache this value, using the listener capabilities of the Optional to
         * be notified if the requested capability get lost.
         *
         * @return The requested an optional holding the requested capability.
         */
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return cap == OW_CAP ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
        }
        
        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("portal_triggered", instance.isPortalTriggered());
            return nbt;
        }
        
        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            instance.setPortalTriggered(nbt.getBoolean("portal_triggered"));
        }
    }
}