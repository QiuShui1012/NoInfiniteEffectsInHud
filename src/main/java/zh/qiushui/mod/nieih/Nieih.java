package zh.qiushui.mod.nieih;

import zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration.IntegrationHook;
import zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration.IntegrationManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkConstants;
import zh.qiushui.mod.nieih.client.NieihClient;

@Mod(Nieih.MOD_ID)
public class Nieih {
    public static final String MOD_ID = "nieih";
    private static final IntegrationManager INTEGRATION_MANAGER = new IntegrationManager();

    @SuppressWarnings("removal")
    public Nieih() {
        var ctx = FMLJavaModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        ctx.registerDisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true);

        if (FMLLoader.getDist() != Dist.CLIENT) return;
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> NieihClient::init);

        IntegrationHook.setModEventBus(ctx.getModEventBus());
        IntegrationHook.setModContainer(ctx.getContainer());
        Nieih.INTEGRATION_MANAGER.compileContent();
        Nieih.INTEGRATION_MANAGER.loadAllIntegrations();
    }
}
