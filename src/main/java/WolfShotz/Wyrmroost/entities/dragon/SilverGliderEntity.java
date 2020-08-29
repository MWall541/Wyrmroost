package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Collection;
import java.util.function.Consumer;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat flightTimer = new TickFloat(1).setLimit(0, 1);

    public TemptGoal temptGoal;

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
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.18);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(1.5);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, temptGoal = CommonGoalWrappers.nonTamedTemptGoal(this, 0.8d, true, Ingredient.fromItems(getFoodItems().toArray(new Item[0]))));
        goalSelector.addGoal(4, CommonGoalWrappers.nonTamedAvoidGoal(this, PlayerEntity.class, 8f, 1f));
        goalSelector.addGoal(5, new DragonBreedGoal(this, true));
        goalSelector.addGoal(6, new FollowOwnerGoal(this, 10, 2f, 10, false));
        goalSelector.addGoal(7, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1));
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

        sitTimer.add((isSitting() || isSleeping())? 0.2f : -0.2f);
        sleepTimer.add(isSleeping()? 0.05f : -0.1f);
        flightTimer.add(isFlying()? -0.1f : 0.1f);
    }

    @Override
    public void updateRidden()
    {
        super.updateRidden();

        if (!(getRidingEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) getRidingEntity();

        if (shouldGlide(player))
        {
            Vec3d vec3d = player.getLookVec();
            player.setMotion(vec3d.x, Math.min(vec3d.y, -0.2d), vec3d.z);
        }
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
        return player.isJumping
                && !player.abilities.isFlying
                && !player.isElytraFlying()
                && !world.containsAnyLiquid(player.getBoundingBox())
                && canFly()
                && player.getPassengers().indexOf(this) == 0
                && getAltitude() - player.getHeight() > 4
                && player.getMotion().y <= 0;
    }

    @Override
    public boolean shouldFly()
    {
        Entity entity = getRidingEntity();
        if (entity instanceof PlayerEntity) return shouldGlide(((PlayerEntity) entity));
        return super.shouldFly();
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
        if (getRNG().nextInt(500) == 0) return -1;
        return getRNG().nextInt(3);
    }

    @Override
    public Vec3d getRidingPosOffset(int passengerIndex) { return new Vec3d(0, 1.81, 0.5d); }

    @Override
    public int getVerticalFaceSpeed() { return 30; }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 75; }

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

    class MoveController extends MovementController
    {
        MoveController() { super(SilverGliderEntity.this); }

        @Override
        public void tick()
        {
            if (action == Action.MOVE_TO)
            {
                double d0 = posX - getPosX();
                double d1 = posZ - getPosZ();
                double d2 = posY - getPosY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                if (d3 < (double) 2.5000003E-7F)
                {
                    setMoveForward(0);
                    return;
                }

                if (d3 > getWidth() * 10) setFlying(true);

                if (!isFlying())
                {
                    super.tick();
                    return;
                }

                action = MovementController.Action.WAIT;

                rotationYaw = MathHelper.func_219800_b(rotationYawHead, rotationYaw, getHorizontalFaceSpeed());
                float moveSpeed = (float) (speed);
                setAIMoveSpeed(moveSpeed);
                setMoveVertical((float) d2);
            }
        }
    }
}
