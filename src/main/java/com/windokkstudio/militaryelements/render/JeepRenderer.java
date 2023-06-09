package com.windokkstudio.militaryelements.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import com.windokkstudio.militaryelements.misc.MathUtil;
import com.windokkstudio.militaryelements.render.models.JeepModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class JeepRenderer<T extends JeepEntity> extends EntityRenderer<T>{

    public ResourceLocation TEXTURE = new ResourceLocation("militaryelements:textures/entities/jeep.png");
    protected final EntityModel<T> jeepEntityModel;

    public JeepRenderer(EntityRendererProvider.Context context) {
        super(context);
        jeepEntityModel = new JeepModel(context.bakeLayer(JeepModel.LAYER_LOCATION));
        this.shadowRadius = 2F;
    }


    @Override
    public void render(T jeepEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.6F, -1.6F, 1.6F);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(0, -1.5, 0);

        Quaternion q = MathUtil.lerpQ(partialTicks, jeepEntity.getQ_Prev(), jeepEntity.getQ_Client());
        poseStack.mulPose(q);

        //Hurt Animation
        float timeSinceHitWithPartial = (float) jeepEntity.getTimeSinceHit() - partialTicks;
        if (timeSinceHitWithPartial > 0.0F) {
            float angle = Mth.clamp(timeSinceHitWithPartial / 10.0F, -30, 30);
            timeSinceHitWithPartial = jeepEntity.tickCount + partialTicks;
            poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(timeSinceHitWithPartial) * angle));
        }
        if (jeepEntity.getThrottle()>0) {
            jeepEntityModel.setupAnim(jeepEntity, 0, 0, 0, 0, 0);
        }
        VertexConsumer vertexConsumer = buffer.getBuffer(jeepEntityModel.renderType(TEXTURE));
        jeepEntityModel.setupAnim(jeepEntity, partialTicks, 0, 0, 0, 0);
        jeepEntityModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        super.render(jeepEntity, 0, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(JeepEntity entity) {
        return TEXTURE;
    }
    
    


}
