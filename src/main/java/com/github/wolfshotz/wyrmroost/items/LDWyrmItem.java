package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.LDWyrmEntity;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class LDWyrmItem extends Item
{
    public static final String DATA_CONTENTS = "DesertWyrm"; // Should ALWAYS be a compound. If it throws a cast class exception SOMETHING fucked up.

    public LDWyrmItem()
    {
        super(WRItems.builder());

        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ClientEvents.CALLBACKS.add(() -> ItemModelsProperties.register(this, Wyrmroost.id("is_alive"), (stack, world, player) ->
        {
            if (stack.hasTag() && stack.getTag().contains(DATA_CONTENTS)) return 1f;
            return 0f;
        })));
    }

    @Override
    public ActionResultType useOnBlock(ItemUseContext context)
    {
        ItemStack stack = context.getStack();
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_CONTENTS))
            {
                World world = context.getWorld();
                if (!world.isClient)
                {
                    BlockPos pos = context.getBlockPos().offset(context.getSide());
                    CompoundNBT contents = tag.getCompound(DATA_CONTENTS);
                    LDWyrmEntity entity = WREntities.LESSER_DESERTWYRM.get().create(world);

                    entity.deserializeNBT(contents);
                    if (stack.hasCustomName())
                        entity.setCustomName(stack.getName()); // Item name takes priority
                    entity.updatePosition(pos.getX(), pos.getY(), pos.getZ());
                    world.spawnEntity(entity);
                    stack.decrement(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
