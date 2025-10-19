package zh.qiushui.mod.nieih;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import zh.qiushui.mod.nieih.client.NieihClient;

@Mod(Nieih.MOD_ID)
public class Nieih {
    public static final String MOD_ID = "nieih";

    @SuppressWarnings("removal")
    public Nieih() {
        var ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        ctx.registerDisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> NieihClient::init);
    }
}
