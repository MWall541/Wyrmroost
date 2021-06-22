package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.blocks.GrowingPlantBlock;
import com.github.wolfshotz.wyrmroost.blocks.PetalsBlock;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

import static com.github.wolfshotz.wyrmroost.util.ModUtils.DIRECTIONS;
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
        if (!level.getBlockState(basePos.below()).is(Tags.Blocks.DIRT)) return false;

        int trunkHeight = random.nextInt(5) + 11;

        for (BlockPos pos : ModUtils.eachPositionIn(new AxisAlignedBB(basePos).move(0, trunkHeight, 0).inflate(9, 7, 9)))
            if (!TreeFeature.isFree(level, pos)) return false;

        BlockPos tip = placeTrunk(level, basePos, trunkHeight, config, random);
        placeRoots(level, basePos, random);
        for (Direction baseDir : HORIZONTALS) placeBranchWithFooliage(random.nextInt(5) + 5, trunkHeight, config, level, tip, baseDir, random);

        return true;
    }

    /**
     * @return the very top of the trunk
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
                placeLog(level, shape.setWithOffset(pointer, direction));
                if (direction.getAxis().isHorizontal() && random.nextInt((int) (trunkHeight * 0.75)) >= i)
                    // decrease thickness based on height (higher up = less thick)
                    placeLog(level, shape.move(direction.getClockWise()));
            }
        }

        return pointer;
    }


    private void placeRoots(ISeedReader level, BlockPos pos, Random random)
    {
        int rootCount = random.nextInt(2) + 3; // max 4
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
                // if the gap is too big to grow roots...
                if (j == 2) continue roots;
            }

            // add base thickness to root
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

    private void placeBranchWithFooliage(int baseLength, int heightFromGround, Type fooliageProvider, ISeedReader level, BlockPos base, Direction direction, Random random)
    {
        if (baseLength < 2) return;
        BlockPos.Mutable mutable = base.mutable().move(direction);
        int horizPath = 0;
        int vertPath;
        int branchOff = 2;
        baseLength += random.nextInt(baseLength) * 0.25;
        for (int pointer = 0; pointer <= baseLength; pointer++)
        {
            BlockPos.Mutable shapePointer = mutable.mutable();
            if (replaceable(level, shapePointer))
                placeLog(level, shapePointer);
            for (Direction dir : ModUtils.DIRECTIONS)
            {
                if (random.nextInt(baseLength) != 0 && replaceable(level, shapePointer.setWithOffset(mutable, dir)))
                    placeLog(level, shapePointer);
            }

            // fooliage
            if (pointer == baseLength)
            {
                int radius = (int) (baseLength * 0.8);
                int height = radius / 2 + 1;
                placeFooliage(radius, height, heightFromGround, fooliageProvider, level, mutable.move(Direction.DOWN, (height / 2) - 1), random);
                break; // reached the end of the branch, no need to continue
            }

            // branch off
            if (branchOff-- <= 0 && pointer > baseLength * 0.1 && random.nextInt(baseLength) < pointer * 2)
            {
                Direction branchOffTowards = random.nextBoolean()? direction.getClockWise() : direction.getCounterClockWise();
                int branchOffLength = (int) ((baseLength - pointer) * (Mafs.nextDouble(random) + 0.5));
                branchOff = 2;
                placeBranchWithFooliage(branchOffLength, heightFromGround, fooliageProvider, level, mutable, branchOffTowards, random); // dirty fucking recursion smh
            }

            vertPath = MathHelper.clamp(horizPath + (random.nextDouble() < 0.9? 1 : -1), -5, 5);
            int absV = Math.abs(vertPath);
            if (absV > 1)
            {
                boolean flag = vertPath >= 0;
                mutable.move(flag? Direction.UP : Direction.DOWN);
                heightFromGround += (flag? 1 : -1);
            }

            int prevH = horizPath;
            horizPath = MathHelper.clamp(horizPath + (random.nextBoolean()? 1 : 0), -5, 5);
            int absH = Math.abs(horizPath);
            Direction dirH = horizPath > 0? direction.getClockWise() : direction.getCounterClockWise();
            if (absH == 5 && prevH != horizPath) direction = dirH;
            else if (absH > 1) mutable.move(dirH);

            mutable.move(direction);
        }
    }


    private void placeFooliage(int radius, int height, int heightFromGround, Type fooliageProvider, ISeedReader level, BlockPos center, Random random)
    {
        BlockState leaves = fooliageProvider.getLeaves();
        BlockState vines = fooliageProvider.getVines();
        BlockPos.Mutable pointer = new BlockPos.Mutable();

        // add tiny branches in every direction to reduce loss of leaves
        for (Direction direction : DIRECTIONS)
        {
            pointer.set(center);
            int distance = (direction.getAxis().isHorizontal()? radius : height) / 2;
            for (int i = 0; i < distance; i++) placeLog(level, pointer.move(direction));
        }

        double invRadius = 1d / radius;
        double invHeight = 1d / height;

        double nextXn = 0;
        forX:
        for (int x = 0; x <= radius; ++x)
        {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadius;
            double nextYn = 0;
            forY:
            for (int y = 0; y <= height; ++y)
            {
                final double yn = nextYn;
                nextYn = (y + 1) * invHeight;
                double nextZn = 0;
                for (int z = 0; z <= radius; ++z)
                {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadius;

                    double distanceSq = xn * xn + yn * yn + zn * zn;
                    if (distanceSq >= 1)
                    {
                        if (z == 0)
                        {
                            if (y == 0) break forX;
                            break forY;
                        }
                        break;
                    }

                    // calculations for each position are only done on one quarter of the sphere.
                    // more efficient to negate values than to make 3x more for-loops
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, x, y, z), leaves, null, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, -x, y, z), leaves, null, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, x, -y, z), leaves, vines, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, x, y, -z), leaves, null, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, -x, -y, z), leaves, vines, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, x, -y, -z), leaves, vines, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, -x, y, -z), leaves, null, random);
                    placeLeavesAndVines(level, distanceSq, pointer.setWithOffset(center, -x, -y, -z), leaves, vines, random);
                }
            }
        }

        placeFallenPetals(level, center, heightFromGround, radius + 2, fooliageProvider, random);
    }

    private static void placeLeavesAndVines(ISeedReader level, double distanceSq, BlockPos offset, BlockState leaves, @Nullable BlockState vines, Random random)
    {
        if (distanceSq >= 0.85 && random.nextDouble() > 0.4) return;

        if (noCollision(level, offset)) level.setBlock(offset, leaves, 19);
        if (vines != null && random.nextDouble() < 0.25)
        {
            BlockPos.Mutable under = offset.mutable();
            GrowingPlantBlock block = (GrowingPlantBlock) vines.getBlock();
            int vineHeight = random.nextInt(9);
            for (int i = 0; i <= vineHeight && noCollision(level, under.move(Direction.DOWN)); i++)
                level.setBlock(under, i == vineHeight? block.getStateForPlacement(level) : block.getBodyBlock().defaultBlockState(), 19);
        }
    }

    private void placeFallenPetals(ISeedReader level, BlockPos basePos, int maxHeight, int radius, Type config, Random random)
    {
        BlockPos.Mutable mutable = basePos.mutable();
        BlockState petals = config.getPetals();

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

                placePetal(level, mutable.setWithOffset(basePos, x, 0, z), petals, maxHeight, random);
                placePetal(level, mutable.setWithOffset(basePos, -x, 0, z), petals, maxHeight, random);
                placePetal(level, mutable.setWithOffset(basePos, x, 0, -z), petals, maxHeight, random);
                placePetal(level, mutable.setWithOffset(basePos, -x, 0, -z), petals, maxHeight, random);
            }
        }
    }

    private static void placePetal(ISeedReader level, BlockPos base, BlockState state, int maxHeight, Random random)
    {
        if (random.nextDouble() > 0.25) return;
        maxHeight += 2;
        BlockPos.Mutable mutable = base.mutable();
        for (int i = 0; i <= maxHeight; i++)
        {
            mutable.setY(base.getY() - i);
            if (Block.isFaceFull(level.getBlockState(mutable).getCollisionShape(level, mutable), Direction.UP) && level.getBlockState(mutable.move(Direction.UP)).isAir())
            {
                level.setBlock(mutable, state.setValue(PetalsBlock.AXIS, random.nextBoolean()? Direction.Axis.X : Direction.Axis.Z), 2);
                break;
            }
        }
    }


    private static void placeLog(ISeedReader level, BlockPos pos)
    {
        if (replaceable(level, pos)) level.setBlock(pos, WRBlocks.OSERI_WOOD.getWood().defaultBlockState(), 2);
    }

    public static boolean noCollision(ISeedReader level, BlockPos pos)
    {
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }

    public static boolean replaceable(ISeedReader level, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        return state.getCollisionShape(level, pos).isEmpty() || state.is(BlockTags.LEAVES);
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
