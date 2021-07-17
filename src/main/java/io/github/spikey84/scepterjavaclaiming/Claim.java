package io.github.spikey84.scepterjavaclaiming;

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





}
