package io.github.spikey84.scepterjavaclaiming;

import me.wasteofoxygen.econ.api.Api;
import org.bukkit.Bukkit;

import java.util.UUID;

public class EconomyManager {
    private Api api;

    public EconomyManager(Api api) {
        this.api = api;
    }

    public void addMoney(UUID uuid, int amount) {
        api.addMoney(Bukkit.getOfflinePlayer(uuid), amount);
    }

    public boolean chargeMoney(UUID uuid, int amount) {
        if (api.getBalance(Bukkit.getOfflinePlayer(uuid)) < amount) {
            return false;
        }
        api.takeMoney(Bukkit.getOfflinePlayer(uuid), amount);
        return true;
    }
}
