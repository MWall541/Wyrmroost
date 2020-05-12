package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class WRWorld
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Wyrmroost.MOD_ID);

//    public static final RegistryObject<Feature<NoFeatureConfig>> CANARI_TREE_FEATURE = feature("canari_tree_structure", () -> new TreeFeature(NoFeatureConfig::deserialize, true, 3, WRBlocks.CANARI_LOG.get().getDefaultState(), WRBlocks.CANARI_LEAVES.get().getDefaultState(), false));

//    public static final RegistryObject<Structure<NoFeatureConfig>> CANARI_TREE_STRUCTURE = feature("canari_tree", CanariTreeStructure::new);

    // Structure Pieces are not a forge registry, so we don't have to be IMC slave bitches and register on vanilla accord:
//    public static final IStructurePieceType CANARI_TREE_PIECE = structurePiece("canari_tree", CanariTreePiece::new);


    private static final Predicate<Biome> NETHER_FILTER = biome -> BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER).stream().anyMatch(biome::equals);
    private static final Predicate<Biome> END_FILTER = biome -> BiomeDictionary.getBiomes(BiomeDictionary.Type.END).stream().anyMatch(biome::equals);

    // =======================================

    private static <C extends IFeatureConfig, T extends Feature<C>> RegistryObject<T> feature(String name, Supplier<T> feature)
    {
        return FEATURES.register(name, feature);
    }

    private static IStructurePieceType structurePiece(String name, IStructurePieceType piece)
    {
        return Registry.register(Registry.STRUCTURE_PIECE, Wyrmroost.rl(name), piece);
    }

    public static void setupWorldGen()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            ore(biome);
        }
    }

    public static void ore(Biome biome)
    {
        if (NETHER_FILTER.test(biome)) // Nether Ores
        {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                    .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WRBlocks.RED_GEODE_ORE.get().getDefaultState(), 8))
                    .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 128))));
            return;
        }

        if (END_FILTER.test(biome)) // End Ores
        {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.EMERALD_ORE
                    .withConfiguration(new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), WRBlocks.PURPLE_GEODE_ORE.get().getDefaultState()))
                    .withPlacement(Placement.RANDOM_COUNT_RANGE.configure(new CountRangeConfig(16, 0, 0, 128))));
            return;
        }
        // Any other biome not tagged for end or nether
        registerOreEntry(biome, WRBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 8, 1, 0, 0, 20);
        registerOreEntry(biome, WRBlocks.PLATINUM_ORE.get().getDefaultState(), 9, 2, 0, 0, 28);
    }

    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     */
    private static void registerOreEntry(Biome biome, BlockState state, int size, int count, int bottomOffset, int topOffset, int maximum)
    {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, size))
                .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(count, bottomOffset, topOffset, maximum))));
    }
}
