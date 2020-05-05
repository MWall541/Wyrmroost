package WolfShotz.Wyrmroost.content.entities.dragon;

import WolfShotz.Wyrmroost.content.entities.dragon.ai.goals.*;
import WolfShotz.Wyrmroost.content.entities.dragonegg.*;
import WolfShotz.Wyrmroost.registry.*;
import WolfShotz.Wyrmroost.util.entityutils.*;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.*;
import WolfShotz.Wyrmroost.util.io.*;
import WolfShotz.Wyrmroost.util.network.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity implements PlayerMount.IHeadMount
{
    private static final Predicate<LivingEntity> TARGETS = target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity;

    public static final Animation SCAVENGE_ANIMATION = new Animation(35);

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(RoostStalkerEntity.class, DataSerializers.ITEMSTACK);

    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world)
    {
        super(stalker, world);

        stepHeight = 0;
        SLEEP_ANIMATION = new Animation(15);
        WAKE_ANIMATION = new Animation(15);
        
        setImmune(DamageSource.DROWN);
    }
    
    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1d, true));
        goalSelector.addGoal(5, new MoveToHomeGoal(this));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.2f, 8, 2));
        goalSelector.addGoal(10, new DragonBreedGoal(this, false, false));
        goalSelector.addGoal(11, new ScavengeGoal(this, 1.1d));
        goalSelector.addGoal(12, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(13, CommonGoalWrappers.lookAt(this, 5f));
        goalSelector.addGoal(14, new LookRandomlyGoal(this));
        goalSelector.addGoal(9, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 7f, 1.15f, 1f)
        {
            @Override
            public boolean shouldExecute() { return !isTamed() && !getItem().isEmpty() && super.shouldExecute(); }
        });

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(4, new HurtByTargetGoal(this).setCallsForHelp());
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, AnimalEntity.class, false, true, TARGETS));
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

        dataManager.set(ITEM, invHandler.map(i -> i.getStackInSlot(0)).orElse(ItemStack.EMPTY));

        // Data Fix - todo: remove later
        ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        if (!stack.isEmpty()) invHandler.ifPresent(i ->
        {
            setItem(stack);
            setStackInSlot(0, stack);
            setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
        });
    }

    public ItemStack getItem() { return dataManager.get(ITEM); }

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
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.285d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4d);
    }
    
    @Override
    public void livingTick()
    {
        super.livingTick();
        
        if (getHealth() < getMaxHealth() && getRNG().nextInt(400) != 0) return;
        
        ItemStack stack = getStackInSlot(0);
        
        if (stack.isEmpty()) return;
        if (isBreedingItem(stack)) eat(stack);
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

            if (stack.isEmpty() && heldItem.isEmpty() && player.getPassengers().size() < 3)
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
    public Collection<Item> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }
    
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
        return new Animation[] {NO_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, SCAVENGE_ANIMATION};
    }

    class ScavengeGoal extends MoveToBlockGoal
    {
        private IInventory chest;
        private int searchDelay = 20 + new Random().nextInt(40) + 5;

        public ScavengeGoal(RoostStalkerEntity dragon, double speed)
        {
            super(dragon, speed, 16);
        }

        @Override
        public boolean shouldExecute()
        {
            return super.shouldExecute() && !isTamed() && !isSleeping() && isHandEmpty();
        }

        @Override
        public void startExecuting()
        {
            chest = getInventoryAtPosition();
            super.startExecuting();
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return super.shouldContinueExecuting() && invHandler.map(e -> e.getStackInSlot(0) == ItemStack.EMPTY).orElse(false) && chest != null && !chest.isEmpty();
        }

        @Override
        public void tick()
        {
            super.tick();

            if (getIsAboveDestination())
            {
                if (!isHandEmpty()) return;

                if (getAnimation() != animation)
                    NetworkUtils.sendAnimationPacket(RoostStalkerEntity.this, SCAVENGE_ANIMATION);

                if (chest == null) return;
                if (chest instanceof ChestTileEntity && ((ChestTileEntity) chest).numPlayersUsing == 0)
                    interactChest(chest, true);
                if (!chest.isEmpty() && --searchDelay <= 0)
                {
                    int index = new Random().nextInt(chest.getSizeInventory());
                    ItemStack stack = chest.getStackInSlot(index);

                    if (!stack.isEmpty() && canPickUpStack(stack))
                    {
                        chest.removeStackFromSlot(index);
                        invHandler.ifPresent(i -> i.setStackInSlot(0, stack));
                        setItem(stack);
                    }
                }
            }
        }

        @Override
        public void resetTask()
        {
            super.resetTask();
            interactChest(chest, false);
            searchDelay = 20 + new Random().nextInt(40) + 5;
        }

        /**
         * Returns the IInventory (if applicable) of the TileEntity at the specified position
         */
        @Nullable
        public IInventory getInventoryAtPosition()
        {
            IInventory iinventory = null;
            BlockState blockstate = world.getBlockState(destinationBlock);
            Block block = blockstate.getBlock();
            if (blockstate.hasTileEntity())
            {
                TileEntity tileentity = world.getTileEntity(destinationBlock);
                if (tileentity instanceof IInventory)
                {
                    iinventory = (IInventory) tileentity;
                    if (iinventory instanceof ChestTileEntity && block instanceof ChestBlock)
                        iinventory = ChestBlock.func_226916_a_((ChestBlock) block, blockstate, world, destinationBlock, true);
                }
            }

            return iinventory;
        }

        /**
         * Return true to set given position as destination
         */
        @Override
        protected boolean shouldMoveTo(IWorldReader world, BlockPos pos)
        {
            return world.getTileEntity(pos) instanceof IInventory;
        }

        /**
         * Used to handle the chest opening animation when being used by the scavenger
         */
        private void interactChest(IInventory intentory, boolean open)
        {
            if (!(intentory instanceof ChestTileEntity)) return; // not a chest, ignore it
            ChestTileEntity chest = (ChestTileEntity) intentory;

            chest.numPlayersUsing = open? 1 : 0;
            chest.getWorld().addBlockEvent(chest.getPos(), chest.getBlockState().getBlock(), 1, chest.numPlayersUsing);
        }

        private boolean isHandEmpty() { return getItem() == ItemStack.EMPTY; }

    }
}
