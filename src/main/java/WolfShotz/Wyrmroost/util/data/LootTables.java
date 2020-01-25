package WolfShotz.Wyrmroost.util.data;

import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.Smelt;

import java.util.List;
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

    public static class BlockLoot extends BlockLootTables
    {
        @Override
        @SuppressWarnings("ConstantConditions")
        protected void addTables()
        {
            registerStandardDropLoot(BLUE_GEODE_BLOCK.get(), RED_GEODE_BLOCK.get(), PURPLE_GEODE_BLOCK.get(), PLATINUM_BLOCK.get(), CANARI_WOOD.get(), STRIPPED_CANARI.get());
            registerLootTable(ASH.get(), block -> droppingWithSilkTouchOrRandomly(block, WRItems.ASH_PILE.get(), RandomValueRange.of(3f, 5f)));
            registerLootTable(BLUE_GEODE_ORE.get(), ore -> droppingItemWithFortune(ore, WRItems.BLUE_GEODE.get()));
            registerLootTable(RED_GEODE_ORE.get(), ore -> droppingItemWithFortune(ore, WRItems.RED_GEODE.get()));
            registerLootTable(PURPLE_GEODE_ORE.get(), ore -> droppingItemWithFortune(ore, WRItems.PURPLE_GEODE.get()));
            registerSilkTouch(CANARI_LEAVES.get());
        }

        private void registerStandardDropLoot(Block... blocks)
        {
            for (Block block : blocks) super.registerDropSelfLootTable(block);
        }
    }

    public static class EntityLoot extends EntityLootTables
    {
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
        }

        private static LootPool.Builder lootTable()
        {
            return LootPool.builder().rolls(ConstantRange.of(1));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, float minIn, float maxIn)
        {
            return ItemLootEntry.builder(itemIn).acceptFunction(SetCount.builder(RandomValueRange.of(minIn, maxIn)));
        }

        private static ItemLootEntry.Builder<?> item(IItemProvider itemIn, int amount)
        {
            return ItemLootEntry.builder(itemIn).acceptFunction(SetCount.builder(ConstantRange.of(amount)));
        }

        private static LootingEnchantBonus.Builder looting(float min, float max)
        {
            return LootingEnchantBonus.builder(RandomValueRange.of(min, max));
        }

        private static LootFunction.Builder<?> onFireCondition()
        {
            return Smelt.func_215953_b().acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE));
        }
    }
}
