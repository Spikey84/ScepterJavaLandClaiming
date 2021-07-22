package io.github.spikey84.scepterjavaclaiming.homes;


import org.bukkit.Location;

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
}
