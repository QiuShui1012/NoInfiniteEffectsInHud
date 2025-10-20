package zh.qiushui.mod.nieih.util;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtil {
    @OnlyIn(Dist.CLIENT)
    public static List<FormattedCharSequence> getMultiple(String baseKey) {
        if (I18n.exists(baseKey)) return Lists.newArrayList(Component.translatable(baseKey).getVisualOrderText());
        List<FormattedCharSequence> list = new ArrayList<>();
        int i = 0;
        do {
            String key = baseKey + "." + i++;
            if (!I18n.exists(key)) break;
            list.add(Component.translatable(key).getVisualOrderText());
        } while (true);
        return list;
    }
}
