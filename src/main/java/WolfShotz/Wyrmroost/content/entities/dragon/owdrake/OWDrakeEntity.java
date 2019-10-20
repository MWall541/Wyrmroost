package WolfShotz.Wyrmroost.content.entities.dragon.owdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.goals.DrakeAttackGoal;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.goals.DrakeTargetGoal;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.event.SetupSounds;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.DragonFollowOwnerGoal;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.DragonGrazeGoal;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SaddleItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static net.minecraft.entity.SharedMonsterAttributes.*;

/**
 * Created by WolfShotz 7/10/19 - 22:18
 */
public class OWDrakeEntity extends AbstractDragonEntity
{
    private static final UUID SPRINTING_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_ID, "Sprinting speed boost", (double) 1.15F, AttributeModifier.Operation.MULTIPLY_TOTAL)).setSaved(false);
    
    // Dragon Entity Animations
    public static final Animation SIT_ANIMATION         = Animation.create(15);
    public static final Animation STAND_ANIMATION       = Animation.create(15);
    public static final Animation GRAZE_ANIMATION       = Animation.create(35);
    public static final Animation HORN_ATTACK_ANIMATION = Animation.create(15);
    public static final Animation ROAR_ANIMATION        = Animation.create(86);
    public static final Animation TALK_ANIMATION        = Animation.create(20);

    // Dragon Entity Data
    private static final DataParameter<Boolean> VARIANT_BOOL = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_CHEST    = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);

    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world) {
        super(drake, world);
        
        eggProperties = new DragonEggProperties(0.65f, 1f, 18000);
        moveController = new MovementController(this);
        
        SLEEP_ANIMATION = Animation.create(20);
        WAKE_ANIMATION = Animation.create(15);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new DrakeAttackGoal(this));
        goalSelector.addGoal(5, new DragonFollowOwnerGoal(this, 1.2d, 12d, 3d));
        goalSelector.addGoal(6, new DragonBreedGoal(this, false, true));
        goalSelector.addGoal(10, new DragonGrazeGoal(this, 2, GRAZE_ANIMATION));
        goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 1d) {@Override public boolean shouldExecute() { return !isSleeping() && super.shouldExecute(); }});
        goalSelector.addGoal(12, new LookAtGoal(this, LivingEntity.class, 10f) {@Override public boolean shouldExecute() { return !isSleeping() && super.shouldExecute(); }});
        goalSelector.addGoal(12, new LookRandomlyGoal(this) {@Override public boolean shouldExecute() { return !isSleeping() && super.shouldExecute(); }});

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this) {@Override public boolean shouldExecute() { return super.shouldExecute() && !isChild(); }});
        targetSelector.addGoal(4, new DrakeTargetGoal(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        
        getAttribute(MAX_HEALTH).setBaseValue(50d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(10);
        getAttribute(FOLLOW_RANGE).setBaseValue(20d);
        getAttribute(ATTACK_KNOCKBACK).setBaseValue(3.2d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(8d);
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(GENDER, getRNG().nextBoolean());
        dataManager.register(VARIANT_BOOL, false);
        dataManager.register(HAS_CHEST, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT nbt) {
        nbt.putBoolean("gender", getGender());
        nbt.putBoolean("variant", getDrakeVariant());
        
        super.writeAdditional(nbt);
    }
    
    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT nbt) {
        setGender(nbt.getBoolean("gender"));
        setDrakeVariant(nbt.getBoolean("variant"));
    
        setHasChest(invHandler.map(h -> !h.getStackInSlot(0).isEmpty()).orElse(false));
        if (nbt.getBoolean("saddled")) invHandler.ifPresent(h -> h.setStackInSlot(1, new ItemStack(Items.SADDLE, 1))); // Datafix
        
        super.readAdditional(nbt);
    }

    /**
     * The Variant of the drake.
     * false == Common, true == Savanna. Boolean since we only have 2 different variants
     */
    public boolean getDrakeVariant() { return dataManager.get(VARIANT_BOOL); }
    public void setDrakeVariant(boolean variant) { dataManager.set(VARIANT_BOOL, variant); }
    
    public boolean hasChest() { return dataManager.get(HAS_CHEST); }
    public void setHasChest(boolean set) { dataManager.set(HAS_CHEST, set); }
    
    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        if (isSprinting() == sprinting) return;
        
        IAttributeInstance attribute = getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        super.setSprinting(sprinting);

        if (attribute.getModifier(SPRINTING_ID) != null) attribute.removeModifier(SPRINTING_SPEED_BOOST);
        if (sprinting) attribute.applyModifier(SPRINTING_SPEED_BOOST);
    }

    @Override
    public int getSpecialChances() { return 100; }
    
    @Override
    public ItemStackHandler createInv() { return new ItemStackHandler(19); }
    
    // ================================

    @Override
    public void livingTick() {
        if (!world.isRemote) {
            if ((getAttackTarget() == null || !getAttackTarget().isAlive()) && isAngry()) setAngry(false);
            setSprinting(isAngry());
        }
        
        if (getAnimation() == ROAR_ANIMATION) {
            if (getAnimationTick() == 1)
                playSound(SetupSounds.OWDRAKE_ROAR, 2.5f, 1f);
            if (getAnimationTick() == 15) {
                getEntitiesNearby(5).forEach(e -> { // Dont get too close now ;)
                    if (e instanceof OWDrakeEntity) return;
                    double angle = (MathUtils.getAngle(posX, e.posX, posZ, e.posZ) + 90) * Math.PI / 180;
                    double x = 1.2 * (-Math.cos(angle));
                    double z = 1.2 * (-Math.sin(angle));
                    e.addVelocity(x, 0.4d, z);
                });
            }
            if (getAnimationTick() > 15)
                getEntitiesNearby(20, this).forEach(e -> {
                    if (e instanceof LivingEntity && !(e instanceof OWDrakeEntity)) ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 120));
                });
        }
        
        if (getAnimation() == HORN_ATTACK_ANIMATION && getAnimationTick() == 8) {
            Entity target = getAttackTarget();
            
            world.playSound(posX, posY, posZ, SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.AMBIENT, 1f, 0.5f, false);
            
            if (target != null) attackEntityAsMob(target);
            else attackInFront(1);
        }

        super.livingTick();
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack) {
        if (super.processInteract(player, hand, stack)) return true;
        
        // If holding a saddle and this is not a child, Saddle up!
        if (stack.getItem() instanceof SaddleItem && !isSaddled() && !isChild()) { // instaceof: for custom saddles (if any)
            getInvCap().ifPresent(s -> {
                s.insertItem(0, stack, false);
                consumeItemFromStack(player, stack);
            });

            return true;
        }
        
        // If Saddled and not sneaking, start riding
        if (isSaddled() && !isChild() && !player.isSneaking() && (!isTamed() || isOwner(player))) {
            setSit(false);
            player.startRiding(this);
        
            return true;
        }
    
        // If Sneaking, Sit
        if (isTamed() && !isBreedingItem(stack) && player.isSneaking() && isOwner(player)) {
            setSit(!isSitting());
        
            return true;
        }
    
    
        // If a child, tame it the old fashioned way
        if (isBreedingItem(stack) && isChild() && !isTamed()) {
            tame(getRNG().nextInt(10) == 0, player);
            consumeItemFromStack(player, stack);
        
            return true;
        }
        
        return false;
    }
    
    /**
     * Called to handle the movement of the entity
     */
    @Override
    public void travel(Vec3d vec3d) {
        if (isBeingRidden() && canBeSteered() && isOwner((LivingEntity) getControllingPassenger())) {
            LivingEntity rider = (LivingEntity) getControllingPassenger();
            if (canPassengerSteer()) {
                float f = rider.moveForward, s = rider.moveStrafing;
                float speed = (float) (getAttribute(MOVEMENT_SPEED).getValue() * (rider.isSprinting()? SPRINTING_SPEED_BOOST.getAmount() : 1));
                boolean moving = (f != 0 || s != 0);
                Vec3d target = new Vec3d(s, vec3d.y, f);

                setSprinting(rider.isSprinting());
                setAIMoveSpeed(speed);
                super.travel(target);
                if (moving || getAnimation() == OWDrakeEntity.HORN_ATTACK_ANIMATION) {
                    prevRotationYaw = rotationYaw = rider.rotationYaw;
                    rotationPitch = rider.rotationPitch * 0.5f;
                    setRotation(rotationYaw, rotationPitch);
                    renderYawOffset = rotationYaw;
                    rotationYawHead = renderYawOffset;
                }
//              setRotation(ModUtils.limitAngle(rotationYaw, ModUtils.calcAngle(target), 15), rotationPitch); TODO: Smooth Rotations

                return;
            }
        }

        super.travel(vec3d);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        
        if (!isTamed() && passenger instanceof LivingEntity && !world.isRemote) {
            int rand = new Random().nextInt(100);

            if (passenger instanceof PlayerEntity && rand == 0) tame(true, (PlayerEntity) passenger);
            else if (rand % 15 == 0) {
                if (EntityPredicates.CAN_AI_TARGET.test(passenger)) setAttackTarget((LivingEntity) passenger);
                passenger.stopRiding();
                ((LivingEntity) passenger).addPotionEffect(new EffectInstance(Effects.LEVITATION, 2, 100, false, false));
//                passenger.setMotion(1, 1, 1);
//                if (passenger instanceof PlayerEntity)
//                    Wyrmroost.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) passenger), new EntityMoveMessage(passenger));
            }
        }
    }
    
    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        Biome biome = worldIn.getBiome(new BlockPos(this));
        Set<Biome> biomes = BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA);
        
        if (biomes.contains(biome)) setDrakeVariant(true);
        
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) { super.fall(distance - 2, damageMultiplier); }
    
    @Override
    public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        setAngry(entitylivingbaseIn != null);
    }
    
    @Override
    public void eatGrassBonus() {
        if (isChild()) addGrowth(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        if (ticksExisted % 2 == 0) playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1);

        super.playStepSound(pos, blockIn);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SetupSounds.OWDRAKE_IDLE; }
    
    @Override
    public void playAmbientSound() {
        if (!isSleeping()) {
            if (noActiveAnimation()) setAnimation(TALK_ANIMATION);
            super.playAmbientSound();
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SetupSounds.OWDRAKE_HURT; }
    
    @Override
    protected void playHurtSound(DamageSource source) {
        if (noActiveAnimation()) setAnimation(TALK_ANIMATION);
        
        super.playHurtSound(source);
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return SetupSounds.OWDRAKE_DEATH; }
    
    @Override
    public void setSit(boolean sitting) {
        if (sitting != isSitting()) setAnimation(sitting? SIT_ANIMATION : STAND_ANIMATION);
    
        super.setSit(sitting);
    }
    
    @Override
    public void performGenericAttack() { setAnimation(HORN_ATTACK_ANIMATION); }
    
    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || getAnimation() == ROAR_ANIMATION; }
    
    @Override
    public EntitySize getSize(Pose poseIn) {
        return (isSitting() || isSleeping())? super.getSize(poseIn).scale(1f, 0.7f) : super.getSize(poseIn);
    }
    
    @Override
    protected int getExperiencePoints(PlayerEntity player) { return 2 + rand.nextInt(3); }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() { return Tags.Items.CROPS_WHEAT.getAllElements().toArray(new Item[0]); }
    
    @Override
    public boolean canFly() { return false; }
    
    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInv, PlayerEntity player) { return new OWDrakeInvContainer(this, playerInv, windowID); }
    
    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, GRAZE_ANIMATION, HORN_ATTACK_ANIMATION, SIT_ANIMATION, STAND_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, ROAR_ANIMATION}; }
    // ==

}
