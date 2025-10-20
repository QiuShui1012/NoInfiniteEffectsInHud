package zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import lombok.extern.slf4j.Slf4j;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.fml.loading.progress.ProgressMeter;
import net.minecraftforge.fml.loading.progress.StartupNotificationManager;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import java.lang.annotation.ElementType;
import java.util.List;

@Slf4j
public class IntegrationManager {
    private final Multimap<String, IntegrationInstance> instances = MultimapBuilder.hashKeys().hashSetValues().build();

    public static final String INTEGRATION_NAME = "L" + Integration.class.getName().replace(".", "/") + ";";

    public IntegrationManager() {
    }

    public void compileContent() {
        IModFileInfo fileInfo = IntegrationHook.getModContainer().getModInfo().getOwningFile();
        ModFileScanData scanData = fileInfo.getFile().getScanResult();
        List<ModFileScanData.AnnotationData> list = scanData.getAnnotations()
            .stream()
            .filter(annotation -> annotation.annotationType().getDescriptor().equals(INTEGRATION_NAME)
                                  && annotation.targetType() == ElementType.TYPE
            )
            .toList();
        log.info("Load Integrations: {}", list.size());
        ProgressMeter meter = StartupNotificationManager.addProgressBar("Load Integrations", list.size());
        for (ModFileScanData.AnnotationData annotation : list) {
            String modId = (String) annotation.annotationData().get("value");
            String version = (String) annotation.annotationData().get("version");
            if (version == null) version = "*";
            log.info("Considering integration {} for {id:{}, version:{}}", annotation.memberName(), modId, version);
            IntegrationInstance instance = new IntegrationInstance(modId, ModVersionRange.of(version), annotation.memberName());
            this.instances.put(modId, instance);
        }
        StartupNotificationManager.popBar(meter);
    }

    public void load(String modId, ModInfo info) {
        for (IntegrationInstance instance : instances.get(modId)) {
            if (!instance.is(info)) continue;
            instance.newInstance();
            log.info("Loading integration {} for {}.", instance.getInstance(), modId);
            instance.invoke();
        }
    }

    public void loadAllIntegrations() {
        if (FMLLoader.getDist().isDedicatedServer()) return;
        for (String key : this.instances.keySet()) {
            LoadingModList.get()
                .getMods()
                .stream()
                .filter(it -> it.getModId().equals(key))
                .findFirst()
                .ifPresent(modInfo -> this.load(key, modInfo));
        }
    }
}
