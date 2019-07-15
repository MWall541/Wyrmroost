package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.setup.SetupRegistryEvents;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 *
 * Class responsible for the setup and registration of entities, and their spawning.
 * @see SetupRegistryEvents SetupRegistryEvents
 */
public class EntitySetup
{
    // Entity List Start
    public static final EntityType<?> overworld_drake = registerEntity("overworld_drake", OWDrakeEntity::new, EntityClassification.CREATURE, 2.376f, 2.45f);
    // Entity List End

    /** Registers World Spawning for entities */
    public static void registerEntityWorldSpawns() {
        registerSpawning(overworld_drake, 10, 1, 3, EntityLocations.getDrake());
    }


    /** Registers Model Rendering and animation for entities */
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders() {
        registerrender(OWDrakeEntity.class, OWDrakeRenderer::new);
    }


    // =========================================
    //      EntitySetup Helper Functions
    // =========================================

    /** Helper method for easier entity rendering registration */
    @OnlyIn(Dist.CLIENT)
    private static <B extends Entity> void registerrender(Class<B> entity, IRenderFactory factory)
        { RenderingRegistry.registerEntityRenderingHandler(entity, factory); }

    /** Helper method allowing for easier entity world spawning setting */
    private static void registerSpawning(EntityType<?> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes) {
        biomes.stream()
                .filter(Objects::nonNull)
                .forEach(biome -> biome.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount)));
    }

    /** Helper Function that turns this stupidly long line into something more nicer to look at */
    private static <T extends Entity> EntityType<?> registerEntity(String name, EntityType.IFactory<T> entity, EntityClassification classify, float x, float y)
        { return EntityType.Builder.create(entity, classify).size(x, y).build(Wyrmroost.modID + ":" + name).setRegistryName(name); }

    /** Immutable List containing all entity elements. Iterated in registryevents for cleaner registration */
    public static List<EntityType<?>> ENTITIES = ImmutableList.of(
            overworld_drake
    );

    /**
     * Inner Class storing entity spawn locations.
     * Overworld Dragons will use BiomeDictionary for compatibility with custom biomes. <p>
     * Iterated in {@link EntitySetup#registerEntityWorldSpawns()}
     */
    private static class EntityLocations
    {
        private static Set<Biome> getDrake() {
            Set<Biome> drakeSpawns = new HashSet<>();
            drakeSpawns.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
            drakeSpawns.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
            return drakeSpawns;
        }
    }

}
