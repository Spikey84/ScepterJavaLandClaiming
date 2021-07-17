package io.github.spikey84.scepterjavaclaiming.listeners;


import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class ClaimToolListener implements Listener {
    private ClaimManager claimManager;
    private Plugin plugin;
    private long time = 0;
    private Map<UUID, Long> times;

    public ClaimToolListener(ClaimManager claimManager, Plugin plugin) {
        this.plugin = plugin;
        this.claimManager = claimManager;
        this.times = Maps.newHashMap();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            time++;
        }, 10, 80);
    }

    @EventHandler
    public void itemInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if(event.getItem() == null || !event.getItem().isSimilar(claimManager.getClaimItem())) return;
        event.setCancelled(true);

        event.getPlayer().sendMessage("Clicked");
    }
}
