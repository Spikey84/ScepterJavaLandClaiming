package io.github.spikey84.scepterjavaclaiming.commands;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class UnTrust implements CommandExecutor {
    private ClaimManager claimManager;

    public UnTrust(ClaimManager claimManager) {
        this.claimManager = claimManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run as a player!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("scepter.default")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        if (args.length == 0) {
            ChatUtil.message(player, "Enter a player to un-trust.");
            return true;
        }

        if (Bukkit.getOfflinePlayerIfCached(args[0]) == null) {
            ChatUtil.message(player, "This player has never logged on.");
            return true;
        }

        UUID target = Objects.requireNonNull(Bukkit.getOfflinePlayerIfCached(args[0])).getUniqueId();

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                continue;
            }

            if (!claim.getOwner().equals(player.getUniqueId())) {
                ChatUtil.message(player, "You must be the owner of this claim to remove a trusted member.");
                return true;
            }

            claimManager.getTrustedMembers().putIfAbsent(claim, Lists.newArrayList());
            if (!claimManager.getTrustedMembers().get(claim).contains(target)) {
                ChatUtil.message(player, "This player is not trusted in this claim.");
                return true;
            }
            claimManager.getTrustedMembers().get(claim).remove(target);
            ChatUtil.message(player, "This player is no longer trusted in your claim.");

            return true;
        }
        ChatUtil.message(player, "You must be a claim to use this command.");

        return true;
    }
}
