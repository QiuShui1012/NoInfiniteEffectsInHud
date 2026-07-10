package com.qiushui1012.mod.nieih;

import com.qiushui1012.mod.nieih.client.gui.ConfigScreen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

//#if MC < 1_17_01
import net.minecraftforge.fml.ExtensionPoint;
//#else
//$$ import net.minecraftforge.fmlclient.ConfigGuiHandler;
//#endif

//#if MC == 1_20_01
//$$ import com.qiushui1012.mod.nieih.integration.compositematerial.CMCompat;
//$$ import net.minecraftforge.api.distmarker.Dist;
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent;
//$$ import net.minecraftforge.fml.DistExecutor;
//$$ import net.minecraftforge.fml.ModList;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//#endif

//#if MC < 1_21_01
import net.minecraftforge.fml.ModLoadingContext;
//#else
//$$ import net.neoforged.fml.ModContainer;
//$$ import java.util.function.Supplier;
//#endif

@Mod(NoInfiniteEffectsInHUD.MOD_ID)
public class NoInfiniteEffectsInHUD {
    public static final String MOD_ID = "nieih";

    //#if MC >= 1_19_00 && MC < 1_21_01
    //$$ @SuppressWarnings("removal")
    //#endif
    public NoInfiniteEffectsInHUD(
        //#if MC >= 1_21_01
        //$$ ModContainer container
        //#endif
    ) {
        //#if FORGE || MC < 1_21_01
        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#else
        //$$ container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#endif

        //#if MC < 1_17_01
        ctx.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new ConfigScreen(screen));
        //#else
        //$$ ctx.registerExtensionPoint(
        //$$     ConfigGuiHandler.ConfigGuiFactory.class,
        //$$     () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> new ConfigScreen(screen))
        //$$ );
        //#endif
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
