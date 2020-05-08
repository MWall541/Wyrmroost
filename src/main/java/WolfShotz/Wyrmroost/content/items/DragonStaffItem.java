package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.io.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DragonStaffItem extends Item
{
    public static final String DATA_DRAGON_ID = "BoundDragon";
    public static final String DATA_ACTION = "Action";

    public DragonStaffItem() { super(ModUtils.itemBuilder().maxStackSize(1)); }

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
                boundDragon(dragon, stack);
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
            if (dragon.isOwner(target))
            {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> StaffScreen.open(dragon, stack, this));
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
        ItemStack stack = context.getItem();
        AbstractDragonEntity dragon = getBoundDragon(context.getWorld(), stack);
        if (dragon != null && getAction(stack).clickBlock(dragon, context)) return ActionResultType.SUCCESS;
        return ActionResultType.PASS;
    }

    /**
     * Triggered when right clicked on air
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        AbstractDragonEntity dragon = getBoundDragon(worldIn, stack);
        if (dragon != null && getAction(stack).rightClick(dragon, playerIn, stack))
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
                tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.bound", dragon.getName().getFormattedText()));
            tooltip.add(new TranslationTextComponent("item.wyrmroost.dragon_staff.action", new StringTextComponent(getAction(stack).toString().toLowerCase()).applyTextStyle(TextFormatting.AQUA).getFormattedText()));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) { return getAction(stack) != Action.DEFAULT; }

    public void setAction(Action action, World world, ItemStack stack)
    {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt(DATA_ACTION, action.ordinal());
        getAction(stack).onSelected(getBoundDragon(world, stack));
    }

    public Action getAction(ItemStack stack)
    {
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_ACTION)) return Action.VALUES[tag.getInt(DATA_ACTION)];
        }
        return Action.DEFAULT;
    }

    public void boundDragon(AbstractDragonEntity dragon, ItemStack stack)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt(DATA_DRAGON_ID, dragon.getEntityId());
    }

    @Nullable
    public AbstractDragonEntity getBoundDragon(World world, ItemStack stack)
    {
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_DRAGON_ID))
                return (AbstractDragonEntity) world.getEntityByID(tag.getInt(DATA_DRAGON_ID));
        }
        return null;
    }

    public void reset(World world, ItemStack stack)
    {
        if (stack.hasTag()) stack.getTag().remove(DATA_DRAGON_ID);
        setAction(Action.DEFAULT, world, stack);
    }

    public enum Action
    {
        DEFAULT
                {
                    @Override
                    public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                    {
                        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> StaffScreen.open(dragon, stack, (DragonStaffItem) stack.getItem()));
                        return true;
                    }
                },

        HOME_POS
                {
                    @Override
                    public void onSelected(AbstractDragonEntity dragon) { dragon.setHomePos(Optional.empty()); }

                    @Override
                    public boolean clickBlock(AbstractDragonEntity dragon, ItemUseContext context)
                    {
                        BlockPos pos = context.getPos();
                        World world = context.getWorld();
                        ItemStack stack = context.getItem();
                        ((DragonStaffItem) stack.getItem()).setAction(DEFAULT, world, stack);
                        if (world.getBlockState(pos).getMaterial().isSolid())
                        {
                            dragon.setHomePos(pos);
                            ModUtils.playLocalSound(world, pos, SoundEvents.BLOCK_BEEHIVE_ENTER, 1, 1);
                        }
                        else
                        {
                            ModUtils.playLocalSound(world, pos, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, 1, 1);
                            for (int i = 0; i < 10; i++)
                                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d, 0, Math.min(dragon.getRNG().nextDouble(), 0.25d), 0);
                        }

                        return true;
                    }
                };

        public static final Action[] VALUES = values(); // cached for speed

        public boolean clickBlock(AbstractDragonEntity dragon, ItemUseContext context) { return false; }

        public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) { return false; }

        public void onSelected(AbstractDragonEntity dragon) {}
    }
}
