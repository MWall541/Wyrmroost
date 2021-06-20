package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Random;

public class MulchBlock extends SnowyDirtBlock implements IGrowable
{
    public MulchBlock()
    {
        super(WRBlocks.properties(Material.DIRT, WRSounds.Types.MULCH)
                .strength(0.5f)
                .harvestTool(ToolType.SHOVEL));
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader level, BlockPos pos, BlockState state, boolean isClient)
    {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(World level, Random random, BlockPos pos, BlockState state)
    {
        return true;
    }

    //taken from GrassBlock todo: optimize?
    @SuppressWarnings("unchecked")
    public void performBonemeal(ServerWorld level, Random random, BlockPos pos, BlockState state)
    {
        BlockPos blockpos = pos.above();
        BlockState blockstate = Blocks.GRASS.defaultBlockState();

        label48:
        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos1 = blockpos;

            for (int j = 0; j < i / 16; ++j)
            {
                blockpos1 = blockpos1.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!level.getBlockState(blockpos1.below()).is(this) || level.getBlockState(blockpos1).isCollisionShapeFullBlock(level, blockpos1))
                    continue label48;
            }

            BlockState blockstate2 = level.getBlockState(blockpos1);
            if (blockstate2.is(blockstate.getBlock()) && random.nextInt(10) == 0)
                ((IGrowable) blockstate.getBlock()).performBonemeal(level, random, blockpos1, blockstate2);

            if (blockstate2.isAir())
            {
                BlockState blockstate1;
                if (random.nextInt(8) == 0)
                {
                    List<ConfiguredFeature<?, ?>> list = level.getBiome(blockpos1).getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) continue;

                    ConfiguredFeature<?, ?> feature = list.get(0);
                    FlowersFeature<IFeatureConfig> flowers = (FlowersFeature<IFeatureConfig>) feature.feature;
                    blockstate1 = flowers.getRandomFlower(random, blockpos1, feature.config());
                }
                else blockstate1 = blockstate;

                if (blockstate1.canSurvive(level, blockpos1))
                    level.setBlock(blockpos1, blockstate1, 3);
            }
        }

    }
}
