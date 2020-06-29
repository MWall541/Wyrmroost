package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import net.minecraft.client.Minecraft;
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
    private final int id;
    private boolean prevIsPressed;

    public WRKeybind(String name, int keyCode, int packetId)
    {
        super(name, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrMakeInput(keyCode), "keyCategory.wyrmroost");
        this.id = packetId;
    }

    @Override
    public void setPressed(boolean pressed)
    {
        super.setPressed(pressed);

        if (Minecraft.getInstance().player != null && prevIsPressed != pressed)
        {
            int context = pressed? 1 : 0;
            if (Screen.hasAltDown()) context |= GLFW.GLFW_MOD_ALT;
            if (Screen.hasControlDown()) context |= GLFW.GLFW_MOD_CONTROL;
            if (Screen.hasShiftDown()) context |= GLFW.GLFW_MOD_SHIFT;
            Wyrmroost.NETWORK.sendToServer(new KeybindPacket(id, context));
        }
        prevIsPressed = pressed;
    }

    public static void registerKeys()
    {
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey1", GLFW.GLFW_KEY_V, KeybindPacket.MOUNT_KEY1));
        ClientRegistry.registerKeyBinding(new WRKeybind("key.mountKey2", GLFW.GLFW_KEY_G, KeybindPacket.MOUNT_KEY2));
    }
}
