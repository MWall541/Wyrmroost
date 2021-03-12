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

            addDrop(MULCH.get(), drops(MULCH.get(), Blocks.DIRT));
            registerLeaves(BLUE_OSERI_LEAVES.get(), BLUE_OSERI_SAPLING.get());
            registerLeaves(GOLD_OSERI_LEAVES.get(), GOLD_OSERI_SAPLING.get());
            registerLeaves(PINK_OSERI_LEAVES.get(), PINK_OSERI_SAPLING.get());
            registerLeaves(PURPLE_OSERI_LEAVES.get(), PURPLE_OSERI_SAPLING.get());

            // All blocks that have not been given special treatment above, drop themselves!
            for (Block block : getKnownBlocks())
            {
                if (block instanceof PetalsBlock || block instanceof VineBlock || block instanceof AbstractPlantBlock || (block instanceof BushBlock && !(block instanceof SaplingBlock)))
                {
                    addDrop(block, BlockLootTables::dropsWithShears);
                    continue;
                }
                if (block instanceof DoorBlock)
                {
                    addDrop(block, BlockLootTables::addDoorDrop);
                    continue;
                }

                ResourceLocation lootTable = block.getLootTableId();
                boolean notInheriting = lootTable.getPath().replace("blocks/", "").equals(block.getRegistryName().getPath());
                if (!lootTables.containsKey(block) && lootTable != LootTables.EMPTY && notInheriting)
                    drops(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ModUtils.getRegistryEntries(REGISTRY);
        }

        private void registerLeaves(Block leaves, Block sapling)
        {
            addDrop(leaves, oakLeavesDrop(leaves, sapling, 0.05f, 0.0625f, 0.083333336f, 0.1f));
        }

        private void registerOre(Block ore, Item output)
        {
            addDrop(ore, oreDrops(ore, output));
        }

        @Override
        protected void addDrop(Block blockIn, LootTable.Builder table)
        {
            super.addDrop(blockIn, table);
            lootTables.put(blockIn, table);
        }
    }

    private static class Entities extends EntityLootTables
    {
        private static final LootFunction.Builder<?> ON_FIRE_SMELT = Smelt.builder().conditionally(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true).build())));

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
                    if (entity.getSpawnGroup() == EntityClassification.MISC) continue;
                    throw new IllegalArgumentException(String.format("Missing Loottable for entry: '%s'", entity.getRegistryName()));
                }
                consumer.accept(entity.getLootTableId(), lootTables.remove(entity));
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
                register(type, LootTable.builder());
            }
        }

        @Override
        protected void register(EntityType<?> type, LootTable.Builder table)
        {
            lootTables.put(type, table);
        }

        @Override
        protected void addTables()
        {
            register(WREntities.LESSER_DESERTWYRM.get(), LootTable.builder().pool(singleRollPool().with(item(WRItems.LDWYRM.get(), 1).apply(ON_FIRE_SMELT))));

            register(WREntities.OVERWORLD_DRAKE.get(), LootTable.builder()
                    .pool(singleRollPool().with(item(Items.LEATHER, 5, 10).apply(looting(1, 4))))
                    .pool(singleRollPool().with(meat(WRItems.RAW_COMMON_MEAT.get(), 1, 7, 2, 3)))
                    .pool(singleRollPool().with(item(WRItems.DRAKE_BACKPLATE.get(), 1)).conditionally(KilledByPlayer.builder()).conditionally(RandomChance.builder(0.15f)).apply(looting(0, 1))));

            register(WREntities.ROOSTSTALKER.get(), LootTable.builder()
                    .pool(singleRollPool().with(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 2, 1, 2)))
                    .pool(singleRollPool().with(item(Items.GOLD_NUGGET, 0, 2))));

            register(WREntities.DRAGON_FRUIT_DRAKE.get(), LootTable.builder().pool(singleRollPool().with(item(Items.APPLE, 0, 6))));

            register(WREntities.CANARI_WYVERN.get(), LootTable.builder()
                    .pool(singleRollPool().with(meat(WRItems.RAW_COMMON_MEAT.get(), 0, 2, 1, 2)))
                    .pool(singleRollPool().with(item(Items.FEATHER, 1, 4).apply(looting(2, 6)))));

            register(WREntities.SILVER_GLIDER.get(), LootTable.builder()
                    .pool(singleRollPool().with(meat(WRItems.RAW_LOWTIER_MEAT.get(), 0, 3, 1, 3))));

            register(WREntities.BUTTERFLY_LEVIATHAN.get(), LootTable.builder()
                    .pool(singleRollPool().with(meat(WRItems.RAW_APEX_MEAT.get(), 6, 10, 2, 4)))
                    .pool(LootPool.builder().rolls(RandomValueRange.between(1, 4)).with(item(Items.SEA_PICKLE, 0, 2).apply(looting(1, 2))).with(item(Items.SEAGRASS, 4, 14)).with(item(Items.KELP, 16, 24)))
                    .pool(singleRollPool().with(item(Items.HEART_OF_THE_SEA, 1).conditionally(RandomChance.builder(0.1f))).with(item(Items.NAUTILUS_SHELL, 1).conditionally(RandomChance.builder(0.15f)))));

            register(WREntities.ROYAL_RED.get(), LootTable.builder()
                    .pool(singleRollPool().with(meat(WRItems.RAW_APEX_MEAT.get(), 4, 8, 3, 5))));

            register(WREntities.COIN_DRAGON.get(), LootTable.builder().pool(singleRollPool()
                    .with(meat(WRItems.RAW_LOWTIER_MEAT.get(), 1, 1, 0, 1))
                    .with(item(Items.GOLD_NUGGET, 4))));

            register(WREntities.ALPINE.get(), LootTable.builder()
                    .pool(singleRollPool().with(meat(WRItems.RAW_COMMON_MEAT.get(), 3, 7, 2, 6)))
                    .pool(singleRollPool().with(item(Items.FEATHER, 3, 10).apply(looting(3, 11)))));
        }

        private static LootingEnchantBonus.Builder looting(float min, float max)
        {
            return LootingEnchantBonus.builder(RandomValueRange.between(min, max));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, float minIn, float maxIn)
        {
            return ItemLootEntry.builder(itemIn).apply(SetCount.builder(RandomValueRange.between(minIn, maxIn)));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, int amount)
        {
            return ItemLootEntry.builder(itemIn).apply(SetCount.builder(ConstantRange.create(amount)));
        }

        private static LootPool.Builder singleRollPool()
        {
            return LootPool.builder().rolls(ConstantRange.create(1));
        }

        private static ItemLootEntry.Builder<?> meat(IItemProvider itemIn, int minAmount, int maxAmount, int lootingMin, int lootingMax)
        {
            return item(itemIn, minAmount, maxAmount).apply(ON_FIRE_SMELT).apply(looting(lootingMin, lootingMax));
        }
    }
}
