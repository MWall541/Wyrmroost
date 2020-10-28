package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.client.render.FogWraithTailsStackRenderer;
import WolfShotz.Wyrmroost.client.render.RenderHelper;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.entities.util.animation.IAnimatable;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.TickFloat;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static WolfShotz.Wyrmroost.client.render.FogWraithTailsStackRenderer.GRAPPLE_ANIMATION;
import static WolfShotz.Wyrmroost.client.render.FogWraithTailsStackRenderer.TAIL_SWIPE_ANIMATION;

public class FogWraithTailsItem extends Item
{
    private static final ImmutableSet<Enchantment> ENCHANTMENTS = ImmutableSet.of(Enchantments.SHARPNESS,
            Enchantments.FIRE_ASPECT,
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.KNOCKBACK,
            Enchantments.SMITE,
            Enchantments.LOOTING);

    public FogWraithTailsItem()
    {
        super(WRItems.builder().maxStackSize(1).setISTER(() -> FogWraithTailsStackRenderer::new));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int timeLeft)
    {
        timeLeft = stack.getUseDuration() - timeLeft;
        Capability cap = getCapability(stack);
        if (cap.noActiveAnimation())
        {
            if (timeLeft > 5)
            {
                cap.setAnimation(GRAPPLE_ANIMATION);
                entity.rotationYaw = entity.rotationYawHead;
            }
            else cap.setAnimation(FogWraithTailsStackRenderer.TAIL_SWIPE_ANIMATION);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        Capability cap = getCapability(stack);
        cap.updateAnimations();
        if (!(entity instanceof LivingEntity)) return;

        Animation animation = cap.getAnimation();
        int tick = cap.getAnimationTick();
        boolean flag = ((LivingEntity) entity).getActiveItemStack() == stack || !cap.noActiveAnimation();
        cap.transition.add(flag? 0.1f : -0.1f);

        if (animation == GRAPPLE_ANIMATION && (tick == 10 || tick == 4))
        {
            Vector3d position = entity.getEyePosition(1);
            Vector3d direction = entity.getLookVec();
            Vector3d end = position.add(direction.mul(10, 10, 10));
            BlockRayTraceResult rtr = world.rayTraceBlocks(new RayTraceContext(position, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, null));
            if (rtr.getType() != RayTraceResult.Type.MISS)
            {
                BlockPos pos = rtr.getPos();
                if (tick == 10)
                {
                    Vector3d motion = entity.getPositionVec().subtract(Vector3d.copy(pos));
                    double y = Math.sqrt(motion.length());
                    entity.setMotion(motion.mul(-0.4d, -0.2d, -0.4d).add(0, y * 0.2, 0));
                }
                else if (world.isRemote)
                {
                    BlockState state = world.getBlockState(pos);
                    pos = pos.offset(rtr.getFace());
                    for (int i = 0; i < 30; i++)
                        world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), state.getSoundType().getBreakSound(), SoundCategory.BLOCKS, state.getSoundType().getVolume(), state.getSoundType().getPitch(), false);
                }
            }
        }
        else if (entity instanceof PlayerEntity && animation == TAIL_SWIPE_ANIMATION && (tick == 2 || tick == 6))
        {
            AxisAlignedBB aabb = entity.getBoundingBox().grow(1).offset(entity.getLookVec().mul(1.7, 0.7f, 1.7));
            RenderHelper.DebugBox.INSTANCE.queue(aabb, 100);
            boolean playHitSound = false;
            for (Entity attacking : world.getEntitiesInAABBexcluding(entity, aabb, e -> e instanceof LivingEntity && !e.isOnSameTeam(entity)))
                playHitSound |= attackEntity((PlayerEntity) entity, (LivingEntity) attacking, 8);
            world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), playHitSound? SoundEvents.ENTITY_PLAYER_ATTACK_STRONG : SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, entity.getSoundCategory(), 1f, 1f);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        playerIn.setActiveHand(handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return ENCHANTMENTS.contains(enchantment);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack)
    {
        return 15;
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
        return (Capability) stack.getCapability(IAnimatable.CapImpl.CAPABILITY).orElseThrow(NullPointerException::new);
    }

    private static boolean attackEntity(LivingEntity attacker, LivingEntity attacking, float damage)
    {
        float knockback = EnchantmentHelper.getKnockbackModifier(attacker) + 1f;
        damage += EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), attacking.getCreatureAttribute());

        int fire = EnchantmentHelper.getFireAspectModifier(attacker);
        if (fire > 0) attacking.setFire(fire * 4);

        boolean flag = attacking.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
        if (flag)
        {
            attacking.applyKnockback(knockback, MathHelper.sin(attacker.rotationYaw * Mafs.PI / 180f), -MathHelper.cos(attacker.rotationYaw * Mafs.PI / 180F));
            attacker.setMotion(attacker.getMotion().mul(0.6, 1, 0.6));

            attacker.applyEnchantments(attacker, attacking);
            attacker.setLastAttackedEntity(attacking);
        }
        return flag;
    }

    public static class Capability extends IAnimatable.CapImpl implements ICapabilityProvider
    {
        public final TickFloat transition = new TickFloat().setLimit(0, 1);
        private final LazyOptional<IAnimatable> holder = LazyOptional.of(() -> this);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side)
        {
            return CapImpl.CAPABILITY.orEmpty(cap, holder);
        }
    }
}
