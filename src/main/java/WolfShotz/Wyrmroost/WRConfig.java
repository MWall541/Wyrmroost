package WolfShotz.Wyrmroost;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Configuration stuff for Wyrmroost
 * Try to keep "chance" values like so: Higher values, higher chances.
 */
public class WRConfig
{
    // Common
    public static boolean debugMode = false;

    // Client
    public static boolean disableFrustumCheck = true;

    // Server
    public static int fireBreathFlammability = 100;
    public static int homeRadius = 16;
    public static double dfdBabyChance = 0.4d;


    /**
     * Config Spec on COMMON Dist
     */
    public static class Common
    {
        public static final Common INSTANCE;
        public static final ForgeConfigSpec SPEC;

        static
        {
            Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
            INSTANCE = pair.getLeft();
            SPEC = pair.getRight();
        }

        public final ForgeConfigSpec.BooleanValue debugMode;

        Common(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Wyrmroost General Options").push("general");
            debugMode = builder
                    .comment("Do not enable this unless you are told to!")
                    .translation("config.wyrmroost.debug")
                    .define("debugMode", false);

            builder.pop();
        }

        public static void reload()
        {
            WRConfig.debugMode = INSTANCE.debugMode.get();
        }
    }

    public static class Client
    {
        public static final Client INSTANCE;
        public static final ForgeConfigSpec SPEC;

        static
        {
            Pair<Client, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Client::new);
            INSTANCE = pair.getLeft();
            SPEC = pair.getRight();
        }

        public final ForgeConfigSpec.BooleanValue disableFrustumCheck;

        public Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Wyrmroost Client Options").push("General");
            disableFrustumCheck = builder
                    .comment("Disables Frustum check when rendering (Dragons parts dont go poof when looking too far) - Only applies to bigger bois")
                    .translation("config.wyrmroost.disableFrustumCheck")
                    .define("disableFrustumCheck", true);

            builder.pop();
        }

        public static void reload()
        {
            WRConfig.disableFrustumCheck = INSTANCE.disableFrustumCheck.get();
        }
    }

    public static class Server
    {
        public static final Server INSTANCE;
        public static final ForgeConfigSpec SPEC;

        static
        {
            Pair<Server, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Server::new);
            INSTANCE = pair.getLeft();
            SPEC = pair.getRight();
        }

        public final ForgeConfigSpec.IntValue homeRadius;
        public final ForgeConfigSpec.DoubleValue dfdBabyChance;
        public final ForgeConfigSpec.IntValue breathFlammability;

        public Server(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Wyrmroost Dragon Options").push("dragons");
            homeRadius = builder
                    .comment("How far dragons can travel from their home points")
                    .translation("config.wyrmroost.homeradius")
                    .defineInRange("homeRadius", 16, 6, 1024);
            dfdBabyChance = builder
                    .comment("Chances for a Dragon Fruit Drake to spawn as a baby. 0 = No Chance, 1 = (practically) Guaranteed. Higher values are better chances")
                    .translation("config.wyrmroost.dfdbabychance")
                    .defineInRange("dfdBabyChance", 0.3d, 0, 1d);
            breathFlammability = builder
                    .comment("Base Flammability for Dragon Fire Breath. (Note that this is a base value and that flammability is also influenced by blocks) A value of 999 will disable fire block damage completely.")
                    .translation("config.wyrmroost.breathFlammability")
                    .defineInRange("breathFlammability", 100, 25, 999);
        }

        public static void reload()
        {
            WRConfig.homeRadius = INSTANCE.homeRadius.get();
            WRConfig.dfdBabyChance = INSTANCE.dfdBabyChance.get();
            WRConfig.fireBreathFlammability = INSTANCE.breathFlammability.get();
        }
    }
}
