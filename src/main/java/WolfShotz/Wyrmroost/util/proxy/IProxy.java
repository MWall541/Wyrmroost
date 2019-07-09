package WolfShotz.Wyrmroost.util.proxy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy
{
    void init();

    World getClientWorld();

    PlayerEntity getPlayerEntity();

    void openScreen(Screen gui);
}
