package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.world.dimension.WyrmroostDimension;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupWorld
{
    // =========
    // Dimension
    // =========
 
    @ObjectHolder(Wyrmroost.MOD_ID + ":dim_wyrmroost")
    public static final ModDimension DIM_WYRMROOST = null;
    
    @SubscribeEvent
    public static void dimensionSetup(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().register (
                new ModDimension() { // Why.
                    @Override
                    public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
                        return WyrmroostDimension::new;
                    }
                }.setRegistryName("dim_wyrmroost")
        );
    }
    
    // ==============
    // Ore Generation
    // ==============
    
    private static final Set<Biome> OVERWORLD_CONFIG = new HashSet<>(ModUtils.collectAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.END), BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER)));
    private static final Predicate<Biome> OVERWORLD_FILTER = biome -> OVERWORLD_CONFIG.stream().noneMatch(biome::equals);
    
    private static final CountRangeConfig PLATINUM_CONFIG = new CountRangeConfig(2, 0, 0, 28);
    private static final CountRangeConfig GEODE_CONFIG = new CountRangeConfig(1, 0, 0, 20);

    public static void setupOreGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (OVERWORLD_FILTER.test(biome)) { // Filter Ores so they dont gen in the nether or end
                registerOreEntry(biome, SetupBlocks.geodeOreBlock.getDefaultState(), 8, GEODE_CONFIG);
                registerOreEntry(biome, SetupBlocks.platinumOreBlock.getDefaultState(), 9, PLATINUM_CONFIG);
            }
        }
    }

    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     * Takes in the biome, ore blockstate, ore size and the chance configuration as params.
     */
    private static void registerOreEntry(Biome biome, BlockState state, int size, CountRangeConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, size),
                        Placement.COUNT_RANGE,
                        config
                ));
    }
}
