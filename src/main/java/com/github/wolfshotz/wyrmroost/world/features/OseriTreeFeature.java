package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.blocks.PetalsBlock;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

import static com.github.wolfshotz.wyrmroost.util.ModUtils.HORIZONTALS;

public class OseriTreeFeature extends Feature<OseriTreeFeature.Type>
{
    public OseriTreeFeature()
    {
        super(Type.CODEC);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos basePos, Type config)
    {
        if (level.getBlockState(basePos.below()).getBlock() != WRBlocks.MULCH.get()) return false;

        int trunkHeight = random.nextInt(5) + 11;

        BlockPos trunkTip = placeTrunk(level, basePos, trunkHeight, config, random);
        placeRoots(level, basePos, random);
        placeFallenPetals(level, basePos, config, random);
        placeBranchesAndFooliage(config, level, trunkTip, random);

        return true;
    }

    /**
     * @return the tip of the trunk that was just placed.
     */
    private BlockPos placeTrunk(ISeedReader level, BlockPos basePos, int trunkHeight, Type config, Random random)
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

            pointer.setWithOffset(basePos, x, i, z);
            BlockPos.Mutable shape = pointer.mutable();
            for (Direction direction : ModUtils.DIRECTIONS)
            {
                placeLog(level, shape.setWithOffset(pointer, direction), config, random);
                if (direction.getAxis().isHorizontal() && random.nextInt((int) (trunkHeight * 0.75)) >= i)
                    // decrease thickness based on height (higher up = less thick)
                    placeLog(level, shape.move(direction.getClockWise()), config, random);
            }
        }

        return pointer.immutable();
    }


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
            int rootLength = random.nextInt(3) + 3; //max 5
            boolean left = random.nextBoolean();
            double dirChance = 0;

            BlockPos.Mutable gap = pointer.mutable();
            for (int j = 0; j < 3 && noCollision(level, gap.move(Direction.DOWN)); j++)
            {
                //if the gap is too big to grow roots...
                if (j == 2) continue roots;
            }

            //add base thickness to root
            placeLog(level, pointer.relative(offsetDir.getOpposite()));

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
                dirChance += 0.45;
            }
        }
    }

    private void placeFallenPetals(ISeedReader level, BlockPos basePos, Type config, Random random)
    {
        BlockPos.Mutable mutable = basePos.mutable();
        final int radius = random.nextInt(5) + 8;

        final double invRadius = 1d / radius;
        final int ceilRadius = (int) Math.ceil(radius);

        double nextXn = 0;
        forX:
        for (int x = 0; x <= ceilRadius; ++x)
        {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadius;
            double nextZn = 0;
            for (int z = 0; z <= ceilRadius; ++z)
            {
                final double zn = nextZn;
                nextZn = (z + 1) * invRadius;

                double distanceSq = (xn * xn) + (zn * zn);
                if (distanceSq > 0.8 + random.nextDouble())
                {
                    if (z == 0) break forX;
                    break;
                }

                placePetal(level, mutable.setWithOffset(basePos, x, 0, z), config.getPetals(), random);
                placePetal(level, mutable.setWithOffset(basePos, -x, 0, z), config.getPetals(), random);
                placePetal(level, mutable.setWithOffset(basePos, x, 0, -z), config.getPetals(), random);
                placePetal(level, mutable.setWithOffset(basePos, -x, 0, -z), config.getPetals(), random);
            }
        }
    }

    private void placeBranchesAndFooliage(Type config, ISeedReader level, BlockPos trunkTip, Random random)
    {
        setBlock(level, trunkTip.above(), config.getLeaves());
    }

    private static void placeLog(ISeedReader level, BlockPos pos)
    {
        placeLog(level, pos, null, null);
    }

    private static void placeLog(ISeedReader level, BlockPos pos, Type config, Random random)
    {
        if (noCollision(level, pos) || level.getBlockState(pos).is(BlockTags.LEAVES))
            level.setBlock(pos, WRBlocks.OSERI_WOOD.getWood().defaultBlockState(), 2);
        if (config != null && random.nextBoolean())
        {
            pos = pos.above();
            if (noCollision(level, pos)) level.setBlock(pos, config.getPetals(), 2);
        }
    }

    private static void placePetal(ISeedReader level, BlockPos base, BlockState state, Random random)
    {
        if (random.nextDouble() > 0.25) return;
        BlockPos.Mutable mutable = base.mutable();
        for (int i = -3; i <= 2; i++)
        {
            mutable.setY(base.getY() + i);
            if (Block.isFaceFull(level.getBlockState(mutable).getCollisionShape(level, mutable), Direction.UP) && level.getBlockState(mutable.move(Direction.UP)).isAir())
            {
                level.setBlock(mutable, state.setValue(PetalsBlock.AXIS, random.nextBoolean()? Direction.Axis.X : Direction.Axis.Z), 2);
                break;
            }
        }
    }

    public static boolean noCollision(ISeedReader level, BlockPos pos)
    {
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }

    public enum Type implements IFeatureConfig
    {
        BLUE(0x6E8AC9, WRBlocks.BLUE_OSERI_LEAVES, WRBlocks.BLUE_OSERI_VINES, WRBlocks.BLUE_OSERI_PETALS),
        GOLD(0xE7CC35, WRBlocks.GOLD_OSERI_LEAVES, WRBlocks.GOLD_OSERI_VINES, WRBlocks.GOLD_OSERI_PETALS),
        PINK(0xE4B8CF, WRBlocks.PINK_OSERI_LEAVES, WRBlocks.PINK_OSERI_VINES, WRBlocks.PINK_OSERI_PETALS),
        PURPLE(0x8D7FC6, WRBlocks.PURPLE_OSERI_LEAVES, WRBlocks.PURPLE_OSERI_VINES, WRBlocks.PURPLE_OSERI_PETALS),
        WHITE(0xFBFEF5, WRBlocks.WHITE_OSERI_LEAVES, WRBlocks.WHITE_OSERI_VINES, WRBlocks.WHITE_OSERI_PETALS);

        public static final Codec<Type> CODEC = Codec.STRING.xmap(Type::byName, c -> c.name().toLowerCase()).orElse(WHITE);
        public static final Type[] VALUES = values();

        public final int color;
        public final Supplier<Block> leaves;
        public final Supplier<Block> vines;
        public final Supplier<Block> petals;

        Type(int rgb, Supplier<Block> leaves, Supplier<Block> vines, Supplier<Block> petals)
        {
            this.color = rgb;
            this.leaves = leaves;
            this.vines = vines;
            this.petals = petals;
        }

        public static Type byName(String name)
        {
            for (Type value : VALUES) if (value.name().toLowerCase(Locale.ROOT).equals(name)) return value;
            return WHITE;
        }

        public BlockState getLeaves()
        {
            return leaves.get().defaultBlockState();
        }

        public BlockState getVines()
        {
            return vines.get().defaultBlockState();
        }

        public BlockState getPetals()
        {
            return petals.get().defaultBlockState();
        }
    }
}
