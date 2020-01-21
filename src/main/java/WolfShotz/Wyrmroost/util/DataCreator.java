package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataCreator
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
    
        if (event.includeServer())
        {
//            gen.addProvider(new ItemTagsData(gen));
            gen.addProvider(new Recipes(gen));
        }
    }
    
    public static class Recipes extends RecipeProvider
    {
        public Recipes(DataGenerator generatorIn)
        {
            super(generatorIn);
        }
    
        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
        {
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.TARRAGON_TOME.get())
                    .addIngredient(Items.BOOK).addIngredient(WRItems.Tags.GEODES)
                    .addCriterion("has_book", hasItem(Items.BOOK))
                    .build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.GEODE_BLUE.get())
                    .addIngredient(WRBlocks.BLUE_GEODE_BLOCK.get())
                    .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.SOUL_CRYSTAL.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('E', Items.ENDER_EYE)
                    .patternLine(" G ").patternLine("GEG").patternLine(" G ")
                    .addCriterion("has_eye", hasItem(Items.ENDER_EYE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_STAFF.get())
                    .key('G', WRItems.GEODE_RED.get()).key('B', Items.BLAZE_ROD)
                    .patternLine("G").patternLine("B")
                    .addCriterion("has_rod", hasItem(Items.BLAZE_ROD))
                    .build(consumer);
        }
    }
    
//    public static class ItemTagsData extends ItemTagsProvider
//    {
//        public ItemTagsData(DataGenerator generatorIn)
//        {
//            super(generatorIn);
//        }
//
//        @Override
//        protected void registerTags()
//        {
//            super.registerTags();
//
//            getBuilder(Tags.Items.EGGS).add(WRItems.DRAGON_EGG.get());
//            getBuilder(WRItems.Tags.GEODES).add(WRItems.GEODE_BLUE.get(), WRItems.GEODE_RED.get(), WRItems.GEODE_PURPLE.get());
//        }
//    }
}
