package io.github.spikey84.scepterjavaclaiming.listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ClaimSetting;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.List;

//TODO mob griefing just outside claim
//TODO fix tnt cannon
//TODO fix crafting table

public class ProtectionListener implements Listener {
    private ClaimManager claimManager;

    public ProtectionListener(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, event.getBlock().getLocation())) continue;
            claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
            if (claim.getMembers().contains(event.getPlayer().getUniqueId()) || claimManager.getTrustedMembers().get(claim).contains(event.getPlayer().getUniqueId())) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, event.getBlock().getLocation())) continue;
            claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
            if (claim.getMembers().contains(event.getPlayer().getUniqueId()) || claimManager.getTrustedMembers().get(claim).contains(event.getPlayer().getUniqueId())) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void interactBlock(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType().equals(Material.CHEST)) {
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_CHEST, event.getClickedBlock(), event.getPlayer())) {
                event.setCancelled(true);
            }
            return;
        } else if (event.getClickedBlock().getType().equals(Material.CRAFTING_TABLE)) {
            Bukkit.getLogger().info("Crafting");
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_CRAFTING_TABLE, event.getClickedBlock(), event.getPlayer())) {
                Bukkit.getLogger().info("Crafting confirmed");
                    event.setCancelled(true);
                }
                return;
        } else if (event.getClickedBlock().getType().equals(Material.ANVIL)) {
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_ANVIL, event.getClickedBlock(), event.getPlayer())) {
                event.setCancelled(true);
            }
            return;
        } else if (event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) {
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_ENCHANTTABLE, event.getClickedBlock(), event.getPlayer())) {
                event.setCancelled(true);
            }
            return;
        } else if (event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            if (!interactionAllowed(ClaimSetting.PUBLIC_USE_ENDERCHEST, event.getClickedBlock(), event.getPlayer())) {
                event.setCancelled(true);
            }
            return;
        }

        if (event.getClickedBlock().getType().toString().toLowerCase().contains("door")) {
            if (!interactionAllowed(event.getClickedBlock().getLocation(), ClaimSetting.PUBLIC_USE_DOORS, event.getPlayer())) event.setCancelled(true);
            return;
        }

        if (event.getClickedBlock().getType().toString().toLowerCase().contains("gate")) {
            if (!interactionAllowed(event.getClickedBlock().getLocation(), ClaimSetting.PUBLIC_USE_GATES, event.getPlayer())) event.setCancelled(true);
            return;
        }
        //if (!interactionAllowed(event.getClickedBlock(),event.getPlayer())) event.setCancelled(true);
    }

//    @EventHandler
//    public void playerInteractEntity(PlayerInteractEntityEvent event) {
//        if (!interactionAllowed(event.getPlayer().getLocation(), event.getPlayer())) event.setCancelled(true);
//    }

    @EventHandler//working
    public void mobSpawning(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Monster) {
            if (!interactionAllowed(event.getLocation(), ClaimSetting.MOB_SPAWNING)) event.setCancelled(true);
        } else if (event.getEntity() instanceof Animals){
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
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) && !event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) return;
        if (!interactionAllowed(event.getEntity().getLocation(), ClaimSetting.FIRE_DAMAGE)) event.setCancelled(true);
    }

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (!interactionAllowed(event.getEntity().getLocation(), ClaimSetting.PVP)) event.setCancelled(true);
    }

    @EventHandler
    public void fireSpread(BlockIgniteEvent event) {//working
        Location location = null;
        if (!event.getCause().equals(BlockIgniteEvent.IgniteCause.SPREAD)) return;

        if (event.getIgnitingBlock() != null) location = event.getIgnitingBlock().getLocation(); else location = event.getIgnitingEntity().getLocation();

        if (!interactionAllowed(location, ClaimSetting.FIRE_SPREAD)) event.setCancelled(true);
    }

    @EventHandler
    public void fireDestroy(BlockBurnEvent event) {//working
        Location location = null;

        if (event.getIgnitingBlock() != null) location = event.getIgnitingBlock().getLocation(); else location = event.getIgnitingBlock().getLocation();

        if (!interactionAllowed(location, ClaimSetting.FIRE_SPREAD)) event.setCancelled(true);
    }


    @EventHandler
    public void portalEvent(PlayerPortalEvent event) {//working
        if (!interactionAllowed(event.getFrom(), ClaimSetting.PUBLIC_USE_PORTALS, event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void enderPearl(PlayerLaunchProjectileEvent event) {
        if (!(event.getProjectile() instanceof EnderPearl)) return;
        if (!interactionAllowed(event.getPlayer().getLocation(), ClaimSetting.ENDER_PEARLS, event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void mobGriefing(EntityBreakDoorEvent event) {//working
        if (!interactionAllowed(event.getBlock().getLocation(), ClaimSetting.MOB_GRIEFING)) event.setCancelled(true);
    }

    @EventHandler
    public void creeperExplosion(EntityExplodeEvent event) {//TODO not working
        if (!interactionAllowed(event.getLocation(), ClaimSetting.MOB_GRIEFING)) event.setCancelled(true);

        Location location1 = event.getLocation().clone();
        location1.setX(location1.getX() + 20);
        location1.setZ(location1.getZ() + 20);
        Location location2 = event.getLocation().clone();
        location1.setX(location1.getX() - 20);
        location1.setZ(location1.getZ() - 20);

        Rectangle rectangle = new Rectangle(location1, location2);

        List<Claim> nearbyClaims = Lists.newArrayList();

        for (Claim claim : claimManager.getClaims()) {
            if (!claim.overlaps(rectangle)) continue;
            nearbyClaims.add(claim);
        }

        for (Block block : event.blockList()) {
            for (Claim claim : claimManager.getClaims()) {
                if (Claim.inClaim(claim, block.getLocation())) event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void endermanGrief(EntityChangeBlockEvent event) {//assumed working
        if (event.getEntityType().equals(EntityType.PLAYER)) return;
        if (!interactionAllowed(event.getEntity().getLocation(), ClaimSetting.MOB_GRIEFING)) event.setCancelled(true);
    }

    public boolean interactionAllowed(ClaimSetting setting, Block block, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
            for (Claim claim : claimManager.getClaims()) {
                if (!Claim.inClaim(claim, block.getLocation())) continue;
                claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
                if (claimManager.getTrustedMembers().get(claim).contains(player.getUniqueId()) || claim.getMembers().contains(player.getUniqueId()) || claim.getClaimSettings().get(setting)) continue;
                return false;
            }
        return true;
    }

    public boolean interactionAllowed(Block block, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, block.getLocation())) continue;
            claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
            if (claimManager.getTrustedMembers().get(claim).contains(player.getUniqueId()) || claim.getMembers().contains(player.getUniqueId())) continue;
            return false;
        }
        return true;
    }

    public boolean interactionAllowed(Location location, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
            if (claimManager.getTrustedMembers().get(claim).contains(player.getUniqueId()) || claim.getMembers().contains(player.getUniqueId())) continue;
            return false;
        }
        return true;
    }

    public boolean interactionAllowed(Location location) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            return false;
        }
        return true;
    }

    public boolean interactionAllowed(Location location, ClaimSetting setting) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            if (claim.getClaimSettings().get(setting)) continue;
            return false;
        }
        return true;
    }

    public boolean interactionAllowed(Location location, ClaimSetting setting, Player player) {
        if (player.hasPermission("scepter.adminbypass")) return true;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, location)) continue;
            claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
            if (claimManager.getTrustedMembers().get(claim).contains(player.getUniqueId()) || claim.getMembers().contains(player.getUniqueId()) || claim.getClaimSettings().get(setting)) continue;
            return false;
        }
        return true;
    }
}