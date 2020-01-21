package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
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
            gen.addProvider(new ItemTagsData(gen));
            gen.addProvider(new BlockTagsData(gen));
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
        @SuppressWarnings("ConstantConditions")
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
        {
            // Misc Tools
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.TARRAGON_TOME.get())
                    .addIngredient(Items.BOOK).addIngredient(WRItems.Tags.GEODES) // Items in this shapeless recipe, max 9 unless ur retarded
                    .addCriterion("has_book", hasItem(Items.BOOK)) // dummy so I don't piss off other mods
                    .build(consumer); // nuff' said?
            ShapedRecipeBuilder.shapedRecipe(WRItems.SOUL_CRYSTAL.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('E', Items.ENDER_EYE) // Keys to define the patterns
                    .patternLine(" G ").patternLine("GEG").patternLine(" G ") // first call is top horizontal line, second call is middle line, third call is bottom line. use the keys u defined above here.
                    .addCriterion("has_eye", hasItem(Items.ENDER_EYE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_STAFF.get())
                    .key('G', WRItems.GEODE_RED.get()).key('B', Items.BLAZE_ROD)
                    .patternLine("G").patternLine("B") // not defining the other spaces in the line means they can be used anywhere in that line space. if theres room, this same line can be used in other lines on the grid
                    .addCriterion("has_rod", hasItem(Items.BLAZE_ROD))
                    .build(consumer);
            
            // Materials
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.GEODE_BLUE.get())
                    .addIngredient(WRBlocks.BLUE_GEODE_BLOCK.get())
                    .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                    .build(consumer, ModUtils.resource("bluegeode_from_block"));
            oreSmeltRecipe(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.GEODE_BLUE.get(), 1f, 200, consumer, "bluegeode");
            ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.BLUE_GEODE_BLOCK.get())
                    .addIngredient(WRItems.GEODE_BLUE.get(), 9) // defining a number in a shapeless recipe will require that many on the grid
                    .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                    .build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.GEODE_RED.get())
                    .addIngredient(WRBlocks.RED_GEODE_BLOCK.get())
                    .addCriterion("has_block", hasItem(WRBlocks.RED_GEODE_BLOCK.get()))
                    .build(consumer, ModUtils.resource("redgeode_from_block"));
            oreSmeltRecipe(WRBlocks.RED_GEODE_ORE.get(), WRItems.GEODE_RED.get(), 1f, 200, consumer, "redgeode");
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.GEODE_PURPLE.get())
                    .addIngredient(WRBlocks.PURPLE_GEODE_BLOCK.get())
                    .addCriterion("has_block", hasItem(WRBlocks.PURPLE_GEODE_BLOCK.get()))
                    .build(consumer, ModUtils.resource("purplegeode_from_block"));
            oreSmeltRecipe(WRBlocks.RED_GEODE_ORE.get(), WRItems.GEODE_RED.get(), 1f, 200, consumer, "purplegeode");
            ShapelessRecipeBuilder.shapelessRecipe(WRItems.PLATINUM_INGOT.get())
                    .addIngredient(WRBlocks.PLATINUM_BLOCK.get())
                    .addCriterion("has_block", hasItem(WRBlocks.PLATINUM_BLOCK.get()))
                    .build(consumer, ModUtils.resource("platinumingot_from_block"));
            oreSmeltRecipe(WRBlocks.PLATINUM_ORE.get(), WRItems.PLATINUM_INGOT.get(), 0.7f, 200, consumer, "platinumingot");
            ShapedRecipeBuilder.shapedRecipe(WRBlocks.ASH_BLOCK.get())
                    .key('A', WRItems.ASH_PILE.get())
                    .patternLine("AA").patternLine("AA")
                    .addCriterion("has_ash", hasItem(WRBlocks.ASH_BLOCK.get()))
                    .build(consumer);
            
            // Tools
            //blue
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_SWORD.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("G").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_PICKAXE.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('S', Items.STICK)
                    .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_AXE.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine("GS").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_SHOVEL.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("S").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_HOE.get())
                    .key('G', WRItems.GEODE_BLUE.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine(" S").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_HELMET.get())
                    .key('G', WRItems.GEODE_BLUE.get())
                    .patternLine("GGG").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_CHESTPLATE.get())
                    .key('G', WRItems.GEODE_BLUE.get())
                    .patternLine("G G").patternLine("GGG").patternLine("GGG")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_LEGGINGS.get())
                    .key('G', WRItems.GEODE_BLUE.get())
                    .patternLine("GGG").patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_BOOTS.get())
                    .key('G', WRItems.GEODE_BLUE.get())
                    .patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_BLUE.get()))
                    .build(consumer);
            //red
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_SWORD.get())
                    .key('G', WRItems.GEODE_RED.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("G").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_PICKAXE.get())
                    .key('G', WRItems.GEODE_RED.get()).key('S', Items.STICK)
                    .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_AXE.get())
                    .key('G', WRItems.GEODE_RED.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine("GS").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_SHOVEL.get())
                    .key('G', WRItems.GEODE_RED.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("S").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_HOE.get())
                    .key('G', WRItems.GEODE_RED.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine(" S").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_HELMET.get())
                    .key('G', WRItems.GEODE_RED.get())
                    .patternLine("GGG").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_CHESTPLATE.get())
                    .key('G', WRItems.GEODE_RED.get())
                    .patternLine("G G").patternLine("GGG").patternLine("GGG")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_LEGGINGS.get())
                    .key('G', WRItems.GEODE_RED.get())
                    .patternLine("GGG").patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_BOOTS.get())
                    .key('G', WRItems.GEODE_RED.get())
                    .patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_RED.get()))
                    .build(consumer);
            //purple
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_SWORD.get())
                    .key('G', WRItems.GEODE_PURPLE.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("G").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_PICKAXE.get())
                    .key('G', WRItems.GEODE_PURPLE.get()).key('S', Items.STICK)
                    .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_AXE.get())
                    .key('G', WRItems.GEODE_PURPLE.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine("GS").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_SHOVEL.get())
                    .key('G', WRItems.GEODE_PURPLE.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("S").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_HOE.get())
                    .key('G', WRItems.GEODE_PURPLE.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine(" S").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_HELMET.get())
                    .key('G', WRItems.GEODE_PURPLE.get())
                    .patternLine("GGG").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_CHESTPLATE.get())
                    .key('G', WRItems.GEODE_PURPLE.get())
                    .patternLine("G G").patternLine("GGG").patternLine("GGG")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_LEGGINGS.get())
                    .key('G', WRItems.GEODE_PURPLE.get())
                    .patternLine("GGG").patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_BOOTS.get())
                    .key('G', WRItems.GEODE_PURPLE.get())
                    .patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.GEODE_PURPLE.get()))
                    .build(consumer);
            //platinum
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_SWORD.get())
                    .key('G', WRItems.PLATINUM_INGOT.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("G").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_PICKAXE.get())
                    .key('G', WRItems.PLATINUM_INGOT.get()).key('S', Items.STICK)
                    .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_AXE.get())
                    .key('G', WRItems.PLATINUM_INGOT.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine("GS").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_SHOVEL.get())
                    .key('G', WRItems.PLATINUM_INGOT.get()).key('S', Items.STICK)
                    .patternLine("G").patternLine("S").patternLine("S")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_HOE.get())
                    .key('G', WRItems.PLATINUM_INGOT.get()).key('S', Items.STICK)
                    .patternLine("GG").patternLine(" S").patternLine(" S")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_HELMET.get())
                    .key('G', WRItems.PLATINUM_INGOT.get())
                    .patternLine("GGG").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_CHESTPLATE.get())
                    .key('G', WRItems.PLATINUM_INGOT.get())
                    .patternLine("G G").patternLine("GGG").patternLine("GGG")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_LEGGINGS.get())
                    .key('G', WRItems.PLATINUM_INGOT.get())
                    .patternLine("GGG").patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(WRItems.PLATINUM_BOOTS.get())
                    .key('G', WRItems.PLATINUM_INGOT.get())
                    .patternLine("G G").patternLine("G G")
                    .addCriterion("has_geode", hasItem(WRItems.PLATINUM_INGOT.get()))
                    .build(consumer);
        }
        
        private void oreSmeltRecipe(IItemProvider recipeIn, IItemProvider resultIn, float experienceIn, int cookingTimeIn, Consumer<IFinishedRecipe> consumer, String id)
        {
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn)
                    .addCriterion("has_ore", hasItem(recipeIn))
                    .build(consumer, ModUtils.resource(id + "_from_smelting"));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn - 100)
                    .addCriterion("has_ore", hasItem(recipeIn))
                    .build(consumer, ModUtils.resource(id + "_from_blasting"));
        }
    }
    
    public static class ItemTagsData extends ItemTagsProvider
    {
        public ItemTagsData(DataGenerator generatorIn)
        {
            super(generatorIn);
        }

        @Override
        protected void registerTags()
        {
            getBuilder(Tags.Items.EGGS).add(WRItems.DRAGON_EGG.get());
            getBuilder(WRItems.Tags.GEODES).add(WRItems.GEODE_BLUE.get(), WRItems.GEODE_RED.get(), WRItems.GEODE_PURPLE.get());
        }
    }
    
    public static class BlockTagsData extends BlockTagsProvider
    {
        public BlockTagsData(DataGenerator generatorIn)
        {
            super(generatorIn);
        }
    
        @Override
        protected void registerTags()
        {
            getBuilder(WRBlocks.Tags.STORAGE_BLOCKS_GEODE).add(WRBlocks.BLUE_GEODE_BLOCK.get(), WRBlocks.RED_GEODE_BLOCK.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
            getBuilder(Tags.Blocks.SUPPORTS_BEACON).add(WRBlocks.Tags.STORAGE_BLOCKS_GEODE);
            getBuilder(BlockTags.LOGS).add(WRBlocks.CANARI_WOOD.get());
        }
    }
}
