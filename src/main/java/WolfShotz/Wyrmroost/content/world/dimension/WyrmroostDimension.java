package WolfShotz.Wyrmroost.content.world.dimension;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.world.IExtendedBiome;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class WyrmroostDimension extends Dimension
{
    public static final DeferredRegister<ModDimension> DIMENSION = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Wyrmroost.MOD_ID);
    public static final RegistryObject<ModDimension> WYRMROOST_DIM = DIMENSION.register("wyrmroost", () -> ModDimension.withFactory(WyrmroostDimension::new));

    public WyrmroostDimension(World worldIn, DimensionType typeIn)
    {
        super(worldIn, typeIn);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator()
    {
        return new WyrmroostChunkGenerator(world);
    }
    
    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
    {
        for (int i = chunkPosIn.getXStart(); i <= chunkPosIn.getXEnd(); ++i)
        {
            for (int j = chunkPosIn.getZStart(); j <= chunkPosIn.getZEnd(); ++j)
            {
                BlockPos blockpos = findSpawn(i, j, checkValid);
                if (blockpos != null) return blockpos;
            }
        }
        
        return null;
    }
    
    @Nullable
    @Override
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(posX, 0, posZ);
        Biome biome = world.getBiome(blockpos$mutableblockpos);
        BlockState blockstate = biome.getSurfaceBuilderConfig().getTop();
        
        if (checkValid && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN))
            return null;
        Chunk chunk = world.getChunk(posX >> 4, posZ >> 4);
        int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
        if (i < 0)
            return null;
        if (chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15) > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, posX & 15, posZ & 15))
            return null;
        
        for (int j = i + 1; j >= 0; --j)
        {
            blockpos$mutableblockpos.setPos(posX, j, posZ);
            BlockState blockstate1 = this.world.getBlockState(blockpos$mutableblockpos);
            if (!blockstate1.getFluidState().isEmpty()) break;
            
            if (blockstate1.equals(blockstate)) return blockpos$mutableblockpos.up().toImmutable();
        }
        
        return null;
    }
    
    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        double d0 = MathHelper.frac((double) worldTime / 24000d - 0.25d);
        double d1 = 0.5d - Math.cos(d0 * Math.PI) / 2d;
        return (float) (d0 * 2d + d1) / 3f;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld() { return true; }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere() { return true; }

    /**
     * Return Vec3D with biome specific fog color
     */
    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        Biome biome = getBiome(Minecraft.getInstance().player.getPosition());
        if (biome instanceof IExtendedBiome) return ((IExtendedBiome) biome).getFogColor(celestialAngle, partialTicks);

        float f = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(f1, f2, f3);
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    @Override
    public boolean doesXZShowFog(int x, int z)
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome) return ((IExtendedBiome) biome).doesXZShowFog(x, z);
        return false;
    }

    @Nullable
    @Override
    public IRenderHandler getSkyRenderer()
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome) return ((IExtendedBiome) biome).getSkyRenderer();

        return super.getSkyRenderer();
    }

    @Nullable
    @Override
    public IRenderHandler getCloudRenderer()
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome) return ((IExtendedBiome) biome).getCloudRenderer();

        return super.getCloudRenderer();
    }

    @Nullable
    @Override
    public IRenderHandler getWeatherRenderer()
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome) return ((IExtendedBiome) biome).getWeatherRenderer();

        return super.getWeatherRenderer();
    }

    @Override
    public float getSunBrightness(float partialTicks)
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome) return ((IExtendedBiome) biome).getSunBrightness(world, partialTicks);

        return super.getSunBrightness(partialTicks);
    }

    @Override
    public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors)
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome)
            ((IExtendedBiome) biome).getLightmapColors(partialTicks, sunBrightness, skyLight, blockLight, colors);
    }

    @Override
    public Vec3d getCloudColor(float partialTicks)
    {
        Biome biome = getBiome();
        if (biome instanceof IExtendedBiome)
            ((IExtendedBiome) biome).getCloudColor(getWorld(), partialTicks);

        return getWorld().getCloudColorBody(partialTicks);
    }

    public Biome getBiome()
    {
        return getBiome(Minecraft.getInstance().player.getPosition());
    }
}
