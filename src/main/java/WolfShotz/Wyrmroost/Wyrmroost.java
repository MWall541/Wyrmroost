package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.content.world.EndOrePlacement;
import WolfShotz.Wyrmroost.content.world.dimension.WyrmroostDimension;
import WolfShotz.Wyrmroost.registry.*;
import WolfShotz.Wyrmroost.util.ConfigData;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Wyrmroost.MOD_ID)
@SuppressWarnings("unused")
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final ItemGroup CREATIVE_TAB = ModUtils.itemGroupFactory("wyrmroost", () -> new ItemStack(ModItems.GEODE_BLUE.get()));
    public static final SimpleChannel NETWORK = ModUtils.simplisticChannel(ModUtils.resource(MOD_ID), "1.0");
    
    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().register(Wyrmroost.class);
        
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS    .register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS      .register(FMLJavaModLoadingContext.get().getModEventBus());
        SetupIO.CONTAINERS  .register(FMLJavaModLoadingContext.get().getModEventBus());
        ModSounds.SOUNDS    .register(FMLJavaModLoadingContext.get().getModEventBus());
        ForgeRegistries.MOD_DIMENSIONS.register(ModDimension.withFactory(WyrmroostDimension::new).setRegistryName("dim_wyrmroost"));
        ForgeRegistries.DECORATORS.register(new EndOrePlacement().setRegistryName("end_ore"));
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigData.CommonConfig.COMMON_SPEC);
    }
    
    // Mod Specific SubscribeEvents
    
    /**
     * FML Common Setup Event
     */
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.Common.class);
        
        SetupWorld.setupOreGen();
        ModEntities.registerEntityWorldSpawns();
        NetworkUtils.registerMessages();
    }
    
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.Client.class);
        
        ModEntities.registerEntityRenders();
        ModKeys.registerKeys();
        SetupIO.screenSetup();
    }
    
    /**
     * Fire on config change
     */
    @SubscribeEvent
    public static void configLoad(ModConfig.ModConfigEvent evt) {
        if (evt.getConfig().getSpec() == ConfigData.CommonConfig.COMMON_SPEC)
            ConfigData.CommonConfig.reload();
    }
    
    /**
     * Registers item colors for rendering
     */
    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item evt) {
        ItemColors handler = evt.getItemColors();
        
        IItemColor eggColor = (stack, tintIndex) -> ((CustomSpawnEggItem) stack.getItem()).getColors(tintIndex);
        CustomSpawnEggItem.EGG_TYPES.forEach(e -> handler.register(eggColor, e));
    }
    
    /**
     * Register models for baking
     */
    @SubscribeEvent
    public static void bakeModels(ModelRegistryEvent evt) {
        for (CustomSpawnEggItem e : CustomSpawnEggItem.EGG_TYPES)
            Minecraft.getInstance().getItemRenderer().getItemModelMesher().register(e, new ModelResourceLocation("minecraft:pig_spawn_egg", "inventory"));
    }
}
