package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeModel;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TranslationUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ScreenModBook extends Screen
{
    private static final OWDrakeModel DRAKE_MODEL = new OWDrakeModel();
    
    private ResourceLocation ioLoc = ModUtils.location("textures/io/modbook/tome.png");
    private int pageNumber = 0;
    private ChangePageButton next, back;
    private float ticker;

    public ScreenModBook() { super(TranslationUtils.translation("Tarragon Tome")); }

    @Override
    protected void init() {
        addButtons();
        back.visible = false;
    }

    private void addButtons() {
        addButton(new Button(width / 2 - 100, 196, 200, 20, TranslationUtils.format("io.modbook.close"), func -> minecraft.displayGuiScreen(null)));
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
        super.render(param1, param2, param3);
        ModUtils.L.info(param3);
        
        ++ticker;
        renderBackground();
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    
        GL11.glPushMatrix();
        bindTexture(ioLoc);
        blit((width - 256) / 2, 5, 0, 0, 256, 192);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        switch (pageNumber) {
            case 0: renderPage1();
            default:
        }
        GL11.glPopMatrix();
    }

    private void renderPage1() {
        GlStateManager.enableColorMaterial();
        GL11.glTranslated(100, 50, 50);
        GL11.glScalef(-20f, 20f, 20f);
        GL11.glRotated(150, 0, 1, 0);
        bindTexture(ModUtils.location(OWDrakeRenderer.DEF_LOC + "owdrake/male_com.png"));
        DRAKE_MODEL.body1.render(0.0625f);
        DRAKE_MODEL.resetToDefaultPose();
        DRAKE_MODEL.animatieIdle(ticker);
    }
    
    @Override
    public boolean isPauseScreen() { return false; }
    
    private void bindTexture(ResourceLocation resource) { minecraft.getTextureManager().bindTexture(resource); }
}