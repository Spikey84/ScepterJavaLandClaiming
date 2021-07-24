package io.github.spikey84.scepterjavaclaiming.commands;

import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnclaimCommand implements CommandExecutor {
    private ClaimManager claimManager;

    public UnclaimCommand(ClaimManager claimManager) {
        this.claimManager = claimManager;

    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run as a player!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("scepter.default")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                ChatUtil.message(player, "Please enter a claim you own to unclaim it.");
                continue;
            }
            if (claim.getOwner().equals(player.getUniqueId())) {
                ChatUtil.message(player, "You must own this claim to unclaim it.");
                continue;
            }
            claimManager.removeMember(player.getUniqueId(), claim);
            ChatUtil.message(player, "This claim has been removed.");
        }
        return true;
    }
}
