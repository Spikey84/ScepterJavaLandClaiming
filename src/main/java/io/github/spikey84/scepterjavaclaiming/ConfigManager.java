package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.utils.Lore;
import io.github.spikey84.scepterjavaclaiming.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ConfigManager {
    private FileConfiguration config;
    private HashMap<ClaimSetting, Boolean> defaultSettings;
    private long timeNumber;
    private String timeFormat;
    private long timeInSeconds;
    private long claimsPerCycle;
    private long defaultClaims;
    private int claimBlockBuyPice;
    private int claimBlockSellPrice;
    private int unclaimTax;
    private int unclaimTaxChargeMin;

    private Material landClaimMat;
    private ItemStack claimTool;

    private Material claimCheckerMat;
    private ItemStack claimChecker;

    private int toolCooldown;

    private List<String> disabledWorlds;

    private List<String> help;

    public ConfigManager(FileConfiguration config) {
        this.config = config;
        defaultSettings = new HashMap<ClaimSetting, Boolean>();

        ConfigurationSection claimSettingsSection = config.getConfigurationSection("ClaimSettings");
        Set<String> keys = claimSettingsSection.getValues(false).keySet();
        for (String settingskey : keys) {
            //if (claimSettingsSection.isConfigurationSection(settingskey)) continue;

            ConfigurationSection section = claimSettingsSection.getConfigurationSection(settingskey);
            defaultSettings.put(ClaimSetting.valueOf(settingskey.toUpperCase().replace(" ", "_")), section.getBoolean("default"));
            Bukkit.getLogger().info(ClaimSetting.valueOf(settingskey.toUpperCase().replace(" ", "_")) + "" + section.getBoolean("default"));
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
            timeInSeconds = 3600;//defaults to an hour
            Bukkit.getLogger().info("TimeFormat value is disabled.");
        }
        claimsPerCycle = config.getLong("ClaimBlocksPerTime");

        defaultClaims = config.getLong("DefaultClaimBlocks");
        claimBlockBuyPice = config.getInt("PricePerClaimBlock");
        claimBlockSellPrice = config.getInt("PricePerClaimBlockSell");
        unclaimTax = config.getInt("UnclaimPercent");
        unclaimTaxChargeMin = config.getInt("UnclaimPercentWorking");

        try {
            landClaimMat = Material.valueOf(config.getString("LandClaimTool").toUpperCase());
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid item, defaulting to golden_shovel");
            landClaimMat = Material.GOLDEN_SHOVEL;
        }

        claimTool = new ItemStack(landClaimMat);
        ItemMeta itemMeta = claimTool.getItemMeta();
        itemMeta.setDisplayName(StringUtils.formatColors(config.getString("LandClaimToolName")));
        itemMeta.setLore(Lore.createLore(StringUtils.formatColors(config.getString("LandClaimToolLore"))).getContents());
        claimTool.setItemMeta(itemMeta);
        claimTool.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        claimTool.addEnchantment(Enchantment.DURABILITY, 1);

        try {
            claimCheckerMat = Material.valueOf(config.getString("LandClaimChecker").toUpperCase());
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid item, defaulting to golden_shovel");
            claimCheckerMat = Material.GOLDEN_SHOVEL;
        }

        claimChecker = new ItemStack(claimCheckerMat);
        itemMeta = claimChecker.getItemMeta();
        itemMeta.setDisplayName(StringUtils.formatColors(config.getString("LandClaimCheckerName")));
        itemMeta.setLore(Lore.createLore(StringUtils.formatColors(config.getString("LandClaimCheckerLore"))).getContents());
        claimChecker.setItemMeta(itemMeta);
        claimChecker.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        claimChecker.addEnchantment(Enchantment.DURABILITY, 1);


        toolCooldown = config.getInt("ToolCooldown");

        disabledWorlds = config.getStringList("ClaimDisabledWorlds");

        help = config.getStringList("ClaimHelp");

    }

    public HashMap<ClaimSetting, Boolean> getDefaultSettings() {
        return defaultSettings;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public long getClaimsPerCycle() {
        return claimsPerCycle;
    }

    public long getDefaultClaims() {
        return defaultClaims;
    }

    public int getClaimBlockBuyPice() {
        return claimBlockBuyPice;
    }

    public int getClaimBlockSellPrice() {
        return claimBlockSellPrice;
    }

    public int getUnclaimTax() {
        return unclaimTax;
    }

    public int getUnclaimTaxChargeMin() {
        return unclaimTaxChargeMin;
    }

    public ItemStack getClaimTool() {
        return claimTool;
    }

    public ItemStack getClaimChecker() {
        return claimChecker;
    }

    public int getToolCooldown() {
        return toolCooldown;
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public List<String> getHelp() {
        return help;
    }
}
