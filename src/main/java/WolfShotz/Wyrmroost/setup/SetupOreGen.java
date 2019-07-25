package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class SetupOreGen
{
    private static CountRangeConfig platinumConfig = new CountRangeConfig(2, 0, 0, 25);
    private static CountRangeConfig geodeConfig = new CountRangeConfig(1, 0, 0, 16);

    public static void setupOreGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            ModUtils.registerOreEntry(biome, BlockSetup.blockgeodeore.getDefaultState(), 8, geodeConfig);
            ModUtils.registerOreEntry(biome, BlockSetup.blockplatinumore.getDefaultState(), 9, platinumConfig);
        }
    }
}
