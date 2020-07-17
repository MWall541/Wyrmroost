package WolfShotz.Wyrmroost.entities.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.DragonEggItem;
import WolfShotz.Wyrmroost.registry.WRItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

public class VillagerHelper
{
    private static final ResourceLocation CD_TRADER_RL = Wyrmroost.rl("coin_dragon_trader");
    public static final RegistryObject<VillagerProfession> PROFESSION_COIN_DRAGON_TRADER = RegistryObject.of(CD_TRADER_RL, ForgeRegistries.PROFESSIONS);

    public static void registerVillagersAndTrades(RegistryEvent.Register<VillagerProfession> evt)
    {
        VillagerProfession prof = new VillagerProfession(CD_TRADER_RL.getPath(), PointOfInterestType.NITWIT, ImmutableSet.of(), ImmutableSet.of(), null).setRegistryName(CD_TRADER_RL);
        evt.getRegistry().register(prof);

        VillagerTrades.VILLAGER_DEFAULT_TRADES.put(prof, new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                1, new ITrade[] {
                        new ItemsForCDTrade(WRItems.BLUE_GEODE.get(), 8, 2, 2),
                        new ItemsForCDTrade(WRItems.RED_GEODE.get(), 5, 4, 3),
                        new ItemsForCDTrade(WRItems.PURPLE_GEODE.get(), 3, 6, 4),
                        new CDForItemsTrade(new ItemStack(WRItems.LDWYRM.get(), 64), 1, 3)
                },
                2, new ITrade[] {
                }

        )));
    }

    public static ITrade CDForEgg(EntityType<? extends AbstractDragonEntity> dragon, int maxUses, int xp)
    {
        ItemStack stack = DragonEggItem.getStack(dragon);
        return new CDForItemsTrade(stack, maxUses, xp);
    }

    private static class ItemForItemTrade implements ITrade
    {
        private final ItemStack buyingItem1, buyingItem2, sellingItem;
        private final int maxUses, xp;
        private final float multiplier;

        public ItemForItemTrade(ItemStack buyingItem1, ItemStack buyingItem2, ItemStack sellingItem, int maxUses, int xp, float multiplier)
        {
            this.buyingItem1 = buyingItem1;
            this.buyingItem2 = buyingItem2;
            this.sellingItem = sellingItem;
            this.maxUses = maxUses;
            this.xp = xp;
            this.multiplier = multiplier;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand)
        {
            return new MerchantOffer(buyingItem1, buyingItem2, sellingItem, maxUses, xp, multiplier);
        }
    }

    private static class CDForItemsTrade implements ITrade
    {
        private final ItemStack selling;
        private final int maxUses, xp;

        public CDForItemsTrade(ItemStack selling, int maxUses, int xp)
        {
            this.selling = selling;
            this.maxUses = maxUses;
            this.xp = xp;
        }

        public CDForItemsTrade(Item item, int amount, int maxUses, int xp)
        {
            this.selling = new ItemStack(item, amount);
            this.maxUses = maxUses;
            this.xp = xp;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand)
        {
            return new MerchantOffer(selling, new ItemStack(WRItems.COIN_DRAGON.get()), maxUses, xp, 0);
        }
    }

    private static class ItemsForCDTrade implements ITrade
    {
        private final Item buying;
        private final int amount, maxUses, xp;

        public ItemsForCDTrade(Item buying, int amount, int maxUses, int xp)
        {
            this.buying = buying;
            this.amount = amount;
            this.maxUses = maxUses;
            this.xp = xp;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand)
        {
            return new MerchantOffer(new ItemStack(buying, amount), new ItemStack(WRItems.COIN_DRAGON.get()), maxUses, xp, 0.15f);
        }
    }
}
