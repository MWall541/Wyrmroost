package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
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
        gen.addProvider(new EntityData(gen));
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
            WRBlocks.WRTags.ITEM_BLOCK_TAGS.forEach(this::copy);

            getOrCreateBuilder(net.minecraftforge.common.Tags.Items.EGGS).add(WRItems.DRAGON_EGG.get());
            getOrCreateBuilder(WRItems.WRTags.GEODES).add(WRItems.BLUE_GEODE.get(), WRItems.RED_GEODE.get(), WRItems.PURPLE_GEODE.get());
            getOrCreateBuilder(WRItems.WRTags.MEATS).add(Items.BEEF, Items.COOKED_BEEF, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.CHICKEN, Items.COOKED_CHICKEN, Items.MUTTON, Items.COOKED_MUTTON, Items.RABBIT, Items.COOKED_RABBIT, WRItems.RAW_LOWTIER_MEAT.get(), WRItems.COOKED_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.COOKED_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.COOKED_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(), WRItems.COOKED_BEHEMOTH_MEAT.get());
            getOrCreateBuilder(WRItems.WRTags.DRAGON_MEATS).add(WRItems.RAW_LOWTIER_MEAT.get(), WRItems.COOKED_LOWTIER_MEAT.get(), WRItems.RAW_COMMON_MEAT.get(), WRItems.COOKED_COMMON_MEAT.get(), WRItems.RAW_APEX_MEAT.get(), WRItems.COOKED_APEX_MEAT.get(), WRItems.RAW_BEHEMOTH_MEAT.get(), WRItems.COOKED_BEHEMOTH_MEAT.get());
            getOrCreateBuilder(WRItems.WRTags.PLATINUM).add(WRItems.PLATINUM_INGOT.get());
            getOrCreateBuilder(ItemTags.ARROWS).add(WRItems.BLUE_GEODE_ARROW.get(), WRItems.RED_GEODE_ARROW.get(), WRItems.PURPLE_GEODE_ARROW.get());
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
            getOrCreateBuilder(WRBlocks.WRTags.STORAGE_BLOCKS_GEODE).add(WRBlocks.BLUE_GEODE_BLOCK.get(), WRBlocks.RED_GEODE_BLOCK.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
            getOrCreateBuilder(WRBlocks.WRTags.STORAGE_BLOCKS_PLATINUM).add(WRBlocks.PLATINUM_BLOCK.get());
            getOrCreateBuilder(net.minecraftforge.common.Tags.Blocks.STORAGE_BLOCKS).addTags(WRBlocks.WRTags.STORAGE_BLOCKS_GEODE, WRBlocks.WRTags.STORAGE_BLOCKS_PLATINUM);
        }
    }

    private static class EntityData extends EntityTypeTagsProvider
    {
        private EntityData(DataGenerator generatorIn) { super(generatorIn); }

        @Override
        protected void registerTags()
        {
            getOrCreateBuilder(EntityTypeTags.ARROWS).add(WREntities.GEODE_TIPPED_ARROW.get());
        }
    }
}
