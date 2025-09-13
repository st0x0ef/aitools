package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.menus.ComputerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypesRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(Registries.MENU, AITools.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ComputerMenu>> COMPUTER = MENU_TYPE.register("computer", () -> new MenuType<>(ComputerMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
