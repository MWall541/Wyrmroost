package WolfShotz.Wyrmroost.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * Data is data. It <I>could</I> be cleaner to integrate this data inside the registry logic and have the registry
 * object instances hold everything.
 * HOWEVER, I feel that, much like the "Seperate Client from Server" concept, I would like to keep
 * data related shit in its own space. Thus the DataGatherer
 * This is because, when it comes to runtime, the object instances that hold that data will never even be used.
 * Nitpicky, but I don't care its saving someone that little bit of memory to squeeze that one last chrome tab in.
 */
public class DataGatherer
{
    public static void gather(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        if (event.includeServer())
        {
            TagData.provide(gen, event.getExistingFileHelper());
            gen.addProvider(new RecipeData(gen));
            gen.addProvider(new LootTableData(gen));
        }
        if (event.includeClient())
        {
            ModelData.provide(gen, event.getExistingFileHelper());
            gen.addProvider(new SoundData(gen, event.getExistingFileHelper()));
        }
    }
}
