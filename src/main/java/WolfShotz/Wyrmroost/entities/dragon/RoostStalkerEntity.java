package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity
{
    public static final int ITEM_SLOT = 0;
    private static final Predicate<LivingEntity> TARGETS = target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity;
    public static final Animation SCAVENGE_ANIMATION = new Animation(35);
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(RoostStalkerEntity.class, DataSerializers.ITEMSTACK);

    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world)
    {
        super(stalker, world);

        stepHeight = 0;

        setImmune(DamageSource.DROWN);

        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }
    
    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1d, true));
        goalSelector.addGoal(5, new MoveToHomeGoal(this));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.2f, 8, 2));
        goalSelector.addGoal(10, new DragonBreedGoal(this, false));
        goalSelector.addGoal(11, new ScavengeGoal(1.1d));
        goalSelector.addGoal(12, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(13, new LookAtGoal(this, LivingEntity.class, 5f));
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

        sleepTimer.add(isSleeping()? 0.08f : -0.15f);

        if (getHealth() < getMaxHealth() && !world.isRemote && getRNG().nextInt(400) == 0)
        {
            ItemStack stack = getStackInSlot(0);
            if (isFoodItem(stack)) eat(stack);
        }
    }
    
    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        ItemStack heldItem = getStackInSlot(0);
        Item item = stack.getItem();

        if (!isTamed() && Tags.Items.EGGS.contains(item))
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
                setSitting(!isSitting());
                
                return true;
            }

            if (stack.isEmpty() && heldItem.isEmpty() && player.getPassengers().size() < 3)
            {
                setSitting(false);
                startRiding(player, true);

                return true;
            }
            
            if (stack.isEmpty() || canPickUpStack(stack))
            {
                setStackInSlot(0, stack);
                player.setHeldItem(hand, heldItem);

                return true;
            }
        }
        return false;
    }

    @Override
    public void doSpecialEffects()
    {
        if (getVariant() == -1 && ticksExisted % 25 == 0)
        {
            double x = getPosX() * (getRNG().nextGaussian() * 0.5d) + 1;
            double y = getPosY() * (getRNG().nextDouble()) + 1;
            double z = getPosZ() * (getRNG().nextGaussian() * 0.5d) + 1;
            world.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == ITEM_SLOT) setItem(stack);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.INVENTORY);
        screen.addAction(StaffAction.TARGET);
        super.addScreenInfo(screen);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);
        container.addSlot(new SlotBuilder(getInvHandler(), ITEM_SLOT));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.GOLD_NUGGET; }

    @Override
    public EntitySize getSize(Pose poseIn) { return getType().getSize().scale(getRenderScale()); }

    @Override
    public int getVariantForSpawn()
    {
        return getRNG().nextInt(185) == 0? -1 : 0;
    }

    @Override
    // Override normal dragon body controller to allow rotations while sitting: its small enough for it, why not. :P
    protected BodyController createBodyController() { return new BodyController(this); }

    @Override
    public boolean canFly() { return false; }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_STALKER_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.ENTITY_STALKER_HURT.get(); }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.ENTITY_STALKER_DEATH.get(); }

    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }
    
    public boolean canPickUpStack(ItemStack stack) { return stack.getItem() != Items.GOLD_NUGGET; }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 1); }

    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, SCAVENGE_ANIMATION}; }

    class ScavengeGoal extends MoveToBlockGoal
    {
        private IInventory chest;
        private int searchDelay = 20 + new Random().nextInt(40) + 5;

        public ScavengeGoal(double speed)
        {
            super(RoostStalkerEntity.this, speed, 16);
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
                    AnimationPacket.send(RoostStalkerEntity.this, SCAVENGE_ANIMATION);

                if (chest == null) return;
                if (chest instanceof ChestTileEntity && ((ChestTileEntity) chest).numPlayersUsing == 0)
                    interactChest(chest, true);
                if (!chest.isEmpty() && --searchDelay <= 0)
                {
                    int index = getRNG().nextInt(chest.getSizeInventory());
                    ItemStack stack = chest.getStackInSlot(index);

                    if (!stack.isEmpty() && canPickUpStack(stack))
                    {
                        chest.removeStackFromSlot(index);
                        setStackInSlot(ITEM_SLOT, stack);
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
