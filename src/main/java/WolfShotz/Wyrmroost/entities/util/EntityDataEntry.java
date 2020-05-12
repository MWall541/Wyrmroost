package WolfShotz.Wyrmroost.entities.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityDataEntry<T>
{
    public static final SerializerType<Boolean> BOOLEAN = new SerializerType<>((key, nbt, value) -> nbt.putBoolean(key, value), ((key, nbt) -> nbt.getBoolean(key)));
    public static final SerializerType<Integer> INTEGER = new SerializerType<>((key, nbt, value) -> nbt.putInt(key, value), (key, nbt) -> nbt.getInt(key));
    public static final SerializerType<CompoundNBT> COMPOUND = new SerializerType<>((key, nbt, value) -> nbt.put(key, value), (key, nbt) -> nbt.getCompound(key));
    public static final SerializerType<Optional<BlockPos>> BLOCK_POS = new SerializerType<>(
            (key, nbt, value) -> value.ifPresent(p -> nbt.putLong(key, p.toLong())),
            (key, nbt) ->
            {
                if (nbt.contains(key)) return Optional.of(BlockPos.fromLong(nbt.getLong(key)));
                else return Optional.empty();
            });

    public final String key;
    public final SerializerType<T> type;
    private final Supplier<T> writer;
    private final Consumer<T> reader;

    public EntityDataEntry(String key, SerializerType<T> type, Supplier<T> write, Consumer<T> read)
    {
        this.key = key;
        this.type = type;
        this.writer = write;
        this.reader = read;
    }

    public void write(CompoundNBT nbt)
    {
        type.write.accept(key, nbt, writer.get());
    }

    public void read(CompoundNBT nbt)
    {
        reader.accept(type.read.apply(key, nbt));
    }

    public static class SerializerType<T>
    {
        public final TriConsumer<String, CompoundNBT, T> write;
        public final BiFunction<String, CompoundNBT, T> read;

        private SerializerType(TriConsumer<String, CompoundNBT, T> write, BiFunction<String, CompoundNBT, T> read)
        {
            this.write = write;
            this.read = read;
        }
    }
}
