package com.windokkstudio.militaryelements.init;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.windokkstudio.militaryelements.MilitaryElements.MODID;

/**
 * This is an example implementation of PlayerAnimator resourceLoading and playerMapping
 */
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AnimationInit {


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ResourceLocation resourceLocation = new ResourceLocation(MODID, "animation");
        //Set the player construct callback. It can be a lambda function.
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                resourceLocation,
                42,
                AnimationInit::registerPlayerAnimation);
    }

    //This method will set your mods animation into the library.
    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        //This will be invoked for every new player
        return new ModifierLayer<>();
    }

}
