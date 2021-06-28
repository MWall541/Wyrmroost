package com.github.wolfshotz.wyrmroost.entities.projectile;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.registry.WREffects;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class SoulCrystalEntity extends ProjectileItemEntity
{
    public static final String DATA_DRAGON = "DragonData";
    public static final byte BREAK_EVENT = 1;

    public SoulCrystalEntity(EntityType<? extends ProjectileItemEntity> type, World world)
    {
        super(type, world);
    }

    public SoulCrystalEntity(ItemStack stack, LivingEntity thrower, World world)
    {
        super(WREntities.SOUL_CRYSTAL.get(), thrower, world);
        setItem(stack);
    }

    @Override
    protected Item getDefaultItem()
    {
        return WRItems.SOUL_CRYSTAL.get();
    }

    @Override
    protected void onHit(RayTraceResult result)
    {
        remove();
        Entity thrower = getOwner();
        ItemStack stack = getItem();

        if (!(thrower instanceof PlayerEntity) || !releaseDragon(level, (PlayerEntity) thrower, stack, new BlockPos(result.getLocation()), thrower.getDirection()).consumesAction())
            super.onHit(result);

        if (stack.getDamageValue() >= stack.getMaxDamage()) level.broadcastEntityEvent(this, BREAK_EVENT);
        else restoreStack(level, (LivingEntity) thrower, result.getLocation(), stack);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result)
    {
        super.onHitEntity(result);
        Entity thrower = getOwner();
        ItemStack stack = getItem();
        if (thrower instanceof PlayerEntity) captureDragon((PlayerEntity) thrower, level, stack, result.getEntity());
    }

    @Override
    public void handleEntityEvent(byte event)
    {

        if (event == BREAK_EVENT)
        {
            ModUtils.playLocalSound(level, blockPosition(), SoundEvents.GLASS_BREAK, 1f, 1.75f);
            ItemParticleData data = new ItemParticleData(ParticleTypes.ITEM, getItem());
            for (int i = 0; i < 8; ++i)
                level.addParticle(data, getX(), getY(), getZ(), Mafs.nextDouble(random) * 0.25, random.nextDouble() * 0.15, Mafs.nextDouble(random) * 0.25);
        }
        else super.handleEntityEvent(event);
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static boolean containsDragon(ItemStack stack)
    {
        return stack.hasTag() && stack.getTag().contains(DATA_DRAGON);
    }

    public static boolean isSuitableEntity(Entity entity)
    {
        return entity instanceof TameableEntity && entity.getType().is(WREntities.Tags.SOUL_BEARERS);
    }

    public static ActionResultType captureDragon(@Nullable PlayerEntity player, World level, ItemStack stack, Entity target)
    {
        if (containsDragon(stack)) return ActionResultType.PASS;
        if (!isSuitableEntity(target)) return fail(player, "not_suitable");
        TameableEntity dragon = (TameableEntity) target;
        if (dragon.getOwner() != player) return fail(player, "not_owner");
        if (level.isClientSide) return ActionResultType.CONSUME;
        if (dragon.hasEffect(WREffects.SOUL_WEAKNESS.get())) return fail(player, "weak");

        if (!dragon.getPassengers().isEmpty()) dragon.ejectPassengers();
        if (dragon instanceof TameableDragonEntity) ((TameableDragonEntity) dragon).dropStorage();

        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT dragonTag = dragon.serializeNBT();
        if (player != null) dragonTag.putString("OwnerName", player.getName().getString());
        tag.put(DATA_DRAGON, dragonTag); // Serializing the dragons data, including its id.
        stack.setTag(tag);
        dragon.restrictTo(BlockPos.ZERO, -1);
        dragon.remove();
        level.playSound(null, dragon.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1, 1);
        return ActionResultType.SUCCESS;
    }

    public static ActionResultType releaseDragon(World level, PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction)
    {
        if (!containsDragon(stack)) return ActionResultType.PASS;

        CompoundNBT tag = stack.getTag().getCompound(DATA_DRAGON);
        EntityType<?> type = EntityType.byString(tag.getString("id")).orElse(null);
        TameableEntity dragon;

        // just in case...
        if (type == null || (dragon = (TameableEntity) type.create(level)) == null)
        {
            Wyrmroost.LOG.error("Something went wrong summoning from a SoulCrystal!");
            return ActionResultType.FAIL;
        }

        // Ensuring the owner is the one summoning
        if (!tag.getUUID("Owner").equals(player.getUUID())) return fail(player, "not_owner");

        EntitySize size = dragon.getDimensions(dragon.getPose());
        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())
            pos = pos.relative(direction);

        // check area for collision to ensure the area is safe.
        dragon.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        AxisAlignedBB aabb = dragon.getBoundingBox();
        if (!level.noCollision(dragon, new AxisAlignedBB(aabb.minX, dragon.getEyeY() - 0.35, aabb.minZ, aabb.maxX, dragon.getEyeY() + 0.35, aabb.maxZ)))
            return fail(player, "fail");

        // Spawn the entity on the server side only
        if (!level.isClientSide)
        {
            // no conflicting id's!
            UUID id = dragon.getUUID();
            dragon.deserializeNBT(tag);
            dragon.setUUID(id);
            dragon.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.yRot, 0f);

            if (stack.hasCustomHoverName()) dragon.setCustomName(stack.getHoverName());
            stack.removeTagKey(DATA_DRAGON);
            level.addFreshEntity(dragon);
            level.playSound(null, dragon.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.AMBIENT, 1, 1);

            float thiccness = dragon.getBbWidth() + dragon.getBbWidth();
            dragon.addEffect(new EffectInstance(WREffects.SOUL_WEAKNESS.get(), (int) thiccness * 200));
            if (!player.abilities.instabuild && thiccness > 5) stack.hurt((int) (thiccness * 0.675f), level.random, (ServerPlayerEntity) player);
        }

        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    public static void restoreStack(World level, @Nullable LivingEntity player, Vector3d origin, ItemStack stack)
    {
        if (player == null)
            InventoryHelper.dropItemStack(level, origin.x(), origin.y(), origin.z(), stack);
        else
        {
            ItemEntity entity = new ItemEntity(level, origin.x(), origin.y(), origin.z(), stack);
            double x = player.getX() - origin.x();
            double y = player.getY() - origin.y() + 0.675;
            double z = player.getZ() - origin.z();
            entity.setDeltaMovement(x * 0.1D, y * 0.1 + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08, z * 0.1);
            level.addFreshEntity(entity);
        }
    }

    private static ActionResultType fail(PlayerEntity player, String message)
    {
        player.displayClientMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal." + message).withStyle(TextFormatting.RED), true);
        return ActionResultType.FAIL;
    }
}
