package io.github.spikey84.scepterjavaclaiming.utils;


import org.bukkit.Location;

public class Rectangle {
    private Location leftLocation;
    private Location rightLocation;
    public Rectangle(Location l1, Location l2) {
        leftLocation = l1.getBlockX() < l2.getBlockX() ? l1 : l2;
        rightLocation = l1.getBlockX() < l2.getBlockX() ? l2 : l1;
    }

    public Location getLeftLocation() {
        return leftLocation;
    }

    public Location getRightLocation() {
        return rightLocation;
    }
}
