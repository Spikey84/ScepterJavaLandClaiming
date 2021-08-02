package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.blacklists.BlackListListener;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.commands.ClaimCommand;
import io.github.spikey84.scepterjavaclaiming.commands.ClaimTab;
import io.github.spikey84.scepterjavaclaiming.commands.UnclaimCommand;
import io.github.spikey84.scepterjavaclaiming.homes.HomeManager;
import io.github.spikey84.scepterjavaclaiming.listeners.ClaimToolListener;
import io.github.spikey84.scepterjavaclaiming.listeners.ProtectionListener;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private Plugin plugin;
    private ClaimManager claimManager;
    private ConfigManager configManager;
    private HomeManager homeManager;
    private ClaimBlocksManager claimBlocksManager;

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        this.plugin = this;
        SchedulerUtils.setPlugin(plugin);

        this.databaseManager = new DatabaseManager(plugin);
        this.claimManager = new ClaimManager(plugin);
        this.configManager = new ConfigManager(getConfig());
        this.homeManager = new HomeManager(claimManager);
        this.claimBlocksManager = new ClaimBlocksManager(configManager);

        getCommand("claim").setExecutor(new ClaimCommand(configManager, claimManager, claimBlocksManager, homeManager, plugin));
        getCommand("claim").setTabCompleter(new ClaimTab());
        getCommand("unclaim").setExecutor(new UnclaimCommand(claimManager));

        Bukkit.getPluginManager().registerEvents(new ClaimToolListener(claimManager, plugin, configManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(claimManager), this);
        Bukkit.getPluginManager().registerEvents(new BlackListListener(claimManager), this);
    }
}
