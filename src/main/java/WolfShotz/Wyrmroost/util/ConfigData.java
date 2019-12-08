package WolfShotz.Wyrmroost.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData
{
    private static final Pair<CommonConfig, ForgeConfigSpec> SPEC_PAIR = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    public static final CommonConfig COMMON = SPEC_PAIR.getLeft();
    public static final ForgeConfigSpec COMMON_SPEC = SPEC_PAIR.getRight();
    
    public static boolean debugMode = false;
    
    public static class CommonConfig {
        public final ForgeConfigSpec.BooleanValue debugMode;
    
        CommonConfig(ForgeConfigSpec.Builder configBuilder) {
            configBuilder.comment("Wyrmroost General Options").push("general");
            debugMode = configBuilder
                                .comment("Do not enable this unless you are told to!")
                                .translation("config.wyrmroost.debug")
                                .define("debugMode", false);
            configBuilder.pop();
        }
    
        public static void reload() {
            ConfigData.debugMode = COMMON.debugMode.get();
            System.out.println("firing");
        }
    }
}
