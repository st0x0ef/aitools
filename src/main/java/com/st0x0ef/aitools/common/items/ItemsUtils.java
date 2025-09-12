package com.st0x0ef.aitools.common.items;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ItemsUtils {
    public static float getMiningSpeedMultiplier(Map<ResourceLocation, Integer> blocksBrokenMap, ResourceLocation blockLocation) {
        int blocksBroken = blocksBrokenMap.getOrDefault(blockLocation, 0);

        float speedMultiplier;

        if (blocksBroken < 110) {
            speedMultiplier = 1f;
        } else if(blocksBroken < 500) {
            speedMultiplier = (float) blocksBroken / 390f + 28f/39f;
        } else {
            speedMultiplier = (float) Math.log10(blocksBroken - 400f);
        }

        return speedMultiplier;
    }
}
