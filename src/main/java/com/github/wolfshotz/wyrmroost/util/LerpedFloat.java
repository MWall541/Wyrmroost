package com.github.wolfshotz.wyrmroost.util;

import net.minecraft.util.math.MathHelper;

public class LerpedFloat
{
    protected float current;
    protected float previous;

    public LerpedFloat()
    {
        current = previous = 0;
    }

    public LerpedFloat(float start)
    {
        current = previous = start;
    }

    public float get(float x)
    {
        return Mafs.linTerp(previous, current, x);
    }

    public float get()
    {
        return current;
    }

    public void set(float value)
    {
        sync();
        current = value;
    }

    public void add(float value)
    {
        sync();
        current += value;
    }

    public void sync()
    {
        previous = current;
    }

    public float getPrevious()
    {
        return previous;
    }

    public static LerpedFloat.Clamped unit()
    {
        return new Clamped(0, 1);
    }

    /**
     * Clamped Implementation.
     * Basically just ensure that the value stays clamped within the specified {@link Clamped#min}-{@link Clamped#max} bounds.
     */
    public static class Clamped extends LerpedFloat
    {
        private final float min;
        private final float max;

        public Clamped(float start, float min, float max)
        {
            super(MathHelper.clamp(start, min, max));
            this.min = min;
            this.max = max;
        }

        public Clamped(float min, float max)
        {
            this(0, min, max);
        }

        @Override
        public void set(float value)
        {
            super.set(MathHelper.clamp(value, min, max));
        }

        @Override
        public void add(float value)
        {
            super.add(value);
            current = MathHelper.clamp(current, min, max);
        }

        public float getMin()
        {
            return min;
        }

        public float getMax()
        {
            return max;
        }
    }
}
