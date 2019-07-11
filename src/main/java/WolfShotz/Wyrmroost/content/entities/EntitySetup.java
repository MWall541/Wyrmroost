package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.setup.SetupRegistryEvents;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

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
    public static List<EntityType<?>> ENTITIES = new ArrayList();

    // Entity List Start
    public static final EntityType<?> overworld_drake = registerEntity("overworld_drake", OWDrakeEntity::new, EntityClassification.CREATURE);
    // Entity List End

    private static <T extends Entity> EntityType<?> registerEntity(String name, EntityType.IFactory<T> entity, EntityClassification classify)
        { return EntityType.Builder.create(entity, classify).build(Wyrmroost.modID + ":" + name).setRegistryName(name); }

    /**
     * Helper Method that adds all entity elements into a List Collection.
     * Called in {@link SetupRegistryEvents SetupRegistryEvents}.
     */
    public static void collectEntities() {
        ENTITIES.add(overworld_drake);
    }


    public static void registerEntityWorldSpawns() {
        registerSpawning(overworld_drake, 10, 1, 5, Biomes.PLAINS, Biomes.FOREST);
    }

    /** Helper method allowing for easier entity world spawning setting */
    private static void registerSpawning(EntityType<?> entity, int frequency, int minAmount, int maxAmount, Biome... biomes) {
        for (Biome biome : biomes) if (biome != null)
            biome.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount));
    }


    /** Handles Model Rendering and animation for entities */
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders() {
        regrender(OWDrakeEntity.class, OWDrakeRenderer::new);
    }

    /** Helper method for easier entity rendering registration */
    @OnlyIn(Dist.CLIENT)
    private static <B extends Entity> void regrender(Class<B> entity, IRenderFactory factory) {
        RenderingRegistry.registerEntityRenderingHandler(entity, factory);
    }

}
