package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.blocks.ComputerBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlocksRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AITools.MODID);

    public static final DeferredBlock<Block> COMPUTER = BLOCKS.registerBlock("computer", ComputerBlock::new, Block.Properties.of().strength(2.5f).noOcclusion());
}
