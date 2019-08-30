package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class RandomFlightGoal extends AbstractFlightGoal
{
    private final int SWITCH_PATH_THRESHOLD = 850;
    private final int LAND_THRESHOLD = 1500;
    
    private int changeDirInterval;
    
    public RandomFlightGoal(SilverGliderEntity glider) {
        super(glider);
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }
    
    
    @Override
    public void tick() {
        --changeDirInterval;
        
        if (rand.nextInt(LAND_THRESHOLD) == 0 && !isDescending()) {
            currentFlightAction = FlightFlag.DESCEND;
            glider.getNavigator().clearPath();
            glider.setMotion(Vec3d.ZERO);
        }
        
        if (isDescending()) {
            glider.getNavigator().tryMoveToXYZ(glider.getPosition().getX() + 0.5d, glider.getPosition().getY() - 4, glider.getPosition().getZ(), 1);
            
            return;
        }
        
//        if (rand.nextInt(SWITCH_PATH_THRESHOLD) == 0) {
//            switchFlightFlag();
//            if (isCircling()) circlePoint = glider.getPosition();
//        }
        
//        if (isCircling()) circlePoint();
        /*else*/ if (changeDirInterval <= 0) {
            changeDirInterval = 80;
            
            double x = currentPos.getX() + (rand.nextFloat() * 2f - 1f) * 15f;
            double y = currentPos.getY() + (rand.nextFloat() * 1.3f + 1f);
            System.out.println(y);
            double z = currentPos.getZ() + (rand.nextFloat() * 2f - 1f) * 15f;
            glider.getNavigator().tryMoveToXYZ(x, y, z, 1);
        }
    }
}
