package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
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

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 *
 * Class responsible for the setup and registration of entities, and their spawning.
 */
@Mod.EventBusSubscriber(modid = Wyrmroost.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEntities
{
    // Entity List Start
    @ObjectHolder(Wyrmroost.modID + ":overworld_drake")
    public static EntityType overworld_drake = buildEntity("overworld_drake", OWDrakeEntity::new, EntityClassification.CREATURE, 2.376f, 2.45f);
    
    @ObjectHolder(Wyrmroost.modID + ":minutus")
    public static EntityType minutus = buildEntity("minutus", MinutusEntity::new, EntityClassification.CREATURE, 0.6f, 0.2f);
    
    @ObjectHolder(Wyrmroost.modID + ":silver_glider")
    public static EntityType silver_glider = buildEntity("silver_glider", SilverGliderEntity::new, EntityClassification.CREATURE, 1.5f, 0.75f);
    
    @ObjectHolder(Wyrmroost.modID + ":roost_stalker")
    public static EntityType roost_stalker = buildEntity("roost_stalker", RoostStalkerEntity::new, EntityClassification.CREATURE, 0.65f, 0.5f);
    // Entity List End
    
    /**
     * Registers World Spawning for entities
     */
    private static void registerEntityWorldSpawns() {
        registerSpawning(overworld_drake, 10, 1, 3, getDrakeBiomes());
        registerSpawning(minutus, 35, 1, 1, BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY));
        registerSpawning(silver_glider, 5, 2, 5, getSilverGliderBiomes());
        registerSpawning(roost_stalker, 10, 3, 18, getStalkerBiomes());
        EntitySpawnPlacementRegistry.register(silver_glider, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverGliderEntity::canSpawnHere);
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
    }

    @SubscribeEvent
    public static void entitySetup(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll (
                overworld_drake,
                minutus,
                silver_glider,
                roost_stalker
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
                BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA),
                BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS)
        );
    }
    
    private static Set<Biome> getSilverGliderBiomes() {
        return ModUtils.collectAll(
                BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH),
                BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN)
        );
    }
    
    private static Set<Biome> getStalkerBiomes() {
        return ModUtils.collectAll(
                BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
                BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
                BiomeDictionary.getBiomes(BiomeDictionary.Type.MOUNTAIN)
        );
    }

    // ================================
    //   SetupEntity Helper Functions
    // ================================

    /**
     * Helper Function that turns this stupidly long line into something more nicer to look at
     */
    private static <T extends Entity> EntityType<?> buildEntity(String name, EntityType.IFactory<T> entity, EntityClassification classify, float width, float height) {
        return EntityType.Builder
                       .create(entity, classify)
                       .size(width, height)
                       .build(Wyrmroost.modID + ":" + name)
                       .setRegistryName(name);
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

}
