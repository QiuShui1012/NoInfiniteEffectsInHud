package zh.qiushui.mod.nieih.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import zh.qiushui.mod.nieih.client.gui.ConfigScreen;

@OnlyIn(Dist.CLIENT)
public class NieihClient {
    public static final ConfigScreenHandler.ConfigScreenFactory FACTORY =
        new ConfigScreenHandler.ConfigScreenFactory(ConfigScreen::new);

    @SuppressWarnings("removal")
    public static void init() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> FACTORY);
    }
}
