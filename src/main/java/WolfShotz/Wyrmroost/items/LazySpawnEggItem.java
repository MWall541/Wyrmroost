package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class LazySpawnEggItem extends Item
{
    public static List<LazySpawnEggItem> EGG_TYPES = Lists.newArrayList();
    public final Supplier<EntityType<?>> type;
    private final int PRIMARY_COLOR, SECONDARY_COLOR;

    public LazySpawnEggItem(Supplier<EntityType<? extends Entity>> type, int primaryColor, int secondaryColor)
    {
        super(ModUtils.itemBuilder());

        this.type = type;
        this.PRIMARY_COLOR = primaryColor;
        this.SECONDARY_COLOR = secondaryColor;
        EGG_TYPES.add(this);
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
        
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        
        if (raytraceresult.getType() != RayTraceResult.Type.BLOCK)
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        
        BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) raytraceresult;
        BlockPos blockpos = blockraytraceresult.getPos();
        
        if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock))
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        
        if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack))
        {
            if (type.get().spawn(worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null)
                return new ActionResult<>(ActionResultType.PASS, itemstack);
            if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
            
            playerIn.addStat(Stats.ITEM_USED.get(this));
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
        
        return new ActionResult<>(ActionResultType.FAIL, itemstack);
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (!(target instanceof AgeableEntity)) return false;
        if (!target.isAlive()) return false;
        if (target.getType() != type.get()) return false;

        ((AgeableEntity) target).createChild((AgeableEntity) target);
        
        return true;
    }

    public int getColors(int index) { return index == 0? PRIMARY_COLOR : SECONDARY_COLOR; }
}
