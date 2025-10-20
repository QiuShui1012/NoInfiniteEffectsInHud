package zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Integration {
    String value();

    String version() default "*";
}
