package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.LesserDesertwyrmEntity;
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

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ItemModelsProperties.register(this, Wyrmroost.id("is_alive"), (stack, world, player) ->
        {
            if (stack.hasTag() && stack.getTag().contains(DATA_CONTENTS)) return 1f;
            return 0f;
        }));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        ItemStack stack = context.getItemInHand();
        if (stack.hasTag())
        {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(DATA_CONTENTS))
            {
                World level = context.getLevel();
                if (!level.isClientSide)
                {
                    BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
                    CompoundNBT contents = tag.getCompound(DATA_CONTENTS);
                    LesserDesertwyrmEntity entity = WREntities.LESSER_DESERTWYRM.get().create(level);

                    entity.deserializeNBT(contents);
                    if (stack.hasCustomHoverName())
                        entity.setCustomName(stack.getHoverName()); // Item name takes priority
                    entity.absMoveTo(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntity(entity);
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }
}
