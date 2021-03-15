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
        return ShapedRecipeBuilder.create(result, count);
    }

    private ShapedRecipeBuilder shaped(IItemProvider result)
    {
        return shaped(result, 1);
    }

    private ShapelessRecipeBuilder shapeless(IItemProvider result, int count)
    {
        REGISTERED.add(result);
        return ShapelessRecipeBuilder.create(result, count);
    }

    private ShapelessRecipeBuilder shapeless(IItemProvider result)
    {
        return shapeless(result, 1);
    }

    /**
     * @param ingredients first element is used for criterion, design accordingly.
     */
    private void shapeless(IItemProvider result, @Nonnull ShapelessPair... ingredients)
    {
        final ShapelessRecipeBuilder offerToer = shapeless(result);
        for (ShapelessPair ingredient : ingredients) offerToer.input(ingredient.item, ingredient.count);
        IItemProvider firstIngredient = ingredients[0].item;
        offerToer.criterion("has_" + firstIngredient.asItem().getRegistryName().getPath(), conditionsFromItem(firstIngredient)).offerTo(consumer);
    }

    private void armorSet(IItemProvider material, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet).input('X', material).pattern("XXX").pattern("X X").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
        shaped(chest).input('X', material).pattern("X X").pattern("XXX").criterion("has_material", conditionsFromItem(material)).pattern("XXX").offerTo(consumer);
        shaped(legs).input('X', material).pattern("XXX").pattern("X X").criterion("has_material", conditionsFromItem(material)).pattern("X X").offerTo(consumer);
        shaped(boots).input('X', material).pattern("X X").pattern("X X").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
    }

    private void armorSet(ITag<Item> materials, IItemProvider helmet, IItemProvider chest, IItemProvider legs, IItemProvider boots)
    {
        shaped(helmet).input('X', materials).pattern("XXX").pattern("X X").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
        shaped(chest).input('X', materials).pattern("X X").pattern("XXX").criterion("has_material", conditionsFromTag(materials)).pattern("XXX").offerTo(consumer);
        shaped(legs).input('X', materials).pattern("XXX").pattern("X X").criterion("has_material", conditionsFromTag(materials)).pattern("X X").offerTo(consumer);
        shaped(boots).input('X', materials).pattern("X X").pattern("X X").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
    }

    private void toolSet(IItemProvider material, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword).input('X', material).input('|', Items.STICK).pattern("X").pattern("X").pattern("|").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
        shaped(pick).input('X', material).input('|', Items.STICK).pattern("XXX").pattern(" | ").pattern(" | ").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
        shaped(axe).input('X', material).input('|', Items.STICK).pattern("XX").pattern("X|").pattern(" |").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
        shaped(shovel).input('X', material).input('|', Items.STICK).pattern("X").pattern("|").pattern("|").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
        shaped(hoe).input('X', material).input('|', Items.STICK).pattern("XX").pattern(" |").pattern(" |").criterion("has_material", conditionsFromItem(material)).offerTo(consumer);
    }

    private void toolSet(ITag<Item> materials, IItemProvider sword, IItemProvider pick, IItemProvider axe, IItemProvider shovel, IItemProvider hoe)
    {
        shaped(sword).input('X', materials).input('|', Items.STICK).pattern("X").pattern("X").pattern("|").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
        shaped(pick).input('X', materials).input('|', Items.STICK).pattern("XXX").pattern(" | ").pattern(" | ").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
        shaped(axe).input('X', materials).input('|', Items.STICK).pattern("XX").pattern("X|").pattern(" |").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
        shaped(shovel).input('X', materials).input('|', Items.STICK).pattern("X").pattern("|").pattern("|").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
        shaped(hoe).input('X', materials).input('|', Items.STICK).pattern("XX").pattern(" |").pattern(" |").criterion("has_material", conditionsFromTag(materials)).offerTo(consumer);
    }

    private void woodGroup(WRBlocks.WoodGroup group, ITag<Item> logTag)
    {
        InventoryChangeTrigger.Instance hasPlanks = conditionsFromItem(group.getPlanks());

        shapeless(group.getPlanks(), 4).input(logTag).criterion("has_wood", conditionsFromTag(logTag)).offerTo(consumer);
        shaped(group.getWood(), 3).pattern("##").pattern("##").input('#', group.getLog()).criterion("has_log", conditionsFromItem(group.getLog())).offerTo(consumer);
        shaped(group.getStrippedWood(), 3).pattern("##").pattern("##").input('#', group.getStrippedLog()).criterion("has_log", conditionsFromItem(group.getStrippedLog())).offerTo(consumer);
        shaped(group.getSlab(), 6).pattern("###").input('#', group.getPlanks()).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getPressurePlate()).pattern("##").input('#', group.getPlanks()).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getFence(), 3).pattern("W#W").pattern("W#W").input('W', group.getPlanks()).input('#', Items.STICK).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getFenceGate()).pattern("#W#").pattern("#W#").input('W', group.getPlanks()).input('#', Items.STICK).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getTrapDoor(), 2).pattern("###").pattern("###").input('#', group.getPlanks()).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getStairs(), 4).pattern("#  ").pattern("## ").pattern("###").input('#', group.getPlanks()).criterion("has_planks", hasPlanks).offerTo(consumer);
        shapeless(group.getButton()).input(group.getPlanks()).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getDoor(), 3).pattern("##").pattern("##").pattern("##").input('#', group.getPlanks()).criterion("has_planks", hasPlanks).offerTo(consumer);
        shaped(group.getSign(), 3).pattern("###").pattern("###").pattern(" X ").input('#', group.getPlanks()).input('X', Items.STICK).criterion("has_planks", hasPlanks).offerTo(consumer);
    }

    private void storageBlock(IItemProvider material, IItemProvider block)
    {
        shaped(block).input('X', material).pattern("XXX").pattern("XXX").pattern("XXX").criterion("has_" + material.asItem().getRegistryName().getPath(), conditionsFromItem(material)).offerTo(consumer);
        shapeless(material, 9).input(block).criterion("has_" + block.asItem().getRegistryName().getPath(), conditionsFromItem(block)).offerTo(consumer);
    }

    private void smelt(IItemProvider ingredient, IItemProvider result, float experience, int time, boolean food)
    {
        String id = result.asItem().getRegistryName().getPath();
        String criterion = "has_" + ingredient.asItem().getRegistryName().getPath();

        CookingRecipeBuilder.createSmelting(Ingredient.ofItems(ingredient), result, experience, time).criterion(criterion, conditionsFromItem(ingredient)).offerTo(consumer, Wyrmroost.id((id + "_from_smelting")));
        if (food)
        {
            CookingRecipeBuilder.create(Ingredient.ofItems(ingredient), result, experience, time + 500, CookingRecipeSerializer.CAMPFIRE_COOKING).criterion(criterion, conditionsFromItem(ingredient)).offerTo(consumer, Wyrmroost.id(id + "_from_campfire"));
            CookingRecipeBuilder.create(Ingredient.ofItems(ingredient), result, experience, time - 100, CookingRecipeSerializer.SMOKING).criterion(criterion, conditionsFromItem(ingredient)).offerTo(consumer, Wyrmroost.id(id + "_from_smoking"));
        }
        else
            CookingRecipeBuilder.createBlasting(Ingredient.ofItems(ingredient), result, experience, time - 100).criterion(criterion, conditionsFromItem(ingredient)).offerTo(consumer, Wyrmroost.id(id + "_from_blasting"));

        REGISTERED.add(result);
    }

    private void smelt(IItemProvider ingredient, IItemProvider result, float experience, int time)
    {
        smelt(ingredient, result, experience, time, false);
    }

    @Override
    protected void generate(Consumer<IFinishedRecipe> consumer)
    {
        this.consumer = consumer;

        exempt(LazySpawnEggItem.class);
        exempt(WRItems.DRAGON_EGG.get(), WRItems.DRAKE_BACKPLATE.get(), WRItems.LDWYRM.get(),
                WRItems.RAW_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(),
                WRBlocks.BLUE_GEODE_ORE.get(), WRBlocks.RED_GEODE_ORE.get(), WRBlocks.PURPLE_GEODE_ORE.get(), WRBlocks.PLATINUM_ORE.get());

        // Misc stuff
        shaped(WRItems.SOUL_CRYSTAL.get()).input('X', WRItems.BLUE_GEODE.get()).input('#', Items.ENDER_EYE).pattern(" X ").pattern("X#X").pattern(" X ").criterion("has_eye", conditionsFromItem(Items.ENDER_EYE)).offerTo(consumer);
        shaped(WRItems.DRAGON_STAFF.get()).input('X', WRItems.RED_GEODE.get()).input('|', Items.BLAZE_ROD).pattern("X").pattern("|").criterion("has_geode", conditionsFromItem(WRItems.RED_GEODE.get())).offerTo(consumer);

        shaped(WRItems.BLUE_GEODE_ARROW.get(), 8).input('G', WRItems.BLUE_GEODE.get()).input('|', Items.STICK).input('F', Items.FEATHER).pattern("G").pattern("|").pattern("F").criterion("has_geode", conditionsFromItem(WRItems.BLUE_GEODE.get())).offerTo(consumer);
        shaped(WRItems.RED_GEODE_ARROW.get(), 8).input('G', WRItems.RED_GEODE.get()).input('|', Items.STICK).input('F', Items.FEATHER).pattern("G").pattern("|").pattern("F").criterion("has_geode", conditionsFromItem(WRItems.RED_GEODE.get())).offerTo(consumer);
        shaped(WRItems.PURPLE_GEODE_ARROW.get(), 8).input('G', WRItems.PURPLE_GEODE.get()).input('|', Items.STICK).input('F', Items.FEATHER).pattern("G").pattern("|").pattern("F").criterion("has_geode", conditionsFromItem(WRItems.PURPLE_GEODE.get())).offerTo(consumer);

        // Materials
        storageBlock(WRItems.BLUE_GEODE.get(), WRBlocks.BLUE_GEODE_BLOCK.get());
        smelt(WRBlocks.BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get(), 1f, 200);
        storageBlock(WRItems.RED_GEODE.get(), WRBlocks.RED_GEODE_BLOCK.get());
        smelt(WRBlocks.RED_GEODE_ORE.get(), WRItems.RED_GEODE.get(), 1.5f, 200);
        storageBlock(WRItems.PURPLE_GEODE.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
        smelt(WRBlocks.PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get(), 2f, 200);

        shaped(WRBlocks.PLATINUM_BLOCK.get()).input('X', WRItems.Tags.INGOTS_PLATINUM).pattern("XXX").pattern("XXX").pattern("XXX").criterion("has_platinum", conditionsFromItem(WRItems.PLATINUM_INGOT.get())).offerTo(consumer);
        shapeless(WRItems.PLATINUM_INGOT.get(), 9).input(WRBlocks.PLATINUM_BLOCK.get()).criterion("has_platinum", conditionsFromItem(WRBlocks.PLATINUM_BLOCK.get())).offerTo(consumer);
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
        shaped(WRItems.JEWELLED_APPLE.get()).input('A', Items.APPLE).input('G', WRItems.Tags.GEMS_GEODE).pattern(" G ").pattern("GAG").pattern(" G ").criterion("has_geode", conditionsFromItem(WRItems.BLUE_GEODE.get())).offerTo(consumer);

        // Dragon armor
        shaped(WRItems.DRAGON_ARMOR_IRON.get()).input('X', Items.IRON_INGOT).input('#', Items.IRON_BLOCK).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_iron", conditionsFromItem(Items.IRON_INGOT)).offerTo(consumer);
        shaped(WRItems.DRAGON_ARMOR_GOLD.get()).input('X', Items.GOLD_INGOT).input('#', Items.GOLD_BLOCK).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_gold", conditionsFromItem(Items.GOLD_INGOT)).offerTo(consumer);
        shaped(WRItems.DRAGON_ARMOR_DIAMOND.get()).input('X', Items.DIAMOND).input('#', Items.DIAMOND_BLOCK).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_diamond", conditionsFromItem(Items.DIAMOND)).offerTo(consumer);
        shaped(WRItems.DRAGON_ARMOR_PLATINUM.get()).input('X', WRItems.PLATINUM_INGOT.get()).input('#', WRBlocks.PLATINUM_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_platinum", conditionsFromItem(WRItems.PLATINUM_INGOT.get())).offerTo(consumer);
        shaped(WRItems.DRAGON_ARMOR_BLUE_GEODE.get()).input('X', WRItems.BLUE_GEODE.get()).input('#', WRBlocks.BLUE_GEODE_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_blue_geode", conditionsFromItem(WRItems.BLUE_GEODE.get())).offerTo(consumer);
        shaped(WRItems.DRAGON_ARMOR_RED_GEODE.get()).input('X', WRItems.RED_GEODE.get()).input('#', WRBlocks.RED_GEODE_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_red_geode", conditionsFromItem(WRItems.RED_GEODE.get())).offerTo(consumer);
        shaped(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get()).input('X', WRItems.PURPLE_GEODE.get()).input('#', WRBlocks.PURPLE_GEODE_BLOCK.get()).pattern("X# ").pattern("X #").pattern(" X ").criterion("has_purple_geode", conditionsFromItem(WRItems.PURPLE_GEODE.get())).offerTo(consumer);

        woodGroup(WRBlocks.OSERI_WOOD, WRBlocks.Tags.getItemTagFor(WRBlocks.Tags.OSERI_LOGS));
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
