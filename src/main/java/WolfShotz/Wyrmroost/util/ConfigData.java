package WolfShotz.Wyrmroost.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData
{
    // Common
    public static boolean debugMode = false;
    public static int homeRadius = 16;
    
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
        
        CommonConfig(ForgeConfigSpec.Builder configBuilder)
        {
            configBuilder.comment("Wyrmroost General Options").push("general");
            debugMode = configBuilder
                    .comment("Do not enable this unless you are told to!")
                    .translation("config.wyrmroost.debug")
                    .define("debugMode", false);
            homeRadius = configBuilder
                    .comment("How far dragons can travel from their home points")
                    .translation("config.wyrmroost.homeradius")
                    .defineInRange("homeRadius", 16, 6, 1024);
            configBuilder.pop();
        }
        
        public static void reload()
        {
            ConfigData.debugMode = COMMON.debugMode.get();
        }
    }
}
