package zh.qiushui.mod.nieih;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED = BUILDER
        .translation("config.nieih.enabled")
        .define("enabled", true);

    public static final ModConfigSpec.BooleanValue INFINITE = BUILDER
        .translation("config.nieih.duration.infinite")
        .define("duration.infinite", false);

    public static final ModConfigSpec.BooleanValue NON_INFINITE = BUILDER
        .translation("config.nieih.duration.nonInfinite")
        .define("duration.nonInfinite", true);

    public static final ModConfigSpec.BooleanValue BENEFICIAL = BUILDER
        .translation("config.nieih.category.beneficial")
        .define("category.beneficial", true);

    public static final ModConfigSpec.BooleanValue NEUTRAL = BUILDER
        .translation("config.nieih.category.neutral")
        .define("category.neutral", true);

    public static final ModConfigSpec.BooleanValue HARMFUL = BUILDER
        .translation("config.nieih.category.harmful")
        .define("category.harmful", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
