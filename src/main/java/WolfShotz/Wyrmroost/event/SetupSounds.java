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
    private static final String ID = Wyrmroost.MOD_ID + ":";
    private static final String ID_E = ID + "entity.";
    
    @ObjectHolder(ID_E + "minutus.idle")                   public static SoundEvent MINUTUS_IDLE;
    @ObjectHolder(ID_E + "minutus.screech")                public static SoundEvent MINUTUS_SCREECH;
    
    @ObjectHolder(ID_E + "silverglider.idle")              public static SoundEvent SILVERGLIDER_IDLE;
    @ObjectHolder(ID_E + "silverglider.hurt")              public static SoundEvent SILVERGLIDER_HURT;
    @ObjectHolder(ID_E + "silverglider.death")             public static SoundEvent SILVERGLIDER_DEATH;
    
    @ObjectHolder(ID_E + "owdrake.idle")                   public static SoundEvent OWDRAKE_IDLE;
    @ObjectHolder(ID_E + "owdrake.roar")                   public static SoundEvent OWDRAKE_ROAR;
    @ObjectHolder(ID_E + "owdrake.hurt")                   public static SoundEvent OWDRAKE_HURT;
    @ObjectHolder(ID_E + "owdrake.death")                  public static SoundEvent OWDRAKE_DEATH;
    
    @ObjectHolder(ID_E + "rooststalker.idle")              public static SoundEvent STALKER_IDLE;
    @ObjectHolder(ID_E + "rooststalker.hurt")              public static SoundEvent STALKER_HURT;
    @ObjectHolder(ID_E + "rooststalker.death")             public static SoundEvent STALKER_DEATH;
    
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
