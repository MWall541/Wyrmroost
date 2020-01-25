package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;

public class Tags
{
    public static class ItemTagsData extends ItemTagsProvider
    {
        public ItemTagsData(DataGenerator generatorIn)
        {
            super(generatorIn);
        }

        @Override
        protected void registerTags()
        {
            getBuilder(net.minecraftforge.common.Tags.Items.EGGS).add(WRItems.DRAGON_EGG.get());
            getBuilder(WRItems.Tags.GEODES).add(WRItems.BLUE_GEODE.get(), WRItems.RED_GEODE.get(), WRItems.PURPLE_GEODE.get());
        }
    }

    public static class BlockTagsData extends BlockTagsProvider
    {
        public BlockTagsData(DataGenerator generatorIn)
        {
            super(generatorIn);
        }

        @Override
        protected void registerTags()
        {
            getBuilder(WRBlocks.Tags.STORAGE_BLOCKS_GEODE).add(WRBlocks.BLUE_GEODE_BLOCK.get(), WRBlocks.RED_GEODE_BLOCK.get(), WRBlocks.PURPLE_GEODE_BLOCK.get());
            getBuilder(net.minecraftforge.common.Tags.Blocks.SUPPORTS_BEACON).add(WRBlocks.Tags.STORAGE_BLOCKS_GEODE);
            getBuilder(BlockTags.LOGS).add(WRBlocks.CANARI_WOOD.get());
        }
    }
}
