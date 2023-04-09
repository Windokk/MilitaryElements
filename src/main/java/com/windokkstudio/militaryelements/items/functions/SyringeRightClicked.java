package com.windokkstudio.militaryelements.items.functions;

import com.windokkstudio.militaryelements.MilitaryElements;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyringeRightClicked{
    public static void execute(LevelAccessor world, Player player_, double x, double y, double z) {

        var player = Minecraft.getInstance().level.getPlayerByUUID(player_.getUUID());

        if (player == null)
            return; //The player can be null because it was a system message or because it is not loaded by this player.

        ResourceLocation resourceLocation2 = new ResourceLocation(MilitaryElements.MODID, "animation");
        //Get the animation for that player
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(resourceLocation2);
        if (animation != null) {
            ResourceLocation resourceLocation1 = new ResourceLocation("militaryelements", "syringue");
            animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(resourceLocation1)));
            if (world instanceof Level _level) {

                if (!_level.isClientSide()) {
                    _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("militaryelements", "syringe_sound")), SoundSource.NEUTRAL, 1, 1);
                } else {
                    _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("militaryelements", "syringe_sound")), SoundSource.NEUTRAL, 1, 1, false);
                }
            }
        }
    }
}
