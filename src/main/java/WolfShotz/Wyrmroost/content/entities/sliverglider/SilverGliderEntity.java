package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.content.entities.ai.goals.NonTamedAvoidGoal;
import WolfShotz.Wyrmroost.content.entities.ai.goals.NonTamedTemptGoal;
import WolfShotz.Wyrmroost.event.SetupSounds;
import WolfShotz.Wyrmroost.util.MathUtils;
import WolfShotz.Wyrmroost.util.ReflectionUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.Random;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    // Dragon Entity Data
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SilverGliderEntity.class, DataSerializers.VARINT);

    // Dragon Animation
    private static final Animation SIT_ANIMATION = Animation.create(10);
    
    public SilverGliderEntity(EntityType<? extends SilverGliderEntity> entity, World world) {
        super(entity, world);
        
        hatchTimer = 18000;
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.257657d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4.0d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(1.2d);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        goalSelector.addGoal(4, new NonTamedTemptGoal(this, 0.6d, true, Ingredient.fromItems(getFoodItems())));
        goalSelector.addGoal(5, new NonTamedAvoidGoal(this, PlayerEntity.class, 16f, 1f, 1.5f, true));
        goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.2f, 10, 4));
        goalSelector.addGoal(7, new DragonBreedGoal(this, true));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1d));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
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

    /**
     * Gets the variant of the Silver Glider.
     */
    public int getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }

    /**
     * Set The chances this dragon can be an albino. Set it to 0 to have no chance
     */
    @Override
    public int getAlbinoChances() { return 0; }

    // ================================
    
    @Override
    public void livingTick() {
        super.livingTick();
        
        flyingThreshold = 3 + (isRiding()? 2 : 0);
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();

        Entity entity = getRidingEntity();
        
        if (entity != null) {
            if (!entity.isAlive()) {
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

                if (ReflectionUtils.isEntityJumping(player) && MathUtils.getAltitude(player) > 1.3 && player.getRidingEntity() == null && !player.abilities.isFlying) {
                    //TODO this is not functional.. AT ALL...
                    
                    Vec3d vec3d3 = player.getMotion();
                    
                    if (vec3d3.y > -0.5D) player.fallDistance = 1.0F;
    
                    Vec3d vec3d = player.getLookVec();
                    float f6 = player.rotationPitch * ((float) Math.PI / 180F);
                    double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                    double d11 = Math.sqrt(func_213296_b(vec3d3));
                    double d12 = vec3d.length();
                    float f3 = MathHelper.cos(f6);
                    
                    f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
                    vec3d3 = player.getMotion().add(0.0D, 0.08d * (-1.0D + (double) f3 * 0.75D), 0.0D);
                    if (vec3d3.y < 0.0D && d9 > 0.0D) {
                        double d3 = vec3d3.y * -0.1D * (double) f3;
                        vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
                    }
    
                    if (f6 < 0.0F && d9 > 0.0D) {
                        double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                        vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
                    }
    
                    if (d9 > 0.0D) vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
    
                    player.setMotion(vec3d3.mul((double) 0.99F, (double) 0.98F, (double) 0.99F));
                }
                
                prevRotationPitch = rotationPitch = player.rotationPitch / 2;
                rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
                setRotation(player.rotationYawHead, rotationPitch);
    
                setPosition(player.posX, player.posY + 1.85d, player.posZ);
            }
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        if (hand != Hand.MAIN_HAND) return false; // only fire on the main hand

        // If holding this dragons favorite food, and not tamed, then tame it!
        if (!isTamed() && isBreedingItem(stack)) {
            tame(getRNG().nextInt(10) == 0, player);
            
            return true;
        }

        // if tamed, then start riding the player
        if (isTamed() && stack.isEmpty() && !player.isSneaking()) {
            startRiding(player, true);
            setSit(false);
            
            return true;
        }
        
        if (isTamed() && stack.isEmpty() && player.isSneaking()) {
            setSit(!isSitting());
            
            return true;
        }

        return super.processInteract(player, hand);
    }

    public static boolean canSpawnHere(EntityType<SilverGliderEntity> glider, IWorld world, SpawnReason reason, BlockPos blockPos, Random rand) {
        Block block = world.getBlockState(blockPos.down(1)).getBlock();
        
        return block == Blocks.AIR;
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
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || getRidingEntity() != null; }

    /** Array Containing all of the dragons food items */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.TROPICAL_FISH, Items.COD, Items.SALMON, Items.COOKED_COD, Items.COOKED_SALMON, Items.BAKED_POTATO}; }
    
    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
    // ==
}