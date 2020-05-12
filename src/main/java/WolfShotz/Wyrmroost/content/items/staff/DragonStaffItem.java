package WolfShotz.Wyrmroost.content.items.staff;

import WolfShotz.Wyrmroost.client.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class DragonStaffItem extends Item
{
    public static final String DATA_DRAGON_ID = "BoundDragon"; // int
    public static final String DATA_ACTION = "Action"; // int

    public DragonStaffItem() { super(ModUtils.itemBuilder().maxStackSize(1)); }

    public static void setAction(StaffAction action, PlayerEntity player, ItemStack stack)
    {
        assertStaff(stack);
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt(DATA_ACTION, action.ordinal());
        getAction(stack).onSelected(getBoundDragon(player.world, stack), player, stack);
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
        nbt.putInt(DATA_DRAGON_ID, dragon.getEntityId());
    }

    @Nullable
    public static AbstractDragonEntity getBoundDragon(World world, ItemStack stack)
    {
        assertStaff(stack);
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_DRAGON_ID))
                return (AbstractDragonEntity) world.getEntityByID(tag.getInt(DATA_DRAGON_ID));
        }
        return null;
    }

    public static void reset(PlayerEntity player, ItemStack stack)
    {
        if (stack.hasTag()) stack.getTag().remove(DATA_DRAGON_ID);
        setAction(StaffAction.DEFAULT, player, stack);
    }

    public static void assertStaff(ItemStack stack)
    {
        if (!(stack.getItem() instanceof DragonStaffItem))
            throw new AssertionError(String.format("This isnt a staff wtf? [%s] Please contact the mod author", stack.getItem().getRegistryName()));
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
            if (dragon.isOwner(player))
            {
                bindDragon(dragon, stack);
                ModUtils.playLocalSound(player.world, player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Triggered when Right clicking an entity
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (target instanceof AbstractDragonEntity)
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) target;
            if (dragon.isOwner(playerIn))
            {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> StaffScreen.open(dragon, stack));
                return true;
            }
        }
        return false;
    }

    /**
     * Triggered when right clicked on a block
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (context.getPlayer().isSneaking()) return ActionResultType.PASS;
        ItemStack stack = context.getItem();
        AbstractDragonEntity dragon = getBoundDragon(context.getWorld(), stack);
        if (dragon != null && getAction(stack).clickBlock(dragon, context)) return ActionResultType.SUCCESS;
        return ActionResultType.PASS;
    }

    /**
     * Triggered when right clicked on air
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() && stack.hasTag() && stack.getTag().contains(DATA_DRAGON_ID))
        {
            reset(player, stack);
            ModUtils.playLocalSound(world, player.getPosition(), SoundEvents.BLOCK_SCAFFOLDING_STEP, 1, 1);
            return ActionResult.resultSuccess(stack);
        }

        AbstractDragonEntity dragon = getBoundDragon(world, stack);
        if (dragon != null && getAction(stack).rightClick(dragon, player, stack))
            return ActionResult.resultSuccess(stack);
        return ActionResult.resultPass(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.desc").applyTextStyle(TextFormatting.GRAY));
        if (stack.hasTag())
        {
            AbstractDragonEntity dragon = getBoundDragon(worldIn, stack);
            if (dragon != null)
                tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.bound", new StringTextComponent(dragon.getName().getUnformattedComponentText()).applyTextStyle(TextFormatting.AQUA)));
            tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.action", new TranslationTextComponent(getAction(stack).getTranslateKey(dragon)).applyTextStyle(TextFormatting.AQUA).getFormattedText()));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) { return getAction(stack) != StaffAction.DEFAULT; }

    /**
     * Not ideal whatsoever, but its literally the best we can do.
     * Storing the uuid of the dragons means we would have to get that dragon by uuid, which we cant on client,
     * and this item requires almost constant syncing.
     * The solution is to just remove everything.
     */
    @Override
    public boolean updateItemStackNBT(CompoundNBT nbt)
    {
        if (nbt.contains(DATA_DRAGON_ID)) nbt.remove(DATA_DRAGON_ID);
        nbt.putInt(DATA_ACTION, StaffAction.DEFAULT.ordinal());
        return false;
    }
}
