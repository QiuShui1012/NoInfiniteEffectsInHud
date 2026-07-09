package com.qiushui1012.mod.nieih.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.qiushui1012.mod.nieih.Config;
import com.qiushui1012.mod.nieih.util.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = IClientMobEffectExtensions.class, priority = 10, remap = false)
public interface IClientMobEffectExtensionsMixin {
    @SuppressWarnings("RedundantIfStatement")
    @WrapMethod(method = "isVisibleInGui", remap = false)
    default boolean shouldRender(MobEffectInstance instance, Operation<Boolean> operation) {
        if (!operation.call(instance)) return false;
        if (!Config.ENABLED.get()) return false;

        boolean durationCheck = EffectUtil.isEffectInfinite(instance) ? Config.INFINITE.get() : Config.NON_INFINITE.get();
        if (!durationCheck) return false;

        boolean categoryCheck = switch (instance.getEffect().getCategory()) {
            case BENEFICIAL -> Config.BENEFICIAL.get();
            case NEUTRAL -> Config.NEUTRAL.get();
            case HARMFUL -> Config.HARMFUL.get();
        };
        if (!categoryCheck) return false;

        return true;
    }
}
