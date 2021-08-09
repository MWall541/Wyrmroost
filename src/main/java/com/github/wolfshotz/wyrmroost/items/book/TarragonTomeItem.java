package com.github.wolfshotz.wyrmroost.items.book;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.TarragonTomeRenderer;
import com.github.wolfshotz.wyrmroost.client.screen.BookScreen;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.book.action.BookAction;
import com.github.wolfshotz.wyrmroost.items.book.action.BookActions;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TarragonTomeItem extends Item
{
    public static final String DATA_DRAGON_ID = "BoundDragon"; // int
    public static final String DATA_ACTION = "Action";

    public TarragonTomeItem()
    {
        super(WRItems.builder().stacksTo(1).rarity(Rarity.RARE).setISTER(() -> TarragonTomeRenderer::new));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new Animations();
    }

    @Override
    public void inventoryTick(ItemStack stack, World level, Entity entity, int invIndex, boolean holding)
    {
        if (holding || invIndex == 0) ((Animations) ModUtils.getCapability(IAnimatable.CapImpl.CAPABILITY, stack)).tick();
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown())
        {
            clear(stack.getTag());
            ModUtils.playLocalSound(level, player.blockPosition(), SoundEvents.PAINTING_BREAK, 0.75f, 1f);
            return new ActionResult<>(ActionResultType.sidedSuccess(level.isClientSide), stack);
        }
        return new ActionResult<>(getAction(stack).rightClick(getBoundDragon(level, stack), player, stack), stack);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity interacted, Hand hand)
    {
        return super.interactLivingEntity(stack, player, interacted, hand);
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx)
    {
        ItemStack stack = ctx.getItemInHand();
        return getAction(stack).clickBlock(getBoundDragon(ctx.getLevel(), stack), ctx);
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        return (tag != null && tag.contains(DATA_DRAGON_ID)) || super.isFoil(stack);
    }

    public static void setAction(BookAction action, PlayerEntity player, ItemStack stack)
    {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt(DATA_ACTION, BookActions.ACTIONS.indexOf(action));
        action.onSelected(getBoundDragon(player.level, stack), player, stack);
    }

    public static BookAction getAction(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(DATA_ACTION)) return BookActions.ACTIONS.get(tag.getInt(DATA_ACTION));
        return BookActions.DEFAULT;
    }

    public static void bind(TameableDragonEntity dragon, ItemStack stack)
    {
        stack.getOrCreateTag().putInt(DATA_DRAGON_ID, dragon.getId());
    }

    @Nullable
    public static TameableDragonEntity getBoundDragon(World level, ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(DATA_DRAGON_ID))
        {
            Entity entity = level.getEntity(tag.getInt(DATA_DRAGON_ID));
            if (entity instanceof TameableDragonEntity) return (TameableDragonEntity) entity;
        }

        return null;
    }

    private static void clear(@Nullable CompoundNBT tag)
    {
        if (tag == null) return;
        tag.remove(DATA_DRAGON_ID);
        tag.remove(DATA_ACTION);
    }

    public static class Animations extends IAnimatable.CapImpl implements ICapabilityProvider
    {
        private final LazyOptional<IAnimatable> instance = LazyOptional.of(() -> this);
        public final LerpedFloat flipTime = new LerpedFloat();
        public float flipDuration;
        public float flipA;

        public void tick()
        {
            boolean flag = ModUtils.isClient() && ClientEvents.getClient().screen instanceof BookScreen;
            if (!flag && random.nextDouble() < 0.075) flipDuration += random.nextInt(4) - random.nextInt(4);
            float f = MathHelper.clamp((flipDuration - flipTime.get()) * 0.4f, -0.2f, 0.2f);
            flipA += (f - flipA) * 0.9F;
            flipTime.add(flipA);
        }

        public void flipPages(float amount)
        {
            flipDuration += amount;
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return CapImpl.CAPABILITY.orEmpty(cap, instance);
        }
    }
}
