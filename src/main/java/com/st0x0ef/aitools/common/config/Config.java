package com.st0x0ef.aitools.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_MINING_RADIUS = BUILDER
            .comment("The maximum mining radius level for the AI Tools. The radius is calculated as (2 * radiusLevel + 1).")
            .defineInRange("maxMiningRadius", 5, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_FORTUNE_LEVEL = BUILDER
            .comment("The maximum fortune level for the AI Tools.")
            .defineInRange("maxFortuneLevel", 5, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_SPEED_MULTIPLIER = BUILDER
            .comment("The maximum speed multiplier for the AI Tools.")
            .defineInRange("maxSpeedMultiplier", 5, 1, Integer.MAX_VALUE);


    public static final ModConfigSpec SPEC = BUILDER.build();
}