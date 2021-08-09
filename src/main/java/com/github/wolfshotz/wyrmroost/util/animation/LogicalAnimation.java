package com.github.wolfshotz.wyrmroost.util.animation;

import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.model.Model;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class LogicalAnimation<T extends IAnimatable, M extends Model> extends Animation
{
    private final BiConsumer<T, Integer> logic;
    private final Consumer<M> animLogic;

    public LogicalAnimation(int duration, @Nullable BiConsumer<T, Integer> logic, Supplier<Consumer<M>> animLogic)
    {
        super(duration);
        //@formatter:off
        this.logic = logic == null? (t, i) -> {} : logic;
        //@formatter:on
        this.animLogic = ModUtils.isClient() && animLogic != null? animLogic.get() : null;
    }

    public static <T extends IAnimatable, M extends Model> Animation create(int duration, @Nullable BiConsumer<T, Integer> logic, Supplier<Consumer<M>> animLogic)
    {
        return new LogicalAnimation<>(duration, logic, animLogic);
    }

    @Override
    public void tick(IAnimatable animatable, int time)
    {
        logic.accept((T) animatable, time);
    }

    @Override
    public void animate(Model model)
    {
        animLogic.accept((M) model);
    }
}
