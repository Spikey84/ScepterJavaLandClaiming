package io.github.spikey84.scepterjavaclaiming;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClaimManager {
    private List<Claim> claims;
    private Plugin plugin;
    private HashMap<UUID, Rectangle> tempClaiming;
    private HashMap<Claim, List<UUID>> trustedMembers;

    public ClaimManager(Plugin plugin) {
        this.plugin = plugin;

        this.trustedMembers = Maps.newHashMap();

        claims = Lists.newArrayList();
        tempClaiming = Maps.newHashMap();
        try (Connection connection = DatabaseManager.getConnection()) {
            claims = ClaimDAO.getClaims(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addClaim(Claim claim) {
        List<Claim> toRM = Lists.newArrayList();
        for (Claim c : claims) {
            if (c.getId() == claim.getId()) toRM.add(c);
        }
        claims.removeAll(toRM);
        claims.add(claim);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.addClaim(connection, claim);
                addMember(claim.getOwner(), claim);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delClaim(Claim claim) {
        claims.remove(claim);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.delClaim(connection, claim.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addMember(UUID uuid, Claim claim) {
        claim.addMember(uuid);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.addMember(connection, claim.getId(), uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeMember(UUID uuid, Claim claim) {
        claim.removeMember(uuid);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.removeMember(connection, claim.getId(), uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Claim getClaimByID(int id) {
        for (Claim claim : claims) {
            if (claim.getId() == id) return claim;
        }
        return null;
    }

    public HashMap<Claim, List<UUID>> getTrustedMembers() {
        return trustedMembers;
    }

    public HashMap<UUID, Rectangle> getTempClaiming() {
        return tempClaiming;
    }

    public List<Claim> getClaims() {
        return claims;
    }
}
