package zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration;

import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;

public class IntegrationHook {
    @Getter
    @Setter
    private static IEventBus modEventBus = null;
    @Getter
    @Setter
    private static ModContainer modContainer = null;
}
