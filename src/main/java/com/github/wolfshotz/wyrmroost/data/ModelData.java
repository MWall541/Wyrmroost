package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.items.CoinDragonItem;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
class ModelData
{
    private static ExistingFileHelper theGOODExistingFileHelper;

    static void provide(DataGenerator gen, ExistingFileHelper fileHelper)
    {
        theGOODExistingFileHelper = fileHelper;

        gen.addProvider(new Blocks(gen));
        gen.addProvider(new Items(gen));
    }

    private static class Blocks extends BlockStateProvider
    {
        private final List<Block> ignored = new ArrayList<>();

        Blocks(DataGenerator generator)
        {
            super(generator, Wyrmroost.MOD_ID, theGOODExistingFileHelper);
        }

        @Override
        protected void registerStatesAndModels()
        {
            cross(WRBlocks.GILLA.get());
            cross(WRBlocks.SILVER_MOSS_BODY.get());
            cross(WRBlocks.SILVER_MOSS.get());
            vine(WRBlocks.MOSS_VINE.get());
            snowy(WRBlocks.MULCH.get());


            // All unregistered blocks will be done here. They will be simple blocks with all sides of the same texture
            // If this is unwanted, it is important to define so above
            List<String> MISSING_TEXTURES = new ArrayList<>();
            ignored.addAll(registeredBlocks.keySet());
            for (Block block : ModUtils.getRegistryEntries(WRBlocks.REGISTRY))
            {
                if (ignored.contains(block)) continue;
                if (block instanceof FlowingFluidBlock) continue;

                ResourceLocation name = block.getRegistryName();
                if (!theGOODExistingFileHelper.exists(new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath()), ResourcePackType.CLIENT_RESOURCES, ".png", "textures"))
                    MISSING_TEXTURES.add(name.getPath().replace("block/", ""));
                else simpleBlock(block);
            }

            if (!MISSING_TEXTURES.isEmpty())
                Wyrmroost.LOG.error("Blocks are missing Textures! Models will not be registered: {}", MISSING_TEXTURES.toString());
        }

        void cross(Block block)
        {
            getVariantBuilder(block)
                    .partialState()
                    .setModels(new ConfiguredModel(models().cross(block.getRegistryName().getPath(), blockTexture(block))));
        }

        void snowy(Block block)
        {
            String name = block.getRegistryName().getPath();

            getVariantBuilder(block).forAllStates(state ->
            {
                boolean snowy = state.get(SnowyDirtBlock.SNOWY);
                if (snowy)
                    return ConfiguredModel.builder().modelFile(models().getExistingFile(mcLoc("grass_block_snow"))).build();
                else
                    return ConfiguredModel.builder().modelFile(models().cubeBottomTop(name, modLoc("block/" + name + "_side"), mcLoc("block/dirt"), modLoc("block/" + name + "_top"))).build();
            });
        }

        void vine(Block block)
        {
            final String fileName = block.getRegistryName().getPath();
            final ResourceLocation texture = blockTexture(block);

            Wyrmroost.LOG.warn("VINE MODELS REGISTERED FOR: {}, A BLOCKSTATE JSON MUST BE MADE MANUALLY!", fileName);

            ignored.add(block);

            models().singleTexture(fileName + "_" + "u", mcLoc("vine_" + "u"), "vine", texture);
            for (int i = 1; i <= 4; i++)
            {
                for (int j = 1; j <= 2; j++)
                {
                    boolean flag = j == 2;
                    String name = formatVine(fileName, i, flag);
                    String parent = formatVine("vine", i, flag);
                    models().singleTexture(name, mcLoc(parent), "vine", texture);
                    if (i == 2) models().singleTexture(name + "_opposite", mcLoc(parent + "_opposite"), "vine", texture);
                }
            }
        }

        static String formatVine(String pre, int i, boolean u)
        {
            return pre + "_" + i + (u? "u" : "");
        }
    }

    private static class Items extends ItemModelProvider
    {
        private final List<Item> REGISTERED = new ArrayList<>();

        Items(DataGenerator generator)
        {
            super(generator, Wyrmroost.MOD_ID, theGOODExistingFileHelper);
        }

        private static ResourceLocation resource(String path)
        {
            return Wyrmroost.rl("item/" + path);
        }

        private ItemModelBuilder item(IItemProvider item)
        {
            return item(item, Wyrmroost.rl((item instanceof Block? "block/" : "item/") + item.asItem().getRegistryName().getPath()));
        }

        private ItemModelBuilder item(IItemProvider item, ResourceLocation path)
        {
            ItemModelBuilder builder = getBuilderFor(item);

            // model
            builder.parent(uncheckedModel(item instanceof TieredItem? "item/handheld" : "item/generated"));

            // texture
            if (theGOODExistingFileHelper.exists(path, ResourcePackType.CLIENT_RESOURCES, ".png", "textures"))
                builder.texture("layer0", path);
            else
                Wyrmroost.LOG.warn("Missing Texture for Item: {} , model will not be registered.", path.getPath().replace("item/", ""));

            return builder;
        }

        private ItemModelBuilder getBuilderFor(IItemProvider item)
        {
            REGISTERED.add(item.asItem());
            return getBuilder(item.asItem().getRegistryName().getPath());
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        protected void registerModels()
        {
            // path constants
            final ModelFile itemGenerated = uncheckedModel(mcLoc("item/generated"));
            final ModelFile spawnEggTemplate = uncheckedModel(mcLoc("item/template_spawn_egg"));
            final ModelFile bucket = uncheckedModel("forge:item/bucket");

            getBuilderFor(WRItems.DRAGON_EGG.get())
                    .parent(uncheckedModel("builtin/entity"))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .transforms()
                    .transform(ModelBuilder.Perspective.GUI).rotation(160, 8, 30).translation(21, 6, 0).scale(1.5f).end()
                    .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(180, 10, 4).translation(13, 10, -10).scale(1).end()
                    .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(180, 10, 4).translation(-2, 11, -12).scale(1).end()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(253, 65, 0).translation(8, 2, 10).scale(0.75f).end()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(253, 65, 0).translation(3, 13, 7).scale(0.75f).end()
                    .transform(ModelBuilder.Perspective.GROUND).rotation(180, 0, 0).translation(4, 8, -5).scale(0.55f).end();

            getBuilder("desert_wyrm_alive")
                    .parent(itemGenerated)
                    .texture("layer0", resource("desert_wyrm_alive"));
            item(WRItems.LDWYRM.get())
                    .override()
                    .predicate(Wyrmroost.rl("is_alive"), 1f)
                    .model(uncheckedModel(resource("desert_wyrm_alive")));

            item(WRItems.DRAGON_STAFF.get()).parent(uncheckedModel("item/handheld"));

            final ItemModelBuilder cdBuilder = item(WRItems.COIN_DRAGON.get());
            for (int i = 1; i < 5; i++)
            {
                String path = "coin_dragon" + i;
                ResourceLocation rl = resource(path);
                getBuilder(path)
                        .parent(itemGenerated)
                        .texture("layer0", rl);
                cdBuilder.override()
                        .predicate(CoinDragonItem.VARIANT_OVERRIDE, i)
                        .model(uncheckedModel(rl));
            }

            // spawn eggs
            for (LazySpawnEggItem e : LazySpawnEggItem.SPAWN_EGGS)
                getBuilderFor(e).parent(spawnEggTemplate);

            item(WRBlocks.GILLA.get());
            item(WRBlocks.SILVER_MOSS.get());
            item(WRBlocks.MOSS_VINE.get());

            // All standard item blocks
            for (Block block : ModUtils.getRegistryEntries(WRBlocks.REGISTRY))
            {
                if (block.asItem() == net.minecraft.item.Items.AIR || REGISTERED.contains(block.asItem())) continue;
                if (block instanceof FlowingFluidBlock) // Buckets
                {
                    getBuilderFor(((FlowingFluidBlock) block).getFluid().getFilledBucket()).parent(bucket);
                    continue;
                }

                ResourceLocation path = block.getRegistryName();
                getBuilderFor(block).parent(uncheckedModel(path.getNamespace() + ":block/" + path.getPath()));
            }

            // All items that do not require custom attention
            for (Item item : ModUtils.getRegistryEntries(WRItems.REGISTRY)) if (!REGISTERED.contains(item)) item(item);
        }

        @Override
        public String getName()
        {
            return "Wyrmroost Item Models";
        }

        private static ModelFile.UncheckedModelFile uncheckedModel(ResourceLocation path)
        {
            return new ModelFile.UncheckedModelFile(path);
        }

        private static ModelFile.UncheckedModelFile uncheckedModel(String path)
        {
            return new ModelFile.UncheckedModelFile(path);
        }
    }
}
