package com.windokkstudio.militaryelements.render.models;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class JeepModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("militaryelements", "jeep_model"), "main");
	private final ModelPart armature;
	private final ModelPart interior;
	private final ModelPart steeringwheel;
	private final ModelPart glass;
	private final ModelPart lights;
	private final ModelPart wheel1;
	private final ModelPart wheel2;
	private final ModelPart wheel3;
	private final ModelPart wheel4;
	private final ModelPart bb_main;

	public JeepModel(ModelPart root) {
		this.armature = root.getChild("armature");
		this.interior = root.getChild("interior");
		this.steeringwheel = root.getChild("steeringwheel");
		this.glass = root.getChild("glass");
		this.lights = root.getChild("lights");
		this.wheel1 = root.getChild("wheel1");
		this.wheel2 = root.getChild("wheel2");
		this.wheel3 = root.getChild("wheel3");
		this.wheel4 = root.getChild("wheel4");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition armature = partdefinition.addOrReplaceChild("armature", CubeListBuilder.create().texOffs(111, 93).addBox(-6.5F, -1.0F, 20.0F, 14.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(65, 23).addBox(-5.5F, 4.0F, 29.0F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 10.5074F, -11.8632F));

		PartDefinition cube_r1 = armature.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(66, 0).addBox(-6.0F, -5.2735F, -4.3781F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 67).addBox(-6.0F, -3.2735F, -1.8698F, 13.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(35, 23).addBox(-6.0F, -5.2735F, -2.8698F, 13.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 42).addBox(-6.0F, -7.2735F, -5.8698F, 13.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r2 = armature.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 61).addBox(-6.0F, -1.2074F, -2.9781F, 13.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 7.0F, 1.9199F, 0.0F, 0.0F));

		PartDefinition cube_r3 = armature.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(35, 0).addBox(-6.0F, -0.8074F, -5.3281F, 13.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 21.0F, 1.0908F, 0.0F, 0.0F));

		PartDefinition cube_r4 = armature.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 39).addBox(-6.0F, 8.5926F, -4.9781F, 13.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(86, 48).addBox(-6.0F, -0.6074F, -4.9781F, 1.0F, 12.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(78, 107).addBox(6.0F, -0.6074F, -4.9781F, 1.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 8.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r5 = armature.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(70, 14).addBox(-6.0F, -2.5074F, -0.9781F, 13.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 8.0F, 0.9163F, 0.0F, 0.0F));

		PartDefinition cube_r6 = armature.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(28, 50).addBox(-6.0F, -4.3692F, -2.4381F, 13.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 8.0F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r7 = armature.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(58, 50).addBox(-6.0F, -11.2735F, -0.8698F, 13.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r8 = armature.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(104, 42).addBox(-6.0F, -2.2735F, -0.8698F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.3928F, 6.9428F, 1.3963F, 0.0F, 0.0F));

		PartDefinition cube_r9 = armature.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 30).addBox(-6.0F, -4.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 10).addBox(6.0F, -4.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.4926F, 9.8219F, 0.8727F, 0.0F, 0.0F));

		PartDefinition cube_r10 = armature.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 39).addBox(-6.0F, 0.5F, -1.8791F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(33, 39).addBox(6.0F, 0.5F, -1.8791F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.873F, 9.9345F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r11 = armature.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(104, 39).addBox(-6.0F, -0.5F, -0.8791F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.2047F, 16.2273F, -2.138F, 0.0F, -3.1416F));

		PartDefinition cube_r12 = armature.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(28, 67).addBox(-7.0F, -10.3301F, 1.9254F, 13.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.136F, 22.4984F, 0.5236F, 0.0F, 3.1416F));

		PartDefinition cube_r13 = armature.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(98, 14).addBox(-7.0F, -1.2093F, -1.8694F, 13.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.971F, -0.7841F, 1.4835F, 0.0F, 3.1416F));

		PartDefinition cube_r14 = armature.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(98, 59).addBox(-7.0F, -1.2093F, -1.8694F, 13.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.971F, 1.2159F, 1.7017F, 0.0F, 3.1416F));

		PartDefinition cube_r15 = armature.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(96, 10).addBox(-7.0F, -0.6226F, -0.1964F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5612F, -5.183F, 0.48F, 0.0F, 3.1416F));

		PartDefinition cube_r16 = armature.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(103, 64).addBox(-7.0F, -0.6226F, -0.1964F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.9474F, -4.142F, 0.7854F, 0.0F, 3.1416F));

		PartDefinition cube_r17 = armature.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(103, 67).addBox(-7.0F, -2.3547F, -1.1964F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.9474F, -1.142F, 1.0472F, 0.0F, 3.1416F));

		PartDefinition cube_r18 = armature.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(103, 70).addBox(-7.0F, -2.3547F, -1.1964F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.3758F, 4.3497F, 2.138F, 0.0F, 3.1416F));

		PartDefinition cube_r19 = armature.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(104, 29).addBox(-7.0F, -0.3547F, -1.1964F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.2891F, 4.1502F, 2.3562F, 0.0F, 3.1416F));

		PartDefinition cube_r20 = armature.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(70, 18).addBox(-7.0F, -1.476F, -0.9035F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(102, 26).addBox(-7.0F, -0.476F, -0.9035F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.3025F, 5.8154F, 2.7925F, 0.0F, 3.1416F));

		PartDefinition cube_r21 = armature.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(13, 103).addBox(-7.0F, -2.6213F, -0.5765F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.1699F, 6.8481F, 2.9234F, 0.0F, 3.1416F));

		PartDefinition cube_r22 = armature.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(104, 45).addBox(-7.0F, -1.6213F, -0.5765F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.3297F, 7.6278F, 2.2253F, 0.0F, 3.1416F));

		PartDefinition cube_r23 = armature.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 49).addBox(-7.0F, -12.6213F, 4.4235F, 13.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 63).addBox(-7.0F, 0.5F, 0.0235F, 13.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.864F, 19.4984F, 1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r24 = armature.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(102, 18).addBox(-7.0F, -1.5F, -0.4965F, 13.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.864F, 19.4984F, 0.0F, 0.0F, 3.1416F));

		PartDefinition cube_r25 = armature.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(102, 22).addBox(-7.0F, -0.5F, 3.5035F, 13.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.864F, 19.4984F, -1.5708F, 0.0F, 3.1416F));

		PartDefinition cube_r26 = armature.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(104, 32).addBox(-7.0F, -4.5F, -0.8802F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.864F, 16.4984F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition cube_r27 = armature.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(104, 35).addBox(-7.0F, -2.5F, -0.5F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.7391F, 16.4521F, -2.9671F, 0.0F, 3.1416F));

		PartDefinition cube_r28 = armature.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(4, 30).addBox(-6.0F, -0.5F, -0.8791F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(35, 0).addBox(6.0F, -0.5F, -0.8791F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.2047F, 9.6418F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r29 = armature.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(8, 30).addBox(6.0F, -2.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 30).addBox(-6.0F, -2.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.7391F, 9.4521F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r30 = armature.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 63).addBox(-6.0F, -0.2735F, -0.4585F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.1535F, -5.5196F, 0.4363F, 0.0F, 0.0F));

		PartDefinition interior = partdefinition.addOrReplaceChild("interior", CubeListBuilder.create().texOffs(35, 7).addBox(-5.5F, -0.3333F, -6.1667F, 11.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-5.5F, -3.3333F, 4.8333F, 11.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.5F, -4.3333F, -18.1667F, 11.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.3333F, 0.1667F));

		PartDefinition steeringwheel = partdefinition.addOrReplaceChild("steeringwheel", CubeListBuilder.create().texOffs(0, 10).addBox(0.2222F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 93).addBox(-0.7778F, -0.6962F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(54, 73).addBox(-0.7778F, -3.5F, -0.6962F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 10.095F, 3.261F, 0.0F, -1.2217F, 1.5708F));

		PartDefinition hexadecagon_r1 = steeringwheel.addOrReplaceChild("hexadecagon_r1", CubeListBuilder.create().texOffs(71, 3).addBox(-0.5F, -3.5F, -0.6962F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 83).addBox(-0.5F, -0.6962F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2778F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r2 = steeringwheel.addOrReplaceChild("hexadecagon_r2", CubeListBuilder.create().texOffs(75, 3).addBox(-0.5F, -3.5F, -0.6962F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 103).addBox(-0.5F, -0.6962F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2778F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r3 = steeringwheel.addOrReplaceChild("hexadecagon_r3", CubeListBuilder.create().texOffs(32, 73).addBox(-0.5F, -0.6962F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2778F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hexadecagon_r4 = steeringwheel.addOrReplaceChild("hexadecagon_r4", CubeListBuilder.create().texOffs(102, 104).addBox(-0.5F, -0.6962F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2778F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition glass = partdefinition.addOrReplaceChild("glass", CubeListBuilder.create(), PartPose.offset(0.0F, 4.9481F, 6.3506F));

		PartDefinition cube_r31 = glass.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(64, 117).addBox(-6.0F, -4.3366F, -0.1813F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 115).addBox(0.0F, -4.3366F, -0.1813F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 112).addBox(6.0F, -4.3366F, -0.1813F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0519F, -0.0025F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r32 = glass.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(14, 106).addBox(4.0F, -6.0F, -0.1813F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0519F, -0.0025F, 0.0F, 0.3054F, -1.5708F));

		PartDefinition lights = partdefinition.addOrReplaceChild("lights", CubeListBuilder.create().texOffs(35, 7).addBox(4.5F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(35, 27).addBox(-6.5F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 19.0F));

		PartDefinition wheel1 = partdefinition.addOrReplaceChild("wheel1", CubeListBuilder.create().texOffs(25, 132).addBox(-1.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 19.0F, -12.0F));

		PartDefinition cube_r33 = wheel1.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r34 = wheel1.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r35 = wheel1.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r36 = wheel1.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r37 = wheel1.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r38 = wheel1.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r39 = wheel1.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition wheelinterior2 = wheel1.addOrReplaceChild("wheelinterior2", CubeListBuilder.create().texOffs(7, 131).addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(7, 131).addBox(-0.5F, -2.0F, 1.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r40 = wheelinterior2.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0355F, 0.0355F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r41 = wheelinterior2.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0355F, 0.0355F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r42 = wheelinterior2.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(7, 132).addBox(-0.5F, -2.0F, 2.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(7, 132).addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r43 = wheelinterior2.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0355F, -0.0355F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r44 = wheelinterior2.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0355F, -0.0355F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r45 = wheelinterior2.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(1, 126).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition wheel2 = partdefinition.addOrReplaceChild("wheel2", CubeListBuilder.create().texOffs(25, 132).addBox(-1.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 19.0F, 14.0F));

		PartDefinition cube_r46 = wheel2.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r47 = wheel2.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r48 = wheel2.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r49 = wheel2.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r50 = wheel2.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r51 = wheel2.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r52 = wheel2.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(25, 132).addBox(-4.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition wheelinterior3 = wheel2.addOrReplaceChild("wheelinterior3", CubeListBuilder.create().texOffs(7, 131).addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(7, 131).addBox(-0.5F, -2.0F, 1.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r53 = wheelinterior3.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0355F, 0.0355F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r54 = wheelinterior3.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0355F, 0.0355F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r55 = wheelinterior3.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(7, 132).addBox(-0.5F, -2.0F, 2.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(7, 132).addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r56 = wheelinterior3.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0355F, -0.0355F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r57 = wheelinterior3.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(6, 133).addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0355F, -0.0355F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r58 = wheelinterior3.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(1, 126).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition wheel3 = partdefinition.addOrReplaceChild("wheel3", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(-1.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 19.0F, 14.0F));

		PartDefinition cube_r59 = wheel3.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r60 = wheel3.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r61 = wheel3.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r62 = wheel3.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r63 = wheel3.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r64 = wheel3.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r65 = wheel3.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition wheelinterior4 = wheel3.addOrReplaceChild("wheelinterior4", CubeListBuilder.create().texOffs(7, 131).mirror().addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(7, 131).mirror().addBox(-0.5F, -2.0F, 1.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r66 = wheelinterior4.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0355F, 0.0355F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r67 = wheelinterior4.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0355F, 0.0355F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r68 = wheelinterior4.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(7, 132).mirror().addBox(-0.5F, -2.0F, 2.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(7, 132).mirror().addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r69 = wheelinterior4.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0355F, -0.0355F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r70 = wheelinterior4.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0355F, -0.0355F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r71 = wheelinterior4.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(1, 126).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition wheel4 = partdefinition.addOrReplaceChild("wheel4", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(-1.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 19.0F, -12.0F));

		PartDefinition cube_r72 = wheel4.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r73 = wheel4.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r74 = wheel4.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r75 = wheel4.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r76 = wheel4.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, 3.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r77 = wheel4.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r78 = wheel4.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(25, 132).mirror().addBox(2.0F, -2.0F, -4.8284F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition wheelinterior5 = wheel4.addOrReplaceChild("wheelinterior5", CubeListBuilder.create().texOffs(7, 131).mirror().addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(7, 131).mirror().addBox(-0.5F, -2.0F, 1.1213F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r79 = wheelinterior5.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0355F, 0.0355F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r80 = wheelinterior5.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0355F, 0.0355F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r81 = wheelinterior5.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(7, 132).mirror().addBox(-0.5F, -2.0F, 2.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(7, 132).mirror().addBox(-0.5F, -2.0F, -4.1213F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r82 = wheelinterior5.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0355F, -0.0355F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r83 = wheelinterior5.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(6, 133).mirror().addBox(-0.5F, 2.2782F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.0355F, -0.0355F, -2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r84 = wheelinterior5.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(1, 126).mirror().addBox(-0.5F, -2.0F, -4.0F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition char2_r1 = bb_main.addOrReplaceChild("char2_r1", CubeListBuilder.create().texOffs(0, 106).addBox(-2.5F, -4.0F, -1.0F, 5.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -11.9332F, -3.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition chair1_r1 = bb_main.addOrReplaceChild("chair1_r1", CubeListBuilder.create().texOffs(20, 105).addBox(-2.0F, -6.7F, -2.0F, 5.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -10.0F, -0.8663F, 0.48F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		armature.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		interior.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		steeringwheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		glass.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lights.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}