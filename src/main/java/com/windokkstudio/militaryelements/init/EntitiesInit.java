package com.windokkstudio.militaryelements.init;

import com.google.common.collect.ImmutableSet;
import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntitiesInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MilitaryElements.MODID);

    public static AABB box1 = new AABB(0,0,0.7F,1,1,1);
    public static AABB box2 = new AABB(0,0,-0.3F,1,1,1);
    public static AABB box3 = new AABB(0,0,-1.1F,1,1,0.6);



    public static final RegistryObject<EntityType<JeepEntity>> JEEP = ENTITY_TYPES.register("jeep", () -> createEntityType(JeepEntity::new, EntityDimensions.scalable(1.3F, 1F)));


    private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, true, ImmutableSet.of(), size, 5, 3);
    }


}


