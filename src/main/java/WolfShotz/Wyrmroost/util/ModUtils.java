package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 * Utility Class to help with Internationalization, ResourceLocations, etc.
 */
public class ModUtils
{
    /** Debug Logger */
    public static final Logger L = LogManager.getLogger(Wyrmroost.modID);

    /**
     * Register a new Resource Location.
     * @param path
     */
    public static ResourceLocation location(String path) { return new ResourceLocation(Wyrmroost.modID, path); }

    /**
     * Item Properties builder
     */
    public static Item.Properties itemBuilder() { return new Item.Properties().group(Wyrmroost.creativeTab); }

    /**
     * Get the Client World
     */
    @OnlyIn(Dist.CLIENT)
    public static World getClientWorld() { return Minecraft.getInstance().world; }

    public static World getServerWorld() { throw new IllegalStateException("lol no"); }

    // ===============
    //   I18n / Text
    // ===============

    /**
     * Translate the passed string into an ITextComponent.
     * Basically just jam whatevers passed into the lang file.
     * @param text
     * @param formats OPTIONAL
     * @return Translated Text Component
     */
    public static ITextComponent translation(String text, TextFormatting... formats) {
        TranslationTextComponent translator = new TranslationTextComponent(text);
        for (TextFormatting format : formats) translator.applyTextStyle(format);
        return translator;
    }

    /**
     * Minecrafts version of internationalization. Can be used as an alternative to:
     * <p> <code>ModUtils.translation()</code>
     * @param text
     */
    public static String format(String text) { return I18n.format(text); }

    /**
     * Add a tooltip to an Item. (The Mouse-over description)
     * <p> Output: "item.<code>MODID</code>.<code>ITEM</code>.tooltip"
     * @param item (<code>this</code>)
     * @param formats OPTIONAL
     * @return Item's Translation key + <code>".tooltip"</code>
     */
    public static ITextComponent tooltip(Item item, TextFormatting... formats) {
        return translation(item.getTranslationKey() + ".tooltip", formats);
    }

    /**
     * Remove any white space.
     * @param text
     */
    public static String clean(String text) { return text.replace(" ", ""); }

    // ==============
    //   Reflection
    // ==============

    /**
     *
     * @return the protected boolean value of
     */
    public static boolean isEntityJumping(LivingEntity entity) {
        boolean reflect = ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, entity, "field_70703_bu");
        return reflect;
    }

    // ==============
    //   Math Utils
    // ==============

    /**
     * Attempt to rotate the first angle to become the second angle, but only allow overall direction change to at max be
     * third parameter
     */
    public float limitAngle(float sourceAngle, float targetAngle, float maximumChange) {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);

        if (f > maximumChange) f = maximumChange;
        if (f < -maximumChange) f = -maximumChange;

        float f1 = sourceAngle + f;

        if (f1 < 0.0F) f1 += 360.0F;
        else if (f1 > 360.0F) f1 -= 360.0F;

        return f1;
    }
}
