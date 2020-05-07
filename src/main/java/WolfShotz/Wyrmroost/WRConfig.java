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
    public static int homeRadius = 16;

    public static double dfdBabyChance = 0.3d;

    // Client
    public static boolean disableFrustumCheck = true;


    /**
     * Config Spec on COMMON Dist
     */
    public static class CommonConfig
    {
        private static final Pair<CommonConfig, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        public static final CommonConfig COMMON = CommonConfig.SPEC_PAIR.getLeft();
        public static final ForgeConfigSpec COMMON_SPEC = CommonConfig.SPEC_PAIR.getRight();

        public final ForgeConfigSpec.BooleanValue debugMode;
        public final ForgeConfigSpec.IntValue homeRadius;

        public final ForgeConfigSpec.DoubleValue dfdBabyChance;

        CommonConfig(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Wyrmroost General Options").push("general");
            debugMode = builder
                    .comment("Do not enable this unless you are told to!")
                    .translation("config.wyrmroost.debug")
                    .define("debugMode", false);
            homeRadius = builder
                    .comment("How far dragons can travel from their home points")
                    .translation("config.wyrmroost.homeradius")
                    .defineInRange("homeRadius", 16, 6, 1024);
            builder.comment("Wyrmroost Dragon Options").push("dragons");
            dfdBabyChance = builder
                    .comment("Chances for a Dragon Fruit Drake to spawn as a baby. 0 = No Chance, 1 = (practically) Guaranteed. Higher values are better chances")
                    .translation("config.wyrmroost.dfdbabychance")
                    .defineInRange("dfdBabyChance", 0.3d, 0, 1d);


            builder.pop();
        }

        public static void reload()
        {
            WRConfig.debugMode = COMMON.debugMode.get();
            WRConfig.homeRadius = COMMON.homeRadius.get();
            WRConfig.dfdBabyChance = COMMON.dfdBabyChance.get();
        }
    }

    public static class ClientConfig
    {
        private static final Pair<ClientConfig, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        public static final ClientConfig CLIENT = ClientConfig.SPEC_PAIR.getLeft();
        public static final ForgeConfigSpec CLIENT_SPEC = ClientConfig.SPEC_PAIR.getRight();

        public final ForgeConfigSpec.BooleanValue disableFrustumCheck;

        public ClientConfig(ForgeConfigSpec.Builder builder)
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
            WRConfig.disableFrustumCheck = CLIENT.disableFrustumCheck.get();
        }
    }
}
