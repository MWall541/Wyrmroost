package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.content.entities.sliverglider.SilverGliderEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

public class ForgeEvents
{
    public static void fix3rdPersonCamera(EntityViewRenderEvent.FOVModifier event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance)) {
/*            if (mc.gameSettings.thirdPersonView == 1) GL11.glTranslatef(0, -0.5f, -0.5f);
            if (mc.gameSettings.thirdPersonView == 2) GL11.glTranslatef(0, -0.2f, -0.2f);*/
            return;
        }
    }
}
