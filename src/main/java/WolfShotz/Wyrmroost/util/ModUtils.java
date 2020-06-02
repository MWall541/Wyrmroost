package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

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
    public static <T extends Entity> EntityType<T> getTypeByString(@Nonnull String key)
    {
        return (EntityType<T>) EntityType.byKey(key).orElse(null);
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

    public static Set<Biome> getBiomesByTypes(BiomeDictionary.Type... types)
    {
        Set<Biome> biomes = Sets.newHashSet();
        for (BiomeDictionary.Type type : types) biomes.addAll(BiomeDictionary.getBiomes(type));
        return biomes;
    }

}
