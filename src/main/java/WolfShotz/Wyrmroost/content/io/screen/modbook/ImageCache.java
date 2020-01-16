package WolfShotz.Wyrmroost.content.io.screen.modbook;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ImageCache
{
    public final ResourceLocation loc;
    public final int posX, posY, pngWidth, pngHeight;
    
    public ImageCache(ResourceLocation loc, int posX, int posY, int pngWidth, int pngHeight)
    {
        this.loc = loc;
        this.posX = posX;
        this.posY = posY;
        this.pngWidth = pngWidth;
        this.pngHeight = pngHeight;
    }
}
