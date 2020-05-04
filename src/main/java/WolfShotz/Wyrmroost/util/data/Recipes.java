package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
    public static final Set<IItemProvider> REGISTERED = Sets.newHashSet();

    public Recipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    private static ShapedRecipeBuilder shaped(IItemProvider item)
    {
        return shaped(item, 1);
    }

    private static ShapedRecipeBuilder shaped(IItemProvider item, int count)
    {
        REGISTERED.add(item);
        return ShapedRecipeBuilder.shapedRecipe(item, count);
    }

    private static ShapelessRecipeBuilder shapeless(IItemProvider item)
    {
        return shapeless(item, 1);
    }

    private static ShapelessRecipeBuilder shapeless(IItemProvider item, int count)
    {
        REGISTERED.add(item);
        return ShapelessRecipeBuilder.shapelessRecipe(item, count);
    }

    @Override
    public void act(DirectoryCache cache) throws IOException
    {
        super.act(cache);

        for (Item item : ModUtils.getRegistryEntries(WRItems.ITEMS))
        {
            if (!REGISTERED.contains(item))
                ModUtils.L.warn("Item '{}' does not have a recipe associated with it!", item.getRegistryName());
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        // Misc Tools
//        shapeless(WRItems.TARRAGON_TOME.get())
//                .addIngredient(Items.BOOK).addIngredient(WRItems.Tags.GEODES) // Items in this shapeless recipe, max 9 unless ur retarded
//                .addCriterion("has_book", hasItem(Items.BOOK)) // dummy so I don't piss off other mods
//                .build(consumer); // nuff' said?

        shaped(WRItems.SOUL_CRYSTAL.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('E', Items.ENDER_EYE) // Keys to define the patterns
                .patternLine(" G ").patternLine("GEG").patternLine(" G ") // first call is top horizontal line, second call is middle line, third call is bottom line. use the keys u defined above here.
                .addCriterion("has_eye", hasItem(Items.ENDER_EYE))
                .build(consumer);

        shaped(WRItems.DRAGON_STAFF.get())
                .key('G', WRItems.RED_GEODE.get()).key('B', Items.BLAZE_ROD)
                .patternLine("G").patternLine("B") // not defining the other spaces in the line means they can be used anywhere in that line space. if theres room, this same line can be used in other lines on the grid
                .addCriterion("has_rod", hasItem(Items.BLAZE_ROD))
                .build(consumer);

        // Materials
        // blue geode
        shapeless(WRItems.BLUE_GEODE.get(), 9)
                .addIngredient(WRBlocks.BLUE_GEODE_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                .build(consumer, Wyrmroost.rl("bluegeode_from_block"));
        shapeless(WRBlocks.BLUE_GEODE_BLOCK.get())
                .addIngredient(WRItems.BLUE_GEODE.get(), 9) // defining a number in a shapeless recipe will require that many on the grid
                .addCriterion("has_block", hasItem(WRBlocks.BLUE_GEODE_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get(), 1f, 200, consumer, "bluegeode");

        // red geode
        shapeless(WRItems.RED_GEODE.get(), 9)
                .addIngredient(WRBlocks.RED_GEODE_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.RED_GEODE_BLOCK.get()))
                .build(consumer, Wyrmroost.rl("redgeode_from_block"));
        shapeless(WRBlocks.RED_GEODE_BLOCK.get())
                .addIngredient(WRItems.RED_GEODE.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.RED_GEODE_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.RED_GEODE_ORE.get(), WRItems.RED_GEODE.get(), 1f, 200, consumer, "redgeode");

        // purple geode
        shapeless(WRItems.PURPLE_GEODE.get(), 9)
                .addIngredient(WRBlocks.PURPLE_GEODE_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.PURPLE_GEODE_BLOCK.get()))
                .build(consumer, Wyrmroost.rl("purplegeode_from_block"));
        shapeless(WRBlocks.PURPLE_GEODE_BLOCK.get())
                .addIngredient(WRItems.PURPLE_GEODE.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.PURPLE_GEODE_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get(), 1f, 200, consumer, "purplegeode");

        // platinum
        shapeless(WRItems.PLATINUM_INGOT.get(), 9)
                .addIngredient(WRBlocks.PLATINUM_BLOCK.get())
                .addCriterion("has_block", hasItem(WRBlocks.PLATINUM_BLOCK.get()))
                .build(consumer, Wyrmroost.rl("platinumingot_from_block"));
        shapeless(WRBlocks.PLATINUM_BLOCK.get())
                .addIngredient(WRItems.PLATINUM_INGOT.get(), 9)
                .addCriterion("has_block", hasItem(WRBlocks.PLATINUM_BLOCK.get()))
                .build(consumer);
        oreSmeltRecipe(WRBlocks.PLATINUM_ORE.get(), WRItems.PLATINUM_INGOT.get(), 0.7f, 200, consumer, "platinumingot");

        // blue crystal
//        shapeless(WRItems.BLUE_SHARD.get(), 9)
//                .addIngredient(WRBlocks.BLUE_CRYSTAL_BLOCK.get())
//                .addCriterion("has_block", hasItem(WRBlocks.BLUE_CRYSTAL_BLOCK.get()))
//                .build(consumer, Wyrmroost.rl("blueshard_from_block"));
//        shapeless(WRBlocks.BLUE_CRYSTAL_BLOCK.get())
//                .addIngredient(WRItems.BLUE_SHARD.get(), 9)
//                .addCriterion("has_block", hasItem(WRBlocks.BLUE_CRYSTAL_BLOCK.get()))
//                .build(consumer);
//        oreSmeltRecipe(WRBlocks.BLUE_CRYSTAL_ORE.get(), WRItems.BLUE_SHARD.get(), 0.7f, 200, consumer, "blueshard");

        // green crystal
//        shapeless(WRItems.GREEN_SHARD.get(), 9)
//                .addIngredient(WRBlocks.GREEN_CRYSTAL_BLOCK.get())
//                .addCriterion("has_block", hasItem(WRBlocks.GREEN_CRYSTAL_BLOCK.get()))
//                .build(consumer, Wyrmroost.rl("greenshard_from_block"));
//        shapeless(WRBlocks.GREEN_CRYSTAL_BLOCK.get())
//                .addIngredient(WRItems.GREEN_SHARD.get(), 9)
//                .addCriterion("has_block", hasItem(WRBlocks.GREEN_CRYSTAL_BLOCK.get()))
//                .build(consumer);
//        oreSmeltRecipe(WRBlocks.GREEN_CRYSTAL_ORE.get(), WRItems.GREEN_SHARD.get(), 0.7f, 200, consumer, "greenshard");

        // orange crystal
//        shapeless(WRItems.ORANGE_SHARD.get(), 9)
//                .addIngredient(WRBlocks.ORANGE_CRYSTAL_BLOCK.get())
//                .addCriterion("has_block", hasItem(WRBlocks.ORANGE_CRYSTAL_BLOCK.get()))
//                .build(consumer, Wyrmroost.rl("orangeshard_from_block"));
//        shapeless(WRBlocks.ORANGE_CRYSTAL_BLOCK.get())
//                .addIngredient(WRItems.ORANGE_SHARD.get(), 9)
//                .addCriterion("has_block", hasItem(WRBlocks.ORANGE_CRYSTAL_BLOCK.get()))
//                .build(consumer);
//        oreSmeltRecipe(WRBlocks.ORANGE_CRYSTAL_ORE.get(), WRItems.ORANGE_SHARD.get(), 0.7f, 200, consumer, "orangeshard");

        // yellow crystal
//        shapeless(WRItems.YELLOW_SHARD.get(), 9)
//                .addIngredient(WRBlocks.YELLOW_CRYSTAL_BLOCK.get())
//                .addCriterion("has_block", hasItem(WRBlocks.YELLOW_CRYSTAL_BLOCK.get()))
//                .build(consumer, Wyrmroost.rl("yellowshard_from_block"));
//        shapeless(WRBlocks.YELLOW_CRYSTAL_BLOCK.get())
//                .addIngredient(WRItems.YELLOW_SHARD.get(), 9)
//                .addCriterion("has_block", hasItem(WRBlocks.YELLOW_CRYSTAL_BLOCK.get()))
//                .build(consumer);
//        oreSmeltRecipe(WRBlocks.YELLOW_CRYSTAL_ORE.get(), WRItems.YELLOW_SHARD.get(), 0.7f, 200, consumer, "yellowshard");

        // ash
//        shaped(WRBlocks.ASH.get())
//                .key('A', WRItems.ASH_PILE.get())
//                .patternLine("AA").patternLine("AA")
//                .addCriterion("has_ash", hasItem(WRBlocks.ASH.get()))
//                .build(consumer);
//        shaped(WRBlocks.ASH_BLOCK.get())
//                .key('A', WRItems.ASH_PILE.get())
//                .patternLine("AAA").patternLine("AAA")
//                .addCriterion("has_ash", hasItem(WRBlocks.ASH_BLOCK.get()))
//                .build(consumer);
//        shapeless(Items.CHARCOAL.getItem(), 4)
//                .addIngredient(WRBlocks.ASH_LOG.get())
//                .addCriterion("has_log", hasItem(WRBlocks.ASH_BLOCK.get()))
//                .build(consumer);

        // woods
//        shapeless(WRBlocks.CANARI_PLANKS.get(), 4)
//                .addIngredient(WRItems.Tags.CANARI_LOGS)
//                .addCriterion("has_log", hasItem(WRBlocks.CANARI_LOG.get()))
//                .build(consumer);
//        shapeless(WRBlocks.BLUE_CORIN_PLANKS.get(), 4)
//                .addIngredient(WRItems.Tags.BLUE_CORIN_LOGS)
//                .addCriterion("has_log", hasItem(WRBlocks.BLUE_CORIN_LOG.get()))
//                .build(consumer);
//        shapeless(WRBlocks.TEAL_CORIN_PLANKS.get(), 4)
//                .addIngredient(WRItems.Tags.TEAL_CORIN_LOGS)
//                .addCriterion("has_log", hasItem(WRBlocks.TEAL_CORIN_LOG.get()))
//                .build(consumer);
//        shapeless(WRBlocks.RED_CORIN_PLANKS.get(), 4)
//                .addIngredient(WRItems.Tags.RED_CORIN_LOGS)
//                .addCriterion("has_log", hasItem(WRBlocks.RED_CORIN_LOG.get()))
//                .build(consumer);

        // Tools
        //blue
        makeToolSet(WRItems.BLUE_GEODE.get(), WRItems.BLUE_GEODE_SWORD.get(), WRItems.BLUE_GEODE_PICKAXE.get(), WRItems.BLUE_GEODE_AXE.get(), WRItems.BLUE_GEODE_SHOVEL.get(), WRItems.BLUE_GEODE_HOE.get(), consumer);
        makeArmorSet(WRItems.BLUE_GEODE.get(), WRItems.BLUE_GEODE_HELMET.get(), WRItems.BLUE_GEODE_CHESTPLATE.get(), WRItems.BLUE_GEODE_LEGGINGS.get(), WRItems.BLUE_GEODE_BOOTS.get(), consumer);
        //red
        makeToolSet(WRItems.RED_GEODE.get(), WRItems.RED_GEODE_SWORD.get(), WRItems.RED_GEODE_PICKAXE.get(), WRItems.RED_GEODE_AXE.get(), WRItems.RED_GEODE_SHOVEL.get(), WRItems.RED_GEODE_HOE.get(), consumer);
        makeArmorSet(WRItems.RED_GEODE.get(), WRItems.RED_GEODE_HELMET.get(), WRItems.RED_GEODE_CHESTPLATE.get(), WRItems.RED_GEODE_LEGGINGS.get(), WRItems.RED_GEODE_BOOTS.get(), consumer);
        //purple
        makeToolSet(WRItems.PURPLE_GEODE.get(), WRItems.PURPLE_GEODE_SWORD.get(), WRItems.PURPLE_GEODE_PICKAXE.get(), WRItems.PURPLE_GEODE_AXE.get(), WRItems.PURPLE_GEODE_SHOVEL.get(), WRItems.PURPLE_GEODE_HOE.get(), consumer);
        makeArmorSet(WRItems.PURPLE_GEODE.get(), WRItems.PURPLE_GEODE_HELMET.get(), WRItems.PURPLE_GEODE_CHESTPLATE.get(), WRItems.PURPLE_GEODE_LEGGINGS.get(), WRItems.PURPLE_GEODE_BOOTS.get(), consumer);
        //platinum
        makeToolSet(WRItems.PLATINUM_INGOT.get(), WRItems.PLATINUM_SWORD.get(), WRItems.PLATINUM_PICKAXE.get(), WRItems.PLATINUM_AXE.get(), WRItems.PLATINUM_SHOVEL.get(), WRItems.PLATINUM_HOE.get(), consumer);
        makeArmorSet(WRItems.PLATINUM_INGOT.get(), WRItems.PLATINUM_HELMET.get(), WRItems.PLATINUM_CHESTPLATE.get(), WRItems.PLATINUM_LEGGINGS.get(), WRItems.PLATINUM_BOOTS.get(), consumer);
        //drake
        shapeless(WRItems.DRAKE_HELMET.get())
                .addIngredient(Items.IRON_HELMET).addIngredient(WRItems.DRAKE_BACKPLATE.get(), 3)
                .addCriterion("has_item", hasItem(WRItems.DRAKE_BACKPLATE.get()))
                .build(consumer);
        shapeless(WRItems.DRAKE_CHESTPLATE.get())
                .addIngredient(Items.IRON_CHESTPLATE).addIngredient(WRItems.DRAKE_BACKPLATE.get(), 6)
                .addCriterion("has_item", hasItem(WRItems.DRAKE_BACKPLATE.get()))
                .build(consumer);
        shapeless(WRItems.DRAKE_LEGGINGS.get())
                .addIngredient(Items.IRON_LEGGINGS).addIngredient(WRItems.DRAKE_BACKPLATE.get(), 5)
                .addCriterion("has_item", hasItem(WRItems.DRAKE_BACKPLATE.get()))
                .build(consumer);
        shapeless(WRItems.DRAKE_BOOTS.get())
                .addIngredient(Items.IRON_BOOTS).addIngredient(WRItems.DRAKE_BACKPLATE.get(), 2)
                .addCriterion("has_item", hasItem(WRItems.DRAKE_BACKPLATE.get()))
                .build(consumer);

        // Food
        foodCookRecipe(WRItems.MINUTUS.get(), WRItems.COOKED_MINUTUS.get(), 0.35f, 200, "cookedminutus", consumer);
        foodCookRecipe(WRItems.LOWTIER_MEAT_RAW.get(), WRItems.LOWTIER_MEAT_COOKED.get(), 0.35f, 150, "cooked_lowtier", consumer);
        foodCookRecipe(WRItems.COMMON_MEAT_RAW.get(), WRItems.COMMON_MEAT_COOKED.get(), 0.35f, 200, "cooked_common", consumer);
        foodCookRecipe(WRItems.APEX_MEAT_RAW.get(), WRItems.APEX_MEAT_COOKED.get(), 0.35f, 200, "cooked_apex", consumer);
        foodCookRecipe(WRItems.BEHEMOTH_MEAT_RAW.get(), WRItems.BEHEMOTH_MEAT_COOKED.get(), 0.5f, 250, "cooked_behemoth", consumer);
        shaped(WRItems.JEWELLED_APPLE.get())
                .key('G', WRItems.BLUE_GEODE.get()).key('A', Items.APPLE)
                .patternLine(" G ").patternLine("GAG").patternLine(" G ")
                .addCriterion("has_apple", hasItem(Items.APPLE))
                .build(consumer);

        // Dragon armor
        shaped(WRItems.DRAGON_ARMOR_IRON.get())
                .key('I', Items.IRON_INGOT).key('B', Blocks.IRON_BLOCK)
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        shaped(WRItems.DRAGON_ARMOR_GOLD.get())
                .key('I', Items.GOLD_INGOT).key('B', Blocks.GOLD_BLOCK)
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_ingot", hasItem(Items.GOLD_INGOT))
                .build(consumer);
        shaped(WRItems.DRAGON_ARMOR_DIAMOND.get())
                .key('I', Items.DIAMOND).key('B', Blocks.DIAMOND_BLOCK)
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_diamond", hasItem(Items.DIAMOND))
                .build(consumer);
        shaped(WRItems.DRAGON_ARMOR_PLATINUM.get())
                .key('I', WRItems.PLATINUM_INGOT.get()).key('B', WRBlocks.PLATINUM_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_ingot", hasItem(WRItems.PLATINUM_INGOT.get()))
                .build(consumer);
        shaped(WRItems.DRAGON_ARMOR_BLUE_GEODE.get())
                .key('I', WRItems.BLUE_GEODE.get()).key('B', WRBlocks.BLUE_GEODE_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get()))
                .build(consumer);
        shaped(WRItems.DRAGON_ARMOR_RED_GEODE.get())
                .key('I', WRItems.RED_GEODE.get()).key('B', WRBlocks.RED_GEODE_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get()))
                .build(consumer);
        shaped(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get())
                .key('I', WRItems.PURPLE_GEODE.get()).key('B', WRBlocks.PURPLE_GEODE_BLOCK.get())
                .patternLine("IB ").patternLine("I B").patternLine(" I ")
                .addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get()))
                .build(consumer);
    }

    private void makeToolSet(IItemProvider material, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe, Consumer<IFinishedRecipe> consumer)
    {
        shaped(sword)
                .key('M', material).key('S', Items.STICK)
                .patternLine("M").patternLine("M").patternLine("S")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
        shaped(pick)
                .key('M', material).key('S', Items.STICK)
                .patternLine("MMM").patternLine(" S ").patternLine(" S ")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
        shaped(axe)
                .key('M', material).key('S', Items.STICK)
                .patternLine("MM").patternLine("MS").patternLine(" S")
                .addCriterion("has_material", hasItem(axe))
                .build(consumer);
        shaped(shovel)
                .key('M', material).key('S', Items.STICK)
                .patternLine("M").patternLine("S").patternLine("S")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
        shaped(hoe)
                .key('M', material).key('S', Items.STICK)
                .patternLine("MM").patternLine(" S").patternLine(" S")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
    }

    private void makeArmorSet(IItemProvider material, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots, Consumer<IFinishedRecipe> consumer)
    {
        shaped(helmet)
                .key('M', material)
                .patternLine("MMM").patternLine("M M")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
        shaped(chest)
                .key('M', material)
                .patternLine("M M").patternLine("MMM").patternLine("MMM")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
        shaped(legs)
                .key('M', material)
                .patternLine("MMM").patternLine("M M").patternLine("M M")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
        shaped(boots)
                .key('M', material)
                .patternLine("M M").patternLine("M M")
                .addCriterion("has_material", hasItem(material))
                .build(consumer);
    }

    private void oreSmeltRecipe(IItemProvider recipeIn, IItemProvider resultIn, float experienceIn, int cookingTimeIn, Consumer<IFinishedRecipe> consumer, String id)
    {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn)
                .addCriterion("has_ore", hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_smelting"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn - 100)
                .addCriterion("has_ore", hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_blasting"));
        REGISTERED.add(recipeIn);
    }

    private void foodCookRecipe(IItemProvider recipeIn, IItemProvider resultIn, float experienceIn, int cookingTimeIn, String id, Consumer<IFinishedRecipe> consumer)
    {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn)
                .addCriterion("has_food", hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_smelting"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn + 500, CookingRecipeSerializer.CAMPFIRE_COOKING)
                .addCriterion("has_food", hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_campfire"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn - 100, CookingRecipeSerializer.SMOKING)
                .addCriterion("has_food", hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_smoking"));
        REGISTERED.add(recipeIn);
        REGISTERED.add(resultIn);
    }
}
