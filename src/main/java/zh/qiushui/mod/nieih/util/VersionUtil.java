package zh.qiushui.mod.nieih.util;

import lombok.extern.slf4j.Slf4j;
import net.neoforged.fml.loading.FMLLoader;

@Slf4j
public class VersionUtil {
    private static float version;

    public static float getVersion() {
        if (version != 0f) return version;
        log.debug(FMLLoader.versionInfo().mcVersion().substring(2));
        return version = Float.parseFloat(FMLLoader.versionInfo().mcVersion().substring(2));
    }
}
