package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class RecipeData extends RecipeProvider
{
    private static final Set<IItemProvider> REGISTERED = new HashSet<>();

    private Consumer<IFinishedRecipe> consumer;

    RecipeData(DataGenerator generatorIn) { super(generatorIn); }

    @Override
    public void act(DirectoryCache cache) throws IOException
    {
        super.act(cache);

        Set<Item> registered = REGISTERED.stream().map(IItemProvider::asItem).collect(Collectors.toSet());
        for (Item item : ModUtils.getRegistryEntries(WRItems.REGISTRY))
        {
            if (!registered.contains(item))
                Wyrmroost.LOG.warn("Item '{}' does not have a recipe associated with it!", item.getRegistryName());
        }
    }

    private ShapedRecipeBuilder shaped(IItemProvider result, int count)
    {
        REGISTERED.add(result);
        return ShapedRecipeBuilder.shapedRecipe(result, count);
    }

    private ShapedRecipeBuilder shaped(IItemProvider result) { return shaped(result, 1); }

    private ShapelessRecipeBuilder shapeless(IItemProvider result, int count)
    {
        REGISTERED.add(result);
        return ShapelessRecipeBuilder.shapelessRecipe(result, count);
    }

    private ShapelessRecipeBuilder shapeless(IItemProvider result) { return shapeless(result, 1); }

    /**
     * @param ingredients first element is used for criterion, design accordingly.
     */
    private void shapeless(IItemProvider result, @Nonnull ShapelessPair... ingredients)
    {
        final ShapelessRecipeBuilder builder = shapeless(result);
        for (ShapelessPair ingredient : ingredients) builder.addIngredient(ingredient.item, ingredient.count);
        IItemProvider firstIngredient = ingredients[0].item;
        builder.addCriterion("has_" + firstIngredient.asItem().getRegistryName().getPath(), hasItem(firstIngredient)).build(consumer);
    }

    private void armorSet(IItemProvider material, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet).key('X', material).patternLine("XXX").patternLine("X X").addCriterion("has_material", hasItem(material)).build(consumer);
        shaped(chest).key('X', material).patternLine("X X").patternLine("XXX").addCriterion("has_material", hasItem(material)).patternLine("XXX").build(consumer);
        shaped(legs).key('X', material).patternLine("XXX").patternLine("X X").addCriterion("has_material", hasItem(material)).patternLine("X X").build(consumer);
        shaped(boots).key('X', material).patternLine("X X").patternLine("X X").addCriterion("has_material", hasItem(material)).build(consumer);
    }

    private void armorSet(ITag<Item> materials, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet).key('X', materials).patternLine("XXX").patternLine("X X").addCriterion("has_material", hasItem(materials)).build(consumer);
        shaped(chest).key('X', materials).patternLine("X X").patternLine("XXX").addCriterion("has_material", hasItem(materials)).patternLine("XXX").build(consumer);
        shaped(legs).key('X', materials).patternLine("XXX").patternLine("X X").addCriterion("has_material", hasItem(materials)).patternLine("X X").build(consumer);
        shaped(boots).key('X', materials).patternLine("X X").patternLine("X X").addCriterion("has_material", hasItem(materials)).build(consumer);
    }

    private void toolSet(IItemProvider material, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword).key('X', material).key('|', Items.STICK).patternLine("X").patternLine("X").patternLine("|").addCriterion("has_material", hasItem(material)).build(consumer);
        shaped(pick).key('X', material).key('|', Items.STICK).patternLine("XXX").patternLine(" | ").patternLine(" | ").addCriterion("has_material", hasItem(material)).build(consumer);
        shaped(axe).key('X', material).key('|', Items.STICK).patternLine("XX").patternLine("X|").patternLine(" |").addCriterion("has_material", hasItem(material)).build(consumer);
        shaped(shovel).key('X', material).key('|', Items.STICK).patternLine("X").patternLine("|").patternLine("|").addCriterion("has_material", hasItem(material)).build(consumer);
        shaped(hoe).key('X', material).key('|', Items.STICK).patternLine("XX").patternLine(" |").patternLine(" |").addCriterion("has_material", hasItem(material)).build(consumer);
    }

    private void toolSet(ITag<Item> materials, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword).key('X', materials).key('|', Items.STICK).patternLine("X").patternLine("X").patternLine("|").addCriterion("has_material", hasItem(materials)).build(consumer);
        shaped(pick).key('X', materials).key('|', Items.STICK).patternLine("XXX").patternLine(" | ").patternLine(" | ").addCriterion("has_material", hasItem(materials)).build(consumer);
        shaped(axe).key('X', materials).key('|', Items.STICK).patternLine("XX").patternLine("X|").patternLine(" |").addCriterion("has_material", hasItem(materials)).build(consumer);
        shaped(shovel).key('X', materials).key('|', Items.STICK).patternLine("X").patternLine("|").patternLine("|").addCriterion("has_material", hasItem(materials)).build(consumer);
        shaped(hoe).key('X', materials).key('|', Items.STICK).patternLine("XX").patternLine(" |").patternLine(" |").addCriterion("has_material", hasItem(materials)).build(consumer);
    }

    private void storageBlock(IItemProvider material, IItemProvider block)
    {
        shaped(block).key('X', material).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion("has_" + material.asItem().getRegistryName().getPath(), hasItem(material)).build(consumer);
        shapeless(material, 9).addIngredient(block).addCriterion("has_" + block.asItem().getRegistryName().getPath(), hasItem(block)).build(consumer);
    }

    private void smelt(IItemProvider ingredient, IItemProvider result, float experience, int time, boolean food)
    {
        String id = result.asItem().getRegistryName().getPath();
        String criterion = "has_" + ingredient.asItem().getRegistryName().getPath();

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ingredient), result, experience, time).addCriterion(criterion, hasItem(ingredient)).build(consumer, Wyrmroost.rl((id + "_from_smelting")));
        if (food)
        {
            CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient), result, experience, time + 500, CookingRecipeSerializer.CAMPFIRE_COOKING).addCriterion(criterion, hasItem(ingredient)).build(consumer, Wyrmroost.rl(id + "_from_campfire"));
            CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient), result, experience, time - 100, CookingRecipeSerializer.SMOKING).addCriterion(criterion, hasItem(ingredient)).build(consumer, Wyrmroost.rl(id + "_from_smoking"));
        }
        else
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ingredient), result, experience, time - 100).addCriterion(criterion, hasItem(ingredient)).build(consumer, Wyrmroost.rl(id + "_from_blasting"));

        REGISTERED.add(result);
    }

    private void smelt(IItemProvider ingredient, IItemProvider result, float experience, int time) { smelt(ingredient, result, experience, time, false); }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        this.consumer = consumer;

        exempt(LazySpawnEggItem.class);
        exempt(WRItems.DRAGON_EGG.get(), WRItems.DRAKE_BACKPLATE.get(), WRItems.LDWYRM.get(),
                WRItems.RAW_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(),
                WRBlocks.BLUE_GEODE_ORE.get(), WRBlocks.RED_GEODE_ORE.get(), WRBlocks.PURPLE_GEODE_ORE.get(), WRBlocks.PLATINUM_ORE.get());

        // Misc stuff
        shaped(WRItems.SOUL_CRYSTAL.get()).key('X', WRItems.BLUE_GEODE.get()).key('#', Items.ENDER_EYE).patternLine(" X ").patternLine("X#X").patternLine(" X ").addCriterion("has_eye", hasItem(Items.ENDER_EYE)).build(consumer);
        shaped(WRItems.DRAGON_STAFF.get()).key('X', WRItems.RED_GEODE.get()).key('|', Items.BLAZE_ROD).patternLine("X").patternLine("|").addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get())).build(consumer);

        shaped(WRItems.BLUE_GEODE_ARROW.get(), 8).key('G', WRItems.BLUE_GEODE.get()).key('|', Items.STICK).key('F', Items.FEATHER).patternLine("G").patternLine("|").patternLine("F").addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get())).build(consumer);
        shaped(WRItems.RED_GEODE_ARROW.get(), 8).key('G', WRItems.RED_GEODE.get()).key('|', Items.STICK).key('F', Items.FEATHER).patternLine("G").patternLine("|").patternLine("F").addCriterion("has_geode", hasItem(WRItems.RED_GEODE.get())).build(consumer);
        shaped(WRItems.PURPLE_GEODE_ARROW.get(), 8).key('G', WRItems.PURPLE_GEODE.get()).key('|', Items.STICK).key('F', Items.FEATHER).patternLine("G").patternLine("|").patternLine("F").addCriterion("has_geode", hasItem(WRItems.PURPLE_GEODE.get())).build(consumer);

        // Materials
        storageBlock(WRItems.BLUE_GEODE.get(), WRBlocks.BLUE_GEODE_BLOCK.get());
        smelt(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get(), 1f, 200);
        storageBlock(WRItems.RED_GEODE.get(), WRBlocks.RED_GEODE_BLOCK.get());
        smelt(WRBlocks.RED_GEODE_ORE.get(), WRItems.RED_GEODE.get(), 1.5f, 200);
        storageBlock(WRItems.PURPLE_GEODE.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
        smelt(WRBlocks.PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get(), 2f, 200);

        shaped(WRBlocks.PLATINUM_BLOCK.get()).key('X', WRItems.Tags.INGOTS_PLATINUM).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion("has_platinum", hasItem(WRItems.PLATINUM_INGOT.get())).build(consumer);
        shapeless(WRItems.PLATINUM_INGOT.get(), 9).addIngredient(WRBlocks.PLATINUM_BLOCK.get()).addCriterion("has_platinum", hasItem(WRBlocks.PLATINUM_BLOCK.get())).build(consumer);
        smelt(WRBlocks.PLATINUM_ORE.get(), WRItems.PLATINUM_INGOT.get(), 0.7f, 200);

        // Tools
        toolSet(WRItems.BLUE_GEODE.get(), WRItems.BLUE_GEODE_SWORD.get(), WRItems.BLUE_GEODE_PICKAXE.get(), WRItems.BLUE_GEODE_AXE.get(), WRItems.BLUE_GEODE_SHOVEL.get(), WRItems.BLUE_GEODE_HOE.get());
        toolSet(WRItems.RED_GEODE.get(), WRItems.RED_GEODE_SWORD.get(), WRItems.RED_GEODE_PICKAXE.get(), WRItems.RED_GEODE_AXE.get(), WRItems.RED_GEODE_SHOVEL.get(), WRItems.RED_GEODE_HOE.get());
        toolSet(WRItems.PURPLE_GEODE.get(), WRItems.PURPLE_GEODE_SWORD.get(), WRItems.PURPLE_GEODE_PICKAXE.get(), WRItems.PURPLE_GEODE_AXE.get(), WRItems.PURPLE_GEODE_SHOVEL.get(), WRItems.PURPLE_GEODE_HOE.get());
        toolSet(WRItems.Tags.INGOTS_PLATINUM, WRItems.PLATINUM_SWORD.get(), WRItems.PLATINUM_PICKAXE.get(), WRItems.PLATINUM_AXE.get(), WRItems.PLATINUM_SHOVEL.get(), WRItems.PLATINUM_HOE.get());

        armorSet(WRItems.BLUE_GEODE.get(), WRItems.BLUE_GEODE_HELMET.get(), WRItems.BLUE_GEODE_CHESTPLATE.get(), WRItems.BLUE_GEODE_LEGGINGS.get(), WRItems.BLUE_GEODE_BOOTS.get());
        armorSet(WRItems.RED_GEODE.get(), WRItems.RED_GEODE_HELMET.get(), WRItems.RED_GEODE_CHESTPLATE.get(), WRItems.RED_GEODE_LEGGINGS.get(), WRItems.RED_GEODE_BOOTS.get());
        armorSet(WRItems.PURPLE_GEODE.get(), WRItems.PURPLE_GEODE_HELMET.get(), WRItems.PURPLE_GEODE_CHESTPLATE.get(), WRItems.PURPLE_GEODE_LEGGINGS.get(), WRItems.PURPLE_GEODE_BOOTS.get());
        armorSet(WRItems.Tags.INGOTS_PLATINUM, WRItems.PLATINUM_HELMET.get(), WRItems.PLATINUM_CHESTPLATE.get(), WRItems.PLATINUM_LEGGINGS.get(), WRItems.PLATINUM_BOOTS.get());

        shapeless(WRItems.DRAKE_HELMET.get(), new ShapelessPair(WRItems.DRAKE_BACKPLATE.get(), 3), new ShapelessPair(WRItems.PLATINUM_HELMET.get()));
        shapeless(WRItems.DRAKE_CHESTPLATE.get(), new ShapelessPair(WRItems.DRAKE_BACKPLATE.get(), 6), new ShapelessPair(WRItems.PLATINUM_CHESTPLATE.get()));
        shapeless(WRItems.DRAKE_LEGGINGS.get(), new ShapelessPair(WRItems.DRAKE_BACKPLATE.get(), 5), new ShapelessPair(WRItems.PLATINUM_LEGGINGS.get()));
        shapeless(WRItems.DRAKE_BOOTS.get(), new ShapelessPair(WRItems.DRAKE_BACKPLATE.get(), 2), new ShapelessPair(WRItems.PLATINUM_BOOTS.get()));

        // Food
        smelt(WRItems.LDWYRM.get(), WRItems.COOKED_MINUTUS.get(), 0.35f, 200, true);
        smelt(WRItems.RAW_LOWTIER_MEAT.get(), WRItems.COOKED_LOWTIER_MEAT.get(), 0.35f, 150, true);
        smelt(WRItems.RAW_COMMON_MEAT.get(), WRItems.COOKED_COMMON_MEAT.get(), 0.35f, 200, true);
        smelt(WRItems.RAW_APEX_MEAT.get(), WRItems.COOKED_APEX_MEAT.get(), 0.35f, 200, true);
        smelt(WRItems.RAW_BEHEMOTH_MEAT.get(), WRItems.COOKED_BEHEMOTH_MEAT.get(), 0.5f, 250, true);
        shaped(WRItems.JEWELLED_APPLE.get()).key('A', Items.APPLE).key('G', WRItems.Tags.GEMS_GEODE).patternLine(" G ").patternLine("GAG").patternLine(" G ").addCriterion("has_geode", hasItem(WRItems.BLUE_GEODE.get())).build(consumer);

        // Dragon armor
        shaped(WRItems.DRAGON_ARMOR_IRON.get()).key('X', Items.IRON_INGOT).key('#', Items.IRON_BLOCK).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_iron", hasItem(Items.IRON_INGOT)).build(consumer);
        shaped(WRItems.DRAGON_ARMOR_GOLD.get()).key('X', Items.GOLD_INGOT).key('#', Items.GOLD_BLOCK).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_gold", hasItem(Items.GOLD_INGOT)).build(consumer);
        shaped(WRItems.DRAGON_ARMOR_DIAMOND.get()).key('X', Items.DIAMOND).key('#', Items.DIAMOND_BLOCK).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_diamond", hasItem(Items.DIAMOND)).build(consumer);
        shaped(WRItems.DRAGON_ARMOR_PLATINUM.get()).key('X', WRItems.PLATINUM_INGOT.get()).key('#', WRBlocks.PLATINUM_BLOCK.get()).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_platinum", hasItem(WRItems.PLATINUM_INGOT.get())).build(consumer);
        shaped(WRItems.DRAGON_ARMOR_BLUE_GEODE.get()).key('X', WRItems.BLUE_GEODE.get()).key('#', WRBlocks.BLUE_GEODE_BLOCK.get()).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_blue_geode", hasItem(WRItems.BLUE_GEODE.get())).build(consumer);
        shaped(WRItems.DRAGON_ARMOR_RED_GEODE.get()).key('X', WRItems.RED_GEODE.get()).key('#', WRBlocks.RED_GEODE_BLOCK.get()).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_red_geode", hasItem(WRItems.RED_GEODE.get())).build(consumer);
        shaped(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get()).key('X', WRItems.PURPLE_GEODE.get()).key('#', WRBlocks.PURPLE_GEODE_BLOCK.get()).patternLine("X# ").patternLine("X #").patternLine(" X ").addCriterion("has_purple_geode", hasItem(WRItems.PURPLE_GEODE.get())).build(consumer);
    }

    private static void exempt(IItemProvider... exempts) { Collections.addAll(REGISTERED, exempts); }

    private static void exempt(Class<? extends IItemProvider> exemptClass)
    {
        Set<IItemProvider> set = new HashSet<>();
        for (Item item : ModUtils.getRegistryEntries(WRItems.REGISTRY)) if (exemptClass.isInstance(item)) set.add(item);
        REGISTERED.addAll(set);
    }

    private static class ShapelessPair
    {
        IItemProvider item;
        int count;

        public ShapelessPair(IItemProvider item, int count)
        {
            this.item = item;
            this.count = count;
        }

        public ShapelessPair(IItemProvider item)
        {
            this.item = item;
            this.count = 1;
        }
    }
}
