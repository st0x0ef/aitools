package com.st0x0ef.aitools;

import com.st0x0ef.aitools.common.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(AITools.MODID)
public class AITools {
    public static final String MODID = "aitools";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AITools(IEventBus bus, ModContainer modContainer) {
        DataComponentsRegistry.DATA_COMPONENTS_TYPES.register(bus);
        BlocksRegistry.BLOCKS.register(bus);
        ItemsRegistry.ITEMS.register(bus);
        CreativeTabsRegistry.CREATIVE_MODE_TABS.register(bus);
        BlockEntitiesRegistry.BLOCK_ENTITY_TYPE.register(bus);
        MenuTypesRegistry.MENU_TYPE.register(bus);

        //NeoForge.EVENT_BUS.register(this);

        //modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
