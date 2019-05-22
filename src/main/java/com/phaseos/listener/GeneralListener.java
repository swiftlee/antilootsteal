package com.phaseos.listener;

import com.phaseos.antilootsteal.Antilootsteal;
import com.phaseos.util.PlayerUtils;
import com.phaseos.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GeneralListener implements Listener {

    public static HashMap<UUID, HashMap<Long, List<ItemStack>>> playerItems = new HashMap<>();
    private static Set<UUID> cooldown = new HashSet<>();
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
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        UUID playerId = e.getPlayer().getUniqueId();
        if (PlayerUtils.canPickup(playerId, e.getItem().getItemStack())) {
            PlayerUtils.removeItem(playerId, e.getItem().getItemStack());
        } else {
            if (!cooldown.contains(e.getPlayer().getUniqueId())) {
                UUID id = e.getPlayer().getUniqueId();
                cooldown.add(id);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        cooldown.remove(id);
                    }
                }.runTaskLater(plugin, 20 * 3L);
            } else {
                e.getPlayer().sendMessage(TextUtils.fmt(plugin.getConfig().getString("loot-message")).replace("{killer}", Bukkit.getOfflinePlayer(PlayerUtils.getWhoCanPickup(e.getItem().getItemStack())).getName()).replace("{seconds}", String.valueOf(PlayerUtils.getTimeUntilExpired(e.getItem().getItemStack()))));
            }
            e.setCancelled(true);
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
