package com.windokkstudio.militaryelements.init.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings({"unused", "EmptyMethod"})
public class OpenInventoryPacket {

    public OpenInventoryPacket() {}
    public OpenInventoryPacket(FriendlyByteBuf buffer) {}
    public void toBytes(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
        });
        ctx.setPacketHandled(true);
    }
}
