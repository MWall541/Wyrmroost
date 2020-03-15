package WolfShotz.Wyrmroost.util.world;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.IRenderHandler;

import java.util.function.Consumer;

public class WRBiome extends Biome
{
    private final int weight;

    // Queue work to do later (example add features, structures) at common setup
    // This is for fixing bull shit with null registries
    public Consumer<Biome> queueWork = b ->
    {
    };

    /**
     * @param biomeBuilder - typical biome builder crap
     * @param weight       - rarity in which we may see this biome
     */
    public WRBiome(int weight, Builder biomeBuilder)
    {
        super(biomeBuilder);
        this.weight = weight;
    }

    public IRenderHandler getSkyRenderer() { return null; }

    public IRenderHandler getCloudRenderer() { return null; }

    public IRenderHandler getWeatherRenderer() { return null; }

    public float getSunBrightness(World world, float partialTicks) { return world.getSunBrightnessBody(partialTicks); }

    public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {}

    public Vec3d getCloudColor(World world, float partialTicks) { return world.getCloudColorBody(partialTicks); }

    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        // Taken from overworld
        float f = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);

        return new Vec3d(f1, f2, f3);
    }

    public boolean doesXZShowFog(int x, int z) { return false; }

    public int getWeight() { return weight; }
}
