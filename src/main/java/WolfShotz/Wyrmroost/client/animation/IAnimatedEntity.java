package WolfShotz.Wyrmroost.client.animation;

public interface IAnimatedEntity
{
    Animation NO_ANIMATION = new Animation(0);

    int getAnimationTick();

    void setAnimationTick(int tick);

    Animation getAnimation();

    void setAnimation(Animation animation);

    Animation[] getAnimations();

    default boolean noActiveAnimation() { return getAnimation() == NO_ANIMATION; }
}
