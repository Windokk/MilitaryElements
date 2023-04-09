package com.windokkstudio.militaryelements;

import com.mojang.logging.LogUtils;
import com.windokkstudio.militaryelements.init.BlockInit;
import com.windokkstudio.militaryelements.init.ItemInit;
import com.windokkstudio.militaryelements.init.PotionInit;
import com.windokkstudio.militaryelements.init.SoundInit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.Logger;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MilitaryElements.MODID)
public class MilitaryElements
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "militaryelements";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();
    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry(action, tick));
    }


    public MilitaryElements(){
        //Bus loading
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        //Registries Initializers
        SoundInit.SOUNDS.register(bus);
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        PotionInit.POTIONS.register(bus);

    }

    //Registers the creative tab
    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return ItemInit.FLAG.get().getDefaultInstance();
        }
    };


    private void commonSetup(final FMLCommonSetupEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {}
    }
}
