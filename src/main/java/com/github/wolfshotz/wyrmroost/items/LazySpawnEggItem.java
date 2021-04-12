package com.github.wolfshotz.wyrmroost.items;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class LazySpawnEggItem<T extends Entity> extends SpawnEggItem
{
    public static final Set<LazySpawnEggItem<?>> SPAWN_EGGS = new HashSet<>();

    public final Lazy<EntityType<T>> type;

    @SuppressWarnings("ConstantConditions")
    public LazySpawnEggItem(Supplier<EntityType<T>> type, int primaryColor, int secondaryColor)
    {
        super(null, primaryColor, secondaryColor, WRItems.builder());

        this.type = Lazy.of(type);
        SPAWN_EGGS.add(this);
    }

    @Override
    public ITextComponent getName(ItemStack stack)
    {
        ResourceLocation regName = type.get().getRegistryName();
        return new TranslationTextComponent("entity." + regName.getNamespace() + "." + regName.getPath())
                .append(" ")
                .append(new TranslationTextComponent("item.wyrmroost.spawn_egg"));
    }

    public EntityType<?> getType(@Nullable CompoundNBT tag)
    {
        if (tag != null && tag.contains("EntityTag", 10))
        {
            CompoundNBT childTag = tag.getCompound("EntityTag");
            if (childTag.contains("id", 8))
                return EntityType.byString(childTag.getString("id")).orElse(type.get());
        }

        return type.get();
    }

    public static void addEggsToMap()
    {
        try
        {
            Map<EntityType<?>, SpawnEggItem> eggMap = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b");
            for (LazySpawnEggItem<?> item : SPAWN_EGGS) eggMap.put(item.type.get(), item);
        }
        catch (Exception e)
        {
            Wyrmroost.LOG.fatal("Something threw a fit when trying to touch the SpawnEgg map", e);
        }
    }
}
