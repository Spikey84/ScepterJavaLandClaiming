package io.github.spikey84.scepterjavaclaiming.homes;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Home {
    private Location location;
    private UUID uuid;
    private int id = -1;

    public Home(Location location, UUID uuid) {
        this.location = location;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void teleportToHome(Player player) {
        player.teleport(location);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
