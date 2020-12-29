package com.github.wolfshotz.wyrmroost.items;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggProperties;
import com.github.wolfshotz.wyrmroost.registry.WRItems;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Bus.MOD)
public class LazySpawnEggItem extends SpawnEggItem
{
    public static Set<LazySpawnEggItem> EGG_TYPES = new HashSet<>();
    public final Lazy<EntityType<?>> type;
    private final int PRIMARY_COLOR, SECONDARY_COLOR;

    public LazySpawnEggItem(Supplier<EntityType<? extends Entity>> type, int primaryColor, int secondaryColor)
    {
        super(null, primaryColor, secondaryColor, WRItems.builder());

        this.type = Lazy.of(type);
        this.PRIMARY_COLOR = primaryColor;
        this.SECONDARY_COLOR = secondaryColor;
        EGG_TYPES.add(this);
    }
    
	// Use reflection to add the mod spawn eggs to the EGGS map in SpawnEggItem
	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			try {
			Map<EntityType<?>, SpawnEggItem> eggs = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class,
					null, "field_195987_b");
			for (LazySpawnEggItem egg : EGG_TYPES)
				eggs.put(egg.type.get(), egg);
			} catch (Exception e) {
				Wyrmroost.LOG.warn("Unable to access SpawnEggItem.EGGS");
			} 
		});
	}
    
	@Override
	public EntityType<?> getType(CompoundNBT nbt) {
		if (nbt != null && nbt.contains("EntityTag", 10)) {
			CompoundNBT compoundnbt = nbt.getCompound("EntityTag");
			if (compoundnbt.contains("id", 8)) {
				return EntityType.byKey(compoundnbt.getString("id")).orElse(type.get());
			}
		}
		return type.get();
	}

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        ResourceLocation regName = type.get().getRegistryName();
        return new TranslationTextComponent("entity." + regName.getNamespace() + "." + regName.getPath())
                .appendString(" ")
                .append(new TranslationTextComponent("item.wyrmroost.spawn_egg"));
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
    {
        if (!(target instanceof AgeableEntity)) return ActionResultType.PASS;
        if (!target.isAlive()) return ActionResultType.PASS;
        if (target.getType() != type.get()) return ActionResultType.PASS;

        if (!target.world.isRemote)
        {
            AgeableEntity entity = ((AgeableEntity) type.get().spawn(((ServerWorld) target.world), stack, playerIn, target.getPosition(), SpawnReason.SPAWN_EGG, false, false));
            entity.setGrowingAge(entity instanceof AbstractDragonEntity? DragonEggProperties.get(type.get()).getGrowthTime() : -24000);
        }

        return ActionResultType.func_233537_a_(playerIn.world.isRemote);
    }

    public int getColor(int index) { return index == 0? PRIMARY_COLOR : SECONDARY_COLOR; }

    /**
     * Do note that using this method is entirely dependent on the provided EntityType's registry name with addition to:
     * "_spawn_egg"
     */
    @Nullable
    public static Item getEggFor(EntityType<?> type)
    {
        ResourceLocation loc = type.getRegistryName();
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(loc.getNamespace(), loc.getPath() + "_spawn_egg"));
    }
}
