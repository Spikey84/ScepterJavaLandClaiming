package io.github.spikey84.scepterjavaclaiming.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {
    static String prefix = ChatColor.WHITE + "[" + ChatColor.YELLOW + "Claiming" + ChatColor.WHITE + "]";

    public static void message(Player player, String string) {
        player.sendMessage(prefix + " " + string);
    }
}
