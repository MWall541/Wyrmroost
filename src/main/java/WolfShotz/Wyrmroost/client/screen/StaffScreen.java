package WolfShotz.Wyrmroost.client.screen;

import WolfShotz.Wyrmroost.client.screen.widgets.NameFieldWidget;
import WolfShotz.Wyrmroost.client.screen.widgets.StaffActionButton;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class StaffScreen extends Screen
{
    public final AbstractDragonEntity dragon;
    public final List<String> toolTip = Lists.newArrayList();
    public final List<StaffAction> actions = Lists.newArrayList();

    public StaffScreen(AbstractDragonEntity dragon)
    {
        super(new StringTextComponent("Staff Screen"));
        this.dragon = dragon;
    }

    @Override
    protected void init()
    {
        actions.clear();
        toolTip.clear();
        dragon.addScreenInfo(this);

        addButton(new NameFieldWidget(font, (width / 2) - 40, (height / 2) + 25, 100, 12, dragon));

        initActions();
    }

    public void initActions()
    {
        int radius = Math.max(actions.size() * 20, 100);
        for (int i = 0; i < actions.size(); i++)
        {
            StaffAction action = actions.get(i);
            String name = new TranslationTextComponent(action.getTranslateKey(dragon)).getUnformattedComponentText();
            double deg = 2 * Math.PI * i / actions.size() - Math.toRadians(90);
            int x = (int) (radius * Math.cos(deg));
            int y = (int) (radius * Math.sin(deg));
            x += (width / 2) - 50;
            y += (height / 2) - 10;
            addButton(new StaffActionButton(x, y, name, action));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderBackground();
        buttons.forEach(b -> b.render(mouseX, mouseY, partialTicks));
        int x = width / 2;
        int y = height / 2;

        int scale = (int) (dragon.getWidth() * dragon.getHeight()) * 2;

        InventoryScreen.drawEntityOnScreen(x, y + 20, scale, x - mouseX, y - mouseY, dragon);
        if (mouseX >= x - 35 && mouseY >= y - 45 && mouseX < x + 35 && mouseY < y + 15)
            renderTooltip(toolTip, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() { return false; }

    public void addAction(StaffAction action) { actions.add(action); }

    public static void open(AbstractDragonEntity dragon, ItemStack stack)
    {
        DragonStaffItem.bindDragon(dragon, stack);
        if (dragon.world.isRemote) Minecraft.getInstance().displayGuiScreen(new StaffScreen(dragon));
    }
}
