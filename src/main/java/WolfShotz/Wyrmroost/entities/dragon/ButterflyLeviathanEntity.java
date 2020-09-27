package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Consumer;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class ButterflyLeviathanEntity extends AbstractDragonEntity
{
    public static final DataParameter<Boolean> HAS_CONDUIT = EntityDataManager.createKey(ButterflyLeviathanEntity.class, DataSerializers.BOOLEAN);
    public static final Animation LIGHTNING_ANIMATION = new Animation(64);
    public static final Animation CONDUIT_ANIMATION = new Animation(59);
    public static final Animation BITE_ANIMATION = new Animation(17);
    public static final int CONDUIT_SLOT = 0;

    public final TickFloat beachedTimer = new TickFloat().setLimit(0, 1);
    public int lightningCooldown;
    public boolean beached = true;

    public ButterflyLeviathanEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);
        ignoreFrustumCheck = WRConfig.disableFrustumCheck;
        moveController = new MoveController();
        stepHeight = 2;

        setPathPriority(PathNodeType.WATER, 0);
        setImmune(DamageSource.LIGHTNING_BOLT);
        setImmune(DamageSource.IN_FIRE);

        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(100d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.08d);
        getAttribute(SWIM_SPEED).setBaseValue(0.1d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(1);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(8d);
        getAttribute(FOLLOW_RANGE).setBaseValue(32d);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, sitGoal = new SitGoal(this));
        goalSelector.addGoal(1, new MoveToHomeGoal(this));
        goalSelector.addGoal(2, new AttackGoal());
        goalSelector.addGoal(3, new FollowOwnerGoal());
        goalSelector.addGoal(4, new DragonBreedGoal(this, 1));
        goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1, 40));
        goalSelector.addGoal(6, new LookAtGoal(this, LivingEntity.class, 14f));
        goalSelector.addGoal(7, new LookRandomlyGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
        targetSelector.addGoal(4, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, LivingEntity.class, false, true, e ->
                e.getType() == EntityType.SQUID || e.getType() == EntityType.GUARDIAN || e.getType() == EntityType.PLAYER));

    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(HAS_CONDUIT, false);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        Vec3d conduitPos = getConduitPos();

        // cooldown for lightning attack
        if (lightningCooldown > 0) --lightningCooldown;

        // handle "beached" logic (if this fat bastard is on land)
        boolean prevBeached = beached;
        if (!beached && onGround && !inWater) beached = true;
        else if (beached && inWater) beached = false;
        if (prevBeached != beached) recalculateSize();
        beachedTimer.add(beached? 0.1f : -0.05f);

        // handle water jumping rotations
        if (isJumpingOutOfWater() && !firstUpdate)
        {
            Vec3d motion = getMotion();
            double planeMotionSq = Math.sqrt(Entity.horizontalMag(motion));
            rotationPitch = (float) (Math.signum(-motion.y) * Math.acos(planeMotionSq / motion.length()) * 180 / Math.PI);
        }

        // conduit effects
        if (hasConduit())
        {
            if (world.isRemote && isWet() && getRNG().nextDouble() <= 0.1)
            {
                for (int i = 0; i < 16; ++i)
                    world.addParticle(ParticleTypes.NAUTILUS,
                            conduitPos.x,
                            conduitPos.y + 2.25,
                            conduitPos.z,
                            Mafs.nextDouble(getRNG()) * 1.5f,
                            Mafs.nextDouble(getRNG()),
                            Mafs.nextDouble(getRNG()) * 1.5f);
            }

            // nearby entities: if evil, kill, if not, give reallly cool potion effect
            if (ticksExisted % 80 == 0)
            {
                boolean attacked = false;
                for (LivingEntity entity : getEntitiesNearby(25, Entity::isWet))
                {
                    if (entity != getAttackTarget() && (entity instanceof PlayerEntity || isOnSameTeam(entity)))
                        entity.addPotionEffect(new EffectInstance(Effects.CONDUIT_POWER, 220, 0, true, true));

                    if (!attacked && entity instanceof IMob)
                    {
                        attacked = true;
                        entity.attackEntityFrom(DamageSource.MAGIC, 4);
                        playSound(SoundEvents.BLOCK_CONDUIT_ATTACK_TARGET, 1, 1);
                    }
                }
            }

            // play some sounds because immersion is important for some reason
            if (world.isRemote && ticksExisted % 100 == 0)
                if (rand.nextBoolean()) playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT, 1f, 1f, true);
                else playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT_SHORT, 1f, 1f, true);
        }

        // handle animation logic
        Animation animation = getAnimation();
        int animTick = getAnimationTick();

        // zap the fuckers
        if (animation == LIGHTNING_ANIMATION)
        {
            lightningCooldown += 6;
            if (animTick == 10) playSound(WRSounds.ENTITY_BFLY_ROAR.get(), 3f, 1f, true);
            if (!world.isRemote && isWet() && animTick >= 10)
            {
                LivingEntity target = getAttackTarget();
                if (target != null)
                {
                    if (hasConduit())
                    {
                        if (animTick % 10 == 0)
                            ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, target.getPosX() + Mafs.nextDouble(getRNG()) * 2.333, target.getPosY(), target.getPosZ() + Mafs.nextDouble(getRNG()) * 2.333, false));
                    }
                    else if (animTick == 10)
                        ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), false));
                }
            }
        }
        else if (animation == CONDUIT_ANIMATION) // oooo very scary
        {
            if (animTick == 0) playSound(WRSounds.ENTITY_BFLY_ROAR.get(), 5f, 1, true);
            else if (animTick == 15)
            {
                playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1, 1);
                if (!world.isRemote)
                {
                    Vec3d vec3d = getConduitPos();
                    ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, vec3d.x, vec3d.y + 1, vec3d.z, true));
                }
                else
                {
                    for (int i = 0; i < 26; ++i)
                    {
                        double velX = Math.cos(i);
                        double velZ = Math.sin(i);
                        world.addParticle(ParticleTypes.CLOUD, conduitPos.x, conduitPos.y + 0.8, conduitPos.z, velX, 0, velZ);
                    }
                }
            }
        }
        else if (animation == BITE_ANIMATION)
        {
            if (animTick == 0) playSound(WRSounds.ENTITY_BFLY_HURT.get(), 1, 1, true);
            else if (animTick == 6) attackInFront(0.85);
        }
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        if (isInWater())
        {
            float speed = getTravelSpeed();
            if (canPassengerSteer())
            {
                speed *= 0.225f;
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;

                rotationYawHead = entity.rotationYawHead;
                if (!isJumpingOutOfWater()) rotationPitch = entity.rotationPitch * 0.5f;

                if (entity.moveForward != 0)
                    moveY = getLookVec().y * speed * 18;

                setAIMoveSpeed(speed);
                vec3d = new Vec3d(moveX, moveY, moveZ);
            }

            // add motion if were coming out of water fast; jump out of water like a dolphin
            if (getMotion().y > 0.5 && world.getBlockState(new BlockPos(getEyePosition(1)).up()).getFluidState().isEmpty())
                addVelocity(0, 0.25, 0);

            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));

            if (vec3d.z == 0 && getAttackTarget() == null && !isSitting())
                setMotion(getMotion().add(0, -0.003d, 0));
        }
        else super.travel(vec3d);
    }

    @Override
    protected float getTravelSpeed()
    {
        //@formatter:off
        return isInWater()? (float) getAttribute(SWIM_SPEED).getValue() * 2
                          : (float) getAttribute(MOVEMENT_SPEED).getValue();
        //@formatter:on
    }

    @Override
    public void doSpecialEffects()
    {
        if (getVariant() == -1 && ticksExisted % 25 == 0)
        {
            double x = getPosX() + (Mafs.nextDouble(getRNG()) * getWidth() + 1);
            double y = getPosY() + (getRNG().nextDouble() * getHeight() + 1);
            double z = getPosZ() + (Mafs.nextDouble(getRNG()) * getWidth() + 1);
            world.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == CONDUIT_SLOT)
        {
            boolean flag = stack.getItem() == Items.CONDUIT;
            boolean hadConduit = hasConduit();
            dataManager.set(HAS_CONDUIT, flag);
            if (!onLoad && flag && !hadConduit) setAnimation(CONDUIT_ANIMATION);
        }
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (pressed /*&& noActiveAnimation()*/)
        {
            if (key == KeybindPacket.MOUNT_KEY1) setAnimation(BITE_ANIMATION);
            else if (key == KeybindPacket.MOUNT_KEY2 && !world.isRemote && canZap())
            {
                EntityRayTraceResult ertr = Mafs.rayTraceEntities(getControllingPlayer(), 40, e -> e instanceof LivingEntity && e != this);
                if (ertr != null)
                {
                    setAttackTarget((LivingEntity) ertr.getEntity());
                    AnimationPacket.send(this, LIGHTNING_ANIMATION);
                }
            }
        }
    }

    public Vec3d getConduitPos()
    {
        return getEyePosition(1)
                .add(0, 0.4, 0)
                .add(getVectorForRotation(rotationPitch, rotationYaw).mul(4d, 4d, 4));
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.INVENTORY);
        super.addScreenInfo(screen);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);
        container.addSlot(new SlotBuilder(getInvHandler(), CONDUIT_SLOT).only(Items.CONDUIT).limit(1));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_BFLY_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.ENTITY_BFLY_HURT.get(); }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.ENTITY_BFLY_DEATH.get(); }

    @Override
    protected PathNavigator createNavigator(World world) { return new Navigator(); }

    public boolean hasConduit() { return dataManager.get(HAS_CONDUIT); }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 1); }

    @Override
    public boolean canAttack(LivingEntity target) { return !isChild() && super.canAttack(target); }

    public boolean isJumpingOutOfWater() { return !isInWater() && !beached; }

    public boolean canZap() { return isWet() && lightningCooldown <= 0; }

    @Override
    public boolean canBreatheUnderwater() { return true; }

    public CreatureAttribute getCreatureAttribute() { return CreatureAttribute.WATER; }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize size) { return size.height * (beached? 1f : 0.6f); }

    @Override
    public EntitySize getSize(Pose poseIn) { return getType().getSize().scale(getRenderScale()); }

    @Override
    protected boolean canBeRidden(Entity entityIn) { return isTamed() && !isChild(); }

    @Override // 2 passengers
    protected boolean canFitPassenger(Entity passenger) { return getPassengers().size() < 2; }

    @Override
    public Vec3d getPassengerPosOffset(Entity entity, int index) { return new Vec3d(0, getMountedYOffset(), index == 1? -2 : 0); }

    @Override
    public int getHorizontalFaceSpeed() { return 6; }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(2); }

    @Override
    public boolean canFly() { return false; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }

    @Override
    public Animation[] getAnimations() { return new Animation[] {LIGHTNING_ANIMATION, CONDUIT_ANIMATION, BITE_ANIMATION}; }

    public static Consumer<EntityType<ButterflyLeviathanEntity>> getSpawnConditions()
    {
        return type ->
        {
            for (Biome biome : ModUtils.getBiomesByTypes(BiomeDictionary.Type.OCEAN))
                biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(type, 2, 0, 1));

            EntitySpawnPlacementRegistry.register(type,
                    EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                    Heightmap.Type.OCEAN_FLOOR_WG,
                    (entity, world, reason, pos, rand) -> reason == SpawnReason.SPAWN_EGG || world.getFluidState(pos).getFluid() instanceof WaterFluid);
        };
    }

    public class Navigator extends SwimmerPathNavigator
    {
        public Navigator()
        {
            super(ButterflyLeviathanEntity.this, ButterflyLeviathanEntity.this.world);
        }

        @Override
        protected PathFinder getPathFinder(int range)
        {
            return new PathFinder(nodeProcessor = new WalkAndSwimNodeProcessor(), range);
        }

        @Override
        public boolean canEntityStandOnPos(BlockPos pos)
        {
            return !world.getBlockState(pos.down()).isAir(world, pos.down());
        }

        @Override
        protected boolean canNavigate() { return true; }
    }

    public class MoveController extends MovementController
    {
        public MoveController()
        {
            super(ButterflyLeviathanEntity.this);
        }

        public void tick()
        {
            if (action == MovementController.Action.MOVE_TO && !getNavigator().noPath())
            {
                double d0 = this.posX - getPosX();
                double d1 = this.posY - getPosY();
                double d2 = this.posZ - getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < 0.0001d) setMoveForward(0.0F);
                else
                {
                    float yaw = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    rotationYaw = this.limitAngle(rotationYaw, yaw, 10.0F);
                    renderYawOffset = rotationYaw;
                    rotationYawHead = rotationYaw;
                    float speed = (float) (this.speed * getAttribute(SWIM_SPEED).getValue());
                    if (isInWater())
                    {
                        setAIMoveSpeed(speed * 0.02F);
                        float f2 = -((float) (MathHelper.atan2(d1, MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double) (180F / (float) Math.PI)));
                        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                        rotationPitch = this.limitAngle(rotationPitch, f2, 5.0F);
                        float f3 = MathHelper.cos(rotationPitch * ((float) Math.PI / 180F));
                        float f4 = MathHelper.sin(rotationPitch * ((float) Math.PI / 180F));
                        moveForward = f3 * speed;
                        moveVertical = -f4 * speed;
                    }
                    else setAIMoveSpeed(speed * 0.1F);

                }
            }
            else
            {
                setAIMoveSpeed(0.0F);
                setMoveStrafing(0.0F);
                setMoveVertical(0.0F);
                setMoveForward(0.0F);
            }
        }
    }

    public class AttackGoal extends Goal
    {
        public AttackGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute()
        {
            return !canPassengerSteer() && getAttackTarget() != null;
        }

        @Override
        public void tick()
        {
            LivingEntity target = getAttackTarget();
            double distFromTarget = getDistanceSq(target);

            getLookController().setLookPositionWithEntity(target, 1, 1);

            if (getNavigator().noPath() || ticksExisted % 10 == 0)
                getNavigator().tryMoveToEntityLiving(target, 1.2);

            if (noActiveAnimation())
            {
                if (distFromTarget > 225 && canZap())
                    AnimationPacket.send(ButterflyLeviathanEntity.this, LIGHTNING_ANIMATION);
                else if (distFromTarget < 50)
                    AnimationPacket.send(ButterflyLeviathanEntity.this, BITE_ANIMATION);
            }
        }
    }

    // because the vanilla one will throw a fucking fit if we have a water navigator
    public class FollowOwnerGoal extends Goal
    {
        private int recalcPathTime;

        public FollowOwnerGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute()
        {
            final int MINIMUM_FOLLOW_DIST = 225;

            if (isSitting() || getLeashed()) return false;
            LivingEntity owner = getOwner();
            if (owner == null) return false;
            if (owner.isSpectator()) return false;
            return getDistanceSq(owner) > MINIMUM_FOLLOW_DIST;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            final int MINMUM_TRAVEL_DIST = 18;

            if (isSitting() || getLeashed()) return false;
            LivingEntity owner = getOwner();
            if (owner == null) return false;
            if (owner.isSpectator()) return false;
            return getDistanceSq(owner) > MINMUM_TRAVEL_DIST;
        }

        @Override
        public void resetTask() { navigator.clearPath(); }

        @Override
        public void tick()
        {
            LivingEntity owner = getOwner();
            getLookController().setLookPositionWithEntity(owner, 10, getVerticalFaceSpeed());

            if (--recalcPathTime <= 0)
            {
                recalcPathTime = 10;
                if (getDistanceSq(owner) > 260) tryTeleportToOwner();
                else getNavigator().tryMoveToEntityLiving(owner, 1.1d);
            }
        }
    }
}
