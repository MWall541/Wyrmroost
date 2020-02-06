package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.MathUtils;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.entityutils.multipart.MultiPartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
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
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity target)
    {
        AbstractDragonEntity dragon = getDragonTarget(target, player);
        if (dragon == null) return false;

        CompoundNBT tag = new CompoundNBT();
        tag.putUniqueId("boundID", dragon.getUniqueID());
        stack.setTag(tag);
        dragon.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
        return true;
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        AbstractDragonEntity dragon = getDragonTarget(target, player);
        if (dragon == null) return false;
        if (player.world.isRemote) return true;
        
        NetworkHooks.openGui((ServerPlayerEntity) player, dragon, buf -> buf.writeInt(dragon.getEntityId()));
        player.playSound(SoundEvents.UI_TOAST_IN, 1f, 1f);
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!isBound(stack)) return ActionResult.newResult(ActionResultType.PASS, stack);

        if (player.isSneaking()) // Clear Bounded dragon
        {
            stack.setTag(new CompoundNBT());
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        }
        
        RayTraceResult rtr = MathUtils.rayTrace(world, player, 50, true);
        AbstractDragonEntity dragon = getDragon(stack, ModUtils.getServerWorld(player));

        ModUtils.L.info(dragon.getHomePos().isPresent());

        if (rtr.getType() == RayTraceResult.Type.ENTITY)
        {
            EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
            if (!(ertr.getEntity() instanceof LivingEntity)) return ActionResult.newResult(ActionResultType.PASS, stack); // shouldnt attack any non living creatures...

            // Found an entity
            LivingEntity entity = (LivingEntity) ertr.getEntity();

            if (entity instanceof AbstractDragonEntity && ((AbstractDragonEntity) entity).getOwner() == player) // If the entity found is a dragon of ours, call it over
            {
                if (dragon.isFlying() && !world.isRemote)
                    dragon.getFlightMoveController().resetCourse().setMoveTo(player.posX - random.nextInt(3), Math.ceil(player.posY), player.posZ - random.nextInt(3), dragon.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getBaseValue());
                else if (dragon.isSitting()) dragon.setSit(false);
                return ActionResult.newResult(ActionResultType.SUCCESS, stack);
            }

            if (dragon.shouldAttackEntity(entity, dragon.getOwner())) // If the entity found is attackable, then go for it
            {
                dragon.setSit(false);
                dragon.setAttackTarget(entity);
                player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1f, 0.2f);
                return ActionResult.newResult(ActionResultType.SUCCESS, stack);
            }
        }
        else if (rtr.getType() == RayTraceResult.Type.BLOCK) // or, if the ray trace was a block, lets see if we can set a homepos
        {
            BlockRayTraceResult brtr = (BlockRayTraceResult) rtr;
            BlockPos homePos = brtr.getPos();
            if (!world.getBlockState(homePos).isSolid()) return ActionResult.newResult(ActionResultType.PASS, stack);

            if (!world.isRemote)
            {
                if (dragon.getHomePos().isPresent() && homePos.equals(dragon.getHomePos().get().down(1)))
                    dragon.setHomePos(Optional.empty());
                else dragon.setHomePos(homePos.up(1));
            }
            player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
            return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        }

        return ActionResult.newResult(ActionResultType.PASS, stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return isBound(stack);
    }

    /**
     * Is there a dragon bound to this staff?
     */
    public static boolean isBound(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag == null) return false;
        return tag.hasUniqueId("boundID");
    }

    /**
     * run a check using {@link #isBound} first as we could have no dragon(s) bound
     */
    @Nullable
    public static AbstractDragonEntity getDragon(ItemStack stack, ServerWorld world)
    {
        CompoundNBT tag = stack.getTag();
        return isBound(stack) ? (AbstractDragonEntity) world.getEntityByUuid(tag.getUniqueId("boundID")) : null;
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
