package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SoulCrystalItem extends Item
{
    public SoulCrystalItem() {
        super(ModUtils.itemBuilder().maxStackSize(1));
        setRegistryName("soul_crystal");
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        World world = player.world;
        if (world.isRemote) return false;
        if (containsDragon(stack)) return false;
        if (!(target instanceof AbstractDragonEntity)) return false;
        AbstractDragonEntity dragon = (AbstractDragonEntity) target;
        if (dragon.getOwner() != player) return false;
        
        CompoundNBT tag = new CompoundNBT();
        target.writeAdditional(tag);
        tag.putString("entity", EntityType.getKey(dragon.getType()).toString());
        stack.setTag(tag);
        dragon.remove();
        player.setHeldItem(hand, stack);
        world.playSound(null, player.getPosition(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1, 1);
        
        return true;
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        if (!containsDragon(stack)) return super.onItemUse(context);
        World world = context.getWorld();
        AbstractDragonEntity entity = getEntity(stack, world);
        BlockPos pos = context.getPos().offset(context.getFace());
        
        // Spawn the entity on the server side only
        if (!world.isRemote) {
            entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            stack.setTag(null);
            world.addEntity(entity);
        }
        
        // Client Side Cosmetics
        if (world.isRemote) {
            PlayerEntity player = context.getPlayer();
            EntitySize size = entity.getSize(entity.getPose());
            
            player.swingArm(context.getHand());
            double posX = pos.getX() + (size.width + 0.5) * 0.1;
            double posY = pos.getY() + (size.height / 2);
            double posZ = pos.getZ() + (size.width + 0.5) * 0.1;
            for (int x = (int) -(size.width * 5); x < (int) (size.width * 5); ++x) {
                double sx = Math.cos(x / 3) * random.nextFloat();
                double sz = Math.sin(x / 3) * random.nextFloat();
                world.addParticle(ParticleTypes.PORTAL, posX, posY, posZ, sx, random.nextDouble() - 0.8, sz);
            }
        }
        world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT, 1, 1);
        
        return ActionResultType.SUCCESS;
    }
    
/*    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult result = rayTrace(world, player, RayTraceContext.FluidMode.NONE);
        if (!containsDragon(stack)) return super.onItemRightClick(world, player, hand);
        
        player.getCooldownTracker().setCooldown(stack.getItem(), 40);
        world.playSound(null, player.getPosition(), getEntity(stack,world).getIdleSound(), SoundCategory.AMBIENT, 0.5f, 0.5f);
        
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }*/
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (containsDragon(stack)) {
            AbstractDragonEntity dragon = getEntity(stack, world);
            
            tooltip.add(new StringTextComponent("Name: " + dragon.getName().getUnformattedComponentText()));
            tooltip.add(new StringTextComponent("Health: " + Math.round(dragon.getHealth()) / 2 + " ")
                                .appendSibling(new StringTextComponent(Character.toString('\u2764')).applyTextStyle(TextFormatting.DARK_RED)));
            tooltip.add(new StringTextComponent("Tamer: " + dragon.getOwner().getName().getUnformattedComponentText()));
        }
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ITextComponent name = super.getDisplayName(stack);
        
        if (containsDragon(stack))
            name.applyTextStyle(TextFormatting.AQUA).applyTextStyle(TextFormatting.ITALIC);
        
        return name;
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) { return containsDragon(stack); }
    
    private boolean containsDragon(ItemStack stack) { return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains("entity"); }
    
    private AbstractDragonEntity getEntity(ItemStack stack, World world) {
        EntityType type = EntityType.byKey(stack.getTag().getString("entity")).orElse(null);
        
        if (type == null) return null;
        AbstractDragonEntity entity = (AbstractDragonEntity) type.create(world);
        entity.read(stack.getTag());
        return entity;
    }
}
