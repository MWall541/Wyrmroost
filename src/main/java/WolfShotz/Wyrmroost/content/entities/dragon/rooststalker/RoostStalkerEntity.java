package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.goals.ScavengeGoal;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.goals.StoleItemFlee;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.entityutils.PlayerMount;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.CommonEntityGoals;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import WolfShotz.Wyrmroost.util.io.ContainerBase;
import com.google.common.collect.Lists;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity implements PlayerMount.IHeadMount
{
    private static final Predicate<LivingEntity> TARGETS = target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity;

    public static final Animation SCAVENGE_ANIMATION = new Animation(35);

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(RoostStalkerEntity.class, DataSerializers.ITEMSTACK);

    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world)
    {
        super(stalker, world);

        moveController = new MovementController(this);
        stepHeight = 0;

        SLEEP_ANIMATION = new Animation(15);
        WAKE_ANIMATION = new Animation(15);
        
        setImmune(DamageSource.DROWN);
    }
    
    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, 1d, true));
        goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.2f, 8, 2));
        goalSelector.addGoal(9, new StoleItemFlee(this));
        goalSelector.addGoal(10, new DragonBreedGoal(this, false, false));
        goalSelector.addGoal(11, new ScavengeGoal(this, 1.1d, SCAVENGE_ANIMATION));
        goalSelector.addGoal(12, CommonEntityGoals.wanderAvoidWater(this, 1d));
        goalSelector.addGoal(13, CommonEntityGoals.lookAt(this, 5f));
        goalSelector.addGoal(14, CommonEntityGoals.lookRandomly(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this).setCallsForHelp());
        targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGETS));
    }

    @Override
    protected void registerData()
    {
        super.registerData();

        dataManager.register(ITEM, ItemStack.EMPTY);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        setItem(invHandler.map(i -> i.getStackInSlot(0)).orElse(ItemStack.EMPTY));

        // Data Fix - todo: remove later
        ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        if (!stack.isEmpty()) invHandler.ifPresent(i -> {
            setItem(stack);
            setStackInSlot(0, stack);
            setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
        });
    }

    public ItemStack getItem()
    {
        return dataManager.get(ITEM);
    }

    public void setItem(ItemStack item)
    {
        dataManager.set(ITEM, item);
        if (!item.isEmpty()) playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.5f, 1);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(10d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4d);
    }
    
    @Override
    public void livingTick()
    {
        super.livingTick();
        
        if (getHealth() < getMaxHealth() && getRNG().nextInt(400) != 0) return;
        
        ItemStack stack = getStackInSlot(0);
        
        if (stack.isEmpty()) return;
        if (isBreedingItem(stack))
        {
            eat(stack);
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.processInteract(player, hand, stack)) return true;
        
        ItemStack heldItem = getStackInSlot(0);
        Item item = stack.getItem();
        
        if (!isTamed() && Tags.Items.EGGS.contains(item) || item == WRItems.DRAGON_EGG.get())
        {
            eat(stack);
            if (tame(rand.nextInt(4) == 0, player))
                getAttribute(MAX_HEALTH).setBaseValue(20d);
            
            return true;
        }
        
        if (isTamed() && isOwner(player))
        {
            if (player.isSneaking())
            {
                setSit(!isSitting());
                
                return true;
            }
            
            if (stack.isEmpty() && heldItem.isEmpty() && !PlayerMount.hasHeadOccupant(player))
            {
                setSit(false);
                startRiding(player, true);
                
                return true;
            }
            
            if (stack.isEmpty() || canPickUpStack(stack))
            {
                setItem(stack);
                setStackInSlot(0, stack);
                player.setHeldItem(hand, heldItem);
                
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.GOLD_NUGGET;
    }
    
    @Override
    public void updateRidden()
    {
        super.updateRidden();
        
        Entity entity = getRidingEntity();
        
        if (!entity.isAlive())
        {
            stopRiding();
            return;
        }
        if (!(entity instanceof PlayerEntity)) return;
        
        PlayerEntity player = (PlayerEntity) entity;
        
        if (player.isSneaking() && !player.abilities.isFlying)
        {
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
    
    @Override
    public int getSpecialChances()
    {
        return 185;
    }
    
    @Override
    // Override normal dragon body controller to allow rotations while sitting: its small enough for it, why not. :P
    protected BodyController createBodyController()
    {
        return new BodyController(this);
    }
    
    @Override
    public boolean canFly()
    {
        return false;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.STALKER_IDLE.get();
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.STALKER_HURT.get();
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.STALKER_DEATH.get();
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public List<Item> getFoodItems()
    {
        return Lists.newArrayList(Items.BEEF, Items.COOKED_BEEF, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.CHICKEN, Items.COOKED_CHICKEN, Items.MUTTON, Items.COOKED_MUTTON, WRItems.FOOD_COMMON_MEAT_RAW.get(), WRItems.FOOD_COMMON_MEAT_COOKED.get());
    }
    
    public boolean canPickUpStack(ItemStack stack)
    {
        return !(stack.getItem() instanceof BlockItem) && stack.getItem() != Items.GOLD_NUGGET;
    }
    
    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerBase.StalkerContainer(this, playerInv, windowID);
    }

    @Override
    public LazyOptional<ItemStackHandler> createInv() { return LazyOptional.of(() -> new ItemStackHandler(1)); }

    @Override
    public DragonEggProperties createEggProperties() { return new DragonEggProperties(0.25f, 0.35f, 6000); }
    
    @Override
    public Animation[] getAnimations()
    {
        return new Animation[]{NO_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, SCAVENGE_ANIMATION};
    }
}
