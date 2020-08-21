package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 * Utility Class to help with Internationalization, ResourceLocations, etc.
 */
public class ModUtils
{
    private ModUtils() {} // NU CONSTRUCTOR

    /**
     * Item Properties builder
     */
    public static Item.Properties itemBuilder() { return new Item.Properties().group(Wyrmroost.ITEM_GROUP); }
    
    /**
     * Block Properties builder
     */
    public static Block.Properties blockBuilder(Material material)
    {
        Block.Properties properties = Block.Properties.create(material);
        if (material == Material.WOOD) properties.harvestTool(ToolType.AXE).hardnessAndResistance(2).sound(SoundType.WOOD);
        else if (material == Material.ROCK) properties.harvestTool(ToolType.PICKAXE);
        else if (material == Material.SAND) properties.harvestTool(ToolType.SHOVEL).sound(SoundType.SAND);
        return properties;
    }

    public static <T extends IForgeRegistryEntry<T>> Set<T> getRegistryEntries(DeferredRegister<T> registry)
    {
        Set<T> entries = new HashSet<>();
        for (RegistryObject<T> entry : registry.getEntries()) entries.add(entry.get());
        return entries;
    }

    /**
     * Checks both hands of the passed player for an item that is instance of itemClass.
     * If one hand has one, return that ItemStack, else return the main hands stack.
     *
     * @param player the player
     * @param item   the item were trying to get
     * @return An ItemStack if it conatains the specified item, null otherwise
     */
    @Nullable
    public static ItemStack getHeldStack(PlayerEntity player, Item item)
    {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        return item == main.getItem()? main : item == off.getItem()? off : null;
    }

    /**
     * Get an entity type by a string "key"
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityType<T> getEntityTypeByKey(@Nonnull String key)
    {
        return (EntityType<T>) EntityType.byKey(key).orElse(null);
    }

    /**
     * Play a sound on the local client.
     *
     * @param world  :thinking:
     * @param pos    the pos to play it at (I haven't seen this change anything tho...)
     * @param sound  again, :thinking:
     * @param volume so help me god
     * @param pitch  the pitch of the sound. lower values = sulfur hexafloride, higher values = dying chipmunk
     */
    public static void playLocalSound(World world, BlockPos pos, SoundEvent sound, float volume, float pitch)
    {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.NEUTRAL, volume, pitch, false);
    }

    /**
     * Get all biomes of defined types.
     *
     * @param types The types of biomes
     * @return a set of all biomes that consist of the defined types.
     */
    public static Set<Biome> getBiomesByTypes(BiomeDictionary.Type... types)
    {
        Set<Biome> biomes = Sets.newHashSet();
        for (BiomeDictionary.Type type : types) biomes.addAll(BiomeDictionary.getBiomes(type));
        return biomes;
    }

    /**
     * Get all (approximate) {@link BlockPos}'s in an {@link AxisAlignedBB}
     * <p>
     * Iterable Version - for statements ftw
     *
     * @param aabb please tell me your not asking what this is for
     * @return an iterable of block pos's and not a damned stream
     */
    public static Iterable<BlockPos> getBlockPosesInAABB(AxisAlignedBB aabb)
    {
        return BlockPos.getAllInBoxMutable((int) aabb.minX, (int) aabb.minY, (int) aabb.minZ, (int) aabb.maxX, (int) aabb.maxY, (int) aabb.maxZ);
    }
}
