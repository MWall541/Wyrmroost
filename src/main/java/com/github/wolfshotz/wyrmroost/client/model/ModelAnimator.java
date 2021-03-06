package com.github.wolfshotz.wyrmroost.client.model;

import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class ModelAnimator
{
    private int tempTick = 0;
    private int prevTempTick;
    private boolean correctAnimation = false;
    private float partialTicks;
    private IAnimatable entity;
    private final HashMap<WRModelRenderer, BoxPosCache> boxPosCache = new HashMap<>();
    private final HashMap<WRModelRenderer, BoxPosCache> prevPosCache = new HashMap<>();

    private ModelAnimator() {}

    public static ModelAnimator create() { return new ModelAnimator(); }

    public IAnimatable getEntity() { return entity; }

    public void update(IAnimatable entity, float partialTicks)
    {
        this.tempTick = prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.partialTicks = partialTicks;
        boxPosCache.clear();
        prevPosCache.clear();
    }
    
    public boolean setAnimation(Animation animation)
    {
        tempTick = prevTempTick = 0;
        correctAnimation = entity.getAnimation() == animation;
        return correctAnimation;
    }

    public void startKeyframe(int duration)
    {
        if (correctAnimation)
        {
            prevTempTick = tempTick;
            tempTick += duration;
        }
    }

    public void setStaticKeyframe(int duration)
    {
        startKeyframe(duration);
        endKeyframe(true);
    }

    public void resetKeyframe(int duration)
    {
        startKeyframe(duration);
        endKeyframe();
    }

    public void rotate(WRModelRenderer box, float x, float y, float z)
    {
        if (correctAnimation) getPosCache(box).addRotation(x, y, z);
    }

    public void move(WRModelRenderer box, float x, float y, float z)
    {
        if (correctAnimation) getPosCache(box).addOffset(x, y, z);
    }

    private BoxPosCache getPosCache(WRModelRenderer box)
    {
        return boxPosCache.computeIfAbsent(box, (b) -> new BoxPosCache());
    }
    
    public void endKeyframe()
    {
        endKeyframe(false);
    }
    
    private void endKeyframe(boolean stationary)
    {
        if (correctAnimation)
        {
            int animationTick = entity.getAnimationTick();
            if (animationTick >= prevTempTick && animationTick < tempTick)
            {
                if (stationary)
                {
                    for (Map.Entry<WRModelRenderer, BoxPosCache> entry : prevPosCache.entrySet())
                    {
                        ModelRenderer box = entry.getKey();
                        BoxPosCache cache = entry.getValue();
                        box.pitch += cache.getRotationX();
                        box.yaw += cache.getRotationY();
                        box.roll += cache.getRotationZ();
                        box.pivotX += cache.getOffsetX();
                        box.pivotY += cache.getOffsetY();
                        box.pivotZ += cache.getOffsetZ();
                    }
                }
                else
                {
                    float tick = ((float) (animationTick - prevTempTick) + partialTicks) / (tempTick - prevTempTick);
                    float inc = MathHelper.sin(tick * Mafs.PI / 2f);
                    float dec = 1f - inc;

                    for (Map.Entry<WRModelRenderer, BoxPosCache> entry : prevPosCache.entrySet())
                    {
                        ModelRenderer box = entry.getKey();
                        BoxPosCache cache = entry.getValue();
                        box.pitch += dec * cache.getRotationX();
                        box.yaw += dec * cache.getRotationY();
                        box.roll += dec * cache.getRotationZ();
                        box.pivotX += dec * cache.getOffsetX();
                        box.pivotY += dec * cache.getOffsetY();
                        box.pivotZ += dec * cache.getOffsetZ();
                    }

                    for (Map.Entry<WRModelRenderer, BoxPosCache> entry : boxPosCache.entrySet())
                    {
                        ModelRenderer box = entry.getKey();
                        BoxPosCache cache = entry.getValue();
                        box.pitch += inc * cache.getRotationX();
                        box.yaw += inc * cache.getRotationY();
                        box.roll += inc * cache.getRotationZ();
                        box.pivotX += inc * cache.getOffsetX();
                        box.pivotY += inc * cache.getOffsetY();
                        box.pivotZ += inc * cache.getOffsetZ();
                    }
                }
            }
            
            if (!stationary)
            {
                prevPosCache.clear();
                prevPosCache.putAll(boxPosCache);
                boxPosCache.clear();
            }
        }
    }
}
