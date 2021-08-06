package io.github.spikey84.scepterjavaclaiming.homes;

import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DelHome implements CommandExecutor {
    private HomeManager homeManager;
    private Plugin plugin;

    public DelHome(HomeManager homeManager, Plugin plugin) {
        this.homeManager = homeManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (!sender.hasPermission("core.home")) {
            ChatUtil.message(player, "You do not have permission to run this command.");
            return true;
        }

        int id = 0;

        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            ChatUtil.message(player, "This is not a valid number.");
            return true;
        }

        Home tmpHome = null;

        for (Home home : homeManager.getHomes(player.getUniqueId())) {
            if (home.getId() != id) continue;
            tmpHome = home;
            break;
        }

        if (tmpHome == null) {
            ChatUtil.message(player, "You do not have a home with this id.");
            return true;
        }

        homeManager.delHome(tmpHome);
        ChatUtil.message(player, "Home has been deleted.");
        return true;
    }
}
