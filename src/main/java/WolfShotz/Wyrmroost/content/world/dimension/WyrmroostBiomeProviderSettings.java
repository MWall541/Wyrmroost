package WolfShotz.Wyrmroost.content.world.dimension;

import net.minecraft.world.biome.provider.IBiomeProviderSettings;

public class WyrmroostBiomeProviderSettings implements IBiomeProviderSettings
{
    private long seed;

    public WyrmroostBiomeProviderSettings setSeed(long seed) {
        seed = seed;
        return this;
    }

    public long getSeed() {
        return this.seed;
    }
}
