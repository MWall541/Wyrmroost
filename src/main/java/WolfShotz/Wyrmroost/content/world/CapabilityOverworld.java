package WolfShotz.Wyrmroost.content.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityOverworld
{
    @CapabilityInject(CapabilityOverworld.class)
    public static Capability<CapabilityOverworld> OW_CAP;
    
    private boolean isSpawnsTriggered;
    
    public boolean isSpawnsTriggered() { return isSpawnsTriggered; }
    
    public void setSpawnsTriggered(boolean flag) { this.isSpawnsTriggered = flag; }
    
    public static class PropertiesDispatcher implements ICapabilitySerializable<CompoundNBT>
    {
        private CapabilityOverworld worldData = new CapabilityOverworld();
    
        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("triggerowspawns", worldData.isSpawnsTriggered());
            return nbt;
        }
    
        @Override
        public void deserializeNBT(CompoundNBT nbt) { worldData.setSpawnsTriggered(nbt.getBoolean("triggerowspawns")); }
    
        /**
         * Retrieves the Optional handler for the capability requested on the specific side.
         * The return value <strong>CAN</strong> be the same for multiple faces.
         * Modders are encouraged to cache this value, using the listener capabilities of the Optional to
         * be notified if the requested capability get lost.
         *
         * @param cap
         * @param side
         * @return The requested an optional holding the requested capability.
         */
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == OW_CAP? LazyOptional.of(() -> (T) worldData) : LazyOptional.empty();
        }
    }
}