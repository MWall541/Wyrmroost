package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.registry.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Wyrmroost.MOD_ID)
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final ItemGroup ITEM_GROUP = new WRItemGroup();
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(rl("network"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public Wyrmroost()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonEvents.onModConstruction(eventBus);

        WREntities.ENTITIES.register(eventBus);
        WRBlocks.BLOCKS.register(eventBus);
        WRItems.ITEMS.register(eventBus);
//        WRFluids.FLUIDS.register(eventBus);
        WRIO.CONTAINERS.register(eventBus);
        WRSounds.SOUNDS.register(eventBus);
//        WRBiomes.BIOMES.register(eventBus);
//        WRWorld.FEATURES.register(eventBus);
//        eventBus.addGenericListener(ModDimension.class, (RegistryEvent.Register<ModDimension> e) -> e.getRegistry().register(ModDimension.withFactory(WyrmroostDimension::new).setRegistryName("wyrmroost")));

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WRConfig.CommonConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WRConfig.ClientConfig.CLIENT_SPEC);
    }

    /**
     * Register a new Wyrmroost Specific Resource Location. <P>
     * Don't bash me for the method name it makes total sense ffs: <P>
     * <b><i>r</i></b>esource <P>
     * <b><i>l</i></b>ocation <P>
     *
     * @return somethin related to a resource idk
     */
    public static ResourceLocation rl(String path) { return new ResourceLocation(MOD_ID, path); }

    /**
     * Its still <b>Creative Tab</b>. Idc what anyone says.
     */
    static class WRItemGroup extends ItemGroup
    {
        public WRItemGroup() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(WRItems.BLUE_GEODE.get()); }

        @Override
        public void fill(NonNullList<ItemStack> items)
        {
            super.fill(items);
            if (WRConfig.debugMode)
                items.add(new ItemStack(Items.STICK).setDisplayName(new StringTextComponent("Debug Stick")));
        }
    }
}
