package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
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
        if (noMulchBelow(level, basePos)) return false;

        BlockPos trunkTip = placeTrunk(level, basePos, random);
        if (trunkTip == null) return false;

        return placeRoots(level, basePos, random) && placeFooliage(level, trunkTip, random);
    }

    /**
     * @return the tip of the trunk that was just placed.
     */
    private BlockPos placeTrunk(ISeedReader level, BlockPos basePos, Random random)
    {
        return null;
    }

    private boolean placeRoots(ISeedReader level, BlockPos pos, Random random)
    {
        return false;
    }

    private boolean placeFooliage(ISeedReader level, BlockPos trunkTip, Random random)
    {
        return false;
    }

    private void placeLog(ISeedReader level, BlockPos pos)
    {
        BlockPos pointer;
        boolean closed = level.getBlockState(pointer = pos.above()).isSolidRender(level, pointer) && level.getBlockState(pointer = pos.below()).isSolidRender(level, pointer);
        level.setBlock(pos, (closed? WRBlocks.OSERI_WOOD.getWood() : WRBlocks.OSERI_WOOD.getLog()).defaultBlockState(), 2);
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
