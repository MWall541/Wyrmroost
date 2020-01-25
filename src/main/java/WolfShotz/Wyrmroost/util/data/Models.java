package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.RegistryObject;

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
//            simpleBlock(WRBlocks.BLUE_CORIN_PLANKS.get());

            logBlock((LogBlock) WRBlocks.TEAL_CORIN_LOG.get());
            axisBlock((RotatedPillarBlock) WRBlocks.STRIPPED_TEAL_CORIN_LOG.get());
            simpleBlock(WRBlocks.TEAL_CORIN_WOOD.get(), "teal_corin_log");
//            simpleBlock(WRBlocks.TEAL_CORIN_PLANKS.get());

            logBlock((LogBlock) WRBlocks.RED_CORIN_LOG.get());
            simpleBlock(WRBlocks.RED_CORIN_WOOD.get(), "red_corin_log");
            axisBlock((RotatedPillarBlock) WRBlocks.STRIPPED_RED_CORIN_LOG.get());
//            simpleBlock(WRBlocks.RED_CORIN_PLANKS.get());
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

            getBuilder("minutus_alive").texture("layer0", resource("minutus/minutus_alive"));
            item(WRItems.MINUTUS.get(), resource("minutus/minutus"))
                    .override()
                    .predicate(ModUtils.resource("isalive"), 1)
                    .model(new ModelFile.UncheckedModelFile(resource("minutus_alive")));

            item(WRItems.SOUL_CRYSTAL.get(), resource("tools/soul_crystal"));
            item(WRItems.DRAGON_STAFF.get(), resource("tools/dragon_staff"));
            item(WRItems.BLUE_GEODE.get(), resource("materials/geode_blue"));
            item(WRItems.RED_GEODE.get(), resource("materials/geode_red"));
            item(WRItems.PURPLE_GEODE.get(), resource("materials/geode_purple"));
            item(WRItems.PLATINUM_INGOT.get(), resource("materials/platinum_ingot"));
            item(WRItems.ASH_PILE.get(), resource("materials/ash_pile"));

            handHeld(WRItems.BLUE_GEODE_SWORD.get(), resource("tools/geode/blue/blue_geode_sword"));
            handHeld(WRItems.BLUE_GEODE_PICKAXE.get(), resource("tools/geode/blue/blue_geode_pickaxe"));
            handHeld(WRItems.BLUE_GEODE_AXE.get(), resource("tools/geode/blue/blue_geode_axe"));
            handHeld(WRItems.BLUE_GEODE_SHOVEL.get(), resource("tools/geode/blue/blue_geode_shovel"));
            handHeld(WRItems.BLUE_GEODE_HOE.get(), resource("tools/geode/blue/blue_geode_hoe"));
            item(WRItems.BLUE_GEODE_HELMET.get(), resource("tools/geode/blue/blue_geode_helmet"));
            item(WRItems.BLUE_GEODE_CHESTPLATE.get(), resource("tools/geode/blue/blue_geode_chestplate"));
            item(WRItems.BLUE_GEODE_LEGGINGS.get(), resource("tools/geode/blue/blue_geode_leggings"));
            item(WRItems.BLUE_GEODE_BOOTS.get(), resource("tools/geode/blue/blue_geode_boots"));

            handHeld(WRItems.RED_GEODE_SWORD.get(), resource("tools/geode/red/red_geode_sword"));
            handHeld(WRItems.RED_GEODE_PICKAXE.get(), resource("tools/geode/red/red_geode_pickaxe"));
            handHeld(WRItems.RED_GEODE_AXE.get(), resource("tools/geode/red/red_geode_axe"));
            handHeld(WRItems.RED_GEODE_SHOVEL.get(), resource("tools/geode/red/red_geode_shovel"));
            handHeld(WRItems.RED_GEODE_HOE.get(), resource("tools/geode/red/red_geode_hoe"));
            item(WRItems.RED_GEODE_HELMET.get(), resource("tools/geode/red/red_geode_helmet"));
            item(WRItems.RED_GEODE_CHESTPLATE.get(), resource("tools/geode/red/red_geode_chestplate"));
            item(WRItems.RED_GEODE_LEGGINGS.get(), resource("tools/geode/red/red_geode_leggings"));
            item(WRItems.RED_GEODE_BOOTS.get(), resource("tools/geode/red/red_geode_boots"));

            handHeld(WRItems.PURPLE_GEODE_SWORD.get(), resource("tools/geode/purple/purple_geode_sword"));
            handHeld(WRItems.PURPLE_GEODE_PICKAXE.get(), resource("tools/geode/purple/purple_geode_pickaxe"));
            handHeld(WRItems.PURPLE_GEODE_AXE.get(), resource("tools/geode/purple/purple_geode_axe"));
            handHeld(WRItems.PURPLE_GEODE_SHOVEL.get(), resource("tools/geode/purple/purple_geode_shovel"));
            handHeld(WRItems.PURPLE_GEODE_HOE.get(), resource("tools/geode/purple/purple_geode_hoe"));
            item(WRItems.PURPLE_GEODE_HELMET.get(), resource("tools/geode/purple/purple_geode_helmet"));
            item(WRItems.PURPLE_GEODE_CHESTPLATE.get(), resource("tools/geode/purple/purple_geode_chestplate"));
            item(WRItems.PURPLE_GEODE_LEGGINGS.get(), resource("tools/geode/purple/purple_geode_leggings"));
            item(WRItems.PURPLE_GEODE_BOOTS.get(), resource("tools/geode/purple/purple_geode_boots"));
        }

        protected ItemModelBuilder item(Item item, ResourceLocation texture)
        {
            return super.getBuilder(item.getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", texture);
        }

        protected ItemModelBuilder handHeld(Item item, ResourceLocation texture)
        {
            return super.getBuilder(item.getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                    .texture("layer0", texture);
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
