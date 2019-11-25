package WolfShotz.Wyrmroost.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.regex.Pattern;

public class TranslationUtils
{
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
     * Equivalent to {@link #translation} but instead uses {@link StringTextComponent}
     * @param text
     * @param formats
     * @return
     */
    public static ITextComponent stringTranslation(String text, TextFormatting... formats) {
        StringTextComponent stringComponent = new StringTextComponent(text);
        for (TextFormatting format : formats) stringComponent.applyTextStyle(format);
        return stringComponent;
    }
    
    public static ITextComponent appendableTranslation(String... strings) {
        TranslationTextComponent translation = new TranslationTextComponent(strings[0]);
        for (int i = 1; i < strings.length; ++i) translation.appendSibling(new TranslationTextComponent(strings[i]));
        return translation;
    }
    
    /**
     * Minecrafts version of internationalization. Can be used as an alternative to:
     * <p> <code>ModUtils.translation()</code>
     * @param text
     */
    @OnlyIn(Dist.CLIENT)
    public static String format(String text) { return I18n.format(text); }
    
    /**
     * Add a tooltip to an Item. (The Mouse-over description)
     * <p> Output: "item.<code>MODID</code>.<code>ITEM</code>.tooltip"
     * @param item (<code>this</code>)
     * @param formats OPTIONAL
     * @return Item's Translation key + <code>".tooltip"</code>
     */
    public static ITextComponent addTooltip(Item item, TextFormatting... formats) {
        return translation(item.getTranslationKey() + ".tooltip", formats);
    }
    
    public static boolean containsArray(String string, String... elements) {
        for (String test : elements) if (string.contains(test)) return true;
        return false;
    }
    
    public static String replaceFirst(String string, String replacing, String replaceWith) { return Pattern.compile(replacing, Pattern.LITERAL).matcher(string).replaceFirst(replaceWith); }
}
