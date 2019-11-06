package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

public class PortalBlock extends BlockBase
{
    public PortalBlock(String name, Properties properties) {
        super(name, ModUtils.blockBuilder(Material.PORTAL).doesNotBlockMovement().lightValue(15).hardnessAndResistance(-1f, Float.MAX_VALUE).noDrops());
    }
    
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = ((ServerPlayerEntity) entityIn);
        ServerWorld world = DimensionManager.getWorld(player.getServer(), ModUtils.getDimensionInstance(), false, true);
//        ((ServerPlayerEntity) entityIn).teleport(world, )
    }
}