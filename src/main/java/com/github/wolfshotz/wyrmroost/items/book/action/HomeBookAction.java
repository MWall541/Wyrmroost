package com.github.wolfshotz.wyrmroost.items.book.action;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.book.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HomeBookAction implements BookAction
{
    @Override
    public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        if (dragon.hasRestriction())
        {
            dragon.clearHome();
            TarragonTomeItem.setAction(BookActions.DEFAULT, player, stack);
        }
        else if (player.level.isClientSide)
            player.displayClientMessage(new TranslationTextComponent(TRANSLATE_PATH + "home.set.info"), true);
    }

    @Override
    public ActionResultType clickBlock(TameableDragonEntity dragon, ItemUseContext context)
    {
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        TarragonTomeItem.setAction(BookActions.DEFAULT, context.getPlayer(), stack);
        if (level.getBlockState(pos).getMaterial().isSolid())
        {
            dragon.setHomePos(pos);
            ModUtils.playLocalSound(level, pos, SoundEvents.BEACON_POWER_SELECT, 0.75f, 2f);
            ModUtils.playLocalSound(level, pos, SoundEvents.BOOK_PAGE_TURN, 0.75f, 1f);
        }
        else
        {
            ModUtils.playLocalSound(level, pos, SoundEvents.REDSTONE_TORCH_BURNOUT, 0.75f, 1);
            for (int i = 0; i < 10; i++)
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d, 0, i * 0.025, 0);
        }

        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    @Override
    public void render(TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
    {
        RayTraceResult rtr = ClientEvents.getClient().hitResult;
        if (rtr instanceof BlockRayTraceResult)
            RenderHelper.drawBlockPos(ms,
                    ((BlockRayTraceResult) rtr).getBlockPos(),
                    Math.cos((dragon.tickCount + partialTicks) * 0.2) * 4.5 + 4.5,
                    0x4d0000ff,
                    true);
    }

    @Override
    public String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        if (dragon != null && dragon.hasRestriction())
            return TRANSLATE_PATH + "home.remove";
        return TRANSLATE_PATH + "home.set";
    }
}
