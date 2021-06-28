package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
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

    RecipeData(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    public void run(DirectoryCache cache) throws IOException
    {
        super.run(cache);

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
        return ShapedRecipeBuilder.shaped(result, count);
    }

    private ShapedRecipeBuilder shaped(IItemProvider result)
    {
        return shaped(result, 1);
    }

    private ShapelessRecipeBuilder shapeless(IItemProvider result, int count)
    {
        REGISTERED.add(result);
        return ShapelessRecipeBuilder.shapeless(result, count);
    }

    private ShapelessRecipeBuilder shapeless(IItemProvider result)
    {
        return shapeless(result, 1);
    }

    /**
     * @param ingredients first element is used for unlockedBy(), design accordingly.
     */
    private void shapeless(IItemProvider result, @Nonnull ShapelessPair... ingredients)
    {
        final ShapelessRecipeBuilder builder = shapeless(result);
        for (ShapelessPair ingredient : ingredients) builder.requires(ingredient.item, ingredient.count);
        IItemProvider firstIngredient = ingredients[0].item;
        builder.unlockedBy("has_" + firstIngredient.asItem().getRegistryName().getPath(), has(firstIngredient)).save(consumer);
    }

    private void armorSet(IItemProvider material, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet).define('X', material).pattern("XXX").pattern("X X").unlockedBy("has_material", has(material)).save(consumer);
        shaped(chest).define('X', material).pattern("X X").pattern("XXX").unlockedBy("has_material", has(material)).pattern("XXX").save(consumer);
        shaped(legs).define('X', material).pattern("XXX").pattern("X X").unlockedBy("has_material", has(material)).pattern("X X").save(consumer);
        shaped(boots).define('X', material).pattern("X X").pattern("X X").unlockedBy("has_material", has(material)).save(consumer);
    }

    private void armorSet(ITag<Item> materials, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet).define('X', materials).pattern("XXX").pattern("X X").unlockedBy("has_material", has(materials)).save(consumer);
        shaped(chest).define('X', materials).pattern("X X").pattern("XXX").unlockedBy("has_material", has(materials)).pattern("XXX").save(consumer);
        shaped(legs).define('X', materials).pattern("XXX").pattern("X X").unlockedBy("has_material", has(materials)).pattern("X X").save(consumer);
        shaped(boots).define('X', materials).pattern("X X").pattern("X X").unlockedBy("has_material", has(materials)).save(consumer);
    }

    private void toolSet(IItemProvider material, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword).define('X', material).define('|', Items.STICK).pattern("X").pattern("X").pattern("|").unlockedBy("has_material", has(material)).save(consumer);
        shaped(pick).define('X', material).define('|', Items.STICK).pattern("XXX").pattern(" | ").pattern(" | ").unlockedBy("has_material", has(material)).save(consumer);
        shaped(axe).define('X', material).define('|', Items.STICK).pattern("XX").pattern("X|").pattern(" |").unlockedBy("has_material", has(material)).save(consumer);
        shaped(shovel).define('X', material).define('|', Items.STICK).pattern("X").pattern("|").pattern("|").unlockedBy("has_material", has(material)).save(consumer);
        shaped(hoe).define('X', material).define('|', Items.STICK).pattern("XX").pattern(" |").pattern(" |").unlockedBy("has_material", has(material)).save(consumer);
    }

    private void toolSet(ITag<Item> materials, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword).define('X', materials).define('|', Items.STICK).pattern("X").pattern("X").pattern("|").unlockedBy("has_material", has(materials)).save(consumer);
        shaped(pick).define('X', materials).define('|', Items.STICK).pattern("XXX").pattern(" | ").pattern(" | ").unlockedBy("has_material", has(materials)).save(consumer);
        shaped(axe).define('X', materials).define('|', Items.STICK).pattern("XX").pattern("X|").pattern(" |").unlockedBy("has_material", has(materials)).save(consumer);
        shaped(shovel).define('X', materials).define('|', Items.STICK).pattern("X").pattern("|").pattern("|").unlockedBy("has_material", has(materials)).save(consumer);
        shaped(hoe).define('X', materials).define('|', Items.STICK).pattern("XX").pattern(" |").pattern(" |").unlockedBy("has_material", has(materials)).save(consumer);
    }

    private void woodGroup(WRBlocks.WoodGroup group, ITag<Item> logTag)
    {
        InventoryChangeTrigger.Instance hasPlanks = has(group.getPlanks());

        shapeless(group.getPlanks(), 4).requires(logTag).unlockedBy("has_wood", has(logTag)).save(consumer);
        shaped(group.getWood(), 3).pattern("##").pattern("##").define('#', group.getLog()).unlockedBy("has_log", has(group.getLog())).save(consumer);
        shaped(group.getStrippedWood(), 3).pattern("##").pattern("##").define('#', group.getStrippedLog()).unlockedBy("has_log", has(group.getStrippedLog())).save(consumer);
        shaped(group.getSlab(), 6).pattern("###").define('#', group.getPlanks()).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getPressurePlate()).pattern("##").define('#', group.getPlanks()).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getFence(), 3).pattern("W#W").pattern("W#W").define('W', group.getPlanks()).define('#', Items.STICK).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getFenceGate()).pattern("#W#").pattern("#W#").define('W', group.getPlanks()).define('#', Items.STICK).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getTrapDoor(), 2).pattern("###").pattern("###").define('#', group.getPlanks()).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getStairs(), 4).pattern("#  ").pattern("## ").pattern("###").define('#', group.getPlanks()).unlockedBy("has_planks", hasPlanks).save(consumer);
        shapeless(group.getButton()).requires(group.getPlanks()).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getDoor(), 3).pattern("##").pattern("##").pattern("##").define('#', group.getPlanks()).unlockedBy("has_planks", hasPlanks).save(consumer);
        shaped(group.getSign(), 3).pattern("###").pattern("###").pattern(" X ").define('#', group.getPlanks()).define('X', Items.STICK).unlockedBy("has_planks", hasPlanks).save(consumer);
    }

    private void storageBlock(IItemProvider material, IItemProvider block)
    {
        shaped(block).define('X', material).pattern("XXX").pattern("XXX").pattern("XXX").unlockedBy("has_" + material.asItem().getRegistryName().getPath(), has(material)).save(consumer);
        shapeless(material, 9).requires(block).unlockedBy("has_" + block.asItem().getRegistryName().getPath(), has(block)).save(consumer);
    }

    private void smelt(IItemProvider ingredient, IItemProvider result, float experience, int time, boolean food)
    {
        String id = result.asItem().getRegistryName().getPath();
        String hasMaterial = "has_" + ingredient.asItem().getRegistryName().getPath();

        CookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, experience, time).unlockedBy(hasMaterial, has(ingredient)).save(consumer, Wyrmroost.id((id + "_from_smelting")));
        if (food)
        {
            CookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, experience, time + 500, CookingRecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy(hasMaterial, has(ingredient)).save(consumer, Wyrmroost.id(id + "_from_campfire"));
            CookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, experience, time - 100, CookingRecipeSerializer.SMOKING_RECIPE).unlockedBy(hasMaterial, has(ingredient)).save(consumer, Wyrmroost.id(id + "_from_smoking"));
        }
        else
            CookingRecipeBuilder.blasting(Ingredient.of(ingredient), result, experience, time - 100).unlockedBy(hasMaterial, has(ingredient)).save(consumer, Wyrmroost.id(id + "_from_blasting"));

        REGISTERED.add(result);
    }

    private void smelt(IItemProvider ingredient, IItemProvider result, float experience, int time)
    {
        smelt(ingredient, result, experience, time, false);
    }

    @Override // tf is this name mojang
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
    {
        this.consumer = consumer;

        exempt(LazySpawnEggItem.class);
        exempt(WRItems.DRAGON_EGG.get(), WRItems.DRAKE_BACKPLATE.get(), WRItems.LDWYRM.get(),
                WRItems.RAW_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(),
                WRBlocks.BLUE_GEODE_ORE.get(), WRBlocks.RED_GEODE_ORE.get(), WRBlocks.PURPLE_GEODE_ORE.get(), WRBlocks.PLATINUM_ORE.get());

        // Misc stuff
        shaped(WRItems.SOUL_CRYSTAL.get()).define('X', WRItems.BLUE_GEODE.get()).define('#', Items.ENDER_EYE).pattern(" X ").pattern("X#X").pattern(" X ").unlockedBy("has_eye", has(Items.ENDER_EYE)).save(consumer);
        shaped(WRItems.DRAGON_STAFF.get()).define('X', WRItems.RED_GEODE.get()).define('|', Items.BLAZE_ROD).pattern("X").pattern("|").unlockedBy("has_geode", has(WRItems.RED_GEODE.get())).save(consumer);

        shaped(WRItems.BLUE_GEODE_ARROW.get(), 8).define('G', WRItems.BLUE_GEODE.get()).define('|', Items.STICK).define('F', Items.FEATHER).pattern("G").pattern("|").pattern("F").unlockedBy("has_geode", has(WRItems.BLUE_GEODE.get())).save(consumer);
        shaped(WRItems.RED_GEODE_ARROW.get(), 8).define('G', WRItems.RED_GEODE.get()).define('|', Items.STICK).define('F', Items.FEATHER).pattern("G").pattern("|").pattern("F").unlockedBy("has_geode", has(WRItems.RED_GEODE.get())).save(consumer);
        shaped(WRItems.PURPLE_GEODE_ARROW.get(), 8).define('G', WRItems.PURPLE_GEODE.get()).define('|', Items.STICK).define('F', Items.FEATHER).pattern("G").pattern("|").pattern("F").unlockedBy("has_geode", has(WRItems.PURPLE_GEODE.get())).save(consumer);

        // Materials
        storageBlock(WRItems.BLUE_GEODE.get(), WRBlocks.BLUE_GEODE_BLOCK.get());
        smelt(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get(), 1f, 200);
        storageBlock(WRItems.RED_GEODE.get(), WRBlocks.RED_GEODE_BLOCK.get());
        smelt(WRBlocks.RED_GEODE_ORE.get(), WRItems.RED_GEODE.get(), 1.5f, 200);
        storageBlock(WRItems.PURPLE_GEODE.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
        smelt(WRBlocks.PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get(), 2f, 200);

        shaped(WRBlocks.PLATINUM_BLOCK.get()).define('X', WRItems.Tags.INGOTS_PLATINUM).pattern("XXX").pattern("XXX").pattern("XXX").unlockedBy("has_platinum", has(WRItems.PLATINUM_INGOT.get())).save(consumer);
        shapeless(WRItems.PLATINUM_INGOT.get(), 9).requires(WRBlocks.PLATINUM_BLOCK.get()).unlockedBy("has_platinum", has(WRBlocks.PLATINUM_BLOCK.get())).save(consumer);
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
        shaped(WRItems.JEWELLED_APPLE.get()).define('A', Items.APPLE).define('G', WRItems.Tags.GEMS_GEODE).pattern(" G ").pattern("GAG").pattern(" G ").unlockedBy("has_geode", has(WRItems.BLUE_GEODE.get())).save(consumer);

        // Dragon armor
        shaped(WRItems.DRAGON_ARMOR_IRON.get()).define('X', Items.IRON_INGOT).define('#', Items.IRON_BLOCK).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_iron", has(Items.IRON_INGOT)).save(consumer);
        shaped(WRItems.DRAGON_ARMOR_GOLD.get()).define('X', Items.GOLD_INGOT).define('#', Items.GOLD_BLOCK).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_gold", has(Items.GOLD_INGOT)).save(consumer);
        shaped(WRItems.DRAGON_ARMOR_DIAMOND.get()).define('X', Items.DIAMOND).define('#', Items.DIAMOND_BLOCK).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_diamond", has(Items.DIAMOND)).save(consumer);
        shaped(WRItems.DRAGON_ARMOR_PLATINUM.get()).define('X', WRItems.PLATINUM_INGOT.get()).define('#', WRBlocks.PLATINUM_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_platinum", has(WRItems.PLATINUM_INGOT.get())).save(consumer);
        shaped(WRItems.DRAGON_ARMOR_BLUE_GEODE.get()).define('X', WRItems.BLUE_GEODE.get()).define('#', WRBlocks.BLUE_GEODE_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_blue_geode", has(WRItems.BLUE_GEODE.get())).save(consumer);
        shaped(WRItems.DRAGON_ARMOR_RED_GEODE.get()).define('X', WRItems.RED_GEODE.get()).define('#', WRBlocks.RED_GEODE_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_red_geode", has(WRItems.RED_GEODE.get())).save(consumer);
        shaped(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get()).define('X', WRItems.PURPLE_GEODE.get()).define('#', WRBlocks.PURPLE_GEODE_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").unlockedBy("has_purple_geode", has(WRItems.PURPLE_GEODE.get())).save(consumer);

        woodGroup(WRBlocks.OSERI_WOOD, WRBlocks.Tags.getItemTagFor(WRBlocks.Tags.OSERI_LOGS));
        shaped(WRBlocks.OSERI_WOOD.getLog(), 4).define('#', WRBlocks.OSERI_WOOD.getWood()).pattern("##").pattern("##").unlockedBy("has_wood", has(WRBlocks.OSERI_WOOD.getLog())); // here because normal logs are unobtainable
        woodGroup(WRBlocks.SAL_WOOD, WRBlocks.Tags.getItemTagFor(WRBlocks.Tags.SAL_LOGS));
    }

    private static void exempt(IItemProvider... exempts)
    {
        Collections.addAll(REGISTERED, exempts);
    }

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
