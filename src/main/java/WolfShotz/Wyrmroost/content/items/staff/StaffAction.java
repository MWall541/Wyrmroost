package WolfShotz.Wyrmroost.content.items.staff;

import WolfShotz.Wyrmroost.client.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.content.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Optional;

public enum StaffAction
{
    DEFAULT
            {
                @Override
                public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> StaffScreen.open(dragon, stack));
                    return true;
                }
            },

    HOME_POS
            {
                @Override
                public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    if (dragon.getHomePos().isPresent())
                    {
                        dragon.setHomePos(Optional.empty());
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
                public String getTranslateKey(AbstractDragonEntity dragon)
                {
                    if (dragon.getHomePos().isPresent()) return "item.wyrmroost.dragon_staff.action.home.remove";
                    return "item.wyrmroost.dragon_staff.action.home.set";
                }
            },

    TARGETING
            {
                @Override
                public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    dragon.clearAI();
                    dragon.setSit(false);
                }

                @Override
                public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
                {
                    RayTraceResult rtr = QuikMaths.rayTrace(dragon.world, player, 20, false);
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
                    dragon.setSit(!dragon.isSitting());
                    DragonStaffItem.setAction(DEFAULT, player, stack);
                }

                @Override
                public String getTranslateKey(AbstractDragonEntity dragon)
                {
                    if (dragon.isSitting()) return "item.wyrmroost.dragon_staff.action.sit.come";
                    return "item.wyrmroost.dragon_staff.action.sit.stay";
                }
            };

    public static final StaffAction[] VALUES = values(); // cached for speed

    public boolean clickBlock(AbstractDragonEntity dragon, ItemUseContext context) { return false; }

    public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) { return false; }

    public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) {}

    public String getTranslateKey(AbstractDragonEntity dragon)
    {
        return "item.wyrmroost.dragon_staff.action." + toString().toLowerCase();
    }
}
