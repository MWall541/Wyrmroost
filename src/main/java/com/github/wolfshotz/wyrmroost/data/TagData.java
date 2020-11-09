package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class TagData
{
    // note block tags need to run before item tags
    static void provide(DataGenerator gen, ExistingFileHelper fileHelper)
    {
        gen.addProvider(new BlockData(gen));
        gen.addProvider(new ItemData(gen));
        gen.addProvider(new EntityData(gen));
    }

    private static class ItemData extends ItemTagsProvider
    {
        public ItemData(DataGenerator gen)
        {
            super(gen);
        }

        @Override
        protected void registerTags()
        {
            WRBlocks.Tags.ITEM_BLOCK_TAGS.forEach(this::copy);

            getBuilder(net.minecraftforge.common.Tags.Items.EGGS).add(WRItems.DRAGON_EGG.get());
            getBuilder(WRItems.Tags.GEODES).add(WRItems.BLUE_GEODE.get(), WRItems.RED_GEODE.get(), WRItems.PURPLE_GEODE.get());
            getBuilder(WRItems.Tags.DRAGON_MEATS).add(WRItems.RAW_LOWTIER_MEAT.get(), WRItems.COOKED_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.COOKED_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.COOKED_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(), WRItems.COOKED_BEHEMOTH_MEAT.get());
            getBuilder(WRItems.Tags.MEATS).add(WRItems.Tags.DRAGON_MEATS).add(Items.BEEF, Items.COOKED_BEEF, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.CHICKEN, Items.COOKED_CHICKEN, Items.MUTTON, Items.COOKED_MUTTON, Items.RABBIT, Items.COOKED_RABBIT);
            getBuilder(WRItems.Tags.PLATINUM).add(WRItems.PLATINUM_INGOT.get());
            getBuilder(ItemTags.ARROWS).add(WRItems.BLUE_GEODE_ARROW.get(), WRItems.RED_GEODE_ARROW.get(), WRItems.PURPLE_GEODE_ARROW.get());
        }
    }

    private static class BlockData extends BlockTagsProvider
    {
        public BlockData(DataGenerator generatorIn)
        {
            super(generatorIn);
        }

        @Override
        protected void registerTags()
        {
            getBuilder(WRBlocks.Tags.STORAGE_BLOCKS_GEODE).add(WRBlocks.BLUE_GEODE_BLOCK.get(), WRBlocks.RED_GEODE_BLOCK.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
            getBuilder(WRBlocks.Tags.STORAGE_BLOCKS_PLATINUM).add(WRBlocks.PLATINUM_BLOCK.get());
            getBuilder(net.minecraftforge.common.Tags.Blocks.STORAGE_BLOCKS).add(WRBlocks.Tags.STORAGE_BLOCKS_GEODE, WRBlocks.Tags.STORAGE_BLOCKS_PLATINUM);
        }
    }

    private static class EntityData extends EntityTypeTagsProvider
    {
        private EntityData(DataGenerator generatorIn) { super(generatorIn); }

        @Override
        protected void registerTags()
        {
            getBuilder(EntityTypeTags.ARROWS).add(WREntities.GEODE_TIPPED_ARROW.get());
        }
    }
}
