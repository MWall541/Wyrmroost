package WolfShotz.Wyrmroost.util;

import java.util.Arrays;

public class FloatBuffer
{
    private final float[] buffer;
    private int index = 0;

    public FloatBuffer(int size)
    {
        this.buffer = new float[size];
    }

    public void fill(float value)
    {
        Arrays.fill(buffer, value);
    }

    public void next(float value)
    {
        index++;
        index %= buffer.length; // reset the index to the beginning
        buffer[index] = value;
    }

    public float get(int index)
    {
        return buffer[index & buffer.length - 1];
    }
}
