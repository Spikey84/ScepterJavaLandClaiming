package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.commands.ClaimItemCommand;
import io.github.spikey84.scepterjavaclaiming.listeners.ClaimToolListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.util.Collections;
import java.util.List;

public class Main extends JavaPlugin {
    private Plugin plugin;
    private ClaimManager claimManager;
    private ConfigManager configManager;

    private final FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.claimManager = new ClaimManager(plugin);
        this.configManager = new ConfigManager(getConfig());


        List<Boolean> defaultSettingValues = Collections.emptyList();
        ConfigurationSection section = config.getConfigurationSection("ClaimSettings");
        for (String key : section.getKeys(false)) {
            defaultSettingValues.add(section.getBoolean(key));
        }

        getCommand("claimtool").setExecutor(new ClaimItemCommand(claimManager));

        Bukkit.getPluginManager().registerEvents(new ClaimToolListener(claimManager), this);
    }
}
