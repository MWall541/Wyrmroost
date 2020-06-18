package WolfShotz.Wyrmroost.registry;

import com.google.common.collect.Lists;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;
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

    public static WRKeybind MOUNT_ATTACK = new Builder("key.mountAttack", GLFW.GLFW_KEY_V).build();
    public static WRKeybind MOUNT_SPECIAL = new Builder("key.mountSpecial", GLFW.GLFW_KEY_G).setHoldToToggle().build();

    private final boolean hold;
    public boolean prevIsDown;

    public WRKeybind(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, boolean hold, InputMappings.Input keyCode, String category)
    {
        super(description, keyConflictContext, keyModifier, keyCode, category);
        this.hold = hold;
        KEYS.add(this);
    }

    @Override
    public boolean isKeyDown() { return prevIsDown = super.isKeyDown(); }

    public boolean isActivated()
    {
        boolean prev = prevIsDown;
        boolean down = isKeyDown();
        if (!hold && !down) return false;
        return prev != down;
    }

    public static void registerKeys() { KEYS.forEach(ClientRegistry::registerKeyBinding); }

    public static class Builder
    {
        private final int keyCode;
        private final String name;
        private InputMappings.Type type = InputMappings.Type.KEYSYM;
        private String category = "keyCategory.wyrmroost";
        private IKeyConflictContext conflictContext = KeyConflictContext.IN_GAME;
        private KeyModifier modifier = KeyModifier.NONE;
        private boolean hold = false;

        public Builder(String name, int keyCode)
        {
            this.keyCode = keyCode;
            this.name = name;
        }

        public Builder setCategory(String category)
        {
            this.category = category;
            return this;
        }

        public Builder setInputType(InputMappings.Type type)
        {
            this.type = type;
            return this;
        }

        public Builder setConflictContext(IKeyConflictContext context)
        {
            this.conflictContext = context;
            return this;
        }

        public Builder setModifier(KeyModifier modifier)
        {
            this.modifier = modifier;
            return this;
        }

        public Builder setHoldToToggle()
        {
            this.hold = true;
            return this;
        }

        public WRKeybind build()
        {
            return new WRKeybind(name, conflictContext, modifier, hold, type.getOrMakeInput(keyCode), category);
        }
    }
}
