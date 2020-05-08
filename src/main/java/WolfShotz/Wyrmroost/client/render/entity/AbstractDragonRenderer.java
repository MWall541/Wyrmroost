package WolfShotz.Wyrmroost.client.render.entity;

import WolfShotz.Wyrmroost.client.RenderStates;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.items.DragonStaffItem;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity> extends MobRenderer<T, EntityModel<T>>
{
    public static final String DEF_LOC = "textures/entity/dragon/";
    public boolean isChristmas = false;

    public AbstractDragonRenderer(EntityRendererManager manager, EntityModel<T> model, float shadowSize)
    {
        super(manager, model, shadowSize);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (day > 14 && day < 26)) isChristmas = true;
    }

    @Override
    public void render(T dragon, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        boolean flag = shouldRenderOutlines(dragon);
        if (flag)
        {
            OutlineLayerBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
            int color = dragon.getTeamColor();
            int red = color >> 16 & 255;
            int green = color >> 8 & 255;
            int blue = color & 255;
            buffer.setColor(red, green, blue, 255);
            bufferIn = buffer;
        }

        super.render(dragon, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    protected RenderType func_230042_a_(T entity, boolean isVisible, boolean isVisibleToPlayerOnly)
    {
        ResourceLocation texture = getEntityTexture(entity);
        if (isVisibleToPlayerOnly) return RenderType.getEntityTranslucent(texture);
        else if (isVisible) return entityModel.getRenderType(texture);
        else return entity.isGlowing()? RenderType.getOutline(texture) : null;
    }

    public boolean shouldRenderOutlines(AbstractDragonEntity dragon)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return false;
        ItemStack stack = ModUtils.getHeldStack(Minecraft.getInstance().player, WRItems.DRAGON_STAFF.get());
        return stack.getItem() == WRItems.DRAGON_STAFF.get() && Objects.equals(((DragonStaffItem) stack.getItem()).getBoundDragon(dragon.world, stack), dragon);
    }

    /**
     * A conditional layer that can only render if certain conditions are met.
     * E.G. is the dragon sleeping, saddled, etc
     */
    public class ConditionalLayer extends LayerRenderer<T, EntityModel<T>>
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
            getEntityModel().setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            getEntityModel().setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            getEntityModel().render(ms, builder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1, 1, 1, 1);
        }

        public ConditionalLayer addCondition(Predicate<T> condition)
        {
            this.conditions = conditions.and(condition);
            return this;
        }
    }

    public class GlowLayer extends LayerRenderer<T, EntityModel<T>>
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
                IVertexBuilder builder = bufferIn.getBuffer(RenderStates.getGlow(texture.apply(entity)));
                getEntityModel().render(ms, builder, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        }
    }
}