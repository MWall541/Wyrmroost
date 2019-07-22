package WolfShotz.Wyrmroost.util.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy
{
    void init();

    World getClientWorld();

    PlayerEntity getPlayerEntity();
}
