package com.windokkstudio.militaryelements.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class JeepRenderer<T extends JeepEntity> extends EntityRenderer<T> {


    public JeepRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T JeepEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(JeepEntity, 0, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(JeepEntity entity) {
        ResourceLocation resourceLocation = new ResourceLocation(MilitaryElements.MODID,"textures/entities/jeep.png");
        return resourceLocation;

    }

}
