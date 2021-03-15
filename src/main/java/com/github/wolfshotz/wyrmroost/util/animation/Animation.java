package com.github.wolfshotz.wyrmroost.util.animation;

public class Animation
{
    private final int duration;

    public Animation(int duration)
    {
        this.duration = duration;
    }

    public int getDuration()
    {
        return duration;
    }

    @Override
    public String toString()
    {
        if (this == IAnimatable.NO_ANIMATION) return "Animation{NONE}";
        return "Animation{duration=" + duration + '}';
    }
}
