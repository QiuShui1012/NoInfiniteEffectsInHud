package zh.qiushui.mod.nieih.client.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;
import zh.qiushui.mod.nieih.Config;

import java.util.Collections;
import java.util.List;

public class ConfigScreen extends Screen {
    private final Screen last;
    private final Minecraft minecraft = Minecraft.getInstance();

    private ConfigList list;

    @Nullable
    private List<? extends FormattedCharSequence> activeTooltip;
    private int tooltipTicks;

    public ConfigScreen(Screen last) {
        super(Component.translatable("screen.nieih.config.title"));
        this.last = last;
    }

    private void setActiveTooltip(List<? extends FormattedCharSequence> tooltip) {
        this.activeTooltip = tooltip;
    }

    @Override
    public void tick() {
        // makes tooltips not appear immediately
        if (this.tooltipTicks < 10) {
            this.tooltipTicks++;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.list = new ConfigList(
            new Entry(Config.ENABLED),
            new Entry(Config.DISPLAY_INFINITE),
            new Entry(Config.DISPLAY_NON_INFINITE)
        );
        this.addWidget(this.list);
        this.addRenderableWidget(Button.builder(
            CommonComponents.GUI_DONE,
            button -> {
                this.list.children().forEach(entry -> entry.config.set(entry.value));
                this.minecraft.setScreen(this.last);
            }
        ).bounds(this.width / 2 - 154, this.height - 28, 150, 20).build());
        this.addRenderableWidget(Button.builder(
            CommonComponents.GUI_CANCEL,
            button -> this.onClose()
        ).bounds(this.width / 2 + 4, this.height - 28, 150, 20).build());
    }

    @Override
    public void onClose() {
        super.onClose();
        Screen confirmScreen;
        if (this.list.children().stream().allMatch(entry -> entry.value == entry.config.get())) {
            confirmScreen = this.last;
        } else {
            confirmScreen = new ConfirmScreen(
                result -> this.minecraft.setScreen(result ? this.last : this),
                Component.translatable("screen.nieih.confirm.title"),
                Component.translatable("screen.nieih.confirm.info")
            );
        }
        this.minecraft.setScreen(confirmScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        List<? extends FormattedCharSequence> lastTooltip = this.activeTooltip;
        this.activeTooltip = null;
        this.renderBackground(guiGraphics);
        this.list.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.getTitle(), this.width / 2, 7, 0xffffff);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.activeTooltip != lastTooltip) {
            this.tooltipTicks = 0;
        }
        if (this.activeTooltip != null && this.tooltipTicks >= 10) {
            guiGraphics.renderTooltip(this.font, this.activeTooltip, mouseX, mouseY);
        }
    }

    public class ConfigList extends ContainerObjectSelectionList<ConfigScreen.Entry> {
        public ConfigList(ConfigScreen.Entry... entries) {
            super(
                ConfigScreen.this.minecraft,
                ConfigScreen.this.width,
                ConfigScreen.this.height,
                22,
                ConfigScreen.this.height - 36,
                24
            );
            for (ConfigScreen.Entry entry : entries) {
                this.addEntry(entry);
            }
        }

        @Override
        protected int getScrollbarPosition() {
            return this.width / 2 + 144;
        }

        @Override
        public int getRowWidth() {
            return 260;
        }
    }

    public class Entry extends ContainerObjectSelectionList.Entry<ConfigScreen.Entry> {
        private static final Component GUI_RESET = Component.translatable("gui.reset");

        private final Component name;
        private final List<FormattedCharSequence> tooltip;
        private final ForgeConfigSpec.BooleanValue config;
        private boolean value;

        private final List<AbstractWidget> children = Lists.newArrayList();
        private final Button button;
        private final Button resetButton;

        protected Entry(ForgeConfigSpec.BooleanValue config) {
            String name = config.getPath().get(0);
            this.name = Component.translatable("config.nieih." + name + ".name");
            this.tooltip = Collections.singletonList(Component.translatable("config.nieih." + name + ".desc").getVisualOrderText());
            this.config = config;
            this.value = config.get();

            this.button = Button.builder(CommonComponents.optionStatus(this.value), button -> {
                final boolean newValue = !this.value;
                this.value = newValue;
                this.updateButtonStatus(newValue);
                this.updateResetButtonStatus(this.value != this.config.getDefault());
            }).bounds(10, 5, 44, 20).build();
            this.resetButton = Button.builder(GUI_RESET, button -> {
                this.value = this.config.getDefault();
                this.updateButtonStatus(this.config.getDefault());
                this.updateResetButtonStatus(false);
            }).bounds(0, 0, 20, 20).build();
            this.resetButton.active = this.value != this.config.getDefault();
            this.children.add(this.button);
            this.children.add(this.resetButton);
        }

        private void updateButtonStatus(boolean newValue) {
            this.button.setMessage(CommonComponents.optionStatus(newValue));
        }

        private void updateResetButtonStatus(boolean active) {
            this.resetButton.active = active;
        }

        @Override
        public List<AbstractWidget> children() {
            return this.children;
        }

        public boolean isHovered(int mouseX, int mouseY) {
            return ConfigScreen.this.list != null
                   && this.isMouseOver(mouseX, mouseY)
                   && mouseX < ConfigScreen.this.list.getRowLeft() + ConfigScreen.this.list.getRowWidth() - 67;
        }

        @Override
        public void render(
            GuiGraphics guiGraphics, int index, int entryTop, int entryLeft, int rowWidth, int entryHeight, int mouseX, int mouseY,
            boolean hovered, float partialTicks
        ) {
            if (this.isHovered(mouseX, mouseY)) {
                ConfigScreen.this.setActiveTooltip(this.tooltip);
            }
            int color = this.isHovered(mouseX, mouseY) ? 0xffff55 : 0xffffff;
            guiGraphics.drawString(ConfigScreen.this.font, this.name, entryLeft, entryTop + 6, color);
            this.button.setX(entryLeft + rowWidth - 67);
            this.button.setY(entryTop);
            this.button.render(guiGraphics, mouseX, mouseY, partialTicks);
            this.resetButton.setX(entryLeft + rowWidth - 21);
            this.resetButton.setY(entryTop);
            this.resetButton.render(guiGraphics, mouseX, mouseY, partialTicks);
            if (this.resetButton.active && this.resetButton.isHoveredOrFocused()) {
                ConfigScreen.this.setActiveTooltip(ConfigScreen.this.font.split(GUI_RESET, 200));
            }
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput output) {
                    output.add(NarratedElementType.TITLE, ConfigScreen.Entry.this.name);
                }
            });
        }
    }
}
