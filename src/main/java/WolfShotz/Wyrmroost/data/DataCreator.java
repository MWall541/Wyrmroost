package WolfShotz.Wyrmroost.data;

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

//        if (event.includeServer())
//        {
//            Tags.provide(gen);
//            gen.addProvider(new Recipes(gen));
//            gen.addProvider(new LootTables(gen));
//        }
        if (event.includeClient())
        {
            Models.provide(gen, event.getExistingFileHelper());
//            gen.addProvider(new Sounds(gen, event.getExistingFileHelper()));
        }
    }
}
