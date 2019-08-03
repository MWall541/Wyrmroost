package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.content.entities.sliverglider.SilverGliderEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeEvents
{
    @SubscribeEvent
    public void fix3rdPersonCamera(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getInstance();
        System.out.println("are we working.. or..");

        if (mc.player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance)) {
            if (mc.gameSettings.thirdPersonView == 1) GlStateManager.translated(0, 1d, 1d);
            if (mc.gameSettings.thirdPersonView == 2);
            return;
        }
    }
}
