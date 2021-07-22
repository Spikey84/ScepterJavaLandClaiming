package io.github.spikey84.scepterjavaclaiming;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.ObjectInputFilter;
import java.util.HashMap;

public class ConfigManager {
    private FileConfiguration config;
    private HashMap<ClaimSetting, Boolean> defaultSettings;
    private long timeNumber;
    private String timeFormat;
    private long timeInSeconds;
    private int claimsPerCycle;
    private int defaultClaims;
    private int claimBlock

    public ConfigManager(FileConfiguration config) {
        this.config = config;
        defaultSettings = new HashMap<ClaimSetting, Boolean>();

        ConfigurationSection claimSettingsSection = config.getConfigurationSection("ClaimSettings");
        String[] keys = claimSettingsSection.getValues(false).keySet().toArray(new String[0]);
        for (String settingskey : keys) {
            if (claimSettingsSection.isConfigurationSection(settingskey)) continue;

            ConfigurationSection section = claimSettingsSection.getConfigurationSection(settingskey);
            defaultSettings.put(ClaimSetting.valueOf(settingskey.toUpperCase()), section.getBoolean("default"));
        }

        timeNumber = config.getInt("Time");
        timeFormat = config.getString("TimeFormat");
        if (timeFormat.contains("minute"))
            timeInSeconds = timeNumber*60;
        else if (timeFormat.contains("hour")) {
            timeInSeconds = timeNumber*60*60;
        } else if (timeFormat.contains("second")) {
            timeInSeconds = timeNumber;
        } else {
            Bukkit.getLogger().info("TimeFormat value is disabled.");
        }
    }

    public HashMap<ClaimSetting, Boolean> getDefaultSettings() {
        return defaultSettings;
    }
}
