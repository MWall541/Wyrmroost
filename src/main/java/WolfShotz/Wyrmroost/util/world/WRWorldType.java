package WolfShotz.Wyrmroost.util.world;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.ZoomLayer;

import java.util.function.LongFunction;

public class WRWorldType extends WorldType
{
    public static final WRWorldType WYRMROOST = new WRWorldType("wyrmroost");

    public WRWorldType(String name)
    {
        super(name);
    }

    @Override
    public <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, OverworldGenSettings chunkSettings, LongFunction<C> contextFactory)
    {
        parentLayer = (new WRBiomeLayer(chunkSettings)).apply(contextFactory.apply(200L), parentLayer);
        parentLayer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, parentLayer, 2, contextFactory);
        return parentLayer;
    }
}
