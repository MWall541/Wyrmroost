package WolfShotz.Wyrmroost.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 * Utility Class to help with internationalization and text component translations
 */
public class UtilI18n
{
    /**
     * Translate passed string for internationalization.
     * @param text
     * @return
     */
    public static String translate(String text) { return I18n.format(text); }


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
}
