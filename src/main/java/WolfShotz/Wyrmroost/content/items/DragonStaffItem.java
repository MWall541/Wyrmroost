package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class DragonStaffItem extends Item
{
    public DragonStaffItem() {
        super(ModUtils.itemBuilder().maxStackSize(1));
        setRegistryName("dragon_staff");
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (!target.isAlive()) return false;
        if (!(target instanceof AbstractDragonEntity)) return false;
        
        AbstractDragonEntity dragon = (AbstractDragonEntity) target;
        World world = player.world;
        
        System.out.println(isBound(stack));
        if (isBound(stack) && !world.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, dragon, buf -> buf.writeInt(dragon.getEntityId()));
            return true;
        }
        
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("bound", target.getEntityId());
        stack.setTag(tag);
        player.setHeldItem(hand, stack);
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        boolean isBound = isBound(stack);
        
        if (isBound && player.isSneaking()) {
            stack.getTag().remove("bound");
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        RayTraceResult rtr = MathUtils.rayTrace(world, player, 50, true);
        
        if (rtr.getType() != RayTraceResult.Type.ENTITY) return new ActionResult<>(ActionResultType.FAIL, stack);
        
        EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
        Entity entityResult = ertr.getEntity();
        
        if (entityResult instanceof AbstractDragonEntity && ((AbstractDragonEntity) entityResult).isOwner(player)) {
            ((AbstractDragonEntity) entityResult).callDragon(player);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        else if (isBound && entityResult instanceof LivingEntity) {
            AbstractDragonEntity ownedDragon = getDragon(stack, world);
            if (ownedDragon.shouldAttackEntity((LivingEntity) entityResult, player)) ownedDragon.setAttackTarget((LivingEntity) entityResult);
            player.playSound(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1f, 1f);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        
        return new ActionResult<>(ActionResultType.PASS, stack);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote && isBound(stack)) getDragon(stack, world).setGlowing(isSelected);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) { return isBound(stack); }
    
    public boolean isBound(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag == null) return false;
        return tag.contains("bound");
    }
    
    public AbstractDragonEntity getDragon(ItemStack stack, World world) {
        assert (isBound(stack));
        CompoundNBT tag = stack.getTag();
        return (AbstractDragonEntity) world.getEntityByID(tag.getInt("bound"));
    }
}
