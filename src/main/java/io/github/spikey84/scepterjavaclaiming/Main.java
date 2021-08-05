package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.blacklists.BlackListListener;
import io.github.spikey84.scepterjavaclaiming.blocks.ClaimBlocksManager;
import io.github.spikey84.scepterjavaclaiming.commands.*;
import io.github.spikey84.scepterjavaclaiming.cooldowns.CooldownManager;
import io.github.spikey84.scepterjavaclaiming.homes.HomeCommand;
import io.github.spikey84.scepterjavaclaiming.homes.HomeManager;
import io.github.spikey84.scepterjavaclaiming.homes.SethomeCommand;
import io.github.spikey84.scepterjavaclaiming.listeners.ClaimToolListener;
import io.github.spikey84.scepterjavaclaiming.listeners.ProtectionListener;
import io.github.spikey84.scepterjavaclaiming.particles.ParticleManager;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import me.wasteofoxygen.econ.ScepterJavaEconomy;
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
    private ParticleManager particleManager;
    private EconomyManager economyManager;
    private CooldownManager cooldownManager;

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
        this.particleManager = new ParticleManager(claimManager, configManager);
        ScepterJavaEconomy ecoPlugin = (ScepterJavaEconomy) getServer().getPluginManager().getPlugin("ScepterJavaEconomy");
        this.economyManager = new EconomyManager(ecoPlugin.getApi());
        this.cooldownManager = new CooldownManager();


        getCommand("claim").setExecutor(new ClaimCommand(configManager, claimManager, claimBlocksManager, homeManager, plugin, cooldownManager));
        getCommand("claim").setTabCompleter(new ClaimTab());
        getCommand("unclaim").setExecutor(new UnclaimCommand(claimManager, configManager, claimBlocksManager));
        getCommand("home").setExecutor(new HomeCommand(homeManager, plugin));
        getCommand("sethome").setExecutor(new SethomeCommand(homeManager));
        getCommand("adminclaim").setExecutor(new AdminClaim(claimManager, plugin, configManager));
        getCommand("adminclaim").setTabCompleter(new AdminTab());
        getCommand("trust").setExecutor(new TrustCommand(claimManager));
        getCommand("untrust").setExecutor(new UnTrust(claimManager));
        getCommand("sellclaimblocks").setExecutor(new SellCommand(claimBlocksManager, configManager, economyManager));
        getCommand("buyclaimblocks").setExecutor(new BuyCommand(economyManager, configManager, claimBlocksManager));

        Bukkit.getPluginManager().registerEvents(new ClaimToolListener(claimManager, plugin, configManager), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(claimManager), this);
        Bukkit.getPluginManager().registerEvents(new BlackListListener(claimManager), this);
    }
}
