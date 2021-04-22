package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Just an extension of {@link Tree} to allow use of vanilla sapling blocks.
 *
 * This makes the configred feature using abstract away from using the vanilla {@link BaseTreeFeatureConfig}
 * in favor of using just a normal feature.
 * In fact, it also allows more than just tree features. If for whatever reason a datapack replaces the tree configured
 * features, then the saplings growing will grow that replaced feature.
 */
public class TreeGen extends Tree
{
    private final RegistryKey<ConfiguredFeature<?, ?>> treeFeature;

    public TreeGen(RegistryKey<ConfiguredFeature<?, ?>> treeFeature)
    {
        this.treeFeature = treeFeature;
    }

    @Override
    public boolean growTree(ServerWorld level, ChunkGenerator generator, BlockPos pos, BlockState state, Random random)
    {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        boolean placed = WRWorld.getConfiguredFeature(level, treeFeature).place(level, generator, random, pos);
        if (!placed) level.setBlock(pos, state, 4);
        return placed;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean beehive)
    {
        return null;
    }
}
