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
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos pos, Config config)
    {
        return false;
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
        public final Supplier<BlockState> leaves;
        public final Supplier<BlockState> vines;

        Type(int rgb, Supplier<Block> leaves, Supplier<Block> vines)
        {
            this.color = rgb;
            this.leaves = () -> leaves.get().defaultBlockState();
            this.vines = () -> vines.get().defaultBlockState();
        }
    }

    public static class Config implements IFeatureConfig
    {
        public static final Codec<Config> CODEC = Codec.INT.xmap(i -> new Config(Type.VALUES[i]), c -> c.treeType.ordinal());

        final Type treeType;

        public Config(Type treeType)
        {
            this.treeType = treeType;
        }
    }
}
