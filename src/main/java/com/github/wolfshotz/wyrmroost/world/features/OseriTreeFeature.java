package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
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

import java.util.Random;
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
        if (level.getBlockState(basePos.below()).getBlock() != WRBlocks.MULCH.get()) return false;

        int trunkHeight = random.nextInt(5) + 11;

        BlockPos trunkTip = placeTrunk(level, basePos, trunkHeight, random);
        placeRoots(level, basePos, random);
        placeFooliage(level, trunkTip, random);

        return true;
    }

    /**
     * @return the tip of the trunk that was just placed.
     */
    private BlockPos placeTrunk(ISeedReader level, BlockPos basePos, int trunkHeight, Random random)
    {
        BlockPos.Mutable pointer = basePos.mutable();
        double xDiff = MathHelper.clamp(random.nextDouble(), 0.35, 0.65);
        double zDiff = 1 - xDiff;
        int xInverse = random.nextBoolean()? 1 : -1;

        for (int i = 0; i < trunkHeight; i++)
        {
            float path = MathHelper.sin(i * 0.35f) * 4;
            int x = (int) (path * xDiff) * xInverse;
            int z = (int) (path * zDiff) * -xInverse;
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    pointer.setWithOffset(basePos, x + (j - 1), i, z + (k - 1));

                    if (i == 0)
                    {
                        //fill possible gaps under trunk with varied logs
                        BlockPos.Mutable correction = pointer.mutable();
                        for (int l = 0; l < random.nextInt(3) + 1; l++)
                        {
                            if (noCollision(level, correction.move(Direction.DOWN))) placeLog(level, correction);
                            else break;
                        }
                    }

                    placeLog(level, pointer);
                }
            }
        }

        return pointer.immutable();
    }

    private static final Direction[] HORIZONTALS = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    private void placeRoots(ISeedReader level, BlockPos pos, Random random)
    {
        int rootCount = random.nextInt(2) + 3; //max 4
        int index = random.nextInt(HORIZONTALS.length);
        roots:
        for (int i = 0; i < rootCount; i++)
        {
            Direction rootDir = HORIZONTALS[index++];
            index %= HORIZONTALS.length; // cycle through
            BlockPos.Mutable pointer = pos.mutable().move(rootDir, 2);
            Direction offsetDir = random.nextBoolean()? rootDir.getClockWise() : rootDir.getCounterClockWise();
            if (random.nextBoolean()) pointer.move(offsetDir);
            int rootLength = random.nextInt(3) + 4; //max 6
            boolean left = random.nextBoolean();
            double dirChance = 0;

            BlockPos.Mutable gap = pointer.mutable();
            for (int j = 0; j < 3 && noCollision(level, gap.move(Direction.DOWN)); j++)
            {
                if (j == 2) continue roots;
            }

            //add base thickness (or have trunk noise handle it?)
            placeLog(level, pointer.relative(offsetDir.getOpposite(), 1));

            for (int j = 0; j < rootLength; j++)
            {
                boolean last = j == rootLength - 1;
                placeLog(level, pointer);
                pointer.move(rootDir);

                if (!noCollision(level, pointer) && noCollision(level, pointer.above()))
                    pointer.move(Direction.UP);
                else if (noCollision(level, pointer.below()))
                {
                    if (last) placeLog(level, pointer.below());
                    else pointer.move(Direction.DOWN);
                }

                if (j != 0 && random.nextDouble() < dirChance * (last? 3 : 1))
                {
                    rootDir = left? rootDir.getClockWise() : rootDir.getCounterClockWise();
                    left = !left;
                    pointer.move(rootDir);
                    dirChance -= 0.5;
                }
                dirChance += 0.25;
            }
        }
    }

    private void placeFooliage(ISeedReader level, BlockPos trunkTip, Random random)
    {
    }

    private void placeLog(ISeedReader level, BlockPos pos)
    {
        if (noCollision(level, pos)) level.setBlock(pos, WRBlocks.OSERI_WOOD.getWood().defaultBlockState(), 2);
    }

    private static boolean noCollision(ISeedReader level, BlockPos pos)
    {
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
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
