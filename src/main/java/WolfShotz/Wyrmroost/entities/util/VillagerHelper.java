package WolfShotz.Wyrmroost.entities.util;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.event.village.WandererTradesEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;

public class VillagerHelper
{
    public static void addWandererTrades(WandererTradesEvent evt)
    {
        List<ITrade> list = evt.getGenericTrades();

        list.add(cdForItems(WRItems.BLUE_GEODE.get(), 12, 1, 3));
        list.add(cdForItems(WRItems.RED_GEODE.get(), 6, 1, 4));
        list.add(cdForItems(WRItems.PURPLE_GEODE.get(), 3, 1, 5));
        list.add(cdForItems(WRItems.TRUMPET.get(), 1, 4, 2));
        list.add(cdForItems(WRItems.JEWELLED_APPLE.get(), 2, 3, 1));
        list.add(new ItemsForItemsTrade(new ItemStack(Items.EMERALD, 6), new ItemStack(WRItems.BLUE_GEODE.get(), 4), 4, 1, 10));
    }

    private static ITrade cdForItems(ItemStack selling, int maxUses, int xp)
    {
        return new ItemsForItemsTrade(new ItemStack(WRItems.COIN_DRAGON.get()), selling, maxUses, xp, 0);
    }

    private static ITrade cdForItems(Item item, int count, int maxUses, int xp)
    {
        return cdForItems(new ItemStack(item, count), maxUses, xp);
    }

    private static class ItemsForItemsTrade implements ITrade
    {
        private final ItemStack buying1, buying2, selling;
        private final int maxUses, xp;
        private final float priceMultiplier;

        public ItemsForItemsTrade(ItemStack buying1, ItemStack buying2, ItemStack selling, int maxUses, int xp, float priceMultiplier)
        {
            this.buying1 = buying1;
            this.buying2 = buying2;
            this.selling = selling;
            this.maxUses = maxUses;
            this.xp = xp;
            this.priceMultiplier = priceMultiplier;
        }

        public ItemsForItemsTrade(ItemStack buying1, ItemStack selling, int maxUses, int xp, float priceMultiplier)
        {
            this(buying1, ItemStack.EMPTY, selling, maxUses, xp, priceMultiplier);
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand)
        {
            return new MerchantOffer(buying1, buying2, selling, maxUses, xp, priceMultiplier);
        }
    }

//    private static class CDForMapTrade implements ITrade
//    {
//        private final String structureName;
//        private final MapDecoration.Type type;
//
//        @Nullable
//        public MerchantOffer getOffer(Entity trader, Random rand)
//        {
//            if (trader.world.isRemote) return null;
//
//            ServerWorld world = (ServerWorld) trader.world;
//            BlockPos blockPos = world.findNearestStructure(structureName, new BlockPos(trader), 100, true);
//            if (blockPos == null) return null;
//
//            ItemStack itemstack = FilledMapItem.setupNewMap(world, blockPos.getX(), blockPos.getZ(), (byte) 2, true, true);
//            FilledMapItem.func_226642_a_(world, itemstack);
//            MapData.addTargetDecoration(itemstack, blockPos, "+", type);
//            itemstack.setDisplayName(new TranslationTextComponent("filled_map." + structureName.toLowerCase(Locale.ROOT)));
//            return new MerchantOffer(new ItemStack(WRItems.COIN_DRAGON.get(), 1), new ItemStack(Items.COMPASS), itemstack, 1, 5, 0.2f);
//        }
//    }
}
