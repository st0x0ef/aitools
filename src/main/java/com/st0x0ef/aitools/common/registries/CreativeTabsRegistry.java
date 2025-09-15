package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AITools.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AI_TOOLS_TAB = CREATIVE_MODE_TABS.register("aitools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.aitools"))
            .icon(() -> ItemsRegistry.AI_PICKAXE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemsRegistry.AI_PICKAXE.get());
                output.accept(ItemsRegistry.AI_AXE.get());
                output.accept(ItemsRegistry.FORTUNE_GEM.get());
                output.accept(ItemsRegistry.COMPUTER_ITEM.get());
            }).build());
}
