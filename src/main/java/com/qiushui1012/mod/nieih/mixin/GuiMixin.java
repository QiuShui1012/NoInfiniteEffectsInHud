package com.qiushui1012.mod.nieih.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.qiushui1012.mod.nieih.Config;
import com.qiushui1012.mod.nieih.util.EffectUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Gui.class, priority = 10)
public class GuiMixin {
    @Definition(
        id = "shouldRenderHUD",
        //#if MC < 1_17_01
        method = "Lnet/minecraft/world/effect/MobEffectInstance;shouldRenderHUD()Z"
        //#else
        //$$ method = "Lnet/minecraftforge/client/EffectRenderer;shouldRenderHUD(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
        //$$ remap = false
        //#endif
    )
    @Expression(
        //#if MC < 1_17_01
        "?.shouldRenderHUD()"
        //#else
        //$$ "?.shouldRenderHUD(?)"
        //#endif
    )
    @ModifyExpressionValue(method = "renderEffects", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean shouldRender(boolean original, @Local MobEffectInstance instance) {
        if (!original) return false;
        if (!Config.ENABLED.get()) return false;

        boolean durationCheck = EffectUtil.isEffectInfinite(instance) ? Config.INFINITE.get() : Config.NON_INFINITE.get();
        if (!durationCheck) return false;

        boolean categoryCheck = false;
        switch (instance.getEffect().getCategory()) {
            case BENEFICIAL:
                categoryCheck = Config.BENEFICIAL.get();
                break;
            case NEUTRAL:
                categoryCheck = Config.NEUTRAL.get();
                break;
            case HARMFUL:
                categoryCheck = Config.HARMFUL.get();
                break;
        }
        return categoryCheck;
    }
}
