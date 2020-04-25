package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.content.fluids.CausticWaterFluid;
import WolfShotz.Wyrmroost.util.QuikMaths;
import WolfShotz.Wyrmroost.util.entityutils.PlayerMount;
import WolfShotz.Wyrmroost.util.entityutils.ai.FlyerMoveController;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.CommonEntityGoals;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.FlyerFollowOwnerGoal;
import WolfShotz.Wyrmroost.util.entityutils.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class CanariWyvernEntity extends AbstractDragonEntity implements PlayerMount.IShoulderMount
{
    public static final Animation FLAP_WINGS_ANIMATION = new Animation(22);
    public static final Animation PREEN_ANIMATION = new Animation(36);
    public static final Animation THREAT_ANIMATION = new Animation(40);

    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        setImmune(CausticWaterFluid.CAUSTIC_WATER);
        shouldFlyThreshold = 2;

        moveController = new FlyerMoveController(this, true);
        lookController = new LookController(this);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(3, new FlyerFollowOwnerGoal(this, 7, 1, 4, true));
        goalSelector.addGoal(4, new FlyerWanderGoal(this, true));
        goalSelector.addGoal(5, CommonEntityGoals.lookAt(this, 5));
        goalSelector.addGoal(6, new LookRandomlyGoal(this));
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(16d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.2d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(5.5d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.3);
    }

    @Override
    protected BodyController createBodyController() { return new BodyController(this); }

    // ================================
    //           Entity NBT
    // ================================

    @Override
    protected void registerData()
    {
        super.registerData();

        dataManager.register(VARIANT, getRNG().nextInt(5));
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
    }

    @Override
    public int getSpecialChances() { return 0; }

    // ================================

    @Override
    public void livingTick()
    {
        super.livingTick();

        if (!isSleeping() && !isFlying() && getRidingEntity() == null && noActiveAnimation())
        {
            if (getRNG().nextInt(650) == 0) setAnimation(FLAP_WINGS_ANIMATION);
            else if (getRNG().nextInt(350) == 0) setAnimation(PREEN_ANIMATION);
        }

        if (getAnimation() == FLAP_WINGS_ANIMATION)
        {
            if (animationTick == 5 || animationTick == 12) playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 0.7f, 2, true);
            if (animationTick == 9 && getRNG().nextInt(25) == 0)
                entityDropItem(new ItemStack(Items.FEATHER), 0.5f);
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.processInteract(player, hand, stack)) return true;

        if (isOwner(player))
        {
            if (player.isSneaking())
            {
                setSit(!isSitting());

                return true;
            }

            if (PlayerMount.getShoulderEntityCount(player) < 2)
            {
                setSit(true);
                startRiding(player, true);

                return true;
            }
        }
        return false;
    }

    @Override
    public void travel(Vec3d vec3d)
    {
        if (!isFlying()) // Flying is controlled entirely in the move helper
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

        if (!entity.isAlive())
        {
            stopRiding();
            return;
        }
        
        if (!(entity instanceof PlayerEntity)) return;
        
        PlayerEntity player = (PlayerEntity) entity;

        if ((player.isSneaking() && !player.abilities.isFlying) || player.getSubmergedHeight() > 1.25 || player.isElytraFlying())
        {
            stopRiding();
            return;
        }
        
        rotationYaw = player.rotationYawHead;
        rotationPitch = player.rotationPitch;
        setRotation(rotationYaw, rotationPitch);
        rotationYawHead = player.rotationYawHead;
        prevRotationYaw = player.rotationYawHead;

        double xOffset = checkShoulderOccupants(player)? -0.35f : 0.35f;

        Vec3d vec3d1 = QuikMaths.calculateYawAngle(player.renderYawOffset, xOffset, 0.1).add(player.posX, 0, player.posZ);
        setPosition(vec3d1.x, player.posY + 1.4, vec3d1.z);
    }

    @Override
    public boolean canBeCollidedWith() { return super.canBeCollidedWith() && !isRiding(); }

    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || getRidingEntity() != null; }

    /**
     * Array Containing all of the dragons food items
     */
    @Override
    public List<Item> getFoodItems() { return Lists.newArrayList(Items.SWEET_BERRIES); }

    @Override
    public DragonEggProperties createEggProperties()
    {
        return new DragonEggProperties(0.25f, 0.35f, 6000)
                .setCustomTexture(Wyrmroost.rl("textures/entity/dragon/canari/egg.png"));
//                       .setConditions(c -> c.world.getBlockState(c.getPosition().down()).getBlock() == WRBlocks.CANARI_LEAVES.get());
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, SLEEP_ANIMATION, WAKE_ANIMATION, FLAP_WINGS_ANIMATION, PREEN_ANIMATION, THREAT_ANIMATION};
    }
}
