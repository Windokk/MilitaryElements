package com.windokkstudio.militaryelements.init;

import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.items.Adrenaline;
import com.windokkstudio.militaryelements.items.Vest_Goggles;
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

    public static final RegistryObject<Item> VESTANDGOGGLES_HELMET = ITEMS.register("vestandgoggles_helmet", () -> new Vest_Goggles.Helmet());

    public static final RegistryObject<Item> VESTANDGOGGLES_CHESTPLATE = ITEMS.register("vestandgoggles_chestplate", () -> new Vest_Goggles.Chestplate());



}
