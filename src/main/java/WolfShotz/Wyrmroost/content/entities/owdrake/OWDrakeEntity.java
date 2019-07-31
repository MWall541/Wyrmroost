package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.ai.GrazeGoal;
import WolfShotz.Wyrmroost.setup.ItemSetup;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SaddleItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static net.minecraft.entity.SharedMonsterAttributes.*;

/**
 * Created by WolfShotz 7/10/19 - 22:18
 */
public class OWDrakeEntity extends AbstractDragonEntity
{
    public static Animation GRAZE_ANIMATION = Animation.create(35);
    public static Animation HORN_ATTACK_ANIMATION = Animation.create(22);

    private static final DataParameter<Boolean> VARIANT = EntityDataManager.createKey(OWDrakeEntity.class, DataSerializers.BOOLEAN);

    public OWDrakeEntity(EntityType<? extends OWDrakeEntity> drake, World world) {
        super(drake, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1d, true));
        goalSelector.addGoal(10, new GrazeGoal(this, 2));
        goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 1d));
        goalSelector.addGoal(12, new LookRandomlyGoal(this));
        goalSelector.addGoal(12, new LookAtGoal(this, LivingEntity.class, 10f));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new NonTamedTargetGoal<>(this, PlayerEntity.class, true, player -> !player.isInvisible() || !((PlayerEntity) player).abilities.isCreativeMode));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(MAX_HEALTH).setBaseValue(50.0d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.20989d);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(6.0d);
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(VARIANT, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("variant", getVariant());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setVariant(compound.getBoolean("variant"));
    }

    /** The Variant of the drake. false == Common, true == Savanna. Boolean since we only have 2 different variants */
    protected boolean getVariant() { return dataManager.get(VARIANT); }
    protected void setVariant(boolean variant) { dataManager.set(VARIANT, variant); }

    @Override
    public boolean canFly() { return false; }

    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 50; }

    // ================================

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        Biome biome = worldIn.getBiome(new BlockPos(this));
        Set<Biome> biomes = BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA);
        if (biomes.contains(biome)) setVariant(true);

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void livingTick() {
        setSprinting(isAngry());

        super.livingTick();
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() instanceof SaddleItem && !isSaddled()) { // instaceof: for custom saddles (if any)
            consumeItemFromStack(player, stack);
            setSaddled(true);
            playSound(SoundEvents.ENTITY_HORSE_SADDLE, 1f, 1f);
            return true;
        }

        if (isSaddled() && !isFoodItem(stack) && !player.isSneaking() && !world.isRemote) {
            player.startRiding(this);
            setSitting(false);
            return true;
        }

        if (isTamed()) {
            if (getHealth() < getMaxHealth() && isFoodItem(stack) && !player.isSneaking()) {
                consumeItemFromStack(player, stack);
                heal(2f);
                return true;
            }

            if (!isFoodItem(stack) && player.isSneaking() && isOwner(player) && !world.isRemote && sitGoal != null) {
                sitGoal.setSitting(!isSitting());
                isJumping = false;
                navigator.clearPath();
                setAttackTarget(null);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (!isTamed() && passenger instanceof LivingEntity && !world.isRemote) {
            int rand = new Random().nextInt(100);

            if (passenger instanceof PlayerEntity && rand == 0) {
                setTamedBy((PlayerEntity) passenger);
                navigator.clearPath();
                setAttackTarget(null);
                setHealth(getMaxHealth());
                playTameEffect(true);
                world.setEntityState(this, (byte) 7);
            } else
            if (rand % 15 == 0) {
                removePassengers();
                playTameEffect(false);
                world.setEntityState(this, (byte) 6);
            }
        }
        super.updatePassenger(passenger);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (getAnimation() != HORN_ATTACK_ANIMATION) setAnimation(HORN_ATTACK_ANIMATION);

        return super.attackEntityAsMob(entityIn);
    }

    @Override
    public void eatGrassBonus() {
        if (isChild()) addGrowth(60);
        if (getHealth() < getMaxHealth()) heal(4f);
    }

    @Override
    public void setAttackTarget(@Nullable LivingEntity entityIn) {
        if (entityIn != null || !isTamed()) setAngry(true);
        else setAngry(false);

        super.setAttackTarget(entityIn);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 1);

        super.playStepSound(pos, blockIn);
    }

    /** Array Containing all of the dragons food items */
    @Override
    public Item[] getFoodItems() { return new Item[] {Items.WHEAT, ItemSetup.itemfood_dragonfruit}; } //TODO

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) { return null; }

    // == Entity Animation ==
    @Override
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, GRAZE_ANIMATION, HORN_ATTACK_ANIMATION}; }
    // ==

}
