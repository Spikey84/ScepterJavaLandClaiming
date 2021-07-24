package io.github.spikey84.scepterjavaclaiming.utils;


import org.bukkit.Location;

public class Rectangle {
    private Location location1;
    private Location location2;
    public Rectangle(Location l1, Location l2) {
        location1 = l1;
        location2 = l2;
    }

    public Location getLeftLocation() {
        return location1.getBlockX() < location2.getBlockX() ? location1 : location2;
    }

    public Location getRightLocation() {
        return location1.getBlockX() < location2.getBlockX() ? location2 : location1;
    }

    public Location getTopLocation() {
        return location1.getBlockZ() < location2.getBlockZ() ? location2 : location1;
    }

    public Location getBottomLocation() {
        return location1.getBlockZ() < location2.getBlockZ() ? location1 : location2;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    public boolean notNullLocations() {
        if (location2 == null || location1 == null) return false;
        return true;
    }

    public int getXLength() {
        return (getRightLocation().getBlockX() - getLeftLocation().getBlockX());
    }

    public int getZLength() {
        return (getTopLocation().getBlockZ() - getBottomLocation().getBlockZ());
    }

    public int getSize() {
        return (getRightLocation().getBlockX() - getLeftLocation().getBlockX()) * (getTopLocation().getBlockZ() - getBottomLocation().getBlockZ());
    }
}
