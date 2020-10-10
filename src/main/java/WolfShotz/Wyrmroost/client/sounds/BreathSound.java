package WolfShotz.Wyrmroost.client.sounds;

import WolfShotz.Wyrmroost.entities.dragon.RoyalRedEntity;
import WolfShotz.Wyrmroost.registry.WRSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;

public class BreathSound extends TickableSound
{
    private final RoyalRedEntity dragon;

    public BreathSound(RoyalRedEntity dragon)
    {
        super(WRSounds.FIRE_BREATH.get(), SoundCategory.PLAYERS);
        this.dragon = dragon;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.1f;
    }

    @Override
    public void tick()
    {
        float tick = dragon.breathTimer.get();
        if (!dragon.isAlive() || tick == 0)
        {
            finishPlaying();
            return;
        }
        volume = tick;
        Vector3d mouth = dragon.getApproximateMouthPos();
        x = (float) mouth.x;
        y = (float) mouth.y;
        z = (float) mouth.z;
    }

    public static void play(RoyalRedEntity dragon)
    {
        Minecraft.getInstance().getSoundHandler().play(new BreathSound(dragon));
    }
}
