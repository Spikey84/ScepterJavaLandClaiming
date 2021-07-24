package io.github.spikey84.scepterjavaclaiming.listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ClaimSetting;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class ProtectionListener implements Listener {
    private ClaimManager claimManager;

    public ProtectionListener(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, event.getBlock().getLocation())) continue;
            if (claim.getMembers().contains(event.getPlayer().getUniqueId())) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void interactBlock(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;
        switch (event.getClickedBlock().getType()) {
            case CHEST -> {
                if (!interactionAllowed(ClaimSetting.PUBLIC_USE_CHEST, event.getClickedBlock(), event.getPlayer()))
                    event.setCancelled(true);
                return;
            }
            case CRAFTING_TABLE -> {
                if (!interactionAllowed(ClaimSetting.PUBLIC_USE_CRAFTING_TABLE, event.getClickedBlock(), event.getPlayer()))
                    event.setCancelled(true);
                return;
            }
            case ANVIL -> {
                if (!interactionAllowed(ClaimSetting.PUBLIC_USE_ANVILS, event.getClickedBlock(), event.getPlayer()))
                    event.setCancelled(true);
                return;
            }
            case ENCHANTING_TABLE -> {
                if (!interactionAllowed(ClaimSetting.PUBLIC_USE_ENCHANTTABLE, event.getClickedBlock(), event.getPlayer()))
                    event.setCancelled(true);
                return;
            }
            case ENDER_CHEST -> {
                if (!interactionAllowed(ClaimSetting.PUBLIC_USE_ENDERCHEST, event.getClickedBlock(), event.getPlayer()))
                    event.setCancelled(true);
                return;
            }
        }
        if (event.getMaterial().name().contains("door")) {
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_DOORS, event.getClickedBlock(), event.getPlayer())) event.setCancelled(true);
            return;
        }

        if (event.getMaterial().name().contains("gate")) {
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_GATES, event.getClickedBlock(), event.getPlayer())) event.setCancelled(true);
            return;
        }
        if (!interactionAllowed(event.getClickedBlock(),event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent event) {
        if (!interactionAllowed(event.getPlayer().getLocation(), event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void mobSpawning(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Monster) {
            if (!interactionAllowed(event.getLocation(), ClaimSetting.MOB_SPAWNING)) event.setCancelled(true);
        } else {
            if (!interactionAllowed(event.getLocation(), ClaimSetting.ANIMAL_SPAWNING)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent event) {
        if (!interactionAllowed(event.getItemDrop().getLocation(), ClaimSetting.ITEM_DROP, event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void itemPickup(PlayerAttemptPickupItemEvent event) {
        if (!interactionAllowed(event.getItem().getLocation(), ClaimSetting.ITEM_PICKUP, event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void fireDamage(EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || !event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) return;
        if (!interactionAllowed(event.getEntity().getLocation(), ClaimSetting.FIRE_DAMAGE)) event.setCancelled(true);
    }

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!interactionAllowed(event.getEntity().getLocation(), ClaimSetting.PVP, (Player) event.getDamager())) event.setCancelled(true);
    }

    @EventHandler
    public void fireSpread(BlockIgniteEvent event) {
        Location location = null;

        if (event.getIgnitingBlock() != null) location = event.getIgnitingBlock().getLocation(); else location = event.getIgnitingEntity().getLocation();

        if (!interactionAllowed(location, ClaimSetting.FIRE_SPREAD)) event.setCancelled(true);
    }

    @EventHandler
    public void portalEvent(PlayerPortalEvent event) {
        if (!interactionAllowed(event.getFrom(), event.getPlayer())) return;
    }

    @EventHandler
    public void enderPearl(PlayerLaunchProjectileEvent event) {
        if (!(event.getProjectile() instanceof EnderPearl)) return;
        if (!interactionAllowed(event.getPlayer().getLocation(), ClaimSetting.ENDER_PEARLS, event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void mobGriefing(EntityBreakDoorEvent event) {
        if (!interactionAllowed(event.getBlock().getLocation(), ClaimSetting.MOB_GRIEFING)) event.setCancelled(true);
    }

    @EventHandler
    public void creeperExplosion(EntityExplodeEvent event) {
        if (!interactionAllowed(event.getLocation(), ClaimSetting.MOB_GRIEFING)) event.setCancelled(true);
    }

    @EventHandler
    public void endermanGrief(EntityChangeBlockEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) return;
        if (!interactionAllowed(event.getEntity().getLocation(), ClaimSetting.MOB_GRIEFING)) event.setCancelled(true);
    }

    public boolean interactionAllowed(ClaimSetting setting, Block block, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
            for (Claim claim : claimManager.getClaims()) {
                if (!Claim.inClaim(claim, block.getLocation())) continue;
                if (claim.getMembers().contains(player.getUniqueId()) || claim.getClaimSettings().get(setting)) continue;
                return true;
            }
        return false;
    }

    public boolean interactionAllowed(Block block, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, block.getLocation())) continue;
            if (claim.getMembers().contains(player.getUniqueId())) continue;
            return true;
        }
        return false;
    }

    public boolean interactionAllowed(Location location, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            if (claim.getMembers().contains(player.getUniqueId())) continue;
            return true;
        }
        return false;
    }

    public boolean interactionAllowed(Location location) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            return true;
        }
        return false;
    }

    public boolean interactionAllowed(Location location, ClaimSetting setting) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            if (claim.getClaimSettings().get(setting)) continue;
            return true;
        }
        return false;
    }

    public boolean interactionAllowed(Location location, ClaimSetting setting, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            if (claim.getMembers().contains(player.getUniqueId()) || claim.getClaimSettings().get(setting)) continue;
            return true;
        }
        return false;
    }
}