package com.github.wolfshotz.wyrmroost.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by WolfShotz 7/9/19 - 00:31
 */
public final class ModUtils
{
    private ModUtils() {} // NU CONSTRUCTOR

    public static final Direction[] DIRECTIONS = Direction.values(); // cached directions cus vanilla didn't do it
    public static final Direction[] HORIZONTALS = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    public static final boolean DECK_THE_HALLS;

    static
    {
        Calendar calendar = Calendar.getInstance();
        DECK_THE_HALLS = calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) >= 19;
    }

    /**
     * This method is purely just to help prevent sided-loading problems.
     * Wrapping any client-only code in a double lambda helps to prevent class-loading from trying to crash the game
     * due to non-existant code by @OnlyIn usages.
     * Now this could be avoided using {@link net.minecraftforge.fml.DistExecutor} BUT, I tried that.
     * It turns out the method will still be called on the server side, and in some very certain situations, will still crash.
     * So to prevent that, Ill just manually put my own `if` statement and have this run if on client/server, simple.
     * I mean I hate it but, I'll take it.
     */
    public static void run(Supplier<Runnable> run)
    {
        run.get().run();
    }

    /**
     * Majorly self-explanatory
     */
    @SafeVarargs
    public static <T> boolean equalsAny(T comparator, T... comparing)
    {
        for (T t : comparing) if (comparator.equals(t)) return true;
        return false;
    }

    /**
     * @param registry the DeferredRegistry instance holding the objects
     * @param <T>      the type of registry
     * @return An Immutable Set that contains all entries of the provided DeferredRegister
     */
    public static <T extends IForgeRegistryEntry<T>> Set<T> getRegistryEntries(DeferredRegister<T> registry)
    {
        ImmutableSet.Builder<T> entries = ImmutableSet.builder();
        for (RegistryObject<T> entry : registry.getEntries()) entries.add(entry.get());
        return entries.build();
    }

    /**
     * Checks both hands of the passed player for an item.
     * If one hand has one, return that ItemStack, else return the main hands stack.
     *
     * @param player the player
     * @param item   the item were trying to get
     * @return An ItemStack if it conatains the specified item, null otherwise
     */
    @Nullable
    public static ItemStack getHeldStack(PlayerEntity player, Item item)
    {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return item == main.getItem()? main : item == off.getItem()? off : null;
    }

    /**
     * Get an entity type by a string "key"
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityType<T> getEntityTypeByKey(@Nonnull String key)
    {
        return (EntityType<T>) EntityType.byString(key).orElse(null);
    }

    /**
     * Play a sound on the local client.
     *
     * @param level  :thinking:
     * @param pos    the pos to play it at (I haven't seen this change anything tho...)
     * @param sound  again, :thinking:
     * @param volume so help me god
     * @param pitch  the pitch of the sound. lower values = sulfur hexafloride, higher values = dying chipmunk
     */
    public static void playLocalSound(World level, BlockPos pos, SoundEvent sound, float volume, float pitch)
    {
        level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.NEUTRAL, volume, pitch, false);
    }

    /**
     * Get all (approximate) {@link BlockPos}'s in an {@link AxisAlignedBB}
     * <p>
     * Iterable Version - for statements ftw
     *
     * @param aabb please tell me your not asking what this is for
     */
    public static Iterable<BlockPos> eachPositionIn(AxisAlignedBB aabb)
    {
        return BlockPos.betweenClosed(
                MathHelper.floor(aabb.minX),
                MathHelper.floor(aabb.minY),
                MathHelper.floor(aabb.minZ),
                MathHelper.ceil(aabb.maxX),
                MathHelper.ceil(aabb.maxY),
                MathHelper.ceil(aabb.maxZ));
    }
}
