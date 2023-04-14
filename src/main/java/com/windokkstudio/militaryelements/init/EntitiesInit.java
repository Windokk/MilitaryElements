package com.windokkstudio.militaryelements.init;

import com.google.common.collect.ImmutableSet;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntitiesInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MilitaryElements.MODID);

    public static final RegistryObject<EntityType<JeepEntity>> JEEP = register("jeep",
            EntityType.Builder.<JeepEntity>of(JeepEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

                    .sized(0.6f, 1.8f));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
    }


}


