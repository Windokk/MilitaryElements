package com.windokkstudio.militaryelements.animations;

import com.windokkstudio.militaryelements.MilitaryElements;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Example, how to trigger animations on specific players
 * Always trigger animation on client-side.  Maybe as a response to a network packet or event
 */
@Mod.EventBusSubscriber(modid = MilitaryElements.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerAnimationTriggerExample {

    //We need to know when to play an animation
    //This can be anything depending on your ideas (see Emotecraft, BetterCombat ...)
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        //Test if it is a player (main or other) and the message
        if (event.getMessage().contains(Component.literal("waving"))) {


            //Get the player from Minecraft, using the chat profile ID. From network packets, you'll receive entity IDs instead of UUIDs
            var player = Minecraft.getInstance().level.getPlayerByUUID(event.getMessageSigner().profileId());

            if (player == null) return; //The player can be null because it was a system message or because it is not loaded by this player.

            ResourceLocation resourceLocation2 =new ResourceLocation(MilitaryElements.MODID, "animation");
            //Get the animation for that player
            var animation = (ModifierLayer<IAnimation>)PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(resourceLocation2);
            if (animation != null) {
                ResourceLocation resourceLocation1 = new ResourceLocation("militaryelements", "syringue");
                animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(resourceLocation1)));
                var level = Minecraft.getInstance().level;
                double x = player.getX();
                double y =player.getY();
                double z = player.getZ();
                ResourceLocation resourceLocation = new ResourceLocation("militaryelements","syringe");
                level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(resourceLocation), SoundSource.NEUTRAL, 1, 1);

            }
        }
    }


}
