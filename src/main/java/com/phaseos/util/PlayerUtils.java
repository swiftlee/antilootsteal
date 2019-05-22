package com.phaseos.util;

import com.phaseos.listener.GeneralListener;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerUtils {
    public static boolean canPickup(UUID playerId, ItemStack stack) {
        return getWhoCanPickup(stack) == null || Objects.equals(getWhoCanPickup(stack), playerId);
    }

    public static boolean canPickup(ItemStack stack) {
        return GeneralListener.playerItems.values().stream().anyMatch(stackMap -> stackMap.values().contains(stack));
    }

    public static void removeItem(UUID playerId, ItemStack stack) {
        List<ItemStack> stacks = GeneralListener.playerItems.get(playerId).values().stream().filter(itemStackList -> itemStackList.contains(stack)).findFirst().orElse(null);
        if (stacks == null)
            return;
        stacks.remove(stack);
    }

    public static UUID getWhoCanPickup(ItemStack stack) {
        for (java.util.Map.Entry<UUID, HashMap<Long, List<ItemStack>>> stackMap : GeneralListener.playerItems.entrySet()) {
            if (stackMap.getValue().values().stream().anyMatch(itemStacks -> itemStacks.contains(stack))) {
                return stackMap.getKey();
            }
        }
        return null;
    }

    public static long getTimeUntilExpired(ItemStack stack) {
        for (java.util.Map.Entry<UUID, HashMap<Long, List<ItemStack>>> stackMap : GeneralListener.playerItems.entrySet()) {
            if (stackMap.getValue().values().stream().anyMatch(itemStacks -> itemStacks.contains(stack))) {
                List<ItemStack> stacks = stackMap.getValue().values().stream().filter(itemStacks -> itemStacks.contains(stack)).findFirst().get();
                for (java.util.Map.Entry<Long, List<ItemStack>> map : stackMap.getValue().entrySet()) {
                    if (map.getValue().equals(stacks))
                        return (long) ((map.getKey() / 1_000_000_000.0) + 15) - (long) (System.nanoTime() / 1_000_000_000.0);
                }
            }
        }
        return 0;
    }
}
