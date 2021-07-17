package io.github.spikey84.scepterjavaclaiming;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class ClaimManager {
    private List<Claim> claims;
    private Plugin plugin;

    public ClaimManager(Plugin plugin) {
        plugin = plugin;
        claims = Lists.newArrayList();
        try (Connection connection = DatabaseManager.getConnection()) {
            claims = ClaimDAO.getClaims(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addClaim(Claim claim) {
        claims.add(claim);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.addClaim(connection, claim);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delClaim(Claim claim) {
        claims.remove(claim);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.delClaim(connection, claim.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addMember(UUID uuid, Claim claim) {
        claim.addMember(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.addMember(connection, claim.getId(), uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeMember(UUID uuid, Claim claim) {
        claim.removeMember(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.removeMember(connection, claim.getId(), uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ItemStack getClaimItem() {
        ItemStack itemStack = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLocalizedName(ChatColor.DARK_PURPLE + "Claiming Tool");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}