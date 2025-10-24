package zh.qiushui.mod.nieih.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import zh.qiushui.mod.nieih.util.ClientMobEffectExtensionsWrapper;

@Mixin(value = IClientMobEffectExtensions.class, priority = 10)
public interface IClientMobEffectExtensionsMixin {
    /**
     * @author QiuShui1012
     * @reason Wrap extensions and control all api-based effects rendering in gui
     */
    @Overwrite(remap = false)
    static IClientMobEffectExtensions of(MobEffect effect) {
        return new ClientMobEffectExtensionsWrapper(
            effect.getEffectRendererInternal() instanceof IClientMobEffectExtensions r
            ? r
            : IClientMobEffectExtensions.DEFAULT
        );
    }
}
