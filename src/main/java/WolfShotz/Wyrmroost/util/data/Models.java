package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

public class Models
{
    public static class BlockModels extends BlockStateProvider
    {
        public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper)
        {
            super(generator, Wyrmroost.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerStatesAndModels()
        {
            simpleBlock(WRBlocks.BLUE_GEODE_BLOCK.get());
            simpleBlock(WRBlocks.RED_GEODE_BLOCK.get());
            simpleBlock(WRBlocks.PURPLE_GEODE_BLOCK.get());
            simpleBlock(WRBlocks.BLUE_GEODE_ORE.get());
            simpleBlock(WRBlocks.RED_GEODE_ORE.get());
            simpleBlock(WRBlocks.PURPLE_GEODE_ORE.get());
            simpleBlock(WRBlocks.PLATINUM_BLOCK.get());
            simpleBlock(WRBlocks.PLATINUM_ORE.get());

            simpleBlock(WRBlocks.BLUE_CRYSTAL_BLOCK.get());
            simpleBlock(WRBlocks.BLUE_CRYSTAL.get());
            simpleBlock(WRBlocks.BLUE_CRYSTAL_ORE.get());
            simpleBlock(WRBlocks.GREEN_CRYSTAL_BLOCK.get());
            simpleBlock(WRBlocks.GREEN_CRYSTAL.get());
            simpleBlock(WRBlocks.GREEN_CRYSTAL_ORE.get());
            simpleBlock(WRBlocks.ORANGE_CRYSTAL_BLOCK.get());
            simpleBlock(WRBlocks.ORANGE_CRYSTAL.get());
            simpleBlock(WRBlocks.ORANGE_CRYSTAL_ORE.get());
            simpleBlock(WRBlocks.YELLOW_CRYSTAL_BLOCK.get());
            simpleBlock(WRBlocks.YELLOW_CRYSTAL.get());
            simpleBlock(WRBlocks.YELLOW_CRYSTAL_ORE.get());

            simpleBlock(WRBlocks.ASH.get());
            simpleBlock(WRBlocks.ASH_BLOCK.get());
            logBlock((LogBlock) WRBlocks.ASH_LOG.get());

//            logBlock((LogBlock) WRBlocks.CANARI_LOG.get());
//            axisBlock((LogBlock) WRBlocks.STRIPPED_CANARI.get());
//            simpleBlock(WRBlocks.CANARI_WOOD.get());
//            simpleBlock(WRBlocks.CANARI_PLANKS.get());

            logBlock((LogBlock) WRBlocks.BLUE_CORIN_LOG.get());
            axisBlock((RotatedPillarBlock) WRBlocks.STRIPPED_BLUE_CORIN_LOG.get());
            simpleBlock(WRBlocks.BLUE_CORIN_WOOD.get(), "blue_corin_log");
            simpleBlock(WRBlocks.BLUE_CORIN_PLANKS.get());

            logBlock((LogBlock) WRBlocks.TEAL_CORIN_LOG.get());
            axisBlock((RotatedPillarBlock) WRBlocks.STRIPPED_TEAL_CORIN_LOG.get());
            simpleBlock(WRBlocks.TEAL_CORIN_WOOD.get(), "teal_corin_log");
            simpleBlock(WRBlocks.TEAL_CORIN_PLANKS.get());

            logBlock((LogBlock) WRBlocks.RED_CORIN_LOG.get());
            simpleBlock(WRBlocks.RED_CORIN_WOOD.get(), "red_corin_log");
            axisBlock((RotatedPillarBlock) WRBlocks.STRIPPED_RED_CORIN_LOG.get());
            simpleBlock(WRBlocks.RED_CORIN_PLANKS.get());
        }

        protected void axisBlock(RotatedPillarBlock block)
        {
            axisBlock(block, blockTexture(block), new ResourceLocation(blockTexture(block) + "_top"));
        }

        protected void simpleBlock(Block block, String path)
        {
            simpleBlock(block, cubeAll(block.getRegistryName().getPath(), ModUtils.resource("block/" + path)));
        }
    }

    public static class ItemModels extends ItemModelProvider
    {
        public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper)
        {
            super(generator, Wyrmroost.MOD_ID, existingFileHelper);
        }

        private static final List<Item> IGNORE = Lists.newArrayList(WRItems.MINUTUS.get(), WRItems.DRAGON_EGG.get());
        static
        {
            IGNORE.addAll(ModUtils.getRegistryEntries(WRItems.ITEMS).stream().filter(BlockItem.class::isInstance).collect(Collectors.toList()));
            IGNORE.addAll(CustomSpawnEggItem.EGG_TYPES);
            IGNORE.addAll(Lists.newArrayList( // MISSING TEXTURES
                    WRItems.FOOD_LOWTIER_MEAT_RAW.get(),
                    WRItems.FOOD_LOWTIER_MEAT_COOKED.get(),
                    WRItems.FOOD_APEX_MEAT_RAW.get(),
                    WRItems.FOOD_APEX_MEAT_COOKED.get(),
                    WRItems.FOOD_BEHEMOTH_MEAT_RAW.get(),
                    WRItems.FOOD_BEHEMOTH_MEAT_COOKED.get(),
                    WRItems.DRAKE_BOOTS.get(),
                    WRItems.DRAKE_LEGGINGS.get(),
                    WRItems.DRAKE_CHESTPLATE.get(),
                    WRItems.DRAKE_HELMET.get()
            ));
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        protected void registerModels()
        {
            // Item Blocks
            for (RegistryObject<Block> registryBlock : WRBlocks.BLOCKS.getEntries())
            {
                ResourceLocation path = registryBlock.get().getRegistryName();
                ModelFile model = new ModelFile.UncheckedModelFile(path.getNamespace() + ":block/" + path.getPath());
                getBuilder(path.getPath()).parent(model);
            }

            for (CustomSpawnEggItem item : CustomSpawnEggItem.EGG_TYPES)
            {
                getBuilder(item.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_spawn_egg")));
            }

            getBuilder(WRItems.DRAGON_EGG.get().getRegistryName().getPath()) // TODO
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                    .transforms()
                        .transform(ModelBuilder.Perspective.GUI).rotation(160, 8, 30).translation(21, 6, 0).scale(1.5f).end()
                        .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(180, -35, 0).translation(2, 11, -12).end()
                        .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(180, 35, 0).translation(-2, 11, -12).end()
                        .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(253, 65, 0).translation(8, 2, 10).scale(0.75f).end()
                        .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(253, 65, 0).translation(3, 13, 7).scale(0.75f).end()
                        .transform(ModelBuilder.Perspective.GROUND).rotation(180, 0, 0).translation(4, 8, -5).scale(0.55f);

            getBuilder("minutus_alive").texture("layer0", resource("minutus_alive"));
            item(WRItems.MINUTUS.get())
                    .override()
                    .predicate(ModUtils.resource("isalive"), 1)
                    .model(new ModelFile.UncheckedModelFile(resource("minutus_alive")));

            ModUtils.getRegistryEntries(WRItems.ITEMS)
                    .stream()
                    .filter(e -> !IGNORE.contains(e))
                    .forEach(this::item);

//            item(WRItems.TARRAGON_TOME.get(), "tarragon_tome");
//            item(WRItems.SOUL_CRYSTAL.get(), "tools/soul_crystal");
//            item(WRItems.DRAGON_STAFF.get(), "tools/dragon_staff");
//
//            item(WRItems.BLUE_GEODE.get(), "materials/blue_geode");
//            item(WRItems.RED_GEODE.get(), "materials/red_geode");
//            item(WRItems.PURPLE_GEODE.get(), "materials/purple_geode");
//            item(WRItems.PLATINUM_INGOT.get(), "materials/platinum_ingot");
//            item(WRItems.BLUE_SHARD.get(), "materials/blue_shard");
//            item(WRItems.GREEN_SHARD.get(), "materials/green_shard");
//            item(WRItems.ORANGE_SHARD.get(), "materials/orange_shard");
//            item(WRItems.YELLOW_SHARD.get(), "materials/yellow_shard");
//            item(WRItems.ASH_PILE.get(), "materials/ash_pile");
//            item(WRItems.DRAKE_BACKPLATE.get(), "materials/")
//
//            handHeld(WRItems.BLUE_GEODE_SWORD.get(), "tools/geode/blue/blue_geode_sword");
//            handHeld(WRItems.BLUE_GEODE_PICKAXE.get(), "tools/geode/blue/blue_geode_pickaxe");
//            handHeld(WRItems.BLUE_GEODE_AXE.get(), "tools/geode/blue/blue_geode_axe");
//            handHeld(WRItems.BLUE_GEODE_SHOVEL.get(), "tools/geode/blue/blue_geode_shovel");
//            handHeld(WRItems.BLUE_GEODE_HOE.get(), "tools/geode/blue/blue_geode_hoe");
//            item(WRItems.BLUE_GEODE_HELMET.get(), "tools/geode/blue/blue_geode_helmet");
//            item(WRItems.BLUE_GEODE_CHESTPLATE.get(), "tools/geode/blue/blue_geode_chestplate");
//            item(WRItems.BLUE_GEODE_LEGGINGS.get(), "tools/geode/blue/blue_geode_leggings");
//            item(WRItems.BLUE_GEODE_BOOTS.get(), "tools/geode/blue/blue_geode_boots");
//
//            handHeld(WRItems.RED_GEODE_SWORD.get(), "tools/geode/red/red_geode_sword");
//            handHeld(WRItems.RED_GEODE_PICKAXE.get(), "tools/geode/red/red_geode_pickaxe");
//            handHeld(WRItems.RED_GEODE_AXE.get(), "tools/geode/red/red_geode_axe");
//            handHeld(WRItems.RED_GEODE_SHOVEL.get(), "tools/geode/red/red_geode_shovel");
//            handHeld(WRItems.RED_GEODE_HOE.get(), "tools/geode/red/red_geode_hoe");
//            item(WRItems.RED_GEODE_HELMET.get(), "tools/geode/red/red_geode_helmet");
//            item(WRItems.RED_GEODE_CHESTPLATE.get(), "tools/geode/red/red_geode_chestplate");
//            item(WRItems.RED_GEODE_LEGGINGS.get(), "tools/geode/red/red_geode_leggings");
//            item(WRItems.RED_GEODE_BOOTS.get(), "tools/geode/red/red_geode_boots");
//
//            handHeld(WRItems.PURPLE_GEODE_SWORD.get(), "tools/geode/purple/purple_geode_sword");
//            handHeld(WRItems.PURPLE_GEODE_PICKAXE.get(), "tools/geode/purple/purple_geode_pickaxe");
//            handHeld(WRItems.PURPLE_GEODE_AXE.get(), "tools/geode/purple/purple_geode_axe");
//            handHeld(WRItems.PURPLE_GEODE_SHOVEL.get(), "tools/geode/purple/purple_geode_shovel");
//            handHeld(WRItems.PURPLE_GEODE_HOE.get(), "tools/geode/purple/purple_geode_hoe");
//            item(WRItems.PURPLE_GEODE_HELMET.get(), "tools/geode/purple/purple_geode_helmet");
//            item(WRItems.PURPLE_GEODE_CHESTPLATE.get(), "tools/geode/purple/purple_geode_chestplate");
//            item(WRItems.PURPLE_GEODE_LEGGINGS.get(), "tools/geode/purple/purple_geode_leggings");
//            item(WRItems.PURPLE_GEODE_BOOTS.get(), "tools/geode/purple/purple_geode_boots");
//
//            handHeld(WRItems.PLATINUM_SWORD.get(), "tools/platinum/platinum_sword");
//            handHeld(WRItems.PLATINUM_PICKAXE.get(), "tools/platinum/platinum_pickaxe");
//            handHeld(WRItems.PLATINUM_AXE.get(), "tools/platinum/platinum_axe");
//            handHeld(WRItems.PLATINUM_SHOVEL.get(), "tools/platinum/platinum_shovel");
//            handHeld(WRItems.PLATINUM_HOE.get(), "tools/platinum/platinum_hoe");
//            item(WRItems.PLATINUM_HELMET.get(), "tools/platinum/platinum_helmet");
//            item(WRItems.PLATINUM_CHESTPLATE.get(), "tools/platinum/platinum_chestplate");
//            item(WRItems.PLATINUM_LEGGINGS.get(), "tools/platinum/platinum_leggings");
//            item(WRItems.PLATINUM_BOOTS.get(), "tools/platinum/platinum_boots");
//
////            item(WRItems.FOOD_LOWTIER_MEAT_RAW.get(), "food/lowtier_meat_raw");
//            item(WRItems.FOOD_COMMON_MEAT_RAW.get(), "food/common_meat_raw");
////            item(WRItems.FOOD_APEX_MEAT_RAW.get(), "food/apex_meat_raw");
////            item(WRItems.FOOD_BEHEMOTH_MEAT_RAW.get(), "food/behemoth_meat_raw");
////            item(WRItems.FOOD_LOWTIER_MEAT_COOKED.get(), "food/lowtier_meat_cooked");
//            item(WRItems.FOOD_COMMON_MEAT_COOKED.get(), "food/common_meat_cooked");
////            item(WRItems.FOOD_APEX_MEAT_COOKED.get(), "food/apex_meat_cooked");
////            item(WRItems.FOOD_BEHEMOTH_MEAT_COOKED.get(), "food/behemoth_meat_cooked");
//            item(WRItems.FOOD_COOKED_MINUTUS.get(), "minutus/cooked_minutus");
//            item(WRItems.FOOD_JEWELLED_APPLE.get(), "food/jewelled_apple_blue");
//            item(WRItems.FOOD_DRAGON_FRUIT.get(), "food/dragon_fruit");
//            item(WRItems.FOOD_BLUEBERRIES.get(), "food/blueberries");
//
//            item(WRItems.DRAGON_ARMOR_IRON.get(), "tools/dragonarmors/iron");
//            item(WRItems.DRAGON_ARMOR_GOLD.get(), "tools/dragonarmors/gold");
//            item(WRItems.DRAGON_ARMOR_DIAMOND.get(), "tools/dragonarmors/diamond");
//            item(WRItems.DRAGON_ARMOR_PLATINUM.get(), "tools/dragonarmors/platinum");
//            item(WRItems.DRAGON_ARMOR_BLUE_GEODE.get(), "tools/dragonarmors/blue_geode");
//            item(WRItems.DRAGON_ARMOR_RED_GEODE.get(), "tools/dragonarmors/red_geode");
//            item(WRItems.DRAGON_ARMOR_PURPLE_GEODE.get(), "tools/dragonarmors/purple_geode");
        }

        protected ItemModelBuilder item(Item item)
        {
            ResourceLocation name = item.getRegistryName();
            String parent = (item instanceof TieredItem)? "item/handheld" : "item/generated";

            return super.getBuilder(name.getPath())
                    .parent(new ModelFile.UncheckedModelFile(parent))
                    .texture("layer0", resource(name.getPath()));
        }

        private static ResourceLocation resource(String path)
        {
            return ModUtils.resource("item/" + path);
        }

        @Override
        public String getName()
        {
            return "Wyrmroost Item Models";
        }
    }
}
