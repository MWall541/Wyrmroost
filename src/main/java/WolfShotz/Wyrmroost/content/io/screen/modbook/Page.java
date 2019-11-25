package WolfShotz.Wyrmroost.content.io.screen.modbook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.antlr.v4.runtime.misc.Triple;

import java.util.ArrayList;
import java.util.List;
// blit(xPos, yPos, initialX pixel, initialY pixel, x width, y width, .png width, .png height);
@OnlyIn(Dist.CLIENT)
public class Page
{
    private final FontRenderer font;
    private final ItemRenderer itemRenderer;
    public List<String> lines = new ArrayList<>();
    public List<Triple<ItemStack, Integer, Integer>> items = new ArrayList<>();
    public List<ImageCache> images = new ArrayList<>();
    public ResourceLocation backgroundResource;
    
    public Page(FontRenderer font, ItemRenderer itemRenderer) {
        this.font = font;
        this.itemRenderer = itemRenderer;
    }
    
    public void render(int center, float partialTicks) {
        if (backgroundResource != null) {
            Minecraft.getInstance().getTextureManager().bindTexture(backgroundResource);
            AbstractGui.blit(center, 5, 0, 0, 256, 192, 256, 192);
        }
        if (!images.isEmpty()) {
            for (ImageCache imageGroup : images) {
                Minecraft.getInstance().getTextureManager().bindTexture(imageGroup.loc);
                AbstractGui.blit(imageGroup.posX, imageGroup.posY, 0, 0, imageGroup.pngWidth, imageGroup.pngHeight, imageGroup.pngWidth, imageGroup.pngHeight);
            }
        }
        if (!lines.isEmpty()) {
            for (int i = 0; i <= lines.size() - 1; ++i)
                font.drawSplitString(lines.get(i), center - 110, (11 * i) + 15, 120, 0x000000);
        }
        if (!items.isEmpty()) items.forEach(group -> itemRenderer.renderItemIntoGUI(group.a, center + group.b, group.c));
    }
    
    public void addLine(String text) { lines.add(text); }
    public void addItem(ItemStack item, int x, int y) { items.add(new Triple<>(item, x, y)); }
    public void addImage(ResourceLocation loc, int x, int y, int imgWidth, int imgHeight) { images.add(new ImageCache(loc, x, y, imgWidth, imgHeight)); }
}
