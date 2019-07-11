package WolfShotz.Wyrmroost.content.io;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.util.ResourceLocation;

public class ScreenModBook extends Screen
{
    private ResourceLocation ioLoc = ModUtils.location("textures/io/modbook/background.png");
    private int pageNumber = 1;
    private ChangePageButton next;
    private ChangePageButton back;

    //TODO: PLACEHOLDER FOR updatePages() (We dont know the amount of pages yet...)
    private int NUM_OF_PAGES = 10;

    public ScreenModBook() {
        super(ModUtils.translation("Tarragon Tome"));
    }

    @Override
    protected void init() { addButtons(); }

    private void addButtons() {
        addButton(new Button(width / 2 - 100, 196, 200, 20, ModUtils.format("io.modbook.close"), func -> minecraft.displayGuiScreen(null)));
        // Notes bcus its still obfuscated: ChangePageButton((int) x locale, (int) y locale, (boolean) true = right | false = left, (lambda) what does this button do?, (boolean) plays sound?)
        next = addButton(new ChangePageButton(width / 2 + 80, 159, true, func -> updatePages(true), true));
        back = addButton(new ChangePageButton(width / 2 - 93, 159, false, func -> updatePages(false), true));
    }

    private void updatePages(boolean increment) {
        if (increment && pageNumber < NUM_OF_PAGES) ++pageNumber;
        else if (pageNumber > 1) --pageNumber;
        next.visible = pageNumber < NUM_OF_PAGES;
        back.visible = pageNumber > 1;
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void render(int param1, int param2, float param3) {
        renderBackground();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        minecraft.getTextureManager().bindTexture(this.ioLoc);
        minecraft.fontRenderer.drawString(Integer.toString(pageNumber), width / 2 + 90, 25, 0x000000);
        super.render(param1, param2, param3);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}