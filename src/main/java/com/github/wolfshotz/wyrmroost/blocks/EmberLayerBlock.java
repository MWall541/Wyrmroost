package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EmberLayerBlock extends SnowBlock
{
    public EmberLayerBlock()
    {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.NETHER)
                .lightLevel(s -> 3)
                .randomTicks()
                .strength(0.25f)
                .isValidSpawn((state, level, pos, type) -> type.fireImmune())
                .hasPostProcess((s, l, p) -> true)
                .emissiveRendering((s, l, p) -> true)
                .sound(SoundType.SAND));
    }

    // inherit ember block behaviours

    @Override
    public void stepOn(World level, BlockPos pos, Entity stepping)
    {
        WRBlocks.EMBER_BLOCK.get().stepOn(level, pos, stepping);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random rng)
    {
        WRBlocks.EMBER_BLOCK.get().randomTick(state, level, pos, rng);
    }
}
