package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * Data is data. It <I>could</I> be cleaner to integrate this data inside the registry logic and have the registry
 * object instances hold everything.
 * HOWEVER, I feel that, much like the "Seperate Client from Server" concept, I would like to keep
 * data related shit in its own space. Thus the DataGatherer
 * This is because, when it comes to runtime, the object instances that hold that data will never even be used.
 * Nitpicky, but I don't care its saving someone that little bit of memory to squeeze that one last chrome tab in.
 *
 * Also, this class' package and everything in it IS NOT compiled with the jar, as it is not needed for production.
 * We're just generating jsons, no need to have that delivered.
 */
@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGatherer
{
    @SubscribeEvent
    public static void gather(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        if (event.includeServer())
        {
            TagData.provide(gen, fileHelper);
            gen.addProvider(new RecipeData(gen));
            gen.addProvider(new LootTableData(gen));
            WorldData.provide(gen);
        }
        if (event.includeClient())
        {
            gen.addProvider(new BlockModelData(gen, fileHelper));
            gen.addProvider(new ItemModelData(gen, fileHelper));
            gen.addProvider(new SoundData(gen, event.getExistingFileHelper()));
        }
    }
}
