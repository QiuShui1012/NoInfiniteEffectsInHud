package zh.qiushui.mod.nieih;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Nieih.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLED = BUILDER
        .translation("config.nieih.enabled.desc")
        .define("enabled", true);

    public static final ForgeConfigSpec.BooleanValue DISPLAY_INFINITE = BUILDER
        .translation("config.nieih.displayInfinite.desc")
        .define("displayInfinite", false);

    public static final ForgeConfigSpec.BooleanValue DISPLAY_NON_INFINITE = BUILDER
        .translation("config.nieih.displayNonInfinite.desc")
        .define("displayNonInfinite", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();
}
