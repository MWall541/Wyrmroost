package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class SoulCrystalItem extends Item
{
    public static final String DATA_DRAGON = "DragonData";

    public SoulCrystalItem()
    {
        super(WRItems.builder().stacksTo(1));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        World level = player.level;
        if (containsDragon(stack)) return ActionResultType.PASS;
        if (!isSuitableEntity(target)) return ActionResultType.PASS;
        TameableEntity dragon = (TameableEntity) target;
        if (dragon.getOwner() != player)
        {
            player.displayClientMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.not_owner").withStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        if (!dragon.getPassengers().isEmpty()) dragon.ejectPassengers();
        if (!level.isClientSide)
        {
            CompoundNBT tag = stack.getOrCreateTag();
            CompoundNBT dragonTag = dragon.serializeNBT();
            dragonTag.putString("OwnerName", player.getName().getString());
            tag.put(DATA_DRAGON, dragonTag); // Serializing the dragons data, including its id.
            stack.setTag(tag);
            dragon.remove();
            player.setItemInHand(hand, stack);
            level.playSound(null, player.blockPosition(), SoundEvents.END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1, 1);
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

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        return releaseDragon(context.getLevel(), context.getPlayer(), context.getItemInHand(), context.getClickedPos(), context.getHorizontalDirection());
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
    {
        BlockRayTraceResult rt = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        ItemStack stack = player.getItemInHand(hand);
        if (rt.getType() == RayTraceResult.Type.MISS) return ActionResult.pass(stack);
        BlockPos pos = rt.getBlockPos();
        if (!(level.getBlockState(pos).getBlock() instanceof FlowingFluidBlock)) return ActionResult.pass(stack);
        return new ActionResult<>(releaseDragon(level, player, stack, pos, rt.getDirection()), stack);
    }

    @Override
    public ITextComponent getName(ItemStack stack)
    {
        TranslationTextComponent name = (TranslationTextComponent) super.getName(stack);
        if (containsDragon(stack)) name.withStyle(TextFormatting.AQUA, TextFormatting.ITALIC);
        return name;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (containsDragon(stack))
        {
            CompoundNBT tag = stack.getTag().getCompound(DATA_DRAGON);
            ITextComponent name;

            if (tag.contains("CustomName"))
                name = ITextComponent.Serializer.fromJson(tag.getString("CustomName"));
            else name = EntityType.byString(tag.getString("id")).orElse(null).getDescription();

            tooltip.add(name.copy().withStyle(TextFormatting.BOLD));
            tooltip.add(new StringTextComponent("Tamed by ").append(new StringTextComponent(tag.getString("OwnerName")).withStyle(TextFormatting.ITALIC)));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return containsDragon(stack);
    }

    private static boolean containsDragon(ItemStack stack)
    {
        return stack.hasTag() && stack.getTag().contains(DATA_DRAGON);
    }

    private static boolean isSuitableEntity(LivingEntity entity)
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

    private static ActionResultType releaseDragon(World level, PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction)
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
            pos = pos.relative(direction, (int) (direction.getAxis().isHorizontal()? size.width : 1));

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
            level.addFreshEntity(dragon);
            level.playSound(null, dragon.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.AMBIENT, 1, 1);
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

        return ActionResultType.SUCCESS;
    }
}
