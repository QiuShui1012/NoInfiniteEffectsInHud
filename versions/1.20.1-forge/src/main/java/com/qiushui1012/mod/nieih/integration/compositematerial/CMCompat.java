package com.qiushui1012.mod.nieih.integration.compositematerial;

import com.qiushui1012.mod.nieih.Config;
import com.qiushui1012.mod.nieih.mixin.InventoryAccessor;
import com.qiushui1012.mod.nieih.util.EffectUtil;
import io.github.rcneg.compositematerial.common.init.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class CMCompat {
    public static void apply() {
        EffectUtil.registerChecker(CMCompat::isEffectInPrimitiveTotem);
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean isEffectInPrimitiveTotem(MobEffectInstance effect) {
        if (!Config.CM_COMPAT.get()) return false;

        ItemStack stack = null;
        for (NonNullList<ItemStack> stacks : ((InventoryAccessor) Minecraft.getInstance().player.getInventory()).getCompartments()) {
            for (ItemStack item : stacks) {
                if (item.is(ItemRegistry.PRIMITIVE_TOTEM.get())) stack = item;
            }
        }

        CompoundTag tag = stack.getOrCreateTag();
        ListTag addition = tag.getList("PrimitiveAddition", Tag.TAG_STRING);
        ListTag amplifier = tag.getList("PrimitiveAmplifier", Tag.TAG_INT);
        if (addition.size() != amplifier.size()) return false;

        for (int i = 0; i < addition.size(); ++i) {
            MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(addition.get(i).getAsString()));
            if (!effect.getEffect().equals(mobEffect)) continue;
            return effect.getAmplifier() == amplifier.getInt(i) && effect.getDuration() <= 210;
        }
        return false;
    }
}
