package zh.qiushui.mod.nieih;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import zh.qiushui.mod.nieih.client.gui.ConfigScreen;
import zh.qiushui.mod.nieih.util.ExceptionUtil;
import zh.qiushui.mod.nieih.util.VersionUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiFunction;

@Mod(value = Nieih.MOD_ID, dist = Dist.CLIENT)
public class Nieih {
    public static final String MOD_ID = "nieih";

    public Nieih(FMLModContainer modContainer) {
        Nieih.init(modContainer);
    }

    @SuppressWarnings("JavaLangInvokeHandleSignature")
    private static void init(FMLModContainer container) {
        var lookup = MethodHandles.lookup();

        if (VersionUtil.getVersion() < 20.3) {
            try {
                lookup.findVirtual(
                    ModLoadingContext.class,
                    "registerConfig",
                    MethodType.methodType(void.class, ModConfig.Type.class, IConfigSpec.class)
                ).invoke(ModLoadingContext.get(), ModConfig.Type.CLIENT, Config.SPEC);
            } catch (Throwable e) {
                throw ExceptionUtil.unexpected("no ModLoadingContext.registerConfig in 1.20.2", e);
            }
        } else {
            try {
                lookup.findVirtual(
                    FMLModContainer.class,
                    "registerConfig",
                    MethodType.methodType(void.class, ModConfig.Type.class, IConfigSpec.class)
                ).invoke(container, ModConfig.Type.CLIENT, Config.SPEC);
            } catch (Throwable e) {
                throw ExceptionUtil.unexpected("no FMLModContainer.registerConfig in 1.20.3+", e);
            }
        }

        if (VersionUtil.getVersion() <= 20.4) {
            try {
                Class<?> clazz = Class.forName("net.neoforged.neoforge.client.ConfigScreenHandler$ConfigScreenFactory");
                MethodHandle cons = lookup.findConstructor(clazz, MethodType.methodType(void.class, BiFunction.class));
                ModLoadingContext.get().registerExtensionPoint(
                    cast(clazz),
                    () -> {
                        try {
                            return cast(cons.invoke((BiFunction<Minecraft, Screen, Screen>) ConfigScreen::new));
                        } catch (Throwable e) {
                            throw ExceptionUtil.cannot("create ConfigScreenHandler$ConfigScreenFactory instance", e);
                        }
                    }
                );
            } catch (ClassNotFoundException e) {
                throw ExceptionUtil.unexpected("no ConfigScreenHandler$ConfigScreenFactory in 1.20.4-", e);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw ExceptionUtil.unexpected("no ConfigScreenHandler$ConfigScreenFactory constructor in 1.20.4-", e);
            }
        } else if (VersionUtil.getVersion() <= 21.6) {
            try {
                Class.forName("net.neoforged.neoforge.client.gui.IConfigScreenFactory");
                ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigScreen::new);
            } catch (ClassNotFoundException e) {
                throw ExceptionUtil.unexpected("no IConfigScreenFactory in 1.20.3+", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object value) {
        return (T) value;
    }
}
