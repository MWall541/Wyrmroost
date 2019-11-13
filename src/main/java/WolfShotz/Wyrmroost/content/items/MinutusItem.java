package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import WolfShotz.Wyrmroost.event.SetupSounds;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MinutusItem extends Item
{
    public MinutusItem() {
        super(ModUtils.itemBuilder());

        addPropertyOverride(ModUtils.location("isalive"), (item, world, player) -> {
            if (item.hasTag()) if (item.getTag().getBoolean("isalive")) return 1f;
            return 0f;
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity player, int itemSlot, boolean isSelected) {
        if (stack.hasTag() && stack.getTag().getBoolean("isalive") && new Random().nextInt(60) == 0 && isSelected)
            worldIn.playSound(player.posX, player.posY, player.posZ, SetupSounds.MINUTUS_SCREECH.get(), SoundCategory.NEUTRAL, 1f, 1f, false);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        CompoundNBT tag = stack.getTag();
        if (stack.hasTag() && tag.getBoolean("isalive")) {
            Hand hand = context.getHand();
            context.getPlayer().setHeldItem(hand, ItemStack.EMPTY);

            EntityType type = EntityType.byKey(tag.getString("entitytype")).orElse(null);
            if (type == null) return ActionResultType.FAIL;
            World world = context.getWorld();
            BlockPos pos = context.getPos();
            MinutusEntity entity = new MinutusEntity(type, world);

            tag.remove("entitytype");
            entity.read(tag);
            entity.setPosition(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);

            if (!world.isRemote) world.addEntity(entity);
            if (world.isRemote) context.getPlayer().swingArm(hand);

            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
