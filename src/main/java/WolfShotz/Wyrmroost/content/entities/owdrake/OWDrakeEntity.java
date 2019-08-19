package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.content.entities.ai.goals.DragonGrazeGoal;
import WolfShotz.Wyrmroost.content.entities.ai.goals.SleepGoal;
import WolfShotz.Wyrmroost.content.entities.owdrake.goals.DrakeAttackGoal;
import WolfShotz.Wyrmroost.content.entities.owdrake.goals.DrakeTargetGoal;
import WolfShotz.Wyrmroost.event.SetupSounds;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SaddleItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
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
    private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_ID, "Sprinting speed boost", (double) 0.8F, AttributeModifier.Operation.MULTIPLY_TOTAL)).setSaved(false);
    
    // Dragon Entity Animations
    public static final Animation SIT_ANIMATION = Animation.create(15);
    public static final Animation STAND_ANIMATION = Animation.create(15);
    public static final Animation GRAZE_ANIMATION = Animation.create(35);
    public static final Animation HORN_ATTACK_ANIMATION = Animation.create(22);
    public static final Animation ROAR_ANIMATION = Animation.create(35);
    public static final Animation TALK_ANIMATION = Animation.create(20);

    // Dragon Entity Data
    private static final DataParameter<Boolean> VARIANT = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);

    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world) {
        super(drake, world);

        moveController = new MovementController(this);
        
        hatchTimer = 12000;
        
        SLEEP_ANIMATION = Animation.create(20);
        WAKE_ANIMATION = Animation.create(15);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new DrakeAttackGoal(this));
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.2f, 12, 3));
        goalSelector.addGoal(6, new DragonBreedGoal(this, true));
        goalSelector.addGoal(10, new DragonGrazeGoal(this, 2, GRAZE_ANIMATION));
        goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 1d));
        goalSelector.addGoal(12, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(13, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
        targetSelector.addGoal(4, new DrakeTargetGoal(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        
        getAttribute(MAX_HEALTH).setBaseValue(50.0d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(10);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(6.0d);
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(VARIANT, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("variant", getVariant());
    
        super.writeAdditional(compound);
    }
    
    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        setVariant(compound.getBoolean("variant"));
    
        super.readAdditional(compound);
    }

    /**
     * The Variant of the drake.
     * false == Common, true == Savanna. Boolean since we only have 2 different variants
     */
    public boolean getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(boolean variant) { dataManager.set(VARIANT, variant); }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        IAttributeInstance attribute = getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        super.setSprinting(sprinting);

        if (attribute.getModifier(SPRINTING_ID) != null) attribute.removeModifier(SPRINTING_SPEED_BOOST);
        if (sprinting) attribute.applyModifier(SPRINTING_SPEED_BOOST);
    }

    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 50; }

    // ================================

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        Biome biome = worldIn.getBiome(new BlockPos(this));
        Set<Biome> biomes = BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA);

        if (biomes.contains(biome)) setVariant(true);

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void livingTick() {
        if (!world.isRemote) {
            setSprinting(isAngry());
            if (getAttackTarget() == null && isAngry()) setAngry(false);
        }
    
        if (getAnimation() == ROAR_ANIMATION && getAnimationTick() == 1)
            playSound(SetupSounds.OWDRAKE_ROAR, 1, 1);
        
        if (getAnimation() == HORN_ATTACK_ANIMATION && getAnimationTick() == 10 && getAttackTarget() != null)
            attackEntityAsMob(getAttackTarget());

        super.livingTick();
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        // If holding a saddle and this is not a child, Saddle up!
        if (stack.getItem() instanceof SaddleItem && !isSaddled() && !isChild()) { // instaceof: for custom saddles (if any)
            consumeItemFromStack(player, stack);
            setSaddled(true);
            playSound(SoundEvents.ENTITY_HORSE_SADDLE, 1f, 1f);

            return true;
        }
        
        // If Saddled and not sneaking, start riding
        if (isSaddled() && !isChild() && !isBreedingItem(stack) && hand == Hand.MAIN_HAND && !player.isSneaking()) {
            if (isSleeping()) setSleeping(false);
            setSit(false);
            player.startRiding(this);

            return true;
        }
    
        // If Sneaking, Sit
        if (isTamed() && !isBreedingItem(stack) && player.isSneaking() && isOwner(player)) {
            setSit(!isSitting());
        
            return true;
        }
        
        // If holding this dragons favorite food...
        if (isBreedingItem(stack)) {
            
            // If a child, tame it the old fashioned way
            if (isChild() && !isTamed()) {
                tame(getRNG().nextInt(10) == 0, player);
                consumeItemFromStack(player, stack);
                
                return true;
            }
            
            // If health is low, then heal up (Heal has priority over setting love mode!)
            if (isTamed() && getHealth() < getMaxHealth() && !player.isSneaking()) {
                consumeItemFromStack(player, stack);
                heal(stack.getItem() == Items.HAY_BLOCK? 6f : 2f);
        
                return true;
            }
        }

        return super.processInteract(player, hand);
    }
    
    /**
     * Called to handle the movement of the entity
     */
    @Override
    public void travel(Vec3d vec3d) {
        if (isBeingRidden() && canBeSteered() && isTamed()) {
            LivingEntity rider = (LivingEntity) getControllingPassenger();
            if (canPassengerSteer()) {
                float f = rider.moveForward, s = rider.moveStrafing;
                float speed = (float) getAttribute(MOVEMENT_SPEED).getValue() * (rider.isSprinting() ? 1.89f : 1);
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
                passenger.addVelocity(0, 5, 0);
                removePassengers();
            }
        }
    }
    
    @Override
    public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        setAngry(entitylivingbaseIn != null);
    }
    
    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || getAnimation() == ROAR_ANIMATION; }
    
    @Override
    public void eatGrassBonus() {
        if (isChild()) addGrowth(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1);

        super.playStepSound(pos, blockIn);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SetupSounds.OWDRAKE_IDLE; }
    
    @Override
    public void playAmbientSound() {
        if (!isSleeping()) {
            if (!hasActiveAnimation()) setAnimation(TALK_ANIMATION);
            super.playAmbientSound();
        }
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SetupSounds.OWDRAKE_HURT; }
    
    @Override
    protected void playHurtSound(DamageSource source) {
        if (!hasActiveAnimation()) setAnimation(TALK_ANIMATION);
        
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
    public void performGenericAttack() {
        setAnimation(HORN_ATTACK_ANIMATION);
    }
    
    @Override
    public EntitySize getSize(Pose poseIn) {
        return (isSitting() || isSleeping())? super.getSize(poseIn).scale(1f, 0.7f) : super.getSize(poseIn);
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.WHEAT, Items.HAY_BLOCK.asItem()}; }
    
    @Override
    public boolean canFly() { return false; }
    
    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, GRAZE_ANIMATION, HORN_ATTACK_ANIMATION, SIT_ANIMATION, STAND_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, ROAR_ANIMATION}; }
    // ==

}
