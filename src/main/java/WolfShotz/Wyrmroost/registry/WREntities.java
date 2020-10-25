package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.CommonEvents;
import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.entity.EmptyRenderer;
import WolfShotz.Wyrmroost.client.render.entity.alpine.AlpineRenderer;
import WolfShotz.Wyrmroost.client.render.entity.butterfly.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.client.render.entity.canari.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.client.render.entity.coin_dragon.CoinDragonRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_fruit.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.ldwyrm.LDWyrmRenderer;
import WolfShotz.Wyrmroost.client.render.entity.orbwyrm.OrbwyrmRenderer;
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
import WolfShotz.Wyrmroost.entities.projectile.WindGustEntity;
import WolfShotz.Wyrmroost.entities.projectile.breath.FireBreathEntity;
import WolfShotz.Wyrmroost.items.LazySpawnEggItem;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS;
import static net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType.ON_GROUND;

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
            .attributes(LDWyrmEntity::getAttributes)
            .spawnPlacement(ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LDWyrmEntity::getSpawnPlacement)
            .spawnBiomes(LDWyrmEntity::setSpawnBiomes)
            .spawnEgg(0xD6BCBC, 0xDEB6C7)
            .renderer(() -> LDWyrmRenderer::new)
            .build(b -> b.size(0.6f, 0.2f));

    public static final RegistryObject<EntityType<OWDrakeEntity>> OVERWORLD_DRAKE = Builder.creature("overworld_drake", OWDrakeEntity::new)
            .attributes(OWDrakeEntity::getAttributes)
            .spawnPlacement()
            .spawnBiomes(OWDrakeEntity::setSpawnBiomes)
            .spawnEgg(0x788716, 0x3E623E)
            .dragonEgg(new DragonEggProperties(0.65f, 1f, 18000))
            .renderer(() -> OWDrakeRenderer::new)
            .build(b -> b.size(2.376f, 2.58f));

    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER = Builder.creature("silver_glider", SilverGliderEntity::new)
            .attributes(SilverGliderEntity::getAttributes)
            .spawnPlacement(NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SilverGliderEntity::getSpawnPlacement)
            .spawnBiomes(SilverGliderEntity::setSpawnBiomes)
            .spawnEgg(0xC8C8C8, 0xC4C4C4)
            .dragonEgg(new DragonEggProperties(0.4f, 0.65f, 12000))
            .renderer(() -> SilverGliderRenderer::new)
            .build(b -> b.size(1.5f, 0.75f));

    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER = Builder.creature("roost_stalker", RoostStalkerEntity::new)
            .attributes(RoostStalkerEntity::getAttributes)
            .spawnPlacement()
            .spawnBiomes(RoostStalkerEntity::setSpawnBiomes)
            .spawnEgg(0x52100D, 0x959595)
            .dragonEgg(new DragonEggProperties(0.25f, 0.35f, 6000))
            .renderer(() -> RoostStalkerRenderer::new)
            .build(b -> b.size(0.65f, 0.5f));

    public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN = Builder.withClassification("butterfly_leviathan", ButterflyLeviathanEntity::new, EntityClassification.WATER_CREATURE)
            .attributes(ButterflyLeviathanEntity::getAttributes)
            .spawnPlacement(NO_RESTRICTIONS, Heightmap.Type.OCEAN_FLOOR_WG, ButterflyLeviathanEntity::getSpawnPlacement)
            .spawnBiomes(ButterflyLeviathanEntity::setSpawnBiomes)
            .spawnEgg(0x17283C, 0x7A6F5A)
            .dragonEgg(new DragonEggProperties(0.75f, 1.25f, 40000).setConditions(Entity::isInWater))
            .renderer(() -> ButterflyLeviathanRenderer::new)
            .build(b -> b.size(4f, 3f));

    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE = Builder.creature("dragon_fruit_drake", DragonFruitDrakeEntity::new)
            .attributes(DragonFruitDrakeEntity::getAttributes)
            .spawnPlacement(ON_GROUND, Heightmap.Type.MOTION_BLOCKING, AnimalEntity::canAnimalSpawn)
            .spawnBiomes(DragonFruitDrakeEntity::setSpawnBiomes)
            .spawnEgg(0xe05c9a, 0x788716)
            .dragonEgg(new DragonEggProperties(0.45f, 0.75f, 9600))
            .renderer(() -> DragonFruitDrakeRenderer::new)
            .build(b -> b.size(1.5f, 1.9f));

    public static final RegistryObject<EntityType<CanariWyvernEntity>> CANARI_WYVERN = Builder.creature("canari_wyvern", CanariWyvernEntity::new)
            .attributes(CanariWyvernEntity::getAttributes)
            .spawnPlacement(NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, AbstractDragonEntity::canFlyerSpawn)
            .spawnBiomes(CanariWyvernEntity::setSpawnBiomes)
            .spawnEgg(0x1D1F28, 0x492E0E)
            .dragonEgg(new DragonEggProperties(0.25f, 0.35f, 6000).setConditions(c -> c.world.getBlockState(c.getPosition().down()).getBlock() == Blocks.JUNGLE_LEAVES))
            .renderer(() -> CanariWyvernRenderer::new)
            .build(b -> b.size(0.65f, 0.85f));

    public static final RegistryObject<EntityType<RoyalRedEntity>> ROYAL_RED = Builder.creature("royal_red", RoyalRedEntity::new)
            .attributes(RoyalRedEntity::getAttributes)
            .spawnPlacement(NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, AbstractDragonEntity::canFlyerSpawn)
            .spawnBiomes(RoyalRedEntity::setSpawnBiomes)
            .spawnEgg(0x8a0900, 0x0)
            .dragonEgg(new DragonEggProperties(0.6f, 1f, 72000))
            .renderer(() -> RoyalRedRenderer::new)
            .build(b -> b.size(3f, 3.9f).immuneToFire());

    public static final RegistryObject<EntityType<CoinDragonEntity>> COIN_DRAGON = Builder.creature("coin_dragon", CoinDragonEntity::new)
            .renderer(() -> CoinDragonRenderer::new)
            .attributes(CoinDragonEntity::getAttributes)
            .build(b -> b.size(0.35f, 0.435f));

    public static final RegistryObject<EntityType<AlpineEntity>> ALPINE = Builder.creature("alpine", AlpineEntity::new)
            .attributes(AlpineEntity::getAttributes)
            .spawnPlacement(NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, AbstractDragonEntity::canFlyerSpawn)
            .spawnBiomes(AlpineEntity::setSpawnBiomes)
            .spawnEgg(0xe3f8ff, 0xa8e9ff)
            .dragonEgg(new DragonEggProperties(1, 1, 12000))
            .renderer(() -> AlpineRenderer::new)
            .build(b -> b.size(2f, 2f));

    public static final RegistryObject<EntityType<OrbwyrmEntity>> ORBWYRM = Builder.creature("orbwyrm", OrbwyrmEntity::new)
            .attributes(OrbwyrmEntity::getAttributes)
//            .spawnPlacement()
//            .spawnBiomes(OWDrakeEntity::setSpawnBiomes)
            .spawnEgg(0x41444F, 0x16171C)
//            .dragonEgg(new DragonEggProperties(0.65f, 1f, 18000))
            .renderer(() -> OrbwyrmRenderer::new)
            .build(b -> b.size(2.8f, 3.76f));


    public static final RegistryObject<EntityType<GeodeTippedArrowEntity>> GEODE_TIPPED_ARROW = Builder.<GeodeTippedArrowEntity>withClassification("geode_tipped_arrow", GeodeTippedArrowEntity::new, EntityClassification.MISC)
            .renderer(() -> GeodeTippedArrowRenderer::new)
            .build(b -> b.size(0.5f, 0.5f).setCustomClientFactory(GeodeTippedArrowEntity::new));

    public static final RegistryObject<EntityType<FireBreathEntity>> FIRE_BREATH = Builder.<FireBreathEntity>withClassification("fire_breath", FireBreathEntity::new, EntityClassification.MISC)
            .renderer(() -> BreathWeaponRenderer::new)
            .build(b -> b.size(0.75f, 0.75f).disableSerialization().disableSummoning());

    public static final RegistryObject<EntityType<WindGustEntity>> WIND_GUST = Builder.<WindGustEntity>withClassification("wind_gust", WindGustEntity::new, EntityClassification.MISC)
            .renderer(() -> EmptyRenderer::new)
            .build(b -> b.size(4, 4).disableSerialization().disableSummoning());

    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG = Builder.<DragonEggEntity>withClassification("dragon_egg", DragonEggEntity::new, EntityClassification.MISC)
            .renderer(() -> DragonEggRenderer::new)
            .build(b -> b.disableSummoning().setCustomClientFactory(DragonEggEntity::new));

    @SuppressWarnings("unchecked")
    private static class Builder<T extends Entity>
    {
        private final String name;
        private final EntityType.IFactory<T> factory;
        private final EntityClassification classification;
        private RegistryObject<EntityType<T>> registered;

        public Builder(String name, EntityType.IFactory<T> factory, EntityClassification classification)
        {
            this.name = name;
            this.factory = factory;
            this.classification = classification;
        }

        private Builder<T> spawnEgg(int primColor, int secColor)
        {
            WRItems.register(name + "_spawn_egg", () -> new LazySpawnEggItem(registered::get, primColor, secColor));
            return this;
        }

        private Builder<T> renderer(Supplier<IRenderFactory<T>> renderFactory)
        {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> ClientEvents.CALLBACKS.add(() -> RenderingRegistry.registerEntityRenderingHandler(registered.get(), renderFactory.get())));
            return this;
        }

        private Builder<T> attributes(Supplier<AttributeModifierMap.MutableAttribute> map)
        {
            try
            {
                CommonEvents.CALLBACKS.add(() -> GlobalEntityTypeAttributes.put((EntityType<? extends LivingEntity>) registered.get(), map.get().create()));
            }
            catch (ClassCastException e)
            {
                throw new ClassCastException("Entity Attributes cannot be Applied to non-living! Erroring Entity: " + name);
            }
            return this;
        }

        private <F extends MobEntity> Builder<T> spawnPlacement(EntitySpawnPlacementRegistry.PlacementType type, Heightmap.Type height, EntitySpawnPlacementRegistry.IPlacementPredicate<F> predicate)
        {
            try
            {
                CommonEvents.CALLBACKS.add(() -> EntitySpawnPlacementRegistry.register((EntityType<F>) registered.get(), type, height, predicate));
            }
            catch (ClassCastException e)
            {
                throw new ClassCastException("Entity Spawn Placement cannot be applied to non-mob! Erroring Entity: " + name);
            }
            return this;
        }

        private Builder<T> spawnPlacement()
        {
            return spawnPlacement(ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
        }

        private Builder<T> spawnBiomes(Consumer<BiomeLoadingEvent> consumer)
        {
            WRWorld.BIOME_LISTENERS.add(consumer);
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
            return registered = REGISTRY.register(name, () -> builder.build(Wyrmroost.MOD_ID + ":" + name));
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

    public static class Attributes
    {
        public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Wyrmroost.MOD_ID);

        public static final RegistryObject<Attribute> PROJECTILE_DAMAGE = ranged("generic.projectileDamage", 2d, 0, 2048d);

        private static RegistryObject<Attribute> ranged(String name, double defaultValue, double min, double max)
        {
            return register(name.toLowerCase().replace('.', '_'), () -> new RangedAttribute("attribute.name." + name, defaultValue, min, max));
        }

        private static RegistryObject<Attribute> register(String name, Supplier<Attribute> attribute)
        {
            return REGISTRY.register(name, attribute);
        }
    }
}
