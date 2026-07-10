package com.qiushui1012.mod.nieih.client.gui;

import com.qiushui1012.mod.nieih.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

//#if MC < 1_19_00
import net.minecraft.network.chat.TranslatableComponent;
//#endif

//#if MC < 1_20_00
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
//#elseif MC < 26_01_00
//$$ import net.minecraft.client.gui.GuiGraphics;
//#else
//$$ import net.minecraft.client.gui.GuiGraphicsExtractor;
//$$ import net.minecraft.client.input.MouseButtonEvent;
//#endif

public class ConfigScreen extends Screen {
    private static final int BACKGROUND_COLOR = 0xC0101010;
    private static final int BUTTON_COLOR = 0xFF3C3C3C;
    private static final int BUTTON_HOVER_COLOR = 0xFF5A5A5A;
    private static final int CATEGORY_COLOR = 0xFF242424;
    private static final int DISABLED_COLOR = 0xFF777777;
    private static final int FALSE_COLOR = 0xFFE05252;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int TRUE_COLOR = 0xFF54B85A;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 90;
    private static final int CONTENT_WIDTH = 310;
    private static final int ROW_HEIGHT = 24;
    private static final List<ConfigInfo> INFOS = createInfos();
    public static final Component EDIT = new TranslatableComponent("screen.nieih.button.edit");
    public static final Component RESET = new TranslatableComponent("screen.nieih.button.reset");
    public static final Component BACK = new TranslatableComponent("gui.back");
    public static final Component CANCEL = new TranslatableComponent("gui.cancel");
    public static final Component DONE = new TranslatableComponent("gui.done");

    private final Screen parent;
    private final List<Boolean> values = new ArrayList<>();
    private String category;
    private int scrollOffset;

    public ConfigScreen(Screen parent) {
        super(new TranslatableComponent("screen.nieih.title"));
        this.parent = parent;
        this.loadValues();
    }

    private static List<ConfigInfo> createInfos() {
        List<ConfigInfo> infos = new ArrayList<>();
        infos.add(ConfigInfo.singleDesc("enabled", "config.nieih.enabled", ConfigType.BOOLEAN));
        infos.add(ConfigInfo.singleDesc("duration", "config.nieih.duration", ConfigType.CATEGORY));
        infos.add(ConfigInfo.singleDesc("duration.infinite", "config.nieih.duration.infinite", ConfigType.BOOLEAN));
        infos.add(ConfigInfo.singleDesc("duration.nonInfinite", "config.nieih.duration.nonInfinite", ConfigType.BOOLEAN));
        infos.add(ConfigInfo.singleDesc("category", "config.nieih.category", ConfigType.CATEGORY));
        infos.add(ConfigInfo.singleDesc("category.beneficial", "config.nieih.category.beneficial", ConfigType.BOOLEAN));
        infos.add(ConfigInfo.singleDesc("category.harmful", "config.nieih.category.harmful", ConfigType.BOOLEAN));
        infos.add(ConfigInfo.singleDesc("category.neutral", "config.nieih.category.neutral", ConfigType.BOOLEAN));
        //#if MC == 1_20_01
        //$$ infos.add(ConfigInfo.singleDesc("compat", "config.nieih.compat", ConfigType.CATEGORY));
        //$$ infos.add(ConfigInfo.multipleDesc("compat.cmCompat", "config.nieih.compat.cmCompat", 2, ConfigType.BOOLEAN));
        //#endif
        return infos;
    }

    private void loadValues() {
        for (ConfigInfo info : INFOS) {
            this.values.add(info.getType() == ConfigType.BOOLEAN && this.getValue(info.getPath()));
        }
    }

    private boolean getValue(String path) {
        switch (path) {
            case "enabled": return Config.ENABLED.get();
            case "duration.infinite": return Config.INFINITE.get();
            case "duration.nonInfinite": return Config.NON_INFINITE.get();
            case "category.beneficial": return Config.BENEFICIAL.get();
            case "category.harmful": return Config.HARMFUL.get();
            case "category.neutral": return Config.NEUTRAL.get();
            //#if MC == 1_20_01
            //$$ case "compat.cmCompat": return Config.CM_COMPAT.get();
            //#endif
            default: throw new IllegalArgumentException("Unknown config path: " + path);
        }
    }

    private void saveValues() {
        for (int index = 0; index < INFOS.size(); index++) {
            ConfigInfo info = INFOS.get(index);
            if (info.getType() == ConfigType.BOOLEAN) {
                this.setValue(info.getPath(), this.values.get(index));
            }
        }
    }

    private void setValue(String path, boolean value) {
        switch (path) {
            case "enabled":
                Config.ENABLED.set(value);
                Config.ENABLED.save();
                break;
            case "duration.infinite":
                Config.INFINITE.set(value);
                Config.INFINITE.save();
                break;
            case "duration.nonInfinite":
                Config.NON_INFINITE.set(value);
                Config.NON_INFINITE.save();
                break;
            case "category.beneficial":
                Config.BENEFICIAL.set(value);
                Config.BENEFICIAL.save();
                break;
            case "category.harmful":
                Config.HARMFUL.set(value);
                Config.HARMFUL.save();
                break;
            case "category.neutral":
                Config.NEUTRAL.set(value);
                Config.NEUTRAL.save();
                break;
            //#if MC == 1_20_01
            //$$ case "compat.cmCompat":
            //$$     Config.CM_COMPAT.set(value);
            //$$     Config.CM_COMPAT.save();
            //$$     break;
            //#endif
            default: throw new IllegalArgumentException("Unknown config path: " + path);
        }
    }

    private void resetValues() {
        for (int index = 0; index < INFOS.size(); index++) {
            ConfigInfo info = INFOS.get(index);
            if (info.getType() == ConfigType.BOOLEAN) {
                this.values.set(index, !"duration.infinite".equals(info.getPath()));
            }
        }
    }

    private int contentLeft() {
        return (this.width - CONTENT_WIDTH) / 2;
    }

    private int visibleRows() {
        return Math.max(1, (this.height - 68) / ROW_HEIGHT);
    }

    private List<Integer> visibleInfoIndices() {
        List<Integer> indices = new ArrayList<>();
        for (int index = 0; index < INFOS.size(); index++) {
            String path = INFOS.get(index).getPath();
            if (this.category == null ? !path.contains(".") : path.startsWith(this.category + ".")) {
                indices.add(index);
            }
        }
        return indices;
    }

    private void scroll(double amount) {
        this.scrollOffset -= (int) Math.signum(amount);
        this.scrollOffset = Math.max(0, Math.min(this.scrollOffset,
            Math.max(0, this.visibleInfoIndices().size() - this.visibleRows())));
    }

    @Override
    //#if MC < 26_01_00
    public void render(
    //#else
    //$$ public void extractRenderState(
    //#endif
        //#if MC < 1_20_00
        PoseStack pose,
        //#elseif MC < 26_01_00
        //$$ GuiGraphics graphics,
        //#else
        //$$ GuiGraphicsExtractor graphics,
        //#endif
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        //#if MC < 1_20_00
        super.render(pose, mouseX, mouseY, partialTick);
        //#elseif MC < 26_01_00
        //$$ super.render(graphics, mouseX, mouseY, partialTick);
        //#else
        //$$ super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        //#endif

        //#if MC < 1_20_00
        this.renderBackground(pose);
        //#elseif MC < 1_20_02
        //$$ this.renderBackground(graphics);
        //#endif

        //#if MC < 1_20_00
        GuiComponent.fill(pose, 0, 0, this.width, this.height, BACKGROUND_COLOR);
        GuiComponent.drawCenteredString(pose, this.font, this.title, this.width / 2, 12, TEXT_COLOR);
        //#elseif MC < 26_01_00
        //$$ graphics.fill(0, 0, this.width, this.height, BACKGROUND_COLOR);
        //$$ graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, TEXT_COLOR);
        //#else
        //$$ graphics.fill(0, 0, this.width, this.height, BACKGROUND_COLOR);
        //$$ graphics.centeredText(this.font, this.title, this.width / 2, 12, TEXT_COLOR);
        //#endif

        //#if MC < 1_20_00
        this.renderRows(pose, mouseX, mouseY);
        this.renderButtons(pose, mouseX, mouseY);
        //#elseif MC < 26_01_00
        //$$ this.renderRows(graphics, mouseX, mouseY);
        //$$ this.renderButtons(graphics, mouseX, mouseY);
        //#else
        //$$ this.extractRows(graphics, mouseX, mouseY);
        //$$ this.extractButtons(graphics, mouseX, mouseY);
        //#endif
    }

    //#if MC < 26_01_00
    private void renderRows(
    //#else
    //$$ private void extractRows(
    //#endif
        //#if MC < 1_20_00
        PoseStack pose,
        //#elseif MC < 26_01_00
        //$$ GuiGraphics graphics,
        //#else
        //$$ GuiGraphicsExtractor graphics,
        //#endif
        int mouseX,
        int mouseY
    ) {
        int left = this.contentLeft();
        List<Integer> indices = this.visibleInfoIndices();
        for (int visibleIndex = 0; visibleIndex < this.visibleRows(); visibleIndex++) {
            int pageIndex = visibleIndex + this.scrollOffset;
            if (pageIndex >= indices.size()) break;
            int index = indices.get(pageIndex);
            int top = 34 + visibleIndex * ROW_HEIGHT;
            ConfigInfo info = INFOS.get(index);

            //#if MC < 1_20_00
            GuiComponent.fill(
                pose,
            //#else
            //$$ graphics.fill(
            //#endif
                left,
                top,
                left + CONTENT_WIDTH,
                top + ROW_HEIGHT - 2,
                this.rowColor(index, mouseX, mouseY)
            );

            //#if MC < 1_20_00
            GuiComponent.drawString(pose, this.font, info.getName(), left + 7, top + 7, TEXT_COLOR);
            //#elseif MC < 26_01_00
            //$$ graphics.drawString(this.font, info.getName(), left + 7, top + 7, TEXT_COLOR, false);
            //#else
            //$$ graphics.text(this.font, info.getName(), left + 7, top + 7, TEXT_COLOR, false);
            //#endif

            if (info.getType() == ConfigType.CATEGORY) {
                //#if MC < 1_20_00
                this.drawTextRight(pose, ConfigScreen.EDIT, top, TEXT_COLOR);
                //#elseif MC < 26_01_00
                //$$ this.drawTextRight(graphics, ConfigScreen.EDIT, top, TEXT_COLOR);
                //#else
                //$$ this.textRight(graphics, ConfigScreen.EDIT, top, TEXT_COLOR);
                //#endif
                continue;
            }

            //#if MC < 1_20_00
            this.drawValue(pose, index, top);
            //#elseif MC < 26_01_00
            //$$ this.drawValue(graphics, index, top);
            //#else
            //$$ this.value(graphics, index, top);
            //#endif
        }
    }

    //#if MC < 26_01_00
    private void drawTextRight(
    //#else
    //$$ private void textRight(
    //#endif
        //#if MC < 1_20_00
        PoseStack pose,
        //#elseif MC < 26_01_00
        //$$ GuiGraphics graphics,
        //#else
        //$$ GuiGraphicsExtractor graphics,
        //#endif
        Component text,
        int top,
        int color
    ) {
        //#if MC < 1_20_00
        GuiComponent.drawString(
            pose,
            this.font,
            text,
            this.contentLeft() + CONTENT_WIDTH - 7 - this.font.width(text),
            top + 7,
            color
        );
        //#elseif MC < 26_01_00
        //$$ graphics.drawString(
        //$$     this.font,
        //$$     text,
        //$$     this.contentLeft() + CONTENT_WIDTH - 7 - this.font.width(text),
        //$$     top + 7,
        //$$     color,
        //$$     false
        //$$ );
        //#else
        //$$ graphics.text(
        //$$     this.font,
        //$$     text,
        //$$     this.contentLeft() + CONTENT_WIDTH - 7 - this.font.width(text),
        //$$     top + 7,
        //$$     color,
        //$$     false
        //$$ );
        //#endif
    }

    //#if MC < 26_01_00
    private void drawValue(
    //#else
    //$$ private void value(
    //#endif
        //#if MC < 1_20_00
        PoseStack pose,
        //#elseif MC < 26_01_00
        //$$ GuiGraphics graphics,
        //#else
        //$$ GuiGraphicsExtractor graphics,
        //#endif
        int index,
        int top
    ) {
        boolean enabled = this.values.get(index);
        Component text = new TranslatableComponent(enabled ? "screen.nieih.button.true" : "screen.nieih.button.false");

        //#if MC < 1_20_00
        this.drawTextRight(pose, text, top, enabled ? TRUE_COLOR : FALSE_COLOR);
        //#elseif MC < 26_01_00
        //$$ this.drawTextRight(graphics, text, top, enabled ? TRUE_COLOR : FALSE_COLOR);
        //#else
        //$$ this.textRight(graphics, text, top, enabled ? TRUE_COLOR : FALSE_COLOR);
        //#endif
    }

    //#if MC < 26_01_00
    private void renderButtons(
    //#else
    //$$ private void extractButtons(
    //#endif
        //#if MC < 1_20_00
        PoseStack pose,
        //#elseif MC < 26_01_00
        //$$ GuiGraphics graphics,
        //#else
        //$$ GuiGraphicsExtractor graphics,
        //#endif
        int mouseX,
        int mouseY
    ) {
        //#if MC < 1_20_00
        this.renderButton(pose, this.resetLeft(), mouseX, mouseY, ConfigScreen.RESET);
        this.renderButton(pose, this.cancelLeft(), mouseX, mouseY,
            this.category == null ? ConfigScreen.CANCEL : ConfigScreen.BACK);
        this.renderButton(pose, this.doneLeft(), mouseX, mouseY, ConfigScreen.DONE);
        //#elseif MC < 26_01_00
        //$$ this.renderButton(graphics, this.resetLeft(), mouseX, mouseY, ConfigScreen.RESET);
        //$$ this.renderButton(graphics, this.cancelLeft(), mouseX, mouseY,
        //$$     this.category == null ? ConfigScreen.CANCEL : ConfigScreen.BACK);
        //$$ this.renderButton(graphics, this.doneLeft(), mouseX, mouseY, ConfigScreen.DONE);
        //#else
        //$$ this.extractButton(graphics, this.resetLeft(), mouseX, mouseY, ConfigScreen.RESET);
        //$$ this.extractButton(graphics, this.cancelLeft(), mouseX, mouseY,
        //$$     this.category == null ? ConfigScreen.CANCEL : ConfigScreen.BACK);
        //$$ this.extractButton(graphics, this.doneLeft(), mouseX, mouseY, ConfigScreen.DONE);
        //#endif
    }

    //#if MC < 26_01_00
    private void renderButton(
    //#else
    //$$ private void extractButton(
    //#endif
        //#if MC < 1_20_00
        PoseStack pose,
        //#elseif MC < 26_01_00
        //$$ GuiGraphics graphics,
        //#else
        //$$ GuiGraphicsExtractor graphics,
        //#endif
        int left,
        int mouseX,
        int mouseY,
        Component text
    ) {
        int top = this.height - 27;
        //#if MC < 1_20_00
        GuiComponent.fill(
            pose,
        //#else
        //$$ graphics.fill(
        //#endif
            left,
            top,
            left + BUTTON_WIDTH,
            top + BUTTON_HEIGHT,
            this.buttonHovered(left, mouseX, mouseY) ? BUTTON_HOVER_COLOR : BUTTON_COLOR
        );
        //#if MC < 1_20_00
        GuiComponent.drawCenteredString(
            pose,
        //#elseif MC < 26_01_00
        //$$ graphics.drawCenteredString(
        //#else
        //$$ graphics.centeredText(
        //#endif
            this.font,
            text,
            left + BUTTON_WIDTH / 2,
            top + 6,
            TEXT_COLOR
        );
    }

    private int rowColor(int index, int mouseX, int mouseY) {
        if (!this.rowHovered(index, mouseX, mouseY)) {
            return INFOS.get(index).getType() == ConfigType.CATEGORY ? CATEGORY_COLOR : BUTTON_COLOR;
        }
        return this.optionAvailable(index) ? BUTTON_HOVER_COLOR : DISABLED_COLOR;
    }

    private boolean optionAvailable(int index) {
        return INFOS.get(index).getType() == ConfigType.CATEGORY || index == 0 || this.values.get(0);
    }

    private boolean rowHovered(int index, double mouseX, double mouseY) {
        int pageIndex = this.visibleInfoIndices().indexOf(index);
        int visibleIndex = pageIndex - this.scrollOffset;
        int top = 34 + visibleIndex * ROW_HEIGHT;
        return visibleIndex >= 0 && visibleIndex < this.visibleRows()
            && mouseX >= this.contentLeft() && mouseX < this.contentLeft() + CONTENT_WIDTH
            && mouseY >= top && mouseY < top + ROW_HEIGHT - 2;
    }

    private boolean buttonHovered(int left, double mouseX, double mouseY) {
        int top = this.height - 27;
        return mouseX >= left && mouseX < left + BUTTON_WIDTH && mouseY >= top && mouseY < top + BUTTON_HEIGHT;
    }

    private int resetLeft() {
        return this.width / 2 - BUTTON_WIDTH * 3 / 2 - 4;
    }

    private int cancelLeft() {
        return this.width / 2 - BUTTON_WIDTH / 2;
    }

    private int doneLeft() {
        return this.width / 2 + BUTTON_WIDTH / 2 + 4;
    }

    private boolean click(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.buttonHovered(this.resetLeft(), mouseX, mouseY)) {
                this.resetValues();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
            if (this.buttonHovered(this.cancelLeft(), mouseX, mouseY)) {
                if (this.category == null) this.onClose();
                else this.openCategory(null);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
            if (this.buttonHovered(this.doneLeft(), mouseX, mouseY)) {
                this.saveValues();
                this.closeScreen();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
            for (int index = 0; index < INFOS.size(); index++) {
                if (INFOS.get(index).getType() == ConfigType.CATEGORY && this.rowHovered(index, mouseX, mouseY)) {
                    this.openCategory(INFOS.get(index).getPath());
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }
                if (
                    INFOS.get(index).getType() == ConfigType.BOOLEAN
                    && this.optionAvailable(index)
                    && this.rowHovered(index, mouseX, mouseY)
                ) {
                    this.values.set(index, !this.values.get(index));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }
            }
        }
        return false;
    }

    private void openCategory(String category) {
        this.category = category;
        this.scrollOffset = 0;
    }

    @Override
    public boolean mouseClicked(
    //#if MC < 26_01_00
        double mouseX,
        double mouseY,
        int button
    ) {
        return this.click(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    //#else
    //$$     MouseButtonEvent event,
    //$$     boolean doubleClick
    //$$ ) {
    //$$     return this.click(event.x(), event.y(), event.button()) || super.mouseClicked(event, doubleClick);
    //#endif
    }

    @Override
    public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        //#if MC >= 1_20_02
        //$$ double horizontalAmount,
        //#endif
        double amount
    ) {
        this.scroll(amount);
        return true;
    }

    @Override
    public void onClose() {
        if (this.category != null) {
            this.openCategory(null);
            return;
        }
        this.closeScreen();
    }

    private void closeScreen() {
        //#if MC < 26_02_00
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
        //#else
        //$$ if (this.minecraft != null) this.minecraft.setScreenAndShow(this.parent);
        //#endif
    }
}
