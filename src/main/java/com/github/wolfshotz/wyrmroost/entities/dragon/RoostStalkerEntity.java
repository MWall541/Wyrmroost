package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.containers.util.SlotBuilder;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInvHandler;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.network.packets.AddPassengerPacket;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class RoostStalkerEntity extends AbstractDragonEntity
{
    public static final int ITEM_SLOT = 0;
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.registerData(RoostStalkerEntity.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Boolean> SCAVENGING = EntityDataManager.registerData(RoostStalkerEntity.class, DataSerializers.BOOLEAN);

    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World world)
    {
        super(stalker, world);

        stepHeight = 0;

        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void initGoals()
    {
        super.initGoals();

        goalSelector.add(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.add(4, new MeleeAttackGoal(this, 1.1d, true));
        goalSelector.add(5, new MoveToHomeGoal(this));
        goalSelector.add(6, new WRFollowOwnerGoal(this));
        goalSelector.add(7, new DragonBreedGoal(this));
        goalSelector.add(9, new ScavengeGoal(1.1d));
        goalSelector.add(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.add(11, new LookAtGoal(this, LivingEntity.class, 5f));
        goalSelector.add(12, new LookRandomlyGoal(this));
        goalSelector.add(8, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 7f, 1.15f, 1f)
        {
            @Override
            public boolean canStart()
            {
                return !isTamed() && !getItem().isEmpty() && super.canStart();
            }
        });

        targetSelector.add(1, new OwnerHurtByTargetGoal(this));
        targetSelector.add(2, new OwnerHurtTargetGoal(this));
        targetSelector.add(3, new DefendHomeGoal(this));
        targetSelector.add(4, new HurtByTargetGoal(this).setGroupRevenge());
        targetSelector.add(5, new NonTamedTargetGoal<>(this, LivingEntity.class, true, target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity));
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        dataTracker.startTracking(ITEM, ItemStack.EMPTY);
        dataTracker.startTracking(SCAVENGING, false);
    }

    public ItemStack getItem()
    {
        return dataTracker.get(ITEM);
    }

    private boolean hasItem()
    {
        return getItem() != ItemStack.EMPTY;
    }

    public void setItem(ItemStack item)
    {
        dataTracker.set(ITEM, item);
        if (!item.isEmpty()) playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.5f, 1);
    }

    public boolean isScavenging()
    {
        return dataTracker.get(SCAVENGING);
    }

    public void setScavenging(boolean b)
    {
        dataTracker.set(SCAVENGING, b);
    }

    @Override
    public void tickMovement()
    {
        super.tickMovement();

        sleepTimer.add(isSleeping()? 0.08f : -0.15f);

        if (!world.isClientSide)
        {
            ItemStack item = getStackInSlot(ITEM_SLOT);
            if (isFoodItem(item) && getHealth() < getMaxHealth() && getRandom().nextDouble() <= 0.0075)
                eat(item);
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        final ActionResultType COMMON_SUCCESS = ActionResultType.success(world.isClientSide);

        ItemStack heldItem = getItem();
        Item item = stack.getItem();

        if (!isTamed() && Tags.Items.EGGS.contains(item))
        {
            eat(stack);
            if (tame(getRandom().nextDouble() < 0.25, player)) getAttributeInstance(GENERIC_MAX_HEALTH).setBaseValue(20d);

            return COMMON_SUCCESS;
        }

        if (isTamed() && isBreedingItem(stack))
        {
            if (!world.isClientSide && canEat() && getBreedingAge() == 0)
            {
                lovePlayer(player);
                stack.decrement(1);
                return ActionResultType.SUCCESS;
            }

            return ActionResultType.CONSUME;
        }

        if (isOwner(player))
        {
            if (player.isSneaking())
            {
                setSitting(!isInSittingPose());
                return COMMON_SUCCESS;
            }

            if (stack.isEmpty() && heldItem.isEmpty() && !isLeashed() && player.getPassengerList().size() < 3)
            {
                if (!world.isClientSide && startRiding(player, true))
                {
                    setSitting(false);
                    AddPassengerPacket.send(this, player);
                }

                return COMMON_SUCCESS;
            }

            if ((!stack.isEmpty() && !isBreedingItem(stack)) || !heldItem.isEmpty())
            {
                setStackInSlot(ITEM_SLOT, stack);
                player.setStackInHand(hand, heldItem);

                return COMMON_SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void doSpecialEffects()
    {
        if (getVariant() == -1 && age % 25 == 0)
        {
            double x = getX() + (Mafs.nextDouble(getRandom()) * 0.7d);
            double y = getY() + (getRandom().nextDouble() * 0.5d);
            double z = getZ() + (Mafs.nextDouble(getRandom()) * 0.7d);
            world.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == ITEM_SLOT) setItem(stack);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlotType slot)
    {
        if (slot == EquipmentSlotType.MAINHAND) return getItem();
        return super.getEquippedStack(slot);
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
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source == DamageSource.DROWN || super.isInvulnerableTo(source);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.GOLD_NUGGET;
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        return getType().getDimensions().scaled(getScaleFactor());
    }

    @Override
    public int determineVariant()
    {
        return getRandom().nextDouble() < 0.005? -1 : 0;
    }

    @Override
    // Override normal dragon body controller to allow rotations while sitting: its small enough for it, why not. :P
    protected BodyController createBodyControl()
    {
        return new BodyController(this);
    }

    @Override
    public boolean canFly()
    {
        return false;
    }

    @Override
    public boolean defendsHome()
    {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_STALKER_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_STALKER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_STALKER_DEATH.get();
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.8f;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().isFood() && stack.getItem().getFoodComponent().isMeat();
    }

    @Override
    public DragonInvHandler createInv()
    {
        return new DragonInvHandler(this, 1);
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        Biome.Category category = event.getCategory();
        if (category == Biome.Category.PLAINS || category == Biome.Category.FOREST || category == Biome.Category.EXTREME_HILLS)
            event.getSpawns().spawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ROOSTSTALKER.get(), 7, 2, 9));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(GENERIC_MAX_HEALTH, 8)
                .add(GENERIC_MOVEMENT_SPEED, 0.285)
                .add(GENERIC_ATTACK_DAMAGE, 2);
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
        public boolean canStart()
        {
            boolean flag = !isTamed() && !hasItem() && super.canStart();
            if (flag) return (chest = getInventoryAtPosition()) != null && !chest.isEmpty();
            else return false;
        }

        @Override
        public boolean shouldContinue()
        {
            return !hasItem() && chest != null && super.shouldContinue();
        }

        @Override
        public void tick()
        {
            super.tick();

            if (hasReached())
            {
                if (hasItem()) return;

                setScavenging(true);

                if (chest == null) return;
                if (chest instanceof ChestTileEntity && ((ChestTileEntity) chest).viewerCount == 0)
                    interactChest(chest, true);
                if (!chest.isEmpty() && --searchDelay <= 0)
                {
                    int index = getRandom().nextInt(chest.size());
                    ItemStack stack = chest.getStack(index);

                    if (!stack.isEmpty())
                    {
                        stack = chest.removeStack(index);
                        getInvHandler().insertItem(ITEM_SLOT, stack, false);
                    }
                }
            }
        }

        @Override
        public void stop()
        {
            super.stop();
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
            BlockState blockstate = world.getBlockState(targetPos);
            Block block = blockstate.getBlock();
            if (blockstate.hasTileEntity())
            {
                TileEntity tileentity = world.getBlockEntity(targetPos);
                if (tileentity instanceof IInventory)
                {
                    inv = (IInventory) tileentity;
                    if (inv instanceof ChestTileEntity && block instanceof ChestBlock)
                        inv = ChestBlock.getInventory((ChestBlock) block, blockstate, world, targetPos, true);
                }
            }

            return inv;
        }

        /**
         * Return true to set given position as destination
         */
        @Override
        protected boolean isTargetPos(IWorldReader world, BlockPos pos)
        {
            return world.getBlockEntity(pos) instanceof IInventory;
        }

        /**
         * Used to handle the chest opening animation when being used by the scavenger
         */
        private void interactChest(IInventory intentory, boolean open)
        {
            if (!(intentory instanceof ChestTileEntity)) return; // not a chest, ignore it
            ChestTileEntity chest = (ChestTileEntity) intentory;

            chest.viewerCount = open? 1 : 0;
            chest.getWorld().addSyncedBlockEvent(chest.getPos(), chest.getCachedState().getBlock(), 1, chest.viewerCount);
        }
    }
}
