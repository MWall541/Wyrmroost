package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.canari.CanariEntity;
import WolfShotz.Wyrmroost.content.entities.minutus.MinutusEntity;
import WolfShotz.Wyrmroost.content.entities.minutus.MinutusRenderer;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.content.entities.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by WolfShotz - 7/3/19 19:03 <p>
 *
 * Class responsible for the setup and registration of entities, and their spawning.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntitySetup
{
    // Entity List Start
    @ObjectHolder(Wyrmroost.modID + ":overworld_drake")
    public static EntityType<?> overworld_drake = ModUtils.buildEntity("overworld_drake", OWDrakeEntity::new, EntityClassification.CREATURE, 2.376f, 2.45f);
    @ObjectHolder(Wyrmroost.modID + ":minutus")
    public static EntityType<?> minutus = ModUtils.buildEntity("minutus", MinutusEntity::new, EntityClassification.CREATURE, 0.6f, 0.2f);
    @ObjectHolder(Wyrmroost.modID + ":silver_glider")
    public static EntityType<?> silver_glider = ModUtils.buildEntity("silver_glider", SilverGliderEntity::new, EntityClassification.CREATURE, 3f, 0.8f);
/*
    @ObjectHolder(Wyrmroost.modID + ":canari")
    public static EntityType<?> canari = ModUtils.buildEntity("canari", CanariEntity::new, EntityClassification.CREATURE, 1.5f, 1.5f);
*/
    // Entity List End

    /** Registers World Spawning for entities */
    public static void registerEntityWorldSpawns() {
        ModUtils.registerSpawning(overworld_drake, 10, 1, 3, EntityLocations.getDrake());
        ModUtils.registerSpawning(minutus, 15, 1, 1, BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY));
    }

    /** Registers Model Rendering and animation for entities */
    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenders() {
        ModUtils.registerRender(OWDrakeEntity.class, OWDrakeRenderer::new);
        ModUtils.registerRender(MinutusEntity.class, MinutusRenderer::new);
    }

    @SubscribeEvent
    public static void entitySetup(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll (
                overworld_drake,
                minutus,
                silver_glider
//                canari
        );
        registerEntityWorldSpawns();

        ModUtils.L.info("Entity Setup Complete");
    }

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
