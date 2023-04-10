package com.windokkstudio.militaryelements.items.functions;

import com.windokkstudio.militaryelements.MilitaryElements;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

public class SyringeRightClicked{
    public static FirstPersonMode execute(LevelAccessor world, Player player_, double x, double y, double z) {

        if (player_ == null)
            return null;
        else {
            player_.setHealth(20);
        }

        /*
         * Animation Registering and Playing
         * */
        var player = Minecraft.getInstance().level.getPlayerByUUID(player_.getUUID());
        if (player == null)
            return null;
        ResourceLocation resourceLocation2 = new ResourceLocation(MilitaryElements.MODID, "animation");
        //Get the animation for that player
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(resourceLocation2);
        if (animation != null) {
            ResourceLocation resourceLocation1 = new ResourceLocation("militaryelements", "syringue");
            animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(resourceLocation1)).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowLeftArm(true).setShowRightArm(true)));
            if (world instanceof Level _level) {
                /*
                * Sound Registering and Playing
                * */
                if (!_level.isClientSide()) {
                    _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("militaryelements", "syringe_sound")), SoundSource.NEUTRAL, 1, 1);
                } else {
                    _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("militaryelements", "syringe_sound")), SoundSource.NEUTRAL, 1, 1, false);
                }
            }
        }

        return null;
    }
}
