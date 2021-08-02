package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindHandler;
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

    private final byte behaviorId;
    private final boolean sendsPacket;
    private boolean prevIsPressed;

    public WRKeybind(String name, int keyCode, byte behaviorId, boolean sendsPacket)
    {
        super(name, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrCreate(keyCode), CATEGORY);
        this.behaviorId = behaviorId;
        this.sendsPacket = sendsPacket;
    }

    public WRKeybind(String name, int keyCode, byte behaviorId)
    {
        this(name, keyCode, behaviorId, true);
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
            KeybindHandler handler = new KeybindHandler(behaviorId, mods, pressed);
            handler.process(ClientEvents.getPlayer());
            if (sendsPacket) Wyrmroost.NETWORK.sendToServer(handler);
        }
        prevIsPressed = pressed;
    }

    public static void registerKeys()
    {
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey1", GLFW.GLFW_KEY_V, KeybindHandler.MOUNT_KEY));
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey2", GLFW.GLFW_KEY_G, KeybindHandler.ALT_MOUNT_KEY));
        ClientRegistry.registerKeyBinding(new WRKeybind("key.switch_flight", GLFW.GLFW_KEY_PERIOD, KeybindHandler.SWITCH_FLIGHT, false));
        ClientRegistry.registerKeyBinding(FLIGHT_DESCENT);
    }
}
