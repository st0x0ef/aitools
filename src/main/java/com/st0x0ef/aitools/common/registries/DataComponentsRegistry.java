package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.DataComponents.BlocksBrokenToolData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataComponentsRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AITools.MODID);

    public static final Supplier<DataComponentType<BlocksBrokenToolData>> BLOCKS_BROKEN_MAP = DATA_COMPONENTS_TYPES.registerComponentType("blocks_broken_map",
            builder -> builder.persistent(BlocksBrokenToolData.CODEC).networkSynchronized(BlocksBrokenToolData.STREAM_CODEC));

}
