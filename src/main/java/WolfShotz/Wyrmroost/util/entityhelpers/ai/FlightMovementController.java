package WolfShotz.Wyrmroost.util.entityhelpers.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.NetworkUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * Created by WolfShotz 7/31/19 - 19:20
 *
 * Class Responsible for handling the movement of dragons while flying
 * If The Dragon is on the ground however, just use the vanilla movement controller.
 */
public class FlightMovementController extends MovementController
{
    private AbstractDragonEntity dragon;
    public int courseCooldown;

    public FlightMovementController(AbstractDragonEntity entity) {
        super(entity);
        this.dragon = entity;
    }

    @Override
    public void tick() {
        // Handle Vanilla movement if not flying
        if (!dragon.isFlying()) {
            super.tick();
            return;
        }
        
        if (action == Action.MOVE_TO) {
            if (--courseCooldown <= 0) {
                double x = posX - mob.posX;
                double y = posY - mob.posY;
                double z = posZ - mob.posZ;
                double euclid = MathHelper.sqrt(MathUtils.getSpaceDistSq(mob.posX, posX, mob.posY, posY, mob.posZ, posZ));
    
                if (euclid < (double) 2.5000003E-7F) { // Too small of a move target, dont move
                    action = Action.WAIT;
                    return;
                }
    
                if (isNotColliding(posX, posY, posZ, euclid)) {
                    mob.addVelocity(x / euclid * 0.1d, y / euclid * 0.1d, z / euclid * 0.1d);
                    courseCooldown = new Random().nextInt(5) + 3;
                } else action = Action.WAIT;
            }
    
            dragon.rotationYaw = -((float) MathHelper.atan2(dragon.getMotion().x, dragon.getMotion().z)) * (180F / MathUtils.PI);
            dragon.renderYawOffset = dragon.rotationYaw;
            dragon.getLookController().setLookPosition(posX, posY, posZ, 30, 30);
            
            Vec3d vec3d = dragon.getLookVec();
            Vec3d motion = dragon.getMotion();
            float f = dragon.rotationPitch * 0.017453292F;
            double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
            double d8 = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            double d1 = vec3d.length();
            double f4 = MathHelper.cos(f);
            f4 = f4 * f4 * Math.min(1.0D, d1 / 0.4D);
            dragon.addVelocity(0, f4 * 0.06d, 0);
    
            if (dragon.getMotion().y < 0.0D && d6 > 0.0D) {
                double d2 = dragon.getMotion().y * -0.1D * f4;
                dragon.addVelocity(vec3d.x * d2 / d6, d2, vec3d.z * d2 / d6);
            }
    
            if (f < 0.0F) {
                double d10 = d8 * (double) (-MathHelper.sin(f)) * 0.04D;
                dragon.addVelocity(-(vec3d.x * d10 / d6), d10 * 2.4d, -(vec3d.z * d10 / d6));
            }
    
            if (d6 > 0.0D)
                dragon.addVelocity(vec3d.x / d6 * d8 - dragon.getMotion().x, 0, vec3d.z / d6 * d8 - dragon.getMotion().z);
            
            double planeMot = 0.9900000095367432d;
            dragon.setMotion(dragon.getMotion().mul(planeMot, 0.9800000190734863d, planeMot));
        }
    }
    
    /**
     * Checks if entity bounding box is not colliding with terrain
     */
    private boolean isNotColliding(double x, double y, double z, double offset)
    {
        double x1 = (x - mob.posX) / offset;
        double y1 = (y - mob.posY) / offset;
        double z1 = (z - mob.posZ) / offset;
        AxisAlignedBB axisalignedbb = mob.getBoundingBox();
        
        for (int i = 1; (double) i < offset; ++i)
            if (mob.world.checkBlockCollision(axisalignedbb.offset(x1, y1, z1))) return false;
        
        return true;
    }
}
