package com.phaseos.listener;

import com.phaseos.antilootsteal.Antilootsteal;
import com.phaseos.util.PlayerUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GeneralListener implements Listener {

    public static HashMap<UUID, HashMap<Long, List<ItemStack>>> playerItems = new HashMap<>();
    private Antilootsteal plugin;

    public GeneralListener(Antilootsteal plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryPickUp(InventoryPickupItemEvent e) {
        if (e.getInventory().getType() != InventoryType.PLAYER) {
            if (!PlayerUtils.canPickup(e.getItem().getItemStack())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            UUID playerId = e.getEntity().getUniqueId();
            if (PlayerUtils.canPickup(playerId, e.getItem().getItemStack())) {
                PlayerUtils.removeItem(playerId, e.getItem().getItemStack());
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            HashMap<Long, List<ItemStack>> timeMap = new HashMap<>();
            long time = System.nanoTime();
            timeMap.put(time, e.getDrops());
            playerItems.put(e.getEntity().getKiller().getUniqueId(), timeMap);
            new BukkitRunnable() {
                @Override
                public void run() {
                    // remove the validity of the items dropped at this particular time
                    playerItems.get(e.getEntity().getKiller().getUniqueId()).remove(time, null);
                }
            }.runTaskLater(plugin, 20 * 15L);
        }
    }
}
