package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.event.SetupEntities;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import WolfShotz.Wyrmroost.util.utils.TranslationUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DragonEggItem extends Item
{
    public DragonEggItem() {
        super(ModUtils.itemBuilder());
        setRegistryName("dragon_egg");
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        World world = ctx.getWorld();
        
        CompoundNBT tag = ctx.getItem().getTag();
        if (tag == null || !tag.contains("dragonType")) return super.onItemUse(ctx);
    
        DragonEggEntity eggEntity = new DragonEggEntity(SetupEntities.dragon_egg, world);
        BlockPos pos = ctx.getPos();
        
        eggEntity.readAdditional(tag);
        eggEntity.setPosition(pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d);
        if (!world.isRemote) world.addEntity(eggEntity);
        ctx.getItem().shrink(1);
        
        return ActionResultType.SUCCESS;
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
        
        if (tag != null && tag.contains("hatchTime"))
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt("hatchTime") / 1200).applyTextStyle(TextFormatting.AQUA));
    }
}
