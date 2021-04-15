package com.github.wolfshotz.wyrmroost.client.render.entity.ldwyrm;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.LesserDesertwyrmEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class LDWyrmRenderer extends MobRenderer<LesserDesertwyrmEntity, EntityModel<LesserDesertwyrmEntity>>
{
    private final ResourceLocation TEXTURE = Wyrmroost.id("textures/entity/dragon/lesser_desertwyrm/body.png");
    private final ResourceLocation CHRISTMAS = Wyrmroost.id("textures/entity/dragon/lesser_desertwyrm/christmas.png");

    public LDWyrmRenderer(EntityRendererManager manager)
    {
        super(manager, new LDWyrmModel(), 0);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(LesserDesertwyrmEntity entity)
    {
        return WRConfig.deckTheHalls()? CHRISTMAS : TEXTURE;
    }
}
