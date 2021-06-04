package com.github.wolfshotz.wyrmroost.containers.util;

import com.github.wolfshotz.wyrmroost.client.screen.DragonStaffScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

import static com.github.wolfshotz.wyrmroost.client.ClientEvents.getClient;

public class AccessorySlot extends DynamicSlot
{
    public final int anchorZ;
    @Nullable public final Vec2d uv;

    public AccessorySlot(IItemHandler inventory, int index, int x, int y, int z, Vec2d uv)
    {
        super(inventory, index, x, y);
        this.anchorZ = z;
        this.uv = uv;
    }

    public AccessorySlot(IItemHandler inventory, int index, int x, int y, int z)
    {
        this(inventory, index, x, y, z, null);
    }

    @Override
    public boolean isActive()
    {
        return super.isActive() && getClient().screen instanceof DragonStaffScreen && ((DragonStaffScreen) getClient().screen).showAccessories();
    }

    public void blitShadowIcon(Screen screen, MatrixStack ms, int x, int y)
    {
        if (uv != null && !hasItem())
            screen.blit(ms, x, y, (int) uv.x, (int) uv.y, 16, 16);
    }
}
