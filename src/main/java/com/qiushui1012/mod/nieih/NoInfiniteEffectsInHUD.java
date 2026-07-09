package com.qiushui1012.mod.nieih;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

@Mod(NoInfiniteEffectsInHUD.MOD_ID)
public class NoInfiniteEffectsInHUD {
    public static final String MOD_ID = "nieih";

    @SuppressWarnings({"RedundantSuppression", "unused"})
    public NoInfiniteEffectsInHUD(
        //#if MC >= 1_19_02
        //$$ ModLoadingContext ctx,
        //#endif
        FMLModContainer container
    ) {
        //#if MC < 1_19_02
        // noinspection removal
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#elseif MC < 1_20_06
        //$$ ctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#else
        //$$ container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        //#endif

        //#if MC == 1_20_01
        //$$ net.minecraftforge.fml.DistExecutor.safeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> com.qiushui1012.mod.nieih.integration.compositematerial.CMCompat::apply);
        //#endif
    }
}
