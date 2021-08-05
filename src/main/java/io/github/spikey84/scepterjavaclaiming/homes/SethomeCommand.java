package io.github.spikey84.scepterjavaclaiming.homes;

import io.github.spikey84.scepterjavaclaiming.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

public class SethomeCommand implements CommandExecutor {
    private HomeManager homeManager;

    public SethomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }

        Player player = (Player) sender;

        int homeAmount = 0;

        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getPermission().startsWith("scepter.home")) {
                homeAmount = Integer.parseInt(attachmentInfo.getPermission().substring(attachmentInfo.getPermission().lastIndexOf(".") + 1));
            }
        }

        if (homeManager.getHomes(player.getUniqueId()).size() >= homeAmount) {
            ChatUtil.message(player, "You are already at your max number of homes.");
            return true;
        }

        homeManager.addHome(new Home(player.getLocation(), player.getUniqueId()));
        ChatUtil.message(player, "Home created!");
        return true;
    }
}
