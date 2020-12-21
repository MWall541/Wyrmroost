package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.containers.util.SlotBuilder;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInvHandler;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.SleepController;
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
    // inventory slot const's
    public static final int SADDLE_SLOT = 0;
    public static final int ARMOR_SLOT = 1;
    public static final int CHEST_SLOT = 2;

    // Dragon Entity Data
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);

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
    protected SleepController createSleepController()
    {
        return new SleepController(this).setHomeDefender();
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new ControlledAttackGoal(this, 1.425, true, d -> AnimationPacket.send(d, HORN_ATTACK_ANIMATION)));
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, false, false, EntityPredicates.CAN_AI_TARGET::test));
    }

    // ================================
    //           Entity Data
    // ================================

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(SADDLED, false);
        dataManager.register(ARMOR, ItemStack.EMPTY);
    }

    public boolean hasChest()
    {
        return getStackInSlot(CHEST_SLOT) != ItemStack.EMPTY;
    }

    public boolean isSaddled()
    {
        return dataManager.get(SADDLED);
    }

    @Override
    public int determineVariant()
    {
        if (getRNG().nextDouble() < 0.008) return -1;
        if (world.getBiome(getPosition()).getCategory() == Biome.Category.SAVANNA) return 1;
        return 0;
    }

    @Override
    public DragonInvHandler createInv()
    {
        return new DragonInvHandler(this, 24);
    }

    // ================================

    @Override
    public void livingTick()
    {
        super.livingTick();

        sitTimer.add((func_233684_eK_() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.04f : -0.06f);

        if (thrownPassenger != null)
        {
            thrownPassenger.setMotion(Mafs.nextDouble(getRNG()), 0.1 + getRNG().nextDouble(), Mafs.nextDouble(getRNG()));
            ((ServerChunkProvider) world.getChunkProvider()).sendToTrackingAndSelf(thrownPassenger, new SEntityVelocityPacket(thrownPassenger)); // notify client
            thrownPassenger = null;
        }

        if (!world.isRemote && getAttackTarget() == null && !func_233684_eK_() && !isSleeping() && world.getBlockState(getPosition().down()).getBlock() == Blocks.GRASS_BLOCK && getRNG().nextDouble() < (isChild() || getHealth() < getMaxHealth()? 0.005 : 0.001))
            AnimationPacket.send(this, GRAZE_ANIMATION);

        Animation animation = getAnimation();
        int tick = getAnimationTick();
        LivingEntity target = getAttackTarget();

        if (animation == ROAR_ANIMATION)
        {
            if (tick == 0) playSound(WRSounds.ENTITY_OWDRAKE_ROAR.get(), 3f, 1f, true);
            else if (tick == 15)
            {
                for (LivingEntity e : getEntitiesNearby(15, e -> !isOnSameTeam(e))) // Dont get too close now ;)
                {
                    e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200));
                    if (getDistanceSq(e) <= 10)
                    {
                        double angle = Mafs.getAngle(getPosX(), getPosZ(), e.getPosX(), e.getPosZ()) * Math.PI / 180;
                        e.addVelocity(1.2 * -Math.cos(angle), 0.4d, 1.2 * -Math.sin(angle));
                    }
                }
            }
        }

        if (animation == HORN_ATTACK_ANIMATION)
        {
            if (tick == 8)
            {
                if (target != null) rotationYaw = renderYawOffset = (float) Mafs.getAngle(this, target) + 90f;
                playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1, 0.5f, true);
                AxisAlignedBB box = getOffsetBox(getWidth()).grow(-0.075);
                attackInBox(box);
                for (BlockPos pos : ModUtils.getBlockPosesInAABB(box))
                {
                    if (world.getBlockState(pos).isIn(BlockTags.LEAVES))
                        world.destroyBlock(pos, false, this);
                }
            }
        }

        if (!world.isRemote && animation == GRAZE_ANIMATION && tick == 13)
        {
            BlockPos pos = new BlockPos(Mafs.getYawVec(renderYawOffset, 0, getWidth() / 2 + 1).add(getPositionVec()));
            if (world.getBlockState(pos).isIn(Blocks.GRASS) && WRConfig.canGrief(world))
            {
                world.destroyBlock(pos, false);
                eatGrassBonus();
            }
            else if (world.getBlockState(pos = pos.down()).getBlock() == Blocks.GRASS_BLOCK)
            {
                world.playEvent(2001, pos, Block.getStateId(Blocks.GRASS_BLOCK.getDefaultState()));
                world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
                eatGrassBonus();
            }
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (stack.getItem() == Items.SADDLE && !isSaddled() && !isChild())
        {
            if (!world.isRemote)
            {
                getInvHandler().insertItem(SADDLE_SLOT, stack.copy(), false);
                consumeItemFromStack(player, stack);
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        }

        if (!isTamed() && isChild() && isFoodItem(stack))
        {
            tame(getRNG().nextInt(10) == 0, player);
            consumeItemFromStack(player, stack);
            return ActionResultType.func_233537_a_(world.isRemote);
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void updatePassenger(Entity entity)
    {
        super.updatePassenger(entity);

        if (entity instanceof LivingEntity)
        {
            LivingEntity passenger = ((LivingEntity) entity);
            if (isTamed()) setSprinting(passenger.isSprinting());
            else if (!world.isRemote && passenger instanceof PlayerEntity)
            {
                double rng = getRNG().nextDouble();

                if (rng < 0.01) tame(true, (PlayerEntity) passenger);
                else if (rng <= 0.1)
                {
                    setAttackTarget(passenger);
                    rideCooldown = 60;
                    removePassengers();
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
            dataManager.set(SADDLED, !stack.isEmpty());
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
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (func_233684_eK_() || isSleeping()) size = size.scale(1, 0.75f);
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
    public void setAttackTarget(@Nullable LivingEntity target)
    {
        LivingEntity prev = getAttackTarget();

        super.setAttackTarget(target);

        boolean flag = getAttackTarget() != null;
        setSprinting(flag);

        if (flag && prev != target && target.getType() == EntityType.PLAYER && !isTamed() && noActiveAnimation())
            AnimationPacket.send(OWDrakeEntity.this, OWDrakeEntity.ROAR_ANIMATION);
    }

    @Override
    protected boolean isMovementBlocked()
    {
        return getAnimation() == ROAR_ANIMATION || super.isMovementBlocked();
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().movePosition(-0.5d, 0.75d, 0);
        else event.getInfo().movePosition(-3, 0.3, 0);
    }

    @Override
    public void eatGrassBonus()
    {
        if (isChild()) addGrowth(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }

    @Override
    protected boolean canBeRidden(Entity entity)
    {
        return isSaddled() && !isChild() && (isOwner((LivingEntity) entity) || (!isTamed() && rideCooldown <= 0));
    }

    @Override
    public float getTravelSpeed()
    {
        float speed = (float) getAttributeValue(MOVEMENT_SPEED);
        if (canPassengerSteer()) speed += 0.45f;
        return speed;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1f);
        super.playStepSound(pos, blockIn);
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
            event.getSpawns().func_242575_a(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.OVERWORLD_DRAKE.get(), 8, 1, 3));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 70)
                .createMutableAttribute(MOVEMENT_SPEED, 0.2125)
                .createMutableAttribute(KNOCKBACK_RESISTANCE, 0.75)
                .createMutableAttribute(FOLLOW_RANGE, 20)
                .createMutableAttribute(ATTACK_KNOCKBACK, 2.85)
                .createMutableAttribute(ATTACK_DAMAGE, 8);
    }
}
