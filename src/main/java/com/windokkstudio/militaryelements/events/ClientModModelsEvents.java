package com.windokkstudio.militaryelements.events;


import com.windokkstudio.militaryelements.MilitaryElements;
import com.windokkstudio.militaryelements.render.models.JeepModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = MilitaryElements.MODID, bus = Bus.MOD, value = {Dist.CLIENT})
public class ClientModModelsEvents {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JeepModel.LAYER_LOCATION, JeepModel::createBodyLayer);
    }

}
