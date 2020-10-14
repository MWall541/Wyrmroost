package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.client.render.FogWraithTailsStackRenderer;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.entities.util.animation.CapabilityAnimationHandler;
import WolfShotz.Wyrmroost.entities.util.animation.IAnimatable;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FogWraithTailsItem extends Item
{
    public FogWraithTailsItem()
    {
        super(WRItems.builder().maxStackSize(1).setISTER(() -> FogWraithTailsStackRenderer::new));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int timeLeft)
    {
        timeLeft = stack.getUseDuration() - timeLeft;
        Capability cap = getCapability(stack);
        if (timeLeft < 20)
        {
            cap.setAnimation(FogWraithTailsStackRenderer.GRAPPLE_ANIMATION);
            entity.rotationYaw = entity.rotationYawHead;
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        Capability cap = getCapability(stack);
        cap.updateAnimations();
        if (entity instanceof LivingEntity)
        {
            boolean flag = ((LivingEntity) entity).getActiveItemStack() == stack || !cap.noActiveAnimation();
            cap.transition.add(flag? 0.2f : -0.2f);
        }

        Animation animation = cap.getAnimation();
        int tick = cap.getAnimationTick();

        if (animation == FogWraithTailsStackRenderer.GRAPPLE_ANIMATION && tick == 8)
        {
            Vector3d position = entity.getEyePosition(1);
            Vector3d direction = entity.getLookVec();
            Vector3d end = position.add(direction.mul(5, 5, 5));
            BlockRayTraceResult rtr = world.rayTraceBlocks(new RayTraceContext(position, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, null));
            if (rtr.getType() != RayTraceResult.Type.MISS)
            {
                BlockPos pos = rtr.getPos();

            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        playerIn.setActiveHand(handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new Capability();
    }

    public static Capability getCapability(ItemStack stack)
    {
        return (Capability) stack.getCapability(CapabilityAnimationHandler.ANIMATABLE_CAPABILITY).orElseThrow(NullPointerException::new);
    }

    public static class Capability extends CapabilityAnimationHandler.Holder implements ICapabilityProvider
    {
        public final TickFloat transition = new TickFloat().setLimit(0, 1);
        private final LazyOptional<IAnimatable> holder = LazyOptional.of(() -> this);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side)
        {
            return CapabilityAnimationHandler.ANIMATABLE_CAPABILITY.orEmpty(cap, holder);
        }
    }
}
