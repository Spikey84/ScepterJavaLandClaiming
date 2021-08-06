package io.github.spikey84.scepterjavaclaiming.blacklists;


import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BlackListListener implements Listener {
    private ClaimManager claimManager;

    public BlackListListener(ClaimManager claimManager) {

        this.claimManager = claimManager;
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, event.getTo())) continue;
            if (!claim.getBlackList().contains(event.getPlayer().getUniqueId())) continue;
            if (Claim.inClaim(claim, event.getFrom())) event.getPlayer().teleport(claim.getOrigin().getWorld().getSpawnLocation());
            event.setCancelled(true);


        }
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        if (event.getTo().equals(event.getTo().getWorld().getSpawnLocation())) return;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, event.getTo())) continue;
            if (!claim.getBlackList().contains(event.getPlayer().getUniqueId())) continue;
            event.setCancelled(true);
        }
    }
}
