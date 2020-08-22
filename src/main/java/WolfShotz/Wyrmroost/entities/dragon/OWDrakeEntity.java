package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.goals.*;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.DragonArmorItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.TickFloat;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.Tags;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

/**
 * Created by WolfShotz 7/10/19 - 22:18
 */
public class OWDrakeEntity extends AbstractDragonEntity
{
    // inventory slot const's
    public static final int SADDLE_SLOT = 0;
    public static final int ARMOR_SLOT = 1;
    public static final int CHEST_SLOT = 2;

    // Dragon Entity Animations
    public static final Animation GRAZE_ANIMATION = new Animation(35);
    public static final Animation HORN_ATTACK_ANIMATION = new Animation(15);
    public static final Animation ROAR_ANIMATION = new Animation(86);
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);

    // Dragon Entity Data
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ARMOR = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.ITEMSTACK);

    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world)
    {
        super(drake, world);

        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, true);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);

        sitTimer.set(isSitting()? 1 : 0);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new ControlledAttackGoal(this, 1, true, 2.1, d -> AnimationPacket.send(d, HORN_ATTACK_ANIMATION)));
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.2d, 12f, 3f));
        goalSelector.addGoal(7, new DragonBreedGoal(this, true));
        goalSelector.addGoal(8, new GrazeGoal(this, 2, GRAZE_ANIMATION));
        goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, PlayerEntity.class, false));
        targetSelector.addGoal(4, new HurtByTargetGoal(this)
        {
            @Override
            public boolean shouldExecute() { return super.shouldExecute() && !isChild(); }
        });
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        
        getAttribute(MAX_HEALTH).setBaseValue(50d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(1); // no knockback
        getAttribute(FOLLOW_RANGE).setBaseValue(20d);
        getAttribute(ATTACK_KNOCKBACK).setBaseValue(3.2d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(5d);
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

    public boolean hasChest() { return getStackInSlot(CHEST_SLOT) != ItemStack.EMPTY; }

    public boolean isSaddled() { return dataManager.get(SADDLED); }

    public DragonArmorItem getArmor() { return (DragonArmorItem) dataManager.get(ARMOR).getItem(); }

    public void setArmored(ItemStack armor)
    {
        if (!(armor.getItem() instanceof DragonArmorItem)) armor = ItemStack.EMPTY;
        dataManager.set(ARMOR, armor);
        if (!world.isRemote)
        {
            IAttributeInstance attribute = getAttribute(SharedMonsterAttributes.ARMOR);
            if (attribute.getModifier(DragonArmorItem.ARMOR_UUID) != null)
                attribute.removeModifier(DragonArmorItem.ARMOR_UUID);
            if (!armor.isEmpty())
                attribute.applyModifier(new AttributeModifier("Armor Modifier", DragonArmorItem.getDmgReduction(armor), AttributeModifier.Operation.ADDITION).setSaved(true));
        }
    }

    public boolean isArmored() { return dataManager.get(ARMOR).getItem() instanceof DragonArmorItem; }

    @Override
    public int getVariantForSpawn()
    {
        if (getRNG().nextInt(100) == 0) return -1;
        if (BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA).contains(world.getBiome(getPosition()))) return 1;
        return 0;
    }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 24); }
    
    // ================================

    @Override
    public void livingTick()
    {
        sitTimer.add((isSitting() || isSleeping())? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.04f : -0.1f);

        LivingEntity target = getAttackTarget();
        boolean shouldSprint = target != null && target.isAlive();
        if (shouldSprint != isSprinting()) setSprinting(shouldSprint);

        if (getAnimation() == ROAR_ANIMATION && getAnimationTick() == 15)
        {
            for (LivingEntity e : getEntitiesNearby(15, e -> !isOnSameTeam(e))) // Dont get too close now ;)
            {
                e.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200));
                if (getDistanceSq(e) <= 5)
                {
                    double angle = (Mafs.getAngle(getPosX(), e.getPosX(), getPosZ(), e.getPosZ()) + 90) * Math.PI / 180;
                    double x = 1.2 * (-Math.cos(angle));
                    double z = 1.2 * (-Math.sin(angle));
                    e.addVelocity(x, 0.4d, z);
                }
            }
        }

        if (getAnimation() == HORN_ATTACK_ANIMATION)
        {
            prevRotationYaw = renderYawOffset = rotationYaw = rotationYawHead;
            if (getAnimationTick() == 8)
            {
                playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1, 0.5f);
                world.playSound(getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.AMBIENT, 1f, 0.5f, false);
                AxisAlignedBB size = getBoundingBox().shrink(0.2);
                AxisAlignedBB aabb = size.offset(Mafs.getYawVec(renderYawOffset, 0, size.getXSize() * 1.2));
                attackInAABB(aabb);
            }
        }
        super.livingTick();
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        if (stack.getItem() == Items.SADDLE && !isSaddled() && !isChild())
        {
            if (!world.isRemote) setStackInSlot(SADDLE_SLOT, stack);
            consumeItemFromStack(player, stack);
            return true;
        }

        if (isSaddled() && !isChild() && (!isTamed() || isOwner(player)))
        {
            if (!world.isRemote) player.startRiding(this);
            return true;
        }

        if (isFoodItem(stack) && isChild() && !isTamed())
        {
            tame(getRNG().nextInt(10) == 0, player);
            consumeItemFromStack(player, stack);

            return true;
        }
        
        return false;
    }

    /**
     * todo
     */
    @Override
    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);

        if (!world.isRemote && !isTamed() && passenger instanceof LivingEntity)
        {
            int rand = getRNG().nextInt(100);

            if (passenger instanceof PlayerEntity && rand == 0) tame(true, (PlayerEntity) passenger);
            else if (rand % 20 == 0 && EntityPredicates.CAN_AI_TARGET.test(passenger))
                setAttackTarget((LivingEntity) passenger);
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

        if (slot == ARMOR_SLOT) setArmored(stack);
    }

    @Override
    public void handleSleep()
    {
        if (!isSleeping()
                && --sleepCooldown <= 0
                && !world.isDaytime()
                && (!isTamed() || isSitting())
                && !isBeingRidden()
                && getAttackTarget() == null
                && getNavigator().noPath()
                && !isInWaterOrBubbleColumn()
                && !isFlying()
                && getRNG().nextInt(300) == 0) setSleeping(true);
        else if (isSleeping() && world.isDaytime() && getRNG().nextInt(150) == 0) setSleeping(false);
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) { return isSaddled(); }

    @Override
    public boolean canFly() { return false; }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) { return super.onLivingFall(distance - 2, damageMultiplier); }

    @Override
    public void setAttackTarget(@Nullable LivingEntity target)
    {
        if (target != null && getAttackTarget() != target)
        {
            if (!isTamed() && getAnimation() != OWDrakeEntity.ROAR_ANIMATION)
                AnimationPacket.send(OWDrakeEntity.this, OWDrakeEntity.ROAR_ANIMATION);
        }
        super.setAttackTarget(target);
    }
    
    @Override
    public void eatGrassBonus()
    {
        if (isChild()) addGrowth(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1f);
        super.playStepSound(pos, blockIn);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_OWDRAKE_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.ENTITY_OWDRAKE_HURT.get(); }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.ENTITY_OWDRAKE_DEATH.get(); }

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
        if (isSitting() || isSleeping()) size = size.scale(1, 0.75f);
        return size;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) { return 2 + rand.nextInt(3); }

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
    public void setMountCameraAngles(boolean backView)
    {
        if (backView) GlStateManager.translated(0, -0.5d, 0.5d);
        else GlStateManager.translated(0, 0, -3d);
    }

    @Override
    public void setAnimation(Animation animation)
    {
        super.setAnimation(animation);
        if (animation == ROAR_ANIMATION) playSound(WRSounds.ENTITY_OWDRAKE_ROAR.get(), 3f, 1f);
    }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return Tags.Items.CROPS_WHEAT.getAllElements(); }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, GRAZE_ANIMATION, HORN_ATTACK_ANIMATION, ROAR_ANIMATION};
    }
}
