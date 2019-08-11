package WolfShotz.Wyrmroost.content.entities.rooststalker;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.content.entities.rooststalker.goals.ScavengeGoal;
import WolfShotz.Wyrmroost.event.SetupItems;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Predicate;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity
{
    private int eatTicks;
    private static final Predicate<LivingEntity> TARGETS = target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity;
    
    public static final Animation SIT_ANIMATION = Animation.create(15);
    
    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world) {
        super(stalker, world);
        
        moveController = new MovementController(this);
        
        hatchTimer = 2400;
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.2f, 8, 1));
        goalSelector.addGoal(8, new MeleeAttackGoal(this, 1d, true));
        goalSelector.addGoal(9, new DragonBreedGoal(this));
        goalSelector.addGoal(10, new ScavengeGoal(this, 0.8d));
        goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 1d));
        goalSelector.addGoal(12, new LookAtGoal(this, LivingEntity.class, 5f));
        goalSelector.addGoal(13, new LookRandomlyGoal(this));
    
        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this).setCallsForHelp(RoostStalkerEntity.class));
        targetSelector.addGoal(1, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGETS));
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(20d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(3d);
    }
    
    @Override
    public void livingTick() {
        super.livingTick();
        
        if (eatTicks > 0) {
            --eatTicks;
            
            
            return;
        }
        
        if (getRNG().nextInt(350) != 0 && eatTicks == 0) return;
        
        ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        
        if (stack.isEmpty()) return;
        if (stack.getItem().isFood()) {
            Food food = stack.getItem().getFood();
            
            if (food.isMeat()) eatTicks = 50;
            
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
        if (!isTamed() && stack.getItem() == SetupItems.egg) {
            tame(getRNG().nextBoolean(), player);
            consumeItemFromStack(player, stack);
            
            return true;
        }
    
        if (isTamed()) {
        
            // if player is sneaking, sit/stand
            if (player.isSneaking()) {
                setSit(!isSitting());
            
                return true;
            }
        
            if (!stack.isEmpty()) {
            
                // if were low on health, then heal
                if (getHealth() < getMaxHealth() && isBreedingItem(stack)) {
                    heal(2);
                    if (world.isRemote) {
                        for (int i=0; i < 6; ++i) {
                            double x = posX + getRNG().nextInt(2) - 1;
                            double y = posY + getRNG().nextDouble();
                            double z = posZ + getRNG().nextInt(2) - 1;
                            world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
                        }
                    }
                    consumeItemFromStack(player, stack);
                
                    return true;
                }
            
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
            
                // Swap mouth holding items
                if (!heldItem.isEmpty()) player.setHeldItem(hand, heldItem);
                else player.setHeldItem(hand, ItemStack.EMPTY);
                setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
            
                return true;
            }
            
            // Retrieve the item in the mouth
            if (!heldItem.isEmpty()) {
                setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                player.setHeldItem(hand, heldItem);
                
                return true;
            } else
                if (player.getPassengers().isEmpty()) {
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
        
        if (player.isSneaking()) {
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
    
    /**
     * Set The chances this dragon can be an albino.
     * Set it to 0 to have no chance.
     * Lower values have greater chances.
     */
    @Override
    public int getAlbinoChances() { return 50; }
    
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.EGG, SetupItems.egg, Items.BEEF, Items.COOKED_BEEF, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.CHICKEN, Items.COOKED_CHICKEN, Items.MUTTON, Items.COOKED_MUTTON}; }
    
    // == Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION}; }
    // ==
}
