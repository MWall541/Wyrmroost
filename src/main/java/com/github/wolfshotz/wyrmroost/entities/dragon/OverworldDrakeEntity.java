package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.screen.DragonStaffScreen;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.CollapsibleWidget;
import com.github.wolfshotz.wyrmroost.containers.DragonStaffContainer;
import com.github.wolfshotz.wyrmroost.containers.util.DynamicSlot;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInventory;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.*;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializer;
import com.github.wolfshotz.wyrmroost.items.DragonArmorItem;
import com.github.wolfshotz.wyrmroost.items.staff.action.StaffActions;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
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
public class OverworldDrakeEntity extends TameableDragonEntity
{
    private static final EntitySerializer<OverworldDrakeEntity> SERIALIZER = TameableDragonEntity.SERIALIZER.concat(b -> b
            .track(EntitySerializer.BOOL, "Gender", TameableDragonEntity::isMale, TameableDragonEntity::setGender)
            .track(EntitySerializer.INT, "Variant", TameableDragonEntity::getVariant, TameableDragonEntity::setVariant)
            .track(EntitySerializer.BOOL, "Sleeping", TameableDragonEntity::isSleeping, TameableDragonEntity::setSleeping));

    // inventory slot constants
    public static final int SADDLE_SLOT = 0;
    public static final int ARMOR_SLOT = 1;
    public static final int CHEST_SLOT = 2;

    // Dragon Entity Data
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.defineId(OverworldDrakeEntity.class, DataSerializers.BOOLEAN);

    // Dragon Entity Animations
    public static final Animation GRAZE_ANIMATION = new Animation(35);
    public static final Animation HORN_ATTACK_ANIMATION = new Animation(15);
    public static final Animation ROAR_ANIMATION = new Animation(86);

    public final LerpedFloat sitTimer = LerpedFloat.unit();
    public LivingEntity thrownPassenger;

    public OverworldDrakeEntity(EntityType<? extends OverworldDrakeEntity> drake, World level)
    {
        super(drake, level);
    }

    @Override
    public EntitySerializer<OverworldDrakeEntity> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(GENDER, false);
        entityData.define(SLEEPING, false);
        entityData.define(VARIANT, 0);
        entityData.define(SADDLED, false);
        entityData.define(ARMOR, ItemStack.EMPTY);
    }

    @Override
    public DragonInventory createInv()
    {
        return new DragonInventory(this, 18);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new ControlledAttackGoal(this, 1.425, true, () -> AnimationPacket.send(this, HORN_ATTACK_ANIMATION)));
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
        targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, PlayerEntity.class, true, EntityPredicates.ATTACK_ALLOWED::test));
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        sitTimer.add((isInSittingPose() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.04f : -0.06f);

        if (thrownPassenger != null)
        {
            thrownPassenger.setDeltaMovement(Mafs.nextDouble(getRandom()), 0.1 + getRandom().nextDouble(), Mafs.nextDouble(getRandom()));
            ((ServerChunkProvider) level.getChunkSource()).broadcastAndSend(thrownPassenger, new SEntityVelocityPacket(thrownPassenger)); // notify client
            thrownPassenger = null;
        }

        if (!level.isClientSide && getTarget() == null && !isInSittingPose() && !isSleeping() && level.getBlockState(blockPosition().below()).getBlock() == Blocks.GRASS_BLOCK && getRandom().nextDouble() < (isBaby() || getHealth() < getMaxHealth()? 0.005 : 0.001))
            AnimationPacket.send(this, GRAZE_ANIMATION);

        Animation animation = getAnimation();
        int tick = getAnimationTick();
        LivingEntity target = getTarget();

        if (animation == ROAR_ANIMATION)
        {
            if (tick == 0) playSound(WRSounds.ENTITY_OWDRAKE_ROAR.get(), 3f, 1f, true);
            else if (tick == 15)
            {
                for (LivingEntity entity : getEntitiesNearby(15, e -> !isAlliedTo(e))) // Dont get too close now ;)
                {
                    entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
                    if (distanceToSqr(entity) <= 10)
                    {
                        double angle = Mafs.getAngle(getX(), getZ(), entity.getX(), entity.getZ()) * Math.PI / 180;
                        entity.push(1.2 * -Math.cos(angle), 0.4d, 1.2 * -Math.sin(angle));
                    }
                }
            }
        }

        if (animation == HORN_ATTACK_ANIMATION)
        {
            if (tick == 8)
            {
                if (target != null) yRot = yBodyRot = (float) Mafs.getAngle(this, target) + 90f;
                playSound(SoundEvents.IRON_GOLEM_ATTACK, 1, 0.5f, true);
                AxisAlignedBB box = getOffsetBox(getBbWidth()).inflate(-0.075);
                attackInBox(box);
                for (BlockPos pos : ModUtils.eachPositionIn(box))
                {
                    if (level.getBlockState(pos).is(BlockTags.LEAVES))
                        level.destroyBlock(pos, false, this);
                }
            }
        }

        if (!level.isClientSide && animation == GRAZE_ANIMATION && tick == 13)
        {
            BlockPos pos = new BlockPos(Mafs.getYawVec(yBodyRot, 0, getBbWidth() / 2 + 1).add(position()));
            if (level.getBlockState(pos).is(Blocks.GRASS) && WRConfig.canGrief(level))
            {
                level.destroyBlock(pos, false);
                ate();
            }
            else if (level.getBlockState(pos = pos.below()).getBlock() == Blocks.GRASS_BLOCK)
            {
                level.levelEvent(2001, pos, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
                ate();
            }
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SADDLE && !isSaddled() && !isBaby())
        {
            if (!level.isClientSide)
            {
                getInventory().insertItem(SADDLE_SLOT, stack.copy(), false);
                stack.shrink(1);
            }
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        if (!isTame() && isBaby() && isFoodItem(stack))
        {
            tame(getRandom().nextInt(10) == 0, player);
            stack.shrink(1);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void positionRider(Entity entity)
    {
        super.positionRider(entity);

        if (entity instanceof LivingEntity)
        {
            LivingEntity passenger = ((LivingEntity) entity);
            if (isTame()) setSprinting(passenger.isSprinting());
            else if (!level.isClientSide && passenger instanceof PlayerEntity)
            {
                double rng = getRandom().nextDouble();

                if (rng < 0.01) tame(true, (PlayerEntity) passenger);
                else if (rng <= 0.1)
                {
                    setTarget(passenger);
                    boardingCooldown = 60;
                    ejectPassengers();
                    thrownPassenger = passenger; // needs to be queued for next tick otherwise some voodoo shit breaks the throwing off logic >.>
                }
            }
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        boolean playSound = !stack.isEmpty() && !onLoad;
        switch (slot)
        {
            case SADDLE_SLOT:
                entityData.set(SADDLED, !stack.isEmpty());
                if (playSound) playSound(SoundEvents.HORSE_SADDLE, 1f, 1f);
                break;
            case ARMOR_SLOT:
                setArmor(stack);
                if (playSound) playSound(SoundEvents.ARMOR_EQUIP_DIAMOND, 1f, 1f);
                break;
            case CHEST_SLOT:
                if (playSound) playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1f, 1f);
                break;
        }
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
        EntitySize size = getType().getDimensions().scale(getScale());
        if (isInSittingPose() || isSleeping()) size = size.scale(1, 0.75f);
        return size;
    }

    @Override
    public void applyStaffInfo(DragonStaffContainer container)
    {
        super.applyStaffInfo(container);

        DragonInventory i = getInventory();
        CollapsibleWidget chestWidget = DragonStaffContainer.collapsibleWidget( 0, 174, 121, 75, CollapsibleWidget.TOP)
                .condition(this::hasChest);
        ModUtils.createContainerSlots(i, 3, 17, 12, 5, 3, DynamicSlot::new, chestWidget::addSlot);

        container.slot(DragonStaffContainer.accessorySlot(i, ARMOR_SLOT, 15, -11, 22, DragonStaffScreen.ARMOR_UV).only(DragonArmorItem.class))
                .slot(DragonStaffContainer.accessorySlot(i, CHEST_SLOT, -15, -11, 22, DragonStaffScreen.CHEST_UV).only(ChestBlock.class).limit(1).canTake(p -> i.isEmptyAfter(CHEST_SLOT)))
                .slot(DragonStaffContainer.accessorySlot(i, SADDLE_SLOT, 0, -15, -7, DragonStaffScreen.SADDLE_UV).only(Items.SADDLE))
                .addStaffActions(StaffActions.TARGET)
                .addCollapsible(chestWidget);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target)
    {
        LivingEntity prev = getTarget();

        super.setTarget(target);

        boolean flag = getTarget() != null;
        setSprinting(flag);

        if (flag && prev != target && target.getType() == EntityType.PLAYER && !isTame() && noActiveAnimation())
            AnimationPacket.send(this, OverworldDrakeEntity.ROAR_ANIMATION);
    }

    @Override
    public boolean isImmobile()
    {
        return getAnimation() == ROAR_ANIMATION || super.isImmobile();
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().move(-0.5d, 0.75d, 0);
        else event.getInfo().move(-3, 0.3, 0);
    }

    @Override
    public void ate()
    {
        if (isBaby()) ageUp(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }

    @Override
    protected boolean canAddPassenger(Entity entity)
    {
        return isSaddled() && !isBaby() && (isOwnedBy((LivingEntity) entity) || (!isTame() && boardingCooldown <= 0));
    }

    @Override
    public float getTravelSpeed()
    {
        float speed = (float) getAttributeValue(MOVEMENT_SPEED);
        if (canBeControlledByRider()) speed += 0.45f;
        return speed;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        playSound(SoundEvents.COW_STEP, 0.3f, 1f);
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
        return stack.getItem().is(Tags.Items.CROPS_WHEAT);
    }

    public boolean hasChest()
    {
        return !getStackInSlot(CHEST_SLOT).isEmpty();
    }

    public boolean isSaddled()
    {
        return entityData.get(SADDLED);
    }

    @Override
    public void dropStorage()
    {
        DragonInventory inv = getInventory();
        for (int i = CHEST_SLOT + 1; i < inv.getSlots(); i++)
            spawnAtLocation(inv.getStackInSlot(i), getBbHeight() / 2f);
    }

    @Override
    public int determineVariant()
    {
        if (getRandom().nextDouble() < 0.008) return -1;
        if (level.getBiome(blockPosition()).getBiomeCategory() == Biome.Category.SAVANNA) return 1;
        return 0;
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
            event.getSpawns().addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.OVERWORLD_DRAKE.get(), 8, 1, 3));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 70)
                .add(MOVEMENT_SPEED, 0.2125)
                .add(KNOCKBACK_RESISTANCE, 0.75)
                .add(FOLLOW_RANGE, 20)
                .add(ATTACK_KNOCKBACK, 2.85)
                .add(ATTACK_DAMAGE, 8);
    }
}
