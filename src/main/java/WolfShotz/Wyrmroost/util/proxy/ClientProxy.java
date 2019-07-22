package WolfShotz.Wyrmroost.util.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy implements IProxy
{

    @Override
    public void init() { }

    @Override
    public World getClientWorld() { return Minecraft.getInstance().world; }

    @Override
    public PlayerEntity getPlayerEntity() { return Minecraft.getInstance().player; }
}
