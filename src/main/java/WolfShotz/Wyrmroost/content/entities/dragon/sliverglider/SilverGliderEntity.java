package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.event.SetupSounds;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.*;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.ReflectionUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    // Dragon Entity Data
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SilverGliderEntity.class, DataSerializers.VARINT);

    // Dragon Animation
    public static final Animation SIT_ANIMATION = Animation.create(10);
    public static final Animation STAND_ANIMATION = Animation.create(10);
    public static final Animation TAKE_OFF_ANIMATION = Animation.create(10);
    
    public SilverGliderEntity(EntityType<? extends SilverGliderEntity> entity, World world) {
        super(entity, world);
        
        hatchTimer = 12000;
        
        SLEEP_ANIMATION = Animation.create(20);
        WAKE_ANIMATION = Animation.create(15);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.257657d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.366836d);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new NonTamedTemptGoal(this, 0.6d, true, Ingredient.fromItems(getFoodItems())));
        goalSelector.addGoal(5, new NonTamedAvoidGoal(this, PlayerEntity.class, 16f, 1f, 1.5f, true));
        goalSelector.addGoal(6, new DragonBreedGoal(this, true));
        goalSelector.addGoal(7, new DragonFollowOwnerGoal(this, 1.2d, 12d, 3d, 15d));
//        goalSelector.addGoal(8, new OrbitFlightGoal(this));
        goalSelector.addGoal(8, aiFlyWander = new FlightWanderGoal(this, 1500, 1));
        goalSelector.addGoal(9, new WanderGoal(this, 1d));
        goalSelector.addGoal(10, new WatchGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));
    }
    
    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();

        dataManager.register(VARIANT, getRNG().nextInt(3)); // For females, this value is redundant.
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putInt("variant", getVariant());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        setVariant(compound.getInt("variant"));
    }
    
    @Override
    public void setFlying(boolean fly) {
        super.setFlying(fly);
    
        setAnimation(TAKE_OFF_ANIMATION);
    }
    
    /**
     * Gets the variant of the Silver Glider.
     */
    public int getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }
    
    @Override
    public int getSpecialChances() { return 500; }

    // ================================
    
    
    @Override
    public void livingTick() {
        super.livingTick();
    }
    
    @Override
    public void tick() {
        super.tick();
        
        shouldFlyThreshold = 3 + (isRiding()? 2 : 0);
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();

        Entity entity = getRidingEntity();
        
        if (entity != null) {
            if (!entity.isAlive() || entity.isInWater()) {
                stopRiding();
                return;
            }

            setMotion(Vec3d.ZERO);

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                if (player.isSneaking() && !player.abilities.isFlying) {
                    stopRiding();
                    return;
                }
                
                if (shouldGlide(player) && !player.isElytraFlying()) {
                    Vec3d lookVec = player.getLookVec();
                    Vec3d playerMot = player.getMotion();
                    double xMot = playerMot.x + (lookVec.x / 12);
                    double zMot = playerMot.z + (lookVec.z / 12);
                    double yMot = lookVec.y * 1.5;
                    
                    if (yMot >= 0) yMot = -0.1f;
                    
                    player.setMotion(xMot, yMot, zMot);
                    if (!isFlying()) setFlying(true);
                }
                
                prevRotationPitch = rotationPitch = player.rotationPitch / 2;
                rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
                setRotation(player.rotationYawHead, rotationPitch);
                
                Vec3d rotationOffset = MathUtils.rotateYaw(player.renderYawOffset, 0, 0.5d);
                double offsetX = rotationOffset.x;
                double offsetZ = rotationOffset.z;
                if (player.isElytraFlying()) {
                    float angle = (0.01745329251f * player.renderYawOffset) + 90;
                    offsetX = (double) (-2f * MathHelper.sin((float) (Math.PI + angle)));
                    offsetZ = (double) (-2f * MathHelper.cos(angle));
                }
                
                setPosition(player.posX + offsetX, player.posY + 1.55d, player.posZ + offsetZ);
            }
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (hand != Hand.MAIN_HAND) return false; // only fire on the main hand
        
        // If holding this dragons favorite food, and not tamed, then tame it!
        if (!isTamed() && isBreedingItem(stack)) {
            tame(getRNG().nextInt(5) == 0, player);
            eat(stack);
            if (isSleeping()) setSleeping(false);
            
            return true;
        }

        // if tamed, then start riding the player
        if (isTamed() && stack.isEmpty() && !player.isSneaking() && player.getPassengers().isEmpty() && isOwner(player)) {
            startRiding(player, true);
            setSit(false);
            getNavigator().clearPath();
            
            return true;
        }
        
        if (isTamed() && stack.isEmpty() && player.isSneaking() && isOwner(player)) {
            setSit(!isSitting());
            
            return true;
        }

        return super.processInteract(player, hand);
    }
    
    public boolean shouldGlide(PlayerEntity player) {
        return (ReflectionUtils.isEntityJumping(player) && MathUtils.getAltitude(player) > shouldFlyThreshold) &&
                       player.getRidingEntity() == null &&
                       !player.abilities.isFlying       &&
                       !player.isInWater()              &&
                       super.canFly();
    }
    
    public static boolean canSpawnHere(EntityType<SilverGliderEntity> glider, IWorld world, SpawnReason reason, BlockPos blockPos, Random rand) {
        Block block = world.getBlockState(blockPos.down(1)).getBlock();
        
        return block == Blocks.AIR || block == Blocks.SAND || block == Blocks.WATER;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void doSpecialEffects() {
        if (ticksExisted % 5 == 0) {
            double x = posX + getRNG().nextGaussian();
            double y = posY + getRNG().nextDouble();
            double z = posZ + getRNG().nextGaussian();
            world.addParticle(new RedstoneParticleData(1f, 0.8f, 0, 1f), x, y, z, 0, 0.1925f, 0);
        }
    }
    
    @Override
    public boolean canFly() {
        if (isRiding() && getRidingEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) getRidingEntity();
            return super.canFly() && shouldGlide(player) || player.isElytraFlying();
        }
        if (isRiding()) return false;
        return super.canFly();
    }
    
    public boolean isRiding() { return getRidingEntity() != null; }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return SetupSounds.SILVERGLIDER_IDLE; }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) { return SetupSounds.SILVERGLIDER_HURT; }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return SetupSounds.SILVERGLIDER_DEATH; }
    
    @Override
    public void setSit(boolean sitting) {
        if (sitting != isSitting()) setAnimation(sitting? SIT_ANIMATION : STAND_ANIMATION);
        
        super.setSit(sitting);
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || getRidingEntity() != null; }
    
    @Override
    public boolean canBeCollidedWith() { return !isRiding(); }
    
    /** Array Containing all of the dragons food items */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.TROPICAL_FISH, Items.COD, Items.SALMON, Items.COOKED_COD, Items.COOKED_SALMON, Items.BAKED_POTATO}; }
    
    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, SIT_ANIMATION, STAND_ANIMATION, TAKE_OFF_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION}; }
    // ==
}