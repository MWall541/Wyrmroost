package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.setup.EntitySetup;
import WolfShotz.Wyrmroost.setup.ItemSetup;
import WolfShotz.Wyrmroost.setup.SetupOreGen;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.proxy.ClientProxy;
import WolfShotz.Wyrmroost.util.proxy.IProxy;
import WolfShotz.Wyrmroost.util.proxy.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Wyrmroost.modID)
public class Wyrmroost
{
    public static final String modID = "wyrmroost";
    public static final ItemGroup creativeTab = new CreativeTab();

    public static IProxy proxy;

    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    }

    private void setup(final FMLCommonSetupEvent event) {
        SetupOreGen.setupOreGen();

        ModUtils.L.debug("setup complete");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntitySetup.registerEntityRenders();

        ModUtils.L.debug("clientSetup complete");
    }

    private static class CreativeTab extends ItemGroup
    {
        private CreativeTab() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(ItemSetup.itemgeode); }
    }

}
