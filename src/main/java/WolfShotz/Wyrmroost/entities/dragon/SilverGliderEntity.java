package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.network.packets.SGGlidePacket;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Random;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);

    public TemptGoal temptGoal;
    public boolean isGliding; // controlled by player-gliding.

    public SilverGliderEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
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

        sitTimer.add((func_233684_eK_() || isSleeping())? 0.2f : -0.2f);
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

        if (world.isRemote && isGliding != FLAG)
        {
            SGGlidePacket.send(FLAG);
            isGliding = FLAG;
        }

        if (isGliding)
        {
            Vector3d vec3d = player.getLookVec().scale(0.3);
            player.setMotion(player.getMotion().scale(0.6).add(vec3d.x, Math.min(vec3d.y * 2, 0), vec3d.z));
            if (!world.isRemote) ((ServerPlayerEntity) player).connection.floating = false;
            player.fallDistance = 0;
        }
    }

    @Override
    public void travel(Vector3d vec3d)
    {
        Vector3d look = getLookVec();
        if (isFlying() && look.y < 0) setMotion(getMotion().add(0, look.y * 0.25, 0));

        super.travel(vec3d);
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (!isTamed() && isBreedingItem(stack))
        {
            if (!world.isRemote && (temptGoal.isRunning() || player.isCreative()))
            {
                tame(getRNG().nextDouble() < 0.333, player);
                eat(stack);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.CONSUME;
        }

        if (isOwner(player) && player.getPassengers().size() == 0 && !player.isSneaking())
        {
            startRiding(player, true);
            setSit(false);
            clearAI();
            return ActionResultType.func_233537_a_(world.isRemote);
        }

        return super.playerInteraction(player, hand, stack);
    }

    public boolean shouldGlide(PlayerEntity player)
    {
        if (isChild()) return false;
        if (!player.isJumping) return false;
        if (player.abilities.isFlying) return false;
        if (player.isElytraFlying()) return false;
        if (player.isInWater()) return false;
        if (player.getMotion().y > 0) return false;
        if (isGliding() && !player.isOnGround()) return true;
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
            world.addParticle(new RedstoneParticleData(1f, 0.8f, 0, 1f), x, y, z, 0, 0.2f, 0);
        }
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (func_233684_eK_() || isSleeping()) size = size.scale(1, 0.87f);
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
    public Vector3d getRidingPosOffset(int passengerIndex) { return new Vector3d(0, 1.81, 0.5d); }

    @Override
    public boolean shouldFly() { return isRiding()? isGliding() : super.shouldFly(); }

    @Override
    public int getVerticalFaceSpeed() { return 30; }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 75; }

    public boolean isGliding() { return isGliding; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return ItemTags.FISHES.getAllElements(); }

    public static boolean getSpawnPlacement(EntityType<SilverGliderEntity> fEntityType, IServerWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random)
    {
        if (spawnReason == SpawnReason.SPAWNER) return true;
        Block block = world.getBlockState(blockPos.down()).getBlock();
        return block == Blocks.AIR || block == Blocks.SAND && world.getLightSubtracted(blockPos, 0) > 8;
    }

    @Nullable
    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.OCEAN || event.getCategory() == Biome.Category.BEACH)
            event.getSpawns().func_242575_a(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.SILVER_GLIDER.get(), 10, 1, 4));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 20)
                .createMutableAttribute(MOVEMENT_SPEED, 0.23)
                .createMutableAttribute(FLYING_SPEED, 0.24);
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
