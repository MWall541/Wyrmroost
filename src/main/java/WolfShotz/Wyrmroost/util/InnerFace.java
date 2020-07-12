package WolfShotz.Wyrmroost.util;

public interface InnerFace extends Runnable
{
    default InnerFace andThen(InnerFace innerFace)
    {
        return () ->
        {
            run();
            innerFace.run();
        };
    }
}
