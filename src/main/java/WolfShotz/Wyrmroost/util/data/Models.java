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

        private static final List<Item> IGNORE = Lists.newArrayList(WRItems.MINUTUS.get(), WRItems.DRAGON_EGG.get(), WRItems.DRAGON_STAFF.get());
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

            // Eggs
            for (CustomSpawnEggItem item : CustomSpawnEggItem.EGG_TYPES)
            {
                getBuilder(item.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(mcLoc("item/template_spawn_egg")));
            }

            // Dragon Egg
            getBuilder(WRItems.DRAGON_EGG.get().getRegistryName().getPath()) // TODO
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                    .transforms()
                        .transform(ModelBuilder.Perspective.GUI).rotation(160, 8, 30).translation(21, 6, 0).scale(1.5f).end()
                        .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(180, -35, 0).translation(2, 11, -12).end()
                        .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(180, 35, 0).translation(-2, 11, -12).end()
                        .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(253, 65, 0).translation(8, 2, 10).scale(0.75f).end()
                        .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(253, 65, 0).translation(3, 13, 7).scale(0.75f).end()
                        .transform(ModelBuilder.Perspective.GROUND).rotation(180, 0, 0).translation(4, 8, -5).scale(0.55f);

            // Minutus
            getBuilder("minutus_alive")
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", resource("minutus_alive"));
            item(WRItems.MINUTUS.get())
                    .override()
                    .predicate(ModUtils.resource("isalive"), 1)
                    .model(new ModelFile.UncheckedModelFile(resource("minutus_alive")));

            getBuilder(WRItems.DRAGON_STAFF.get().getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                    .texture("layer0", resource(WRItems.DRAGON_STAFF.get().getRegistryName().getPath()));

            // All items that do not require custom attention
            ModUtils.getRegistryEntries(WRItems.ITEMS)
                    .stream()
                    .filter(e -> !IGNORE.contains(e))
                    .forEach(this::item);
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
