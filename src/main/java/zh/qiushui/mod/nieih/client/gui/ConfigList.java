package zh.qiushui.mod.nieih.client.gui;

import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import zh.qiushui.mod.nieih.util.ExceptionUtil;
import zh.qiushui.mod.nieih.util.VersionUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ConfigList {
    static final MethodHandle CONSTRUCTOR_ENTRIES;
    static final MethodHandle CONSTRUCTOR_CATEGORY;

    static {
        var lookup = MethodHandles.lookup();
        Class<?> clazz;
        try {
            if (VersionUtil.getVersion() < 20.3) {
                clazz = Class.forName("zh.qiushui.mod.nieih.client.gui.ConfigList1202");
            } else {
                clazz = Class.forName("zh.qiushui.mod.nieih.client.gui.ConfigListAfter1202");
            }
        } catch (ClassNotFoundException e) {
            throw ExceptionUtil.unexpected("no ConfigListImpl", e);
        }
        try {
            CONSTRUCTOR_ENTRIES = lookup.findConstructor(clazz, MethodType.methodType(void.class, ConfigScreen.Entry[].class));
            CONSTRUCTOR_CATEGORY = lookup.findConstructor(clazz, MethodType.methodType(void.class, ConfigScreen.CategoryEntry.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw ExceptionUtil.unexpected("no two constructors in ConfigListImpl", e);
        }
    }

    @SuppressWarnings({"unchecked", "ConfusingArgumentToVarargsMethod"})
    static ContainerObjectSelectionList<ConfigScreen.Entry> of(ConfigScreen.Entry... entries) {
        try {
            return (ContainerObjectSelectionList<ConfigScreen.Entry>) CONSTRUCTOR_ENTRIES.invoke(entries);
        } catch (Throwable e) {
            throw ExceptionUtil.cannot("create ConfigList with entries", e);
        }
    }

    @SuppressWarnings("unchecked")
    static ContainerObjectSelectionList<ConfigScreen.Entry> of(ConfigScreen.CategoryEntry category) {
        try {
            return (ContainerObjectSelectionList<ConfigScreen.Entry>) CONSTRUCTOR_CATEGORY.invoke(category);
        } catch (Throwable e) {
            throw ExceptionUtil.cannot("create ConfigList with category", e);
        }
    }

    @SuppressWarnings({"unchecked", "ConfusingArgumentToVarargsMethod"})
    static ContainerObjectSelectionList<ConfigScreen.Entry> recreate(ContainerObjectSelectionList<ConfigScreen.Entry> list) {
        try {
            return (ContainerObjectSelectionList<ConfigScreen.Entry>) CONSTRUCTOR_ENTRIES.invoke(
                list.children().toArray(ConfigScreen.Entry[]::new)
            );
        } catch (Throwable e) {
            throw ExceptionUtil.cannot("recreate ConfigList with entries", e);
        }
    }
}
