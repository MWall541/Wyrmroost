package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.items.base.ArmorBase;
import WolfShotz.Wyrmroost.items.base.ArmorMaterials;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.UUID;

public class DrakeArmorItem extends ArmorBase
{
    private static final UUID KB_RESISTANCE_ID = UUID.fromString("eaa010aa-299d-4c76-9f02-a1283c9e890b");
    private static final AttributeModifier KB_RESISTANCE = new AttributeModifier(KB_RESISTANCE_ID, "Drake armor knockback resistance", 10, AttributeModifier.Operation.ADDITION);

    public DrakeArmorItem(EquipmentSlotType equipType)
    {
        super(ArmorMaterials.DRAKE, equipType);
    }

    @Override
    public void applyFullSetBonus(LivingEntity entity, boolean hasFullSet)
    {
        ModifiableAttributeInstance attribute = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (attribute.hasModifier(KB_RESISTANCE)) attribute.removeModifier(KB_RESISTANCE);
        if (hasFullSet) attribute.applyNonPersistentModifier(KB_RESISTANCE);
    }
}
