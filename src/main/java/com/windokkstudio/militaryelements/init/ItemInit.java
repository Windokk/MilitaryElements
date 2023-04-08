package com.windokkstudio.militaryelements.init;

import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.items.Adrenaline;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.swing.*;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MilitaryElements.MODID);
    //Items
    public static final RegistryObject<Item> FLAG = ITEMS.register("flag",
            () -> new Item(new Item.Properties().tab(MilitaryElements.TAB)));

    public static final RegistryObject<Item> ADRENALINE = ITEMS.register("adrenaline",
            () -> new Adrenaline(new Item.Properties().tab(MilitaryElements.TAB).stacksTo(64).rarity(Rarity.COMMON)));


}
