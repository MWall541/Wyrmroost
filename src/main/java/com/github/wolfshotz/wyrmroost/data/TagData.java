package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class TagData
{
    // note block tags need to run before item tags
    static void provide(DataGenerator gen, ExistingFileHelper fileHelper)
    {
        BlockData blockGen = new BlockData(gen, fileHelper);
        gen.addProvider(blockGen);
        gen.addProvider(new ItemData(gen, blockGen, fileHelper));
        gen.addProvider(new EntityData(gen, fileHelper));
    }

    private static class ItemData extends ItemTagsProvider
    {
        public ItemData(DataGenerator gen, BlockData blockGen, ExistingFileHelper fileHelper)
        {
            super(gen, blockGen, Wyrmroost.MOD_ID, fileHelper);
        }

        @Override
        protected void registerTags()
        {
            WRBlocks.Tags.ITEM_BLOCK_TAGS.forEach(this::copy);

            getOrCreateBuilder(Tags.Items.EGGS).add(WRItems.DRAGON_EGG.get());

            getOrCreateBuilder(ItemTags.field_232902_M_).add(WRItems.DRAGON_ARMOR_GOLD.get()); // PIGLIN_LOVED

            getOrCreateBuilder(Tags.Items.GEMS).addTag(WRItems.Tags.GEMS_GEODE);
            getOrCreateBuilder(WRItems.Tags.GEMS_GEODE).add(WRItems.BLUE_GEODE.get(), WRItems.RED_GEODE.get(), WRItems.PURPLE_GEODE.get());

            getOrCreateBuilder(WRItems.Tags.DRAGON_MEATS).add(WRItems.RAW_LOWTIER_MEAT.get(), WRItems.COOKED_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.COOKED_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.COOKED_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(), WRItems.COOKED_BEHEMOTH_MEAT.get());

            getOrCreateBuilder(Tags.Items.INGOTS).addTag(WRItems.Tags.INGOTS_PLATINUM);
            getOrCreateBuilder(WRItems.Tags.INGOTS_PLATINUM).add(WRItems.PLATINUM_INGOT.get());

            getOrCreateBuilder(ItemTags.ARROWS).add(WRItems.BLUE_GEODE_ARROW.get(), WRItems.RED_GEODE_ARROW.get(), WRItems.PURPLE_GEODE_ARROW.get());

            getOrCreateBuilder(ItemTags.field_232908_Z_).addTag(WRItems.Tags.GEMS_GEODE);
        }
    }

    private static class BlockData extends BlockTagsProvider
    {
        public BlockData(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper)
        {
            super(generatorIn, Wyrmroost.MOD_ID, existingFileHelper);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void registerTags()
        {
            getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).addTag(WRBlocks.Tags.STORAGE_BLOCKS_GEODE);

            getOrCreateBuilder(Tags.Blocks.ORES).addTag(WRBlocks.Tags.ORES_GEODE);
            getOrCreateBuilder(WRBlocks.Tags.ORES_GEODE).add(WRBlocks.BLUE_GEODE_ORE.get(), WRBlocks.RED_GEODE_ORE.get(), WRBlocks.PURPLE_GEODE_ORE.get());

            getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTags(WRBlocks.Tags.STORAGE_BLOCKS_GEODE, WRBlocks.Tags.STORAGE_BLOCKS_PLATINUM);
            getOrCreateBuilder(WRBlocks.Tags.STORAGE_BLOCKS_GEODE).add(WRBlocks.BLUE_GEODE_BLOCK.get(), WRBlocks.RED_GEODE_BLOCK.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
            getOrCreateBuilder(WRBlocks.Tags.STORAGE_BLOCKS_PLATINUM).add(WRBlocks.PLATINUM_BLOCK.get());

            getOrCreateBuilder(BlockTags.DRAGON_IMMUNE).add(WRBlocks.PURPLE_GEODE_ORE.get());
        }
    }

    private static class EntityData extends EntityTypeTagsProvider
    {
        private EntityData(DataGenerator generatorIn, ExistingFileHelper helper)
        {
            super(generatorIn, Wyrmroost.MOD_ID, helper);
        }

        @Override
        protected void registerTags()
        {
            getOrCreateBuilder(EntityTypeTags.ARROWS).add(WREntities.GEODE_TIPPED_ARROW.get());
        }
    }
}
