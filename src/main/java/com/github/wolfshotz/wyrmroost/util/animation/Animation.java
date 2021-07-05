package com.github.wolfshotz.wyrmroost.util.animation;

import net.minecraft.client.renderer.model.Model;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Animation<T extends IAnimatable, M extends Model>
{
    private final int duration;
    private final BiConsumer<T, Integer> logic;
    private final Consumer<M> animLogic;

    private Animation(int duration, @Nullable BiConsumer<T, Integer> logic, Consumer<M> animLogic)
    {
        //@formatter:off
        this.duration = duration;
        this.logic = logic == null? (t, i) -> {} : logic;
        this.animLogic = animLogic;
        //@formatter:on
    }

    public static <T extends IAnimatable, M extends Model> Animation<T, M> create(int duration, @Nullable BiConsumer<T, Integer> logic, Consumer<M> animLogic)
    {
        return new Animation<>(duration, logic, animLogic);
    }

    @SuppressWarnings("unchecked")
    public void tick(IAnimatable animatable, int time)
    {
        logic.accept((T) animatable, time);
    }

    @SuppressWarnings("unchecked")
    public void animate(Model model)
    {
        animLogic.accept((M) model);
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
