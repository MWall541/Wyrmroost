package WolfShotz.Wyrmroost.util.entityhelpers.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Credit to Bob Mowzie of Mowzies mobs.
 *
 * Created by Josh on 8/30/2018.
 */
public class DynamicChain
{
    private Vec3d[] p;
    private Vec3d[] v;
    private Vec3d[] a;
    private Vec3d[] F;
    private float[] m;
    private float[] d;
    private Vec3d[] T;
    private Vec3d[] r;
    private Vec3d[] rv;
    private Vec3d[] ra;
    private Entity entity;
    private Vec3d prevP;
    private Vec3d prevV;
    
    private Vec3d[] pOrig;
    private int prevUpdateTick;
    
    public DynamicChain(Entity entity) {
        this.entity = entity;
        p = new Vec3d[0];
        v = new Vec3d[0];
        a = new Vec3d[0];
        F = new Vec3d[0];
        m = new float[0];
        d = new float[0];
        T = new Vec3d[0];
        ra = new Vec3d[0];
        rv = new Vec3d[0];
        r = new Vec3d[0];
        pOrig = new Vec3d[0];
        prevUpdateTick = -1;
    }
    
    public void updateBendConstraint(float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        Vec3d[] prevPos = new Vec3d[p.length];
        Vec3d[] prevVel = new Vec3d[v.length];
        for (int i = 0; i < p.length; i++) {
            prevPos[i] = p[i];
            prevVel[i] = v[i];
        }
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length - 1; i++) {
                if (i == 0) {
                    Vec3d root = pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    
                    p[i] = root;
                    v[i] = p[i].subtract(prevPos[i]);
                    a[i] = v[i].subtract(prevVel[i]);
                }
                
                Vec3d target = angleBetween(
                        pOrig[i], //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()),
                        pOrig[i + 1]); //origModelRenderers[i + 1].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()));
                //float gravity = 1 - (float) Math.pow(1 - gravityAmount, (i + 1));
                //target = new Vec3d(target.x, (1-gravity) * target.y + gravity * Math.PI, target.z);
                
                r[i] = angleBetween(p[i], p[i + 1]);
                
                T[i] = wrapAngles(r[i].subtract(target)).scale(-stiffness/(Math.pow(i + 1, stiffnessFalloff)));
                double down = Math.PI/2;
                Vec3d gravityVec = wrapAngles(new Vec3d(
                        0,
                        gravityAmount * d[i+1] * m[i+1] * (Math.sin(down - r[i].y + down)),
                        0));
                Vec3d floorVec = new Vec3d(0, 1 * d[i+1] * m[i+1] * (Math.sin(Math.PI/2 - r[i].y + Math.PI/2)), 0);
                if (useFloor && entity.onGround && p[i+1].y < entity.posY) {
                    T[i] = T[i].subtract(floorVec);
                }
                T[i] = wrapAngles(T[i].add(gravityVec));
                ra[i] = T[i].scale(1 / (m[i + 1] * d[i + 1] * d[i + 1]));
                rv[i] = rv[i].add(ra[i].scale(1/((float)numUpdates))).scale(damping);
                rv[i] = wrapAngles(rv[i]);
                r[i] = r[i].add(rv[i].scale(1/((float)numUpdates)));
                r[i] = wrapAngles(r[i]);
                
                p[i + 1] = fromPitchYaw((float)(r[i].y - Math.PI/2), (float)(r[i].x - Math.PI/2)).scale(d[i + 1]).add(p[i]);
                v[i + 1] = p[i + 1].subtract(prevPos[i+1]);
                a[i + 1] = v[i + 1].subtract(prevVel[i+1]);
            }
        }
    }
    
    public void updateSpringConstraint(float gravityAmount, float dampAmount, float stiffness, float maxForce, boolean doAttract, float attractFalloff, int numUpdates) {
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length; i++) {
                a[i] = F[i].scale(1 / m[i]);
                v[i] = v[i].add(a[i].scale(1/((float)numUpdates)));
                p[i] = p[i].add(v[i].scale(1/((float)numUpdates)));
                
                Vec3d disp;
                if (i == 0) {
                    Vec3d root = pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    disp = p[i].subtract(root);
                } else {
                    disp = p[i].subtract(p[i - 1]);
                }
                disp = disp.normalize().scale(disp.length() - d[i]);
                Vec3d damp = v[i].scale(dampAmount);
                Vec3d gravity = new Vec3d(0, -gravityAmount, 0);
                Vec3d attract = pOrig[0].subtract(p[i]); //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()).subtract(p[i]);
                F[i] = disp.scale(-stiffness * (disp.length())).add(gravity.scale(m[i])).subtract(damp);
                if (i == 0 || doAttract) {
                    F[i] = F[i].add(attract.scale(1 / (1 + i * i * attractFalloff)));
                }
                if (F[i].length() > maxForce) F[i].normalize().scale(maxForce);

            }
        }
    }
    
    public void setChain(SocketRendererModel[] chainOrig, SocketRendererModel[] chainDynamic) {
        p = new Vec3d[chainOrig.length];
        v = new Vec3d[chainOrig.length];
        a = new Vec3d[chainOrig.length];
        F = new Vec3d[chainOrig.length];
        m = new float[chainOrig.length];
        d = new float[chainOrig.length];
        T = new Vec3d[chainOrig.length];
        r = new Vec3d[chainOrig.length];
        rv = new Vec3d[chainOrig.length];
        ra = new Vec3d[chainOrig.length];
        pOrig = new Vec3d[chainOrig.length];
        for (int i = 0; i < chainOrig.length; i++) {
            pOrig[i] = chainOrig[i].getWorldPos(entity, 0);
            p[i] = pOrig[i];
            v[i] = new Vec3d(0, 0, 0);
            a[i] = new Vec3d(0, 0, 0);
            F[i] = new Vec3d(0, 0, 0);
            T[i] = new Vec3d(0, 0, 0);
            r[i] = new Vec3d(0, 0, 0);
            rv[i] = new Vec3d(0, 0, 0);
            ra[i] = new Vec3d(0, 0, 0);
            m[i] = 0.5f + 0.5f / (i + 1);
            
            if (i > 0) d[i] = (float) p[i].distanceTo(p[i-1]);
            else d[i] = 1f;
            
            chainOrig[i].isHidden = true;
        }
        
        for (int i = 0; i < chainOrig.length - 1; i++) {
            r[i] = angleBetween(p[i], p[i + 1]);
        }
        
        prevP = p[0];
        prevV = v[0];
        
        for (int i = 0; i < chainOrig.length; i++) {
            if (chainDynamic[i] == null) {
                chainDynamic[i] = new SocketRendererModel(chainOrig[i]);
            }
        }
    }
    
    public void updateChain(float delta, SocketRendererModel[] chainOrig, SocketRendererModel[] chainDynamic, float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        if (p.length != chainOrig.length || Double.isNaN(p[1].x)) {
            setChain(chainOrig, chainDynamic);
        }
        
        if (prevUpdateTick != entity.ticksExisted) {
            for (int i = 0; i < chainOrig.length; i++) {
                pOrig[i] = chainOrig[i].getWorldPos(entity, delta);
            }
            
            updateBendConstraint(gravityAmount, stiffness, stiffnessFalloff, damping, numUpdates, useFloor);
            
            prevUpdateTick = entity.ticksExisted;
        }
        
        if (chainDynamic == null) return;
        for (int i = chainDynamic.length - 1; i >= 0; i--) {
            if (chainDynamic[i] == null) return;
            Vec3d renderPos = p[i].add(v[i].scale(delta)).add(a[i].scale(0.5 * delta * delta));
            chainDynamic[i].setWorldPos(entity, renderPos, delta);
            
            if (i < chainDynamic.length - 1) {
                Vec3d p1 = new Vec3d(chainDynamic[i].rotationPointX, chainDynamic[i].rotationPointY, chainDynamic[i].rotationPointZ);
                Vec3d p2 = new Vec3d(chainDynamic[i+1].rotationPointX, chainDynamic[i+1].rotationPointY, chainDynamic[i+1].rotationPointZ);
                Vec3d diff = p2.subtract(p1);
                float yaw = (float)Math.atan2(diff.x, diff.z);
                float pitch = -(float)Math.asin(diff.y/diff.length());
                chainDynamic[i].rotateAngleY = chainDynamic[i].defaultRotationY + yaw;
                chainDynamic[i].rotateAngleX = chainDynamic[i].defaultRotationZ + pitch;
                chainDynamic[i].rotateAngleZ = (float) r[i].z;
                
                Vec3d diffRotated = diff;
                diffRotated = diffRotated.rotateYaw(yaw);
                diffRotated = diffRotated.rotatePitch(pitch);
            }
        }
    }
    
    public void render(float f5, SocketRendererModel[] dynModelRenderers) {
        if (dynModelRenderers == null) return;
        for (int i = 0; i < dynModelRenderers.length - 1; i++) {
            if (dynModelRenderers[i] == null) return;
            dynModelRenderers[i].render(f5);
        }
    }
    
    private static Vec3d fromPitchYaw(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch);
        float f3 = MathHelper.sin(-pitch);
        return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
    
    private static Vec3d angleBetween(Vec3d p1, Vec3d p2) {
        float dz = (float) (p2.z - p1.z);
        float dx = (float) (p2.x - p1.x);
        float dy = (float) (p2.y - p1.y);
        
        float yaw = (float) MathHelper.atan2(dz, dx);
        float pitch = (float) MathHelper.atan2(Math.sqrt(dz * dz + dx * dx), dy);
        return wrapAngles(new Vec3d(yaw, pitch, 0));
    }
    
    public static Vec3d toPitchYaw(Vec3d vector) {
        
        double f3 = vector.y;
        double pitch = -Math.asin(f3);
        double f2 = -Math.cos(pitch);
        double f1 = vector.x/f2;
        double yaw = -Math.asin(f1) + Math.PI/2;
        
        return wrapAngles(new Vec3d(yaw, pitch, 0));
    }
    
    private static Vec3d toEuler(Vec3d axis, double angle) {
        System.out.println(axis + ", " + angle);
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double t = 1-c;
        
        double yaw;
        double pitch;
        double roll;
        
        double x = axis.x;
        double y = axis.y;
        double z = axis.z;
    
        final double v = x * y * t + z * s;
        if (v > 0.998) { // north pole singularity detected
            yaw = 2*Math.atan2(x*Math.sin(angle/2),Math.cos(angle/2));
            pitch = Math.PI/2;
            roll = 0;
        }
        else if (v < -0.998) { // south pole singularity detected
            yaw = -2*Math.atan2(x*Math.sin(angle/2),Math.cos(angle/2));
            pitch = -Math.PI/2;
            roll = 0;
        }
        else {
            yaw = Math.atan2(y * s - x * z * t, 1 - (y * y + z * z) * t);
            pitch = Math.asin(v);
            roll = Math.atan2(x * s - y * z * t, 1 - (x * x + z * z) * t);
        }
        
        return new Vec3d(yaw, pitch, roll);
    }
    
    private static Vec3d wrapAngles(Vec3d r) {
        double x = r.x;
        double y = r.y;
        double z = r.z;
        
        while (x > Math.PI) x -= 2 * Math.PI;
        while (x < -Math.PI) x += 2 * Math.PI;
        
        while (y > Math.PI) y -= 2 * Math.PI;
        while (y < -Math.PI) y += 2 * Math.PI;
        
        while (z > Math.PI) z -= 2 * Math.PI;
        while (z < -Math.PI) z += 2 * Math.PI;
        
        return new Vec3d(x,y,z);
    }
    
    private static Vec3d multiply(Vec3d u, Vec3d v, boolean preserveDir) {
        if (preserveDir) {
            return new Vec3d(u.x * Math.abs(v.x), u.y * Math.abs(v.y), u.z * Math.abs(v.z));
        }
        return new Vec3d(u.x * v.x, u.y * v.y, u.z * v.z);
    }
}