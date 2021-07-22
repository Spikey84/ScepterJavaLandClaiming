package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Claim {
    private Location origin;
    private int xLength;

    private int zLength;
    private UUID owner;
    private List<UUID> members;
    private HashMap<ClaimSetting, Boolean> claimSettings;
    private int id;

    public Claim(Location origin, int xLength, int zLength, UUID owner, List<UUID> members, HashMap<ClaimSetting, Boolean> claimSettings) {
        this.origin = origin;

        this.xLength = xLength;
        this.zLength = zLength;
        this.owner = owner;
        this.members = members;
        this.claimSettings = claimSettings;
        this.id = -1;
    }

    public Location getOrigin() {
        return origin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXLength() {
        return xLength;
    }

    public int getZLength() {
        return zLength;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public HashMap<ClaimSetting, Boolean> getClaimSettings() {
        return claimSettings;
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
    }

    public boolean overlaps(Claim claim) {
        if (this.origin.getWorld() != claim.origin.getWorld()) return false;

        Rectangle rectangle1 = new Rectangle(this.origin, new Location(this.origin.getWorld(), this.origin.getBlockX() + this.xLength, this.origin.getBlockY(), this.origin.getBlockZ() + this.zLength));
        Rectangle rectangle2 = new Rectangle(claim.getOrigin(), new Location(claim.origin.getWorld(), claim.origin.getBlockX() + claim.xLength, claim.origin.getBlockY(), claim.origin.getBlockZ() + claim.zLength));

        if (rectangle1.getLeftLocation().getBlockX() == rectangle1.getRightLocation().getBlockX() || rectangle1.getLeftLocation().getBlockZ() == rectangle1.getRightLocation().getBlockZ() || rectangle2.getLeftLocation().getBlockX() == rectangle2.getRightLocation().getBlockX() || rectangle2.getLeftLocation().getBlockZ() == rectangle2.getRightLocation().getBlockZ()) return false;

        if (rectangle1.getLeftLocation().getBlockX() >= rectangle2.getRightLocation().getBlockX() || rectangle2.getLeftLocation().getBlockX() >= rectangle1.getRightLocation().getBlockX()) return false;

        if (rectangle1.getLeftLocation().getBlockZ() >= rectangle2.getRightLocation().getBlockZ() || rectangle2.getLeftLocation().getBlockZ() >= rectangle1.getRightLocation().getBlockZ()) return false;

        return true;
    }

    public static boolean inClaim(Claim claim, Location location) {
        Rectangle rectangle = new Rectangle(claim.getOrigin(), new Location(claim.origin.getWorld(), claim.origin.getBlockX() + claim.xLength, claim.origin.getBlockY(), claim.origin.getBlockZ() + claim.zLength));
        if (location.getBlockX() >= rectangle.getLeftLocation().getBlockX() && location.getBlockX() <= rectangle.getRightLocation().getBlockX() && location.getBlockZ() >= rectangle.getLeftLocation().getBlockZ() && location.getBlockZ() <= rectangle.getRightLocation().getBlockZ()) return true;
        return false;
    }



}
