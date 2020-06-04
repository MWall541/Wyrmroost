package WolfShotz.Wyrmroost.client.render.entity;

import WolfShotz.Wyrmroost.client.model.WREntityModel;
import WolfShotz.Wyrmroost.client.render.RenderEvents;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import java.util.Calendar;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity, M extends WREntityModel<T>> extends MobRenderer<T, M>
{
    public static final String BASE_PATH = "textures/entity/dragon/";
    public boolean isChristmas = false;

    public AbstractDragonRenderer(EntityRendererManager manager, M model, float shadowSize)
    {
        super(manager, model, shadowSize);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (day > 14 && day < 26)) isChristmas = true;
    }

    /**
     * A conditional layer that can only render if certain conditions are met.
     * E.G. is the dragon sleeping, saddled, etc
     */
    public class ConditionalLayer extends LayerRenderer<T, M>
    {
        public Predicate<T> conditions;
        public final Function<T, RenderType> type;

        public ConditionalLayer(Predicate<T> conditions, Function<T, RenderType> type)
        {
            super(AbstractDragonRenderer.this);
            this.conditions = conditions;
            this.type = type;
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer type, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            if (!conditions.test(entity)) return;

            IVertexBuilder builder = type.getBuffer(this.type.apply(entity));
            getEntityModel().render(ms, builder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1, 1, 1, 1);
        }

        public ConditionalLayer addCondition(Predicate<T> condition)
        {
            this.conditions = conditions.and(condition);
            return this;
        }
    }

    public class GlowLayer extends LayerRenderer<T, M>
    {
        public final Function<T, ResourceLocation> texture;
        private final Predicate<T> condition;

        public GlowLayer(Predicate<T> condition, Function<T, ResourceLocation> texture)
        {
            super(AbstractDragonRenderer.this);
            this.texture = texture;
            this.condition = condition;
        }

        public GlowLayer(Function<T, ResourceLocation> texture)
        {
            this(e -> true, texture);
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            if (condition.test(entity))
            {
                IVertexBuilder builder = bufferIn.getBuffer(RenderEvents.getGlowType(texture.apply(entity)));
                getEntityModel().render(ms, builder, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        }
    }
}