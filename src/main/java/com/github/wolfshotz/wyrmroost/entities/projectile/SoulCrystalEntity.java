package com.github.wolfshotz.wyrmroost.entities.projectile;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
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
import net.minecraft.util.*;
import net.minecraft.util.math.*;
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
        if (!(thrower instanceof PlayerEntity) || !releaseDragon(level, (PlayerEntity) thrower, null, stack, new BlockPos(result.getLocation()), thrower.getDirection()).consumesAction())
            super.onHit(result);
        else if (stack.getDamageValue() >= stack.getMaxDamage())
            level.broadcastEntityEvent(this, BREAK_EVENT);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result)
    {
        super.onHitEntity(result);
        Entity thrower = getOwner();
        ItemStack stack = getItem();
        if (!(thrower instanceof PlayerEntity) || !captureDragon((PlayerEntity) thrower, level, stack, result.getEntity(), null).consumesAction())
            restoreStack(level, (LivingEntity) thrower, null, result.getLocation(), stack);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result)
    {
        super.onHitBlock(result);
        restoreStack(level, null, null, result.getLocation(), getItem());
    }

    @Override
    public void handleEntityEvent(byte event)
    {
        super.handleEntityEvent(event);
        if (event == BREAK_EVENT)
        {
            ModUtils.playLocalSound(level, blockPosition(), SoundEvents.GLASS_BREAK, 0.25f, 1.75f);
            ItemParticleData data = new ItemParticleData(ParticleTypes.ITEM, getItem());
            for (int i = 0; i < 8; ++i)
                level.addParticle(data, getX(), getY(), getZ(), Mafs.nextDouble(random) * 0.25, random.nextDouble() * 0.15, Mafs.nextDouble(random) * 0.25);
        }
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
        if (entity instanceof TameableEntity)
        {
            ResourceLocation rl = entity.getType().getRegistryName();
            switch (rl.getNamespace())
            {
                case "wyrmroost":
                case "dragonmounts":
                case "wings":
                    return true;
                case "iceandfire":
                    String path = rl.getPath();
                    return path.contains("dragon");
                default:
                    break;
            }
        }
        return false;
    }

    public static ActionResultType captureDragon(@Nullable PlayerEntity player, World level, ItemStack stack, Entity target, @Nullable Hand hand)
    {
        if (containsDragon(stack)) return ActionResultType.PASS;
        if (!isSuitableEntity(target)) return ActionResultType.FAIL;
        TameableEntity dragon = (TameableEntity) target;
        if (dragon.getOwner() != player && player != null)
        {
            player.displayClientMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.not_owner").withStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        if (!dragon.getPassengers().isEmpty()) dragon.ejectPassengers();
        if (!level.isClientSide)
        {
            CompoundNBT tag = stack.getOrCreateTag();
            CompoundNBT dragonTag = dragon.serializeNBT();
            if (player != null) dragonTag.putString("OwnerName", player.getName().getString());
            tag.put(DATA_DRAGON, dragonTag); // Serializing the dragons data, including its id.
            stack.setTag(tag);
            dragon.restrictTo(BlockPos.ZERO, -1);
            dragon.remove();
            level.playSound(null, dragon.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1, 1);

            restoreStack(level, player, hand, dragon.position(), stack);
        }
        else // Client side Aesthetics
        {
            double width = dragon.getBbWidth();
            for (int i = 0; i <= Math.floor(width) * 25; ++i)
            {
                double calcX = MathHelper.cos(i + 360 / Mafs.PI * 360f) * (width * 1.5);
                double calcZ = MathHelper.sin(i + 360 / Mafs.PI * 360f) * (width * 1.5);
                double x = dragon.getX() + calcX;
                double y = dragon.getY() + (dragon.getBbHeight() * 1.8);
                double z = dragon.getZ() + calcZ;
                double xMot = -calcX / 5f;
                double yMot = -(dragon.getBbHeight() / 8);
                double zMot = -calcZ / 5f;

                level.addParticle(ParticleTypes.END_ROD, x, y, z, xMot, yMot, zMot);
            }
        }
        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    public static ActionResultType releaseDragon(World level, PlayerEntity player, Hand hand, ItemStack stack, BlockPos pos, Direction direction)
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
        if (!tag.getUUID("Owner").equals(player.getUUID()))
        {
            player.displayClientMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.not_owner").withStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        EntitySize size = dragon.getDimensions(dragon.getPose());
        if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())
            pos = pos.relative(direction);

        // check area for collision to ensure the area is safe.
        dragon.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        AxisAlignedBB aabb = dragon.getBoundingBox();
        if (!level.noCollision(dragon, new AxisAlignedBB(aabb.minX, dragon.getEyeY() - 0.35, aabb.minZ, aabb.maxX, dragon.getEyeY() + 0.35, aabb.maxZ)))
        {
            player.displayClientMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.fail").withStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

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
            stack.hurtAndBreak(MathHelper.clamp((int) ((dragon.getBbWidth() + dragon.getBbHeight()) * 0.65), 1, 10), (ServerPlayerEntity) player, p ->
            {
                if (hand != null) p.broadcastBreakEvent(hand);
            });
            level.addFreshEntity(dragon);
            level.playSound(null, dragon.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.AMBIENT, 1, 1);
            restoreStack(level, player, hand, Vector3d.atCenterOf(pos), stack);
        }
        else // Client Side Aesthetics
        {
            double posX = pos.getX() + 0.5d;
            double posY = pos.getY() + (size.height / 2);
            double posZ = pos.getZ() + 0.5d;
            for (int i = 0; i < dragon.getBbWidth() * 25; ++i)
            {
                double x = MathHelper.cos(i + 360 / Mafs.PI * 360f) * (dragon.getBbWidth() * 1.5d);
                double z = MathHelper.sin(i + 360 / Mafs.PI * 360f) * (dragon.getBbWidth() * 1.5d);
                double xMot = x / 10f;
                double yMot = dragon.getBbHeight() / 18f;
                double zMot = z / 10f;

                level.addParticle(ParticleTypes.END_ROD, posX, posY, posZ, xMot, yMot, zMot);
                level.addParticle(ParticleTypes.CLOUD, posX, posY + (i * 0.25), posZ, 0, 0, 0);
            }
        }

        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    public static void restoreStack(World level, @Nullable LivingEntity player, @Nullable Hand hand, Vector3d origin, ItemStack stack)
    {
        if (player == null)
            InventoryHelper.dropItemStack(level, origin.x(), origin.y(), origin.z(), stack);
        else if (hand == null)
        {
            ItemEntity entity = new ItemEntity(level, origin.x(), origin.y(), origin.z(), stack);
            double x = player.getX() - origin.x();
            double y = player.getY() - origin.y() + 0.675;
            double z = player.getZ() - origin.z();
            entity.setDeltaMovement(x * 0.1D, y * 0.1 + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08, z * 0.1);
            level.addFreshEntity(entity);
        }
        else
        {
            if (player instanceof PlayerEntity && player.getItemInHand(hand).getCount() != 1)
                ((PlayerEntity) player).inventory.add(stack);
        }
    }
}
