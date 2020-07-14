package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.entities.dragon.CoinDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CoinDragonItem extends Item
{
    public static final String DATA_ENTITY = "CoinDragonData";

    public CoinDragonItem() { super(ModUtils.itemBuilder().maxStackSize(1)); }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        CoinDragonEntity entity = WREntities.COIN_DRAGON.get().create(context.getWorld());
        BlockPos pos = context.getPos().offset(context.getFace());
        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.hasNoCollisions(entity))
        {
            context.getPlayer().sendStatusMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.fail").applyTextStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        if (world.isRemote) return ActionResultType.SUCCESS;

        ItemStack stack = context.getItem();
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_ENTITY)) entity.deserializeNBT(tag.getCompound(DATA_ENTITY));
            if (stack.hasDisplayName()) entity.setCustomName(stack.getDisplayName()); // set entity name from stack name
        }

        stack.shrink(1);
        world.addEntity(entity);
        return ActionResultType.SUCCESS;
    }
}
