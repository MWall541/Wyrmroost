package WolfShotz.Wyrmroost.content.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.client.screen.staff.StaffScreen;
import WolfShotz.Wyrmroost.content.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.DragonBodyController;
import WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.goals.*;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.content.entities.multipart.IMultiPartEntity;
import WolfShotz.Wyrmroost.content.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.content.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.NetworkUtils;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.io.SlotBuilder;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.WalkAndSwimNodeProcessor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class ButterflyLeviathanEntity extends AbstractDragonEntity implements IMultiPartEntity
{
    public static final int CONDUIT_SLOT = 0;
    public static final DataParameter<Boolean> HAS_CONDUIT = EntityDataManager.createKey(ButterflyLeviathanEntity.class, DataSerializers.BOOLEAN);

    public static final Animation CONDUIT_ANIMATION = new Animation(46);
    public static final Animation ROAR_ANIMATION = new Animation(46);
    public static final Animation BITE_ANIMATION = new Animation(20);

    // Multipart
    public MultiPartEntity headPart;
    public MultiPartEntity wingLeftPart;
    public MultiPartEntity wingRightPart;
    public MultiPartEntity tail1Part;
    public MultiPartEntity tail2Part;
    public MultiPartEntity tail3Part;

    public RandomWalkingGoal moveGoal;
    public int lightningAttackCooldown;
    private boolean prevUnderWater;

    public ButterflyLeviathanEntity(EntityType<? extends ButterflyLeviathanEntity> blevi, World world)
    {
        super(blevi, world);
        ignoreFrustumCheck = WRConfig.disableFrustumCheck;
        moveController = new MoveController();
        stepHeight = 2;

//        if (!world.isRemote)
//        {
//            headPart = createPart(this, 4.2f, 0, 0.75f, 2.25f, 1.75f);
//            wingLeftPart = createPart(this, 5f, -90, 0.35f, 2.25f, 3.15f);
//            wingRightPart = createPart(this, 5f, 90, 0.35f, 2.25f, 3.15f);
//            tail1Part = createPart(this, 4.5f, 180, 0.35f, 2.25f, 2.25f, 0.85f);
//            tail2Part = createPart(this, 8f, 180, 0.35f, 2.25f, 2.25f, 0.75f);
//            tail3Part = createPart(this, 12f, 180, 0.5f, 2f, 2f, 0.5f);
//        }
//        setImmune(BrineFluid.BRINE_WATER);
        setImmune(DamageSource.LIGHTNING_BOLT);

        addVariantData(2, true);
    }

    @Override
    protected BodyController createBodyController() { return new BFlyBodyController(); }

    public static void handleSpawning()
    {
        WREntities.registerBiomeSpawnEntry(WREntities.BUTTERFLY_LEVIATHAN.get(), 1, 1, 1, WREntities.getByTypes(BiomeDictionary.Type.OCEAN));
        EntitySpawnPlacementRegistry.register(WREntities.BUTTERFLY_LEVIATHAN.get(),
                EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                ButterflyLeviathanEntity::canSpawnHere);
    }

    public static boolean canSpawnHere(EntityType<? extends MobEntity> typeIn, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn)
    {
        return worldIn.getBlockState(pos).getFluidState().isTagged(FluidTags.WATER);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) { return new Navigator(); }

    @Override
    public CreatureAttribute getCreatureAttribute() { return CreatureAttribute.WATER; }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, sitGoal = new WaterSitGoal(this));
        goalSelector.addGoal(2, new MoveToHomeGoal(this));
        goalSelector.addGoal(3, new ControlledAttackGoal(this, 1, true, 3.4, b -> swingArm(Hand.MAIN_HAND)));
//        goalSelector.addGoal(3, CommonGoalWrappers.followOwner(this, 1.2d, 20f, 3f));
        goalSelector.addGoal(4, new DragonBreedGoal(this, false, false));
        goalSelector.addGoal(5, moveGoal = new RandomSwimmingGoal(this, 1d, 10));
        goalSelector.addGoal(6, CommonGoalWrappers.lookAt(this, 10f));
        goalSelector.addGoal(7, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this, Entity::isInWater));
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, PlayerEntity.class, false));
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(100d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.08d); // On land speed
        getAttribute(SWIM_SPEED).setBaseValue(0.1d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(10);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(8d);
        getAttribute(FOLLOW_RANGE).setBaseValue(28d);
    }

    // ================================
    //           Entity NBT
    // ================================
    
    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(HAS_CONDUIT, false);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
        dataManager.set(HAS_CONDUIT, invHandler.map(i -> i.getStackInSlot(0).getItem() == Items.CONDUIT).orElse(false)); // bcus effects shouldnt be done on load
    }

    public void setHasConduit(boolean flag, boolean playEffects)
    {
        if (hasConduit() == flag) return;
        dataManager.set(HAS_CONDUIT, flag);
        if (flag)
        {
            getAttribute(MOVEMENT_SPEED).setBaseValue(0.06d);
            if (playEffects) setAnimation(CONDUIT_ANIMATION);
        }
        else
        {
            getAttribute(MOVEMENT_SPEED).setBaseValue(0.045d);
            if (playEffects) playSound(SoundEvents.BLOCK_CONDUIT_DEACTIVATE, 1, 1);
        }
    }

    public boolean hasConduit() { return dataManager.get(HAS_CONDUIT); }

    // =================================

    @Override
    public void tick()
    {
        //        tickParts();

        boolean underWater = isUnderWater();
        if (underWater != prevUnderWater) onWaterChange(underWater);
        prevUnderWater = underWater;

        if (lightningAttackCooldown > 0) --lightningAttackCooldown;

        if (hasConduit())
        {
            long i = world.getGameTime();
            if (world.isRemote) spawnConduitParticles();
            if (i % 40L == 0L) applyEffects();
            if (i % 80L == 0L)
            {
                if (rand.nextBoolean()) playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT, 2f, 1f);
                else playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT_SHORT, 2f, 1f);
            }
        }

        if (getAnimation() == CONDUIT_ANIMATION)
        {
            if (animationTick == 1) playSound(WRSounds.BFLY_ROAR.get(), 3f, 1f);
            if (animationTick == 15)
            {
                playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1, 1);
                if (!world.isRemote)
                    ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, getPosX(), getPosY(), getPosZ(), true));

                Vec3d vec3d = getConduitPos(new Vec3d(getPosX(), getPosY(), getPosZ()));
                for (int i = 0; i < 26; ++i)
                {
                    double velX = Math.cos(i);
                    double velZ = Math.sin(i);
                    world.addParticle(ParticleTypes.CLOUD, vec3d.x, vec3d.y + 0.8, vec3d.z, velX, 0, velZ);
                }
            }
        }

        if (getAnimation() == ROAR_ANIMATION && !world.isRemote)
        {
            if (animationTick == 1) playSound(WRSounds.BFLY_ROAR.get(), 3, 1);
            if (animationTick == 15) strikeTarget();
        }

        if (getAnimation() == BITE_ANIMATION)
        {
            rotationYaw = rotationYawHead;
            if (animationTick == 1) playSound(WRSounds.BFLY_HURT.get(), 1, 1);
            if (animationTick == 10)
            {
                AxisAlignedBB size = getBoundingBox().shrink(0.3);
                AxisAlignedBB aabb = size.offset(QuikMaths.calculateYawAngle(renderYawOffset, 0, size.getXSize() * 1.5));
                attackInAABB(aabb);
            }
        }

        super.tick();
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        float speed = (float) (getAttribute(MOVEMENT_SPEED).getValue());
        if (isInWater()) speed = (float) getAttribute(SWIM_SPEED).getValue();
        if (canPassengerSteer() && canBeSteered())
        {
            LivingEntity rider = (LivingEntity) getControllingPassenger();

            rotationPitch = rider.rotationPitch * 0.5f;
            rotationYawHead = rider.rotationYawHead;
            rotationYaw = MathHelper.func_219800_b(rotationYawHead, rotationYaw, 8);
            if (isInWater() && (rider.moveForward != 0 || rider.moveStrafing != 0))
            {
                float yVel = -(MathHelper.sin(rotationPitch * (QuikMaths.PI / 180f))) * (speed * 15);
                if (yVel > 0f && !isUnderWater() && getMotion().y < 0.5f) yVel = 0;
                setMotion(getMotion().x, yVel, getMotion().z);
            }
            setAIMoveSpeed(speed);
            vec3d = new Vec3d(rider.moveStrafing, vec3d.y, rider.moveForward);
        }

        if (isServerWorld() && isInWater())
        {
            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));
        }
        else super.travel(vec3d);
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.processInteract(player, hand, stack)) return true;

        boolean isFoodItem = isFoodItem(stack);
        if (!isTamed() && !isInWater() && (isBreedingItem(stack) || isFoodItem))
        {
            int chances = isFoodItem? 3 : 7;
            tame(getRNG().nextInt(chances) == 0, player);
            return true;
        }

        if (isOwner(player))
        {
            if (player.isSneaking())
            {
                setSit(!isSitting());
                return true;
            }

            if (!world.isRemote && !isChild())
            {
                player.startRiding(this);
                setSit(false);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == CONDUIT_SLOT) setHasConduit(!stack.isEmpty(), !onLoad);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.INVENTORY);
        screen.addAction(StaffAction.TARGETING);
        super.addScreenInfo(screen);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);
        container.addSlot(new SlotBuilder(getInvHandler(), CONDUIT_SLOT, SlotBuilder.CENTER_X, SlotBuilder.CENTER_Y).limit(1).only(Items.CONDUIT));
    }

    public void applyEffects()
    {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(25d).expand(0, world.getHeight(), 0);
        List<PlayerEntity> list = world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb);
        if (list.isEmpty()) return;
        for (PlayerEntity player : list)
            if (player.isWet() && getPosition().withinDistance(new BlockPos(player), 18d))
                player.addPotionEffect(new EffectInstance(Effects.CONDUIT_POWER, 260, 0, true, true));
    }
    
    private void spawnConduitParticles()
    {
        if (rand.nextInt(35) != 0) return;
        Vec3d vec3d = getConduitPos(new Vec3d(getPosX(), getPosY(), getPosZ()));
        for (int i = 0; i < 16; ++i)
        {
            double motionX = QuikMaths.nextPseudoDouble(rand) * 1.5f;
            double motionY = QuikMaths.nextPseudoDouble(rand);
            double motionZ = QuikMaths.nextPseudoDouble(rand) * 1.5f;
            world.addParticle(ParticleTypes.NAUTILUS, vec3d.x, vec3d.y + 2.25, vec3d.z, motionX, motionY, motionZ);
        }
    }

    public void onWaterChange(boolean underWater)
    {
        if (underWater)
        {
            if (moveGoal != null) moveGoal.setExecutionChance(10);
        }
        else
        {
            if (moveGoal != null) moveGoal.setExecutionChance(120);
        }

        recalculateSize();
    }

    @Override
    public void performGenericAttack() { swingArm(Hand.MAIN_HAND); }

    @Override
    public void performAltAttack(boolean shouldContinue)
    {
        if (world.isRemote) return;
        if (!canLightningStrike()) return;
        PlayerEntity rider = getControllingPlayer();
        if (rider == null) return;
        RayTraceResult rtr = QuikMaths.rayTrace(world, rider, 25d, false);
        if (rtr.getType() != RayTraceResult.Type.ENTITY) return;
        EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
        if (!(ertr.getEntity() instanceof LivingEntity)) return;
        setAttackTarget((LivingEntity) ertr.getEntity());
        NetworkUtils.sendAnimationPacket(this, ROAR_ANIMATION);
    }

    public void strikeTarget()
    {
        LivingEntity target = getAttackTarget();
        if (!world.isRemote)
            ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), false));
        lightningAttackCooldown = 125;
        if (getControllingPlayer() != null) setAttackTarget(null);
    }

    public boolean canLightningStrike()
    {
        return lightningAttackCooldown <= 0 && (isInWater() || world.isRaining()) && hasConduit();
    }

    @Override
    public void swingArm(Hand hand)
    {
        super.swingArm(hand);
        setAnimation(BITE_ANIMATION);
    }

    @Override
    public void handleSleep() {}

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        float eyeHeight = 3.1f;
        if (isUnderWater()) eyeHeight = 2f;
        if (isChild()) eyeHeight *= 0.35f;
        return eyeHeight;
    }

    @Override
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked() || getAnimation() == CONDUIT_ANIMATION || getAnimation() == ROAR_ANIMATION;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.BFLY_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.BFLY_HURT.get(); }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.BFLY_DEATH.get(); }

    @Override
    public int getTalkInterval() { return 165; }

    @Override
    public MultiPartEntity[] getParts()
    {
        return new MultiPartEntity[]{headPart, wingLeftPart, wingRightPart, tail1Part, tail2Part, tail3Part};
    }

    @Override
    public void setMountCameraAngles(boolean backView)
    {
        if (backView) GlStateManager.translated(0, -1d, -10d);
        else GlStateManager.translated(0, -1, -7d);
    }

    public Vec3d getConduitPos(Vec3d offset)
    {
        return QuikMaths.calculateYawAngle(renderYawOffset, 0, 4.2).add(offset.x, offset.y + getEyeHeight() + 2, offset.z);
    }

    public boolean isUnderWater() { return areEyesInFluid(FluidTags.WATER, true); }

    @Override
    public boolean canFly() { return false; }

    @Override
    public boolean canBeSteered() { return true; }

    @Override
    public boolean canBeRiddenInWater(Entity rider) { return true; }

    @Override
    public boolean isNotColliding(IWorldReader worldIn) { return worldIn.checkNoEntityCollision(this); }

    @Override
    public boolean canBreatheUnderwater() { return true; }

    @Override
    public boolean isPushedByWater() { return false; }

    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public Collection<Item> getFoodItems()
    {
        return WRItems.Tags.MEATS.getAllElements();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return Lists.newArrayList(Items.KELP, Items.DRIED_KELP, Items.DRIED_KELP_BLOCK, Items.SEAGRASS, Items.SEA_PICKLE)
                .contains(stack.getItem());
    }

    @Override
    public LazyOptional<DragonInvHandler> createInv() { return LazyOptional.of(() -> new DragonInvHandler(this, 1)); }
    
    @Override
    public DragonEggProperties createEggProperties()
    {
        return new DragonEggProperties(0.75f, 1.25f, 40000).setConditions(Entity::isInWater);
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, CONDUIT_ANIMATION, ROAR_ANIMATION, BITE_ANIMATION};
    }

    class BFlyBodyController extends DragonBodyController
    {
        public BFlyBodyController() { super(ButterflyLeviathanEntity.this); }

        @Override
        public void updateRenderAngles()
        {
            double deltaX = getPosX() - dragon.prevPosX;
            double deltaZ = getPosZ() - dragon.prevPosZ;
            double dist = deltaX * deltaX + deltaZ * deltaZ;

            if (!dragon.isInWater() && dist > 0.0001)
            {
                super.updateRenderAngles();
                return;
            }

            dragon.renderYawOffset = dragon.rotationYaw;
            dragon.rotationYawHead = MathHelper.func_219800_b(dragon.rotationYawHead, dragon.renderYawOffset, (float) dragon.getHorizontalFaceSpeed());
        }
    }

    class MoveController extends MovementController
    {
        public MoveController() { super(ButterflyLeviathanEntity.this); }

        public void tick()
        {
            if (mob.isInWater()) mob.fallDistance = 0;

            if (action == MovementController.Action.MOVE_TO && !mob.getNavigator().noPath())
            {
                double x = posX - getPosX();
                double y = posY - getPosY();
                double z = posZ - getPosZ();
                double planeDistSq = x * x + y * y + z * z;
                if (planeDistSq < 2.5000003E-7)
                {
                    mob.setMoveForward(0);
                    return;
                }
                float f = (float) (MathHelper.atan2(z, x) * (180f / QuikMaths.PI)) - 90f;
                mob.rotationYaw = limitAngle(mob.rotationYaw, f, 10f);
                mob.renderYawOffset = mob.rotationYaw;
                mob.rotationYawHead = mob.rotationYaw;
                float speed = (float) (mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                if (mob.isInWater())
                {
                    speed = (float) (mob.getAttribute(LivingEntity.SWIM_SPEED).getValue());
                    mob.setAIMoveSpeed(speed);
                    float f2 = -((float) (MathHelper.atan2(y, MathHelper.sqrt(x * x + z * z)) * (double) (180f / QuikMaths.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85f, 85f);
                    mob.rotationPitch = limitAngle(mob.rotationPitch, f2, 5f);
                    float f3 = MathHelper.cos(mob.rotationPitch * (QuikMaths.PI / 180f));
                    float f4 = MathHelper.sin(mob.rotationPitch * (QuikMaths.PI / 180f));
                    mob.moveForward = f3 * (speed * 8);
                    mob.moveVertical = -f4 * (speed * 8);
                }
                else mob.setAIMoveSpeed(speed);
            }
            else
            {
                mob.setAIMoveSpeed(0);
                mob.setMoveStrafing(0);
                mob.setMoveVertical(0);
                mob.setMoveForward(0);
            }
        }
    }

    public class Navigator extends SwimmerPathNavigator
    {
        public Navigator() { super(ButterflyLeviathanEntity.this, ButterflyLeviathanEntity.this.world); }

        @Override
        protected PathFinder getPathFinder(int pathSearchRange)
        {
            return new PathFinder(new WalkAndSwimNodeProcessor(), pathSearchRange);
        }

        @Override
        public boolean canEntityStandOnPos(BlockPos pos) { return !world.getBlockState(pos).isAir(world, pos); }

        @Override
        protected boolean canNavigate() { return true; }
    }

}
