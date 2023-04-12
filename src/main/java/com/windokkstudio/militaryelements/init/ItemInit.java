package com.windokkstudio.militaryelements.init;

import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.entities.vehicles.JeepEntity;
import com.windokkstudio.militaryelements.items.vehicles.JeepItem;
import com.windokkstudio.militaryelements.items.consumable.Adrenaline;
import com.windokkstudio.militaryelements.items.armors.Vest_Goggles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MilitaryElements.MODID);
    //Items
    public static final RegistryObject<Item> FLAG = ITEMS.register("flag",
            () -> new Item(new Item.Properties().tab(MilitaryElements.TAB)));

    public static final RegistryObject<Item> ADRENALINE = ITEMS.register("adrenaline",
            () -> new Adrenaline(new Item.Properties().tab(MilitaryElements.TAB).stacksTo(64).rarity(Rarity.COMMON)));

    public static final RegistryObject<Item> GOGGLES = ITEMS.register("vestandgoggles_helmet", () -> new Vest_Goggles.Helmet());

    public static final RegistryObject<Item> VEST = ITEMS.register("vestandgoggles_chestplate", () -> new Vest_Goggles.Chestplate());

    public static final RegistryObject<Item> JEEP = ITEMS.register("jeep",() -> new JeepItem(new Item.Properties().tab(MilitaryElements.TAB),EntitiesInit.JEEP));


}
