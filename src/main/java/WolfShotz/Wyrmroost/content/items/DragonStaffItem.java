package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.entityhelpers.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
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
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class DragonStaffItem extends Item
{
    public DragonStaffItem() { super(ModUtils.itemBuilder().maxStackSize(1)); }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity target) {
        AbstractDragonEntity dragon = getDragonTarget(target, player);
        if (dragon == null) return false;
        
        CompoundNBT tag = new CompoundNBT();
        tag.putUniqueId("boundID", dragon.getUniqueID());
        stack.setTag(tag);
        dragon.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
        return true;
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (player.world.isRemote) return false;
        AbstractDragonEntity dragon = getDragonTarget(target, player);
        if (dragon == null) return false;
        
        NetworkHooks.openGui((ServerPlayerEntity) player, dragon, buf -> buf.writeInt(dragon.getEntityId()));
        player.playSound(SoundEvents.UI_TOAST_IN, 1f, 1f);
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        if (player.isSneaking() && isBound(stack)) { // Clear Bounded dragon
            stack.getTag().remove("boundID");
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        if (world.isRemote) return new ActionResult<>(ActionResultType.SUCCESS, stack);
        
        // Get entities at crosshair by ratrace, have bounded dragon attack that entity.
        RayTraceResult rtr = MathUtils.rayTrace(world, player, 50, true);
        if (rtr.getType() != RayTraceResult.Type.ENTITY) return ModUtils.passAction(stack);
        EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
        if (!(ertr.getEntity() instanceof LivingEntity)) return ModUtils.passAction(stack);
        LivingEntity entity = (LivingEntity) ertr.getEntity();
        AbstractDragonEntity dragon = getDragon(stack, (ServerWorld) world);
    
        if (entity instanceof AbstractDragonEntity && ((AbstractDragonEntity) entity).getOwner() == player) {
            if (dragon.isFlying())
                dragon.getFlightMoveController().resetCourse().setMoveTo(player.posX - random.nextInt(3), Math.ceil(player.posY), player.posZ - random.nextInt(3), dragon.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getBaseValue());
            else if (dragon.isSitting()) dragon.setSit(false);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        if (dragon.shouldAttackEntity(entity, dragon.getOwner())) {
            dragon.setSit(false);
            dragon.setAttackTarget(entity);
            player.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1f, 0.2f);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
    
        return ModUtils.passAction(stack);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) { return isBound(stack); }
    
    public boolean isBound(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag == null) return false;
        return tag.hasUniqueId("boundID");
    }
    
    /**
     * run a check using {@link #isBound} first
     */
    public AbstractDragonEntity getDragon(ItemStack stack, ServerWorld world) {
        assert (isBound(stack));
        CompoundNBT tag = stack.getTag();
        return (AbstractDragonEntity) world.getEntityByUuid(tag.getUniqueId("boundID"));
    }
    
    /**
     * @param target the entity were checking if is a dragon or not
     * @nullable Can return null if this isnt a dragon, or isnt tamed.
     */
    @Nullable
    public AbstractDragonEntity getDragonTarget(Entity target, PlayerEntity player) {
        if (!target.isAlive()) return null;
        AbstractDragonEntity dragon;
        if (target instanceof AbstractDragonEntity) dragon = (AbstractDragonEntity) target;
        else if (target instanceof MultiPartEntity) dragon = (AbstractDragonEntity) ((MultiPartEntity) target).host;
        else return null;
        if (dragon.getOwner() != player) return null;
    
        return dragon;
    }
}
