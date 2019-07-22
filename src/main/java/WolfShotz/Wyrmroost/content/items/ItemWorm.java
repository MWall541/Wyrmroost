package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemWorm extends Item {
    public ItemWorm() {
        super(ModUtils.itemBuilder());
        setRegistryName("desertwyrm");

        addPropertyOverride(new ResourceLocation("isalive"), (item, world, player) -> {
            if (item.hasTag()) if (item.getTag().getBoolean("isalive")) return 1f;
            return 0f;
        });
    }
}
