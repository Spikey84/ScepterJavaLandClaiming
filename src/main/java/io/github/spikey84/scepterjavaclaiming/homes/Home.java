package io.github.spikey84.scepterjavaclaiming.homes;


import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Home {
    private Location location;
    private int claimId;

    public Home(Location location, int claimId) {
        this.location = location;
        this.claimId = claimId;
    }

    public int getClaimId() {
        return claimId;
    }

    public Location getLocation() {
        return location;
    }

    public void teleportToHome(Player player) {
        player.teleport(location);
    }
}
