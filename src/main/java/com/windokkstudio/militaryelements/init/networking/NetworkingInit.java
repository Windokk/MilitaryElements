package com.windokkstudio.militaryelements.init.networking;

import com.windokkstudio.militaryelements.MilitaryElements;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkingInit {

    private static final String PROTOCOL_VERSION = "9";
    public static SimpleChannel INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MilitaryElements.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        int id = -1;

        INSTANCE.registerMessage(
                ++id,
                RotationPacket.class,
                RotationPacket::toBytes,
                RotationPacket::new,
                RotationPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );

        INSTANCE.registerMessage(
                ++id,
                OpenInventoryPacket.class,
                OpenInventoryPacket::toBytes,
                OpenInventoryPacket::new,
                OpenInventoryPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );



        INSTANCE.registerMessage(
                ++id,
                OpenPlaneInventoryPacket.class,
                OpenPlaneInventoryPacket::toBytes,
                OpenPlaneInventoryPacket::new,
                OpenPlaneInventoryPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );


        INSTANCE.registerMessage(
                ++id,
                ChangeThrottlePacket.class,
                ChangeThrottlePacket::toBytes,
                ChangeThrottlePacket::new,
                ChangeThrottlePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}
