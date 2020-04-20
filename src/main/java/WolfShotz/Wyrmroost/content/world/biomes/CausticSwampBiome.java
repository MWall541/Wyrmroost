package WolfShotz.Wyrmroost.content.world.biomes;

import WolfShotz.Wyrmroost.util.world.WRBiome;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class CausticSwampBiome extends WRBiome
{
    public CausticSwampBiome()
    {
        super(10, new Biome.Builder()
                .surfaceBuilder(new CausticSurfaceBuilder(), SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                .precipitation(RainType.RAIN)
                .category(Category.SWAMP)
                .depth(0.2F)
                .scale(0.1F)
                .temperature(2f)
                .downfall(0.9F)
                .waterColor(6388580)
                .waterFogColor(2302743));

        addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.SHEEP, 12, 4, 4));
        addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PIG, 10, 4, 4));
        addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
        addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.COW, 8, 4, 4));
        addSpawn(EntityClassification.AMBIENT, new Biome.SpawnListEntry(EntityType.BAT, 10, 8, 8));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 100, 4, 4));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SKELETON, 100, 4, 4));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.CREEPER, 100, 4, 4));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 100, 4, 4));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.WITCH, 5, 1, 1));
        addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 1, 1, 1));

        addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL));

        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSwampClayDisks(this);
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addExtraReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addFossils(this);
//        addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature(Feature.LAKE, new LakesConfig(WRFluids.CAUSTIC_WATER.getBlock().getDefaultState()), Placement.WATER_LAKE, new LakeChanceConfig(4)));
        addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, createDecoratedFeature(Feature.LAKE, new LakesConfig(Blocks.LAVA.getDefaultState()), Placement.LAVA_LAKE, new LakeChanceConfig(80)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.SEAGRASS, new SeaGrassConfig(64, 0.6D), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NO_PLACEMENT_CONFIG));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.SWAMP_FLOWER, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_32, new FrequencyConfig(1)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(Blocks.GRASS.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(5)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.DEAD_BUSH, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(1)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.WATERLILY, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(4)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.BROWN_MUSHROOM.getDefaultState()), Placement.COUNT_CHANCE_HEIGHTMAP, new HeightWithChanceConfig(8, 0.25F)));
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.RED_MUSHROOM.getDefaultState()), Placement.COUNT_CHANCE_HEIGHTMAP_DOUBLE, new HeightWithChanceConfig(8, 0.125F)));

        queueWork = b ->
        {
//            b.addStructure(WRWorld.CANARI_TREE_STRUCTURE.get(), IFeatureConfig.NO_FEATURE_CONFIG);

//            b.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(WRWorld.CANARI_TREE_FEATURE.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(2, 0.1F, 1)));
//            b.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, createDecoratedFeature(WRWorld.CANARI_TREE_STRUCTURE.get(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(2, 0.1F, 1)));
        };
    }

    @Override
    public int getGrassColor(BlockPos pos)
    {
        double d0 = INFO_NOISE.getValue((double) pos.getX() * 0.0225D, (double) pos.getZ() * 0.0225D);
        return d0 < -0.1D ? 5011004 : 6975545;
    }

    @Override
    public int getFoliageColor(BlockPos pos)
    {
        return super.getFoliageColor(pos);
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        float f = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(0, f2 * 0.5f, f3 * 0.1f);
    }

    public static class CausticSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
    {
        public CausticSurfaceBuilder()
        {
            super(SurfaceBuilderConfig::deserialize);
        }

        @Override
        public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config)
        {
//            defaultFluid = WRFluids.CAUSTIC_WATER.getBlock().getDefaultState();

            int i = x & 15;
            int j = z & 15;
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int k = startHeight; k >= 0; --k)
            {
                mutablePos.setPos(i, k, j);
                if (!chunkIn.getBlockState(mutablePos).isAir())
                {
                    Block block = chunkIn.getBlockState(mutablePos).getBlock();
                    if (k == 62 && block != defaultFluid.getBlock() && block == Blocks.WATER)
                        chunkIn.setBlockState(mutablePos, defaultFluid, false);
                    break;
                }
            }

            SurfaceBuilder.SWAMP.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);
        }
    }
}
