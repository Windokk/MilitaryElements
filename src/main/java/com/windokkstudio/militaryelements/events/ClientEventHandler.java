package com.windokkstudio.militaryelements.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import com.windokkstudio.militaryelements.init.networking.ChangeThrottlePacket;
import com.windokkstudio.militaryelements.init.networking.NetworkingInit;
import com.windokkstudio.militaryelements.init.networking.OpenPlaneInventoryPacket;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import static com.windokkstudio.militaryelements.misc.MathUtil.*;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {
    
    public static KeyMapping openJeepInventory;
    public static KeyMapping dropPayloadKey;
    public static KeyMapping engineUp;
    public static KeyMapping engineDown;
    public static KeyMapping pitchUp;
    public static KeyMapping pitchDown;
    public static KeyMapping yawRight;
    public static KeyMapping yawLeft;

    static {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ClientEventHandler::registerKeyBindings);
    }

    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        openJeepInventory = new KeyMapping("key.jeep_inventory_open.desc", GLFW.GLFW_KEY_X, "key.militaryelements.category");
        engineUp = new KeyMapping("key.jeep_engine_up.desc", GLFW.GLFW_KEY_Z, "key.militaryelements.category");
        engineDown = new KeyMapping("key.jeep_engine_down.desc", GLFW.GLFW_KEY_S, "key.militaryelements.category");
        event.register(openJeepInventory);
        event.register(engineUp);
        event.register(engineDown);
    }



    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPre(RenderLivingEvent.Pre<LivingEntity, ?> event) {
        LivingEntity livingEntity = event.getEntity();
        Entity entity = livingEntity.getRootVehicle();
        if (entity instanceof JeepEntity planeEntity) {
            PoseStack matrixStack = event.getPoseStack();
            matrixStack.pushPose();

            matrixStack.translate(0, 0.375, 0);
            Quaternion quaternion = lerpQ(event.getPartialTick(), planeEntity.getQ_Prev(), planeEntity.getQ_Client());
            quaternion.set(quaternion.i(), -quaternion.j(), -quaternion.k(), quaternion.r());
            matrixStack.mulPose(quaternion);
            float rotationYaw = lerpAngle(event.getPartialTick(), entity.yRotO, entity.getYRot());

            matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotationYaw));
            matrixStack.translate(0, -0.375, 0);

            if (degreesDifferenceAbs(planeEntity.rotationRoll, 0) > 90) { //TODO: Do I actually need this?
                livingEntity.yHeadRot = planeEntity.getYRot() * 2 - livingEntity.yHeadRot;
            }
            if (degreesDifferenceAbs(planeEntity.prevRotationRoll, 0) > 90) {
                livingEntity.yHeadRotO = planeEntity.yRotO * 2 - livingEntity.yHeadRotO;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPost(RenderLivingEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();
        Entity entity = livingEntity.getRootVehicle();
        if (entity instanceof JeepEntity planeEntity) {
            event.getPoseStack().popPose();

            if (degreesDifferenceAbs(planeEntity.rotationRoll, 0) > 90) {
                livingEntity.yHeadRot = planeEntity.getYRot() * 2 - event.getEntity().yHeadRot;
            }
            if (degreesDifferenceAbs(planeEntity.prevRotationRoll, 0) > 90) {
                livingEntity.yHeadRotO = planeEntity.yRotO * 2 - event.getEntity().yHeadRotO;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientPlayerTick(PlayerTickEvent event) {
        final Player player = event.player;
        if ((event.phase == Phase.END) && (player instanceof LocalPlayer)) {
            if (player.getVehicle() instanceof JeepEntity jeepEntity) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.options.getCameraType() != CameraType.FIRST_PERSON) {
                    jeepEntity.applyYawToEntity(player);
                }

                if (engineUp.isDown()) {
                    NetworkingInit.INSTANCE.sendToServer(new ChangeThrottlePacket(ChangeThrottlePacket.Type.UP));
                } else{
                    NetworkingInit.INSTANCE.sendToServer(new ChangeThrottlePacket(ChangeThrottlePacket.Type.DOWN));
                }
            }
        }
    }
}
