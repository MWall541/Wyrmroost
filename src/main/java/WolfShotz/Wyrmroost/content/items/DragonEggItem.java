package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.event.SetupEntities;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import WolfShotz.Wyrmroost.util.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity.DragonTypes;

public class DragonEggItem extends Item
{
    public DragonEggItem() {
        super(ModUtils.itemBuilder().maxStackSize(1));
        setRegistryName("dragon_egg");
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        if (player.isSneaking()) return super.onItemUse(ctx);
        
        World world = ctx.getWorld();
        
        CompoundNBT tag = ctx.getItem().getTag();
        if (tag == null || !tag.contains("dragonType")) return super.onItemUse(ctx);
    
        DragonEggEntity eggEntity = new DragonEggEntity(SetupEntities.dragon_egg, world);
        BlockPos pos = ctx.getPos();
        
        eggEntity.readAdditional(tag);
        eggEntity.setPosition(pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d);
        if (!world.isRemote) world.addEntity(eggEntity);
        if (!player.isCreative()) player.setHeldItem(ctx.getHand(), ItemStack.EMPTY);
        
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!player.isCreative() || !player.isSneaking()) return super.onItemRightClick(world, player, hand);
        ItemStack stack = player.getHeldItem(hand);
        CompoundNBT currentTag = stack.getTag();
        CompoundNBT tag = new CompoundNBT();
        String dragonType;
        
        if (currentTag != null && currentTag.contains("dragonType")) {
            DragonTypes typeEnum = DragonEggEntity.getDragonTypeEnum(currentTag.getString("dragonType"));
            int ordinal = (typeEnum.ordinal() >= DragonTypes.values().length - 1)? 0 : typeEnum.ordinal() + 1;
            
            dragonType = EntityType.getKey(DragonTypes.values()[ordinal].getType()).toString();
        }
        else dragonType = EntityType.getKey(DragonTypes.values()[new Random().nextInt(DragonTypes.values().length)].getType()).toString();
        
        tag.putString("dragonType", dragonType);
        tag.putInt("hatchTime", ((AbstractDragonEntity) ModUtils.getTypeByString(dragonType).create(world)).hatchTimer);
        stack.setTag(tag);
        player.sendStatusMessage(getDisplayName(stack), true);
        
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
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
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getTag();
        
        if (tag != null && tag.contains("hatchTime"))
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt("hatchTime") / 1200).applyTextStyle(TextFormatting.AQUA));
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.isCreative())
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.creativetooltip").applyTextStyle(TextFormatting.GRAY));
    }
}
