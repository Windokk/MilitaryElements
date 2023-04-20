package com.windokkstudio.militaryelements.init.networking;

import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class OpenPlaneInventoryPacket {

    public OpenPlaneInventoryPacket() {}
    public OpenPlaneInventoryPacket(FriendlyByteBuf buffer) {}
    public void toBytes(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender != null) {
                Entity entity = sender.getVehicle();
                if (entity instanceof JeepEntity planeEntity) {

                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
