package com.st0x0ef.aitools.common.DataComponents;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public record BlocksBrokenToolData(Map<ResourceLocation, Integer> blocksBrokenData) implements Serializable {
    public static final Codec<BlocksBrokenToolData> CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT)
                    .xmap(BlocksBrokenToolData::new, BlocksBrokenToolData::blocksBrokenData);



    public static final StreamCodec<ByteBuf, BlocksBrokenToolData> STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.VAR_INT)
                    .map(BlocksBrokenToolData::new, (data) -> new HashMap<>(data.blocksBrokenData()));
}