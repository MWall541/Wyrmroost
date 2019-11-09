package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.event.SetupWorld;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

public class PortalBlock extends BlockBase
{
    public PortalBlock() {
        super("wyrmroost_portal", ModUtils.blockBuilder(Material.PORTAL).doesNotBlockMovement().lightValue(15).hardnessAndResistance(-1f, Float.MAX_VALUE).noDrops());
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.dimension.getModType() == SetupWorld.DIM_WYRMROOST) return;
        if (!(entityIn instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = ((ServerPlayerEntity) entityIn);
        ServerWorld world = DimensionManager.getWorld(player.getServer(), ModUtils.getDimensionInstance(), false, true);
        WorldBorder border = world.getWorldBorder();
    
        double x = player.posX;
        double z = player.posZ;
        double d3 = Math.min(-2.9999872E7D, border.minX() + 16d);
        double d4 = Math.min(-2.9999872E7D, border.minZ() + 16d);
        double d5 = Math.min(2.9999872E7D, border.maxX() - 16d);
        double d6 = Math.min(2.9999872E7D, border.maxZ() - 16d);
        x = MathHelper.clamp(x, d3, d5);
        z = MathHelper.clamp(z, d4, d6);
        double y = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) x, (int) z); // doesnt work, cant get chunk surface when it isnt loaded yet...
        
        ((ServerPlayerEntity) entityIn).teleport(world, x, y, z, player.rotationYaw, player.rotationPitch);
    }
}