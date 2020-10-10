package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TrumpetItem extends Item
{
    public TrumpetItem() { super(WRItems.builder()); }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        SoundEvent sound = player.getRNG().nextBoolean()? WRSounds.ENTITY_BFLY_IDLE.get() : WRSounds.ENTITY_BFLY_ROAR.get();
        world.playSound(player, player.getPosition(), sound, SoundCategory.PLAYERS, 0.75f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        player.getCooldownTracker().setCooldown(this, 50);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent("item.wyrmroost.trumpet.desc").mergeStyle(TextFormatting.GRAY));
    }
}
