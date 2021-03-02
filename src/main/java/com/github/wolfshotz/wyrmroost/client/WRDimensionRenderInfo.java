package com.github.wolfshotz.wyrmroost.client;

import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class WRDimensionRenderInfo extends DimensionRenderInfo
{
    public WRDimensionRenderInfo()
    {
        super(128f, true, DimensionRenderInfo.FogType.NORMAL, false, false);
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

    private void renderWeatherParticles(int ticks, ClientWorld world, Minecraft client, ActiveRenderInfo renderInfo)
    {

    }

    // woulda put it in static block but class loading :)
    public static void init()
    {
        try
        {
            Object2ObjectMap<ResourceLocation, DimensionRenderInfo> renderMap = ObfuscationReflectionHelper.getPrivateValue(DimensionRenderInfo.class, null, "field_239208_a_");
            renderMap.put(WRWorld.THE_WYRMROOST.func_240901_a_(), new WRDimensionRenderInfo());
        }
        catch (Throwable e)
        {
            throw new RuntimeException("Could not apply Wyrmroost Dimension Render info.\n" + e);
        }
    }
}
