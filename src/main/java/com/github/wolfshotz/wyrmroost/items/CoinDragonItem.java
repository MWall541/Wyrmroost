package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.CoinDragonEntity;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class CoinDragonItem extends Item
{
    public static final String DATA_ENTITY = "CoinDragonData";
    public static final ResourceLocation VARIANT_OVERRIDE = Wyrmroost.rl("variant");

    public CoinDragonItem()
    {
        super(WRItems.builder().maxStackSize(1));
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ClientEvents.CALLBACKS.add(() -> ItemModelsProperties.func_239418_a_(this, VARIANT_OVERRIDE, (s, w, p) -> s.getOrCreateTag().getCompound(DATA_ENTITY).getInt(CoinDragonEntity.DATA_VARIANT))));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        CoinDragonEntity entity = WREntities.COIN_DRAGON.get().create(context.getWorld());
        BlockPos pos = context.getPos().offset(context.getFace());
        ItemStack stack = context.getItem();
        PlayerEntity player = context.getPlayer();

        if (!world.isRemote && stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_ENTITY)) entity.deserializeNBT(tag.getCompound(DATA_ENTITY));
            if (stack.hasDisplayName()) entity.setCustomName(stack.getDisplayName()); // set entity name from stack name
        }

        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.hasNoCollisions(entity))
        {
            player.sendStatusMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.fail").mergeStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        if (!player.isCreative() || stack.getOrCreateTag().contains(DATA_ENTITY))
            player.setHeldItem(context.getHand(), ItemStack.EMPTY);
        entity.setMotion(Vector3d.ZERO);
        entity.rotationYaw = entity.rotationYawHead = player.rotationYawHead + 180;
        world.addEntity(entity);
        return ActionResultType.SUCCESS;
    }
}
