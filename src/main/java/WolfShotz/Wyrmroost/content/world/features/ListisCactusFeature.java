//package WolfShotz.Wyrmroost.content.world.features;
//
//import WolfShotz.Wyrmroost.registry.WRBlocks;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.GenerationSettings;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraft.world.gen.feature.NoFeatureConfig;
//
//import java.util.Random;
//
//public class ListisCactusFeature extends Feature<NoFeatureConfig>
//{
//    public ListisCactusFeature() { super(NoFeatureConfig::deserialize); }
//
//    @Override
//    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
//    {
//        if (worldIn.isAirBlock(pos) && worldIn.getBlockState(pos.down()).isSolid())
//        {
//            int height = rand.nextInt(3) + 1;
//            for (int perStem = 0; perStem < height; ++perStem)
//                worldIn.setBlockState(pos.up(perStem), WRBlocks.LISTIS_CACTUS.get().getDefaultState(), 2);
//        }
//
//        return true;
//    }
//}
