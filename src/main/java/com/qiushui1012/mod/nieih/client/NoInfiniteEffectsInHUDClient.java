package com.qiushui1012.mod.nieih.client;

import com.qiushui1012.mod.nieih.client.gui.ConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//#if MC < 1_17_01
import net.minecraftforge.fml.ExtensionPoint;
//#elseif MC < 1_20_06
//$$ import net.minecraftforge.fmlclient.ConfigGuiHandler;
//#else
//$$ import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//#endif

//#if MC < 1_21_01
import net.minecraftforge.fml.ModLoadingContext;
//#else
//$$ import net.neoforged.fml.ModContainer;
//#endif

public class NoInfiniteEffectsInHUDClient {
    @OnlyIn(Dist.CLIENT)
    public static void registerConfigScreen(
        //#if MC < 1_21_01
        ModLoadingContext ctx
        //#else
        //$$ ModContainer ctx
        //#endif
    ) {
        //#if MC < 1_17_01
        ctx.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new ConfigScreen(screen));
        //#elseif MC < 1_20_06
        //$$ ctx.registerExtensionPoint(
        //$$     ConfigGuiHandler.ConfigGuiFactory.class,
        //$$     () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> new ConfigScreen(screen))
        //$$ );
        //#else
        //$$ ctx.registerExtensionPoint(IConfigScreenFactory.class, (mc, screen) -> new ConfigScreen(screen));
        //#endif
    }
}
