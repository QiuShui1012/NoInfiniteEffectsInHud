package com.qiushui1012.mod.nieih.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

@SuppressWarnings("unused")
public class Patterns {
    @Pattern
    public static MobEffect extractFromInstance(MobEffectInstance instance) {
        //#if MC < 1_20_06
        return instance.getEffect();
        //#else
        //$$ return instance.getEffect().value();
        //#endif
    }
}
