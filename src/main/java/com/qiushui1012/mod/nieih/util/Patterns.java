package com.qiushui1012.mod.nieih.util;

import net.minecraft.network.chat.Component;
//#if MC < 1_19_00
import net.minecraft.network.chat.TranslatableComponent;
//#endif
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

    @Pattern
    public static Component translatable(String key) {
        //#if MC < 1_19_00
        return new TranslatableComponent(key);
        //#else
        //$$ return Component.translatable(key);
        //#endif
    }

    @Pattern
    public static Component translatable(String key, Object... args) {
        //#if MC < 1_19_00
        return new TranslatableComponent(key, args);
        //#else
        //$$ return Component.translatable(key, args);
        //#endif
    }
}
