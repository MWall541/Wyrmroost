package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.Smelt;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static WolfShotz.Wyrmroost.registry.WRBlocks.*;

public class LootTables extends LootTableProvider
{
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = ImmutableList.of(
            Pair.of(BlockLoot::new, LootParameterSets.BLOCK),
            Pair.of(EntityLoot::new, LootParameterSets.ENTITY)
    );

    public LootTables(DataGenerator gen)
    {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return tables;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {}

    public static class BlockLoot extends BlockLootTables
    {
        public static final LootFunction.Builder<?> FORTUNE = ApplyBonus.uniformBonusCount(Enchantments.FORTUNE);

        public final Map<Block, LootTable.Builder> lootTables = Maps.newHashMap();

        @Override
        @SuppressWarnings("ConstantConditions")
        protected void addTables()
        {
//            registerLootTable(ASH.get(), block -> droppingWithSilkTouchOrRandomly(block, WRItems.ASH_PILE.get(), RandomValueRange.of(3f, 5f)));

            registerOre(BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get());
            registerOre(RED_GEODE_ORE.get(), WRItems.RED_GEODE.get());
            registerOre(PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get());

//            registerOre(BLUE_CRYSTAL_ORE.get(), WRItems.BLUE_SHARD.get());
//            registerLootTable(BLUE_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.BLUE_SHARD.get(), 1).acceptFunction(FORTUNE))));
//
//            registerOre(GREEN_CRYSTAL_ORE.get(), WRItems.GREEN_SHARD.get());
//            registerLootTable(GREEN_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.GREEN_SHARD.get(), 1).acceptFunction(FORTUNE))));
//
//            registerOre(ORANGE_CRYSTAL_ORE.get(), WRItems.ORANGE_SHARD.get());
//            registerLootTable(ORANGE_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.ORANGE_SHARD.get(), 1).acceptFunction(FORTUNE))));
//
//            registerOre(YELLOW_CRYSTAL_ORE.get(), WRItems.YELLOW_SHARD.get());
//            registerLootTable(YELLOW_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.YELLOW_SHARD.get(), 1).acceptFunction(FORTUNE))));
//
//            registerSilkTouch(CANARI_LEAVES.get());

            // All blocks that have not been given special treatment above, drop themselves!
            for (Block block : getKnownBlocks())
            {
                if (!lootTables.containsKey(block) && block.getLootTable() != net.minecraft.world.storage.loot.LootTables.EMPTY) // Loottable is already set to not have one, ignore.
                    registerDropSelfLootTable(block);
            }
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
        {
            addTables();

            for (Block block : getKnownBlocks())
            {
                ResourceLocation loot = block.getLootTable();
                if (loot == net.minecraft.world.storage.loot.LootTables.EMPTY)
                    continue; // Loottable is already set to not have one, ignore.
                if (!lootTables.containsKey(block))
                    throw new IllegalStateException(String.format("Missing loottable '%s' for '%s', How the fuck did this happen?", loot, Registry.BLOCK.getKey(block)));
                consumer.accept(loot, lootTables.remove(block));
            }

            if (!lootTables.isEmpty())
                throw new IllegalStateException("Created block loot tables for non-blocks: " + lootTables.keySet());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() { return ModUtils.getRegistryEntries(BLOCKS); }

        private void registerOre(Block ore, Item output)
        {
            registerLootTable(ore, block -> droppingItemWithFortune(block, output));
        }

        @Override
        protected void registerLootTable(Block blockIn, LootTable.Builder table) { lootTables.put(blockIn, table); }
    }

    public static class EntityLoot extends EntityLootTables
    {
        private static final EntityPredicate.Builder ON_FIRE = EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true).build());
        private static final LootFunction.Builder<?> FIRE_CONDITION = Smelt.func_215953_b().acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE));

        private final Map<EntityType<?>, LootTable.Builder> lootTables = Maps.newHashMap();

        private static LootPool.Builder lootTable()
        {
            return LootPool.builder().rolls(ConstantRange.of(1));
        }

        /**
         * Our way is much neater and cooler anyway. fuck mojang
         */
        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
        {
            addTables();

            for (EntityType<?> entity : getKnownEntities())
            {
                if (!lootTables.containsKey(entity))
                {
                    if (entity.getClassification() == EntityClassification.MISC) continue;
                    throw new IllegalArgumentException(String.format("Missing Loottable for entry: '%s'", entity.getRegistryName()));
                }
                consumer.accept(entity.getLootTable(), lootTables.remove(entity));
            }
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities()
        {
            return ModUtils.getRegistryEntries(WREntities.ENTITIES);
        }

        /**
         * @param types the types to register an empty loot tables
         * @deprecated SHOULD ONLY USE THIS WHEN AN ENTITY ABSOLUTELY DOES NOT HAVE ONE, OR IS UNDECIDED!
         */
        public void registerEmptyTables(EntityType<?>... types)
        {
            for (EntityType<?> type : types)
            {
                ModUtils.L.warn("Registering EMPTY Loottable for: '{}'", type.getRegistryName());
                registerLootTable(type, LootTable.builder());
            }
        }

        @Override
        protected void registerLootTable(EntityType<?> type, LootTable.Builder table)
        {
            lootTables.put(type, table);
        }

        private static LootingEnchantBonus.Builder looting(float min, float max)
        {
            return LootingEnchantBonus.builder(RandomValueRange.of(min, max));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, float minIn, float maxIn)
        {
            return ItemLootEntry.builder(itemIn).acceptFunction(SetCount.builder(RandomValueRange.of(minIn, maxIn)));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, int amount)
        {
            return ItemLootEntry.builder(itemIn).acceptFunction(SetCount.builder(ConstantRange.of(amount)));
        }

        private static LootPool.Builder meat(IItemProvider itemIn, int minAmount, int maxAmount, int lootingMin, int lootingMax)
        {
            return lootTable().addEntry(item(itemIn, minAmount, maxAmount)
                    .acceptFunction(FIRE_CONDITION)
                    .acceptFunction(looting(lootingMin, lootingMax)));
        }

        @Override
        protected void addTables()
        {
            // TODO Silver glider, Butterfly Leviathan
            registerEmptyTables(WREntities.SILVER_GLIDER.get(), WREntities.BUTTERFLY_LEVIATHAN.get());

            registerLootTable(WREntities.LESSER_DESERTWYRM.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(WRItems.LDWYRM.get(), 1).acceptFunction(FIRE_CONDITION)))
            );
            registerLootTable(WREntities.OVERWORLD_DRAKE.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(Items.LEATHER, 1f, 16f).acceptFunction(looting(1f, 4f))))
                    .addLootPool(meat(WRItems.RAW_COMMON_MEAT.get(), 2, 6, 1, 4))
                    .addLootPool(lootTable().addEntry(ItemLootEntry.builder(WRItems.DRAKE_BACKPLATE.get())).acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.65f, 0.03f)))
            );
            registerLootTable(WREntities.ROOSTSTALKER.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(Items.GOLD_NUGGET, 0f, 4f).acceptFunction(looting(1f, 3f))))
                    .addLootPool(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 2, 1, 2))
            );
            registerLootTable(WREntities.DRAGON_FRUIT_DRAKE.get(), LootTable.builder().addLootPool(meat(WRItems.RAW_LOWTIER_MEAT.get(), 1, 2, 0, 2)));
            registerLootTable(WREntities.CANARI_WYVERN.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(Items.FEATHER, 0f, 3f).acceptFunction(looting(0f, 2f))))
                    .addLootPool(meat(WRItems.RAW_COMMON_MEAT.get(), 0, 2, 0, 2))
            );
            registerLootTable(WREntities.SILVER_GLIDER.get(), LootTable.builder().addLootPool(meat(WRItems.RAW_LOWTIER_MEAT.get(), 1, 4, 2, 3)));
        }
    }
}
