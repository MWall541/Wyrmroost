package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.containers.util.SlotBuilder;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializer;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffActions;
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

public class RoostStalkerEntity extends TameableDragonEntity
{
    public static final EntitySerializer<RoostStalkerEntity> SERIALIZER = TameableDragonEntity.SERIALIZER.concat(b -> b
            .track(EntitySerializer.BOOL, "Sleeping", TameableDragonEntity::isSleeping, TameableDragonEntity::setSleeping)
            .track(EntitySerializer.INT, "Variant", TameableDragonEntity::getVariant, TameableDragonEntity::setVariant));

    public static final int ITEM_SLOT = 0;
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.defineId(RoostStalkerEntity.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Boolean> SCAVENGING = EntityDataManager.defineId(RoostStalkerEntity.class, DataSerializers.BOOLEAN);

    public RoostStalkerEntity(EntityType<? extends RoostStalkerEntity> stalker, World level)
    {
        super(stalker, level);
        maxUpStep = 0;
    }

    @Override
    public EntitySerializer<RoostStalkerEntity> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(SLEEPING, false);
        entityData.define(VARIANT, 0);
        entityData.define(ITEM, ItemStack.EMPTY);
        entityData.define(SCAVENGING, false);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1d, true));
        goalSelector.addGoal(5, new MoveToHomeGoal(this));
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this));
        goalSelector.addGoal(9, new ScavengeGoal(1.1d));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 5f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
        goalSelector.addGoal(8, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 7f, 1.15f, 1f)
        {
            @Override
            public boolean canUse()
            {
                return !isTame() && !getItem().isEmpty() && super.canUse();
            }
        });

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(4, new HurtByTargetGoal(this).setAlertOthers());
        targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, LivingEntity.class, true, target -> target instanceof ChickenEntity || target instanceof RabbitEntity || target instanceof TurtleEntity));
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        sleepTimer.add(isSleeping()? 0.08f : -0.15f);

        if (!level.isClientSide)
        {
            ItemStack item = getStackInSlot(ITEM_SLOT);
            if (isFoodItem(item) && getHealth() < getMaxHealth() && getRandom().nextDouble() <= 0.0075)
                eat(item);
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        final ActionResultType COMMON_SUCCESS = ActionResultType.sidedSuccess(level.isClientSide);

        ItemStack heldItem = getItem();
        Item item = stack.getItem();

        if (!isTame() && Tags.Items.EGGS.contains(item))
        {
            eat(stack);
            if (tame(getRandom().nextDouble() < 0.25, player)) getAttribute(MAX_HEALTH).setBaseValue(20d);

            return COMMON_SUCCESS;
        }

        if (isTame() && isFood(stack))
        {
            if (!level.isClientSide && canFallInLove() && getAge() == 0)
            {
                setInLove(player);
                stack.shrink(1);
                return ActionResultType.SUCCESS;
            }

            return ActionResultType.CONSUME;
        }

        if (isOwnedBy(player))
        {
            if (player.isShiftKeyDown())
            {
                setOrderedToSit(!isInSittingPose());
                return COMMON_SUCCESS;
            }

            if (stack.isEmpty() && heldItem.isEmpty() && !isLeashed() && player.getPassengers().size() < 3)
            {
                if (!level.isClientSide && startRiding(player, true))
                {
                    setOrderedToSit(false);
                    AddPassengerPacket.send(this, player);
                }

                return COMMON_SUCCESS;
            }

            if ((!stack.isEmpty() && !isFood(stack)) || !heldItem.isEmpty())
            {
                setStackInSlot(ITEM_SLOT, stack);
                player.setItemInHand(hand, heldItem);

                return COMMON_SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public void doSpecialEffects()
    {
        if (getVariant() == -1 && tickCount % 25 == 0)
        {
            double x = getX() + (Mafs.nextDouble(getRandom()) * 0.7d);
            double y = getY() + (getRandom().nextDouble() * 0.5d);
            double z = getZ() + (Mafs.nextDouble(getRandom()) * 0.7d);
            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == ITEM_SLOT) setItem(stack);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slot)
    {
        if (slot == EquipmentSlotType.MAINHAND) return getItem();
        return super.getItemBySlot(slot);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffActions.TARGET);
        super.addScreenInfo(screen);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);
        container.addSlot(new SlotBuilder(getInventory(), ITEM_SLOT));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source == DamageSource.DROWN || super.isInvulnerableTo(source);
    }

    @Override
    public boolean isFood(ItemStack stack)
    {
        return stack.getItem() == Items.GOLD_NUGGET;
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        return getType().getDimensions().scale(getScale());
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

    public ItemStack getItem()
    {
        return entityData.get(ITEM);
    }

    private boolean hasItem()
    {
        return getItem() != ItemStack.EMPTY;
    }

    public void setItem(ItemStack item)
    {
        entityData.set(ITEM, item);
        if (!item.isEmpty()) playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 0.5f, 1);
    }

    public boolean isScavenging()
    {
        return entityData.get(SCAVENGING);
    }

    public void setScavenging(boolean b)
    {
        entityData.set(SCAVENGING, b);
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
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties().isMeat();
    }

    @Override
    public DragonInventory createInv()
    {
        return new DragonInventory(this, 1);
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        Biome.Category category = event.getCategory();
        if (category == Biome.Category.PLAINS || category == Biome.Category.FOREST || category == Biome.Category.EXTREME_HILLS)
            event.getSpawns().addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ROOSTSTALKER.get(), 7, 2, 9));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 8)
                .add(MOVEMENT_SPEED, 0.285)
                .add(ATTACK_DAMAGE, 2);
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
        public boolean canUse()
        {
            boolean flag = !isTame() && !hasItem() && super.canUse();
            if (flag) return (chest = getInventoryAtPosition()) != null && !chest.isEmpty();
            else return false;
        }

        @Override
        public boolean canContinueToUse()
        {
            return !hasItem() && chest != null && super.canContinueToUse();
        }

        @Override
        public void tick()
        {
            super.tick();

            if (isReachedTarget())
            {
                if (hasItem()) return;

                setScavenging(true);

                if (chest == null) return;
                if (chest instanceof ChestTileEntity && ((ChestTileEntity) chest).openCount == 0)
                    interactChest(chest, true);
                if (!chest.isEmpty() && --searchDelay <= 0)
                {
                    int index = getRandom().nextInt(chest.getContainerSize());
                    ItemStack stack = chest.getItem(index);

                    if (!stack.isEmpty())
                    {
                        stack = chest.removeItemNoUpdate(index);
                        getInventory().insertItem(ITEM_SLOT, stack, false);
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
            BlockState blockstate = level.getBlockState(blockPos);
            Block block = blockstate.getBlock();
            if (blockstate.hasTileEntity())
            {
                TileEntity tileentity = level.getBlockEntity(blockPos);
                if (tileentity instanceof IInventory)
                {
                    inv = (IInventory) tileentity;
                    if (inv instanceof ChestTileEntity && block instanceof ChestBlock)
                        inv = ChestBlock.getContainer((ChestBlock) block, blockstate, level, blockPos, true);
                }
            }

            return inv;
        }

        /**
         * Return true to set given position as destination
         */
        @Override
        protected boolean isValidTarget(IWorldReader world, BlockPos pos)
        {
            return level.getBlockEntity(pos) instanceof IInventory;
        }

        /**
         * Used to handle the chest opening animation when being used by the scavenger
         */
        private void interactChest(IInventory intentory, boolean open)
        {
            if (!(intentory instanceof ChestTileEntity)) return; // not a chest, ignore it
            ChestTileEntity chest = (ChestTileEntity) intentory;

            chest.openCount = open? 1 : 0;
            chest.getLevel().blockEvent(chest.getBlockPos(), chest.getBlockState().getBlock(), 1, chest.openCount);
        }
    }
}
