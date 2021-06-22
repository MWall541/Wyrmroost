package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class MossVinesFeature extends Feature<NoFeatureConfig>
{
    public MossVinesFeature()
    {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig feature)
    {
        BlockPos.Mutable mutable = pos.mutable();

        int maxHeight = level.getMaxBuildHeight();
        for (int i = 64; i < maxHeight; ++i)
        {
            mutable.setWithOffset(pos, Mafs.nextInt(random, 4), i, Mafs.nextInt(random, 4));
            if (level.isEmptyBlock(mutable))
            {
                for (Direction direction : ModUtils.DIRECTIONS)
                {
                    if (direction != Direction.DOWN && acceptableNeighbor(level, mutable.relative(direction), direction))
                    {
                        level.setBlock(mutable, WRBlocks.MOSS_VINE
                                .get()
                                .defaultBlockState()
                                .setValue(VineBlock.getPropertyForFace(direction), true), 2);
                        break;
                    }
                }
            }
        }

        return true;
    }

    static boolean acceptableNeighbor(ISeedReader level, BlockPos pos, Direction dir)
    {
        BlockState state = level.getBlockState(pos);
        return state.getMaterial().isSolidBlocking() && Block.isFaceFull(state.getCollisionShape(level, pos), dir.getOpposite());
    }
}
