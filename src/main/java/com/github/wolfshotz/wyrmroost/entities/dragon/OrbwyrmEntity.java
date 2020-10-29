package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.entities.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class OrbwyrmEntity extends AbstractDragonEntity
{
    public static final Animation HISS_ANIMATION = new Animation(67);
    public static final Animation BITE_ANIMATION = new Animation(19);
    public static final Animation SHOOT_SILK_ANIMATION = new Animation(100);

    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);

    public OrbwyrmEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, false);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        sitTimer.add(func_233684_eK_() || isSleeping()? 0.065f : -0.065f);
        sleepTimer.add(isSleeping()? 0.1f : -0.1f);

        Animation animation = getAnimation();
        int tick = getAnimationTick();

        if (animation == HISS_ANIMATION)
        {
            if (tick == 0) playSound(WRSounds.ENTITY_ORBWYRM_HISS.get(), 2, 1, true);
        }
        else if (animation == BITE_ANIMATION)
        {
            if (tick == 0) playSound(WRSounds.ENTITY_ORBWYRM_DEATH.get(), 1, 1, true);
            else if (tick == 7) attackInFront(3.65f, -0.3f);
        }
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY1 && pressed && noActiveAnimation())
        {
            if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) setAnimation(HISS_ANIMATION);
            else setAnimation(BITE_ANIMATION);
        }
    }

    @Override
    public int determineVariant()
    {
        if (getRNG().nextDouble() < 0.01) return -1;
        return getRNG().nextInt(3);
    }

    @Override
    public void setMotionMultiplier(BlockState state, Vector3d multiplier)
    {
        if (!state.isIn(Blocks.COBWEB)) super.setMotionMultiplier(state, multiplier);
    }

    @Override
    public Vector3d getPassengerPosOffset(Entity entity, int index)
    {
        if (getAnimation() == HISS_ANIMATION && index == 0) return new Vector3d(0, getMountedYOffset() + 0.5, -0.85);
        return super.getPassengerPosOffset(entity, index);
    }

    @Override
    protected boolean canBeRidden(Entity entity)
    {
        return !isChild() && isTamed();
    }

    @Override
    public boolean canFly()
    {
        return false;
    }

    @Override
    public Collection<? extends IItemProvider> getFoodItems()
    {
        return WRItems.WRTags.MEATS.getAllElements();
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        return getType().getSize().scale(getRenderScale());
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * 0.965f;
    }

    public CreatureAttribute getCreatureAttribute()
    {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public float getRenderScale() { return isChild()? 0.5f : isMale()? 0.75f : 1f; }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {HISS_ANIMATION, BITE_ANIMATION, SHOOT_SILK_ANIMATION};
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 40)
                .createMutableAttribute(MOVEMENT_SPEED, 0.4)
                .createMutableAttribute(KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(ATTACK_DAMAGE, 3)
                .createMutableAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 1);
    }
}
