package WolfShotz.Wyrmroost.content.io.screen.modbook;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeModel;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerModel;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderModel;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class DragonIndexButton extends AbstractButton
{
    public static final List<DragonIndexButton> BUTTONS = new ArrayList<>();
    public final TarragonTomeScreen SCREEN;
    private final List<String> NAME = new ArrayList<>();
    private final int PAGE_TO_SET;
    public final boolean UNLOCKED;
    private float ticker;
    
    public DragonIndexButton(int x, int y, TarragonTomeScreen screen, int pageToSet, String name, boolean unlocked)
    {
        super(x, y, 25, 25, "");
        this.SCREEN = screen;
        this.PAGE_TO_SET = pageToSet;
        this.NAME.add(name);
        this.UNLOCKED = unlocked;
        BUTTONS.add(this);
    }
    
    @Override
    public void onPress()
    {
        if (UNLOCKED) SCREEN.updatePages(PAGE_TO_SET);
    }
    
    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks)
    {
        final float tick = ++ticker + partialTicks;
        
        GlStateManager.pushMatrix();

        if (isHovered && UNLOCKED)
            GuiUtils.drawHoveringText(NAME, mouseX, mouseY, width, height, -1, Minecraft.getInstance().fontRenderer);
//        GlStateManager.disableLighting();

        if (!UNLOCKED) GlStateManager.color4f(0, 0, 0, 1f);
        GlStateManager.translated(x + 15, y + 7, 50d);
        renderModel(tick / 2);
        
        GlStateManager.disableLighting();

        GlStateManager.popMatrix();

    }
    
    public void playDownSound(SoundHandler soundHandler)
    {
        if (UNLOCKED) soundHandler.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f));
    }
    
    public abstract void renderModel(float tick);
    
    
    public static DragonIndexButton owDrakeButton(int x, int y, TarragonTomeScreen screen, int pageToSet, boolean unlocked)
    {
        final OWDrakeModel MODEL = new OWDrakeModel();
        final ResourceLocation TEXTURE = ModUtils.resource("textures/entity/dragon/owdrake/male_com.png");
        
        return new DragonIndexButton(x, y, screen, pageToSet, "Overworld Drake", unlocked)
        {
            
            @Override
            public void renderModel(float tick)
            {
                GlStateManager.scalef(-20, 20, 20);
                GlStateManager.rotatef(160f, 0, 1, 0);
                SCREEN.bindTexture(TEXTURE);
                MODEL.body1.render(0.0625f);
                MODEL.resetToDefaultPose();
                if (isHovered && UNLOCKED) MODEL.animatieIdle(tick);
            }
        };
    }
    
    public static DragonIndexButton silverGliderButton(int x, int y, TarragonTomeScreen screen, int pageToSet, boolean unlocked)
    {
        final SilverGliderModel MODEL = new SilverGliderModel();
        final ResourceLocation TEXTURE = ModUtils.resource("textures/entity/dragon/silverglider/female.png");
        
        return new DragonIndexButton(x, y, screen, pageToSet, "SilverGlider", unlocked)
        {
            @Override
            public void renderModel(float tick)
            {
                GlStateManager.translated(0, 10, 0);
                GlStateManager.scalef(-10, 10, 10);
                GlStateManager.rotatef(160f, 0, 1, 0);
                SCREEN.bindTexture(TEXTURE);
                MODEL.mainbody.offsetY = -1f;
                MODEL.defaultGroundPose();
                MODEL.mainbody.render(0.0625f);
                MODEL.resetToDefaultPose();
                if (isHovered && UNLOCKED) MODEL.idleAnim(tick);
            }
        };
    }
    
    public static DragonIndexButton roostStalkerButton(int x, int y, TarragonTomeScreen screen, int pageToSet, boolean unlocked)
    {
        final RoostStalkerModel MODEL = new RoostStalkerModel();
        final ResourceLocation TEXTURE = ModUtils.resource("textures/entity/dragon/rooststalker/body.png");
        
        return new DragonIndexButton(x, y, screen, pageToSet, "Roost Stalker", unlocked)
        {
            
            @Override
            public void renderModel(float tick)
            {
                GlStateManager.scalef(-15, 15, 15);
                GlStateManager.rotatef(160f, 0, 1, 0);
                SCREEN.bindTexture(TEXTURE);
                MODEL.torso.render(0.0625f);
                MODEL.resetToDefaultPose();
                if (isHovered && UNLOCKED) MODEL.idle(tick, true);
            }
        };
    }
}
