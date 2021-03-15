package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WREffects
{
    public static final DeferredRegister<Effect> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Effect> SILK = register("silk", () -> new WREffect(EffectType.HARMFUL, 0x242424).addAttributeModifier(Attributes.GENERIC_MOVEMENT_SPEED, "9b2a8a43-9d6b-40f9-a8d3-7b9219f07271", -0.9, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static RegistryObject<Effect> register(String name, Supplier<Effect> sup)
    {
        return REGISTRY.register(name, sup);
    }

    /**
     * Because it's protected. BECAUSE IT'S PROTECTED
     */
    private static class WREffect extends Effect
    {
        WREffect(EffectType type, int liquidColor)
        {
            super(type, liquidColor);
        }
    }
}
