package WolfShotz.Wyrmroost.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Configuration stuff for Wyrmroost
 * Try to keep "chance" values like so: Higher values, higher chances.
 */
public class ConfigData
{
    // Common
    public static boolean debugMode = false;
    public static int homeRadius = 16;

    public static double dfdBabyChance = 0.3d;

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
            builder.pop();
            builder.comment("Wyrmroost Dragon Options").push("dragons");
            dfdBabyChance = builder
                    .comment("Chances for a Dragon Fruit Drake to spawn as a baby. 0 = No Chance, 1 = Guaranteed. Higher values are better chances")
                    .translation("config.wyrmroost.dfdbabychance")
                    .defineInRange("dfdBabyChance", 0.3d, 0, 1d);

        }

        public static void reload()
        {
            ConfigData.debugMode = COMMON.debugMode.get();
            ConfigData.homeRadius = COMMON.homeRadius.get();
            ConfigData.dfdBabyChance = COMMON.dfdBabyChance.get();
        }
    }
}
