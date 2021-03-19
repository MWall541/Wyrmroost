package com.github.wolfshotz.wyrmroost.items.staff;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DragonStaffItem extends Item
{
    public static final String DATA_DRAGON_ID = "BoundDragon"; // int
    public static final String DATA_ACTION = "Action";

    public DragonStaffItem()
    {
        super(WRItems.builder().stacksTo(1));
    }

    /**
     * Triggered when right clicked on air
     */
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && stack.hasTag() && stack.getTag().contains(DATA_DRAGON_ID))
        {
            reset(stack.getTag());
            ModUtils.playLocalSound(level, player.blockPosition(), SoundEvents.SCAFFOLDING_STEP, 1, 1);
            return ActionResult.success(stack);
        }

        AbstractDragonEntity dragon = getBoundDragon(level, stack);
        if (dragon != null && getAction(stack).rightClick(dragon, player, stack))
            return ActionResult.success(stack);
        return ActionResult.pass(stack);
    }

    /**
     * Triggered when Attacking an entity with this item
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity)
    {
        if (entity instanceof AbstractDragonEntity)
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
            if (dragon.isOwnedBy(player))
            {
                bindDragon(dragon, stack);
                ModUtils.playLocalSound(player.level, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, 1, 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Triggered when Right clicking an entity
     */
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (target instanceof AbstractDragonEntity)
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) target;
            if (dragon.isOwnedBy(playerIn))
            {
                bindDragon(dragon, stack);
                if (playerIn.level.isClientSide) StaffScreen.open(dragon, stack);
                return ActionResultType.sidedSuccess(playerIn.level.isClientSide);
            }
        }
        return ActionResultType.PASS;
    }

    /**
     * Triggered when right clicked on a block
     */
    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        if (context.getPlayer().isShiftKeyDown()) return ActionResultType.PASS;
        ItemStack stack = context.getItemInHand();
        AbstractDragonEntity dragon = getBoundDragon(context.getLevel(), stack);
        if (dragon != null && getAction(stack).clickBlock(dragon, context)) return ActionResultType.SUCCESS;
        return ActionResultType.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.desc").withStyle(TextFormatting.GRAY));
        if (stack.hasTag())
        {
            AbstractDragonEntity dragon = getBoundDragon(worldIn, stack);
            if (dragon != null)
            {
                tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.bound", ((IFormattableTextComponent) dragon.getName()).withStyle(TextFormatting.AQUA)));
                tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.action", getAction(stack).getTranslation(dragon)).withStyle(TextFormatting.AQUA));
            }
        }
    }

    /**
     * Not ideal whatsoever, but its literally the best we can do.
     * Storing the uuid of the dragons means we would have to get that dragon by uuid, which we cant on client,
     * and this item requires almost constant syncing.
     * The solution is to just remove everything.
     */
    @Override
    public boolean verifyTagAfterLoad(CompoundNBT nbt)
    {
        reset(nbt);
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return getAction(stack) != StaffAction.DEFAULT;
    }

    public static void reset(@Nullable CompoundNBT tag)
    {
        if (tag == null) return;
        if (tag.contains(DATA_DRAGON_ID)) tag.remove(DATA_DRAGON_ID);
        if (tag.contains(DATA_ACTION)) tag.remove(DATA_ACTION);
    }

    public static void setAction(StaffAction action, PlayerEntity player, ItemStack stack)
    {
        assertStaff(stack);
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt(DATA_ACTION, action.ordinal());
        getAction(stack).onSelected(getBoundDragon(player.level, stack), player, stack);
    }

    public static StaffAction getAction(ItemStack stack)
    {
        assertStaff(stack);
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_ACTION)) return StaffAction.VALUES[tag.getInt(DATA_ACTION)];
        }
        return StaffAction.DEFAULT;
    }

    public static void bindDragon(AbstractDragonEntity dragon, ItemStack stack)
    {
        assertStaff(stack);
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt(DATA_DRAGON_ID, dragon.getId());
    }

    @Nullable
    public static AbstractDragonEntity getBoundDragon(World level, ItemStack stack)
    {
        assertStaff(stack);
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_DRAGON_ID))
            {
                Entity entity = level.getEntity(tag.getInt(DATA_DRAGON_ID));
                if (entity instanceof AbstractDragonEntity) return ((AbstractDragonEntity) entity);
            }
        }
        return null;
    }

    public static void assertStaff(ItemStack stack)
    {
        if (!(stack.getItem() instanceof DragonStaffItem))
            throw new AssertionError(String.format("This isnt a staff wtf? [%s] Please contact the mod author", stack.getItem().getRegistryName()));
    }
}
