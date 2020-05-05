package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;

public class DragonStaffItem extends Item
{
    public DragonStaffItem()
    {
        super(ModUtils.itemBuilder().maxStackSize(1));
    }

    /**
     * run a check using {@link #isBound} first as we could have no dragon(s) bound
     */
    @Nullable
    public static AbstractDragonEntity getDragon(ItemStack stack, ServerWorld world)
    {
        CompoundNBT tag = stack.getTag();
        return isBound(stack)? (AbstractDragonEntity) world.getEntityByUuid(tag.getUniqueId("boundID")) : null;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        AbstractDragonEntity dragon = getDragonTarget(target, player);
        if (dragon == null) return false;
        if (player.world.isRemote) return true;

        try { NetworkHooks.openGui((ServerPlayerEntity) player, dragon, buf -> buf.writeInt(dragon.getEntityId())); }
        catch (NullPointerException e) {/* fail silently, no gui is found for this dragon.*/}
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity target)
    {
        AbstractDragonEntity dragon = getDragonTarget(target, player);
        if (dragon == null) return false;

        CompoundNBT tag = stack.getOrCreateTag();
        tag.putUniqueId("boundID", dragon.getUniqueID());
        stack.setTag(tag);
        player.world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1, false);
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (context.getWorld().isRemote) return ActionResultType.SUCCESS;
        BlockPos homePos = context.getPos();
        World world = context.getWorld();
        if (!world.getBlockState(homePos).isSolid()) return ActionResultType.PASS;

        PlayerEntity player = context.getPlayer();
        AbstractDragonEntity dragon = getDragon(context.getItem(), ModUtils.getServerWorld(player));

        if (dragon.getHomePos().isPresent() && homePos.equals(dragon.getHomePos().get().down(1)))
            dragon.setHomePos(Optional.empty());
        else dragon.setHomePos(homePos.up(1));

        player.sendStatusMessage(new StringTextComponent(String.format("Set Home Position to: %s, %s, %s",
                Math.ceil(homePos.getX()),
                Math.ceil(homePos.getY()),
                Math.ceil(homePos.getZ()))), true);
        dragon.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1, true);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasEffect(ItemStack stack) { return isBound(stack); }

    /**
     * Is there a dragon bound to this staff?
     */
    public static boolean isBound(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag == null) return false;
        return tag.hasUniqueId("boundID");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        // Clear Bounded Dragon
        if (player.isSneaking())
        {
            stack.setTag(new CompoundNBT());
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        }

        if (!world.isRemote)
        {
            // Raytrace targets
            RayTraceResult rtr = QuikMaths.rayTrace(world, player, 50, true);
            AbstractDragonEntity dragon = getDragon(stack, ModUtils.getServerWorld(player));

            if (rtr.getType() == RayTraceResult.Type.ENTITY)
            {
                EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;

                // Dragon Commanding
                AbstractDragonEntity dragonTarget = getDragonTarget(ertr.getEntity(), player);

                if (dragonTarget != null && dragonTarget.getOwner() == player)
                {
                    boolean flag = false;
                    if (dragonTarget.isFlying())
                    {
                        dragonTarget.tryTeleportToOwner();
                        world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1, 1);
                        flag = true;
                    }
                    else if (dragonTarget.isSitting())
                    {
                        dragonTarget.setSit(false);
                        flag = true;
                    }
                    if (flag)
                    {
                        world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1, 1);
                        return ActionResult.newResult(ActionResultType.SUCCESS, stack);
                    }
                }

                // Attack

                if (!isBound(stack)) return ActionResult.newResult(ActionResultType.PASS, stack);
                if (!(ertr.getEntity() instanceof LivingEntity))
                    return ActionResult.newResult(ActionResultType.PASS, stack); // shouldnt attack any non living creatures...

                LivingEntity entity = (LivingEntity) ertr.getEntity();

                if (dragon.shouldAttackEntity(entity, dragon.getOwner())) // If the entity found is attackable, then go for it
                {
                    dragon.setSit(false);
                    dragon.setAttackTarget(entity);
                    player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1f, 0.2f);
                    return ActionResult.newResult(ActionResultType.SUCCESS, stack);
                }
            }
        }

        return ActionResult.newResult(ActionResultType.PASS, stack);
    }

    /**
     * @param target the entity were checking if is a dragon or not
     * @nullable Can return null if this isnt a dragon, or isnt tamed.
     */
    @Nullable
    public static AbstractDragonEntity getDragonTarget(Entity target, PlayerEntity player)
    {
        if (!target.isAlive()) return null;
        AbstractDragonEntity dragon;
        if (target instanceof AbstractDragonEntity) dragon = (AbstractDragonEntity) target;
        else if (target instanceof MultiPartEntity) dragon = (AbstractDragonEntity) ((MultiPartEntity) target).host;
        else return null;
        if (dragon.getOwner() != player) return null;

        return dragon;
    }
}
