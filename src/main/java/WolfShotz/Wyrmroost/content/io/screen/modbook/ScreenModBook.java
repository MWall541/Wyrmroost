package WolfShotz.Wyrmroost.content.io.screen.modbook;

import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TranslationUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

// #blit(xPos, yPos, initialX pixel, initialY pixel, x width, y width, .png width, .png height);
public class ScreenModBook extends Screen
{
    private static final ResourceLocation TEXTURE = ModUtils.resource("textures/io/modbook/tome.png");
    private final IResource TEXT = getResource();
    private final List<Page> PAGES = Lists.newArrayList();
    private int pageNumber = 0;
    private ChangePageButton next, back;

    public ScreenModBook() { super(TranslationUtils.translation("Tarragon Tome")); }

    @Override
    protected void init() {
        addButtons();
        back.visible = false;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResource().getInputStream(), StandardCharsets.UTF_8))) {
            for (String line : reader.lines().collect(Collectors.toList())) {
                if (PAGES.isEmpty() && line.isEmpty()) continue;
                if (line.startsWith("//")) continue;
                if (line.contains("[]===[]")) {
                    PAGES.add(new Page(font, itemRenderer));
                    
                    continue;
                }
                Page currentPage = PAGES.get(PAGES.size() - 1);
                if (line.contains("{backgroundResource}")) {
                    String fileName = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                    currentPage.backgroundResource = ModUtils.resource("textures/io/modbook/" + fileName);
                    
                    continue;
                }
                if (line.contains("{renderItem}")) {
                    String[] renderItemMethod = makeParameterArray(line, "{renderItem}", 3);
                    ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(renderItemMethod[0])));
                    int posX = Integer.parseInt(renderItemMethod[1]);
                    int posY = Integer.parseInt(renderItemMethod[2]);
                    currentPage.addItem(stack, posX, posY);
                    
                    continue;
                }
                if (line.contains("{renderImage}")) {
                    String[] renderImageMethod = makeParameterArray(line, "{renderImage}", 5);
                    ResourceLocation loc = ModUtils.resource("textures/io/modbook/" + renderImageMethod[0]);
                    int posX = Integer.parseInt(renderImageMethod[1]);
                    int posZ = Integer.parseInt(renderImageMethod[2]);
                    int imageWidth = Integer.parseInt(renderImageMethod[3]);
                    int imageHeight = Integer.parseInt(renderImageMethod[4]);
                    currentPage.addImage(loc, posX, posZ, imageWidth, imageHeight);
                    
                    continue;
                }
                currentPage.addLine(line);
            }
        } catch (Exception e) {
            ModUtils.L.warn("Could not read Book text!" + " ResourceManager: " + TEXT);
            e.printStackTrace();
        }
    }

    private void addButtons() {
        addButton(new Button(width / 2 - 100, 196, 200, 20, TranslationUtils.format("io.modbook.close"), func -> minecraft.displayGuiScreen(null)));
        // Notes bcus its still obfuscated: ChangePageButton((int) x locale, (int) y locale, (boolean) true = right | false = left, (lambda) what does this button do?, (boolean) plays sound?)
        next = addButton(new ChangePageButton(width / 2 + 100, 147, true, func -> updatePages(true), true));
        back = addButton(new ChangePageButton(width / 2 - 124, 147, false, func -> updatePages(false), true));
    }

    private void updatePages(boolean increment) {
        if (increment && pageNumber < PAGES.size() - 1) ++pageNumber;
        else if (pageNumber > 0) --pageNumber;
        next.visible = pageNumber < PAGES.size() - 1;
        back.visible = pageNumber > 0;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        
        bindTexture(TEXTURE);
        blit((width - 256) / 2, 5, 0, 0, 256, 192);
        
        if (!PAGES.isEmpty()) PAGES.get(pageNumber).render(width / 2, partialTicks);
    
        super.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean isPauseScreen() { return false; }
    
    public IResource getResource() {
        try {
            IResourceManager manager = Minecraft.getInstance().getResourceManager();
            String lang = Minecraft.getInstance().gameSettings.language;
            ResourceLocation resource = ModUtils.resource("lang/book/" + lang + ".txt");
            
            try { return manager.getResource(resource); }
            catch (FileNotFoundException e) {
                resource = ModUtils.resource("lang/book/en_us.txt");
                String fallBack = resource.getPath().replaceAll("-([a-z\\-]{2,6})_?([a-z]{0,3})", "");
                
                return manager.getResource(new ResourceLocation(resource.getNamespace(), fallBack));
            }
        } catch (Exception e) {
            ModUtils.L.warn("Could not get book text resources!");
            return null;
        }
    }
    
    private static String[] makeParameterArray(String line, String method, int numOfParams) {
        String[] contents = new String[numOfParams];
        String clean = clean(line, method);
        for (int i = 0; i <= numOfParams - 1; ++i) {
            String content = clean.substring(clean.indexOf("[") + 1, clean.indexOf("]"));
            contents[i] = content;
            clean = clean(clean, "[" + content + "]");
        }
        return contents;
    }
    
    private static String clean(String string, String toReplace) { return TranslationUtils.replaceFirst(string, toReplace, "").trim(); }
    
    private void bindTexture(ResourceLocation resource) { minecraft.getTextureManager().bindTexture(resource); }
}