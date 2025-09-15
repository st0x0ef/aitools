package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.DataComponents.AIToolData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataComponentsRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AITools.MODID);

    public static final Supplier<DataComponentType<AIToolData>> AI_TOOL_DATA = DATA_COMPONENTS_TYPES.registerComponentType("ai_tool_data",
            builder -> builder.persistent(AIToolData.CODEC).networkSynchronized(AIToolData.STREAM_CODEC));

}
