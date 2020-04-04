package WolfShotz.Wyrmroost.registry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class WRKeyBinds
{
    private static final String CATEGORY = "keyCategory.wyrmroost";

    public static KeyBinding genericAttack;
    public static KeyBinding specialAttack;

    public static void registerKeys()
    {
        genericAttack = registerKey("key.genericAttack", 86, CATEGORY);
        specialAttack = registerKey("key.specialAttack", 71, CATEGORY);
    }
    
    public static KeyBinding registerKey(String description, int keyCode, String category)
    {
        KeyBinding key = new KeyBinding(description, keyCode, category);
        ClientRegistry.registerKeyBinding(key);
        
        return key;
    }
}
