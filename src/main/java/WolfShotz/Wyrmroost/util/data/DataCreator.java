package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataCreator
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
    
        if (event.includeServer())
        {
            gen.addProvider(new Tags.BlockTagsData(gen));
            gen.addProvider(new Tags.ItemTagsData(gen));
            gen.addProvider(new Recipes(gen));
            gen.addProvider(new LootTables(gen));
        }
        if (event.includeClient())
        {
            gen.addProvider(new Models.BlockModels(gen, event.getExistingFileHelper()));
            gen.addProvider(new Models.ItemModels(gen, event.getExistingFileHelper()));
        }
    }
}
