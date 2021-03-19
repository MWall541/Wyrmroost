package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.items.CoinDragonItem;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.entity.ai.attributes.Attributes.*;

/**
 * Simple Entity really, just bob and down in the same spot, and land to sleep at night. Easy.
 */
public class CoinDragonEntity extends MobEntity
{
    public static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(CoinDragonEntity.class, DataSerializers.INT);
    public static String DATA_VARIANT = "Variant";

    public CoinDragonEntity(EntityType<? extends CoinDragonEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new LookAtGoal(this, PlayerEntity.class, 4));
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(VARIANT, getRandom().nextInt(5));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound)
    {
        super.addAdditionalSaveData(compound);
        compound.putInt(DATA_VARIANT, getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound)
    {
        super.readAdditionalSaveData(compound);
        setVariant(compound.getInt(DATA_VARIANT));
    }

    public int getVariant()
    {
        return entityData.get(VARIANT);
    }

    public void setVariant(int variant)
    {
        entityData.set(VARIANT, variant);
    }

    // move up if too low, move down if too high, else, just bob up and down
    @Override
    public void travel(Vector3d positionIn)
    {
        if (isNoAi()) return;
        double moveSpeed = 0.02;
        double yMot;
        double altitiude = getAltitude();
        if (altitiude < 1.5) yMot = moveSpeed;
        else if (altitiude > 3) yMot = -moveSpeed;
        else yMot = Math.sin(tickCount * 0.1) * 0.0035;

        setDeltaMovement(getDeltaMovement().add(0, yMot, 0));
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(0.91));
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        ActionResultType stackResult = player.getItemInHand(hand).interactLivingEntity(player, this, hand);
        if (stackResult.consumesAction()) return stackResult;

        ItemEntity itemEntity = new ItemEntity(level, getX(), getY(), getZ(), getItemStack());
        double x = player.getX() - getX();
        double y = player.getY() - getY();
        double z = player.getZ() - getZ();
        itemEntity.setDeltaMovement(x * 0.1, y * 0.1 + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08, z * 0.1);
        level.addFreshEntity(itemEntity);
        remove();
        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size)
    {
        return size.height * 0.8645f;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(WRItems.COIN_DRAGON.get());
    }

    @Override
    public boolean requiresCustomPersistence()
    {
        return true;
    }

    @Override
    public boolean isSuppressingSlidingDownLadder()
    {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier)
    {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos)
    {
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_COINDRAGON_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_COINDRAGON_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_COINDRAGON_IDLE.get();
    }

    public double getAltitude()
    {
        BlockPos.Mutable pos = blockPosition().mutable().move(0, -1, 0);
        while (pos.getY() > 0 && !level.getBlockState(pos).canOcclude()) pos.setY(pos.getY() - 1);
        return getY() - pos.getY();
    }

    public ItemStack getItemStack()
    {
        ItemStack stack = new ItemStack(WRItems.COIN_DRAGON.get());
        stack.getOrCreateTag().put(CoinDragonItem.DATA_ENTITY, serializeNBT());
        if (hasCustomName()) stack.setHoverName(getCustomName());
        return stack;
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 4)
                .add(MOVEMENT_SPEED, 0.02);
    }
}