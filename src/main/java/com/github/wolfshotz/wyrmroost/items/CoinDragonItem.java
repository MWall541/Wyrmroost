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
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.functions.SetNBT;
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

import java.util.Random;

public class CoinDragonItem extends Item
{
    public static final String DATA_ENTITY = "CoinDragonData";
    public static final ResourceLocation VARIANT_OVERRIDE = Wyrmroost.id("variant");

    public CoinDragonItem()
    {
        super(WRItems.builder().maxCount(1));
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ClientEvents.CALLBACKS.add(() -> ItemModelsProperties.register(this, VARIANT_OVERRIDE, (s, w, p) -> s.getOrCreateTag().getCompound(DATA_ENTITY).getInt(CoinDragonEntity.DATA_VARIANT))));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ActionResultType useOnBlock(ItemUseContext context)
    {
        World world = context.getWorld();
        CoinDragonEntity entity = WREntities.COIN_DRAGON.get().create(context.getWorld());
        BlockPos pos = context.getBlockPos().offset(context.getSide());
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();

        if (!world.isClientSide && stack.hasTag()) // read data first!: setting position before reading will reset that position!
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_ENTITY)) entity.deserializeNBT(tag.getCompound(DATA_ENTITY));
            if (stack.hasCustomName()) entity.setCustomName(stack.getName()); // set entity name from stack name
        }

        entity.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.isSpaceEmpty(entity))
        {
            player.sendMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.fail").formatted(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        if (!player.isCreative() || stack.getOrCreateTag().contains(DATA_ENTITY))
            player.setStackInHand(context.getHand(), ItemStack.EMPTY);
        entity.setVelocity(Vector3d.ZERO);
        entity.yaw = entity.headYaw = player.headYaw + 180;
        world.spawnEntity(entity);
        return ActionResultType.SUCCESS;
    }

    public static LootEntry.Builder<?> getLootEntry()
    {
        CompoundNBT parent = new CompoundNBT();
        CompoundNBT child = new CompoundNBT(); // because the parent nbt gets merged with the stack, we need to nest a child within the one getting merged
        child.putInt(CoinDragonEntity.DATA_VARIANT, new Random().nextInt(5));
        parent.put(DATA_ENTITY, child);
        return ItemLootEntry.builder(WRItems.COIN_DRAGON.get()).apply(SetNBT.builder(parent));
    }
}
