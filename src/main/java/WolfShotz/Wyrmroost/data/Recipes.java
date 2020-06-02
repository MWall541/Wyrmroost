package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Recipes extends RecipeProvider
{
    public static final Set<IItemProvider> REGISTERED; // The Results

    static // Uncraftables
    {
        REGISTERED = Sets.newHashSet(WRItems.DRAGON_EGG.get(), WRItems.DRAKE_BACKPLATE.get(), WRItems.LDWYRM.get());
        REGISTERED.addAll(ModUtils.getRegistryEntries(WRItems.ITEMS).stream().filter(CustomSpawnEggItem.class::isInstance).collect(Collectors.toSet()));
    }

    private Consumer<IFinishedRecipe> consumer;

    public Recipes(DataGenerator generatorIn) { super(generatorIn); }

    @Override
    public void act(DirectoryCache cache) throws IOException
    {
        super.act(cache);

        for (Item item : ModUtils.getRegistryEntries(WRItems.ITEMS))
        {
            if (!REGISTERED.contains(item))
                Wyrmroost.LOG.warn("Item '{}' does not have a recipe associated with it!", item.getRegistryName());
        }
    }

    public void shaped(IItemProvider result, int count, String[] patternLines, IItemProvider... ingredients)
    {
        if (REGISTERED.contains(result))
        {
            Wyrmroost.LOG.warn(result.asItem().getRegistryName().getPath() + " is Already Registered!");
            return;
        }
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(result, count);
        char[] chars = new char[] {'X', '#', 'W'};
        for (int i = 0; i < 3; ++i)
        {
            try
            {
                builder.patternLine(patternLines[i]);
                builder.key(chars[i], ingredients[i]);
            }
            catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        Item item = ingredients[0].asItem();
        builder.addCriterion("has_" + item.getRegistryName().getPath(), hasItem(item)).build(consumer);
        REGISTERED.add(result);
    }

    public void shaped(IItemProvider result, String[] patternLines, IItemProvider... ingredients)
    {
        shaped(result, 1, patternLines, ingredients);
    }

    public void shapeless(IItemProvider result, int count, Map<IItemProvider, Integer> ingredients)
    {
        if (REGISTERED.contains(result))
        {
            Wyrmroost.LOG.warn(result.asItem().getRegistryName().getPath() + " is Already Registered!");
            return;
        }
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(result, count);
        ingredients.forEach(builder::addIngredient);
        ingredients.keySet().stream().findFirst().ifPresent(i -> builder.addCriterion(i.asItem().getRegistryName().getPath(), hasItem(i.asItem())).build(consumer));

        REGISTERED.add(result);
    }

    public void shapeless(IItemProvider result, Map<IItemProvider, Integer> ingredients)
    {
        shapeless(result, 1, ingredients);
    }

    private void armorSet(IItemProvider material, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet, new String[] {"XXX", "X X"}, material);
        shaped(chest, new String[] {"X X", "XXX", "XXX"}, material);
        shaped(legs, new String[] {"XXX", "X X", "X X"}, material);
        shaped(boots, new String[] {"X X", "X X"}, material);
    }

    private void toolSet(IItemProvider material, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword, new String[] {"X", "X", "#"}, material, Items.STICK);
        shaped(pick, new String[] {"XXX", " # ", " # "}, material, Items.STICK);
        shaped(axe, new String[] {"XX", "X#", " #"}, material, Items.STICK);
        shaped(shovel, new String[] {"X", "#", "#"}, material, Items.STICK);
        shaped(hoe, new String[] {"XX", " #", " #"}, material, Items.STICK);
    }

    private void storageBlock(IItemProvider material, IItemProvider block)
    {
        shapeless(block.asItem(), ImmutableMap.of(material, 9));
        shapeless(material, 9, ImmutableMap.of(material, 1));
    }

    private void oreSmelt(IItemProvider recipeIn, IItemProvider resultIn, float experienceIn, int cookingTimeIn)
    {
        String id = resultIn.asItem().getRegistryName().getPath();
        String criterion = recipeIn.asItem().getRegistryName().getPath();

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn)
                .addCriterion("has_" + criterion, hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_smelting"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn - 100)
                .addCriterion("has_" + criterion, hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_blasting"));

        REGISTERED.add(recipeIn.asItem()); // to pass validation; actually obtainable in the ground
        REGISTERED.add(resultIn);
    }

    private void foodCook(IItemProvider recipeIn, IItemProvider resultIn, float experienceIn, int cookingTimeIn)
    {
        String id = resultIn.asItem().getRegistryName().getPath();
        String criterion = recipeIn.asItem().getRegistryName().getPath();

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn)
                .addCriterion("has_" + criterion, hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_smelting"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn + 500, CookingRecipeSerializer.CAMPFIRE_COOKING)
                .addCriterion("has_" + criterion, hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_campfire"));
        CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(recipeIn), resultIn, experienceIn, cookingTimeIn - 100, CookingRecipeSerializer.SMOKING)
                .addCriterion("has_" + criterion, hasItem(recipeIn))
                .build(consumer, Wyrmroost.rl(id + "_from_smoking"));

        REGISTERED.add(recipeIn); // to pass validation; actually obtainable from stabbing innocent creatures
        REGISTERED.add(resultIn);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        this.consumer = consumer;

        // Misc stuff
        shaped(WRItems.SOUL_CRYSTAL.get(), new String[] {" X ", "X#X", " X "}, WRItems.BLUE_GEODE.get(), Items.ENDER_EYE);
        shaped(WRItems.DRAGON_STAFF.get(), new String[] {"X", "#"}, WRItems.RED_GEODE.get(), Items.BLAZE_ROD);

        // Materials
        storageBlock(WRItems.BLUE_GEODE.get(), WRBlocks.BLUE_GEODE_BLOCK.get());
        oreSmelt(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get(), 1f, 200);

        storageBlock(WRItems.RED_GEODE.get(), WRBlocks.RED_GEODE_BLOCK.get());
        oreSmelt(WRBlocks.RED_GEODE_ORE.get(), WRItems.RED_GEODE.get(), 1f, 200);

        storageBlock(WRItems.PURPLE_GEODE.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
        oreSmelt(WRBlocks.PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get(), 1f, 200);

        storageBlock(WRItems.PLATINUM_INGOT.get(), WRBlocks.PLATINUM_BLOCK.get());
        oreSmelt(WRBlocks.PLATINUM_ORE.get(), WRItems.PLATINUM_INGOT.get(), 0.7f, 200);

        // Tools
        toolSet(WRItems.BLUE_GEODE.get(), WRItems.BLUE_GEODE_SWORD.get(), WRItems.BLUE_GEODE_PICKAXE.get(), WRItems.BLUE_GEODE_AXE.get(), WRItems.BLUE_GEODE_SHOVEL.get(), WRItems.BLUE_GEODE_HOE.get());
        armorSet(WRItems.BLUE_GEODE.get(), WRItems.BLUE_GEODE_HELMET.get(), WRItems.BLUE_GEODE_CHESTPLATE.get(), WRItems.BLUE_GEODE_LEGGINGS.get(), WRItems.BLUE_GEODE_BOOTS.get());

        toolSet(WRItems.RED_GEODE.get(), WRItems.RED_GEODE_SWORD.get(), WRItems.RED_GEODE_PICKAXE.get(), WRItems.RED_GEODE_AXE.get(), WRItems.RED_GEODE_SHOVEL.get(), WRItems.RED_GEODE_HOE.get());
        armorSet(WRItems.RED_GEODE.get(), WRItems.RED_GEODE_HELMET.get(), WRItems.RED_GEODE_CHESTPLATE.get(), WRItems.RED_GEODE_LEGGINGS.get(), WRItems.RED_GEODE_BOOTS.get());

        toolSet(WRItems.PURPLE_GEODE.get(), WRItems.PURPLE_GEODE_SWORD.get(), WRItems.PURPLE_GEODE_PICKAXE.get(), WRItems.PURPLE_GEODE_AXE.get(), WRItems.PURPLE_GEODE_SHOVEL.get(), WRItems.PURPLE_GEODE_HOE.get());
        armorSet(WRItems.PURPLE_GEODE.get(), WRItems.PURPLE_GEODE_HELMET.get(), WRItems.PURPLE_GEODE_CHESTPLATE.get(), WRItems.PURPLE_GEODE_LEGGINGS.get(), WRItems.PURPLE_GEODE_BOOTS.get());

        toolSet(WRItems.PLATINUM_INGOT.get(), WRItems.PLATINUM_SWORD.get(), WRItems.PLATINUM_PICKAXE.get(), WRItems.PLATINUM_AXE.get(), WRItems.PLATINUM_SHOVEL.get(), WRItems.PLATINUM_HOE.get());
        armorSet(WRItems.PLATINUM_INGOT.get(), WRItems.PLATINUM_HELMET.get(), WRItems.PLATINUM_CHESTPLATE.get(), WRItems.PLATINUM_LEGGINGS.get(), WRItems.PLATINUM_BOOTS.get());

        shapeless(WRItems.DRAKE_HELMET.get(), ImmutableMap.of(WRItems.DRAKE_BACKPLATE.get(), 3));
        shapeless(WRItems.DRAKE_CHESTPLATE.get(), ImmutableMap.of(WRItems.DRAKE_BACKPLATE.get(), 6));
        shapeless(WRItems.DRAKE_LEGGINGS.get(), ImmutableMap.of(WRItems.DRAKE_BACKPLATE.get(), 5));
        shapeless(WRItems.DRAKE_BOOTS.get(), ImmutableMap.of(WRItems.DRAKE_BACKPLATE.get(), 2));

        // Food
        foodCook(WRItems.LDWYRM.get(), WRItems.COOKED_MINUTUS.get(), 0.35f, 200);
        foodCook(WRItems.RAW_LOWTIER_MEAT.get(), WRItems.COOKED_LOWTIER_MEAT.get(), 0.35f, 150);
        foodCook(WRItems.RAW_COMMON_MEAT.get(), WRItems.COOKED_COMMON_MEAT.get(), 0.35f, 200);
        foodCook(WRItems.RAW_APEX_MEAT.get(), WRItems.COOKED_APEX_MEAT.get(), 0.35f, 200);
        foodCook(WRItems.RAW_BEHEMOTH_MEAT.get(), WRItems.COOKED_BEHEMOTH_MEAT.get(), 0.5f, 250);
        shaped(WRItems.JEWELLED_APPLE.get(), new String[] {" X ", "X#X", " X "}, WRItems.BLUE_GEODE.get(), Items.APPLE);

        // Dragon armor
        shaped(WRItems.DRAGON_ARMOR_IRON.get(), new String[] {"X# ", "X #", " X "}, Items.IRON_INGOT, Items.IRON_BLOCK);
        shaped(WRItems.DRAGON_ARMOR_GOLD.get(), new String[] {"X# ", "X #", " X "}, Items.GOLD_INGOT, Items.GOLD_BLOCK);
        shaped(WRItems.DRAGON_ARMOR_DIAMOND.get(), new String[] {"X# ", "X #", " X "}, Items.DIAMOND, Items.DIAMOND_BLOCK);
        shaped(WRItems.DRAGON_ARMOR_PLATINUM.get(), new String[] {"X# ", "X #", " X "}, WRItems.PLATINUM_INGOT.get(), WRBlocks.PLATINUM_BLOCK.get());
        shaped(WRItems.DRAGON_ARMOR_BLUE_GEODE.get(), new String[] {"X# ", "X #", " X "}, WRItems.BLUE_GEODE.get(), WRBlocks.BLUE_GEODE_BLOCK.get());
        shaped(WRItems.DRAGON_ARMOR_RED_GEODE.get(), new String[] {"X# ", "X #", " X "}, WRItems.RED_GEODE.get(), WRBlocks.RED_GEODE_BLOCK.get());
        shaped(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get(), new String[] {"X# ", "X #", " X "}, WRItems.PURPLE_GEODE.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
    }
}
