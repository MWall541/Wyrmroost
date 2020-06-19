package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * @see org.lwjgl.glfw.GLFW
 */
public class WRKeybind extends KeyBinding
{
    public static final List<KeyBinding> KEYS = Lists.newArrayList(); // used to register them later, could be used for something else idfk

    public static WRKeybind MOUNT_ATTACK = new WRKeybind("key.mountAttack", GLFW.GLFW_KEY_V, KeybindPacket.MOUNT_ATTACK);
    public static WRKeybind MOUNT_SPECIAL = new WRKeybind("key.mountSpecial", GLFW.GLFW_KEY_G, KeybindPacket.MOUNT_SPECIAL, true);

    private final boolean hold;
    private final int id;
    private boolean prevIsPressed;

    public WRKeybind(String name, int keyCode, int packetId)
    {
        this(name, keyCode, packetId, false);
    }

    public WRKeybind(String name, int keyCode, int packetId, boolean holdable)
    {
        super(name, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM.getOrMakeInput(keyCode), "keyCategory.wyrmroost");
        this.hold = holdable;
        this.id = packetId;
        KEYS.add(this);
    }

    @Override
    public void setPressed(boolean pressed)
    {
        super.setPressed(pressed);

        if ((hold || isKeyDown()) && Minecraft.getInstance().player != null && prevIsPressed != pressed)
        {
            int modifiers = 0;
            if (Screen.hasAltDown()) modifiers |= GLFW.GLFW_MOD_ALT;
            if (Screen.hasControlDown()) modifiers |= GLFW.GLFW_MOD_CONTROL;
            if (Screen.hasShiftDown()) modifiers |= GLFW.GLFW_MOD_SHIFT;
            Wyrmroost.NETWORK.sendToServer(new KeybindPacket(id, modifiers));
            prevIsPressed = pressed;
        }
    }

    public static void registerKeys() { KEYS.forEach(ClientRegistry::registerKeyBinding); }
}
