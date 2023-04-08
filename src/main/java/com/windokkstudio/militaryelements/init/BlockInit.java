package com.windokkstudio.militaryelements.init;

import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.blocks.CamouflageNet;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.io.comparator.DefaultFileComparator;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MilitaryElements.MODID);
    public static final RegistryObject<Block> SANDBAGS = register("sandbags",
            () -> new Block(BlockBehaviour.Properties.of(Material.CLOTH_DECORATION).requiresCorrectToolForDrops().strength(1,10)),new Item.Properties().tab(MilitaryElements.TAB));

    public static final RegistryObject<Block> CAMOUFLAGE_NET = register("net",
            () -> new CamouflageNet(),new Item.Properties().tab(MilitaryElements.TAB));


    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties){
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}
