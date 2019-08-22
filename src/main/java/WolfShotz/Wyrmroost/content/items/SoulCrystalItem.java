package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TranslationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        if (!(target instanceof AbstractDragonEntity)) return false;
        AbstractDragonEntity dragon = (AbstractDragonEntity) target;
        if (dragon.getOwner() != player) return false;
    
        CompoundNBT tag = new CompoundNBT();
        target.writeAdditional(tag);
        tag.putString("entity", EntityType.getKey(dragon.getType()).toString());
        tag.putUniqueId("entityID", dragon.getUniqueID());
        stack.setTag(tag);
        dragon.remove();
        player.setHeldItem(hand, stack);
        player.swingArm(hand);
        world.playSound(null, player.getPosition(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1, 1);
        world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.AMBIENT, 0.02f, 1f);
    
        return true;
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        if (!containsDragon(stack)) return ActionResultType.PASS;
        World world = context.getWorld();
        Entity entity = getEntity(stack, world);
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
        
            player.swingArm(context.getHand());
            for (int x = -10; x < 11; ++x) {
                double sx = Math.cos(x / 3) * random.nextFloat();
                double sz = Math.sin(x / 3) * random.nextFloat();
                world.addParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, sx, random.nextDouble() - 0.8, sz);
            }
        }
        world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT, 1, 1);
    
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) { return containsDragon(stack); }
    
    private boolean containsDragon(ItemStack stack) { return !stack.isEmpty() && stack.hasTag() && stack.getTag().hasUniqueId("entityID"); }
    
    private Entity getEntity(ItemStack stack, World world) {
        EntityType type = EntityType.byKey(stack.getTag().getString("entity")).orElse(null);
        
        if (type == null) return null;
        Entity entity = type.create(world);
        entity.read(stack.getTag());
        return entity;
    }
}
