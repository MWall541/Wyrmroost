package WolfShotz.Wyrmroost.event;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class SetupKeyBinds
{
    private static final String category = "wyrmroost.keyCategory";
    
    public static KeyBinding genericAttack;
    public static KeyBinding specialAttack;
    
    public static void registerKeys() {
        genericAttack = new KeyBinding("key.genericAttack", 86, category);
        specialAttack = new KeyBinding("key.specialAttack", 82, category);
        
        ClientRegistry.registerKeyBinding(genericAttack);
        ClientRegistry.registerKeyBinding(specialAttack);
    }
}
