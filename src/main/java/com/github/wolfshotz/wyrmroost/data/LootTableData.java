package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.PetalsBlock;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.Smelt;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.wolfshotz.wyrmroost.registry.WRBlocks.*;


class LootTableData extends LootTableProvider
{
    LootTableData(DataGenerator gen)
    {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return ImmutableList.of(Pair.of(BlookLoot::new, LootParameterSets.BLOCK), Pair.of(Entities::new, LootParameterSets.ENTITY));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
    {
    }

    private static class BlookLoot extends BlockLootTables
    {
        public final Map<Block, LootTable.Builder> lootTables = new HashMap<>();

        @Override
        protected void addTables()
        {
            registerOre(BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get());
            registerOre(RED_GEODE_ORE.get(), WRItems.RED_GEODE.get());
            registerOre(PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get());

            add(MULCH.get(), createSingleItemTableWithSilkTouch(MULCH.get(), Blocks.DIRT));
            registerLeaves(BLUE_OSERI_LEAVES.get(), BLUE_OSERI_SAPLING.get());
            registerLeaves(GOLD_OSERI_LEAVES.get(), GOLD_OSERI_SAPLING.get());
            registerLeaves(PINK_OSERI_LEAVES.get(), PINK_OSERI_SAPLING.get());
            registerLeaves(PURPLE_OSERI_LEAVES.get(), PURPLE_OSERI_SAPLING.get());

            // All blocks that have not been given special treatment above, drop themselves!
            for (Block block : getKnownBlocks())
            {
                if (block instanceof PetalsBlock || block instanceof VineBlock || block instanceof AbstractPlantBlock || (block instanceof BushBlock && !(block instanceof SaplingBlock)))
                {
                    add(block, BlockLootTables::createShearsOnlyDrop);
                    continue;
                }
                if (block instanceof DoorBlock)
                {
                    add(block, BlockLootTables::createDoorTable);
                    continue;
                }

                ResourceLocation lootTable = block.getLootTable();
                boolean notInheriting = lootTable.getPath().replace("blocks/", "").equals(block.getRegistryName().getPath());
                if (!lootTables.containsKey(block) && lootTable != LootTables.EMPTY && notInheriting)
                    dropSelf(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ModUtils.getRegistryEntries(REGISTRY);
        }

        private void registerLeaves(Block leaves, Block sapling)
        {
            add(leaves, createOakLeavesDrops(leaves, sapling, 0.05f, 0.0625f, 0.083333336f, 0.1f));
        }

        private void registerOre(Block ore, Item output)
        {
            add(ore, createOreDrop(ore, output));
        }

        @Override
        protected void add(Block blockIn, LootTable.Builder table)
        {
            super.add(blockIn, table);
            lootTables.put(blockIn, table);
        }
    }

    private static class Entities extends EntityLootTables
    {
        private static final LootFunction.Builder<?> ON_FIRE_SMELT = Smelt.smelted().when(EntityHasProperty.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build())));

        private final Map<EntityType<?>, LootTable.Builder> lootTables = new HashMap<>();

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
                    if (entity.getCategory() == EntityClassification.MISC) continue;
                    throw new IllegalArgumentException(String.format("Missing Loottable for entry: '%s'", entity.getRegistryName()));
                }
                consumer.accept(entity.getDefaultLootTable(), lootTables.remove(entity));
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
        @Deprecated
        public void registerEmptyTables(EntityType<?>... types)
        {
            for (EntityType<?> type : types)
            {
                Wyrmroost.LOG.warn("Registering EMPTY Loottable for: '{}'", type.getRegistryName());
                add(type, LootTable.lootTable());
            }
        }

        @Override
        protected void add(EntityType<?> type, LootTable.Builder table)
        {
            lootTables.put(type, table);
        }

        @Override
        protected void addTables()
        {
            add(WREntities.LESSER_DESERTWYRM.get(), LootTable.lootTable().withPool(singleRollPool().add(item(WRItems.LDWYRM.get(), 1).apply(ON_FIRE_SMELT))));

            add(WREntities.OVERWORLD_DRAKE.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(item(Items.LEATHER, 5, 10).apply(looting(1, 4))))
                    .withPool(singleRollPool().add(meat(WRItems.RAW_COMMON_MEAT.get(), 1, 7, 2, 3)))
                    .withPool(singleRollPool().add(item(WRItems.DRAKE_BACKPLATE.get(), 1)).when(KilledByPlayer.killedByPlayer()).when(RandomChance.randomChance(0.15f)).apply(looting(0, 1))));

            add(WREntities.ROOSTSTALKER.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 2, 1, 2)))
                    .withPool(singleRollPool().add(item(Items.GOLD_NUGGET, 0, 2))));

            add(WREntities.DRAGON_FRUIT_DRAKE.get(), LootTable.lootTable().withPool(singleRollPool().add(item(Items.APPLE, 0, 6))));

            add(WREntities.CANARI_WYVERN.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(meat(WRItems.RAW_COMMON_MEAT.get(), 0, 2, 1, 2)))
                    .withPool(singleRollPool().add(item(Items.FEATHER, 1, 4).apply(looting(2, 6)))));

            add(WREntities.SILVER_GLIDER.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 3, 1, 3))));

            add(WREntities.BUTTERFLY_LEVIATHAN.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(meat(WRItems.RAW_APEX_MEAT.get(), 6, 10, 2, 4)))
                    .withPool(LootPool.lootPool().setRolls(RandomValueRange.between(1, 4)).add(item(Items.SEA_PICKLE, 0, 2).apply(looting(1, 2))).add(item(Items.SEAGRASS, 4, 14)).add(item(Items.KELP, 16, 24)))
                    .withPool(singleRollPool().add(item(Items.HEART_OF_THE_SEA, 1).when(RandomChance.randomChance(0.1f))).add(item(Items.NAUTILUS_SHELL, 1).when(RandomChance.randomChance(0.15f)))));

            add(WREntities.ROYAL_RED.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(meat(WRItems.RAW_APEX_MEAT.get(), 4, 8, 3, 5))));

            add(WREntities.COIN_DRAGON.get(), LootTable.lootTable().withPool(singleRollPool()
                    .add(meat(WRItems.RAW_LOWTIER_MEAT.get(), 1, 1, 0, 1))
                    .add(item(Items.GOLD_NUGGET, 4))));

            add(WREntities.ALPINE.get(), LootTable.lootTable()
                    .withPool(singleRollPool().add(meat(WRItems.RAW_COMMON_MEAT.get(), 3, 7, 2, 6)))
                    .withPool(singleRollPool().add(item(Items.FEATHER, 3, 10).apply(looting(3, 11)))));
        }

        private static LootingEnchantBonus.Builder looting(float min, float max)
        {
            return LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(min, max));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, float minIn, float maxIn)
        {
            return ItemLootEntry.lootTableItem(itemIn).apply(SetCount.setCount(RandomValueRange.between(minIn, maxIn)));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, int amount)
        {
            return ItemLootEntry.lootTableItem(itemIn).apply(SetCount.setCount(ConstantRange.exactly(amount)));
        }

        private static LootPool.Builder singleRollPool()
        {
            return LootPool.lootPool().setRolls(ConstantRange.exactly(1));
        }

        private static ItemLootEntry.Builder<?> meat(IItemProvider itemIn, int minAmount, int maxAmount, int lootingMin, int lootingMax)
        {
            return item(itemIn, minAmount, maxAmount).apply(ON_FIRE_SMELT).apply(looting(lootingMin, lootingMax));
        }
    }
}
