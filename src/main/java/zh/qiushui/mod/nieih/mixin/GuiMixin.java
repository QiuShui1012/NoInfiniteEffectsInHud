package zh.qiushui.mod.nieih.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zh.qiushui.mod.nieih.Config;
import zh.qiushui.mod.nieih.util.EffectUtil;

@Mixin(Gui.class)
public class GuiMixin {
    @Definition(id = "isVisibleInGui", method = "Lnet/minecraftforge/client/extensions/common/IClientMobEffectExtensions;isVisibleInGui(Lnet/minecraft/world/effect/MobEffectInstance;)Z")
    @Expression("?.isVisibleInGui(?)")
    @ModifyExpressionValue(
        method = "renderEffects",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean shouldRender(
        boolean original,
        @SuppressWarnings("LocalMayBeArgsOnly") @Local MobEffectInstance instance
    ) {
        return Config.ENABLED.get()
               ? (EffectUtil.isEffectInfinite(instance) ? Config.DISPLAY_INFINITE.get() : Config.DISPLAY_NON_INFINITE.get()) && original
               : original;
    }
}
