package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.IMob;
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class ButterflyLeviathanEntity extends AbstractDragonEntity
{
    public static final DataParameter<Boolean> HAS_CONDUIT = EntityDataManager.createKey(ButterflyLeviathanEntity.class, DataSerializers.BOOLEAN);
    public static final Animation ROAR_ANIMATION = new Animation(64);
    public static final Animation CONDUIT_ANIMATION = new Animation(59);
    public static final int CONDUIT_SLOT = 0;

    public final TickFloat beachedTimer = new TickFloat().setLimit(0, 1);

    public ButterflyLeviathanEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);
        ignoreFrustumCheck = WRConfig.disableFrustumCheck;
        stepHeight = 2;

        setPathPriority(PathNodeType.WATER, 0);
        setImmune(DamageSource.LIGHTNING_BOLT);

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
    protected void registerData()
    {
        super.registerData();
        dataManager.register(HAS_CONDUIT, false);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

//        boolean beached = !isInWater() && ;
//        beachedTimer.add(beached? 0.1f : -0.1f);

        Vec3d conduitPos = getConduitPos();
        if (hasConduit())
        {
            if (world.isRemote && getRNG().nextDouble() <= 0.1)
            {
                for (int i = 0; i < 16; ++i)
                {
                    double motionX = Mafs.nextDouble(getRNG()) * 1.5f;
                    double motionY = Mafs.nextDouble(getRNG());
                    double motionZ = Mafs.nextDouble(getRNG()) * 1.5f;
                    world.addParticle(ParticleTypes.NAUTILUS, conduitPos.x, conduitPos.y + 2.25, conduitPos.z, motionX, motionY, motionZ);
                }
            }

            if (ticksExisted % 80 == 0)
            {
                boolean attacked = false;
                for (LivingEntity entity : getEntitiesNearby(35, Entity::isWet))
                {
                    entity.addPotionEffect(new EffectInstance(Effects.CONDUIT_POWER, 220, 0, true, true));
                    if (!attacked && entity instanceof IMob)
                    {
                        attacked = true;
                        entity.attackEntityFrom(DamageSource.MAGIC, 4);
                        playSound(SoundEvents.BLOCK_CONDUIT_ATTACK_TARGET, 1, 1);
                    }
                }
            }

            if (world.isRemote && ticksExisted % 100 == 0)
                if (rand.nextBoolean()) playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT, 1f, 1f, true);
                else playSound(SoundEvents.BLOCK_CONDUIT_AMBIENT_SHORT, 1f, 1f, true);
        }

        Animation animation = getAnimation();
        int animTick = getAnimationTick();

        if (animation == ROAR_ANIMATION)
        {
            if (animTick == 10) playSound(WRSounds.ENTITY_BFLY_ROAR.get(), 3f, 1f, true);
            if (!world.isRemote)
            {
                LivingEntity target = getAttackTarget();
                if (target != null)
                {
                    if (hasConduit())
                    {
                        if (animTick % 4 == 0)
                            ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, target.getPosX() * Mafs.nextDouble(getRNG()), target.getPosY(), target.getPosZ() * Mafs.nextDouble(getRNG()), false));
                    }
                    else if (animTick == 10)
                        ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), false));
                }
            }
        }
        else if (animation == CONDUIT_ANIMATION && animTick == 15)
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

    @Override
    public void travel(Vec3d vec3d)
    {
        if (isInWater())
        {
            if (canPassengerSteer())
            {
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;
                float speed = (float) getAttribute(SWIM_SPEED).getValue();

                rotationYawHead = entity.rotationYawHead;
                rotationPitch = entity.rotationPitch * 0.5f;

                double vertical = entity.getLookVec().y;
                if (entity.moveForward != 0 && (eyesInWater || vertical < -0.25)) moveY = vertical * speed * 18;

                setAIMoveSpeed(speed);
                vec3d = new Vec3d(moveX, moveY, moveZ);
            }

            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));
            if (vec3d.z == 0 && getAttackTarget() == null && !isSitting())
                setMotion(getMotion().add(0, -0.004d, 0));
        }
        else super.travel(vec3d);
    }

    @Override
    protected float getTravelSpeed()
    {
        //@formatter:off
        return isInWater()? (float) getAttribute(SWIM_SPEED).getValue()
                          : (float) getAttribute(MOVEMENT_SPEED).getValue() * 0.225f;
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
        if (slot == CONDUIT_SLOT && !onLoad)
        {
            boolean flag = stack.getItem() == Items.CONDUIT;
            boolean hadConduit = hasConduit();
            dataManager.set(HAS_CONDUIT, flag);
            if (flag && !hadConduit) setAnimation(CONDUIT_ANIMATION);
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
        super.addScreenInfo(screen);
        screen.addAction(StaffAction.INVENTORY);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);
        container.addSlot(new SlotBuilder(getInvHandler(), CONDUIT_SLOT).only(Items.CONDUIT).limit(1));
    }

    @Override
    protected PathNavigator createNavigator(World world) { return new Navigator(); }

    public boolean hasConduit() { return dataManager.get(HAS_CONDUIT); }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 1); }

    @Override
    public boolean canBreatheUnderwater() { return true; }

    public CreatureAttribute getCreatureAttribute() { return CreatureAttribute.WATER; }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize size) { return size.height * (isInWater()? 1f : 0.6f); }

    @Override
    protected boolean canBeRidden(Entity entityIn) { return isTamed() && !isChild(); }

    @Override
    public int getHorizontalFaceSpeed() { return 6; }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(2); }

    @Override
    public boolean canFly() { return false; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }

    @Override
    public Animation[] getAnimations() { return new Animation[] {ROAR_ANIMATION, CONDUIT_ANIMATION}; }

    @Override
    public void setAnimation(Animation animation)
    {
        super.setAnimation(animation);
        if (animation == CONDUIT_ANIMATION) playSound(WRSounds.ENTITY_BFLY_ROAR.get(), 3f, 1f, true);
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
}
