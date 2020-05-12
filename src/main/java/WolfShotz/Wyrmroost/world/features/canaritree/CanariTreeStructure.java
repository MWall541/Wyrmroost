//package WolfShotz.Wyrmroost.world.features.canaritree;
//
//import net.minecraft.util.Rotation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.math.MutableBoundingBox;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.GenerationSettings;
//import net.minecraft.world.gen.feature.NoFeatureConfig;
//import net.minecraft.world.gen.feature.structure.ScatteredStructure;
//import net.minecraft.world.gen.feature.structure.Structure;
//import net.minecraft.world.gen.feature.structure.StructureStart;
//import net.minecraft.world.gen.feature.template.TemplateManager;
//
//import java.util.Random;
//
//public class CanariTreeStructure extends ScatteredStructure<NoFeatureConfig>
//{
//    public CanariTreeStructure()
//    {
//        super(NoFeatureConfig::deserialize);
//    }
//
//    @Override
//    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
//    {
//        int i = pos.getX() >> 4;
//        int j = pos.getZ() >> 4;
//        int k = i << 4;
//        int l = j << 4;
//
//        for (Long olong : worldIn.getChunk(i, j).getStructureReferences(this.getStructureName()))
//        {
//            ChunkPos chunkpos = new ChunkPos(olong);
//            StructureStart structurestart = worldIn.getChunk(chunkpos.x, chunkpos.z).getStructureStart(this.getStructureName());
//            if (structurestart != null && structurestart != StructureStart.DUMMY)
//            {
//                structurestart.generateStructure(worldIn, rand, new MutableBoundingBox(k, l, k + 15, l + 15), new ChunkPos(i, j));
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    protected int getSeedModifier() { return 14357801; }
//
//    @Override
//    public IStartFactory getStartFactory() { return Start::new; }
//
//    @Override
//    public String getStructureName() { return "Canari_Tree"; }
//
//    @Override
//    public int getSize() { return 4; }
//
//    public static class Start extends StructureStart
//    {
//        public Start(Structure<?> p_i51341_1_, int p_i51341_2_, int p_i51341_3_, Biome p_i51341_4_, MutableBoundingBox p_i51341_5_, int p_i51341_6_, long p_i51341_7_)
//        {
//            super(p_i51341_1_, p_i51341_2_, p_i51341_3_, p_i51341_4_, p_i51341_5_, p_i51341_6_, p_i51341_7_);
//        }
//
//        @Override
//        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
//        {
//            int i = chunkX * 16;
//            int j = chunkZ * 16;
//            BlockPos blockpos = new BlockPos(i, 90, j);
//            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
//
//            components.add(new CanariTreePiece(templateManagerIn, blockpos, rotation));
//            recalculateStructureSize();
//        }
//    }
//}
