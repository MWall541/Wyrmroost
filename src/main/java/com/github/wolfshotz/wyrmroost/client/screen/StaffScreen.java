package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.client.screen.widgets.NameFieldWidget;
import com.github.wolfshotz.wyrmroost.client.screen.widgets.StaffActionButton;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class StaffScreen extends Screen
{
    private final AbstractDragonEntity dragon;
    public final List<String> toolTip = new ArrayList<>();
    public final List<StaffAction> actions = new ArrayList<>();

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

        addButton(new NameFieldWidget(font, (width / 2) - 63, (height / 2) + 25, 120, 12, dragon));

        initActions();
    }

    public void initActions()
    {
        int size = actions.size();
        int radius = Math.max(size * 20, 100);
        for (int i = 0; i < size; i++)
        {
            StaffAction action = actions.get(i);
            ITextComponent name = action.getTranslation(dragon);
            double deg = 2 * Math.PI * i / size - Math.toRadians(90);
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
        for (Widget b : buttons) b.render(mouseX, mouseY, partialTicks);
        int x = width / 2;
        int y = (height / 2) + (int) (dragon.getHeight() / 2);

        if (dragon.getVariant() < 0)
            drawString(font, Character.toString('\u2726'), x - 40, y - 40, 0xffff00);

        int scale = (int) -(dragon.getWidth() * dragon.getHeight()) + 23; // linear decay: smaller scale bigger the dragon. if things get problematic, exponential?
        InventoryScreen.drawEntityOnScreen(x, y, scale, x - mouseX, y - mouseY, dragon);
        if (mouseX >= x - 40 && mouseY >= y - 40 && mouseX < x + 45 && mouseY < y + 15)
            renderTooltip(toolTip, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() { return false; }

    public void addAction(StaffAction action) { actions.add(action); }

    public void addTooltip(String string) { toolTip.add(string); }

    public static void open(AbstractDragonEntity dragon, ItemStack stack)
    {
        DragonStaffItem.bindDragon(dragon, stack);
        if (dragon.world.isRemote) Minecraft.getInstance().displayGuiScreen(new StaffScreen(dragon));
    }
}
