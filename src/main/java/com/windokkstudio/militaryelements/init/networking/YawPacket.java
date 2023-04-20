package com.windokkstudio.militaryelements.init.networking;

import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class YawPacket {

    private final byte yawRight;

    public YawPacket(byte yawRight) {
        this.yawRight = yawRight;
    }

    public YawPacket(FriendlyByteBuf buffer) {
        this.yawRight = buffer.readByte();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByte(yawRight);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender != null && sender.getVehicle() instanceof JeepEntity planeEntity && planeEntity.getControllingPassenger() == sender) {
                planeEntity.setYawRight(yawRight);
            }
        });
        ctx.setPacketHandled(true);
    }
}
