package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.content.io.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DragonStaffItem extends Item
{
    public static final String DATA_BOUND = "BoundDragon";

    public Mode mode;

    public DragonStaffItem() { super(ModUtils.itemBuilder().maxStackSize(1)); }

    public static void openGui(AbstractDragonEntity dragon)
    {
        if (dragon.world.isRemote) Minecraft.getInstance().displayGuiScreen(new StaffScreen(dragon));
    }

    /**
     * @param target the entity we're checking if is a dragon or not
     * @nullable Can return null if this isnt a dragon, or isnt tamed.
     */
    @Nullable
    public static AbstractDragonEntity isSuitableTarget(Entity target, PlayerEntity player)
    {
        if (!target.isAlive()) return null;
        AbstractDragonEntity dragon;
        if (target instanceof AbstractDragonEntity) dragon = (AbstractDragonEntity) target;
        else if (target instanceof MultiPartEntity) dragon = (AbstractDragonEntity) ((MultiPartEntity) target).host;
        else return null;
        if (dragon.getOwner() != player) return null;

        return dragon;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        AbstractDragonEntity dragon = isSuitableTarget(target, player);
        if (dragon == null) return false;
        openGui(dragon);
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity target)
    {
        AbstractDragonEntity dragon = isSuitableTarget(target, player);
        if (dragon == null) return false;
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt(DATA_BOUND, dragon.getEntityId());
        player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1, false);
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking())
        {
            clearBound(stack.getOrCreateTag());
            setMode(Mode.NONE);
            world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1, false);
            return ActionResult.resultSuccess(stack);
        }

        AbstractDragonEntity dragon = getBoundDragon(world, player.getHeldItem(hand).getOrCreateTag());
        if (dragon != null && mode.rightClick(this, dragon, player)) return ActionResult.resultSuccess(stack);

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        AbstractDragonEntity dragon = getBoundDragon(context.getWorld(), context.getPlayer().getHeldItem(context.getHand()).getOrCreateTag());
        if (dragon != null && mode.clickBlock(this, context, dragon)) return ActionResultType.SUCCESS;
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasEffect(ItemStack stack) { return stack.getOrCreateTag().contains(DATA_BOUND); }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public AbstractDragonEntity getBoundDragon(World world, CompoundNBT nbt)
    {
        Entity entity = world.getEntityByID(nbt.getInt(DATA_BOUND));
        if (entity instanceof AbstractDragonEntity) return (AbstractDragonEntity) entity;
        return null;
    }

    public void clearBound(CompoundNBT nbt) { nbt.remove(DATA_BOUND); }

    public enum Mode
    {
        NONE
                {
                    @Override
                    public boolean rightClick(DragonStaffItem staff, AbstractDragonEntity dragon, PlayerEntity player)
                    {
                        openGui(dragon);
                        return true;
                    }
                },

        HOMEPOS
                {
                    @Override
                    public boolean clickBlock(DragonStaffItem staff, ItemUseContext context, AbstractDragonEntity dragon)
                    {
                        BlockPos pos = context.getPos();
                        if (context.getWorld().getBlockState(pos).getMaterial().isSolid())
                        {
                            dragon.setHomePos(pos);
                            return true;
                        }
                        staff.setMode(NONE);
                        return false;
                    }
                },

        TARGET
                {
                    @Override
                    public boolean rightClick(DragonStaffItem staff, AbstractDragonEntity dragon, PlayerEntity player)
                    {
                        RayTraceResult rtr = QuikMaths.rayTrace(player.world, player, 15, false);
                        if (rtr.getType() == RayTraceResult.Type.ENTITY)
                        {
                            EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
                            if (ertr.getEntity() instanceof LivingEntity)
                            {
                                LivingEntity entity = (LivingEntity) ertr.getEntity();
                                if (dragon.shouldAttackEntity(entity, player))
                                {
                                    dragon.setAttackTarget(entity);
                                    return true;
                                }
                            }
                        }

                        if (rtr.getType() == RayTraceResult.Type.BLOCK)
                        {
                            BlockPos pos = ((BlockRayTraceResult) rtr).getPos();
                            for (int i = 0; i < 15; i++)
                                player.world.addParticle(ParticleTypes.SMOKE, pos.getX(), pos.getY() + 0.5d, pos.getZ(), 0, 0, 0);
                        }

                        player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1, 1, false);
                        staff.setMode(NONE);
                        return false;
                    }
                };

        public boolean clickBlock(DragonStaffItem staff, ItemUseContext context, AbstractDragonEntity dragon) { return false; }

        public boolean rightClick(DragonStaffItem staff, AbstractDragonEntity dragon, PlayerEntity player) { return false; }
    }
}
