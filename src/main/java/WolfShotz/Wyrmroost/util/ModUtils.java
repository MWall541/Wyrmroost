package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 * Utility Class to help with Internationalization, ResourceLocations, etc.
 */
public class ModUtils
{
    private ModUtils() {} // NU CONSTRUCTOR
    
    /**
     * Debug Logger
     */
    public static final Logger L = LogManager.getLogger(Wyrmroost.MOD_ID);

    /**
     * Register a new Resource Location.
     * @param path
     */
    public static ResourceLocation resource(String path) { return new ResourceLocation(Wyrmroost.MOD_ID, path); }

    /**
     * Item Properties builder
     */
    public static Item.Properties itemBuilder() { return new Item.Properties().group(Wyrmroost.CREATIVE_TAB); }
    
    /**
     * Block Properties builder
     */
    public static Block.Properties blockBuilder(Material material) { return Block.Properties.create(material); }
    
    /**
     * Get the Client World
     */
    @OnlyIn(Dist.CLIENT)
    public static World getClientWorld() { return Minecraft.getInstance().world; }
    
    /**
     * Merge all elements from passed sets into one set.
     * @param sets The sets to merge
     * @return One set collection with merged elements
     */
    @SafeVarargs
    public static <T> Set<T> collectAll(Set<T>... sets) {
        Set<T> set = new HashSet<>();
        for (Set<T> setParam : sets) set.addAll(setParam);
        return set;
    }
    
    /**
     * Cleander way of making array's from collections
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection) {
        return (T[]) collection.toArray();
    }
    
    /**
     * Get entities nearby this one (exclusing itself)
     */
    public static List<LivingEntity> getEntitiesNearby(LivingEntity entity, double radius) {
        return entity.world.getEntitiesWithinAABB(LivingEntity.class, entity.getBoundingBox().grow(radius), found -> found != entity && entity.getPassengers().stream().noneMatch(found::equals));
    }
    
    /**
     * Get an entity type by a string "key"
     * @param key
     * @return
     */
    @Nullable
    public static <T extends Entity> EntityType<T> getTypeByString(@Nonnull String key) { return (EntityType<T>) EntityType.byKey(key).orElse(null); }
    
    /**
     * Is the given aabb clear of all (solid) blocks?
     */
    public static boolean isBoxSafe(AxisAlignedBB aabb, World world) {
        for (int x = MathHelper.floor(aabb.minX); x < MathHelper.floor(aabb.maxX); ++x)
            for (int y = MathHelper.ceil(aabb.minY); y < MathHelper.floor(aabb.maxY); ++y)
                for (int z = MathHelper.floor(aabb.minZ); z < MathHelper.floor(aabb.maxZ); ++z)
                    if (world.getBlockState(new BlockPos(x, y, z)).isSolid()) return false;
                    
        return true;
    }
    
    /**
     * Return a "PASS" action result
     */
    public static <T> ActionResult<T> passAction(T result) { return new ActionResult<>(ActionResultType.PASS, result); }
    
    /**
     * Get the instance of the wyrmroost dimension
     */
    public static DimensionType getDimensionInstance() { return DimensionType.byName(ModUtils.resource("dim_wyrmroost")); }
    
    /**
     * Iterate through all slots in this handler and check if they are all empty. if not, return false;
     */
    public static boolean isItemHandlerEmpty(IItemHandler handler) {
        for (int i=0; i < handler.getSlots(); ++i) if (!handler.getStackInSlot(i).isEmpty()) return false;
        return true;
    }
}
