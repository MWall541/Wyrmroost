package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class AddPassengerPacket
{
    public final int passengerID, vehicleID;

    AddPassengerPacket(Entity passenger, Entity vehicle)
    {
        this.passengerID = passenger.getId();
        this.vehicleID = vehicle.getId();
    }

    public AddPassengerPacket(PacketBuffer buf)
    {
        passengerID = buf.readInt();
        vehicleID = buf.readInt();
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeInt(passengerID);
        buf.writeInt(vehicleID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx)
    {
        Entity passenger = ClientEvents.getLevel().getEntity(passengerID);
        Entity vehicle = ClientEvents.getLevel().getEntity(vehicleID);
        if (passenger == null || vehicle == null || !passenger.startRiding(vehicle, true))
        {
            Wyrmroost.LOG.warn("Could not add passenger on client...");
            return false;
        }
        return true;
    }

    public static void send(Entity passenger, Entity vehicle)
    {
        Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> vehicle), new AddPassengerPacket(passenger, vehicle));
    }
}
