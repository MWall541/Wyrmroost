package WolfShotz.Wyrmroost.util;

import java.util.function.Consumer;

public class CallbackHandler<T>
{
    private Consumer<T> callBack;

    public CallbackHandler(Consumer<T> initial) { this.callBack = initial; }

    public CallbackHandler() {}

    public void then(Consumer<T> next)
    {
        if (callBack == null) this.callBack = next;
        else this.callBack = callBack.andThen(next);
    }

    public void clear() { this.callBack = null; }

    public void accept(T t) { if (callBack != null) callBack.accept(t); }

    public void acceptAndClear(T t)
    {
        accept(t);
        clear();
    }
}
