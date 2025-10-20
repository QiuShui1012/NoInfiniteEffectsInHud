package zh.qiushui.mod.nieih.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InventoryUtil {
    public static ItemStack getStack(Inventory inv, Item item) {
        for (NonNullList<ItemStack> stacks : inv.compartments) {
            for (ItemStack stack : stacks) {
                if (stack.is(item)) return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
