package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.containers.util.SlotBuilder;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInvHandler;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.*;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.items.DragonArmorItem;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

import static net.minecraft.entity.ai.attributes.Attributes.*;

/**
 * Created by com.github.WolfShotz 7/10/19 - 22:18
 */
public class OWDrakeEntity extends AbstractDragonEntity
{
    // inventory slot constants
    public static final int SADDLE_SLOT = 0;
    public static final int ARMOR_SLOT = 1;
    public static final int CHEST_SLOT = 2;

    // Dragon Entity Data
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.registerData(OWDrakeEntity.class, DataSerializers.BOOLEAN);

    // Dragon Entity Animations
    public static final Animation GRAZE_ANIMATION = new Animation(35);
    public static final Animation HORN_ATTACK_ANIMATION = new Animation(15);
    public static final Animation ROAR_ANIMATION = new Animation(86);

    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public LivingEntity thrownPassenger;

    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world)
    {
        super(drake, world);

        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, true);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void initGoals()
    {
        super.initGoals();

        goalSelector.add(4, new MoveToHomeGoal(this));
        goalSelector.add(5, new ControlledAttackGoal(this, 1.425, true, () -> AnimationPacket.send(this, HORN_ATTACK_ANIMATION)));
        goalSelector.add(6, new WRFollowOwnerGoal(this));
        goalSelector.add(7, new DragonBreedGoal(this));
        goalSelector.add(8, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.add(9, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.add(10, new LookRandomlyGoal(this));

        targetSelector.add(1, new OwnerHurtByTargetGoal(this));
        targetSelector.add(2, new OwnerHurtTargetGoal(this));
        targetSelector.add(3, new DefendHomeGoal(this));
        targetSelector.add(4, new HurtByTargetGoal(this));
        targetSelector.add(5, new NonTamedTargetGoal<>(this, PlayerEntity.class, true, EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL::test));
    }

    // ================================
    //           Entity Data
    // ================================

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        dataTracker.startTracking(SADDLED, false);
        dataTracker.startTracking(ARMOR, ItemStack.EMPTY);
    }

    public boolean hasChest()
    {
        return getStackInSlot(CHEST_SLOT) != ItemStack.EMPTY;
    }

    public boolean isSaddled()
    {
        return dataTracker.get(SADDLED);
    }

    @Override
    public int determineVariant()
    {
        if (getRandom().nextDouble() < 0.008) return -1;
        if (world.getBiome(getBlockPos()).getCategory() == Biome.Category.SAVANNA) return 1;
        return 0;
    }

    @Override
    public DragonInvHandler createInv()
    {
        return new DragonInvHandler(this, 24);
    }

    // ================================

    @Override
    public void tickMovement()
    {
        super.tickMovement();

        sitTimer.add((isInSittingPose() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.04f : -0.06f);

        if (thrownPassenger != null)
        {
            thrownPassenger.setVelocity(Mafs.nextDouble(getRandom()), 0.1 + getRandom().nextDouble(), Mafs.nextDouble(getRandom()));
            ((ServerChunkProvider) world.getChunkManager()).sendToNearbyPlayers(thrownPassenger, new SEntityVelocityPacket(thrownPassenger)); // notify client
            thrownPassenger = null;
        }

        if (!world.isClient && getTarget() == null && !isInSittingPose() && !isSleeping() && world.getBlockState(getBlockPos().down()).getBlock() == Blocks.GRASS_BLOCK && getRandom().nextDouble() < (isBaby() || getHealth() < getMaxHealth()? 0.005 : 0.001))
            AnimationPacket.send(this, GRAZE_ANIMATION);

        Animation animation = getAnimation();
        int tick = getAnimationTick();
        LivingEntity target = getTarget();

        if (animation == ROAR_ANIMATION)
        {
            if (tick == 0) playSound(WRSounds.ENTITY_OWDRAKE_ROAR.get(), 3f, 1f, true);
            else if (tick == 15)
            {
                for (LivingEntity entity : getEntitiesNearby(15, e -> !isTeammate(e))) // Dont get too close now ;)
                {
                    entity.addStatusEffect(new EffectInstance(Effects.SLOWNESS, 200));
                    if (squaredDistanceTo(entity) <= 10)
                    {
                        double angle = Mafs.getAngle(getX(), getZ(), entity.getX(), entity.getZ()) * Math.PI / 180;
                        entity.addVelocity(1.2 * -Math.cos(angle), 0.4d, 1.2 * -Math.sin(angle));
                    }
                }
            }
        }

        if (animation == HORN_ATTACK_ANIMATION)
        {
            if (tick == 8)
            {
                if (target != null) yaw = bodyYaw = (float) Mafs.getAngle(this, target) + 90f;
                playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1, 0.5f, true);
                AxisAlignedBB box = getOffsetBox(getWidth()).expand(-0.075);
                attackInBox(box);
                for (BlockPos pos : ModUtils.iterateThrough(box))
                {
                    if (world.getBlockState(pos).isIn(BlockTags.LEAVES))
                        world.breakBlock(pos, false, this);
                }
            }
        }

        if (!world.isClient && animation == GRAZE_ANIMATION && tick == 13)
        {
            BlockPos pos = new BlockPos(Mafs.getYawVec(bodyYaw, 0, getWidth() / 2 + 1).add(getPos()));
            if (world.getBlockState(pos).isOf(Blocks.GRASS) && WRConfig.canGrief(world))
            {
                world.breakBlock(pos, false);
                onEatingGrass();
            }
            else if (world.getBlockState(pos = pos.down()).getBlock() == Blocks.GRASS_BLOCK)
            {
                world.syncWorldEvent(2001, pos, Block.getRawIdFromState(Blocks.GRASS_BLOCK.getDefaultState()));
                world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
                onEatingGrass();
            }
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SADDLE && !isSaddled() && !isBaby())
        {
            if (!world.isClient)
            {
                getInvHandler().insertItem(SADDLE_SLOT, stack.copy(), false);
                stack.decrement(1);
            }
            return ActionResultType.success(world.isClient);
        }

        if (!isTamed() && isBaby() && isFoodItem(stack))
        {
            tame(getRandom().nextInt(10) == 0, player);
            stack.decrement(1);
            return ActionResultType.success(world.isClient);
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void updatePassengerPosition(Entity entity)
    {
        super.updatePassengerPosition(entity);

        if (entity instanceof LivingEntity)
        {
            LivingEntity passenger = ((LivingEntity) entity);
            if (isTamed()) setSprinting(passenger.isSprinting());
            else if (!world.isClient && passenger instanceof PlayerEntity)
            {
                double rng = getRandom().nextDouble();

                if (rng < 0.01) tame(true, (PlayerEntity) passenger);
                else if (rng <= 0.1)
                {
                    setTarget(passenger);
                    ridingCooldown = 60;
                    removeAllPassengers();
                    thrownPassenger = passenger; // needs to be queued for next tick otherwise some voodoo shit breaks the throwing off logic >.>
                }
            }
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == SADDLE_SLOT)
        {
            dataTracker.set(SADDLED, !stack.isEmpty());
            if (!stack.isEmpty() && !onLoad) playSound(SoundEvents.ENTITY_HORSE_SADDLE, 1, 1);
        }

        if (slot == ARMOR_SLOT) setArmor(stack);
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY1 && pressed && noActiveAnimation())
        {
            if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) setAnimation(ROAR_ANIMATION);
            else setAnimation(HORN_ATTACK_ANIMATION);
        }
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scaled(getScaleFactor());
        if (isInSittingPose() || isSleeping()) size = size.scaled(1, 0.75f);
        return size;
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

        DragonInvHandler inv = container.inventory;

        container.addSlot(new SlotBuilder(inv, SADDLE_SLOT, 17, 45).only(Items.SADDLE));
        container.addSlot(new SlotBuilder(inv, ARMOR_SLOT, 17, 63).only(DragonArmorItem.class));
        container.addSlot(new SlotBuilder(inv, CHEST_SLOT, 17, 81).only(ChestBlock.class).limit(1).canTake(p -> inv.isEmptyAfter(CHEST_SLOT)));
        container.makeSlots(3, 51, 45, 7, 3, (i, x, z) -> new SlotBuilder(inv, i, x, z).condition(this::hasChest));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target)
    {
        LivingEntity prev = getTarget();

        super.setTarget(target);

        boolean flag = getTarget() != null;
        setSprinting(flag);

        if (flag && prev != target && target.getType() == EntityType.PLAYER && !isTamed() && noActiveAnimation())
            AnimationPacket.send(OWDrakeEntity.this, OWDrakeEntity.ROAR_ANIMATION);
    }

    @Override
    public boolean isImmobile()
    {
        return getAnimation() == ROAR_ANIMATION || super.isImmobile();
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().moveBy(-0.5d, 0.75d, 0);
        else event.getInfo().moveBy(-3, 0.3, 0);
    }

    @Override
    public void onEatingGrass()
    {
        if (isBaby()) growUp(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }

    @Override
    protected boolean canStartRiding(Entity entity)
    {
        return isSaddled() && !isBaby() && (isOwner((LivingEntity) entity) || (!isTamed() && ridingCooldown <= 0));
    }

    @Override
    public float getTravelSpeed()
    {
        float speed = (float) getAttributeValue(GENERIC_MOVEMENT_SPEED);
        if (canBeControlledByRider()) speed += 0.45f;
        return speed;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1f);
        super.playStepSound(pos, blockIn);
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
        return WRSounds.ENTITY_OWDRAKE_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_OWDRAKE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_OWDRAKE_DEATH.get();
    }

    @Override
    public boolean canFly()
    {
        return false;
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().isIn(Tags.Items.CROPS_WHEAT);
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, GRAZE_ANIMATION, HORN_ATTACK_ANIMATION, ROAR_ANIMATION};
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        Biome.Category category = event.getCategory();
        if (category == Biome.Category.SAVANNA || category == Biome.Category.PLAINS)
            event.getSpawns().spawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.OVERWORLD_DRAKE.get(), 8, 1, 3));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(GENERIC_MAX_HEALTH, 70)
                .add(GENERIC_MOVEMENT_SPEED, 0.2125)
                .add(GENERIC_KNOCKBACK_RESISTANCE, 0.75)
                .add(GENERIC_FOLLOW_RANGE, 20)
                .add(GENERIC_ATTACK_KNOCKBACK, 2.85)
                .add(GENERIC_ATTACK_DAMAGE, 8);
    }
}
