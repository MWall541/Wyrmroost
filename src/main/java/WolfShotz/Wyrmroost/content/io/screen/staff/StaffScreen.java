package WolfShotz.Wyrmroost.content.io.screen.staff;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.items.DragonStaffItem;
import WolfShotz.Wyrmroost.util.io.NameFieldWidget;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class StaffScreen extends Screen
{
    public final AbstractDragonEntity dragon;

    public StaffScreen(AbstractDragonEntity dragon)
    {
        super(new StringTextComponent("Staff Screen"));
        this.dragon = dragon;
    }

    public static void open(AbstractDragonEntity dragon, ItemStack stack, DragonStaffItem staff)
    {
        staff.boundDragon(dragon, stack);
        if (dragon.world.isRemote) Minecraft.getInstance().displayGuiScreen(new StaffScreen(dragon));
    }

    @Override
    protected void init()
    {
        addButton(new NameFieldWidget(font, (width / 2) - 40, (height / 2) + 25, 100, 12, dragon));
        addButton(new StaffModeButton(40, 30, 100, 15, "Set Home Position", DragonStaffItem.Action.HOME_POS));
    }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderBackground();
        buttons.forEach(b -> b.render(mouseX, mouseY, partialTicks));
        int x = width / 2;
        int y = height / 2;


        InventoryScreen.drawEntityOnScreen(x, y + 20, 20, x - mouseX, y - mouseY, dragon);
        if (mouseX >= x - 35 && mouseY >= y - 45 && mouseX < x + 35 && mouseY < y + 15)
        {
            List<String> info = Lists.newArrayList();
            info.add("Owner: " + dragon.getOwner().getName().getUnformattedComponentText());
            info.add(String.format("Health: %s / %s", (int) dragon.getHealth(), (int) dragon.getMaxHealth()));
            renderTooltip(info, mouseX, mouseY);
        }
    }
}
