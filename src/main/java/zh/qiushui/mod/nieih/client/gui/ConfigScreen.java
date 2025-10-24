package zh.qiushui.mod.nieih.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;
import zh.qiushui.mod.nieih.Config;
import zh.qiushui.mod.nieih.util.ComponentUtil;

import java.util.List;

public class ConfigScreen extends Screen {
    private static final Component TITLE = Component.translatable("screen.nieih.title");
    private final Screen last;
    private final Minecraft minecraft = Minecraft.getInstance();

    private ConfigList list;

    @Nullable
    private List<FormattedCharSequence> activeTooltip;
    private int tooltipTicks;

    public ConfigScreen(Object ignored, Screen last) {
        super(TITLE);
        this.last = last;
    }

    private ConfigScreen(ConfigScreen last, Component title) {
        super(title);
        this.last = last;
    }

    private void setActiveTooltip(List<FormattedCharSequence> tooltip) {
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
        if (this.list == null) {
            this.list = this.addWidget(new ConfigList(
                new BooleanEntry(Config.ENABLED),
                new CategoryEntry(
                    "duration",
                    new BooleanEntry(Config.INFINITE),
                    new BooleanEntry(Config.NON_INFINITE)
                ),
                new CategoryEntry(
                    "category",
                    new BooleanEntry(Config.BENEFICIAL),
                    new BooleanEntry(Config.NEUTRAL),
                    new BooleanEntry(Config.HARMFUL)
                ),
                new CategoryEntry(
                    "compat",
                    new BooleanEntry(Config.CM_COMPAT)
                )
            ));
        } else {
            this.list = this.addWidget(this.list.recreate());
        }

        if (this.title != TITLE) {
            this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_BACK,
                button -> this.minecraft.setScreen(this.last)
            ).bounds(this.width / 2 - 120, this.height - 28, 240, 20).build());
            return;
        }
        this.addRenderableWidget(Button.builder(
            CommonComponents.GUI_CANCEL,
            button -> this.onClose()
        ).bounds(this.width / 2 - 154, this.height - 28, 150, 20).build());
        this.addRenderableWidget(Button.builder(
            CommonComponents.GUI_DONE,
            button -> {
                this.list.children().forEach(Entry::applyValues);
                this.minecraft.setScreen(this.last);
            }
        ).bounds(this.width / 2 + 4, this.height - 28, 150, 20).build());
    }

    @Override
    public void onClose() {
        super.onClose();
        if (this.minecraft.screen instanceof ConfigScreen) return;
        Screen confirmScreen;
        if (this.list.children().stream().allMatch(Entry::isValueUnchanged)) {
            confirmScreen = this.last;
        } else {
            confirmScreen = new ConfirmScreen(
                result -> this.minecraft.setScreen(result ? this.last : this),
                Component.translatable("screen.nieih.confirm.title"),
                Component.translatable("screen.nieih.confirm.desc")
            );
        }
        this.minecraft.setScreen(confirmScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        List<? extends FormattedCharSequence> lastTooltip = this.activeTooltip;
        this.activeTooltip = null;
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.getTitle(), this.width / 2, 7, 0xffffff);
        this.list.render(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.activeTooltip != lastTooltip) {
            this.tooltipTicks = 0;
        }
        if (this.activeTooltip != null && this.tooltipTicks >= 10) {
            guiGraphics.renderTooltip(this.font, this.activeTooltip, mouseX, mouseY);
        }
    }

    static class ConfigList extends ContainerObjectSelectionList<ConfigScreen.Entry> {
        public ConfigList(ConfigScreen.Entry... entries) {
            super(
                ConfigScreen.getScreen().getMinecraft(),
                ConfigScreen.getScreen().width,
                ConfigScreen.getScreen().height,
                22,
                ConfigScreen.getScreen().height - 36,
                24
            );
            for (ConfigScreen.Entry entry : entries) {
                this.addEntry(entry);
            }

        }

        public ConfigList(ConfigScreen.CategoryEntry category) {
            this(category.entries.toArray(ConfigScreen.Entry[]::new));
        }

        protected int getScrollbarPosition() {
            return this.width / 2 + 144;
        }

        public int getRowWidth() {
            return 260;
        }

        public ConfigList recreate() {
            return new ConfigList(this.children().toArray(ConfigScreen.Entry[]::new));
        }
    }


    abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        protected final Component name;
        protected final List<FormattedCharSequence> tooltip;
        protected final List<AbstractWidget> children = Lists.newArrayList();

        protected Entry(String nameKey) {
            this.name = Component.translatable(nameKey);
            this.tooltip = ComponentUtil.getMultiple(nameKey + ".desc");
        }

        public boolean isHoveredName(int mouseX, int mouseY) {
            return getScreen().list != null
                   && this.isMouseOver(mouseX, mouseY)
                   && mouseX < getScreen().list.getRowLeft() + getScreen().list.getRowWidth() - 67;
        }

        @Override
        public void render(
            GuiGraphics guiGraphics,
            int index,
            int entryTop,
            int entryLeft,
            int rowWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float partialTicks
        ) {
            final int color;
            if (this.isHoveredName(mouseX, mouseY)) {
                getScreen().setActiveTooltip(this.tooltip);
                color = 0xffff55;
            } else {
                color = 0xffffff;
            }
            guiGraphics.drawString(getScreen().font, this.name, entryLeft, entryTop + 6, color);
        }

        protected <W extends AbstractWidget> W addChildren(W children) {
            this.children.add(children);
            return children;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        public abstract void applyValues();

        public abstract boolean isValueUnchanged();

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput output) {
                    output.add(NarratedElementType.TITLE, Entry.this.name);
                }
            });
        }
    }

    static class CategoryEntry extends Entry {
        private static final Component BUTTON_EDIT = Component.translatable("screen.nieih.button.edit");
        private static final Component SEPARATOR = Component.literal(" > ").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);

        final List<Entry> entries;
        private final Button button;

        protected CategoryEntry(String id, Entry... entries) {
            super("config.nieih." + id);
            this.entries = List.of(entries);

            this.button = this.addChildren(Button.builder(
                BUTTON_EDIT,
                button -> {
                    var screen = new ConfigScreen(
                        getScreen(),
                        ComponentUtils.formatList(List.of(getScreen().title, this.name), SEPARATOR)
                    );
                    screen.list = new ConfigList(this);
                    getScreen().minecraft.setScreen(screen);
                }
            ).bounds(0, 5, 76, 20).build());
        }

        @Override
        public void applyValues() {
            this.entries.forEach(Entry::applyValues);
        }

        @Override
        public boolean isValueUnchanged() {
            for (Entry entry : this.entries) {
                if (!entry.isValueUnchanged()) return false;
            }
            return true;
        }

        @Override
        public void render(
            GuiGraphics guiGraphics,
            int index,
            int entryTop,
            int entryLeft,
            int rowWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float partialTicks
        ) {
            super.render(guiGraphics, index, entryTop, entryLeft, rowWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
            this.button.setX(entryLeft + rowWidth - 67);
            this.button.setY(entryTop);
            this.button.render(guiGraphics, mouseX, mouseY, partialTicks);
            if (this.button.isHoveredOrFocused()) {
                getScreen().setActiveTooltip(getScreen().font.split(BUTTON_EDIT, 200));
            }
        }
    }

    private static class BooleanEntry extends Entry {
        private static final Component GUI_RESET = Component.translatable("screen.nieih.button.reset");

        private final ForgeConfigSpec.BooleanValue config;
        private boolean value;

        private final Button button;
        private final Button resetButton;

        protected BooleanEntry(ForgeConfigSpec.BooleanValue config) {
            super("config.nieih." + config.getPath().stream().reduce((a, b) -> a + "." + b).orElseThrow());
            this.config = config;
            this.value = config.get();

            this.button = this.addChildren(Button.builder(
                CommonComponents.optionStatus(this.value), button -> {
                    final boolean newValue = !this.value;
                    this.value = newValue;
                    this.updateButtonStatus(newValue);
                    this.updateResetButtonStatus(this.value != this.config.getDefault());
                }
            ).bounds(0, 5, 44, 20).build());
            this.resetButton = this.addChildren(Button.builder(
                GUI_RESET, button -> {
                    this.value = this.config.getDefault();
                    this.updateButtonStatus(this.config.getDefault());
                    this.updateResetButtonStatus(false);
                }
            ).bounds(0, 0, 30, 20).build());
            this.resetButton.active = this.value != this.config.getDefault();
        }

        private void updateButtonStatus(boolean newValue) {
            this.button.setMessage(CommonComponents.optionStatus(newValue));
        }

        private void updateResetButtonStatus(boolean active) {
            this.resetButton.active = active;
        }

        @Override
        public void applyValues() {
            this.config.set(this.value);
        }

        @Override
        public boolean isValueUnchanged() {
            return this.value == this.config.get();
        }

        @Override
        public void render(
            GuiGraphics guiGraphics,
            int index,
            int entryTop,
            int entryLeft,
            int rowWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float partialTicks
        ) {
            super.render(guiGraphics, index, entryTop, entryLeft, rowWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
            this.button.setX(entryLeft + rowWidth - 67);
            this.button.setY(entryTop);
            this.button.render(guiGraphics, mouseX, mouseY, partialTicks);
            this.resetButton.setX(entryLeft + rowWidth - 21);
            this.resetButton.setY(entryTop);
            this.resetButton.render(guiGraphics, mouseX, mouseY, partialTicks);
            if (this.resetButton.active && this.resetButton.isHoveredOrFocused()) {
                getScreen().setActiveTooltip(getScreen().font.split(GUI_RESET, 200));
            }
        }
    }

    static ConfigScreen getScreen() {
        var screen = Minecraft.getInstance().screen;
        if (!(screen instanceof ConfigScreen configScreen)) {
            throw new IllegalStateException("Use ConfigScreen's elements outside ConfigScreen");
        }
        return configScreen;
    }
}
