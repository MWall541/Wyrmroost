package WolfShotz.Wyrmroost;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("wyrmroost")
public class Wyrmroost
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

//       MinecraftForge.EVENT_BUS.register(this); was this even needed?
    }

    private void setup(final FMLCommonSetupEvent event) { }
}
