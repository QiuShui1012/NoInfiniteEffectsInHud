package com.qiushui1012.mod.nieih;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

//#if MC < 1_21_01
import net.minecraftforge.fml.ModLoadingContext;
//#else
//$$ import net.neoforged.fml.ModContainer;
//#endif

@Mod(NoInfiniteEffectsInHUD.MOD_ID)
public class NoInfiniteEffectsInHUD {
    public static final String MOD_ID = "nieih";

    @SuppressWarnings("RedundantSuppression")
    public NoInfiniteEffectsInHUD(
        //#if MC >= 1_21_01
        //$$ ModContainer container
        //#endif
    ) {
        //#if FORGE || MC < 1_21_01
        // noinspection removal
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#else
        //$$ container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#endif

        //#if MC == 1_20_01
        //$$ net.minecraftforge.fml.DistExecutor.safeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> com.qiushui1012.mod.nieih.integration.compositematerial.CMCompat::apply);
        //#endif
    }
}
