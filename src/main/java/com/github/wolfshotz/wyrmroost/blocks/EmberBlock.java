package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EmberBlock extends Block
{
    public EmberBlock()
    {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.NETHER)
                .requiresCorrectToolForDrops()
                .lightLevel(s -> 3)
                .randomTicks()
                .strength(0.5f)
                .isValidSpawn((state, level, pos, type) -> type.fireImmune())
                .hasPostProcess((s, l, p) -> true)
                .emissiveRendering((s, l, p) -> true)
                .sound(SoundType.SAND));
    }

    @Override
    public void stepOn(World level, BlockPos pos, Entity entity)
    {
        if (!entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity))
            entity.hurt(DamageSource.HOT_FLOOR, 1.0F);

        super.stepOn(level, pos, entity);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random rng)
    {
        BlockPos up = pos.above();
        if (level.getFluidState(pos).is(FluidTags.WATER))
        {
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
            level.sendParticles(ParticleTypes.LARGE_SMOKE, (double) up.getX() + 0.5D, (double) up.getY() + 0.25D, (double) up.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
        }
        BlockState block = level.getBlockState(pos);
        if (block.getBlock() instanceof FallingBlock)
        {
            level.sendParticles(new BlockParticleData(ParticleTypes.FALLING_DUST, block), up.getX() + 0.5, up.getY(), up.getZ() + 0.5, 8, 0, 0.25, 0, 0);
        }
    }
}
