package com.qiushui1012.mod.nieih.util;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.HashSet;
import java.util.Set;

public class EffectUtil {
    private static final Set<EffectInfiniteChecker> CHECKERS = new HashSet<>();

    @SuppressWarnings("unused")
    public static void registerChecker(EffectInfiniteChecker checker) {
        EffectUtil.CHECKERS.add(checker);
    }

    public static boolean isEffectInfinite(MobEffectInstance inst) {
        if (inst.isNoCounter()) return true;
        for (EffectInfiniteChecker checker : EffectUtil.CHECKERS) {
            if (checker.isInfinite(inst)) return true;
        }
        return false;
    }

    public interface EffectInfiniteChecker {
        boolean isInfinite(MobEffectInstance inst);
    }
}
