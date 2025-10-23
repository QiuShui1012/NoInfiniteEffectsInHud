package zh.qiushui.mod.nieih.client.gui;

import net.minecraft.client.gui.components.ContainerObjectSelectionList;

class ConfigListAfter1202 extends ContainerObjectSelectionList<ConfigScreen.Entry> {
    public ConfigListAfter1202(ConfigScreen.Entry... entries) {
        super(
            ConfigScreen.getScreen().getMinecraft(),
            ConfigScreen.getScreen().width,
            ConfigScreen.getScreen().height - 22 - 36,
            22,
            24
        );
        for (ConfigScreen.Entry entry : entries) {
            this.addEntry(entry);
        }
    }

    public ConfigListAfter1202(ConfigScreen.CategoryEntry category) {
        this(category.entries.toArray(ConfigScreen.Entry[]::new));
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
