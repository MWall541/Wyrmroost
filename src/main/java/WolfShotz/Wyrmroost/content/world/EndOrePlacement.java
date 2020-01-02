package WolfShotz.Wyrmroost.content.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.SimplePlacement;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EndOrePlacement extends SimplePlacement<NoPlacementConfig>
{
    @ObjectHolder("wyrmroost:end_ore")
    public static Placement<NoPlacementConfig> endOre;
    
    public EndOrePlacement() { super(NoPlacementConfig::deserialize); }
    
    @Override
    protected Stream<BlockPos> getPositions(Random random, NoPlacementConfig config, BlockPos pos) {
        int i = 3 + random.nextInt(6);
        return IntStream.range(0, i).mapToObj((p_215060_2_) -> {
            int j = random.nextInt(16);
            int k = random.nextInt(128);
            int l = random.nextInt(16);
            return pos.add(j, k, l);
        });
    }
}
