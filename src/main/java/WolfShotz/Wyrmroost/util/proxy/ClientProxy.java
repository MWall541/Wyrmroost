package WolfShotz.Wyrmroost.util.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy
{

    @Override
    public void init() { }

    @Override
    public World getClientWorld() { return Minecraft.getInstance().world; }

    @Override
    public PlayerEntity getPlayerEntity() { return Minecraft.getInstance().player; }

    @Override
    public void openScreen(Screen gui) { Minecraft.getInstance().displayGuiScreen(gui); }
}
