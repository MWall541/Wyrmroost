package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupSounds
{
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.minutus.idle")
    public static SoundEvent MINUTUS_IDLE;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.minutus.screech")
    public static SoundEvent MINUTUS_SCREECH;
    
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.silverglider.idle")
    public static SoundEvent SILVERGLIDER_IDLE;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.silverglider.hurt")
    public static SoundEvent SILVERGLIDER_HURT;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.silverglider.death")
    public static SoundEvent SILVERGLIDER_DEATH;
    
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.owdrake.idle")
    public static SoundEvent OWDRAKE_IDLE;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.owdrake.roar")
    public static SoundEvent OWDRAKE_ROAR;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.owdrake.hurt")
    public static SoundEvent OWDRAKE_HURT;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.owdrake.death")
    public static SoundEvent OWDRAKE_DEATH;
    
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.rooststalker.idle")
    public static SoundEvent STALKER_IDLE;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.rooststalker.hurt")
    public static SoundEvent STALKER_HURT;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":entity.rooststalker.death")
    public static SoundEvent STALKER_DEATH;
    
    @SubscribeEvent
    public static void soundSetup(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                registerSound("entity.minutus.idle"),
                registerSound("entity.minutus.screech"),
                
                registerSound("entity.silverglider.idle"),
                registerSound("entity.silverglider.hurt"),
                registerSound("entity.silverglider.death"),
                
                registerSound("entity.owdrake.idle"),
                registerSound("entity.owdrake.roar"),
                registerSound("entity.owdrake.hurt"),
                registerSound("entity.owdrake.death"),
                
                registerSound("entity.rooststalker.idle"),
                registerSound("entity.rooststalker.hurt"),
                registerSound("entity.rooststalker.death")
        );
    }

    private static SoundEvent registerSound(String name) { return new SoundEvent(ModUtils.location(name)).setRegistryName(name); }
}
