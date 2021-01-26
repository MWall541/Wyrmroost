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

@SuppressWarnings("ConstantConditions")
public class SoulCrystalItem extends Item
{
    public static final String DATA_DRAGON = "DragonData";

    public SoulCrystalItem()
    {
        super(WRItems.builder().maxStackSize(1));
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
    {
        World world = player.world;
        if (containsDragon(stack)) return ActionResultType.PASS;
        if (!isSuitableEntity(target)) return ActionResultType.PASS;
        TameableEntity dragon = (TameableEntity) target;
        if (dragon.getOwner() != player)
        {
            player.sendStatusMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.not_owner").mergeStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        if (!dragon.getPassengers().isEmpty()) dragon.removePassengers();
        if (!world.isRemote)
        {
            CompoundNBT tag = stack.getOrCreateTag();
            CompoundNBT dragonTag = dragon.serializeNBT();
            dragonTag.putString("OwnerName", player.getName().getUnformattedComponentText());
            tag.put(DATA_DRAGON, dragonTag); // Serializing the dragons data, including its id.
            stack.setTag(tag);
            dragon.remove();
            player.setHeldItem(hand, stack);
            world.playSound(null, player.getPosition(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1, 1);
        }
        else // Client side Aesthetics
        {
            double width = dragon.getWidth();
            for (int i = 0; i <= Math.floor(width) * 25; ++i)
            {
                double calcX = MathHelper.cos(i + 360 / Mafs.PI * 360f) * (width * 1.5);
                double calcZ = MathHelper.sin(i + 360 / Mafs.PI * 360f) * (width * 1.5);
                double x = dragon.getPosX() + calcX;
                double y = dragon.getPosY() + (dragon.getHeight() * 1.8);
                double z = dragon.getPosZ() + calcZ;
                double xMot = -calcX / 5f;
                double yMot = -(dragon.getHeight() / 8);
                double zMot = -calcZ / 5f;

                world.addParticle(ParticleTypes.END_ROD, x, y, z, xMot, yMot, zMot);
            }
        }
        return ActionResultType.func_233537_a_(world.isRemote);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        return releaseDragon(context.getWorld(), context.getPlayer(), context.getItem(), context.getPos(), context.getFace());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        BlockRayTraceResult rt = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        ItemStack stack = player.getHeldItem(hand);
        if (rt.getType() == RayTraceResult.Type.MISS) return ActionResult.resultPass(stack);
        BlockPos pos = rt.getPos();
        if (!(world.getBlockState(pos).getBlock() instanceof FlowingFluidBlock)) return ActionResult.resultPass(stack);
        return new ActionResult<>(releaseDragon(world, player, stack, pos, rt.getFace()), stack);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        TranslationTextComponent name = (TranslationTextComponent) super.getDisplayName(stack);
        if (containsDragon(stack)) name.mergeStyle(TextFormatting.AQUA).mergeStyle(TextFormatting.ITALIC);
        return name;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (containsDragon(stack))
        {
            CompoundNBT tag = stack.getTag().getCompound(DATA_DRAGON);
            ITextComponent name;

            if (tag.contains("CustomName"))
                name = ITextComponent.Serializer.func_240643_a_(tag.getString("CustomName"));
            else name = EntityType.byKey(tag.getString("id")).orElse(null).getName();

            tooltip.add(name.copyRaw().mergeStyle(TextFormatting.BOLD));
            tooltip.add(new StringTextComponent("Tamed by ").append(new StringTextComponent(tag.getString("OwnerName")).mergeStyle(TextFormatting.ITALIC)));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack)
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

    private static ActionResultType releaseDragon(World world, PlayerEntity player, ItemStack stack, BlockPos pos, Direction direction)
    {
        if (!containsDragon(stack)) return ActionResultType.PASS;

        CompoundNBT tag = stack.getTag().getCompound(DATA_DRAGON);
        EntityType<?> type = EntityType.byKey(tag.getString("id")).orElse(null);
        TameableEntity dragon;

        // just in case...
        if (type == null || (dragon = (TameableEntity) type.create(world)) == null)
        {
            Wyrmroost.LOG.error("Something went wrong summoning from a SoulCrystal!");
            return ActionResultType.FAIL;
        }

        // Ensuring the owner is the one summoning
        if (!tag.getUniqueId("Owner").equals(player.getUniqueID()))
        {
            player.sendStatusMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.not_owner").mergeStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        // deserialize before setting position, otherwise we end up in original location.
        if (!world.isRemote) dragon.deserializeNBT(tag);

        EntitySize size = dragon.getSize(dragon.getPose());
        if (!world.getBlockState(pos).getCollisionShape(world, pos).isEmpty())
            pos = pos.offset(direction, (int) (direction.getAxis().isHorizontal()? size.width : 1));

        // check area for collision to ensure the area is safe.
        dragon.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.rotationYaw, 0f);
        AxisAlignedBB aabb = dragon.getBoundingBox();
        if (!world.hasNoCollisions(dragon, new AxisAlignedBB(aabb.minX, dragon.getPosYEye() - 0.35, aabb.minZ, aabb.maxX, dragon.getPosYEye() + 0.35, aabb.maxZ)))
        {
            player.sendStatusMessage(new TranslationTextComponent("item.wyrmroost.soul_crystal.fail").mergeStyle(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }

        // Spawn the entity on the server side only
        if (!world.isRemote)
        {
            stack.removeChildTag(DATA_DRAGON);
            world.addEntity(dragon);
            world.playSound(null, dragon.getPosition(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT, 1, 1);
        }
        else // Client Side Aesthetics
        {
            double posX = pos.getX() + 0.5d;
            double posY = pos.getY() + (size.height / 2);
            double posZ = pos.getZ() + 0.5d;
            for (int i = 0; i < dragon.getWidth() * 25; ++i)
            {
                double x = MathHelper.cos(i + 360 / Mafs.PI * 360f) * (dragon.getWidth() * 1.5d);
                double z = MathHelper.sin(i + 360 / Mafs.PI * 360f) * (dragon.getWidth() * 1.5d);
                double xMot = x / 10f;
                double yMot = dragon.getHeight() / 18f;
                double zMot = z / 10f;

                world.addParticle(ParticleTypes.END_ROD, posX, posY, posZ, xMot, yMot, zMot);
                world.addParticle(ParticleTypes.CLOUD, posX, posY + (i * 0.25), posZ, 0, 0, 0);
            }
        }

        return ActionResultType.SUCCESS;
    }
}
