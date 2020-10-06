package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Consumer;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);

    public TemptGoal temptGoal;
    private boolean isGliding; // controlled by player-gliding.

    public SilverGliderEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(20d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.23);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.24);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, temptGoal = CommonGoalWrappers.nonTamedTemptGoal(this, 0.8d, true, Ingredient.fromItems(getFoodItems().toArray(new IItemProvider[0]))));
        goalSelector.addGoal(4, CommonGoalWrappers.nonTamedAvoidGoal(this, PlayerEntity.class, 8f, 1f));
        goalSelector.addGoal(5, new DragonBreedGoal(this, 0));
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new SwoopGoal());
        goalSelector.addGoal(8, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 7f));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(FLYING, false);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        if (isGliding && !isRiding()) isGliding = false;

        sitTimer.add((isSitting() || isSleeping())? 0.2f : -0.2f);
        sleepTimer.add(isSleeping()? 0.05f : -0.1f);
        flightTimer.add(isFlying() || isGliding()? 0.1f : -0.1f);
    }

    @Override
    public void updateRidden()
    {
        super.updateRidden();

        if (!(getRidingEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) getRidingEntity();

        final boolean FLAG = shouldGlide(player);
        if (FLAG)
        {
            Vec3d vec3d = player.getLookVec().scale(0.3);
            player.setMotion(player.getMotion().scale(0.6).add(vec3d.x, 0, vec3d.z));
            player.fallDistance = 0;
        }
        isGliding = FLAG;
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        Vec3d look = getLookVec();
        if (isFlying() && look.y < 0) setMotion(getMotion().add(0, look.y * 0.5, 0));

        super.travel(vec3d);
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        // tame
        if (!isTamed() && !world.isRemote && temptGoal.isRunning() && isBreedingItem(stack))
        {
            tame(getRNG().nextInt(3) == 0, player);
            eat(stack);
            return true;
        }

        if (isOwner(player)) // owner only actions
        {
            // sit
            if (player.isSneaking())
            {
                setSitting(!isSitting());
                return true;
            }

            // ride player
            if (player.getPassengers().size() == 0)
            {
                startRiding(player, true);
                setSitting(false);
                clearAI();
                return true;
            }
        }

        return false;
    }

    public boolean shouldGlide(PlayerEntity player)
    {
        if (isChild()) return false;
        if (!player.isJumping) return false;
        if (player.abilities.isFlying) return false;
        if (player.isElytraFlying()) return false;
        if (player.isInWater()) return false;
        if (player.getMotion().y > 0) return false;
        return getAltitude() - 1.8 > 4;
    }

    @Override
    public void doSpecialEffects()
    {
        if (getVariant() == -1 && ticksExisted % 5 == 0)
        {
            double x = getPosX() + getRNG().nextGaussian();
            double y = getPosY() + getRNG().nextDouble();
            double z = getPosZ() + getRNG().nextGaussian();
            world.addParticle(new RedstoneParticleData(1f, 0.8f, 0, 1f), x, y, z, 0, 0.1925f, 0);
        }
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSitting() || isSleeping()) size = size.scale(1, 0.87f);
        return size;
    }

    @Override
    public int getVariantForSpawn()
    {
        if (getRNG().nextDouble() < 0.002) return -1;
        return getRNG().nextInt(3);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_SILVERGLIDER_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.ENTITY_SILVERGLIDER_HURT.get(); }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.ENTITY_SILVERGLIDER_DEATH.get(); }

    @Override
    public Vec3d getRidingPosOffset(int passengerIndex) { return new Vec3d(0, 1.81, 0.5d); }

    @Override
    public boolean shouldFly() { return isGliding() || super.shouldFly(); }

    @Override
    public int getVerticalFaceSpeed() { return 30; }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 75; }

    public boolean isGliding() { return isGliding; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return ItemTags.FISHES.getAllElements(); }

    public static Consumer<EntityType<SilverGliderEntity>> getSpawnPlacements()
    {
        return t ->
        {
            for (Biome biome : ModUtils.getBiomesByTypes(BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.BEACH))
                biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(WREntities.SILVER_GLIDER.get(), 10, 1, 4));

            EntitySpawnPlacementRegistry.register(WREntities.SILVER_GLIDER.get(),
                    EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
                    Heightmap.Type.MOTION_BLOCKING,
                    (glider, world, reason, pos, rand) ->
                    {
                        if (reason == SpawnReason.SPAWNER) return true;
                        Block block = world.getBlockState(pos.down(1)).getBlock();
                        return block == Blocks.AIR || block == Blocks.SAND && world.getLightSubtracted(pos, 0) > 8;
                    });
        };
    }

    public class SwoopGoal extends Goal
    {
        private BlockPos pos;

        public SwoopGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute()
        {
            if (!isFlying()) return false;
            if (isRiding()) return false;
            if (getRNG().nextDouble() > 0.001) return false;
            if (world.getFluidState(this.pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, getPosition()).down()).isEmpty()) return false;
            return getPosY() - pos.getY() > 8;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return getPosition().distanceSq(pos) > 8;
        }

        @Override
        public void tick()
        {
            if (getNavigator().noPath()) getNavigator().tryMoveToXYZ(pos.getX(), pos.getY() + 2, pos.getZ(), 1);
            getLookController().setLookPosition(pos.getX(), pos.getY() + 2, pos.getZ());
        }
    }
}
