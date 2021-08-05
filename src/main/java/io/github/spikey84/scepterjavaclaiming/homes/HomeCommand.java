package io.github.spikey84.scepterjavaclaiming.homes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class HomeCommand implements CommandExecutor {
    private HomeManager homeManager;
    private Plugin plugin;

    public HomeCommand(HomeManager homeManager, Plugin plugin) {
        this.homeManager = homeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        new HomesInventory(plugin, player, homeManager).open(player);
        return true;
    }
}
