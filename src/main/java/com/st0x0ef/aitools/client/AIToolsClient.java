package com.st0x0ef.aitools.client;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.client.screens.ComputerScreen;
import com.st0x0ef.aitools.common.registries.MenuTypesRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = AITools.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AITools.MODID, value = Dist.CLIENT)
public class AIToolsClient {
    public AIToolsClient(ModContainer container) {

    }

    @SubscribeEvent
    public static void registerScreen(RegisterMenuScreensEvent event) {
        event.register(MenuTypesRegistry.COMPUTER.get(), ComputerScreen::new);
    }
}
