package com.github.wolfshotz.wyrmroost.items.book.action;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.screen.TarragonTomeScreen;
import com.github.wolfshotz.wyrmroost.containers.BookContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.book.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class DefaultBookAction implements BookAction
{
    @Override
    public ActionResultType rightClick(@Nullable TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        boolean client = player.level.isClientSide;
        if (dragon != null)
        {
            if (!client) BookContainer.open((ServerPlayerEntity) player, dragon);
        }
        else if ((dragon = clip(player)) != null)
        {
            TarragonTomeItem.bind(dragon, stack);
            if (client)
            {
                ModUtils.playLocalSound(player.level, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.75f, 2f);
                ModUtils.playLocalSound(player.level, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundCategory.PLAYERS, 0.75f, 1f);
            }
        }
        else if (client) TarragonTomeScreen.open(player, stack);

        return ActionResultType.CONSUME;
    }

    @Override
    public void render(@Nullable TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
    {
        if (dragon == null && (dragon = clip(ClientEvents.getPlayer())) != null)
            RenderHelper.renderEntityOutline(dragon,
                    255,
                    255,
                    255,
                    (int) (MathHelper.cos((dragon.tickCount + partialTicks) * 0.2f) * 35 + 45));
    }

    @Nullable
    private TameableDragonEntity clip(PlayerEntity player)
    {
        EntityRayTraceResult ertr = Mafs.clipEntities(player, 40, 0.75, e -> e instanceof TameableDragonEntity && ((TameableDragonEntity) e).isOwnedBy(player));
        return ertr != null? (TameableDragonEntity) ertr.getEntity() : null;
    }

    @Override
    public String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        return TRANSLATE_PATH + "default";
    }
}
