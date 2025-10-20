package zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@Slf4j
@Getter
@EqualsAndHashCode
public final class IntegrationInstance {
    private final String modId;
    private final ModVersionRange versionRange;
    private final String className;
    private Class<?> clazz;
    private Object instance;
    private MethodHandle constructor;
    private MethodHandle loader;

    @SneakyThrows
    public IntegrationInstance(String modId, ModVersionRange versionRange, String className) {
        this.modId = modId;
        this.versionRange = versionRange;
        this.className = className;
    }

    @SneakyThrows
    public void newInstance() {
        if (this.clazz == null) {
            this.clazz = Class.forName(className);
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            this.constructor = lookup.findConstructor(clazz, MethodType.methodType(void.class));
            MethodHandle loader;
            try {
                loader = lookup.findVirtual(clazz, "apply", MethodType.methodType(void.class));
            } catch (Throwable e) {
                loader = null;
            }
            this.loader = loader;
            if (this.loader == null) {
                log.warn("Integration {} does not declare any loader method.", className);
            }
        }
        if (instance == null) {
            instance = constructor.invoke();
        }
    }

    @SneakyThrows
    public void invoke() {
        if (loader != null) {
            loader.invoke(instance);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean is(ModInfo modInfo) {
        return modId.equals(modInfo.getModId()) && versionRange.containsVersion(modInfo.getVersion());
    }

    @Override
    public String toString() {
        return "IntegrationInstance["
               + "mod_id=" + modId + ", "
               + "instance=" + instance + ", "
               + "loader=" + loader
               + ']';
    }
}
