package zh.qiushui.mod.nieih.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;
import zh.qiushui.mod.nieih.Config;
import zh.qiushui.mod.nieih.util.EffectUtil;

@Mixin(value = IClientMobEffectExtensions.class, priority = 10)
public interface IClientMobEffectExtensionsMixin {
    @SuppressWarnings("RedundantIfStatement")
    @WrapMethod(method = "isVisibleInGui")
    default boolean shouldRender(MobEffectInstance instance, Operation<Boolean> operation) {
        if (!operation.call(instance)) return false;
        if (!Config.ENABLED.get()) return false;

        boolean durationCheck = switch ((Boolean) EffectUtil.isEffectInfinite(instance)) {
            case Boolean b when b -> Config.INFINITE.getAsBoolean();
            case Boolean ignored -> Config.NON_INFINITE.getAsBoolean();
        };
        if (!durationCheck) return false;

        boolean categoryCheck = switch (instance.getEffect().value().getCategory()) {
            case BENEFICIAL -> Config.BENEFICIAL.getAsBoolean();
            case NEUTRAL -> Config.NEUTRAL.getAsBoolean();
            case HARMFUL -> Config.HARMFUL.getAsBoolean();
        };
        if (!categoryCheck) return false;

        return true;
    }
}
