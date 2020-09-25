package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.LDWyrmEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LDWyrmItem extends Item
{
    public static final String DATA_CONTENTS = "DesertWyrm"; // Should ALWAYS be a compound. If it throws a cast class exception SOMETHING fucked up.

    public LDWyrmItem()
    {
        super(WRItems.builder());

        addPropertyOverride(Wyrmroost.rl("is_alive"), (stack, world, player) ->
        {
            if (stack.hasTag() && stack.getTag().contains(DATA_CONTENTS)) return 1f;
            return 0f;
        });
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        ItemStack stack = context.getItem();
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_CONTENTS))
            {
                World world = context.getWorld();
                if (!world.isRemote)
                {
                    BlockPos pos = context.getPos().offset(context.getFace());
                    CompoundNBT contents = tag.getCompound(DATA_CONTENTS);
                    LDWyrmEntity entity = WREntities.LESSER_DESERTWYRM.get().create(world);

                    entity.deserializeNBT(contents);
                    if (stack.hasDisplayName())
                        entity.setCustomName(stack.getDisplayName()); // Item name takes priority
                    entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    world.addEntity(entity);
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
