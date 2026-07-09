package com.qiushui1012.mod.nieih;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLED = BUILDER
        .define("enabled", true);

    public static final ForgeConfigSpec.BooleanValue INFINITE = BUILDER
        .define("duration.infinite", false);

    public static final ForgeConfigSpec.BooleanValue NON_INFINITE = BUILDER
        .define("duration.nonInfinite", true);

    public static final ForgeConfigSpec.BooleanValue BENEFICIAL = BUILDER
        .define("category.beneficial", true);

    public static final ForgeConfigSpec.BooleanValue NEUTRAL = BUILDER
        .define("category.neutral", true);

    public static final ForgeConfigSpec.BooleanValue HARMFUL = BUILDER
        .define("category.harmful", true);

    //#if MC == 1_20_01
    //$$ public static final ForgeConfigSpec.BooleanValue CM_COMPAT = BUILDER
    //$$     .define("compat.cmCompat", true);
    //#endif

    static final ForgeConfigSpec SPEC = BUILDER.build();
}
