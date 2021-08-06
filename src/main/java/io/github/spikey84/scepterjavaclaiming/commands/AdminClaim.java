package io.github.spikey84.scepterjavaclaiming.commands;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.settings.SettingsInventory;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class AdminClaim implements CommandExecutor {
    private ClaimManager claimManager;
    private Plugin plugin;
    private ConfigManager configManager;

    public AdminClaim(ClaimManager claimManager, Plugin plugin, ConfigManager configManager) {
        this.claimManager = claimManager;
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run as a player!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("scepter.adminclaim")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        if (args.length < 1) {
            ChatUtil.message(player, "Please enter a sub command.");
            return true;
        }

        switch (args[0]) {
            case "unclaim": unclaim(player, args); break;
            case "claim": claim(player, args); break;
            case "settings": settings(player, args); break;
            default: ChatUtil.message(player, "This is not a valid subcommand."); break;
        }

        return true;
    }

    public void settings(Player player, String... args) {
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                continue;
            }

            new SettingsInventory(plugin, claimManager, claim, player).open(player);
            return;
        }
        ChatUtil.message(player, "You must be a claim to use this command.");
    }

    public void unclaim(Player player, String... args) {
        Claim finalClaim = null;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                continue;
            }
            finalClaim = claim;
            ChatUtil.message(player, "This claim has been removed.");
            break;
        }
        if (finalClaim != null) {
            claimManager.delClaim(finalClaim);
            return;
        }
        ChatUtil.message(player, "Please enter a claim you own to unclaim it.");
    }

    //TODO FIGURE OUT WHAT THIS SHOULD DO
    public void claim(Player player, String... args) {
        if (!claimManager.getTempClaiming().get(player.getUniqueId()).notNullLocations()) {
            ChatUtil.message(player, "Set a first and second location in order to claim.");
            return;
        }

        Rectangle rectangle = claimManager.getTempClaiming().get(player.getUniqueId());

        Claim claim = new Claim(rectangle.getBottomLeftLocation(), rectangle.getXLength(), rectangle.getZLength(), player.getUniqueId(), Lists.newArrayList(),configManager.getDefaultSettings(), Lists.newArrayList(), rectangle.getLeftLocation().getWorld().toString());

        for (Claim otherClaim : claimManager.getClaims()) {
            if (claim.overlaps(otherClaim)) {
                ChatUtil.message(player, "This claim overlaps an existing claim.");
                return;
            }
        }

        claimManager.addClaim(claim);
        ChatUtil.message(player, "Area claimed!");
    }


}
