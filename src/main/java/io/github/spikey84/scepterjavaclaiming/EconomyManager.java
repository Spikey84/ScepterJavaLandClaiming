package io.github.spikey84.scepterjavaclaiming;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.util.UUID;

public class EconomyManager {
    private Economy api;

    public EconomyManager(Economy api) {
        this.api = api;
    }

    public void addMoney(UUID uuid, int amount) {
        api.depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);
    }

    public boolean chargeMoney(UUID uuid, int amount) {
        if (api.getBalance(Bukkit.getOfflinePlayer(uuid)) < amount) {
            return false;
        }
        api.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        return true;
    }
}
