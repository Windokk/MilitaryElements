package com.windokkstudio.militaryelements.init.networking;

import com.mojang.math.Quaternion;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import com.windokkstudio.militaryelements.init.DataSerializersInit;
import com.windokkstudio.militaryelements.misc.MathUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RotationPacket {

    private final Quaternion quaternion;

    public RotationPacket(Quaternion quaternion) {
        this.quaternion = quaternion;
    }

    public RotationPacket(FriendlyByteBuf buffer) {
        this.quaternion = DataSerializersInit.QUATERNION_SERIALIZER_ENTRY.get().read(buffer);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        DataSerializersInit.QUATERNION_SERIALIZER_ENTRY.get().write(buffer, quaternion);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender != null && sender.getVehicle() instanceof JeepEntity jeepEntity) {
                jeepEntity.setQ(quaternion);
                MathUtil.EulerAngles eulerAngles = MathUtil.toEulerAngles(quaternion);
                jeepEntity.setYRot((float) eulerAngles.yaw);
                jeepEntity.setXRot((float) eulerAngles.pitch);
                jeepEntity.rotationRoll = (float) eulerAngles.roll;
                jeepEntity.setQ_Client(quaternion);
            }
        });
        ctx.setPacketHandled(true);
    }
}
