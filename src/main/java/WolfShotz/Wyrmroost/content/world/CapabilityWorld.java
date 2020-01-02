package WolfShotz.Wyrmroost.content.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityWorld
{
    @CapabilityInject(CapabilityWorld.class)
    public static final Capability<CapabilityWorld> OW_CAP = null;
    
    private boolean portalTriggered;
    public boolean isPortalTriggered() { return portalTriggered; }
    public void setPortalTriggered(boolean flag) { this.portalTriggered = flag; }
    
    public static class PropertiesDispatcher implements ICapabilitySerializable<CompoundNBT>
    {
        private final CapabilityWorld worldData = new CapabilityWorld();
        
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
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) { return OW_CAP.orEmpty(cap, LazyOptional.of(() -> worldData)); }
        
        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("triggerowspawns", worldData.isPortalTriggered());
            return nbt;
        }
    
        @Override
        public void deserializeNBT(CompoundNBT nbt) { worldData.setPortalTriggered(nbt.getBoolean("triggerowspawns")); }
    }
}