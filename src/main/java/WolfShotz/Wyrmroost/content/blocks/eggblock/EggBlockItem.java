package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.event.SetupBlocks;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TranslationUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EggBlockItem extends BlockItem
{
    public EggBlockItem() {
        super(SetupBlocks.egg, ModUtils.itemBuilder());
        setRegistryName("egg");
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        
        if (tag != null && tag.contains("dragonType")) {
            String dragonTranslation = EntityType.byKey(tag.getString("dragonType")).get().getName().getUnformattedComponentText();
    
            return TranslationUtils.appendableTranslation(dragonTranslation + " ", getTranslationKey());
        }
        
        return super.getDisplayName(stack);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getTag();
        
        if (tag != null && tag.contains("hatchTimer"))
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt("hatchTimer") / 1200).applyTextStyle(TextFormatting.AQUA));
    }
}
