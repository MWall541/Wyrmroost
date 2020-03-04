package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            public ItemStack createIcon() { return displayItem.get(); }
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
    public static ClientWorld getClientWorld()
    {
        return Minecraft.getInstance().world;
    }

    /**
     * Get the Server World
     */
    public static ServerWorld getServerWorld(PlayerEntity player)
    {
        return ServerLifecycleHooks.getCurrentServer().getWorld(player.dimension);
    }

    public static <T extends IForgeRegistryEntry<T>> Set<T> getRegistryEntries(DeferredRegister<T> registry)
    {
        return registry.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
    }

    /**
     * Checks both hands of the passed player for an item that is instance of itemClass.
     * If one hand has one, return that ItemStack, else return the main hands stack.
     *
     * @param player    the player
     * @param itemClass the class were checking instanceof
     * @param <T>       Type of item
     * @return An ItemStack which
     */
    public static <T extends Item> ItemStack getHeldStack(PlayerEntity player, Class<T> itemClass)
    {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        return itemClass.isInstance(main.getItem()) ? main : itemClass.isInstance(off.getItem()) ? off : main;
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
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = MathHelper.floor(aabb.minX); x < MathHelper.floor(aabb.maxX); ++x)
            for (int y = MathHelper.ceil(aabb.minY); y < MathHelper.floor(aabb.maxY); ++y)
                for (int z = MathHelper.floor(aabb.minZ); z < MathHelper.floor(aabb.maxZ); ++z)
                    if (world.getBlockState(pos.setPos(x, y, z)).isSolid()) return false;
        
        return true;
    }

    /**
     * Get the instance of the wyrmroost dimension
     */
    public static DimensionType getDimensionInstance() { return DimensionType.byName(Wyrmroost.rl("wyrmroost")); }

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
     * Put a BlockPos into an nbt Compound
     * The positions are put into an int array and then fed into the compound
     */
    public static void putBlockPos(CompoundNBT nbt, BlockPos pos, String key)
    {
        nbt.putIntArray(key, new int[]{pos.getY(), pos.getY(), pos.getZ()});
    }

    /**
     * Retrieve a blockpos from an nbt compound
     * Positions are retrieved from an int array of 3 elements
     */
    public static BlockPos getBlockPos(CompoundNBT nbt, String key)
    {
        int[] poses = nbt.getIntArray(key);
        return new BlockPos(poses[0], poses[1], poses[2]);
    }

    /**
     * Get all blocks inside a AABB
     */
    public static Stream<BlockPos> getAllPosesInBB(AxisAlignedBB bb, World world)
    {
        int minX = MathHelper.floor(bb.minX);
        int maxX = MathHelper.ceil(bb.maxX);
        int minY = MathHelper.floor(bb.minY);
        int maxY = MathHelper.ceil(bb.maxY);
        int minZ = MathHelper.floor(bb.minZ);
        int maxZ = MathHelper.ceil(bb.maxZ);
        return BlockPos.getAllInBox(minX, minY, minZ, maxX - 1, maxY - 1, maxZ - 1);
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
