package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render.ButterflyLeviathanRenderer;
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
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.entityhelpers.multipart.MultiPartEntity;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 *
 * Class responsible for the setup and registration of entities, and their spawning.
 */
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<EntityType<MinutusEntity>> MINUTUS                           = register("minutus", buildCreatureEntity(MinutusEntity::new).size(0.6f, 0.2f));
    public static final RegistryObject<EntityType<OWDrakeEntity>> OVERWORLD_DRAKE                   = register("overworld_drake", buildCreatureEntity(OWDrakeEntity::new).size(2.376f, 2.45f));
    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER                = register("silver_glider", buildCreatureEntity(SilverGliderEntity::new).size(1.5f, 0.75f));
    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER                 = register("roost_stalker", buildCreatureEntity(RoostStalkerEntity::new).size(0.65f, 0.5f));
    public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN    = register("butterfly_leviathan", EntityType.Builder.create(ButterflyLeviathanEntity::new, EntityClassification.WATER_CREATURE).size(4f, 3f));
    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE       = register("dragon_fruit_drake", buildCreatureEntity(DragonFruitDrakeEntity::new).size(1.5f, 2.5f));
    
    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG                      = register("dragon_egg", EntityType.Builder.create(DragonEggEntity::new, EntityClassification.MISC).disableSummoning());
    
    public static final RegistryObject<EntityType<MultiPartEntity>> MULTIPART                       = register("multipart_entity", EntityType.Builder.<MultiPartEntity>create(EntityClassification.MISC).disableSummoning().disableSerialization().setShouldReceiveVelocityUpdates(false));
    
    /**
     * Registers World Spawning for entities
     */
    public static void registerEntityWorldSpawns() {
        registerSpawnEntry(OVERWORLD_DRAKE.get(), getDrakeBiomes(), 8, 1, 3);
        registerSpawnEntry(MINUTUS.get(), getMinutusBiomes(), 35, 1, 1);
        registerCustomSpawnEntry(SILVER_GLIDER.get(), getSilverGliderBiomes(), 2, 2, 5, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverGliderEntity::canSpawnHere);
        registerSpawnEntry(ROOSTSTALKER.get(), getStalkerBiomes(), 9, 3, 18);
        registerCustomSpawnEntry(DRAGON_FRUIT_DRAKE.get(), getDFDBiomes(), 5, 1, 3, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DragonFruitDrakeEntity::canSpawnHere);
    }

    /**
     * Registers Model Rendering and animation for entities
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(OWDrakeEntity.class, OWDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MinutusEntity.class, MinutusRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SilverGliderEntity.class, SilverGliderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RoostStalkerEntity.class, RoostStalkerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ButterflyLeviathanEntity.class, ButterflyLeviathanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DragonFruitDrakeEntity.class, DragonFruitDrakeRenderer::new);
    
        RenderingRegistry.registerEntityRenderingHandler(DragonEggEntity.class, DragonEggRenderer::new);
    
        RenderingRegistry.registerEntityRenderingHandler(MultiPartEntity.class, mgr -> new EntityRenderer<MultiPartEntity>(mgr) {protected ResourceLocation getEntityTexture(MultiPartEntity entity) {return null;}});
    }

    // ================================
    //   Entity Biome Spawn Locations
    // ================================
    // Use Biome Dictionary for overworld
    // dragons for compabitility with
    // custom biomes.
    //
    // Dimension dragons will have spawns
    // in our custom biomes, so they wont be
    // needing this.

    private static Set<Biome> getDrakeBiomes() {
        return ModUtils.collectAll(
                BiomeDictionary.getBiomes(Type.SAVANNA),
                BiomeDictionary.getBiomes(Type.PLAINS)
        );
    }
    
    private static Set<Biome> getSilverGliderBiomes() {
        return ModUtils.collectAll(
                BiomeDictionary.getBiomes(Type.BEACH),
                BiomeDictionary.getBiomes(Type.OCEAN)
        );
    }
    
    private static Set<Biome> getStalkerBiomes() {
        return ModUtils.collectAll(
                BiomeDictionary.getBiomes(Type.FOREST),
                BiomeDictionary.getBiomes(Type.PLAINS),
                BiomeDictionary.getBiomes(Type.MOUNTAIN)
        );
    }
    
    private static Set<Biome> getMinutusBiomes() {
        Set<Biome> biomes = Sets.newHashSet();
        for (Biome biome : BiomeDictionary.getBiomes(Type.SANDY))
            if (!BiomeDictionary.hasType(biome, Type.MESA)) biomes.add(biome);
        
        return biomes;
    }
    
    private static Set<Biome> getDFDBiomes() {
        return BiomeDictionary.getBiomes(Type.JUNGLE);
    }

    // ================================
    //   SetupEntity Helper Functions
    // ================================
    
    private static <T extends Entity> EntityType.Builder<T> buildCreatureEntity(EntityType.IFactory<T> entity) {
        return EntityType.Builder.create(entity, EntityClassification.CREATURE);
    }
    
    private static <T extends MobEntity> void registerSpawnEntry(EntityType<T> entity, Set<Biome> biomes, int frequency, int minAmount, int maxAmount) {
        registerBiomeSpawnEntry(entity, frequency, minAmount, maxAmount, biomes);
        registerGenericSpawnPlacement(entity);
    }
    
    /**
     * Helper method allowing for easier entity world spawning setting
     */
    private static <T extends MobEntity> void registerCustomSpawnEntry(EntityType<T> entity, Set<Biome> biomes, int frequency, int minAmount, int maxAmount, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, EntitySpawnPlacementRegistry.IPlacementPredicate canSpawnHere) {
        registerBiomeSpawnEntry(entity, frequency, minAmount, maxAmount, biomes);
        EntitySpawnPlacementRegistry.register(entity, placementType, heightMapType, canSpawnHere);
    }
    
    /**
     * Helper method allowing for easier entity world spawning setting
     */
    private static void registerBiomeSpawnEntry(EntityType<?> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes) {
        biomes.stream()
                .filter(Objects::nonNull)
                .forEach(biome -> biome.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount)));
    }
    
    private static <T extends MobEntity> void registerGenericSpawnPlacement(EntityType<T> entity) {
        EntitySpawnPlacementRegistry.register(entity,
                                              EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                                              Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                              (type, world, reason, blockPos, rng) -> // Allow Spawning on any block that is lit and above sea level
                                                      blockPos.getY() > world.getSeaLevel() - 20 && world.getLightSubtracted(blockPos, 0) > 8
        );
    }
    
    
    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> type) {
        return ENTITIES.register(name, () -> type.build(Wyrmroost.MOD_ID + ":" + name));
    }
}
