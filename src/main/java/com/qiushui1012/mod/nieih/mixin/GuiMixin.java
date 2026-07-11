package com.qiushui1012.mod.nieih.mixin;

import com.qiushui1012.mod.nieih.Config;
import com.qiushui1012.mod.nieih.util.EffectUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//#if MC < 1_19_00

//#if MC >= 1_17_01
//$$ import net.minecraftforge.client.EffectRenderer;
//#endif

//#else
//$$ import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
//#endif

@Mixin(value = Gui.class, priority = 10)
public class GuiMixin {
    @Redirect(
        method = "renderEffects",
        at = @At(
            value = "INVOKE",
            //#if MC < 1_17_01
            target = "Lnet/minecraft/world/effect/MobEffectInstance;shouldRenderHUD()Z"
            //#elseif MC < 1_19_00
            //$$ target = "Lnet/minecraftforge/client/EffectRenderer;shouldRenderHUD(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
            //#elseif FORGE
            //$$ target = "Lnet/minecraftforge/client/extensions/common/IClientMobEffectExtensions;"
            //$$          + "isVisibleInGui(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
            //#else
            //$$ target = "Lnet/neoforged/neoforge/client/extensions/common/IClientMobEffectExtensions;"
            //$$          + "isVisibleInGui(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
            //#endif

            //#if MC >= 1_17_01
            //$$ remap = false
            //#endif
        )
    )
    private boolean shouldRender(
        //#if MC < 1_19_00

        //#if MC >= 1_17_01
        //$$ EffectRenderer instance,
        //#endif

        //#else
        //$$ IClientMobEffectExtensions instance,
        //#endif
        MobEffectInstance effect
    ) {
        //#if MC < 1_17_01
        if (!effect.shouldRenderHUD()) return false;
        //#elseif MC < 1_19_00
        //$$ if (!instance.shouldRenderHUD(effect)) return false;
        //#else
        //$$ if (!instance.isVisibleInGui(effect)) return false;
        //#endif
        if (!Config.ENABLED.get()) return false;

        boolean durationCheck = EffectUtil.isEffectInfinite(effect) ? Config.INFINITE.get() : Config.NON_INFINITE.get();
        if (!durationCheck) return false;

        boolean categoryCheck = false;
        switch (effect.getEffect().getCategory()) {
            case BENEFICIAL:
                categoryCheck = Config.BENEFICIAL.get();
                break;
            case NEUTRAL:
                categoryCheck = Config.NEUTRAL.get();
                break;
            case HARMFUL:
                categoryCheck = Config.HARMFUL.get();
                break;
            default:
        }
        return categoryCheck;
    }
}
