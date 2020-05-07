package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.client.render.DragonEggStackRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DragonEggItem extends Item
{
    public DragonEggItem()
    {
        super(ModUtils.itemBuilder().maxStackSize(1).setISTER(() -> DragonEggStackRenderer::new));
    }
    
    @Override
    public ActionResultType onItemUse(ItemUseContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        if (player.isSneaking()) return super.onItemUse(ctx);
        
        World world = ctx.getWorld();
        CompoundNBT tag = ctx.getItem().getTag();
        BlockPos pos = ctx.getPos();
        BlockState state = world.getBlockState(pos);
        DragonEggEntity eggEntity = WREntities.DRAGON_EGG.get().create(world);

        if (tag == null || !tag.contains("dragonType")) return ActionResultType.PASS;
        if (!state.getCollisionShape(world, pos).isEmpty()) pos = pos.offset(ctx.getFace());
        if (!world.getEntitiesWithinAABB(DragonEggEntity.class, new AxisAlignedBB(pos)).isEmpty())
            return ActionResultType.FAIL;

        eggEntity.readAdditional(tag);
        eggEntity.setPosition(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);

        if (!world.isRemote) world.addEntity(eggEntity);
        if (!player.isCreative()) player.setHeldItem(ctx.getHand(), ItemStack.EMPTY);
        
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity)
    {
        if (!player.isCreative()) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof AbstractDragonEntity)) return false;
        
        CompoundNBT nbt = new CompoundNBT();
        int hatchTime = ((AbstractDragonEntity) entity.getType().create(player.world)).getEggProperties().getHatchTime();
        
        nbt.putString("dragonType", EntityType.getKey(entity.getType()).toString());
        nbt.putInt("hatchTime", hatchTime);
        stack.setTag(nbt);
        player.sendStatusMessage(getDisplayName(stack), true);
        return true;
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag == null || tag.isEmpty()) return super.getDisplayName(stack);
        Optional<EntityType<?>> type = EntityType.byKey(tag.getString("dragonType"));
        
        if (type.isPresent())
        {
            String dragonTranslation = type.get().getName().getUnformattedComponentText();
            
            return ModUtils.appendableTextTranslation(dragonTranslation + " ", getTranslationKey());
        }
        
        return super.getDisplayName(stack);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        CompoundNBT tag = stack.getTag();
        
        if (tag != null && tag.contains("hatchTime"))
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.tooltip", tag.getInt("hatchTime") / 1200).applyTextStyle(TextFormatting.AQUA));
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.isCreative())
            tooltip.add(new TranslationTextComponent("item.wyrmroost.egg.creativetooltip").applyTextStyle(TextFormatting.GRAY));
    }
}
