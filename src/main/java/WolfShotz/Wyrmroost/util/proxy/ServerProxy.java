package WolfShotz.Wyrmroost.util.proxy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy
{
    @Override
    public void init() { }

    @Override
    public World getClientWorld() { throw new IllegalStateException("Do not call this on the server!"); }

    @Override
    public PlayerEntity getPlayerEntity() { throw new IllegalStateException("Do not call this on the server!"); }

    @Override
    public void openScreen(Screen gui) { throw new IllegalStateException("Only Clients can open GUI's!"); }

}
