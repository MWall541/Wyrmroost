package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.Smelt;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static WolfShotz.Wyrmroost.registry.WRBlocks.*;

class LootTableData extends LootTableProvider
{
    LootTableData(DataGenerator gen) { super(gen); }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootParameterSets.BLOCK),
                Pair.of(Entities::new, LootParameterSets.ENTITY)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {}

    private static class Blocks extends BlockLootTables
    {
        public final Map<Block, LootTable.Builder> lootTables = Maps.newHashMap();

        @Override
        @SuppressWarnings("ConstantConditions")
        protected void addTables()
        {
            registerOre(BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get());
            registerOre(RED_GEODE_ORE.get(), WRItems.RED_GEODE.get());
            registerOre(PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get());

            for (Block block : getKnownBlocks()) // All blocks that have not been given special treatment above, drop themselves!
            {
                if (!lootTables.containsKey(block) && block.getLootTable() != net.minecraft.world.storage.loot.LootTables.EMPTY) // Loottable is already set to not have one, ignore.
                    registerDropSelfLootTable(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() { return ModUtils.getRegistryEntries(REGISTRY); }

        private void registerOre(Block ore, Item output) { registerLootTable(ore, block -> droppingItemWithFortune(block, output)); }

        @Override
        protected void registerLootTable(Block blockIn, LootTable.Builder table)
        {
            super.registerLootTable(blockIn, table);
            lootTables.put(blockIn, table);
        }
    }

    private static class Entities extends EntityLootTables
    {
        private static final ILootCondition.IBuilder UNTAMED_AND_ADULT = EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(new EntityFlagsPredicate(null, null, null, null, false)).nbt(new NBTPredicate(Util.make(new CompoundNBT(), c -> c.putString("OwnerUUID", "")))));
        private static final LootFunction.Builder<?> ON_FIRE_SMELT = Smelt.func_215953_b().acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true).build())));

        private final Map<EntityType<?>, LootTable.Builder> lootTables = Maps.newHashMap();

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
            return ModUtils.getRegistryEntries(WREntities.REGISTRY);
        }

        /**
         * @param types the types to register an empty loot tables
         * @deprecated SHOULD ONLY USE THIS WHEN AN ENTITY ABSOLUTELY DOES NOT HAVE ONE, OR IN TESTING!
         */
        public void registerEmptyTables(EntityType<?>... types)
        {
            for (EntityType<?> type : types)
            {
                Wyrmroost.LOG.warn("Registering EMPTY Loottable for: '{}'", type.getRegistryName());
                registerLootTable(type, LootTable.builder());
            }
        }

        @Override
        protected void registerLootTable(EntityType<?> type, LootTable.Builder table)
        {
            lootTables.put(type, table);
        }

        @Override
        protected void addTables()
        {
            registerLootTable(WREntities.LESSER_DESERTWYRM.get(), LootTable.builder().addLootPool(singleRollPool().addEntry(item(WRItems.LDWYRM.get(), 1).acceptFunction(ON_FIRE_SMELT))));

            registerLootTable(WREntities.OVERWORLD_DRAKE.get(), LootTable.builder()
                    .addLootPool(singleRollPool().addEntry(item(Items.LEATHER, 5, 10).acceptFunction(looting(1, 4))))
                    .addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_COMMON_MEAT.get(), 1, 7, 2, 3)))
                    .addLootPool(singleRollPool().addEntry(item(WRItems.DRAKE_BACKPLATE.get(), 1)).acceptCondition(KilledByPlayer.builder()).acceptCondition(UNTAMED_AND_ADULT).acceptCondition(RandomChance.builder(0.25f))));

            registerLootTable(WREntities.ROOSTSTALKER.get(), LootTable.builder()
                    .addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 3, 1, 3)))
                    .addLootPool(singleRollPool().addEntry(item(Items.GOLD_NUGGET, 0, 2))));

            registerLootTable(WREntities.DRAGON_FRUIT_DRAKE.get(), LootTable.builder().addLootPool(singleRollPool().addEntry(item(Items.APPLE, 0, 6))));

            registerLootTable(WREntities.CANARI_WYVERN.get(), LootTable.builder()
                    .addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_COMMON_MEAT.get(), 0, 3, 1, 2)))
                    .addLootPool(singleRollPool().addEntry(item(Items.FEATHER, 1, 4).acceptFunction(looting(2, 6)))));

            registerLootTable(WREntities.SILVER_GLIDER.get(), LootTable.builder().addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 5, 1, 3))));

            registerLootTable(WREntities.BUTTERFLY_LEVIATHAN.get(), LootTable.builder()
                    .addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_APEX_MEAT.get(), 6, 9, 2, 4)))
                    .addLootPool(LootPool.builder().rolls(RandomValueRange.of(1, 4)).addEntry(item(Items.SEA_PICKLE, 0, 2).acceptFunction(looting(1, 2))).addEntry(item(Items.SEAGRASS, 4, 14)).addEntry(item(Items.KELP, 16, 24)))
                    .addLootPool(singleRollPool().addEntry(item(Items.HEART_OF_THE_SEA, 1).acceptCondition(RandomChance.builder(0.1f))).addEntry(item(Items.NAUTILUS_SHELL, 1).acceptCondition(RandomChance.builder(0.2f)))));

            registerLootTable(WREntities.ROYAL_RED.get(), LootTable.builder()
                    .addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_APEX_MEAT.get(), 4, 8, 3, 5))));

            registerLootTable(WREntities.COIN_DRAGON.get(), LootTable.builder().addLootPool(singleRollPool()
                    .addEntry(meat(WRItems.RAW_LOWTIER_MEAT.get(), 1, 1, 0, 1))
                    .addEntry(item(Items.GOLD_NUGGET, 4))));

            registerLootTable(WREntities.CANARI_WYVERN.get(), LootTable.builder()
                    .addLootPool(singleRollPool().addEntry(meat(WRItems.RAW_COMMON_MEAT.get(), 3, 7, 2, 6)))
                    .addLootPool(singleRollPool().addEntry(item(Items.FEATHER, 3, 10).acceptFunction(looting(3, 11)))));
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

        private static LootPool.Builder singleRollPool() { return LootPool.builder().rolls(ConstantRange.of(1)); }

        private static ItemLootEntry.Builder<?> meat(IItemProvider itemIn, int minAmount, int maxAmount, int lootingMin, int lootingMax)
        {
            return item(itemIn, minAmount, maxAmount).acceptFunction(ON_FIRE_SMELT).acceptFunction(looting(lootingMin, lootingMax));
        }
    }
}
