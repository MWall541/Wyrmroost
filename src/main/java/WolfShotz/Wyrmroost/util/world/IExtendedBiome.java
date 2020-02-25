package WolfShotz.Wyrmroost.util.world;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;

public interface IExtendedBiome
{
    default IRenderHandler getSkyRenderer() { return null; }

    default IRenderHandler getCloudRenderer() { return null; }

    default IRenderHandler getWeatherRenderer() { return null; }

    default float getSunBrightness(World world, float partialTicks) { return world.getSunBrightnessBody(partialTicks); }

    default void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {}

    default Vec3d getCloudColor(World world, float partialTicks) { return world.getCloudColorBody(partialTicks); }

    default Vec3d getFogColor(float celestialAngle, float partialTicks)
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

    default boolean doesXZShowFog(int x, int z) { return false; }
}
