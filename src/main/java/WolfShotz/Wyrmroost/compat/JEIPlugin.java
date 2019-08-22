package WolfShotz.Wyrmroost.compat;

import WolfShotz.Wyrmroost.util.ModUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid() { return ModUtils.location("internal"); }
}
