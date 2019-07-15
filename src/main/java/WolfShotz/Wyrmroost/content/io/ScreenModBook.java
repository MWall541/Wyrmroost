package WolfShotz.Wyrmroost.content.io;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.util.ResourceLocation;

public class ScreenModBook extends Screen
{
    private ResourceLocation ioLoc = ModUtils.location("textures/io/modbook/tome.png");
    private int pageNumber = 0;
    private ChangePageButton next;
    private ChangePageButton back;

    public ScreenModBook() {
        super(ModUtils.translation("Tarragon Tome"));
    }

    @Override
    protected void init() {
        addButtons();
        back.visible = false;
    }

    private void addButtons() {
        addButton(new Button(width / 2 - 100, 196, 200, 20, ModUtils.format("io.modbook.close"), func -> minecraft.displayGuiScreen(null)));
        // Notes bcus its still obfuscated: ChangePageButton((int) x locale, (int) y locale, (boolean) true = right | false = left, (lambda) what does this button do?, (boolean) plays sound?)
        next = addButton(new ChangePageButton(width / 2 + 100, 147, true, func -> updatePages(true), true));
        back = addButton(new ChangePageButton(width / 2 - 124, 147, false, func -> updatePages(false), true));
    }

    private void updatePages(boolean increment) {
        int numOfPages = 10; //TODO: PLACEHOLDER FOR updatePages() (We dont know the amount of pages yet...)
        if (increment && pageNumber < numOfPages) ++pageNumber;
        else if (pageNumber > 0) --pageNumber;
        next.visible = pageNumber < numOfPages;
        back.visible = pageNumber > 0;
    }

    @Override
    public void render(int param1, int param2, float param3) {
        renderBackground();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(this.ioLoc);
        blit((width - 256) / 2, 5, 0, 0, 256, 192);
        super.render(param1, param2, param3);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}