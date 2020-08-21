package WolfShotz.Wyrmroost.items.staff;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.RenderHelper;
import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public enum StaffAction
{
    DEFAULT
            {
                @Override
                public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    if (player.world.isRemote) StaffScreen.open(dragon, stack);
                    return true;
                }
            },

    INVENTORY
            {
                @Override
                public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    DragonStaffItem.setAction(DEFAULT, player, stack);
                    if (!player.world.isRemote)
                        NetworkHooks.openGui((ServerPlayerEntity) player, DragonInvContainer.getProvider(dragon), b -> b.writeInt(dragon.getEntityId()));
                }
            },

    SIT
            {
                @Override
                public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    dragon.setSitting(!dragon.isSitting());
                    DragonStaffItem.setAction(DEFAULT, player, stack);
                }

                @Override
                public String getTranslateKey(@Nullable AbstractDragonEntity dragon)
                {
                    if (dragon != null && dragon.isSitting()) return "item.wyrmroost.dragon_staff.action.sit.come";
                    return "item.wyrmroost.dragon_staff.action.sit.stay";
                }
            },

    HOME
            {
                @Override
                public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    if (dragon.getHomePos().isPresent())
                    {
                        dragon.clearHome();
                        DragonStaffItem.setAction(DEFAULT, player, stack);
                    }
                }

                @Override
                public boolean clickBlock(AbstractDragonEntity dragon, ItemUseContext context)
                {
                    BlockPos pos = context.getPos();
                    World world = context.getWorld();
                    ItemStack stack = context.getItem();
                    DragonStaffItem.setAction(DEFAULT, context.getPlayer(), stack);
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

                @Override
                public void render(AbstractDragonEntity dragon, MatrixStack ms, float partialTicks)
                {
                    RayTraceResult rtr = ClientEvents.getClient().objectMouseOver;
                    if (rtr instanceof BlockRayTraceResult)
                        RenderHelper.drawBlockPos(ms,
                                ((BlockRayTraceResult) rtr).getPos(),
                                dragon.world,
                                Math.cos((dragon.ticksExisted + partialTicks) * 0.2) * 4.5 + 4.5,
                                0x4d0000ff);
                }

                @Override
                public String getTranslateKey(@Nullable AbstractDragonEntity dragon)
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
                public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    dragon.clearAI();
                    dragon.setSitting(false);
                }

                @Override
                public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    RayTraceResult rtr = Mafs.rayTrace(dragon.world, player, TARGET_RANGE, false);
                    if (rtr instanceof EntityRayTraceResult)
                    {
                        EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
                        Entity entity = ertr.getEntity();
                        if (entity instanceof LivingEntity && dragon.shouldAttackEntity((LivingEntity) entity, player))
                        {
                            dragon.setAttackTarget((LivingEntity) entity);
                            ModUtils.playLocalSound(player.world, player.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, 1, 0.5f);
                        }
                    }
                    else if (rtr instanceof BlockRayTraceResult)
                    {
                        BlockRayTraceResult brtr = (BlockRayTraceResult) rtr;
                        BlockPos pos = brtr.getPos();
                        ModUtils.playLocalSound(dragon.world, player.getPosition(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, 1, 1);
                        for (int i = 0; i < 10; i++)
                            dragon.world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d, 0, Math.min(dragon.getRNG().nextDouble(), 0.25d), 0);
                        DragonStaffItem.setAction(DEFAULT, player, stack);
                    }
                    return true;
                }

                @Override
                public void render(AbstractDragonEntity dragon, MatrixStack ms, float partialTicks)
                {
                    RayTraceResult rtr = Mafs.rayTrace(dragon.world, ClientEvents.getPlayer(), TARGET_RANGE, false);
                    if (rtr instanceof EntityRayTraceResult)
                    {
                        Entity target = ((EntityRayTraceResult) rtr).getEntity();
                        if (target != dragon.getAttackTarget())
                            RenderHelper.renderEntityOutline(target, 255, 0, 0, (int) (MathHelper.cos((dragon.ticksExisted + partialTicks) * 0.2f) * 35 + 45));
                    }
                }
            };

    public static final StaffAction[] VALUES = values(); // cache
    private static final String TRANSLATE_PATH = "item.wyrmroost.dragon_staff.action.";

    public boolean clickBlock(AbstractDragonEntity dragon, ItemUseContext context) { return false; }

    public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) { return false; }

    public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) {}

    public void render(AbstractDragonEntity dragon, MatrixStack ms, float partialTicks) {}

    public String getTranslateKey(@Nullable AbstractDragonEntity dragon)
    {
        return TRANSLATE_PATH + name().toLowerCase();
    }
}