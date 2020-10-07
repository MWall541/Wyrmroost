package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.LessShitLookController;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
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
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.EntityViewRenderEvent;
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
    public final TickFloat swimTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
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
        getAttribute(SWIM_SPEED).setBaseValue(0.3d);
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
        goalSelector.addGoal(3, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(4, new DragonBreedGoal(this, 1));
        goalSelector.addGoal(5, new JumpOutOfWaterGoal());
        goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1, 40));
        goalSelector.addGoal(7, new LookAtGoal(this, LivingEntity.class, 14f));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
        targetSelector.addGoal(4, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, LivingEntity.class, false, true, e ->
                (beached || e.isInWater()) && (e.getType() == EntityType.SQUID || e.getType() == EntityType.GUARDIAN || e.getType() == EntityType.PLAYER)));
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
        beachedTimer.add((beached)? 0.1f : -0.05f);
        swimTimer.add(canSwim()? -0.1f : 0.1f);
        sitTimer.add(isSitting()? 0.1f : -0.1f);

        if (isJumpingOutOfWater())
        {
            Vec3d motion = getMotion();
            rotationPitch = (float) (Math.signum(-motion.y) * Math.acos(Math.sqrt(Entity.horizontalMag(motion)) / motion.length()) * (double) (180f / Mafs.PI)) * 0.725f;
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
            ((LessShitLookController) getLookController()).restore();
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
            else if (animTick == 6)
            {
                AxisAlignedBB aabb = getBoundingBox().offset(Mafs.getYawVec(rotationYaw, 0, 5.5)).grow(0.85);
                attackInAABB(aabb, true, 40);
            }
        }
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (((beached && lightningCooldown > 60) || player.isCreative()) && isFoodItem(stack))
        {
            eat(stack);
            if (!world.isRemote) tame(getRNG().nextDouble() < 0.2, player);
            return true;
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        if (isInWater())
        {
            if (canPassengerSteer())
            {
                float speed = getTravelSpeed() * 0.225f;
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;

                rotationYawHead = entity.rotationYawHead;
                if (!isJumpingOutOfWater()) rotationPitch = entity.rotationPitch * 0.5f;
                double lookY = entity.getLookVec().y;
                if (entity.moveForward != 0 && (canSwim() || lookY < 0)) moveY = lookY;

                setAIMoveSpeed(speed);
                vec3d = new Vec3d(moveX, moveY, moveZ);
            }

            // add motion if were coming out of water fast; jump out of water like a dolphin
            if (getMotion().y > 0.25 && world.getBlockState(new BlockPos(getEyePosition(1)).up()).getFluidState().isEmpty())
                setMotion(getMotion().mul(1.2, 1.5f, 1.2d));

            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));

            prevLimbSwingAmount = limbSwingAmount;
            double xDiff = getPosX() - prevPosX;
            double yDiff = getPosY() - prevPosY;
            double zDiff = getPosZ() - prevPosZ;
            float amount = MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) * 4f;
            if (amount > 1f) amount = 1f;

            limbSwingAmount += (amount - limbSwingAmount) * 0.4f;
            limbSwing += limbSwingAmount;

            if (vec3d.z == 0 && getAttackTarget() == null && !isSitting())
                setMotion(getMotion().add(0, -0.003d, 0));
        }
        else super.travel(vec3d);
    }

    @Override
    public float getTravelSpeed()
    {
        //@formatter:off
        return isInWater()? (float) getAttribute(SWIM_SPEED).getValue()
                          : (float) getAttribute(MOVEMENT_SPEED).getValue() * 0.225f;
        //@formatter:on
    }

    @Override
    public ItemStack onFoodEaten(World world, ItemStack stack)
    {
        lightningCooldown = 0;
        return super.onFoodEaten(world, stack);
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
        if (pressed && noActiveAnimation())
        {
            if (key == KeybindPacket.MOUNT_KEY1) setAnimation(BITE_ANIMATION);
            else if (key == KeybindPacket.MOUNT_KEY2 && !world.isRemote && canZap())
            {
                EntityRayTraceResult ertr = Mafs.rayTraceEntities(getControllingPlayer(), 40, e -> e instanceof LivingEntity && e != this);
                if (ertr != null && shouldAttackEntity((LivingEntity) ertr.getEntity(), getOwner()))
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
                .add(getVectorForRotation(rotationPitch, rotationYaw).mul(-4d, -4d, -4));
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

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().movePosition(-10d, 1, 0);
        else event.getInfo().movePosition(-5, -0.75, 0);
    }

    @Override
    public boolean isFoodItem(ItemStack stack) { return WRItems.Tags.MEATS.contains(stack.getItem()); }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.KELP; }

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
    public boolean isImmuneToArrows() { return true; }

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

    public static Consumer<EntityType<ButterflyLeviathanEntity>> getSpawnPlacements()
    {
        return type ->
        {
            for (Biome biome : ModUtils.getBiomesByTypes(BiomeDictionary.Type.OCEAN))
                biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(type, 2, 1, 1));

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

    private class MoveController extends MovementController
    {
        public MoveController()
        {
            super(ButterflyLeviathanEntity.this);
        }

        public void tick()
        {
            if (action == Action.MOVE_TO && !canPassengerSteer())
            {
                action = Action.WAIT;
                double x = posX - getPosX();
                double y = posY - getPosY();
                double z = posZ - getPosZ();
                double distSq = x * x + y * y + z * z;
                if (distSq < 2.5000003E-7) setMoveForward(0f); // why move...
                else
                {
                    float yaw = (float) Math.toDegrees(MathHelper.atan2(z, x)) - 90f;
                    float pitch = -((float)(MathHelper.atan2(y, MathHelper.sqrt(x * x + z * z)) * 180 / Math.PI));
                    pitch = MathHelper.clamp(MathHelper.wrapDegrees(pitch), -85f, 85f);

                    rotationYawHead = yaw;
                    renderYawOffset = rotationYaw = limitAngle(rotationYaw, rotationYawHead, getHorizontalFaceSpeed());
                    rotationPitch = limitAngle(rotationPitch, pitch, 90);
                    ((LessShitLookController) getLookController()).freeze();
                    float speed = (float) this.speed * getTravelSpeed();
                    setAIMoveSpeed(speed);
                    if (isInWater())
                    {
                        moveForward = MathHelper.cos(pitch * (Mafs.PI / 180f)) * speed;
                        moveVertical = -MathHelper.sin(pitch * (Mafs.PI / 180f)) * speed;
                    }
                }
            }
            else
            {
                setAIMoveSpeed(0);
                setMoveStrafing(0);
                setMoveVertical(0);
                setMoveForward(0);
            }
        }
    }

    private class AttackGoal extends Goal
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

            getLookController().setLookPositionWithEntity(target, getHorizontalFaceSpeed(), getVerticalFaceSpeed());

            if (getNavigator().noPath() || ticksExisted % 10 == 0)
                getNavigator().tryMoveToEntityLiving(target, 1.2);

            if (noActiveAnimation())
            {
                if (distFromTarget > 225 && target.getType() == EntityType.PLAYER && canZap())
                    AnimationPacket.send(ButterflyLeviathanEntity.this, LIGHTNING_ANIMATION);
                else if (distFromTarget < 40 && MathHelper.degreesDifferenceAbs((float) Mafs.getAngle(ButterflyLeviathanEntity.this, target) + 90, rotationYaw) < 30)
                    AnimationPacket.send(ButterflyLeviathanEntity.this, BITE_ANIMATION);
            }
        }
    }

    private class JumpOutOfWaterGoal extends Goal
    {
        private BlockPos pos;

        public JumpOutOfWaterGoal()
        {
            setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute()
        {
            if (isSitting()) return false;
            if (canPassengerSteer()) return false;
            if (!canSwim()) return false;
            if (world.getFluidState(this.pos = world.getHeight(Heightmap.Type.WORLD_SURFACE, getPosition()).down()).isEmpty())
                return false;
            if (pos.getY() <= 0) return false;
            return getRNG().nextDouble() < 0.001;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return !canPassengerSteer() && canSwim();
        }

        @Override
        public void startExecuting()
        {
            getNavigator().clearPath();
            this.pos = pos.offset(getHorizontalFacing(), (int) ((pos.getY() - getPosY()) * 0.5d));
        }

        @Override
        public void tick()
        {
            getMoveHelper().setMoveTo(pos.getX(), pos.getY(), pos.getZ(), 1.2d);
        }

        @Override
        public void resetTask()
        {
            pos = null;
            clearAI();
        }
    }
}
