package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.blocks.entities.ComputerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntitiesRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AITools.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ComputerBlockEntity>> COMPUTER = BLOCK_ENTITY_TYPE.register("computer", () -> BlockEntityType.Builder.of(ComputerBlockEntity::new, BlocksRegistry.COMPUTER.get()).build(null));
}
