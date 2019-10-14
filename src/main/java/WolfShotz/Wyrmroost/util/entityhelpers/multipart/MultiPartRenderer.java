package WolfShotz.Wyrmroost.util.entityhelpers.multipart;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class MultiPartRenderer extends EntityRenderer<MultiPartEntity>
{
    public MultiPartRenderer(EntityRendererManager manager) { super(manager); }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(MultiPartEntity entity) { return null; }
}
