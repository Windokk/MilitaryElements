package com.windokkstudio.militaryelements.init.networking;

import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


import java.util.function.Supplier;

public class ChangeThrottlePacket {

    private final Type type;

    public ChangeThrottlePacket(Type type) {
        this.type = type;
    }

    public ChangeThrottlePacket(FriendlyByteBuf buffer) {
        this.type = Type.values()[buffer.readByte()];
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByte(type.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender != null && sender.getVehicle() instanceof JeepEntity jeepEntity) {
                jeepEntity.changeThrottle(type);
            }
        });
        ctx.setPacketHandled(true);
    }

    public enum Type {
        UP,
        DOWN
    }
}
