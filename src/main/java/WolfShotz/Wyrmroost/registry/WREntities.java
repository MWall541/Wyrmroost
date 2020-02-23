package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.CanariWyvernEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggRenderer;
import WolfShotz.Wyrmroost.util.entityutils.multipart.MultiPartEntity;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraftforge.common.BiomeDictionary.Type;
import static net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 * <p>
 * Class responsible for the setup and registration of entities, and their spawning.
 */
public class WREntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<EntityType<MinutusEntity>> MINUTUS = register("minutus", buildCreatureEntity(MinutusEntity::new).size(0.6f, 0.2f));
    public static final RegistryObject<EntityType<OWDrakeEntity>> OVERWORLD_DRAKE = register("overworld_drake", buildCreatureEntity(OWDrakeEntity::new).size(2.376f, 2.45f));
    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER = register("silver_glider", buildCreatureEntity(SilverGliderEntity::new).size(1.5f, 0.75f));
    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER = register("roost_stalker", buildCreatureEntity(RoostStalkerEntity::new).size(0.65f, 0.5f));
    public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN = register("butterfly_leviathan", EntityType.Builder.create(ButterflyLeviathanEntity::new, EntityClassification.WATER_CREATURE).size(4f, 3f));
    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE = register("dragon_fruit_drake", buildCreatureEntity(DragonFruitDrakeEntity::new).size(1.5f, 2.5f));
    public static final RegistryObject<EntityType<CanariWyvernEntity>> CANARI_WYVERN = register("canari_wyvern", buildCreatureEntity(CanariWyvernEntity::new).size(0.45f, 1f));
    
    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG = register("dragon_egg", EntityType.Builder.create(DragonEggEntity::new, EntityClassification.MISC).disableSummoning());
    
    public static final RegistryObject<EntityType<MultiPartEntity>> MULTIPART = register("multipart_entity", EntityType.Builder.<MultiPartEntity>create(MultiPartEntity::new, EntityClassification.MISC).disableSummoning().disableSerialization().setShouldReceiveVelocityUpdates(false));
    
    /**
     * Registers World Spawning for entities
     */
    public static void registerEntityWorldSpawns()
    {
        registerSpawnEntry(OVERWORLD_DRAKE.get(), 8, 1, 3, getByTypes(Type.SANDY, Type.PLAINS));
        registerSpawnEntry(MINUTUS.get(), 35, 1, 1, getByTypes(Type.SANDY).stream().filter(b -> !BiomeDictionary.hasType(b, Type.MESA)).collect(Collectors.toSet()));
        registerCustomSpawnEntry(SILVER_GLIDER.get(), 2, 2, 5, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverGliderEntity::canSpawnHere, getByTypes(Type.BEACH, Type.PLAINS));
        registerSpawnEntry(ROOSTSTALKER.get(), 9, 3, 18, getByTypes(Type.FOREST, Type.PLAINS, Type.MOUNTAIN));
        registerCustomSpawnEntry(DRAGON_FRUIT_DRAKE.get(), 100, 1, 3, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, DragonFruitDrakeEntity::canSpawnHere, BiomeDictionary.getBiomes(Type.JUNGLE));
        registerSpawnEntry(BUTTERFLY_LEVIATHAN.get(), 1, 1, 1, Sets.newHashSet(WRBiomes.STYGIAN_SEA.get()));
    }
    
    /**
     * Registers Model Rendering and animation for entities
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders()
    {
        registerEntityRenderingHandler(OWDrakeEntity.class, OWDrakeRenderer::new);
        registerEntityRenderingHandler(MinutusEntity.class, MinutusRenderer::new);
        registerEntityRenderingHandler(SilverGliderEntity.class, SilverGliderRenderer::new);
        registerEntityRenderingHandler(RoostStalkerEntity.class, RoostStalkerRenderer::new);
        registerEntityRenderingHandler(ButterflyLeviathanEntity.class, ButterflyLeviathanRenderer::new);
        registerEntityRenderingHandler(DragonFruitDrakeEntity.class, DragonFruitDrakeRenderer::new);
        registerEntityRenderingHandler(CanariWyvernEntity.class, CanariWyvernRenderer::new);
        
        registerEntityRenderingHandler(DragonEggEntity.class, DragonEggRenderer::new);
        
        registerEntityRenderingHandler(MultiPartEntity.class, mgr -> new EntityRenderer<MultiPartEntity>(mgr)
        {
            protected ResourceLocation getEntityTexture(MultiPartEntity entity) { return null; }
        });
    }

    // ================================
    //   SetupEntity Helper Functions
    // ================================

    private static <T extends Entity> EntityType.Builder<T> buildCreatureEntity(EntityType.IFactory<T> entity)
    {
        return EntityType.Builder.create(entity, EntityClassification.CREATURE);
    }

    private static <T extends MobEntity> void registerSpawnEntry(EntityType<T> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes)
    {
        registerBiomeSpawnEntry(entity, frequency, minAmount, maxAmount, biomes);
        EntitySpawnPlacementRegistry.register(entity,
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                (type, world, reason, blockPos, rng) -> // Allow Spawning on any block that is lit and above sea level
                        blockPos.getY() > world.getSeaLevel() - 20 && world.getLightSubtracted(blockPos, 0) > 8);
    }

    /**
     * Helper method allowing for easier entity world spawning setting
     */
    private static <T extends MobEntity> void registerCustomSpawnEntry(EntityType<T> entity, int frequency, int minAmount, int maxAmount, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, EntitySpawnPlacementRegistry.IPlacementPredicate canSpawnHere, Set<Biome> biomes)
    {
        registerBiomeSpawnEntry(entity, frequency, minAmount, maxAmount, biomes);
        EntitySpawnPlacementRegistry.register(entity, placementType, heightMapType, canSpawnHere);
    }

    /**
     * Helper method allowing for easier entity world spawning setting
     */
    private static void registerBiomeSpawnEntry(EntityType<?> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes)
    {
        biomes.forEach(biome -> biome.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount)));
    }

    /**
     * Group all biomes into one set according to their BiomeDictionary Types
     */
    private static Set<Biome> getByTypes(Type... types)
    {
        Set<Biome> biomes = Sets.newHashSet();
        for (Type type : types) biomes.addAll(BiomeDictionary.getBiomes(type));
        return biomes;
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> type)
    {
        return ENTITIES.register(name, () -> type.build(Wyrmroost.MOD_ID + ":" + name));
    }
}
