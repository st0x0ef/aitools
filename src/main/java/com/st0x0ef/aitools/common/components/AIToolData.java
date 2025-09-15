package com.st0x0ef.aitools.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public record AIToolData(Map<ResourceLocation, Integer> blocksBrokenData, int fortuneLevel) implements Serializable {
    public static final Codec<AIToolData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("blocksBrokenData").forGetter(AIToolData::blocksBrokenData),
            Codec.INT.fieldOf("fortuneLevel").forGetter(AIToolData::fortuneLevel)
    ).apply(instance, AIToolData::new));



    public static final StreamCodec<ByteBuf, AIToolData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.VAR_INT), AIToolData::blocksBrokenData, ByteBufCodecs.INT, AIToolData::fortuneLevel, AIToolData::new);

    public static AIToolData add(AIToolData data1, AIToolData data2) {
        AIToolData dataToReturn = new AIToolData(data1.blocksBrokenData(), data1.fortuneLevel() + data2.fortuneLevel());
        data2.blocksBrokenData().forEach((location, amount) -> {
            dataToReturn.blocksBrokenData.computeIfPresent(location, (location1, actualAmount) -> actualAmount + amount);
            dataToReturn.blocksBrokenData.putIfAbsent(location, amount);
        });
        return dataToReturn;
    }
}