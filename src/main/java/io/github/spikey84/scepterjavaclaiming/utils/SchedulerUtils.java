package io.github.spikey84.scepterjavaclaiming.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SchedulerUtils {
    private static Plugin plugin = null;

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runRepeating(Runnable runnable, long l) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, l);
    }

    public static void setPlugin(Plugin plugin) {
        SchedulerUtils.plugin = plugin;
    }
}
