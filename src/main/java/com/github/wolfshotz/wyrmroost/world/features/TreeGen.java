package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import net.minecraft.block.BlockState;
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
 * Just an extension for tree to allow use of vanilla sapling blocks.
 * todo
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
        ConfiguredFeature<?, ?> feature = WRWorld.getConfiguredFeature(treeFeature);

        return false;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean beehive)
    {
        return null;
    }
}
