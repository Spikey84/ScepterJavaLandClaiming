package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.commands.ClaimItemCommand;
import io.github.spikey84.scepterjavaclaiming.homes.HomeManager;
import io.github.spikey84.scepterjavaclaiming.listeners.ClaimToolListener;
import io.github.spikey84.scepterjavaclaiming.listeners.ProtectionListener;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.util.Collections;
import java.util.List;

public class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private Plugin plugin;
    private ClaimManager claimManager;
    private ConfigManager configManager;
    private HomeManager homeManager;

    private final FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.plugin = this;
        SchedulerUtils.setPlugin(plugin);

        this.databaseManager = new DatabaseManager(plugin);
        this.claimManager = new ClaimManager(plugin);
        this.configManager = new ConfigManager(getConfig());
        this.homeManager = new HomeManager();

        getCommand("claimtool").setExecutor(new ClaimItemCommand(claimManager));

        Bukkit.getPluginManager().registerEvents(new ClaimToolListener(claimManager, plugin, configManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(claimManager), this);
    }
}
