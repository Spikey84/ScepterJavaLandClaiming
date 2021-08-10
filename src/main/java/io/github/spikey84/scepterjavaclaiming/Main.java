package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.blacklists.BlackListListener;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.commands.*;
import io.github.spikey84.scepterjavaclaiming.cooldowns.CooldownManager;
import io.github.spikey84.scepterjavaclaiming.homes.HomeManager;
import io.github.spikey84.scepterjavaclaiming.listeners.ClaimToolListener;
import io.github.spikey84.scepterjavaclaiming.listeners.ProtectionListener;
import io.github.spikey84.scepterjavaclaiming.particles.ParticleManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private DatabaseManager databaseManager;
    private Plugin plugin;
    private ClaimManager claimManager;
    private ConfigManager configManager;
    private HomeManager homeManager;
    private ClaimBlocksManager claimBlocksManager;
    private ParticleManager particleManager;
    private EconomyManager economyManager;
    private CooldownManager cooldownManager;
    private Economy econ = null;

    public static ChatColor primary = null;
    public static ChatColor secondary = null;
    public static String name = "Claiming";

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        primary =  ChatColor.of(config.getString("primaryColor"));
        secondary =  ChatColor.of(config.getString("secondaryColor"));
        name = config.getString("displayTitle");


        this.plugin = this;
        SchedulerUtils.setPlugin(plugin);

        if (!setupEconomy()) {
            Bukkit.getLogger().info("Eco setup failed.");
        }

        this.databaseManager = new DatabaseManager(plugin);
        this.claimManager = new ClaimManager(plugin);
        this.configManager = new ConfigManager(getConfig());
        this.particleManager = new ParticleManager(claimManager, configManager);
        this.economyManager = new EconomyManager(econ);
        this.cooldownManager = new CooldownManager();


        getCommand("claim").setExecutor(new ClaimCommand(configManager, claimManager, claimBlocksManager, homeManager, plugin, cooldownManager, economyManager));
        getCommand("claim").setTabCompleter(new ClaimTab());
        getCommand("unclaim").setExecutor(new UnclaimCommand(claimManager, configManager, claimBlocksManager, economyManager));
        getCommand("adminclaim").setExecutor(new AdminClaim(claimManager, plugin, configManager));
        getCommand("adminclaim").setTabCompleter(new AdminTab());

        Bukkit.getPluginManager().registerEvents(new ClaimToolListener(claimManager, plugin, configManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(claimManager), this);
        Bukkit.getPluginManager().registerEvents(new BlackListListener(claimManager), this);

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;

    }
}
