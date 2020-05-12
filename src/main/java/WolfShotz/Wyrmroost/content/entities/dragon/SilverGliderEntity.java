package WolfShotz.Wyrmroost.content.entities.dragon;

import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.FlyerMoveController;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.goals.CommonGoalWrappers;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.goals.FlyerFollowOwnerGoal;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.EntityDataEntry;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
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

        addDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        addVariantData(3, true); // For females, this value is redundant.
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.257657d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.366836d);
    }

    public static void setSpawnConditions()
    {
        ModUtils.getBiomesByType(BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.BEACH).forEach(e ->
                e.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WREntities.SILVER_GLIDER.get(), 10, 1, 4)));
        EntitySpawnPlacementRegistry.register(WREntities.SILVER_GLIDER.get(),
                EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING,
                (glider, world, reason, blockPos, rand) -> // Tropical-like creature. Can spawn on beaches and over oceans.
                {
                    if (reason == SpawnReason.SPAWNER) return true;
                    Block block = world.getBlockState(blockPos.down(1)).getBlock();

                    return block == Blocks.AIR || block == Blocks.SAND;
                });
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(4, CommonGoalWrappers.nonTamedTemptGoal(this, 0.6d, true, Ingredient.fromTag(ItemTags.FISHES)));
        goalSelector.addGoal(5, CommonGoalWrappers.nonTamedAvoidGoal(this, PlayerEntity.class, 16f, 1f));
        goalSelector.addGoal(6, new DragonBreedGoal(this, true));
        goalSelector.addGoal(7, new FlyerFollowOwnerGoal(this, 12d, 3d, 15d, true));
        goalSelector.addGoal(10, CommonGoalWrappers.lookAt(this, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));

        goalSelector.addGoal(8, new FlyerWanderGoal(this, true)
        {
            @Override
            public Vec3d getPosition()
            {
                Vec3d vec3d = super.getPosition();
                if (vec3d != null && isFlying())
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
        if (!isFlying()) super.travel(vec3d);
    }

    @Override
    public void updateRidden()
    {
        super.updateRidden();

        Entity entity = getRidingEntity();

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) entity;

            if (shouldGlide(player) && !player.isElytraFlying())
            {
                Vec3d vec3d3 = player.getMotion();
                player.setMotion(vec3d3.add(0, 0.09, 0));

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
            }
            player.fallDistance = 0;
        }
    }

    public boolean shouldGlide(PlayerEntity player)
    {
        return (player.isJumping && getAltitude(true) > shouldFlyThreshold) &&
                player.getRidingEntity() == null &&
                !player.abilities.isFlying &&
                !player.isInWater() &&
                super.canFly();
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
        if (isTamed() && stack.isEmpty() && !player.isSneaking() && player.getPassengers().size() < 3 && isOwner(player))
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

    @Override
    public void doSpecialEffects()
    {
        if (ticksExisted % 5 == 0)
        {
            double x = getPosX() + getRNG().nextGaussian();
            double y = getPosY() + getRNG().nextDouble();
            double z = getPosZ() + getRNG().nextGaussian();
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
    public boolean canBeCollidedWith() { return !isRiding(); }

    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public Collection<Item> getFoodItems()
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