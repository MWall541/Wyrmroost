package WolfShotz.Wyrmroost.entities.util.animation;

public interface IAnimatable
{
    Animation NO_ANIMATION = new Animation(0)
    {
        @Override
        public String toString() { return "NO_ANIMATION"; }
    };

    int getAnimationTick();

    void setAnimationTick(int tick);

    Animation getAnimation();

    void setAnimation(Animation animation);

    Animation[] getAnimations();

    default boolean noActiveAnimation() { return getAnimation() == NO_ANIMATION; }

    default void updateAnimations()
    {
        Animation current = getAnimation();
        if (current != NO_ANIMATION)
        {
            int tick = getAnimationTick() + 1;
            if (tick >= current.getDuration())
            {
                setAnimation(NO_ANIMATION);
                tick = 0;
            }
            setAnimationTick(tick);
        }
    }
}
