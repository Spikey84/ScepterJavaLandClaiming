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

public class BuyCommand implements CommandExecutor {
    private EconomyManager economyManager;
    private ConfigManager configManager;
    private ClaimBlocksManager claimBlocksManager;

    public BuyCommand(EconomyManager economyManager, ConfigManager configManager, ClaimBlocksManager claimBlocksManager) {
        this.economyManager = economyManager;
        this.configManager = configManager;
        this.claimBlocksManager = claimBlocksManager;
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
            ChatUtil.message(player, "Enter a number of claim blocks to buy.");
            return true;
        }

        int numberToBuy = 0;

        try {
            numberToBuy = Integer.parseInt(args[0]);
        } catch (Exception e) {
            ChatUtil.message(player, "This is not a valid number.");
            return true;
        }

        if (numberToBuy == 0) {
            ChatUtil.message(player, "This is not a valid number.");
            return true;
        }

        if (!economyManager.chargeMoney(player.getUniqueId(), numberToBuy * configManager.getClaimBlockBuyPice())) {
            ChatUtil.message(player, "You do not have the required funds. (%s per block)".formatted(configManager.getClaimBlockBuyPice()));
            return true;
        } else {
            claimBlocksManager.setBlockCount(player.getUniqueId(), claimBlocksManager.getBlockCount(player.getUniqueId()) + numberToBuy);
            ChatUtil.message(player, "You have successfully bought %s claim blocks for a cost of %s".formatted(numberToBuy, numberToBuy * configManager.getClaimBlockBuyPice()));
        }

        return true;
    }
}
