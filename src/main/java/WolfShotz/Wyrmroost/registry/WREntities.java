package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.*;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import WolfShotz.Wyrmroost.entities.projectile.breath.FireBreathEntity;
import WolfShotz.Wyrmroost.entities.projectile.breath.RRBreathEntity;
import WolfShotz.Wyrmroost.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.items.GeodeTippedArrowItem;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;

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

    public static final RegistryObject<EntityType<LDWyrmEntity>> LESSER_DESERTWYRM = register("lesser_desertwyrm", LDWyrmEntity::new, 0xD6BCBC, 0xDEB6C7, b -> b.size(0.6f, 0.2f));
    public static final RegistryObject<EntityType<OWDrakeEntity>> OVERWORLD_DRAKE = register("overworld_drake", OWDrakeEntity::new, 0x788716, 0x3E623E, b -> b.size(2.376f, 2.58f));
    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER = register("silver_glider", SilverGliderEntity::new, 0xC8C8C8, 0xC4C4C4, b -> b.size(1.5f, 0.75f));
    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER = register("roost_stalker", RoostStalkerEntity::new, 0x52100D, 0x959595, b -> b.size(0.65f, 0.5f));
    public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN = register("butterfly_leviathan", ButterflyLeviathanEntity::new, EntityClassification.WATER_CREATURE, 0x17283C, 0x7A6F5A, b -> b.size(4f, 3f));
    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE = register("dragon_fruit_drake", DragonFruitDrakeEntity::new, 0xe05c9a, 0x788716, b -> b.size(1.5f, 1.9f));
    public static final RegistryObject<EntityType<CanariWyvernEntity>> CANARI_WYVERN = register("canari_wyvern", CanariWyvernEntity::new, 0x1D1F28, 0x492E0E, b -> b.size(0.7f, 0.85f));
    public static final RegistryObject<EntityType<RoyalRedEntity>> ROYAL_RED = register("royal_red", RoyalRedEntity::new, 0x8a0900, 0x0, b -> b.size(3f, 3.9f).immuneToFire());

    public static final RegistryObject<EntityType<GeodeTippedArrowEntity>> BLUE_GEODE_ARROW = register("blue_geode_tipped_arrow", (t, w) -> new GeodeTippedArrowEntity(t, 3d, (GeodeTippedArrowItem) WRItems.BLUE_GEODE_ARROW.get(), w), EntityClassification.MISC, b -> b.size(0.5f, 0.5f));
    public static final RegistryObject<EntityType<GeodeTippedArrowEntity>> RED_GEODE_ARROW = register("red_geode_tipped_arrow", (t, w) -> new GeodeTippedArrowEntity(t, 3.5d, (GeodeTippedArrowItem) WRItems.RED_GEODE_ARROW.get(), w), EntityClassification.MISC, b -> b.size(0.5f, 0.5f));
    public static final RegistryObject<EntityType<GeodeTippedArrowEntity>> PURPLE_GEODE_ARROW = register("purple_geode_tipped_arrow", (t, w) -> new GeodeTippedArrowEntity(t, 4d, (GeodeTippedArrowItem) WRItems.PURPLE_GEODE_ARROW.get(), w), EntityClassification.MISC, b -> b.size(0.5f, 0.5f));

    public static final RegistryObject<EntityType<FireBreathEntity>> FIRE_BREATH = register("fire_breath", FireBreathEntity::new, EntityClassification.MISC, b -> b.size(1f, 1f).disableSerialization().disableSummoning());
    public static final RegistryObject<EntityType<FireBreathEntity>> ROYAL_RED_BREATH = register("royal_red_breath", RRBreathEntity::new, EntityClassification.MISC, b -> b.size(1f, 1f).disableSerialization().disableSummoning());

    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG = register("dragon_egg", DragonEggEntity::new, EntityClassification.MISC, EntityType.Builder::disableSummoning);

    public static final RegistryObject<EntityType<MultiPartEntity>> MULTIPART = register("multipart_entity", MultiPartEntity::new, EntityClassification.MISC, b -> b.disableSummoning().disableSerialization().setShouldReceiveVelocityUpdates(false));

    /**
     * Registers World Spawning for entities
     */
    public static void registerEntityWorldSpawns()
    {
        // OW
        basicSpawnConditions(OVERWORLD_DRAKE.get(), 8, 1, 3, ModUtils.getBiomesByTypes(Type.SAVANNA, Type.PLAINS));
        LDWyrmEntity.setSpawnConditions();
        SilverGliderEntity.setSpawnConditions();
        basicSpawnConditions(ROOSTSTALKER.get(), 7, 2, 9, ModUtils.getBiomesByTypes(Type.FOREST, Type.PLAINS, Type.MOUNTAIN));
        DragonFruitDrakeEntity.setSpawnConditions();

        ButterflyLeviathanEntity.setSpawnConditions();
        basicSpawnConditions(CANARI_WYVERN.get(), 9, 2, 5, BiomeDictionary.getBiomes(Type.SWAMP));
    }

    // ================================
    //   SetupEntity Helper Functions
    // ================================

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

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.IFactory<T> entity, EntityClassification classification, int primColor, int secColor, @Nullable Consumer<EntityType.Builder<T>> consumer)
    {
        EntityType.Builder<T> builder = EntityType.Builder.create(entity, classification);
        if (consumer != null) consumer.accept(builder);
        RegistryObject<EntityType<T>> object = register(name, builder);
        WRItems.register(name + "_egg", () -> new CustomSpawnEggItem(object::get, primColor, secColor));
        return object;
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.IFactory<T> entity, int primColor, int secColor, @Nullable Consumer<EntityType.Builder<T>> consumer)
    {
        return register(name, entity, EntityClassification.CREATURE, primColor, secColor, consumer);
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.IFactory<T> entity, EntityClassification classification, @Nullable Consumer<EntityType.Builder<T>> consumer)
    {
        EntityType.Builder<T> builder = EntityType.Builder.create(entity, classification);
        if (consumer != null) consumer.accept(builder);
        return register(name, builder);
    }
}
