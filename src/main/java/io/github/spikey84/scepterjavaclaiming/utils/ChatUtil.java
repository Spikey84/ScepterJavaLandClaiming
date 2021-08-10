package io.github.spikey84.scepterjavaclaiming.utils;

import io.github.spikey84.scepterjavaclaiming.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {
    static String prefix = Main.secondary + "[" + Main.primary + Main.name + Main.secondary + "]";

    public static void message(Player player, String string) {
        player.sendMessage(prefix + " " + string);
    }
}
