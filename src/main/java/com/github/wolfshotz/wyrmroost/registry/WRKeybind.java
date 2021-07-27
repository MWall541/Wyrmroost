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
    private static final String CATEGORY = "keyCategory.wyrmroost";

    public static final KeyBinding FLIGHT_DESCENT = new KeyBinding("key.flight_descent", GLFW.GLFW_KEY_LEFT_CONTROL, CATEGORY);

    private final byte id;
    private final boolean sendsPacket;
    private boolean prevIsPressed;

    public WRKeybind(String name, boolean sendsPacket, int keyCode, byte packetKeyID)
    {
        super(name, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrCreate(keyCode), CATEGORY);
        this.sendsPacket = sendsPacket;
        this.id = packetKeyID;
    }

    public WRKeybind(String name, int keyCode, byte packetKeyID)
    {
        this(name, true, keyCode, packetKeyID);
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
            if (sendsPacket) Wyrmroost.NETWORK.sendToServer(packet);
        }
        prevIsPressed = pressed;
    }

    public static void registerKeys()
    {
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey1", GLFW.GLFW_KEY_V, KeybindPacket.MOUNT_KEY1));
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey2", GLFW.GLFW_KEY_G, KeybindPacket.MOUNT_KEY2));
        ClientRegistry.registerKeyBinding(FLIGHT_DESCENT);
        ClientRegistry.registerKeyBinding(new WRKeybind("key.switch_flight", false, GLFW.GLFW_KEY_PERIOD, KeybindPacket.SWITCH_FLIGHT));
    }
}
