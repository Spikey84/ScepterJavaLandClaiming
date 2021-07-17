package io.github.spikey84.scepterjavaclaiming;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.ObjectInputFilter;
import java.util.HashMap;

public class ConfigManager {
    private FileConfiguration config;
    private HashMap<ClaimSetting, Boolean> defaultSettings;

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
    }
}
