package com.qiushui1012.mod.nieih.util;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import java.util.function.Supplier;

public class DistExecutor {
    public static void unsafeRunWhenOn(Dist dist, Supplier<Runnable> run) {
        //#if MC < 26_01_00
        Dist current = FMLLoader.getDist();
        //#else
        //$$ Dist current = FMLLoader.getCurrent().getDist();
        //#endif
        if (current == dist) {
            run.get().run();
        }
    }
}
