package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRBlocks;
import net.minecraft.block.LogBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

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
        }
    }
}
