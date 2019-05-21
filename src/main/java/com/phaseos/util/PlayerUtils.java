package com.phaseos.util;

import com.phaseos.listener.GeneralListener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerUtils {
    public static boolean canPickup(UUID playerId, ItemStack stack) {
        List<ItemStack> stacks = GeneralListener.playerItems.get(playerId).values().stream().filter(itemStackList -> itemStackList.contains(stack)).findFirst().orElse(null);
        if (stacks == null)
            return false;
        return stacks.contains(stack);
    }

    public static boolean canPickup(ItemStack stack) {
        return GeneralListener.playerItems.values().stream().filter(stackMap -> stackMap.values().contains(stack)).count() > 0;
    }

    public static void removeItem(UUID playerId, ItemStack stack) {
        List<ItemStack> stacks = GeneralListener.playerItems.get(playerId).values().stream().filter(itemStackList -> itemStackList.contains(stack)).findFirst().orElse(null);
        if (stacks == null)
            return;
        stacks.remove(stack);
    }
}
