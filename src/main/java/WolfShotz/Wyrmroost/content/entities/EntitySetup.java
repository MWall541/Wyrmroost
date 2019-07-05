package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.overworlddrake.OverworldDrake;
import WolfShotz.Wyrmroost.setup.SetupRegistryEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 *
 * Class responsible for the setup and registration of entities, and their spawning.
 * @see SetupRegistryEvents SetupRegistryEvents
 */
public class EntitySetup
{
    /** List used to "group" entities for easier registration in {@link SetupRegistryEvents SetupRegistryEvents} */
    public static List<EntityType<?>> ENTITIES = new ArrayList();

    // Entity List Start
    public static final EntityType<?> overworld_drake = registerEntity("overworld_drake", OverworldDrake::new, EntityClassification.CREATURE);
    // Entity List End

    /** Handles spawning for Entities. Called in {@link SetupRegistryEvents SetupRegistryEvents}. */
    public static void registerEntityWorldSpawns() {
        registerSpawning(overworld_drake, 10, 1, 5, Biomes.PLAINS, Biomes.FOREST);
    }


    /** Helper method allowing for easier entity registration */
    private static <T extends Entity> EntityType<?> registerEntity(String name, EntityType.IFactory<T> entity, EntityClassification classify) {
        return EntityType.Builder.create(entity, classify).build(Wyrmroost.modID + ":" + name).setRegistryName(name);
    }

    /** Helper Method that adds all entity elements into our List Collection. Called in {@link SetupRegistryEvents SetupRegistryEvents}. */
    public static void collectEntities() {
        ENTITIES.add(overworld_drake);
    }

    /** Helper method allowing for easier entity world spawning setting */
    private static void registerSpawning(EntityType<?> entity, int frequency, int minAmount, int maxAmount, Biome... biomes) {
        for (Biome biome : biomes) if (biome != null) {
            biome.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount));
        }
    }
}
