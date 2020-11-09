package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class LazySpawnEggItem extends Item
{
    public static Set<LazySpawnEggItem> EGG_TYPES = new HashSet<>();
    public final Lazy<EntityType<?>> type;
    private final int PRIMARY_COLOR, SECONDARY_COLOR;

    public LazySpawnEggItem(Supplier<EntityType<? extends Entity>> type, int primaryColor, int secondaryColor)
    {
        super(WRItems.builder());

        this.type = Lazy.of(type);
        this.PRIMARY_COLOR = primaryColor;
        this.SECONDARY_COLOR = secondaryColor;
        EGG_TYPES.add(this);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        ResourceLocation regName = type.get().getRegistryName();
        return new TranslationTextComponent("entity." + regName.getNamespace() + "." + regName.getPath())
                .appendText(" ")
                .appendSibling(new TranslationTextComponent("item.wyrmroost.spawn_egg"));
    }

    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        if (world.isRemote) return ActionResultType.SUCCESS;

        ItemStack itemstack = context.getItem();
        BlockPos blockpos = context.getPos();
        Direction direction = context.getFace();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.getBlock() == Blocks.SPAWNER)
        {
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity instanceof MobSpawnerTileEntity)
            {
                AbstractSpawner abstractspawner = ((MobSpawnerTileEntity) tileentity).getSpawnerBaseLogic();
                abstractspawner.setEntityType(type.get());
                tileentity.markDirty();
                world.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
                itemstack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        
        BlockPos blockpos1;
        if (blockstate.getCollisionShape(world, blockpos).isEmpty()) blockpos1 = blockpos;
        else blockpos1 = blockpos.offset(direction);
        
        if (type.get().spawn(world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null)
            itemstack.shrink(1);
        
        return ActionResultType.SUCCESS;
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        
        if (worldIn.isRemote) return new ActionResult<>(ActionResultType.PASS, itemstack);
        
        RayTraceResult rtr = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        
        if (rtr.getType() != RayTraceResult.Type.BLOCK)
            return new ActionResult<>(ActionResultType.PASS, itemstack);

        BlockRayTraceResult rayTraceResult = ((BlockRayTraceResult) rtr);

        BlockPos blockpos = rayTraceResult.getPos();
        
        if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock))
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        
        if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, rayTraceResult.getFace(), itemstack))
        {
            if (type.get().spawn(worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null)
                return new ActionResult<>(ActionResultType.PASS, itemstack);
            if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
            
            playerIn.addStat(Stats.ITEM_USED.get(this));
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
        
        return new ActionResult<>(ActionResultType.FAIL, itemstack);
    }

    public int getColor(int index) { return index == 0? PRIMARY_COLOR : SECONDARY_COLOR; }

    /**
     * Do note that using this method is entirely dependent on the provided EntityType's registry name with addition to:
     * "_spawn_egg"
     */
    @Nullable
    public static Item getEggFor(EntityType<?> type)
    {
        ResourceLocation loc = type.getRegistryName();
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(loc.getNamespace(), loc.getPath() + "_spawn_egg"));
    }
}
