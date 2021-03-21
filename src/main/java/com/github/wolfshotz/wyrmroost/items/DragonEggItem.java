package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.DragonEggStackRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggEntity;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggProperties;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DragonEggItem extends Item
{
    public DragonEggItem()
    {
        super(WRItems.builder().stacksTo(1).setISTER(() -> DragonEggStackRenderer::new));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity)
    {
        if (!player.isCreative()) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof TameableDragonEntity)) return false;

        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(DragonEggEntity.DATA_DRAGON_TYPE, EntityType.getKey(entity.getType()).toString());
        nbt.putInt(DragonEggEntity.DATA_HATCH_TIME, DragonEggProperties.get(entity.getType()).getHatchTime());
        stack.setTag(nbt);

        player.displayClientMessage(getName(stack), true);
        return true;
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        if (player.isShiftKeyDown()) return super.useOn(ctx);

        World level = ctx.getLevel();
        CompoundNBT tag = ctx.getItemInHand().getTag();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (tag == null || !tag.contains(DragonEggEntity.DATA_DRAGON_TYPE)) return ActionResultType.PASS;
        if (!state.getCollisionShape(level, pos).isEmpty()) pos = pos.relative(ctx.getClickedFace());
        if (!level.getLoadedEntitiesOfClass(DragonEggEntity.class, new AxisAlignedBB(pos)).isEmpty())
            return ActionResultType.FAIL;

        DragonEggEntity eggEntity = new DragonEggEntity(ModUtils.getEntityTypeByKey(tag.getString(DragonEggEntity.DATA_DRAGON_TYPE)), tag.getInt(DragonEggEntity.DATA_HATCH_TIME), level);
        eggEntity.absMoveTo(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);

        if (!level.isClientSide) level.addFreshEntity(eggEntity);
        if (!player.isCreative()) player.setItemInHand(ctx.getHand(), ItemStack.EMPTY);

        return ActionResultType.SUCCESS;
    }
    
    @Override
    public ITextComponent getName(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag == null || tag.isEmpty()) return super.getName(stack);
        Optional<EntityType<?>> type = EntityType.byString(tag.getString(DragonEggEntity.DATA_DRAGON_TYPE));
        
        if (type.isPresent())
        {
            String dragonTranslation = type.get().getDescription().getString();
            return new TranslationTextComponent(dragonTranslation + " ").append(new TranslationTextComponent(getDescriptionId()));
        }
        
        return super.getName(stack);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        CompoundNBT tag = stack.getTag();

        if (tag != null && tag.contains(DragonEggEntity.DATA_HATCH_TIME))
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt(DragonEggEntity.DATA_HATCH_TIME) / 1200).withStyle(TextFormatting.AQUA));
        PlayerEntity player = ClientEvents.getPlayer();
        if (player != null && player.isCreative())
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.creativetooltip").withStyle(TextFormatting.GRAY));
    }

    public static ItemStack getStack(EntityType<?> type)
    {
        return getStack(type, DragonEggProperties.get(type).getHatchTime());
    }

    public static ItemStack getStack(EntityType<?> type, int hatchTime)
    {
        ItemStack stack = new ItemStack(WRItems.DRAGON_EGG.get());
        CompoundNBT tag = new CompoundNBT();
        tag.putString(DragonEggEntity.DATA_DRAGON_TYPE, EntityType.getKey(type).toString());
        tag.putInt(DragonEggEntity.DATA_HATCH_TIME, hatchTime);
        stack.setTag(tag);
        return stack;
    }
}
