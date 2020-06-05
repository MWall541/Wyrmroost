package WolfShotz.Wyrmroost.items.staff;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.RenderEvents;
import WolfShotz.Wyrmroost.client.render.StaffRenderer;
import WolfShotz.Wyrmroost.client.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.QuikMaths;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class StaffAction
{
    public static final List<StaffAction> ACTIONS = Lists.newArrayList();

    public static final StaffAction DEFAULT = new StaffAction()
    {
        @Override
        public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
        {
            if (player.world.isRemote) StaffScreen.open(dragon, stack);
            return true;
        }
    };
    public static final StaffAction INVENTORY = new StaffAction("inventory")
    {
        @Override
        public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
        {
            DragonStaffItem.setAction(DEFAULT, player, stack);
            if (!player.world.isRemote)
                NetworkHooks.openGui((ServerPlayerEntity) player, DragonInvContainer.getProvider(dragon), b -> b.writeInt(dragon.getEntityId()));
        }
    };
    public static final StaffAction SIT = new StaffAction()
    {
        @Override
        public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
        {
            dragon.setSit(!dragon.isSitting());
            DragonStaffItem.setAction(DEFAULT, player, stack);
        }

        @Override
        public String getTranslateKey(@Nullable AbstractDragonEntity dragon)
        {
            if (dragon != null && dragon.isSitting()) return "item.wyrmroost.dragon_staff.action.sit.come";
            return "item.wyrmroost.dragon_staff.action.sit.stay";
        }
    };
    public static final StaffAction HOME_POS = new StaffAction()
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
        public void render(AbstractDragonEntity dragon, MatrixStack ms)
        {
            RayTraceResult rtr = ClientEvents.getMinecraft().objectMouseOver;
            if (rtr instanceof BlockRayTraceResult)
                RenderEvents.drawBlockPos(ms, ((BlockRayTraceResult) rtr).getPos(), dragon.world, 7, 0x4d0000ff);
        }

        @Override
        public String getTranslateKey(@Nullable AbstractDragonEntity dragon)
        {
            if (dragon != null && dragon.getHomePos().isPresent())
                return "item.wyrmroost.dragon_staff.action.home.remove";
            return "item.wyrmroost.dragon_staff.action.home.set";
        }
    };
    public static final StaffAction TARGETING = new StaffAction("targeting")
    {
        public static final int TARGET_RANGE = 20;

        @Override
        public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
        {
            dragon.clearAI();
            dragon.setSit(false);
        }

        @Override
        public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack)
        {
            RayTraceResult rtr = QuikMaths.rayTrace(dragon.world, player, TARGET_RANGE, false);
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
        public void render(AbstractDragonEntity dragon, MatrixStack ms)
        {
            RayTraceResult rtr = QuikMaths.rayTrace(dragon.world, ClientEvents.getPlayer(), TARGET_RANGE, false);
            if (rtr instanceof EntityRayTraceResult)
                StaffRenderer.outlineEntitiesQueue.add(((EntityRayTraceResult) rtr).getEntity());
        }
    };
    public final String name;

    public StaffAction(String name)
    {
        this.name = name.toLowerCase();
        ACTIONS.add(this);
    }

    public StaffAction() { this("default"); }

    // ===
    // @formatter:off
    // ===

    public boolean clickBlock(AbstractDragonEntity dragon, ItemUseContext context) { return false; }

    public boolean rightClick(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) { return false; }

    public void onSelected(AbstractDragonEntity dragon, PlayerEntity player, ItemStack stack) {}

    public void render(AbstractDragonEntity dragon, MatrixStack ms) {}

    public String getTranslateKey(@Nullable AbstractDragonEntity dragon) { return "item.wyrmroost.dragon_staff.action." + name; }
}
