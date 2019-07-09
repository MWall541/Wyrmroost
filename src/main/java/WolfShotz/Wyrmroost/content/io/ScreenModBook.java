package WolfShotz.Wyrmroost.content.io;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class ScreenModBook extends Screen
{
    private ResourceLocation ioLoc = ModUtils.location("textures/io/modbook/background.png");

    public ScreenModBook() {
        super(ModUtils.translation("Tarragon Tome")); //TODO
    }

    @Override
    public void render(int param1, int param2, float param3) {
        super.render(param1, param2, param3);
        renderBackground();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(this.ioLoc);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}