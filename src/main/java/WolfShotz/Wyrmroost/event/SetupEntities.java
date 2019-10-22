package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.renderer.ButterflyLeviathanRenderer;
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
import WolfShotz.Wyrmroost.util.entityhelpers.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.util.entityhelpers.multipart.MultiPartRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Objects;
import java.util.Set;

import static net.minecraftforge.common.BiomeDictionary.Type;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 *
 * Class responsible for the setup and registration of entities, and their spawning.
 */
@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEntities
{
    private static final String ID = Wyrmroost.MOD_ID + ":";
    
    // Entity List Start
    @ObjectHolder(ID + "overworld_drake")       public static EntityType<OWDrakeEntity>             overworldDrake;
    @ObjectHolder(ID + "minutus")               public static EntityType<MinutusEntity>             minutus;
    @ObjectHolder(ID + "silver_glider")         public static EntityType<SilverGliderEntity>        silverGlider;
    @ObjectHolder(ID + "roost_stalker")         public static EntityType<RoostStalkerEntity>        roostStalker;
    @ObjectHolder(ID + "butterfly_leviathan")   public static EntityType<ButterflyLeviathanEntity>  butterflyLeviathan;
    @ObjectHolder(ID + "dragon_fruit_drake")    public static EntityType<DragonFruitDrakeEntity>    dragonFruitDrake;
    
    @ObjectHolder(ID + "dragon_egg")            public static EntityType<DragonEggEntity>           dragonEgg;
    @ObjectHolder(ID + "multipart_entity")      public static EntityType<MultiPartEntity>           multiPartEntity;
    // Entity List End
    
    /**
     * Method called in before item registry event to instatiate these fields.
     * It is important that these fields are populated BEFORE item registration so spawn eggs are registered properly.
     * TODO Not ideal. use this until forge reevaluates
     */
    public static void buildEntities() {
        overworldDrake     = buildEntity("overworld_drake", OWDrakeEntity::new, EntityClassification.CREATURE, 2.376f, 2.45f);
        minutus            = buildEntity("minutus", MinutusEntity::new, EntityClassification.CREATURE, 0.6f, 0.2f);
        silverGlider       = buildEntity("silver_glider", SilverGliderEntity::new, EntityClassification.CREATURE, 1.5f, 0.75f);
        roostStalker       = buildEntity("roost_stalker", RoostStalkerEntity::new, EntityClassification.CREATURE, 0.65f, 0.5f);
        butterflyLeviathan = buildEntity("butterfly_leviathan", ButterflyLeviathanEntity::new, EntityClassification.CREATURE, 4f, 3f);
        dragonFruitDrake   = buildEntity("dragon_fruit_drake", DragonFruitDrakeEntity::new, EntityClassification.CREATURE, 1.5f, 1.4f);
    
        dragonEgg          = buildEntity("dragon_egg", EntityType.Builder
                                                               .<DragonEggEntity>create(DragonEggEntity::new, EntityClassification.MISC)
                                                               .disableSummoning()
                                                               .setCustomClientFactory(DragonEggEntity::new)
        );
        multiPartEntity    = buildEntity("multipart_entity", EntityType.Builder
                                                                     .<MultiPartEntity>create(EntityClassification.MISC)
                                                                     .immuneToFire()
                                                                     .disableSummoning()
                                                                     .disableSerialization()
                                                                     .setShouldReceiveVelocityUpdates(false)
                                                                     .setCustomClientFactory(MultiPartEntity::new)
        );
    }
    
    /**
     * Registers World Spawning for entities
     */
    private static void registerEntityWorldSpawns() {
        registerSpawnEntry(overworldDrake, getDrakeBiomes(), 8, 1, 3);
        registerSpawning(minutus, 35, 1, 1, getMinutusBiomes());
        registerSpawnEntry(silverGlider, getSilverGliderBiomes(), 2, 2, 5, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverGliderEntity::canSpawnHere);
        registerSpawnEntry(roostStalker, getStalkerBiomes(), 9, 3, 18);
    }

    /**
     * Registers Model Rendering and animation for entities
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(MultiPartEntity.class, MultiPartRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(DragonEggEntity.class, DragonEggRenderer::new);
        
        RenderingRegistry.registerEntityRenderingHandler(OWDrakeEntity.class, OWDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MinutusEntity.class, MinutusRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SilverGliderEntity.class, SilverGliderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RoostStalkerEntity.class, RoostStalkerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ButterflyLeviathanEntity.class, ButterflyLeviathanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DragonFruitDrakeEntity.class, DragonFruitDrakeRenderer::new);
        
    }

    @SubscribeEvent
    public static void entitySetup(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll (
                dragonEgg,

                overworldDrake,
                minutus,
                silverGlider,
                roostStalker,
                butterflyLeviathan,
                dragonFruitDrake,
                
                multiPartEntity
        );
        registerEntityWorldSpawns();
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

    // ================================
    //   SetupEntity Helper Functions
    // ================================

    /**
     * Helper Function that turns this stupidly long line into something more nicer to look at
     */
    private static <T extends Entity> EntityType<T> buildEntity(String name, EntityType.IFactory<T> entity, EntityClassification classify, float width, float height) {
        EntityType<T> builder = EntityType.Builder.create(entity, classify).size(width, height).build(Wyrmroost.MOD_ID + ":" + name);
        builder.setRegistryName(name);
        return builder;
    }
    
    /**
     * Builder w/out size
     */
    private static <T extends Entity> EntityType<T> buildEntity(String name, EntityType.IFactory<T> entity, EntityClassification classify) {
        EntityType<T> builder = EntityType.Builder.create(entity, classify).build(Wyrmroost.MOD_ID + ":" + name);
        builder.setRegistryName(name);
        return builder;
    }
    
    private static <T extends Entity> EntityType<T> buildEntity(String name, EntityType.Builder<T> customBuilder) {
        EntityType<T> type = customBuilder.build(name);
        type.setRegistryName(name);
        return type;
    }
    
    private static <T extends AbstractDragonEntity> void registerSpawnEntry(EntityType<T> entity, Set<Biome> biomes, int frequency, int minAmount, int maxAmount) {
        registerSpawning(entity, frequency, minAmount, maxAmount, biomes);
        registerGenericSpawnEntry(entity);
    }
    
    /**
     * Helper method allowing for easier entity world spawning setting
     */
    private static <T extends AbstractDragonEntity> void registerSpawnEntry(EntityType<T> entity, Set<Biome> biomes, int frequency, int minAmount, int maxAmount, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, EntitySpawnPlacementRegistry.IPlacementPredicate canSpawnHere) {
        registerSpawning(entity, frequency, minAmount, maxAmount, biomes);
        EntitySpawnPlacementRegistry.register(entity, placementType, heightMapType, canSpawnHere);
    }
    
    /**
     * Helper method allowing for easier entity world spawning setting
     */
    private static void registerSpawning(EntityType<?> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes) {
        biomes.stream()
                .filter(Objects::nonNull)
                .forEach(biome -> biome
                                          .getSpawns(entity.getClassification())
                                          .add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount)));
    }
    
    private static <T extends AbstractDragonEntity> void registerGenericSpawnEntry(EntityType<T> entity) {
        EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractDragonEntity::canSpawnHere);
    }

}
