package WolfShotz.Wyrmroost.client;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderStates extends RenderType
{
    // dummy
    private RenderStates() { super(null, null, 0, 0, false, false, null, null); }

    public static RenderType getGlow(ResourceLocation locationIn)
    {
        RenderState.TextureState textureState = new RenderState.TextureState(locationIn, false, false);
        return makeType("glow", DefaultVertexFormats.ENTITY, 7, 256, false, true, State.getBuilder()
                .texture(textureState)
                .transparency(RenderState.ADDITIVE_TRANSPARENCY)
                .alpha(DEFAULT_ALPHA)
                .build(false));
    }
}
