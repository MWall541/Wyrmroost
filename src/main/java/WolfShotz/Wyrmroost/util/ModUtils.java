package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 * Utility Class to help with Internationalization, ResourceLocations, etc.
 */
public class ModUtils
{
    private ModUtils()
    {
    } // NU CONSTRUCTOR
    
    /**
     * Debug Logger
     */
    public static final Logger L = LogManager.getLogger(Wyrmroost.MOD_ID);
    
    /**
     * Register a new Resource Location.
     */
    public static ResourceLocation resource(String path)
    {
        return new ResourceLocation(Wyrmroost.MOD_ID, path);
    }
    
    /**
     * Item Properties builder
     */
    public static Item.Properties itemBuilder()
    {
        return new Item.Properties().group(Wyrmroost.CREATIVE_TAB);
    }
    
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
    
    /**
     * Itemgroup (creative tab.. smh) factory
     */
    public static ItemGroup itemGroupFactory(String name, Supplier<ItemStack> displayItem)
    {
        return new ItemGroup(name)
        {
            @Override
            public ItemStack createIcon()
            {
                return displayItem.get();
            }
        };
    }
    
    /**
     * Even simpler network channel builder
     */
    public static SimpleChannel simplisticChannel(ResourceLocation name, String versionSync)
    {
        return NetworkRegistry.ChannelBuilder
                       .named(name)
                       .clientAcceptedVersions(versionSync::equals)
                       .serverAcceptedVersions(versionSync::equals)
                       .networkProtocolVersion(() -> versionSync)
                       .simpleChannel();
    }
    
    /**
     * Get the Client World
     */
    public static World getClientWorld()
    {
        return Minecraft.getInstance().world;
    }
    
    /**
     * Merge all elements from passed sets into one set.
     *
     * @param sets The sets to merge
     * @return One set collection with merged elements
     */
    @SafeVarargs
    public static <T> Set<T> collectAll(Set<T>... sets)
    {
        Set<T> set = new HashSet<>();
        for (Set<T> setParam : sets) set.addAll(setParam);
        return set;
    }

    public static <T extends IForgeRegistryEntry<T>> Set<T> getRegistryEntries(DeferredRegister<T> registry)
    {
        return registry.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
    }

    /**
     * Cleander way of making array's from collections
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection)
    {
        return (T[]) collection.toArray();
    }
    
    /**
     * Get an entity type by a string "key"
     */
    @Nullable
    public static <T extends Entity> EntityType<T> getTypeByString(@Nonnull String key)
    {
        return (EntityType<T>) EntityType.byKey(key).orElse(null);
    }
    
    /**
     * Is the given aabb clear of all (solid) blocks?
     */
    public static boolean isBoxSafe(AxisAlignedBB aabb, World world)
    {
        for (int x = MathHelper.floor(aabb.minX); x < MathHelper.floor(aabb.maxX); ++x)
            for (int y = MathHelper.ceil(aabb.minY); y < MathHelper.floor(aabb.maxY); ++y)
                for (int z = MathHelper.floor(aabb.minZ); z < MathHelper.floor(aabb.maxZ); ++z)
                    if (world.getBlockState(new BlockPos(x, y, z)).isSolid()) return false;
        
        return true;
    }
    
    /**
     * Get the instance of the wyrmroost dimension
     */
    public static DimensionType getDimensionInstance()
    {
        return DimensionType.byName(ModUtils.resource("dim_wyrmroost"));
    }
    
    /**
     * Creates a new TranslationTextComponent appended with the passed strings
     */
    public static ITextComponent appendableTextTranslation(String... strings)
    {
        TranslationTextComponent translation = new TranslationTextComponent(strings[0]);
        for (int i = 1; i < strings.length; ++i) translation.appendSibling(new TranslationTextComponent(strings[i]));
        return translation;
    }
    
    /**
     * Find the first occurrence of a given string and replace it.
     *
     * @param string      The string
     * @param replacing   The first occurence were replacing
     * @param replaceWith What were replacing that first occurence with
     */
    public static String replaceFirst(String string, String replacing, String replaceWith)
    {
        return Pattern.compile(replacing, Pattern.LITERAL).matcher(string).replaceFirst(replaceWith);
    }
}
