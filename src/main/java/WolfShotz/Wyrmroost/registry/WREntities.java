package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.*;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 * <p>
 * Class responsible for the setup and registration of entities, and their spawning.
 * Entity type generics are defined because a) forge told me so and b) because its broken without it.
 */
public class WREntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Wyrmroost.MOD_ID);

    public static final RegistryObject<EntityType<LDWyrmEntity>> LESSER_DESERTWYRM = register("lesser_desertwyrm", 0xD6BCBC, 0xDEB6C7, creature(LDWyrmEntity::new).size(0.6f, 0.2f));
    public static final RegistryObject<EntityType<OWDrakeEntity>> OVERWORLD_DRAKE = register("overworld_drake", 0x788716, 0x3E623E, creature(OWDrakeEntity::new).size(2.376f, 2.45f));
    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER = register("silver_glider", 0xC8C8C8, 0xC4C4C4, creature(SilverGliderEntity::new).size(1.5f, 0.75f));
    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER = register("roost_stalker", 0x52100D, 0x959595, creature(RoostStalkerEntity::new).size(0.65f, 0.5f));
    public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN = register("butterfly_leviathan", 0x17283C, 0x7A6F5A, EntityType.Builder.create(ButterflyLeviathanEntity::new, EntityClassification.WATER_CREATURE).size(4f, 3f));
    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE = register("dragon_fruit_drake", 0xe05c9a, 0x788716, creature(DragonFruitDrakeEntity::new).size(1.5f, 1.9f));
    public static final RegistryObject<EntityType<CanariWyvernEntity>> CANARI_WYVERN = register("canari_wyvern", 0x1D1F28, 0x492E0E, creature(CanariWyvernEntity::new).size(0.7f, 0.85f));

    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG = register("dragon_egg", EntityType.Builder.<DragonEggEntity>create(DragonEggEntity::new, EntityClassification.MISC).disableSummoning());

    public static final RegistryObject<EntityType<MultiPartEntity>> MULTIPART = register("multipart_entity", EntityType.Builder.<MultiPartEntity>create(MultiPartEntity::new, EntityClassification.MISC).disableSummoning().disableSerialization().setShouldReceiveVelocityUpdates(false));

    /**
     * Registers World Spawning for entities
     */
    public static void registerEntityWorldSpawns()
    {
        // OW
        basicSpawnConditions(OVERWORLD_DRAKE.get(), 8, 1, 3, ModUtils.getBiomesByTypes(Type.SAVANNA, Type.PLAINS));
        LDWyrmEntity.setSpawnConditions();
        SilverGliderEntity.setSpawnConditions();
        basicSpawnConditions(ROOSTSTALKER.get(), 7, 3, 13, ModUtils.getBiomesByTypes(Type.FOREST, Type.PLAINS, Type.MOUNTAIN));
        DragonFruitDrakeEntity.setSpawnConditions();

        ButterflyLeviathanEntity.setSpawnConditions();
        basicSpawnConditions(CANARI_WYVERN.get(), 9, 2, 5, BiomeDictionary.getBiomes(Type.SWAMP));
    }

    // ================================
    //   SetupEntity Helper Functions
    // ================================

    private static <T extends Entity> EntityType.Builder<T> creature(EntityType.IFactory<T> entity)
    {
        return EntityType.Builder.create(entity, EntityClassification.CREATURE);
//                .setTrackingRange(80) created some weird limbSwing problems, look into it later
//                .setUpdateInterval(4)
//                .setShouldReceiveVelocityUpdates(true);
    }

    private static <T extends MobEntity> void basicSpawnConditions(EntityType<T> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes)
    {
        biomes.forEach(b -> b.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount)));
        EntitySpawnPlacementRegistry.register(entity,
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MobEntity::canSpawnOn);
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> type)
    {
        return ENTITIES.register(name, () -> type.build(Wyrmroost.MOD_ID + ":" + name));
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, int primColor, int secColor, EntityType.Builder<T> type)
    {
        RegistryObject<EntityType<T>> object = register(name, type);
        WRItems.register(name + "_egg", () -> new CustomSpawnEggItem(object::get, primColor, secColor));
        return object;
    }
}
