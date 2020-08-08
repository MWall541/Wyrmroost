package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.CommonEvents;
import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.entity.alpine.AlpineRenderer;
import WolfShotz.Wyrmroost.client.render.entity.butterfly.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.client.render.entity.canari.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.client.render.entity.coin_dragon.CoinDragonRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_fruit.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.ldwyrm.LDWyrmRenderer;
import WolfShotz.Wyrmroost.client.render.entity.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import WolfShotz.Wyrmroost.client.render.entity.projectile.GeodeTippedArrowRenderer;
import WolfShotz.Wyrmroost.client.render.entity.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.client.render.entity.royal_red.RoyalRedRenderer;
import WolfShotz.Wyrmroost.client.render.entity.silverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.entities.dragon.*;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import WolfShotz.Wyrmroost.entities.projectile.breath.FireBreathEntity;
import WolfShotz.Wyrmroost.items.LazySpawnEggItem;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, Wyrmroost.MOD_ID);

    public static final RegistryObject<EntityType<LDWyrmEntity>> LESSER_DESERTWYRM = Builder.creature("lesser_desertwyrm", LDWyrmEntity::new)
            .spawnEgg(0xD6BCBC, 0xDEB6C7)
            .renderer(LDWyrmRenderer::new)
            .spawnPlacement(LDWyrmEntity.getSpawnPlacements())
            .build(b -> b.size(0.6f, 0.2f));

    public static final RegistryObject<EntityType<OWDrakeEntity>> OVERWORLD_DRAKE = Builder.creature("overworld_drake", OWDrakeEntity::new)
            .spawnEgg(0x788716, 0x3E623E)
            .dragonEgg(new DragonEggProperties(0.65f, 1f, 18000))
            .renderer(OWDrakeRenderer::new)
            .spawnPlacement(t -> basicSpawnConditions(t, 8, 1, 3, ModUtils.getBiomesByTypes(Type.SAVANNA, Type.PLAINS)))
            .build(b -> b.size(2.376f, 2.58f));

    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER = Builder.creature("silver_glider", SilverGliderEntity::new)
            .spawnEgg(0xC8C8C8, 0xC4C4C4)
            .dragonEgg(new DragonEggProperties(0.4f, 0.65f, 12000))
            .renderer(SilverGliderRenderer::new)
            .spawnPlacement(SilverGliderEntity.getSpawnPlacements())
            .build(b -> b.size(1.5f, 0.75f));

    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER = Builder.creature("roost_stalker", RoostStalkerEntity::new)
            .spawnEgg(0x52100D, 0x959595)
            .dragonEgg(new DragonEggProperties(0.25f, 0.35f, 6000))
            .renderer(RoostStalkerRenderer::new)
            .spawnPlacement(t -> basicSpawnConditions(t, 7, 2, 9, ModUtils.getBiomesByTypes(Type.FOREST, Type.PLAINS, Type.MOUNTAIN)))
            .build(b -> b.size(0.65f, 0.5f));

    public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN = Builder.withClassification("butterfly_leviathan", ButterflyLeviathanEntity::new, EntityClassification.WATER_CREATURE)
            .spawnEgg(0x17283C, 0x7A6F5A)
            .dragonEgg(new DragonEggProperties(0.75f, 1.25f, 40000).setConditions(Entity::isInWater))
            .renderer(ButterflyLeviathanRenderer::new)
            .spawnPlacement(ButterflyLeviathanEntity.getSpawnConditions())
            .build(b -> b.size(4f, 3f));

    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE = Builder.creature("dragon_fruit_drake", DragonFruitDrakeEntity::new)
            .spawnEgg(0xe05c9a, 0x788716)
            .dragonEgg(new DragonEggProperties(0.45f, 0.75f, 9600))
            .renderer(DragonFruitDrakeRenderer::new)
            .spawnPlacement(DragonFruitDrakeEntity.getSpawnConditions())
            .build(b -> b.size(1.5f, 1.9f));

    public static final RegistryObject<EntityType<CanariWyvernEntity>> CANARI_WYVERN = Builder.creature("canari_wyvern", CanariWyvernEntity::new)
            .spawnEgg(0x1D1F28, 0x492E0E)
            .dragonEgg(new DragonEggProperties(0.25f, 0.35f, 6000).setConditions(c -> c.world.getBlockState(c.getPosition().down()).getBlock() == Blocks.JUNGLE_LEAVES))
            .renderer(CanariWyvernRenderer::new)
            .spawnPlacement(t -> basicSpawnConditions(t, 9, 2, 5, BiomeDictionary.getBiomes(Type.SWAMP)))
            .build(b -> b.size(0.7f, 0.85f));

    public static final RegistryObject<EntityType<RoyalRedEntity>> ROYAL_RED = Builder.creature("royal_red", RoyalRedEntity::new)
            .spawnEgg(0x8a0900, 0x0)
            .dragonEgg(new DragonEggProperties(0.6f, 1f, 72000))
            .renderer(RoyalRedRenderer::new)
            .build(b -> b.size(3f, 3.9f).immuneToFire());

    public static final RegistryObject<EntityType<CoinDragonEntity>> COIN_DRAGON = Builder.creature("coin_dragon", CoinDragonEntity::new)
            .renderer(CoinDragonRenderer::new)
            .build(b -> b.size(0.35f, 0.435f));

    public static final RegistryObject<EntityType<AlpineEntity>> ALPINE = Builder.creature("alpine", AlpineEntity::new)
            .spawnEgg(0xe3f8ff, 0xa8e9ff)
            .dragonEgg(new DragonEggProperties(1, 1, 12000))
            .renderer(AlpineRenderer::new)
            .build(b -> b.size(2f, 2f));

    public static final RegistryObject<EntityType<GeodeTippedArrowEntity>> GEODE_TIPPED_ARROW = Builder.<GeodeTippedArrowEntity>withClassification("geode_tipped_arrow", GeodeTippedArrowEntity::new, EntityClassification.MISC)
            .renderer(GeodeTippedArrowRenderer::new)
            .build(b -> b.size(0.5f, 0.5f));

    public static final RegistryObject<EntityType<FireBreathEntity>> FIRE_BREATH = Builder.<FireBreathEntity>withClassification("fire_breath", FireBreathEntity::new, EntityClassification.MISC)
            .renderer(BreathWeaponRenderer::new)
            .build(b -> b.size(0.75f, 0.75f).disableSerialization().disableSummoning());

    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG = Builder.<DragonEggEntity>withClassification("dragon_egg", DragonEggEntity::new, EntityClassification.MISC)
            .renderer(DragonEggRenderer::new)
            .build(EntityType.Builder::disableSummoning);

//    public static final RegistryObject<EntityType<MultiPartEntity>> MULTIPART = register("multipart_entity", MultiPartEntity::new, EntityClassification.MISC, b -> b.disableSummoning().disableSerialization().setShouldReceiveVelocityUpdates(false));

    private static <T extends MobEntity> void basicSpawnConditions(EntityType<T> entity, int frequency, int minAmount, int maxAmount, Set<Biome> biomes)
    {
        for (Biome b : biomes)
            b.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount));
        EntitySpawnPlacementRegistry.register(entity,
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MobEntity::canSpawnOn);
    }

    // todo in 1.16: Attributes
    private static class Builder<T extends Entity>
    {
        private final String name;
        private final EntityType.IFactory<T> factory;
        private final EntityClassification classification;
        private final RegistryObject<EntityType<T>> registered;

        public Builder(String name, EntityType.IFactory<T> factory, EntityClassification classification)
        {
            this.name = name;
            this.factory = factory;
            this.classification = classification;
            this.registered = RegistryObject.of(Wyrmroost.rl(name), ForgeRegistries.ENTITIES);
        }

        private Builder<T> spawnEgg(int primColor, int secColor)
        {
            WRItems.register(name + "_egg", () -> new LazySpawnEggItem(registered::get, primColor, secColor));
            return this;
        }

        private Builder<T> renderer(IRenderFactory<T> renderFactory)
        {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> ClientEvents.CALLBACKS.add(() -> RenderingRegistry.registerEntityRenderingHandler(registered.get(), renderFactory)));
            return this;
        }

        /**
         * Just a consumer, tho it is ran at FMLCommonSetupEvent,
         * so it is advisable to take advantage by using this for spawning logic
         */
        private Builder<T> spawnPlacement(Consumer<EntityType<T>> consumer)
        {
            CommonEvents.CALLBACKS.add(() -> consumer.accept(registered.get()));
            return this;
        }

        private Builder<T> dragonEgg(DragonEggProperties props)
        {
            CommonEvents.CALLBACKS.add(() -> DragonEggProperties.MAP.put(registered.get(), props));
            return this;
        }

        private RegistryObject<EntityType<T>> build(Consumer<EntityType.Builder<T>> consumer)
        {
            EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
            consumer.accept(builder);
            return REGISTRY.register(name, () -> builder.build(Wyrmroost.MOD_ID + ":" + name));
        }

        private static <T extends Entity> Builder<T> creature(String name, EntityType.IFactory<T> factory)
        {
            return new Builder<>(name, factory, EntityClassification.CREATURE);
        }

        private static <T extends Entity> Builder<T> withClassification(String name, EntityType.IFactory<T> factory, EntityClassification classification)
        {
            return new Builder<>(name, factory, classification);
        }
    }
}
