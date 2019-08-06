package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.event.ForgeEvents;
import WolfShotz.Wyrmroost.event.SetupEntity;
import WolfShotz.Wyrmroost.event.SetupItem;
import WolfShotz.Wyrmroost.event.SetupOreGen;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Wyrmroost.modID)
public class Wyrmroost
{
    public static final String modID = "wyrmroost";
    public static final ItemGroup creativeTab = new CreativeTab();

    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        SetupOreGen.setupOreGen();

        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::cancelFall);

        ModUtils.L.debug("commonSetup complete");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        SetupEntity.registerEntityRenders();

        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::ridingPerspective);

        ModUtils.L.info("clientSetup complete");
    }

    private static class CreativeTab extends ItemGroup
    {
        private CreativeTab() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(SetupItem.itemgeode); }
    }

}
