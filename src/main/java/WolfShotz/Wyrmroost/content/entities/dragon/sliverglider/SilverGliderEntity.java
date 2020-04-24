package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.ai.FlyerMoveController;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.entityutils.PlayerMount;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.*;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity implements PlayerMount.IHeadMount
{
    // Animation
    public static final Animation SIT_ANIMATION = new Animation(10);
    public static final Animation STAND_ANIMATION = new Animation(10);

    public int shotDownTimer;

    public SilverGliderEntity(EntityType<? extends SilverGliderEntity> entity, World world)
    {
        super(entity, world);

        moveController = new FlyerMoveController(this, true);

        SLEEP_ANIMATION = new Animation(20);
        WAKE_ANIMATION = new Animation(15);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.257657d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.366836d);
    }
    
    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(4, CommonEntityGoals.nonTamedTemptGoal(this, 0.6d, true, Ingredient.fromItems(getFoodItems().toArray(new Item[0]))));
        goalSelector.addGoal(5, new NonTamedAvoidGoal(this, PlayerEntity.class, 16f, 1f, 1.5f, true));
        goalSelector.addGoal(6, new DragonBreedGoal(this, true, true));
        goalSelector.addGoal(7, new FlyerFollowOwnerGoal(this, 12d, 3d, 15d, true));
        goalSelector.addGoal(10, CommonEntityGoals.lookAt(this, 10f));
        goalSelector.addGoal(11, CommonEntityGoals.lookRandomly(this));

        goalSelector.addGoal(8, new FlyerWanderGoal(this, true)
        {
            @Override
            public Vec3d getPosition()
            {
                Vec3d vec3d = super.getPosition();
                if (isFlying())
                {
                    for (int i = 1; i < 4; i++) // avoid water: y value is always positive
                        if (world.getBlockState(SilverGliderEntity.this.getPosition().down(i)).getMaterial().isLiquid())
                            return new Vec3d(vec3d.x, Math.abs(vec3d.y), vec3d.z);
                }
                return vec3d;
            }
        });
    }
    
    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData()
    {
        super.registerData();
        
        dataManager.register(GENDER, getRNG().nextBoolean());
        dataManager.register(VARIANT, getRNG().nextInt(3)); // For females, this value is redundant.
    }
    
    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
        
        nbt.putBoolean("gender", getGender());
        nbt.putInt("variant", getVariant());
    }
    
    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
        
        setVariant(nbt.getInt("variant"));
        setGender(nbt.getBoolean("gender"));
    }
    
    @Override
    public void setFlying(boolean fly)
    {
        if (isFlying() == fly) return;
        super.setFlying(fly);
    }
    
    /**
     * Gets the variant of the Silver Glider.
     */
    public int getVariant()
    {
        return dataManager.get(VARIANT);
    }
    
    public void setVariant(int variant)
    {
        dataManager.set(VARIANT, variant);
    }

    @Override
    public int getSpecialChances() { return 500; }

    // ================================

    @Override
    public void tick()
    {
        super.tick();

        shouldFlyThreshold = 3 + (isRiding()? 2 : 0);
        if (shotDownTimer > 0) --shotDownTimer;
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        if (!isFlying())
        {
            super.travel(vec3d);
            return;
        }
    }

    @Override
    public void updateRidden()
    {
        super.updateRidden();

        Entity entity = getRidingEntity();

        if (entity != null)
        {
            if (!entity.isAlive() || isInWater())
            {
                stopRiding();
                return;
            }
            
            setMotion(Vec3d.ZERO);
            
            if (entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entity;
                
                if (player.isSneaking() && !player.abilities.isFlying)
                {
                    stopRiding();
                    return;
                }
                
                if (shouldGlide(player) && !player.isElytraFlying())
                {
                    Vec3d vec3d3 = player.getMotion();
                    player.setMotion(vec3d3.add(0, 0.09, 0));
                    if (vec3d3.y > -0.5d) player.fallDistance = 1f;

                    Vec3d vec3d = player.getLookVec();
                    float f6 = player.rotationPitch * (QuikMaths.PI / 180F);
                    double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                    double d11 = Math.sqrt(horizontalMag(vec3d3));
                    double d12 = vec3d.length();
                    float f3 = MathHelper.cos(f6);
                    f3 = (float) ((double) f3 * (double) f3 * Math.min(1d, d12 / 0.4d));
                    vec3d3 = player.getMotion().add(0, 0.08 * (-1 + (double) f3 * 0.75d), 0);
                    if (vec3d3.y < 0.0D && d9 > 0)
                    {
                        double d3 = vec3d3.y * -0.1D * (double) f3;
                        vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
                    }

                    if (f6 < 0.0F && d9 > 0.0D)
                    {
                        double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                        vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
                    }

                    if (d9 > 0.0D)
                    {
                        vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
                    }

                    player.setMotion(vec3d3.mul(0.99F, 0.98F, 0.99F));
                    player.move(MoverType.SELF, player.getMotion());
                    if (player.collidedHorizontally && !world.isRemote)
                    {
                        double d14 = Math.sqrt(horizontalMag(player.getMotion()));
                        double d4 = d11 - d14;
                        float f4 = (float) (d4 * 10.0D - 3.0D);
                        if (f4 > 0.0F)
                        {
                            playSound(getFallSound((int) f4), 1.0F, 1.0F);
                            player.attackEntityFrom(DamageSource.FLY_INTO_WALL, f4);
                            attackEntityFrom(DamageSource.FLY_INTO_WALL, f4);
                        }
                    }


//                    Vec3d lookVec = player.getLookVec();
//                    Vec3d playerMot = player.getMotion();
//                    double xMot = playerMot.x + (lookVec.x / 12);
//                    double zMot = playerMot.z + (lookVec.z / 12);
//                    double yMot = lookVec.y * 1.5;
//
//                    if (yMot >= 0) yMot = -0.1f;
//
//                    player.setMotion(xMot, yMot, zMot);
//                    setFlying(true);
                }
                
                prevRotationPitch = rotationPitch = player.rotationPitch / 2;
                rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
                setRotation(player.rotationYawHead, rotationPitch);

                Vec3d rotationOffset = QuikMaths.calculateYawAngle(player.renderYawOffset, 0, 0.5d);
                double offsetX = rotationOffset.x;
                double offsetZ = rotationOffset.z;
                if (player.isElytraFlying())
                {
                    float angle = (0.01745329251f * player.renderYawOffset) + 90;
                    offsetX = -2f * MathHelper.sin((float) (Math.PI + angle));
                    offsetZ = -2f * MathHelper.cos(angle);
                }
                
                setPosition(player.posX + offsetX, player.posY + 1.55d, player.posZ + offsetZ);
            }
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.processInteract(player, hand, stack)) return true;
        
        // If holding this dragons favorite food, and not tamed, then tame it!
        if (!isTamed() && isBreedingItem(stack))
        {
            tame(getRNG().nextInt(5) == 0, player);
            eat(stack);

            return true;
        }
        
        // if tamed, then start riding the player
        if (isTamed() && stack.isEmpty() && !player.isSneaking() && !PlayerMount.hasHeadOccupant(player) && isOwner(player))
        {
            startRiding(player, true);
            setSit(false);
            getNavigator().clearPath();
            
            return true;
        }
        
        if (isTamed() && stack.isEmpty() && player.isSneaking() && isOwner(player))
        {
            setSit(!isSitting());
            
            return true;
        }
        
        return false;
    }
    
    public boolean shouldGlide(PlayerEntity player)
    {
        return (player.isJumping && getAltitude(true) > shouldFlyThreshold) &&
                player.getRidingEntity() == null &&
                !player.abilities.isFlying &&
                !player.isInWater() &&
                super.canFly();
    }
    
    public static <T extends AbstractDragonEntity> boolean canSpawnHere(EntityType<T> glider, IWorld world, SpawnReason reason, BlockPos blockPos, Random rand)
    {
        Block block = world.getBlockState(blockPos.down(1)).getBlock();
        
        return block == Blocks.AIR || block == Blocks.SAND || block == Blocks.WATER;
    }
    
    @Override
    public void doSpecialEffects()
    {
        if (ticksExisted % 5 == 0)
        {
            double x = posX + getRNG().nextGaussian();
            double y = posY + getRNG().nextDouble();
            double z = posZ + getRNG().nextGaussian();
            world.addParticle(new RedstoneParticleData(1f, 0.8f, 0, 1f), x, y, z, 0, 0.1925f, 0);
        }
    }
    
    @Override
    public boolean canFly()
    {
        if (shotDownTimer > 0) return false;
        if (isRiding() && getRidingEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) getRidingEntity();
            return super.canFly() && shouldGlide(player) || player.isElytraFlying();
        }
        if (isRiding()) return false;
        return super.canFly();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!isTamed() && source.isProjectile())
        {
            shotDownTimer = 12000;
            setFlying(false);
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean isRiding()
    {
        return getRidingEntity() != null;
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.5f;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.SILVERGLIDER_IDLE.get();
    }
    
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return WRSounds.SILVERGLIDER_HURT.get();
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.SILVERGLIDER_DEATH.get();
    }
    
    @Override
    public void setSit(boolean sitting)
    {
        if (sitting != isSitting()) setAnimation(sitting? SIT_ANIMATION : STAND_ANIMATION);
        
        super.setSit(sitting);
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || getRidingEntity() != null;
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !isRiding();
    }
    
    /** Array Containing all of the dragons food items */
    @Override
    public List<Item> getFoodItems()
    {
        return new ArrayList<>(ItemTags.FISHES.getAllElements());
    }
    
    @Override
    public DragonEggProperties createEggProperties()
    {
        return new DragonEggProperties(0.4f, 0.65f, 12000);
    }
    
    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, SIT_ANIMATION, STAND_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION};
    }
}