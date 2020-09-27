package WolfShotz.Wyrmroost.client.sounds;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class FlyingSound extends TickableSound
{
    private final AbstractDragonEntity entity;
    private int time;

    public FlyingSound(AbstractDragonEntity entity)
    {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
        this.entity = entity;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1F;
    }

    public void tick()
    {
        if (++time < 20)
        {
            volume = 0;
            pitch = 1;
            return;
        }
        if (entity.isAlive() && entity.isFlying())
        {
            x = (float) entity.getPosX();
            y = (float) entity.getPosY();
            z = (float) entity.getPosZ();
            double length = entity.getMotion().lengthSquared();
            volume = Math.min((float) length * 2f, 1);
            if (volume > 0.4f) pitch = 1f + (volume - 0.6f);
            else pitch = 1f;
        }
        else donePlaying = true;
    }
}
