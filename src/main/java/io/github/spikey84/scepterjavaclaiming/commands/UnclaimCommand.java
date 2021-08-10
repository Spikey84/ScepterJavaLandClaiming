package io.github.spikey84.scepterjavaclaiming.commands;

import io.github.spikey84.scepterjavaclaiming.Claim;
import io.github.spikey84.scepterjavaclaiming.ClaimManager;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.EconomyManager;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnclaimCommand implements CommandExecutor {
    private ClaimManager claimManager;
    private ConfigManager configManager;
    private ClaimBlocksManager claimBlocksManager;
    private EconomyManager economyManager;

    public UnclaimCommand(ClaimManager claimManager, ConfigManager configManager, ClaimBlocksManager claimBlocksManager, EconomyManager economyManager) {
        this.claimManager = claimManager;

        this.configManager = configManager;
        this.claimBlocksManager = claimBlocksManager;
        this.economyManager = economyManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be run as a player!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("claiming.default")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        Claim finalClaim = null;
        for (Claim claim : claimManager.getClaims()) {
            if (!Claim.inClaim(claim, player.getLocation())) {
                continue;
            }
            if (!claim.getOwner().equals(player.getUniqueId())) {
                ChatUtil.message(player, "You must own this claim to unclaim it.");
                continue;
            }
            finalClaim = claim;
            break;
        }
        if (finalClaim == null) {
            ChatUtil.message(player, "Please enter a claim you own to unclaim it.");
            return true;
        }

        claimManager.delClaim(finalClaim);

        //if (finalClaim.getXLength() * finalClaim.getZLength() < configManager.getUnclaimTaxChargeMin()) {
            ChatUtil.message(player, "This claim has been removed. Refunding %s.".formatted((finalClaim.getXLength() * finalClaim.getZLength() * configManager.getClaimBlockSellPrice())));
            economyManager.addMoney(player.getUniqueId(), ((finalClaim.getXLength() * finalClaim.getZLength() * configManager.getClaimBlockSellPrice())));
//        } else {
//            ChatUtil.message(player, "This claim has been removed. Refunding %s.".formatted(((finalClaim.getXLength() * finalClaim.getZLength()) * configManager.getClaimBlockSellPrice() * (configManager.getUnclaimTax()/100))));
//            economyManager.addMoney(player.getUniqueId(), ((finalClaim.getXLength() * finalClaim.getZLength() * configManager.getClaimBlockSellPrice())) * (configManager.getUnclaimTax()/100));
//        }
        return true;
    }
}
