package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * @see org.lwjgl.glfw.GLFW
 */
public class WRKeybind extends KeyBinding
{
    private final byte id;
    private boolean prevIsPressed;

    public WRKeybind(String name, int keyCode, byte packetKeyID)
    {
        super(name, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrCreate(keyCode), "keyCategory.wyrmroost");
        this.id = packetKeyID;
    }

    @Override
    public void setDown(boolean pressed)
    {
        super.setDown(pressed);

        if (ClientEvents.getPlayer() != null && prevIsPressed != pressed)
        {
            byte mods = 0;
            if (Screen.hasAltDown()) mods |= GLFW.GLFW_MOD_ALT;
            if (Screen.hasControlDown()) mods |= GLFW.GLFW_MOD_CONTROL;
            if (Screen.hasShiftDown()) mods |= GLFW.GLFW_MOD_SHIFT;
            KeybindPacket packet = new KeybindPacket(id, mods, pressed);
            packet.process(ClientEvents.getPlayer());
            Wyrmroost.NETWORK.sendToServer(packet);
        }
        prevIsPressed = pressed;
    }

    public static void registerKeys()
    {
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey1", GLFW.GLFW_KEY_V, KeybindPacket.MOUNT_KEY1));
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey2", GLFW.GLFW_KEY_G, KeybindPacket.MOUNT_KEY2));
    }
}
