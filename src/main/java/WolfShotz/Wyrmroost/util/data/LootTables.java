package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.Smelt;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationResults validationresults) {}

    public static class BlockLoot extends BlockLootTables
    {
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void addTables()
        {
            registerStandardDropLoot(
                    BLUE_GEODE_BLOCK.get(), RED_GEODE_BLOCK.get(), PURPLE_GEODE_BLOCK.get(),
                    PLATINUM_ORE.get(), PLATINUM_BLOCK.get(),
                    BLUE_CRYSTAL_BLOCK.get(), GREEN_CRYSTAL_BLOCK.get(), ORANGE_CRYSTAL_BLOCK.get(), YELLOW_CRYSTAL_BLOCK.get(),
                    ASH_BLOCK.get(), ASH_LOG.get(),
                    CANARI_WOOD.get(), STRIPPED_CANARI.get(), CANARI_LOG.get(), CANARI_PLANKS.get(),
                    STRIPPED_BLUE_CORIN_LOG.get(), BLUE_CORIN_LOG.get(), BLUE_CORIN_WOOD.get(), BLUE_CORIN_PLANKS.get(),
                    STRIPPED_TEAL_CORIN_LOG.get(), TEAL_CORIN_LOG.get(), TEAL_CORIN_WOOD.get(), TEAL_CORIN_PLANKS.get(),
                    STRIPPED_RED_CORIN_LOG.get(), RED_CORIN_LOG.get(), RED_CORIN_WOOD.get(), RED_CORIN_PLANKS.get()
            );

            registerLootTable(ASH.get(), block -> droppingWithSilkTouchOrRandomly(block, WRItems.ASH_PILE.get(), RandomValueRange.of(3f, 5f)));

            registerOre(BLUE_GEODE_ORE.get(), WRItems.BLUE_GEODE.get());
            registerOre(RED_GEODE_ORE.get(), WRItems.RED_GEODE.get());
            registerOre(PURPLE_GEODE_ORE.get(), WRItems.PURPLE_GEODE.get());

            registerOre(BLUE_CRYSTAL_ORE.get(), WRItems.BLUE_SHARD.get());
            registerLootTable(BLUE_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.BLUE_SHARD.get(), 1).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));

            registerOre(GREEN_CRYSTAL_ORE.get(), WRItems.GREEN_SHARD.get());
            registerLootTable(GREEN_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.GREEN_SHARD.get(), 1).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));

            registerOre(ORANGE_CRYSTAL_ORE.get(), WRItems.ORANGE_SHARD.get());
            registerLootTable(ORANGE_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.ORANGE_SHARD.get(), 1).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));

            registerOre(YELLOW_CRYSTAL_ORE.get(), WRItems.YELLOW_SHARD.get());
            registerLootTable(YELLOW_CRYSTAL.get(), crystal -> droppingWithSilkTouch(crystal, withExplosionDecay(crystal, item(WRItems.YELLOW_SHARD.get(), 1).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)))));

            registerSilkTouch(CANARI_LEAVES.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ModUtils.getRegistryEntries(BLOCKS);
        }

        private void registerStandardDropLoot(Block... blocks)
        {
            for (Block block : blocks) super.registerDropSelfLootTable(block);
        }

        private void registerOre(Block ore, Item output)
        {
            registerLootTable(ore, block -> droppingItemWithFortune(block, output));
        }
    }

    public static class EntityLoot extends EntityLootTables
    {
        private static final List<EntityType<?>> NO_DROPS = ImmutableList.of(WREntities.SILVER_GLIDER.get(), WREntities.DRAGON_EGG.get(), WREntities.MULTIPART.get(), WREntities.BUTTERFLY_LEVIATHAN.get());
        private static final EntityPredicate.Builder ON_FIRE = EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true).build());

        @Override
        protected void addTables()
        {
            registerLootTable(WREntities.MINUTUS.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(WRItems.MINUTUS.get(), 1).acceptFunction(onFireCondition())))
            );
            registerLootTable(WREntities.OVERWORLD_DRAKE.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(Items.LEATHER, 1f, 16f).acceptFunction(looting(1f, 4f))))
                    .addLootPool(lootTable().addEntry(item(WRItems.FOOD_COMMON_MEAT_RAW.get(), 2f, 6f).acceptFunction(onFireCondition()).acceptFunction(looting(1f, 4f))))
            );
            registerLootTable(WREntities.ROOSTSTALKER.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(Items.GOLD_NUGGET, 0f, 4f).acceptFunction(looting(1f, 3f))))
            );
            registerLootTable(WREntities.DRAGON_FRUIT_DRAKE.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(WRItems.FOOD_DRAGON_FRUIT.get(), 1f, 2f).acceptFunction(looting(0f, 2f))))
            );
            registerLootTable(WREntities.CANARI_WYVERN.get(), LootTable.builder()
                    .addLootPool(lootTable().addEntry(item(Items.FEATHER, 0f, 3f).acceptFunction(looting(0f, 2f))))
            );
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities()
        {
            return ModUtils.getRegistryEntries(WREntities.ENTITIES)
                    .stream()
                    .filter(e -> !NO_DROPS.contains(e))
                    .collect(Collectors.toSet());
        }

        private static LootFunction.Builder<?> onFireCondition()
        {
            return Smelt.func_215953_b().acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE));
        }
    }

    private static LootPool.Builder lootTable()
    {
        return LootPool.builder().rolls(ConstantRange.of(1));
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
}
