package com.github.wolfshotz.wyrmroost.client.sound;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class FlyingSound extends TickableSound
{
    private final AbstractDragonEntity entity;
    private int time;

    public FlyingSound(AbstractDragonEntity entity)
    {
        super(SoundEvents.ELYTRA_FLYING, SoundCategory.PLAYERS);
        this.entity = entity;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1f;
    }

    public void tick()
    {
        if (++time < 20)
        {
            volume = 0;
            pitch = 1;
            return;
        }
        if (entity.isAlive() && entity.isFlying() && entity.getControllingPlayer() == ClientEvents.getPlayer())
        {
            x = (float) entity.getX();
            y = (float) entity.getY();
            z = (float) entity.getZ();
            double length = entity.getDeltaMovement().lengthSqr();
            volume = Math.min((float) length * 2f, 0.75f);
            if (volume > 0.4f) pitch = 1f + (volume - 0.6f);
            else pitch = 1f;
        }
        else stop();
    }

    public static void play(AbstractDragonEntity dragon)
    {
        Minecraft.getInstance().getSoundManager().play(new FlyingSound(dragon));
    }
}
