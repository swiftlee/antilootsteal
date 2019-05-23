package com.phaseos.listener;

import com.phaseos.antilootsteal.Antilootsteal;
import com.phaseos.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GeneralListener implements Listener {

    private static Set<UUID> cooldown = new HashSet<>();
    private Antilootsteal plugin;

    public GeneralListener(Antilootsteal plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryPickUp(InventoryPickupItemEvent e) {
        if (e.getInventory().getType() == InventoryType.HOPPER) {
            if (e.getItem().hasMetadata("MineRiftALS")) {
                String[] vals = e.getItem().getMetadata("MineRiftALS").get(0).asString().split("\\?");
                long time = Long.parseLong(vals[0]) / 1_000_000_000L;
                long now = System.nanoTime() / 1_000_000_000L;
                if (Math.abs(now - time) < 15) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHopperPickup(InventoryPickupItemEvent e) {
        if (e.getInventory().getType() == InventoryType.HOPPER) {
            if (e.getItem().hasMetadata("MineRiftALS")) {
                String[] vals = e.getItem().getMetadata("MineRiftALS").get(0).asString().split("\\?");
                long time = Long.parseLong(vals[0]) / 1_000_000_000L;
                long now = System.nanoTime() / 1_000_000_000L;
                if (Math.abs(now - time) < 15) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("MineRiftALS")) {
            UUID id = e.getPlayer().getUniqueId();
            String[] values = e.getItem().getMetadata("MineRiftALS").get(0).asString().split("\\?");
            long time = Long.parseLong(values[0]) / 1_000_000_000L;
            long now = System.nanoTime() / 1_000_000_000L;
            UUID comparisonId = UUID.fromString(values[1]);
            if (Math.abs(now - time) < 15) {
                if (!id.equals(comparisonId)) {
                    if (!cooldown.contains(e.getPlayer().getUniqueId())) {
                        cooldown.add(id);
                        e.getPlayer().sendMessage(TextUtils.fmt(plugin.getConfig().getString("loot-message")).replace("{killer}", Bukkit.getOfflinePlayer(comparisonId).getName()).replace("{seconds}", String.valueOf((int) (15 - (now - time)))));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(id);
                            }
                        }.runTaskLater(plugin, 20 * 3L);
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player player = e.getEntity();
            long time = System.nanoTime();
            e.getDrops().forEach(stack -> player.getWorld().dropItemNaturally(player.getLocation(), stack).setMetadata("MineRiftALS", new FixedMetadataValue(plugin, String.valueOf(time) + "?" + e.getEntity().getKiller().getUniqueId().toString())));
            e.getDrops().clear();
        }
    }
}
