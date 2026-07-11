package com.qiushui1012.mod.nieih;

import com.qiushui1012.mod.nieih.client.NoInfiniteEffectsInHUDClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

//#if MC == 1_20_01
//$$ import com.qiushui1012.mod.nieih.integration.compositematerial.CMCompat;
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//$$ import net.minecraftforge.fml.DistExecutor;
//$$ import net.minecraftforge.fml.ModList;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//#endif

//#if MC < 1_21_01
import net.minecraftforge.fml.ModLoadingContext;
//#else
//$$ import net.neoforged.fml.ModContainer;
//#endif

@Mod(NoInfiniteEffectsInHUD.MOD_ID)
public class NoInfiniteEffectsInHUD {
    public static final String MOD_ID = "nieih";

    //#if MC >= 1_19_00 && MC < 1_21_01
    //$$ @SuppressWarnings("removal")
    //#endif
    public NoInfiniteEffectsInHUD(
        //#if MC >= 1_21_01
        //$$ ModContainer ctx
        //#endif
    ) {
        //#if FORGE || MC < 1_21_01
        ModLoadingContext ctx = ModLoadingContext.get();
        //#endif
        ctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        //#if MC < 26_01_00
        Dist current = FMLLoader.getDist();
        //#else
        //$$ Dist current = FMLLoader.getCurrent().getDist();
        //#endif
        if (current.isClient()) {
            NoInfiniteEffectsInHUDClient.registerConfigScreen(ctx);
        }
    }

    //#if MC == 1_20_01
    //$$ @SubscribeEvent
    //$$ public static void onSetup(FMLClientSetupEvent event) {
    //$$     if (ModList.get().isLoaded("compositematerial")) {
    //$$         DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CMCompat::apply);
    //$$     }
    //$$ }
    //#endif
}
