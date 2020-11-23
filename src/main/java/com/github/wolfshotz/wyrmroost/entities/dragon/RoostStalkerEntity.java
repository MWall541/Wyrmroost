package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.containers.util.SlotBuilder;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInvHandler;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.SleepController;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity
{
    public static final int ITEM_SLOT = 0;
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(RoostStalkerEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<Boolean> SCAVENGING = EntityDataManager.createKey(RoostStalkerEntity.class, DataSerializers.BOOLEAN);

    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world)
    {
        super(stalker, world);

        stepHeight = 0;

        setImmune(DamageSource.DROWN); // tf

        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected SleepController createSleepController()
    {
        return new SleepController(this).setHomeDefender();
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        if (isTamed())
        {
            goalSelector.addGoal(5, new MoveToHomeGoal(this));
            goalSelector.addGoal(6, new WRFollowOwnerGoal(this));

            targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
            targetSelector.addGoal(3, new DefendHomeGoal(this));
        }
        else
        {
            goalSelector.addGoal(8, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 7f, 1.15f, 1f)
            {
                @Override
                public boolean shouldExecute()
                {
                    return !getItem().isEmpty() && super.shouldExecute();
                }
            });
            goalSelector.addGoal(9, new ScavengeGoal(1.1d));

            targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity));
        }

        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1d, true));
        goalSelector.addGoal(7, new BreedGoal());
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 5f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));

        targetSelector.addGoal(4, new HurtByTargetGoal(this).setCallsForHelp());
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(ITEM, ItemStack.EMPTY);
        dataManager.register(SCAVENGING, false);
    }

    public ItemStack getItem() { return dataManager.get(ITEM); }

    private boolean hasItem() { return getItem() != ItemStack.EMPTY; }

    public void setItem(ItemStack item)
    {
        dataManager.set(ITEM, item);
        if (!item.isEmpty()) playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.5f, 1);
    }

    public boolean isScavenging() { return dataManager.get(SCAVENGING); }

    public void setScavenging(boolean b) { dataManager.set(SCAVENGING, b); }
    
    @Override
    public void livingTick()
    {
        super.livingTick();

        sleepTimer.add(isSleeping()? 0.08f : -0.15f);

        if (!world.isRemote)
        {
            ItemStack item = getStackInSlot(ITEM_SLOT);
            if (isFoodItem(item) && getHealth() < getMaxHealth() && getRNG().nextDouble() <= 0.0075)
                eat(item);

            if (isBreedingItem(item) && canBreed() && getGrowingAge() == 0 && getRNG().nextDouble() < 0.01)
                setInLove(this);
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        final ActionResultType COMMON_SUCCESS = ActionResultType.func_233537_a_(world.isRemote);

        ItemStack heldItem = getItem();
        Item item = stack.getItem();

        if (!isTamed() && net.minecraftforge.common.Tags.Items.EGGS.contains(item))
        {
            eat(stack);
            if (tame(getRNG().nextDouble() < 0.25, player)) getAttribute(MAX_HEALTH).setBaseValue(20d);

            return COMMON_SUCCESS;
        }

        if (isOwner(player))
        {
            if (player.isSneaking())
            {
                setSit(!func_233684_eK_());
                return COMMON_SUCCESS;
            }

            if (stack.isEmpty() && heldItem.isEmpty() && player.getPassengers().size() < 3)
            {
                setSit(false);
                startRiding(player, true);

                return COMMON_SUCCESS;
            }

            setStackInSlot(ITEM_SLOT, stack);
            player.setHeldItem(hand, heldItem);

            return COMMON_SUCCESS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public void doSpecialEffects()
    {
        if (getVariant() == -1 && ticksExisted % 25 == 0)
        {
            double x = getPosX() + (Mafs.nextDouble(getRNG()) * 0.7d);
            double y = getPosY() + (getRNG().nextDouble() * 0.5d);
            double z = getPosZ() + (Mafs.nextDouble(getRNG()) * 0.7d);
            world.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == ITEM_SLOT) setItem(stack);
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slot)
    {
        if (slot == EquipmentSlotType.MAINHAND) return getItem();
        return super.getItemStackFromSlot(slot);
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
    public int determineVariant() { return getRNG().nextDouble() < 0.005? -1 : 0; }

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

    @Override
    protected float getSoundVolume() { return 0.8f; }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().isFood() && stack.getItem().getFood().isMeat();
    }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 1); }

    private static void setInLove(RoostStalkerEntity entity)
    {
        ItemStack item = entity.getStackInSlot(ITEM_SLOT);

        item.shrink(1);
        entity.setInLove(600);
        entity.setStackInSlot(ITEM_SLOT, item.copy());
        entity.world.setEntityState(entity, (byte) 18);
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        Biome.Category category = event.getCategory();
        if (category == Biome.Category.PLAINS || category == Biome.Category.FOREST || category == Biome.Category.EXTREME_HILLS)
            event.getSpawns().func_242575_a(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ROOSTSTALKER.get(), 7, 2, 9));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 8)
                .createMutableAttribute(MOVEMENT_SPEED, 0.285)
                .createMutableAttribute(ATTACK_DAMAGE, 2);
    }

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
            boolean flag = !isTamed() && !hasItem() && super.shouldExecute();
            if (flag) return (chest = getInventoryAtPosition()) != null && !chest.isEmpty();
            else return false;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return !hasItem() && chest != null && super.shouldContinueExecuting();
        }

        @Override
        public void tick()
        {
            super.tick();

            if (getIsAboveDestination())
            {
                if (hasItem()) return;

                setScavenging(true);

                if (chest == null) return;
                if (chest instanceof ChestTileEntity && ((ChestTileEntity) chest).numPlayersUsing == 0)
                    interactChest(chest, true);
                if (!chest.isEmpty() && --searchDelay <= 0)
                {
                    int index = getRNG().nextInt(chest.getSizeInventory());
                    ItemStack stack = chest.getStackInSlot(index);

                    if (!stack.isEmpty())
                    {
                        stack = chest.removeStackFromSlot(index);
                        getInvHandler().insertItem(ITEM_SLOT, stack, false);
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
            setScavenging(false);
        }

        /**
         * Returns the IInventory (if applicable) of the TileEntity at the specified position
         */
        @Nullable
        public IInventory getInventoryAtPosition()
        {
            IInventory inv = null;
            BlockState blockstate = world.getBlockState(destinationBlock);
            Block block = blockstate.getBlock();
            if (blockstate.hasTileEntity())
            {
                TileEntity tileentity = world.getTileEntity(destinationBlock);
                if (tileentity instanceof IInventory)
                {
                    inv = (IInventory) tileentity;
                    if (inv instanceof ChestTileEntity && block instanceof ChestBlock)
                        inv = ChestBlock.getChestInventory((ChestBlock) block, blockstate, world, destinationBlock, true);
                }
            }

            return inv;
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
    }

    class BreedGoal extends DragonBreedGoal
    {
        public BreedGoal()
        {
            super(RoostStalkerEntity.this); // UNLIMITED OFFSPRING, ARMIES ARISE
        }

        @Override
        public void startExecuting() { setInLove((RoostStalkerEntity) targetMate); }
    }
}
