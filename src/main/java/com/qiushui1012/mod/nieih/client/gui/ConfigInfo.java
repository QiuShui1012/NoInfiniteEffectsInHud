package com.qiushui1012.mod.nieih.client.gui;

import lombok.Getter;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//#if MC < 1_19_00
import net.minecraft.network.chat.TranslatableComponent;
//#endif

@Getter
public class ConfigInfo {
    private final String path;
    private final Component name;
    private final List<Component> descriptions;
    private final ConfigType type;

    private ConfigInfo(String path, String baseKey, List<Component> descriptions, ConfigType type) {
        this.path = path;
        this.name = new TranslatableComponent(baseKey);
        this.descriptions = descriptions;
        this.type = type;
    }

    public static ConfigInfo singleDesc(String path, String baseKey, ConfigType type) {
        return new ConfigInfo(path, baseKey,
            Collections.singletonList(new TranslatableComponent(baseKey + ".desc")), type);
    }

    public static ConfigInfo multipleDesc(String path, String baseKey, int descCount, ConfigType type) {
        List<Component> descriptions = new ArrayList<>();
        for (int index = 0; index < descCount; index++) {
            descriptions.add(new TranslatableComponent(baseKey + ".desc." + index));
        }
        return new ConfigInfo(path, baseKey, Collections.unmodifiableList(descriptions), type);
    }
}
