package com.github.wolfshotz.wyrmroost.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EmptyRenderer<T extends Entity> extends EntityRenderer<T>
{
    public EmptyRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity)
    {
        return null;
    }
}
