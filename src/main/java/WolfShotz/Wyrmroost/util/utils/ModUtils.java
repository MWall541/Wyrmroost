package WolfShotz.Wyrmroost.util.utils;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 * Utility Class to help with Internationalization, ResourceLocations, etc.
 */
public class ModUtils
{
    private ModUtils() {} // NU CONSTRUCTOR
    
    /** Debug Logger */
    public static final Logger L = LogManager.getLogger(Wyrmroost.modID);

    /**
     * Register a new Resource Location.
     * @param path
     */
    public static ResourceLocation location(String path) { return new ResourceLocation(Wyrmroost.modID, path); }

    /**
     * Item Properties builder
     */
    public static Item.Properties itemBuilder() { return new Item.Properties().group(Wyrmroost.creativeTab); }
    
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
     * List version of {@link #collectAll(Set[])}
     * @param lists The lists to merge
     * @return One list collection with passed merged elemtents
     */
    @SafeVarargs
    public static <T> List<T> collectAll(List<T>... lists) {
        List<T> set = new ArrayList<>();
        for (List<T> listParam : lists) set.addAll(listParam);
        return set;
    }
}
