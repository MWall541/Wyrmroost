package WolfShotz.Wyrmroost.world.features;

//public class CinisRootFeature extends Feature<NoFeatureConfig>
//{
//    public CinisRootFeature() { super(NoFeatureConfig::deserialize); }
//
//    @Override
//    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
//    {
//        if (worldIn.isAirBlock(pos) && worldIn.getBlockState(pos.down()).isSolid())
//        {
//            BlockState state = WRBlocks.CINIS_ROOT.get().getDefaultState();
//            if (rand.nextInt(3) == 0) state = state.with(BlockStateProperties.AGE_0_1, 1);
//            worldIn.setBlockState(pos, state, 2);
//        }
//
//        return true;
//    }
//}
