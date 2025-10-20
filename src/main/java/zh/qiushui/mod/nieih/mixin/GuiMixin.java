package zh.qiushui.mod.nieih.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zh.qiushui.mod.nieih.Config;
import zh.qiushui.mod.nieih.util.EffectUtil;

@Mixin(value = Gui.class, priority = 10)
public class GuiMixin {
    @WrapOperation(
        method = "renderEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/client/extensions/common/IClientMobEffectExtensions;"
                     + "isVisibleInGui(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
            remap = false
        )
    )
    private boolean shouldRender(
        IClientMobEffectExtensions inst, MobEffectInstance instance, Operation<Boolean> operation
    ) {
        boolean original = operation.call(inst, instance);
        return Config.ENABLED.get()
               ? (EffectUtil.isEffectInfinite(instance) ? Config.DISPLAY_INFINITE.get() : Config.DISPLAY_NON_INFINITE.get()) && original
               : original;
    }
}
