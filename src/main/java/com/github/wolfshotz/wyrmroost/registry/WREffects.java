package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WREffects extends Effect
{
    public static final DeferredRegister<Effect> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Effect> SOUL_WEAKNESS = register("soul_weakness", () -> new WREffects(EffectType.HARMFUL, 0x007375));

    private static RegistryObject<Effect> register(String name, Supplier<Effect> sup)
    {
        return REGISTRY.register(name, sup);
    }

    // used for exposure because the constructor is protected
    public WREffects(EffectType category, int color)
    {
        super(category, color);
    }
}
