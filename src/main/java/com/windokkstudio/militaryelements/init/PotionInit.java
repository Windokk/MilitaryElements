package com.windokkstudio.militaryelements.init;

import com.windokkstudio.militaryelements.MilitaryElements;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionInit {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MilitaryElements.MODID);

    public static final RegistryObject<Potion> ADRENALINE = POTIONS.register("adrenaline",
            () -> new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 0, false, true)));

}
