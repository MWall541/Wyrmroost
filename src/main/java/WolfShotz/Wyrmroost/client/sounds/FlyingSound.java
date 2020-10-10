package WolfShotz.Wyrmroost.client.sounds;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
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
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
        this.entity = entity;
        this.repeat = true;
        this.repeatDelay = 0;
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
            x = (float) entity.getPosX();
            y = (float) entity.getPosY();
            z = (float) entity.getPosZ();
            double length = entity.getMotion().lengthSquared();
            volume = Math.min((float) length * 2f, 0.75f);
            if (volume > 0.4f) pitch = 1f + (volume - 0.6f);
            else pitch = 1f;
        }
        else donePlaying = true;
    }

    public static void play(AbstractDragonEntity dragon)
    {
        Minecraft.getInstance().getSoundHandler().play(new FlyingSound(dragon));
    }
}
