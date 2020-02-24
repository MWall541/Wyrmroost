package WolfShotz.Wyrmroost.content.world.biomes;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.IRenderHandler;

import javax.annotation.Nullable;

public class ExtendedBiome extends Biome
{
    @Nullable
    public IRenderHandler skyRenderer, cloudRenderer, weatherRenderer;

    public ExtendedBiome(Builder biomeBuilder)
    {
        super(biomeBuilder);
    }

    public void setSkyRenderer(IRenderHandler handler) { this.skyRenderer = handler; }

    public void setCloudRenderer(IRenderHandler handler) { this.cloudRenderer = handler; }

    public void setWeatherRenderer(IRenderHandler handler) { this.weatherRenderer = handler; }

    public float getSunBrightness(World world, float partialTicks) { return world.getSunBrightnessBody(partialTicks); }

    public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {}

    public Vec3d getCloudColor(World world, float partialTicks)
    {
        return world.getCloudColorBody(partialTicks);
    }

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
}
