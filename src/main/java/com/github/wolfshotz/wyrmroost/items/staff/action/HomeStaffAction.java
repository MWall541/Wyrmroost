package com.github.wolfshotz.wyrmroost.items.staff.action;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HomeStaffAction implements StaffAction
{
    @Override
    public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        if (dragon.getHomePos().isPresent())
        {
            dragon.clearHome();
            DragonStaffItem.setAction(StaffActions.DEFAULT, player, stack);
        }
    }

    @Override
    public boolean clickBlock(TameableDragonEntity dragon, ItemUseContext context)
    {
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        DragonStaffItem.setAction(StaffActions.DEFAULT, context.getPlayer(), stack);
        if (level.getBlockState(pos).getMaterial().isSolid())
        {
            dragon.setHomePos(pos);
            ModUtils.playLocalSound(level, pos, SoundEvents.BEEHIVE_ENTER, 1, 1);
        }
        else
        {
            ModUtils.playLocalSound(level, pos, SoundEvents.REDSTONE_TORCH_BURNOUT, 1, 1);
            for (int i = 0; i < 10; i++)
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d, 0, i * 0.025, 0);
        }

        return true;
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
        if (dragon != null && dragon.getHomePos().isPresent())
            return TRANSLATE_PATH + "home.remove";
        return TRANSLATE_PATH + "home.set";
    }
}
