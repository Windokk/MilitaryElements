package com.windokkstudio.militaryelements.items.functions;

import com.windokkstudio.militaryelements.MilitaryElements;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyringeRightClicked{
    public static void execute(LevelAccessor world, double x, double y, double z) {



            if (world instanceof Level _level) {

                if (!_level.isClientSide()) {
                    _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("militaryelements", "syringe_sound")), SoundSource.NEUTRAL, 1, 1);
                }
                else {
                    _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("militaryelements", "syringe_sound")), SoundSource.NEUTRAL, 1, 1, false);
                }
            }





    }
}
