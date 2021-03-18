package com.github.wolfshotz.wyrmroost.client.render.entity.projectile;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class GeodeTippedArrowRenderer extends ArrowRenderer<GeodeTippedArrowEntity>
{
    private static final ResourceLocation BLUE = Wyrmroost.id("textures/entity/projectiles/arrow/blue_geode_tipped_arrow.png");
    private static final ResourceLocation RED = Wyrmroost.id("textures/entity/projectiles/arrow/red_geode_tipped_arrow.png");
    private static final ResourceLocation PURPLE = Wyrmroost.id("textures/entity/projectiles/arrow/purple_geode_tipped_arrow.png");

    public GeodeTippedArrowRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(GeodeTippedArrowEntity entity)
    {
        Item item = entity.getItem();
        if (item == WRItems.RED_GEODE_ARROW.get()) return RED;
        else if (item == WRItems.PURPLE_GEODE_ARROW.get()) return PURPLE;
        return BLUE;
    }
}
