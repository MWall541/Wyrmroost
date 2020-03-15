package WolfShotz.Wyrmroost.util.world;

import WolfShotz.Wyrmroost.registry.WRBiomes;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import net.minecraftforge.common.BiomeManager;

import java.util.List;
import java.util.stream.Collectors;

public class WRBiomeLayer implements IC0Transformer
{
    private static final int MUSHROOM_FIELDS = Registry.BIOME.getId(Biomes.MUSHROOM_FIELDS);
    private static final int WARM_OCEAN = Registry.BIOME.getId(Biomes.WARM_OCEAN);
    private static final int LUKEWARM_OCEAN = Registry.BIOME.getId(Biomes.LUKEWARM_OCEAN);
    private static final int OCEAN = Registry.BIOME.getId(Biomes.OCEAN);
    private static final int COLD_OCEAN = Registry.BIOME.getId(Biomes.COLD_OCEAN);
    private static final int FROZEN_OCEAN = Registry.BIOME.getId(Biomes.FROZEN_OCEAN);
    private static final int DEEP_WARM_OCEAN = Registry.BIOME.getId(Biomes.DEEP_WARM_OCEAN);
    private static final int DEEP_LUKEWARM_OCEAN = Registry.BIOME.getId(Biomes.DEEP_LUKEWARM_OCEAN);
    private static final int DEEP_OCEAN = Registry.BIOME.getId(Biomes.DEEP_OCEAN);
    private static final int DEEP_COLD_OCEAN = Registry.BIOME.getId(Biomes.DEEP_COLD_OCEAN);
    private static final int DEEP_FROZEN_OCEAN = Registry.BIOME.getId(Biomes.DEEP_FROZEN_OCEAN);

    private final OverworldGenSettings settings;
    private final List<BiomeManager.BiomeEntry> biomeEntries;

    public WRBiomeLayer(OverworldGenSettings settings)
    {
        this.settings = settings;
        this.biomeEntries = setupEntries();
    }

    protected static boolean isOcean(int biomeIn)
    {
        return biomeIn == WARM_OCEAN
                || biomeIn == LUKEWARM_OCEAN
                || biomeIn == OCEAN
                || biomeIn == COLD_OCEAN
                || biomeIn == FROZEN_OCEAN
                || biomeIn == DEEP_WARM_OCEAN
                || biomeIn == DEEP_LUKEWARM_OCEAN
                || biomeIn == DEEP_OCEAN
                || biomeIn == DEEP_COLD_OCEAN
                || biomeIn == DEEP_FROZEN_OCEAN;
    }

    @Override
    public int apply(INoiseRandom context, int value)
    {
        if (this.settings != null && this.settings.getBiomeId() >= 0)
        {
            return this.settings.getBiomeId();
        }
        else
        {
            value = value & -3841;
            if (!isOcean(value) && value != MUSHROOM_FIELDS)
            {
                int totalWeight = WeightedRandom.getTotalWeight(biomeEntries);
                int weight = context.random(totalWeight / 10) * 10;
                return Registry.BIOME.getId(WeightedRandom.getRandomItem(biomeEntries, weight).biome);
            }
            else
            {
                return value;
            }
        }
    }

    public List<BiomeManager.BiomeEntry> setupEntries()
    {
        return ModUtils.getRegistryEntries(WRBiomes.BIOMES)
                .stream()
                .map(WRBiome.class::cast)
                .map(b -> new BiomeManager.BiomeEntry(b, b.getWeight()))
                .collect(Collectors.toList());
    }
}
