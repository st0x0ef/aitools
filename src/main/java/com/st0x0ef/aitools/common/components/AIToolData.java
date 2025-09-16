package com.st0x0ef.aitools.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.aitools.common.config.Config;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public record AIToolData(Map<ResourceLocation, Integer> blocksBrokenData, int fortuneLevel, int radiusLevel) implements Serializable {
    public static final Codec<AIToolData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("blocksBrokenData").forGetter(AIToolData::blocksBrokenData),
            Codec.INT.fieldOf("fortuneLevel").forGetter(AIToolData::fortuneLevel),
            Codec.INT.fieldOf("radiusLevel").forGetter(AIToolData::radiusLevel)
    ).apply(instance, AIToolData::new));



    public static final StreamCodec<ByteBuf, AIToolData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.VAR_INT), AIToolData::blocksBrokenData, ByteBufCodecs.INT, AIToolData::fortuneLevel, ByteBufCodecs.INT, AIToolData::radiusLevel, AIToolData::new);

    public static AIToolData add(AIToolData data1, AIToolData data2) {
        int newFortuneLevel = Math.min(data1.fortuneLevel() + data2.fortuneLevel(), Config.MAX_FORTUNE_LEVEL.getAsInt());
        int newRadiusLevel = Math.min(data1.radiusLevel() + data2.radiusLevel(), Config.MAX_MINING_RADIUS.getAsInt());

        AIToolData dataToReturn = new AIToolData(data1.blocksBrokenData(), newFortuneLevel, newRadiusLevel);
        data2.blocksBrokenData().forEach((location, amount) -> {
            dataToReturn.blocksBrokenData.computeIfPresent(location, (location1, actualAmount) -> actualAmount + amount);
            dataToReturn.blocksBrokenData.putIfAbsent(location, amount);
        });
        return dataToReturn;
    }
}