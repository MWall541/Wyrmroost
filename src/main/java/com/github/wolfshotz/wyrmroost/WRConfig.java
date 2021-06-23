package com.github.wolfshotz.wyrmroost;

import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;
import java.util.stream.Collectors;

public class WRConfig
{
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec.BooleanValue DEBUG_MODE;
    public static final ForgeConfigSpec.IntValue PLATINUM_ORE_CHANCE;
    public static final ForgeConfigSpec.IntValue PLATINUM_ORE_SIZE;
    public static final ForgeConfigSpec.IntValue PLATINUM_ORE_MAX_HEIGHT;

    public static final ForgeConfigSpec SERVER;
    public static final ForgeConfigSpec.DoubleValue BREATH_FIRE_SPREAD;
    public static final ForgeConfigSpec.IntValue HOME_RADIUS;
    private static final ForgeConfigSpec.BooleanValue RESPECT_MOB_GRIEFING;
    private static final ForgeConfigSpec.BooleanValue DRAGON_GRIEFING;
    private static final List<String> BREED_LIMIT_DEFAULTS = ImmutableList.of("butterfly_leviathan:1", "royal_red:2");
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BREED_LIMITS;
    private static final Object2IntMap<String> BREED_LIMITS_CACHE = new Object2IntOpenHashMap<>(); // gets cleared on first load anyway

    public static final ForgeConfigSpec CLIENT;
    public static final ForgeConfigSpec.BooleanValue NO_CULLING;
    public static final ForgeConfigSpec.BooleanValue DECK_THE_HALLS;
    public static final ForgeConfigSpec.BooleanValue RENDER_OUTLINES;

    public static boolean canGrief(World level)
    {
        return RESPECT_MOB_GRIEFING.get()? level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) : DRAGON_GRIEFING.get();
    }

    public static int getBreedLimitFor(EntityType<?> dragon)
    {
        return BREED_LIMITS_CACHE.getOrDefault(dragon.getRegistryName().getPath(), 0);
    }

    public static boolean deckTheHalls()
    {
        return ModUtils.DECK_THE_HALLS && DECK_THE_HALLS.get();
    }

    public static void loadConfig(ModConfig.ModConfigEvent event)
    {
        ForgeConfigSpec spec = event.getConfig().getSpec();
        if (spec == SERVER)
        {
            try
            {
                BREED_LIMITS_CACHE.clear();
                BREED_LIMITS_CACHE.putAll(parseEntries(BREED_LIMITS.get()));
            }
            catch (Exception e)
            {
                Wyrmroost.LOG.error("Unable to parse config entries. Did you forget something? Using previous values.", e);
            }
        }
    }

    private static Object2IntMap<String> parseEntries(List<? extends String> list)
    {
        return list.stream().map(s -> s.split(":")).collect(Collectors.toMap(
                s -> s[0],
                s -> Integer.parseInt(s[1]),
                (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); },
                Object2IntOpenHashMap::new));
    }

    static
    {
        ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
        common.comment("If your looking for more options, check `wyrmroost-client.toml` or, in `{World Name}/serverconfig/wyrmroost-server.toml`",
                "Wyrmroost General Options");

        common.push("general");

        DEBUG_MODE = common.comment("Do not enable this unless you are told to!")
                .translation("config.wyrmroost.debug")
                .define("debug_mode", false);

        common.pop();

        common.push("ores");

        PLATINUM_ORE_SIZE = common.comment("Size of Platinum Ore pockets [0-100, default: 9]").defineInRange("platinumOreSize", 9, 0, 100);
        PLATINUM_ORE_CHANCE = common.comment("Chances of Platinum Ore pocket being generated [0-100, default: 10]").defineInRange("platinumOreChance", 10, 0, 100);
        PLATINUM_ORE_MAX_HEIGHT = common.comment("Maximal height for Platinum Ore pocket generation [0-255, default: 64]").defineInRange("platinumOreMaxHeight", 64, 0, 255);

        COMMON = common.build();

        ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();
        server.comment("Wyrmroost Server Options",
                "For Singleplayer, These options are \"per-world.\" Meaning that there will be a different version of this config for each world you create.",
                "If you want this config to apply globally, place the file in the `defaultconfigs` folder in your game instance.",
                "Wyrmroost General Options")
                .push("general");

        server.comment("Wyrmroost Dragon Options").push("dragons");

        BREATH_FIRE_SPREAD = server.comment("Base Flammability or spread of fire from Dragon Fire Breath",
                "A value of 0 completely disables fire block damage completely.")
                .translation("config.wyrmroost.breath_fire_spread")
                .defineInRange("breath_fire_spread", 0.8, 0, 1);
        HOME_RADIUS = server.comment("The radius (not diameter!) of how far dragons can travel from their home points")
                .translation("config.wyrmroost.home_radius")
                .defineInRange("home_radius", 16, 6, 1024);
        BREED_LIMITS = server.comment("Breed limit for each dragon. This determines how many times a certain dragon can breed.",
                "Leaving this blank ( `[]` ) will disable the functionality.")
                .translation("config.wyrmroost.breed_limits")
                .defineList("breed_limits", () -> BREED_LIMIT_DEFAULTS, e -> e instanceof String);

        server.push("griefing");

        RESPECT_MOB_GRIEFING = server.comment("If true, dragons will respect the Minecraft Mob Griefing Gamerule.",
                "Otherwise, they will follow the `dragon_griefing` config rule")
                .translation("config.wyrmroost.respect_mob_griefing")
                .define("respect_mob_griefing", true);
        DRAGON_GRIEFING = server.comment("If true and not respecting mob griefing rules (`respect_mob_griefing`),",
                "Allow dragons to destroy blocks.",
                "Note: not all dragons destroy blocks and not all are as destructive as the next.")
                .define("dragon_griefing", true);

        server.pop(2);
        SERVER = server.build();


        ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
        client.comment("Wyrmroost Client Options").push("general");
        NO_CULLING = client.comment("Disables culling for rendering bigger dragons (dragons don't go poof in corner of eye)")
                .translation("config.wyrmroost.check_frustum")
                .define("disable_frustum_check", true);
        RENDER_OUTLINES = client.comment("Toggles the rendering of the Dragon Staff's entity color shaders.",
                "Disable this if you are having issues with this while using shaders.")
                .translation("config.wyrmroost.entity_outlines")
                .define("entity_outlines", true);
        DECK_THE_HALLS = client.comment("Only when the time comes...")
                .translation("config.wyrmroost.deck_the_halls")
                .define("deck_the_halls", true);
        client.pop();
        CLIENT = client.build();
    }
}
