package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

public class DebugRendering
{
    public static Shape now;

    public static void render(MatrixStack ms, float partialTicks)
    {
        if (now != null)
        {
            Vector3d pos = now.position;
            RenderHelper.drawShape(ms, now.shape, pos.x, pos.y, pos.z, now.color);
            if (--now.ticks <= 0) now = null;
        }
    }

    public static void shape(VoxelShape shape, Vector3d pos, int argb, int ticks)
    {
        Wyrmroost.LOG.info("Rendering at {} for {} ticks", pos, ticks);
        now = new Shape(shape, pos, argb, ticks);
    }

    public static void box(AxisAlignedBB aabb, int argb, int ticks)
    {
        shape(VoxelShapes.create(aabb), Vector3d.ZERO, argb, ticks);
    }

    public static void conjoined(int color, int ticks, BlockPos... cubes)
    {
        if (cubes.length == 0) return;

        VoxelShape shape = VoxelShapes.block();
        BlockPos initial = cubes[0];
        for (int i = 1; i < cubes.length; ++i)
        {
            BlockPos cube = cubes[i];
            Vector3i offset = cube.subtract(initial);
            shape = VoxelShapes.or(shape, VoxelShapes.block().move(offset.getX(), offset.getY(), offset.getZ()));
        }
        shape(shape, Vector3d.atLowerCornerOf(initial), color, ticks);
    }

    private static class Shape
    {
        final VoxelShape shape;
        final Vector3d position;
        final int color;
        int ticks;

        public Shape(VoxelShape shape, Vector3d position, int color, int ticks)
        {
            this.shape = shape;
            this.position = position;
            this.color = color;
            this.ticks = ticks;
        }
    }
}
