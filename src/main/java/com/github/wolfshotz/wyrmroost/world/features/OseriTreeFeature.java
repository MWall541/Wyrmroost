package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.DebugRendering;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class OseriTreeFeature extends Feature<OseriTreeFeature.Config>
{
    public OseriTreeFeature()
    {
        super(Config.CODEC);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos basePos, Config config)
    {
        if (noMulchBelow(level, basePos)) return false;

        int trunkHeight = random.nextInt(5) + 11;

        BlockPos trunkTip = placeTrunk(level, basePos, trunkHeight, random);
        if (trunkTip == null) return false;

        placeRoots(level, basePos, random);
        placeFooliage(level, trunkTip, random);

        return true;
    }

    /**
     * @return the tip of the trunk that was just placed.
     */
    private BlockPos placeTrunk(ISeedReader level, BlockPos basePos, int trunkHeight, Random random)
    {
        BlockPos pointer = null;
        double xDiff = MathHelper.clamp(random.nextDouble(), 0.35, 0.75);
        double zDiff = 1 - xDiff;
        int xInverse = random.nextBoolean()? 1 : -1;

        for (int i = 0; i < trunkHeight; i++)
        {
            int x = (int) ((MathHelper.sin(i * 0.35f) * 4) * xDiff) * xInverse;
            int z = (int) ((MathHelper.sin(i * 0.35f) * 4) * zDiff) * -xInverse;
            pointer = basePos.offset(x, i, z);
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
//                    if ((j == 1 && k == 1) || random.nextDouble() < 0.85)
                        placeLog(level, pointer.offset(j - 1, 0, k - 1));
                }
            }
        }

        return pointer;
    }

    private static final Direction[] HORIZONTALS = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    private boolean placeRoots(ISeedReader level, BlockPos pos, Random random)
    {
        int rootCount = random.nextInt(3) + 3; // max 5
        for (int i = 0; i < rootCount; i++)
        {
            BlockPos pointer = null;
            int rootLength = random.nextInt(4) + 3;
            int index = random.nextInt(HORIZONTALS.length);
            Direction rootDir = HORIZONTALS[index];

            for (int j = 0; j < rootLength; j++)
            {
                if (j > 1) rootDir = HORIZONTALS[index];
            }
        }

        return true;
    }

    private boolean placeFooliage(ISeedReader level, BlockPos trunkTip, Random random)
    {
        return false;
    }

    private void placeLog(ISeedReader level, BlockPos pos)
    {
        level.setBlock(pos, WRBlocks.OSERI_WOOD.getLog().defaultBlockState(), 2);
    }

    // must place on mulch only!
    private static boolean noMulchBelow(ISeedReader level, BlockPos pos)
    {
        return level.getBlockState(pos.below()).getBlock() != WRBlocks.MULCH.get();
    }

    public enum Type
    {
        BLUE(0x6E8AC9, WRBlocks.BLUE_OSERI_LEAVES, WRBlocks.BLUE_OSERI_VINES),
        GOLD(0xE7CC35, WRBlocks.GOLD_OSERI_LEAVES, WRBlocks.GOLD_OSERI_VINES),
        PINK(0xE4B8CF, WRBlocks.PINK_OSERI_LEAVES, WRBlocks.PINK_OSERI_VINES),
        PURPLE(0x8D7FC6, WRBlocks.PURPLE_OSERI_LEAVES, WRBlocks.PURPLE_OSERI_VINES),
        WHITE(0xFBFEF5, WRBlocks.WHITE_OSERI_LEAVES, WRBlocks.WHITE_OSERI_VINES);

        public static final Type[] VALUES = values();

        public final int color;
        public final Supplier<Block> leaves;
        public final Supplier<Block> vines;

        Type(int rgb, Supplier<Block> leaves, Supplier<Block> vines)
        {
            this.color = rgb;
            this.leaves = leaves;
            this.vines = vines;
        }

        public static Type byName(String name)
        {
            for (Type value : VALUES) if (value.name().equals(name)) return value;
            return BLUE;
        }

        public BlockState getLeavesState()
        {
            return leaves.get().defaultBlockState();
        }

        public BlockState getVinesState()
        {
            return vines.get().defaultBlockState();
        }
    }

    public static class Config implements IFeatureConfig
    {
        public static final Codec<Config> CODEC = Codec.STRING.xmap(s -> new Config(Type.byName(s)), c -> c.treeType.name().toLowerCase());

        final Type treeType;

        public Config(Type treeType)
        {
            this.treeType = treeType;
        }
    }
}
