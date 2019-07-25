package WolfShotz.Wyrmroost.util.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy
{
    public void init() { }

    public World getClientWorld() { throw new IllegalStateException("Do not call this on the server!"); }

    public PlayerEntity getPlayerEntity() { throw new IllegalStateException("Do not call this on the server!"); }
}
