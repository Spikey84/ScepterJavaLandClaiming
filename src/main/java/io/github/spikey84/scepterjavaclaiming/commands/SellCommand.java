package io.github.spikey84.scepterjavaclaiming.commands;

import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.EconomyManager;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SellCommand implements CommandExecutor {
    private ClaimBlocksManager claimBlocksManager;
    private ConfigManager configManager;
    private EconomyManager economyManager;

    public SellCommand(ClaimBlocksManager claimBlocksManager, ConfigManager configManager, EconomyManager economyManager) {
        this.claimBlocksManager = claimBlocksManager;
        this.configManager = configManager;

        this.economyManager = economyManager;
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
            ChatUtil.message(player, "Enter a number of claim blocks to sell.");
            return true;
        }

        int numberToSell = 0;

        try {
            numberToSell = Integer.parseInt(args[0]);
        } catch (Exception e) {
            ChatUtil.message(player, "This is not a valid number.");
            return true;
        }

        if (numberToSell == 0) {
            ChatUtil.message(player, "This is not a valid number.");
            return true;
        }

        if (claimBlocksManager.getBlockCount(player.getUniqueId()) - numberToSell < 0) {
            ChatUtil.message(player, "You cannot claim more blocks than you have.");
            return true;
        }

        if (claimBlocksManager.getBlockCount(player.getUniqueId()) < numberToSell) {
            ChatUtil.message(player, "You do not have the required blocks.");
            return true;
        } else {
            claimBlocksManager.setBlockCount(player.getUniqueId(), claimBlocksManager.getBlockCount(player.getUniqueId()) - numberToSell);
            economyManager.addMoney(player.getUniqueId(), numberToSell * configManager.getClaimBlockSellPrice());
            ChatUtil.message(player, "You have successfully bought %s claim blocks for a sum of %s".formatted(numberToSell, numberToSell * configManager.getClaimBlockSellPrice()));
        }

        return true;
    }
}
