package WolfShotz.Wyrmroost.util.entityutils.client.animation;

import WolfShotz.Wyrmroost.util.entityutils.client.model.AdvancedRendererModel;
import WolfShotz.Wyrmroost.util.entityutils.client.model.BoxPosCache;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class ModelAnimator
{
    private int tempTick = 0;
    private int prevTempTick;
    private boolean correctAnimation = false;
    private IAnimatedObject entity;
    private boolean keyFrameInverted = false;
    private HashMap<AdvancedRendererModel, BoxPosCache> boxPosCache = new HashMap<>();
    private HashMap<AdvancedRendererModel, BoxPosCache> prevPosCache = new HashMap<>();
    
    private ModelAnimator() {}
    
    public static ModelAnimator create()
    {
        return new ModelAnimator();
    }
    
    public IAnimatedObject getEntity()
    {
        return entity;
    }
    
    public void update(IAnimatedObject entity)
    {
        this.tempTick = prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
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

    public void invertKeyframe(boolean invert)
    {
        keyFrameInverted = invert;
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
    
    public void rotate(AdvancedRendererModel box, float x, float y, float z)
    {
        if (keyFrameInverted)
        {
            x = -x;
            y = -y;
            z = -z;
        }
        if (correctAnimation) getPosCache(box).addRotation(x, y, z);
    }
    
    public void move(AdvancedRendererModel box, float x, float y, float z)
    {
        if (keyFrameInverted)
        {
            x = -x;
            y = -y;
            z = -z;
        }
        if (correctAnimation) getPosCache(box).addOffset(x, y, z);
    }
    
    private BoxPosCache getPosCache(AdvancedRendererModel box)
    {
        return boxPosCache.computeIfAbsent(box, (b) -> new BoxPosCache());
    }
    
    public void endKeyframe()
    {
        endKeyframe(false);
        keyFrameInverted = false;
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
                    for (AdvancedRendererModel box : prevPosCache.keySet())
                    {
                        BoxPosCache cache = prevPosCache.get(box);
                        box.rotateAngleX += cache.getRotationX();
                        box.rotateAngleY += cache.getRotationY();
                        box.rotateAngleZ += cache.getRotationZ();
                        box.rotationPointX += cache.getOffsetX();
                        box.rotationPointY += cache.getOffsetY();
                        box.rotationPointZ += cache.getOffsetZ();
                    }
                }
                else
                {
                    float tick = ((float) (animationTick - prevTempTick) + Minecraft.getInstance().getRenderPartialTicks()) / (float) (tempTick - prevTempTick);
                    float inc = MathHelper.sin((float) ((double) tick * 3.141592653589793D / 2.0D));
                    float dec = 1.0F - inc;
                    
                    for (AdvancedRendererModel box : prevPosCache.keySet())
                    {
                        BoxPosCache cache = prevPosCache.get(box);
                        box.rotateAngleX += dec * cache.getRotationX();
                        box.rotateAngleY += dec * cache.getRotationY();
                        box.rotateAngleZ += dec * cache.getRotationZ();
                        box.rotationPointX += dec * cache.getOffsetX();
                        box.rotationPointY += dec * cache.getOffsetY();
                        box.rotationPointZ += dec * cache.getOffsetZ();
                    }
                    
                    for (AdvancedRendererModel box : boxPosCache.keySet())
                    {
                        BoxPosCache cache = boxPosCache.get(box);
                        box.rotateAngleX += inc * cache.getRotationX();
                        box.rotateAngleY += inc * cache.getRotationY();
                        box.rotateAngleZ += inc * cache.getRotationZ();
                        box.rotationPointX += inc * cache.getOffsetX();
                        box.rotationPointY += inc * cache.getOffsetY();
                        box.rotationPointZ += inc * cache.getOffsetZ();
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
