package WolfShotz.Wyrmroost.content.world.features;

import WolfShotz.Wyrmroost.registry.WRBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class AshTreeFeature extends Feature<NoFeatureConfig>
{
    public AshTreeFeature()
    {
        super(NoFeatureConfig::deserialize);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        if (worldIn.isAirBlock(pos) && worldIn.getBlockState(pos.down()).isSolid())
        {
            int height = rand.nextInt(5) + 1;
            for (int perLog = 0; perLog < height; ++perLog)
                worldIn.setBlockState(pos.up(perLog), WRBlocks.ASH_LOG.get().getDefaultState(), 2);
        }

        return true;
    }
}
