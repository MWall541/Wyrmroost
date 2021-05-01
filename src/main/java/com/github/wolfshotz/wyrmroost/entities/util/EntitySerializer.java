package com.github.wolfshotz.wyrmroost.entities.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntitySerializer<E extends Entity>
{
    public static final NBTBridge<Boolean> BOOL = bridge((key, nbt, value) -> nbt.putBoolean(key, value), (key, nbt) -> nbt.getBoolean(key));
    public static final NBTBridge<Integer> INT = bridge((key, nbt, value) -> nbt.putInt(key, value), (key, nbt) -> nbt.getInt(key));
    public static final NBTBridge<CompoundNBT> NBT = bridge((key, nbt, value) -> nbt.put(key, value), (key, nbt) -> nbt.getCompound(key));
    public static final NBTBridge<BlockPos> POS = bridge((key, nbt, value) -> nbt.putLong(key, value.asLong()), (key, nbt) -> BlockPos.of(nbt.getLong(key)));

    final Entry<?, E>[] entries;

    private EntitySerializer(Entry<?, E>[] entries)
    {
        this.entries = entries;
    }

    public void serialize(E entity, CompoundNBT nbt)
    {
        for (Entry<?, E> entry : entries) entry.serialize(entity, nbt);
    }

    public void deserialize(E entity, CompoundNBT tag)
    {
        for (Entry<?, E> entry : entries) entry.deserialize(entity, tag);
    }

    @SuppressWarnings("unchecked")
    public <T extends E> EntitySerializer<T> concat(Consumer<Builder<T>> consumer)
    {
        Builder<T> builder = new Builder<>();
        consumer.accept(builder);
        return new EntitySerializer<>(ArrayUtils.addAll((Entry<?, T>[]) entries, builder.build()));
    }

    public static <E extends Entity> EntitySerializer<E> builder(Consumer<Builder<E>> consumer)
    {
        Builder<E> builder = new Builder<>();
        consumer.accept(builder);
        return new EntitySerializer<>(builder.build());
    }

    private static <T> NBTBridge<T> bridge(TriConsumer<String, CompoundNBT, T> setter, BiFunction<String, CompoundNBT, T> getter)
    {
        return new NBTBridge<>(setter, getter);
    }

    public static class Entry<T, E extends Entity>
    {
        private final NBTBridge<T> bridge;
        private final String key;
        private final Function<E, T> write;
        private final BiConsumer<E, T> read;

        public Entry(NBTBridge<T> type, String key, Function<E, T> write, BiConsumer<E, T> read)
        {
            this.bridge = type;
            this.key = key;
            this.write = write;
            this.read = read;
        }

        private void serialize(E entity, CompoundNBT nbt)
        {
            bridge.setter.accept(key, nbt, write.apply(entity));
        }

        private void deserialize(E entity, CompoundNBT nbt)
        {
            read.accept(entity, bridge.getter.apply(key, nbt));
        }
    }

    public static class NBTBridge<T>
    {
        private final TriConsumer<String, CompoundNBT, T> setter;
        private final BiFunction<String, CompoundNBT, T> getter;
        private NBTBridge<Optional<T>> optional;

        private NBTBridge(TriConsumer<String, CompoundNBT, T> setter, BiFunction<String, CompoundNBT, T> getter)
        {
            this.setter = setter;
            this.getter = getter;
        }

        public NBTBridge<Optional<T>> optional()
        {
            if (optional == null) // may not always be needed, so initalize lazily.
            {
                return optional = new NBTBridge<>((key, nbt, value) -> value.ifPresent(j -> setter.accept(key, nbt, j)),
                        (key, nbt) -> nbt.contains(key)? Optional.of(getter.apply(key, nbt)) : Optional.empty());
            }
            return optional;
        }
    }

    public static class Builder<E extends Entity>
    {
        private final List<Entry<?, E>> entries = new ArrayList<>();

        public <T> Builder<E> track(NBTBridge<T> bridge, String key, Function<E, T> write, BiConsumer<E, T> read)
        {
            entries.add(new Entry<>(bridge, key, write, read));
            return this;
        }

        @SuppressWarnings("unchecked")
        private Entry<?, E>[] build()
        {
            return entries.toArray(new Entry[0]);
        }
    }
}
