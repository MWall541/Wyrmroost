package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
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
                .key('G', WRItems.BLUE_GEODE.get()).key('E', Items.ENDER_EYE) // Keys to define the patterns
                .patternLine(" G ").patternLine("GEG").patternLine(" G ") // first call is top horizontal line, second call is middle line, third call is bottom line. use the keys u defined above here.
                .addCriterion("has_eye", hasItem(Items.ENDER_EYE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_STAFF.get())
                .key('G', WRItems.RED_GEODE.get()).key('B', Items.BLAZE_ROD)
                .patternLine("G").patternLine("B") // not defining the other spaces in the line means they can be used anywhere in that line space. if theres room, this same line can be used in other lines on the grid
                .addCriterion("has_rod", hasItem(Items.BLAZE_ROD))
                .build(consumer);

        // Materials
        ShapelessRecipeBuilder.shapelessRecipe(WRItems.BLUE_GEODE.get(), 9)
                .addIngredient(WRBlocks.BLUE_GEODE_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                .build(consumer, ModUtils.resource("bluegeode_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.BLUE_GEODE_BLOCK.get())
                .addIngredient(WRItems.BLUE_GEODE.get(), 9) // defining a number in a shapeless recipe will require that many on the grid
                .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get(), 1f, 200, consumer, "bluegeode");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.RED_GEODE.get(), 9)
                .addIngredient(WRBlocks.RED_GEODE_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.RED_GEODE_BLOCK.get()))
                .build(consumer, ModUtils.resource("redgeode_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.RED_GEODE_BLOCK.get())
                .addIngredient(WRItems.RED_GEODE.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.RED_GEODE_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.RED_GEODE_ORE.get(), WRItems.RED_GEODE.get(), 1f, 200, consumer, "redgeode");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.PURPLE_GEODE.get(), 9)
                .addIngredient(WRBlocks.PURPLE_GEODE_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.PURPLE_GEODE_BLOCK.get()))
                .build(consumer, ModUtils.resource("purplegeode_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.PURPLE_GEODE_BLOCK.get())
                .addIngredient(WRItems.PURPLE_GEODE.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.PURPLE_GEODE_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get(), 1f, 200, consumer, "purplegeode");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.PLATINUM_INGOT.get(), 9)
                .addIngredient(WRBlocks.PLATINUM_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.PLATINUM_BLOCK.get()))
                .build(consumer, ModUtils.resource("platinumingot_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.PLATINUM_BLOCK.get())
                .addIngredient(WRItems.PLATINUM_INGOT.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.PLATINUM_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.PLATINUM_ORE.get(), WRItems.PLATINUM_INGOT.get(), 0.7f, 200, consumer, "platinumingot");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.BLUE_SHARD.get(), 9)
                .addIngredient(WRBlocks.BLUE_CRYSTAL_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.BLUE_CRYSTAL_BLOCK.get()))
                .build(consumer, ModUtils.resource("blueshard_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.BLUE_CRYSTAL_BLOCK.get())
                .addIngredient(WRItems.BLUE_SHARD.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.BLUE_CRYSTAL_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.BLUE_CRYSTAL_ORE.get(), WRItems.BLUE_SHARD.get(), 0.7f, 200, consumer, "blueshard");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.GREEN_SHARD.get(), 9)
                .addIngredient(WRBlocks.GREEN_CRYSTAL_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.GREEN_CRYSTAL_BLOCK.get()))
                .build(consumer, ModUtils.resource("greenshard_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.GREEN_CRYSTAL_BLOCK.get())
                .addIngredient(WRItems.GREEN_SHARD.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.GREEN_CRYSTAL_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.GREEN_CRYSTAL_ORE.get(), WRItems.GREEN_SHARD.get(), 0.7f, 200, consumer, "greenshard");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.ORANGE_SHARD.get(), 9)
                .addIngredient(WRBlocks.ORANGE_CRYSTAL_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.ORANGE_CRYSTAL_BLOCK.get()))
                .build(consumer, ModUtils.resource("orangeshard_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.ORANGE_CRYSTAL_BLOCK.get())
                .addIngredient(WRItems.ORANGE_SHARD.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.ORANGE_CRYSTAL_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.ORANGE_CRYSTAL_ORE.get(), WRItems.ORANGE_SHARD.get(), 0.7f, 200, consumer, "orangeshard");

        ShapelessRecipeBuilder.shapelessRecipe(WRItems.YELLOW_SHARD.get(), 9)
                .addIngredient(WRBlocks.YELLOW_CRYSTAL_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.YELLOW_CRYSTAL_BLOCK.get()))
                .build(consumer, ModUtils.resource("yellowshard_from_block"));
        ShapelessRecipeBuilder.shapelessRecipe(WRBlocks.YELLOW_CRYSTAL_BLOCK.get())
                .addIngredient(WRItems.YELLOW_SHARD.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.YELLOW_CRYSTAL_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.YELLOW_CRYSTAL_ORE.get(), WRItems.YELLOW_SHARD.get(), 0.7f, 200, consumer, "yellowshard");

        ShapedRecipeBuilder.shapedRecipe(WRBlocks.ASH.get())
                .key('A', WRItems.ASH_PILE.get())
                .patternLine("AA").patternLine("AA")
                .addCriterion("has_ash", hasItem(WRBlocks.ASH.get()))
                .build(consumer);

        // Tools
        //blue
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_SWORD.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('S', Items.STICK)
                .patternLine("G").patternLine("G").patternLine("S")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_PICKAXE.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('S', Items.STICK)
                .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_AXE.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('S', Items.STICK)
                .patternLine("GG").patternLine("GS").patternLine(" S")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_SHOVEL.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('S', Items.STICK)
                .patternLine("G").patternLine("S").patternLine("S")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_HOE.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('S', Items.STICK)
                .patternLine("GG").patternLine(" S").patternLine(" S")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_HELMET.get())
                .key('G', WRItems.BLUE_GEODE.get())
                .patternLine("GGG").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_CHESTPLATE.get())
                .key('G', WRItems.BLUE_GEODE.get())
                .patternLine("G G").patternLine("GGG").patternLine("GGG")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_LEGGINGS.get())
                .key('G', WRItems.BLUE_GEODE.get())
                .patternLine("GGG").patternLine("G G").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.BLUE_GEODE_BOOTS.get())
                .key('G', WRItems.BLUE_GEODE.get())
                .patternLine("G G").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        //red
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_SWORD.get())
                .key('G', WRItems.RED_GEODE.get()).key('S', Items.STICK)
                .patternLine("G").patternLine("G").patternLine("S")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_PICKAXE.get())
                .key('G', WRItems.RED_GEODE.get()).key('S', Items.STICK)
                .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_AXE.get())
                .key('G', WRItems.RED_GEODE.get()).key('S', Items.STICK)
                .patternLine("GG").patternLine("GS").patternLine(" S")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_SHOVEL.get())
                .key('G', WRItems.RED_GEODE.get()).key('S', Items.STICK)
                .patternLine("G").patternLine("S").patternLine("S")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_HOE.get())
                .key('G', WRItems.RED_GEODE.get()).key('S', Items.STICK)
                .patternLine("GG").patternLine(" S").patternLine(" S")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_HELMET.get())
                .key('G', WRItems.RED_GEODE.get())
                .patternLine("GGG").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_CHESTPLATE.get())
                .key('G', WRItems.RED_GEODE.get())
                .patternLine("G G").patternLine("GGG").patternLine("GGG")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_LEGGINGS.get())
                .key('G', WRItems.RED_GEODE.get())
                .patternLine("GGG").patternLine("G G").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.RED_GEODE_BOOTS.get())
                .key('G', WRItems.RED_GEODE.get())
                .patternLine("G G").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        //purple
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_SWORD.get())
                .key('G', WRItems.PURPLE_GEODE.get()).key('S', Items.STICK)
                .patternLine("G").patternLine("G").patternLine("S")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_PICKAXE.get())
                .key('G', WRItems.PURPLE_GEODE.get()).key('S', Items.STICK)
                .patternLine("GGG").patternLine(" S ").patternLine(" S ")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_AXE.get())
                .key('G', WRItems.PURPLE_GEODE.get()).key('S', Items.STICK)
                .patternLine("GG").patternLine("GS").patternLine(" S")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_SHOVEL.get())
                .key('G', WRItems.PURPLE_GEODE.get()).key('S', Items.STICK)
                .patternLine("G").patternLine("S").patternLine("S")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_HOE.get())
                .key('G', WRItems.PURPLE_GEODE.get()).key('S', Items.STICK)
                .patternLine("GG").patternLine(" S").patternLine(" S")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_HELMET.get())
                .key('G', WRItems.PURPLE_GEODE.get())
                .patternLine("GGG").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_CHESTPLATE.get())
                .key('G', WRItems.PURPLE_GEODE.get())
                .patternLine("G G").patternLine("GGG").patternLine("GGG")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_LEGGINGS.get())
                .key('G', WRItems.PURPLE_GEODE.get())
                .patternLine("GGG").patternLine("G G").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.PURPLE_GEODE_BOOTS.get())
                .key('G', WRItems.PURPLE_GEODE.get())
                .patternLine("G G").patternLine("G G")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
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

        // Food
        foodCookRecipe(WRItems.MINUTUS.get(), WRItems.FOOD_COOKED_MINUTUS.get(), 0.35f, 200, "cookedminutus", consumer);
        foodCookRecipe(WRItems.FOOD_LOWTIER_MEAT_RAW.get(), WRItems.FOOD_LOWTIER_MEAT_COOKED.get(), 0.35f, 150, "cooked_lowtier", consumer);
        foodCookRecipe(WRItems.FOOD_COMMON_MEAT_RAW.get(), WRItems.FOOD_COMMON_MEAT_RAW.get(), 0.35f, 200, "cooked_common", consumer);
        foodCookRecipe(WRItems.FOOD_APEX_MEAT_RAW.get(), WRItems.FOOD_APEX_MEAT_COOKED.get(), 0.35f, 200, "cooked_apex", consumer);
        foodCookRecipe(WRItems.FOOD_BEHEMOTH_MEAT_RAW.get(), WRItems.FOOD_BEHEMOTH_MEAT_COOKED.get(), 0.5f, 250, "cooked_behemoth", consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.FOOD_JEWELLED_APPLE.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('A', Items.APPLE)
                .patternLine(" G ").patternLine("GAG").patternLine(" G ")
                .addCriterion("has_apple", hasItem(Items.APPLE))
                .build(consumer);

        // Dragon armor
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_IRON.get())
                .key('I', Items.IRON_INGOT).key('B', Blocks.IRON_BLOCK)
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_GOLD.get())
                .key('I', Items.GOLD_INGOT).key('B', Blocks.GOLD_BLOCK)
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_ingot", hasItem(Items.GOLD_INGOT))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_DIAMOND.get())
                .key('I', Items.DIAMOND).key('B', Blocks.DIAMOND_BLOCK)
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_diamond", hasItem(Items.DIAMOND))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_PLATINUM.get())
                .key('I', WRItems.PLATINUM_INGOT.get()).key('B', WRBlocks.PLATINUM_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_ingot", hasItem(WRItems.PLATINUM_INGOT.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_BLUE_GEODE.get())
                .key('I', WRItems.BLUE_GEODE.get()).key('B', WRBlocks.BLUE_GEODE_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_RED_GEODE.get())
                .key('I', WRItems.RED_GEODE.get()).key('B', WRBlocks.RED_GEODE_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get())
                .key('I', WRItems.PURPLE_GEODE.get()).key('B', WRBlocks.PURPLE_GEODE_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
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

    private void foodCookRecipe(IItemProvider recipeIn, IItemProvider resultIn, float experienceIn, int cookingTimeIn, String id, Consumer<IFinishedRecipe> consumer)
    {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn)
                .addCriterion("has_food", hasItem(recipeIn))
                .build(consumer, ModUtils.resource(id + "_from_smelting"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn + 500, CookingRecipeSerializer.CAMPFIRE_COOKING)
                .addCriterion("has_food", hasItem(recipeIn))
                .build(consumer, ModUtils.resource(id + "_from_campfire"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn - 100, CookingRecipeSerializer.SMOKING)
                .addCriterion("has_food", hasItem(recipeIn))
                .build(consumer, ModUtils.resource(id + "_from_smoking"));
    }
}
