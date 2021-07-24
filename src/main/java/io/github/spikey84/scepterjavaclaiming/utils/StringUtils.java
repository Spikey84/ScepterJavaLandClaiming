package io.github.spikey84.scepterjavaclaiming.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public static String formatColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
