package com.windokkstudio.militaryelements.init;

import com.windokkstudio.militaryelements.MilitaryElements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.windokkstudio.militaryelements.MilitaryElements.MODID;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MilitaryElements.MODID);

    public static final RegistryObject<SoundEvent> SYRINGE_SOUND = SOUNDS.register("syringe_sound",
            () -> new SoundEvent(new ResourceLocation("militaryelements", "syringe_sound")));

}
