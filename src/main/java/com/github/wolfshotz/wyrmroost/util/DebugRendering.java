package com.github.wolfshotz.wyrmroost.util;

import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.AxisAlignedBB;

public class DebugRendering
{
    public static Box box;

    public static void render(MatrixStack ms, float partialTicks)
    {
        if (box != null)
        {
            RenderHelper.drawAABB(ms, box.aabb, box.color);
            if (--box.time <= 0) box = null;
        }
    }

    public static void box(AxisAlignedBB aabb, int color, int time)
    {
        box = new Box(aabb, color, time);
    }

    private static class Box
    {
        final AxisAlignedBB aabb;
        final int color;
        int time;

        public Box(AxisAlignedBB box, int color, int time)
        {
            this.aabb = box;
            this.color = color;
            this.time = time;
        }
    }
}
