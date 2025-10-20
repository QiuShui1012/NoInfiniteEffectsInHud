package zh.qiushui.mod.nieih.integration.compositematerial;

import zh.qiushui.mod.nieih.Config;
import zh.qiushui.mod.nieih.shadow.dev.anvilcraft.lib.integration.Integration;
import io.github.rcneg.compositematerial.common.init.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import zh.qiushui.mod.nieih.util.EffectUtil;
import zh.qiushui.mod.nieih.util.InventoryUtil;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
@Integration("composite_material")
public class CMCompat {
    public void apply() {
        EffectUtil.registerChecker(CMCompat::isEffectInPrimitiveTotem);
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean isEffectInPrimitiveTotem(MobEffectInstance inst) {
        if (!Config.CM_COMPAT.get()) return false;
        ItemStack stack = InventoryUtil.getStack(Minecraft.getInstance().player.getInventory(), ItemRegistry.PRIMITIVE_TOTEM.get());

        CompoundTag tag = stack.getOrCreateTag();
        ListTag listE = tag.getList("PrimitiveAddition", 8);
        ListTag listA = tag.getList("PrimitiveAmplifier", 3);

        for (int i = 0; i < listE.size(); ++i) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(listE.get(i).getAsString()));
            if (!inst.getEffect().equals(effect)) continue;
            return inst.getAmplifier() == listA.getInt(i) && inst.getDuration() <= 210;
        }
        return false;
    }
}
