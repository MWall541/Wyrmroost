package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai.ButterFlyMoveController;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai.ButterflyNavigator;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.CommonEntityGoals;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.DragonFollowOwnerGoal;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.MoveTowardsHomePointGoal;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import WolfShotz.Wyrmroost.util.entityutils.multipart.IMultiPartEntity;
import WolfShotz.Wyrmroost.util.entityutils.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.util.io.ContainerBase;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

public class ButterflyLeviathanEntity extends AbstractDragonEntity implements IMultiPartEntity
{
    public static final DataParameter<Boolean> HAS_CONDUIT = EntityDataManager.createKey(ButterflyLeviathanEntity.class, DataSerializers.BOOLEAN);

    // Multipart
    public MultiPartEntity headPart;
    public MultiPartEntity wingLeftPart;
    public MultiPartEntity wingRightPart;
    public MultiPartEntity tail1Part;
    public MultiPartEntity tail2Part;
    public MultiPartEntity tail3Part;

    public RandomWalkingGoal moveGoal;

    public ButterflyLeviathanEntity(EntityType<? extends ButterflyLeviathanEntity> blevi, World world)
    {
        super(blevi, world);

        moveController = new ButterFlyMoveController(this);

        if (!world.isRemote)
        {
            headPart = createPart(this, 4.2f, 0, 0.75f, 2.25f, 1.75f);
            wingLeftPart = createPart(this, 5f, -90, 0.35f, 2.25f, 3.15f);
            wingRightPart = createPart(this, 5f, 90, 0.35f, 2.25f, 3.15f);
            tail1Part = createPart(this, 4.5f, 180, 0.35f, 2.25f, 2.25f, 0.85f);
            tail2Part = createPart(this, 8f, 180, 0.35f, 2.25f, 2.25f, 0.75f);
            tail3Part = createPart(this, 12f, 180, 0.5f, 2f, 2f, 0.5f);
        }

        setImmune(new DamageSource("brine_water"));
    }
    
    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(2, new DragonFollowOwnerGoal(this, 1, 20d, 3d));
        goalSelector.addGoal(3, new MoveTowardsHomePointGoal(this, 1));
        goalSelector.addGoal(4, moveGoal = new RandomSwimmingGoal(this, 1d, 10));
        goalSelector.addGoal(5, CommonEntityGoals.lookAt(this, 10f));
        goalSelector.addGoal(6, CommonEntityGoals.lookRandomly(this));
    }
    
    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(70d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.1); // On land speed, in water speed is handled in the move controller
//        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(10);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) { return new ButterflyNavigator(this, worldIn); }

    @Override
    public CreatureAttribute getCreatureAttribute() { return CreatureAttribute.WATER; }
    
    // ================================
    //           Entity NBT
    // ================================
    
    @Override
    protected void registerData()
    {
        super.registerData();

        dataManager.register(VARIANT, rand.nextInt(2));
        dataManager.register(HAS_CONDUIT, false);
    }
    
    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
        
        nbt.putInt("variant", getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        setVariant(nbt.getInt("variant"));
        setHasConduit(invHandler.map(i -> i.getStackInSlot(0).getItem() == Items.CONDUIT).orElse(false));
    }

    public void setHasConduit(boolean flag)
    {
        dataManager.set(HAS_CONDUIT, flag);
        if (flag) playSound(SoundEvents.BLOCK_CONDUIT_ACTIVATE, 1, 1);
        else playSound(SoundEvents.BLOCK_CONDUIT_DEACTIVATE, 1, 1);
    }

    public boolean hasConduit() { return dataManager.get(HAS_CONDUIT); }

    // =================================

    @Override
    public void tick()
    {
        super.tick();
        tickParts();

        recalculateSize();

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
    }
    
    @Override
    protected void updateAITasks()
    {
        super.updateAITasks();

        if (isInWater()) moveGoal.setExecutionChance(10);
        else moveGoal.setExecutionChance(120);
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        float f1 = (float) (getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
        float speed = isInWater() ? f1 * 2 : f1;
        if (canPassengerSteer() && canBeSteered())
        {
            LivingEntity rider = (LivingEntity) getControllingPassenger();

            prevRotationYaw = rotationYaw = rider.rotationYaw;
            rotationPitch = rider.rotationPitch * 0.5f;
            setRotation(rotationYaw, rotationPitch);
            renderYawOffset = rotationYaw;
            rotationYawHead = renderYawOffset;
            if (isInWater() && (rider.moveForward != 0 || rider.moveStrafing != 0))
            {
                float f4 = MathHelper.sin(rotationPitch * (QuikMaths.PI / 180f));
                setMotion(getMotion().x, -f4 * 2, getMotion().z);
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
        
        if (isTamed())
        {
            player.startRiding(this);
            return true;
        }
        
        return false;
    }
    
    public void applyEffects()
    {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(18d).expand(0, world.getHeight(), 0);
        List<PlayerEntity> list = world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb);
        if (list.isEmpty()) return;
        for (PlayerEntity player : list)
            if (player.isWet() && getPosition().withinDistance(new BlockPos(player), 18d))
                player.addPotionEffect(new EffectInstance(Effects.CONDUIT_POWER, 260, 0, true, true));
    }
    
    private void spawnConduitParticles()
    {
        if (rand.nextInt(35) != 0) return;
        for (int i = 0; i < 16; ++i)
        {
            double motionX = QuikMaths.nextPseudoDouble(rand) * 1.5f;
            double motionY = QuikMaths.nextPseudoDouble(rand);
            double motionZ = QuikMaths.nextPseudoDouble(rand) * 1.5f;
//            world.addParticle(ParticleTypes.NAUTILUS, headPart.posX, headPart.posY + 4, headPart.posZ, motionX, motionY, motionZ);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        if (isUnderWater()) return 2f;
        return 3.1f;
    }

    @Override
    public MultiPartEntity[] getParts()
    {
        return new MultiPartEntity[]{headPart, wingLeftPart, wingRightPart, tail1Part, tail2Part, tail3Part};
    }

    @Override
    public void setMountCameraAngles(boolean backView)
    {
        if (backView) GlStateManager.translated(0, -0.5d, -4d);
        else GlStateManager.translated(0, 0, -7d);
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
    public List<Item> getFoodItems() { return Lists.newArrayList(Items.SEAGRASS, Items.KELP); }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerBase.ButterflyContainer(this, playerInv, windowID);
    }

    @Override
    public LazyOptional<ItemStackHandler> createInv()
    {
        return LazyOptional.of(() -> new ItemStackHandler(1));
    }
    
    @Override
    public DragonEggProperties createEggProperties()
    {
        return new DragonEggProperties(1.5f, 2.5f, 40000).setConditions(Entity::isInWater);
    }
    
    @Override
    public Animation[] getAnimations()
    {
        return new Animation[]{NO_ANIMATION};
    }
}
