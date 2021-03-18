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
    public Vector3d getBrightnessDependentFogColor(Vector3d color, float density)
    {
        return color.multiply(density * 0.94F + 0.06F, density * 0.94F + 0.06F, density * 0.91F + 0.09F);
    }

    @Override
    public boolean isFoggyAt(int p_230493_1_, int p_230493_2_)
    {
        return false;
    }

    private void renderWeather(int ticks, float partialTicks, ClientWorld world, Minecraft minecraft, LightTexture lightTexture, double x, double y, double z)
    {

    }

    private void renderWeatherParticles(int ticks, ClientWorld level, Minecraft client, ActiveRenderInfo renderInfo)
    {
        float rainDelta = client.level.getRainLevel(1f) / (Minecraft.useFancyGraphics()? 1f : 2f);
        if (rainDelta > 0)
        {
            Random random = new Random(ticks * 312987231L);
            BlockPos view = new BlockPos(renderInfo.getBlockPosition());
            BlockPos at = null;
            int tries = (int) (100f * rainDelta * rainDelta) / (client.options.particles == ParticleStatus.DECREASED? 2 : 1);

            for (int j = 0; j < tries; ++j)
            {
                int k = random.nextInt(21) - 10;
                int l = random.nextInt(21) - 10;
                BlockPos altitude = level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, view.offset(k, 0, l)).below();
                if (altitude.getY() > 0 && altitude.getY() <= view.getY() + 10 && altitude.getY() >= view.getY() - 10)
                {
                    at = altitude;
                    break;
                }
            }

            if (at != null && level.getBiome(view).getRegistryName().equals(WRWorld.TINCTURE_WEALD.location()) && random.nextInt(20) < weatherTime++)
            {
                weatherTime = 0;
                float pitch = 1f;
                if (at.getY() > view.getY() + 1 && level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, view).getY() > MathHelper.floor((float) view.getY()))
                    pitch = 0.5f;
                client.level.playLocalSound(at, WRSounds.WEATHER_SANDSTORM.get(), SoundCategory.WEATHER, 0.2f, pitch, false);
            }
        }
    }

    // woulda put it in static block but class loading :)
    public static void init()
    {
        try
        {
            Object2ObjectMap<ResourceLocation, DimensionRenderInfo> infoMap = ObfuscationReflectionHelper.getPrivateValue(DimensionRenderInfo.class, null, "field_239208_a_");
            infoMap.put(WRWorld.THE_WYRMROOST.location(), new WRDimensionRenderInfo());
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not apply Wyrmroost Dimension Render info.\n" + e);
        }
    }
}
