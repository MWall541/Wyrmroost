//package WolfShotz.Wyrmroost.registry;
//
//import WolfShotz.Wyrmroost.Wyrmroost;
//import WolfShotz.Wyrmroost.content.world.biomes.AshDesertBiome;
//import WolfShotz.Wyrmroost.content.world.biomes.CausticSwampBiome;
//import WolfShotz.Wyrmroost.content.world.biomes.FrostCrevasseBiome;
//import WolfShotz.Wyrmroost.content.world.biomes.StygianSeaBiome;
//import net.minecraft.world.biome.Biome;
//import net.minecraftforge.fml.RegistryObject;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import java.util.function.Supplier;
//
//public class WRBiomes
//{
//    public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, Wyrmroost.MOD_ID);
//
//    public static final RegistryObject<Biome> ASH_DESERT = register("ash_desert", AshDesertBiome::new);
//    public static final RegistryObject<Biome> STYGIAN_SEA = register("stygian_sea", StygianSeaBiome::new);
//    public static final RegistryObject<Biome> FROST_CREVASSE = register("frost_crevasse", FrostCrevasseBiome::new);
//    public static final RegistryObject<Biome> CAUSTIC_SWAMP = register("caustic_swamp", CausticSwampBiome::new);
//
//    public static RegistryObject<Biome> register(String name, Supplier<Biome> biome)
//    {
//        return BIOMES.register(name, biome);
//    }
//}
