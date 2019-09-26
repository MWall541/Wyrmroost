package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.goals.ScavengeGoal;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.goals.StoleItemFlee;
import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.event.SetupSounds;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.WatchGoal;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity
{
    private static final Predicate<LivingEntity> TARGETS = target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity;
    
    public static final Animation SCAVENGE_ANIMATION = Animation.create(35);
    
    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world) {
        super(stalker, world);
        
        moveController = new MovementController(this);
        
        hatchTimer = 6000;
        stepHeight = 0;
        
        SLEEP_ANIMATION = Animation.create(15);
        WAKE_ANIMATION = Animation.create(15);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.2f, 8, 2));
        goalSelector.addGoal(8, new MeleeAttackGoal(this, 1d, true));
        goalSelector.addGoal(9, new StoleItemFlee(this));
        goalSelector.addGoal(10, new DragonBreedGoal(this, false));
        goalSelector.addGoal(11, new ScavengeGoal(this, 1.1d, SCAVENGE_ANIMATION));
        goalSelector.addGoal(12, new WaterAvoidingRandomWalkingGoal(this, 1d));
        goalSelector.addGoal(13, new WatchGoal(this, LivingEntity.class, 5f));
        goalSelector.addGoal(14, new LookRandomlyGoal(this));
    
        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this).setCallsForHelp());
        targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGETS));
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(10d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4d);
    }
    
    @Override
    public void livingTick() {
        super.livingTick();
        
        if (getHealth() < getMaxHealth() && getRNG().nextInt(400) != 0) return;
        
        ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        
        if (stack.isEmpty()) return;
        if (isBreedingItem(stack)) {
            stack.shrink(1);
            eat(stack);
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        if (hand != Hand.MAIN_HAND) return false; // Only fire on main hand
        
        ItemStack stack = player.getHeldItem(hand);
        ItemStack heldItem = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        
        // Apply Name if holding nametag
        if (stack.getItem() == Items.NAME_TAG) {
            stack.interactWithEntity(player, this, hand);
        
            return true;
        }
        
        // Change to tame if holding a dragon egg
        if (!isTamed() && stack.getItem() == SetupItems.dragonEgg) {
            eat(stack);
            if (tame(getRNG().nextBoolean(), player))
                getAttribute(MAX_HEALTH).setBaseValue(20d);
            
            return true;
        }
    
        if (isTamed()) {
        
            // if player is sneaking, sit/stand
            if (player.isSneaking() && isOwner(player)) {
                setSit(!isSitting());
            
                return true;
            }
        
            if (!stack.isEmpty()) {
            
                // Breed with gold nuggets (Yeah idk, always blame Nova)
                if (getGrowingAge() == 0 && canBreed() && stack.getItem() == Items.GOLD_NUGGET) {
                    consumeItemFromStack(player, stack);
                    setInLove(player);
                    return true;
                }
            
                // Increase the age
                if (isChild() && isBreedingItem(stack)) {
                    consumeItemFromStack(player, stack);
                    ageUp((int)((float)(-getGrowingAge() / 20) * 0.1F), true);
                
                    return true;
                }
            
                if (canPickUpStack(stack) && isOwner(player)) {
                    // Swap mouth holding items
                    if (!heldItem.isEmpty()) player.setHeldItem(hand, heldItem);
                    else player.setHeldItem(hand, ItemStack.EMPTY);
                    setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
    
                    return true;
                } else return false;
            }
            
            // Retrieve the item in the mouth
            if (!heldItem.isEmpty() && isOwner(player)) {
                setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                player.setHeldItem(hand, heldItem);
                
                return true;
            } else if (player.getPassengers().isEmpty() && isOwner(player)) {
                setSit(false);
                startRiding(player, true);
    
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        
        Entity entity = getRidingEntity();
        
        if (!entity.isAlive()) {
            stopRiding();
            return;
        }
        if (!(entity instanceof PlayerEntity)) return;
        
        PlayerEntity player = (PlayerEntity) entity;
        
        if (player.isSneaking() && !player.abilities.isFlying) {
            stopRiding();
            return;
        }
        
        rotationYaw = player.rotationYawHead;
        rotationPitch = player.rotationPitch;
        setRotation(rotationYaw, rotationPitch);
        rotationYawHead = player.rotationYawHead;
        prevRotationYaw = player.rotationYawHead;
        setPosition(player.posX, player.posY + 1.8, player.posZ);
    }
    
    protected void spawnDrops(DamageSource src) {
        ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        
        if (!stack.isEmpty()) {
            entityDropItem(stack);
            setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
        }
        
        super.spawnDrops(src);
    }
    
    @Override // Override normal dragon body controller to allow rotations while sitting: its small enough for it, why not. :P
    protected BodyController createBodyController() { return new BodyController(this); }
    
    /**
     * Set The chances this dragon can be a special.
     * Set it to 0 to have no chance.
     * Lower values have greater chances.
     */
    @Override
    public int getSpecialChances() { return 150; }
    
    @Override
    public boolean canFly() { return false; }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SetupSounds.STALKER_IDLE; }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SetupSounds.STALKER_HURT; }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return SetupSounds.STALKER_DEATH; }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.EGG, SetupItems.dragonEgg, Items.BEEF, Items.COOKED_BEEF, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.CHICKEN, Items.COOKED_CHICKEN, Items.MUTTON, Items.COOKED_MUTTON}; }
    
    public boolean canPickUpStack(ItemStack stack) {
        return !(stack.getItem() instanceof BlockItem) && stack.getItem() != Items.GOLD_NUGGET && stack.getItem() != SetupItems.soulCrystal;
    }
    
    // == Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, SCAVENGE_ANIMATION}; }
    // ==
}
