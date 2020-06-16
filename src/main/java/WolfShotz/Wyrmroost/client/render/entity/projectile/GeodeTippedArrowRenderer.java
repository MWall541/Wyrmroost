package WolfShotz.Wyrmroost.client.render.entity.projectile;

import WolfShotz.Wyrmroost.entities.projectile.GeodeTippedArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class GeodeTippedArrowRenderer extends ArrowRenderer<GeodeTippedArrowEntity>
{
    private final ResourceLocation texture;

    public GeodeTippedArrowRenderer(EntityRendererManager renderManagerIn, ResourceLocation texture)
    {
        super(renderManagerIn);
        this.texture = texture;
    }

    @Override
    public ResourceLocation getEntityTexture(GeodeTippedArrowEntity entity) { return texture; }
}
