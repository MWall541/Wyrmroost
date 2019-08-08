package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.content.entities.sliverglider.goals.NonTamedTemptGoal;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class SilverGliderEntity extends AbstractDragonEntity
{
    public boolean isGliding = false;

    // Entity Animations
    public static final Animation RANDOM_FLAP_ANIMATION = Animation.create(30);

    // Dragon Entity Data
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SilverGliderEntity.class, DataSerializers.VARINT);

    public SilverGliderEntity(EntityType<? extends SilverGliderEntity> entity, World world) {
        super(entity, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(30d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.32d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(4.0d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(1.2d);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        goalSelector.addGoal(4, new NonTamedTemptGoal(this, 0.6d, true, Ingredient.fromItems(getFoodItems())));
        goalSelector.addGoal(6, new DragonBreedGoal(this, 18000));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1d));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();

        dataManager.register(VARIANT, getRNG().nextInt(3)); // For females, this value is redundant.
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putInt("variant", getVariant());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        setVariant(compound.getInt("variant"));
    }

    /**
     * Gets the variant of the Silver Glider.
     */
    public int getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }

    /**
     * Set The chances this dragon can be an albino. Set it to 0 to have no chance
     */
    @Override
    public int getAlbinoChances() { return 0; }

    // ================================


    @Override
    public void livingTick() {
        super.livingTick();

        if (isGliding && getRidingEntity() == null && onGround) isGliding = false;
    }

    @Override
    public void updateRidden() {
        super.updateRidden();

        Entity entity = getRidingEntity();

        isGliding = false;

        if (entity != null) {
            if (!entity.isAlive()) {
                stopRiding();
                return;
            }

            setMotion(Vec3d.ZERO);

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                if (player.isSneaking() && !player.abilities.isFlying) {
                    stopRiding();
                    return;
                }

                if (ModUtils.isEntityJumping(player) && ModUtils.getAltitude(player) > 1.3 && player.getRidingEntity() == null && !player.abilities.isFlying) {
                    Vec3d prevMotion = player.getMotion();
                    double yVec = player.getLookVec().y;
                    double xMotion = (Math.abs(prevMotion.x) >= 1f? 0.8d : 1.1d);
                    double zMotion = (Math.abs(prevMotion.z) >= 1f? 0.8d : 1.1d);
                    double yMotion = 0.75d; // Fallback: WHERE TF ARE WE LOOKING?!

                    if (yVec < 0) yMotion = Math.max(Math.abs(yVec), 0.6);
                    else if (yVec >= 0) {
                        yMotion = Math.min(yVec, 0.75);
                        if (getRNG().nextInt(75) == 0 && getAnimation() != RANDOM_FLAP_ANIMATION)
                            setAnimation(RANDOM_FLAP_ANIMATION);
                    }

                    Vec3d motion = new Vec3d(xMotion, yMotion, zMotion);

                    isGliding = true;
                    player.setMotion(prevMotion.mul(motion));
                }

                prevRotationPitch = rotationPitch = player.rotationPitch / 2;
                rotationYawHead = renderYawOffset = prevRotationYaw = rotationYaw = player.rotationYaw;
                setRotation(player.rotationYawHead, rotationPitch);

                setPosition(player.posX, player.posY + 1.85d, player.posZ);
            }
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        // If holding this dragons favorite food, and not tamed, then tame it!
        if (!isTamed() && isBreedingItem(stack)) {
            tame(getRNG().nextInt(10) == 0, player);
            return true;
        }

        // if tamed, then start riding the player
        if (isTamed() && stack.isEmpty() && hand == Hand.MAIN_HAND) {
            startRiding(player, true);
            return true;
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void dismountEntity(Entity entityIn) {
        super.dismountEntity(entityIn);

        isGliding = false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || getRidingEntity() != null; }

    /** Array Containing all of the dragons food items */
    @Override
    protected Item[] getFoodItems() { return new Item[] {Items.TROPICAL_FISH, Items.COD, Items.SALMON, Items.COOKED_COD, Items.COOKED_SALMON}; }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) { return null; }

    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
    // ==
}