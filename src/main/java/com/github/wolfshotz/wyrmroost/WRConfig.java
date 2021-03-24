package com.github.wolfshotz.wyrmroost;

import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.core.util.Integers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static boolean deckTheHalls = true;
    public static boolean renderEntityOutlines = true;

    // Server
    public static double fireBreathFlammability = 0.8d;
    public static int homeRadius = 16;
    private static boolean respectMobGriefing;
    private static boolean dragonGriefing;
    public static Map<String, Integer> breedLimits;

    public static boolean canGrief(World level)
    {
        return respectMobGriefing? level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) : dragonGriefing;
    }

    public static void configLoad(ModConfig.ModConfigEvent evt)
    {
        try
        {
            ForgeConfigSpec spec = evt.getConfig().getSpec();
            if (spec == Common.SPEC) Common.reload();
            else if (spec == Client.SPEC) Client.reload();
            else if (spec == Server.SPEC) Server.reload();
        }
        catch (Throwable e)
        {
            Wyrmroost.LOG.error("Something went wrong updating Wyrmroost configs, using previous or default values! {}", e.toString());
        }
    }

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
            debugMode = builder.comment("Do not enable this unless you are told to!")
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
        public final ForgeConfigSpec.BooleanValue renderEntityOutlines;
        public final ForgeConfigSpec.BooleanValue deckTheHalls;

        Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Wyrmroost Client Options").push("General");
            disableFrustumCheck = builder.comment("Disables Frustum check when rendering (Dragons parts dont go poof when looking too far) - Only applies to bigger bois")
                    .translation("config.wyrmroost.disableFrustumCheck")
                    .define("disableFrustumCheck", true);
            renderEntityOutlines = builder.comment("Toggles Rendering of the Dragon Staff's Entity Color shaders", "Disable this if you're having issues with shaders")
                    .translation("config.wyrmroost.renderEntityOutlines")
                    .define("renderEntityOutlines", true);
            deckTheHalls = builder.comment("Only when the time comes...")
                    .translation("config.wyrmroost.deckTheHalls")
                    .define("deckTheHalls", true);

            builder.pop();
        }

        public static void reload()
        {
            WRConfig.disableFrustumCheck = INSTANCE.disableFrustumCheck.get();
            WRConfig.deckTheHalls = INSTANCE.deckTheHalls.get() && ModUtils.DECK_THE_HALLS;
            WRConfig.renderEntityOutlines = INSTANCE.renderEntityOutlines.get();
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
        public final ForgeConfigSpec.DoubleValue breathFlammability;
        public final ForgeConfigSpec.BooleanValue respectMobGriefing;
        public final ForgeConfigSpec.BooleanValue dragonGriefing;
        private static final List<String> BREED_LIMIT_DEFAULTS = ImmutableList.of("butterfly_leviathan:1", "royal_red:2");
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> breedLimits;

        Server(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Wyrmroost Dragon Options").push("dragons");
            homeRadius = builder
                    .comment("How far dragons can travel from their home points")
                    .translation("config.wyrmroost.homeradius")
                    .defineInRange("home_radius", 16, 6, 1024);

            breathFlammability = builder
                    .comment("Base Flammability for Dragon Fire Breath.",
                            "A value of 0 will disable fire block damage completely.")
                    .translation("config.wyrmroost.breathFlammability")
                    .defineInRange("breath_lammability", 0.8, 0, 1);

            breedLimits = builder
                    .comment("Breed Limit for each dragon. This determines how many times a certain dragon can breed.",
                            "Leaving this blank will disable the functionality.")
                    .translation("config.wyrmroost.breedlimits")
                    .defineList("breed_limits", () -> BREED_LIMIT_DEFAULTS, o -> o instanceof String);

            builder.push("griefing");

            respectMobGriefing = builder
                    .comment("If True, Dragons will respect the Minecraft MobGriefing Gamerule. Else, follow \"dragonGriefing\" option")
                    .translation("config.wyrmroost.respectMobGriefing")
                    .define("respect_mob_griefing", true);

            dragonGriefing = builder
                    .comment("If true and not respecting MobGriefing rules, allow dragons to grief")
                    .translation("config.wyrmroost.dragonGriefing")
                    .define("dragon_griefing", true);

            builder.pop();
            builder.pop();
        }

        public static void reload()
        {
            WRConfig.homeRadius = INSTANCE.homeRadius.get();
            WRConfig.fireBreathFlammability = INSTANCE.breathFlammability.get();
            WRConfig.respectMobGriefing = INSTANCE.respectMobGriefing.get();
            WRConfig.dragonGriefing = INSTANCE.dragonGriefing.get();
            WRConfig.breedLimits = INSTANCE.breedLimits.get().stream().map(s -> s.split(":")).collect(Collectors.toMap(s -> s[0], s -> Integers.parseInt(s[1])));
        }
    }
}
