package com.github.wolfshotz.wyrmroost.client;

import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Random;

public class WRDimensionRenderInfo extends DimensionRenderInfo
{
    private int weatherTime = 0;

    public WRDimensionRenderInfo()
    {
        super(128f, true, DimensionRenderInfo.FogType.NORMAL, false, false);
        setWeatherRenderHandler(this::renderWeather);
        setWeatherParticleRenderHandler(this::renderWeatherParticles);
    }

    @Override
    public Vector3d func_230494_a_(Vector3d fogDimensions, float density)
    {
        return fogDimensions.mul(density * 0.94F + 0.06F, density * 0.94F + 0.06F, density * 0.91F + 0.09F);
    }

    @Override
    public boolean func_230493_a_(int p_230493_1_, int p_230493_2_)
    {
        return false;
    }

    private void renderWeather(int ticks, float partialTicks, ClientWorld world, Minecraft minecraft, LightTexture lightTexture, double x, double y, double z)
    {

    }

    private void renderWeatherParticles(int ticks, ClientWorld world, Minecraft client, ActiveRenderInfo renderInfo)
    {
        float rainDelta = client.world.getRainStrength(1f) / (Minecraft.isFancyGraphicsEnabled()? 1f : 2f);
        if (rainDelta > 0)
        {
            Random random = new Random(ticks * 312987231L);
            BlockPos view = new BlockPos(renderInfo.getProjectedView());
            BlockPos at = null;
            int tries = (int) (100f * rainDelta * rainDelta) / (client.gameSettings.particles == ParticleStatus.DECREASED? 2 : 1);

            for (int j = 0; j < tries; ++j)
            {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos altitude = world.getHeight(Heightmap.Type.MOTION_BLOCKING, view.add(k, 0, l)).down();
                if (altitude.getY() > 0 && altitude.getY() <= view.getY() + 10 && altitude.getY() >= view.getY() - 10)
                {
                    at = altitude;
                    break;
                }
            }

            if (at != null && world.getBiome(view).getRegistryName().equals(WRWorld.TINCTURE_WEALD.func_240901_a_()) && random.nextInt(20) < weatherTime++)
            {
                weatherTime = 0;
                if (at.getY() > view.getY() + 1 && world.getHeight(Heightmap.Type.MOTION_BLOCKING, view).getY() > MathHelper.floor((float) view.getY()))
                {
                    client.world.playSound(at, WRSounds.WEATHER_SANDSTORM.get(), SoundCategory.WEATHER, 0.1f, 0.5f, false);
                }
                else
                {
                    client.world.playSound(at, WRSounds.WEATHER_SANDSTORM.get(), SoundCategory.WEATHER, 0.2f, 1f, false);
                }
            }
        }
    }

    // woulda put it in static block but class loading :)
    public static void init()
    {
        try
        {
            Object2ObjectMap<ResourceLocation, DimensionRenderInfo> infoMap = ObfuscationReflectionHelper.getPrivateValue(DimensionRenderInfo.class, null, "field_239208_a_");
            infoMap.put(WRWorld.THE_WYRMROOST.func_240901_a_(), new WRDimensionRenderInfo());
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not apply Wyrmroost Dimension Render info.\n" + e);
        }
    }
}
