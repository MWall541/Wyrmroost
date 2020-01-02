package WolfShotz.Wyrmroost.content.io.screen.dragoninvscreens;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.io.NameFieldWidget;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.content.io.screen.base.ContainerScreenBase;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButterflyInvScreen extends ContainerScreenBase<ContainerBase<ButterflyLeviathanEntity>>
{
    public static final ResourceLocation BUTTERFLY_GUI = ModUtils.resource("textures/io/dragonscreen/butterflyinv.png");
    
    public ButterflyInvScreen(ContainerBase<ButterflyLeviathanEntity> container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        background = BUTTERFLY_GUI;
        xSize = 174;
        ySize = 164;
        textureWidth = 174;
        textureHeight = 164;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHealth(140, 2);
    }
    
    @Override
    public void renderEntity(int mouseX, int mouseY) { InventoryScreen.drawEntityOnScreen(guiLeft + 65, guiTop + 53, 9, (guiLeft + 65) - mouseX, (guiTop + 53) - mouseY, dragonInv.dragon); }
    
    @Override
    public NameFieldWidget createNameField() { return new NameFieldWidget(font, guiLeft + 6, guiTop, 80, 12, this); }
}
