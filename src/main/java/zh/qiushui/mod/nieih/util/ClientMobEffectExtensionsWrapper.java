package zh.qiushui.mod.nieih.util;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import zh.qiushui.mod.nieih.Config;

public class ClientMobEffectExtensionsWrapper implements IClientMobEffectExtensions {
    private final IClientMobEffectExtensions original;

    public ClientMobEffectExtensionsWrapper(IClientMobEffectExtensions original) {
        this.original = original;
    }

    @Override
    public boolean isVisibleInInventory(MobEffectInstance instance) {
        return this.original.isVisibleInInventory(instance);
    }

    @Override
    public boolean isVisibleInGui(MobEffectInstance instance) {
        if (!this.original.isVisibleInGui(instance)) return false;
        if (!Config.ENABLED.get()) return false;

        boolean durationCheck;
        if (EffectUtil.isEffectInfinite(instance)) {
            durationCheck = Config.INFINITE.get();
        } else {
            durationCheck = Config.NON_INFINITE.get();
        }
        if (!durationCheck) return false;

        return switch (instance.getEffect().getCategory()) {
            case BENEFICIAL -> Config.BENEFICIAL.get();
            case NEUTRAL -> Config.NEUTRAL.get();
            case HARMFUL -> Config.HARMFUL.get();
        };
    }

    @Override
    public boolean renderInventoryIcon(
        MobEffectInstance instance,
        EffectRenderingInventoryScreen<?> screen,
        GuiGraphics guiGraphics,
        int x,
        int y,
        int blitOffset
    ) {
        return this.original.renderInventoryIcon(instance, screen, guiGraphics, x, y, blitOffset);
    }

    @Override
    public boolean renderInventoryText(
        MobEffectInstance instance,
        EffectRenderingInventoryScreen<?> screen,
        GuiGraphics guiGraphics,
        int x,
        int y,
        int blitOffset
    ) {
        return this.original.renderInventoryText(instance, screen, guiGraphics, x, y, blitOffset);
    }

    @Override
    public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
        return this.original.renderGuiIcon(instance, gui, guiGraphics, x, y, z, alpha);
    }
}
