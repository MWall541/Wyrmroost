package com.github.wolfshotz.wyrmroost.items.book.action;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class TargetBookAction implements BookAction
{
    @Override
    public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        dragon.clearAI();
        dragon.clearHome();
        dragon.setOrderedToSit(false);
    }

    @Override
    public ActionResultType rightClick(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        EntityRayTraceResult ertr = clip(player, dragon);
        if (ertr != null)
        {
            dragon.setTarget((LivingEntity) ertr.getEntity());
            if (player.level.isClientSide)
                ModUtils.playLocalSound(player.level, player.blockPosition(), SoundEvents.BLAZE_SHOOT, 1, 0.5f);
            return ActionResultType.sidedSuccess(player.level.isClientSide);
        }
        return ActionResultType.PASS;
    }

    @Override
    public void render(TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
    {
        EntityRayTraceResult rtr = clip(ClientEvents.getPlayer(), dragon);
        if (rtr != null && rtr.getEntity() != dragon.getTarget())
            RenderHelper.renderEntityOutline(rtr.getEntity(), 255, 0, 0, (int) (MathHelper.cos((dragon.tickCount + partialTicks) * 0.2f) * 35 + 45));
    }

    @Nullable
    private EntityRayTraceResult clip(PlayerEntity player, TameableDragonEntity dragon)
    {
        return Mafs.clipEntities(player,
                40,
                0.35,
                e -> e instanceof LivingEntity && dragon.wantsToAttack((LivingEntity) e, dragon.getOwner()));
    }

    @Override
    public String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        return TRANSLATE_PATH + "target";
    }
}
