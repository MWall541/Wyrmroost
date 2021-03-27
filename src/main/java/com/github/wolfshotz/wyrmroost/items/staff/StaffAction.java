package com.github.wolfshotz.wyrmroost.items.staff;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public enum StaffAction
{
    DEFAULT
            {
                @Override
                public boolean rightClick(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    if (player.level.isClientSide) StaffScreen.open(dragon, stack);
                    return true;
                }
            },

    INVENTORY
            {
                @Override
                public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    DragonStaffItem.setAction(DEFAULT, player, stack);
                    if (!player.level.isClientSide)
                        NetworkHooks.openGui((ServerPlayerEntity) player, DragonInvContainer.getProvider(dragon), b -> b.writeInt(dragon.getId()));
                }
            },

    SIT
            {
                @Override
                public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    dragon.setOrderedToSit(!dragon.isInSittingPose());
                    DragonStaffItem.setAction(DEFAULT, player, stack);
                }

                @Override
                public String getTranslateKey(@Nullable TameableDragonEntity dragon)
                {
                    if (dragon != null && dragon.isInSittingPose())
                        return "item.wyrmroost.dragon_staff.action.sit.come";
                    return "item.wyrmroost.dragon_staff.action.sit.stay";
                }
            },

    HOME
            {
                @Override
                public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    if (dragon.getHomePos().isPresent())
                    {
                        dragon.clearHome();
                        DragonStaffItem.setAction(DEFAULT, player, stack);
                    }
                }

                @Override
                public boolean clickBlock(TameableDragonEntity dragon, ItemUseContext context)
                {
                    BlockPos pos = context.getClickedPos();
                    World level = context.getLevel();
                    ItemStack stack = context.getItemInHand();
                    DragonStaffItem.setAction(DEFAULT, context.getPlayer(), stack);
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
            },

    TARGET
            {
                private static final int TARGET_RANGE = 40;

                @Override
                public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    dragon.clearAI();
                    dragon.clearHome();
                    dragon.setOrderedToSit(false);
                }

                @Override
                public boolean rightClick(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    EntityRayTraceResult ertr = rayTrace(player, dragon);
                    if (ertr != null)
                    {
                        dragon.setTarget((LivingEntity) ertr.getEntity());
                        if (player.level.isClientSide)
                            ModUtils.playLocalSound(player.level, player.blockPosition(), SoundEvents.BLAZE_SHOOT, 1, 0.5f);
                        return true;
                    }
                    return false;
                }

                @Override
                public void render(TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
                {
                    EntityRayTraceResult rtr = rayTrace(ClientEvents.getPlayer(), dragon);
                    if (rtr != null && rtr.getEntity() != dragon.getTarget())
                        RenderHelper.renderEntityOutline(rtr.getEntity(), 255, 0, 0, (int) (MathHelper.cos((dragon.tickCount + partialTicks) * 0.2f) * 35 + 45));
                }

                @Nullable
                private EntityRayTraceResult rayTrace(PlayerEntity player, TameableDragonEntity dragon)
                {
                    return Mafs.rayTraceEntities(player,
                            TARGET_RANGE,
                            e -> e instanceof LivingEntity && dragon.wantsToAttack((LivingEntity) e, dragon.getOwner()));
                }
            };

    public static final StaffAction[] VALUES = values(); // cache
    private static final String TRANSLATE_PATH = "item.wyrmroost.dragon_staff.action.";

    public boolean clickBlock(TameableDragonEntity dragon, ItemUseContext context)
    {
        return false;
    }

    public boolean rightClick(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
        return false;
    }

    public void onSelected(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack)
    {
    }

    public void render(TameableDragonEntity dragon, MatrixStack ms, float partialTicks)
    {
    }

    protected String getTranslateKey(@Nullable TameableDragonEntity dragon)
    {
        return TRANSLATE_PATH + name().toLowerCase();
    }

    public TranslationTextComponent getTranslation(@Nullable TameableDragonEntity dragon)
    {
        return new TranslationTextComponent(getTranslateKey(dragon));
    }
}