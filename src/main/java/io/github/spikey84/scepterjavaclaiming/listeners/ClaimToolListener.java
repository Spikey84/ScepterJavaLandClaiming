package io.github.spikey84.scepterjavaclaiming.listeners;


import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.sqlite.util.StringUtils;

import java.util.Map;
import java.util.UUID;

public class ClaimToolListener implements Listener {
    private ClaimManager claimManager;
    private ConfigManager configManager;
    private Plugin plugin;
    private long time = 0;
    private Map<UUID, Long> times;
    private Map<UUID, Location> firstPos;

    public ClaimToolListener(ClaimManager claimManager, Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.claimManager = claimManager;
        this.configManager = configManager;
        this.times = Maps.newHashMap();
        this.firstPos = Maps.newHashMap();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            time++;
            for (Map.Entry<UUID, Long> entry : times.entrySet()) {
                if (entry.getValue() + 800 > time) continue;
                times.remove(entry.getKey());
                claimManager.getClaiming().put(entry.getKey(), false);
                if (Bukkit.getPlayer(entry.getKey()) == null) continue;
                ChatUtil.message(Bukkit.getPlayer(entry.getKey()), "Claiming expired");
            }

            for (Map.Entry<UUID, Location> entry : firstPos.entrySet()) {
                Location location = entry.getValue();
                location.getWorld().spawnParticle(Particle.LANDING_HONEY,location,1);
            }
        }, 10, 20);
    }

    @EventHandler
    public void itemInteract(PlayerInteractEvent event) {
        times.putIfAbsent(event.getPlayer().getUniqueId(), -1L);
        if (event.getClickedBlock() == null) return;
        if(event.getItem() == null || !event.getItem().isSimilar(claimManager.getClaimItem())) return;
        event.setCancelled(true);

        Player player = event.getPlayer();

        player.sendMessage("Clicked");

        if (times.get(player.getUniqueId()) == -1L) {
            times.put(player.getUniqueId(), time);
            firstPos.put(player.getUniqueId(), event.getClickedBlock().getLocation());
            claimManager.getClaiming().put(player.getUniqueId(), true);
            ChatUtil.message(player, "Please select where you want the other corner of your claim to be.");
            return;
        }

        Claim claim = new Claim(firstPos.get(player.getUniqueId()),Math.abs(firstPos.get(player.getUniqueId()).getBlockX()-player.getLocation().getBlockX()),Math.abs(firstPos.get(player.getUniqueId()).getBlockZ()-player.getLocation().getBlockZ()),player.getUniqueId(), null, configManager.getDefaultSettings());

        for (Claim c : claimManager.getClaims()) {
            if (claim.overlaps(claim)) {
                ChatUtil.message(player, "New claim overlaps with an existing one!");
                times.put(player.getUniqueId(), -1L);
                return;
            }
        }

        claimManager.addClaim(claim);
        ChatUtil.message(player, "Claim Created.");
        times.remove(player.getUniqueId());



    }
}
