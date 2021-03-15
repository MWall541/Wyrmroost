package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.DragonEggStackRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DragonEggItem extends Item
{
    public DragonEggItem()
    {
        super(WRItems.builder().maxCount(1).setISTER(() -> DragonEggStackRenderer::new));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity)
    {
        if (!player.isCreative()) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof AbstractDragonEntity)) return false;

        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(DragonEggEntity.DATA_DRAGON_TYPE, EntityType.getId(entity.getType()).toString());
        nbt.putInt(DragonEggEntity.DATA_HATCH_TIME, DragonEggProperties.MAP.get(entity.getType()).getHatchTime());
        stack.setTag(nbt);

        player.sendMessage(getName(stack), true);
        return true;
    }

    @Override
    public ActionResultType useOnBlock(ItemUseContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        if (player.isSneaking()) return super.useOnBlock(ctx);

        World world = ctx.getWorld();
        CompoundNBT tag = ctx.getStack().getTag();
        BlockPos pos = ctx.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (tag == null || !tag.contains(DragonEggEntity.DATA_DRAGON_TYPE)) return ActionResultType.PASS;
        if (!state.getCollisionShape(world, pos).isEmpty()) pos = pos.offset(ctx.getSide());
        if (!world.getEntitiesIncludingUngeneratedChunks(DragonEggEntity.class, new AxisAlignedBB(pos)).isEmpty())
            return ActionResultType.FAIL;

        DragonEggEntity eggEntity = new DragonEggEntity(ModUtils.getEntityTypeByKey(tag.getString(DragonEggEntity.DATA_DRAGON_TYPE)), tag.getInt(DragonEggEntity.DATA_HATCH_TIME), world);
        eggEntity.updatePosition(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);

        if (!world.isClient) world.spawnEntity(eggEntity);
        if (!player.isCreative()) player.setStackInHand(ctx.getHand(), ItemStack.EMPTY);
        
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public ITextComponent getName(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag == null || tag.isEmpty()) return super.getName(stack);
        Optional<EntityType<?>> type = EntityType.get(tag.getString(DragonEggEntity.DATA_DRAGON_TYPE));
        
        if (type.isPresent())
        {
            String dragonTranslation = type.get().getName().getString();
            return new TranslationTextComponent(dragonTranslation + " ").append(new TranslationTextComponent(getTranslationKey()));
        }
        
        return super.getName(stack);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        CompoundNBT tag = stack.getTag();

        if (tag != null && tag.contains(DragonEggEntity.DATA_HATCH_TIME))
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt(DragonEggEntity.DATA_HATCH_TIME) / 1200).formatted(TextFormatting.AQUA));
        PlayerEntity player = ClientEvents.getPlayer();
        if (player != null && player.isCreative())
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.creativetooltip").formatted(TextFormatting.GRAY));
    }

    public static ItemStack getStack(EntityType<?> type)
    {
        return getStack(type, DragonEggProperties.MAP.get(type).getHatchTime());
    }

    public static ItemStack getStack(EntityType<?> type, int hatchTime)
    {
        ItemStack stack = new ItemStack(WRItems.DRAGON_EGG.get());
        CompoundNBT tag = new CompoundNBT();
        tag.putString(DragonEggEntity.DATA_DRAGON_TYPE, EntityType.getId(type).toString());
        tag.putInt(DragonEggEntity.DATA_HATCH_TIME, hatchTime);
        stack.setTag(tag);
        return stack;
    }
}
