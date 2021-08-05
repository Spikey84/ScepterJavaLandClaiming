package io.github.spikey84.scepterjavaclaiming;

import io.github.spikey84.scepterjavaclaiming.blacklists.BlackListDAO;
import io.github.spikey84.scepterjavaclaiming.utils.Rectangle;
import io.github.spikey84.scepterjavaclaiming.utils.SchedulerUtils;
import org.bukkit.Location;

import java.sql.Connection;
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
    private List<UUID> blackList;
    private String worldName;

    public Claim(Location origin, int xLength, int zLength, UUID owner, List<UUID> members, HashMap<ClaimSetting, Boolean> claimSettings, List<UUID> blackList, String worldName) {
        this.origin = origin;

        this.xLength = xLength;
        this.zLength = zLength;
        this.owner = owner;
        this.members = members;
        this.claimSettings = claimSettings;
        this.id = -1;
        this.blackList = blackList;
        this.worldName = worldName;
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

    public List<UUID> getBlackList() {
        return blackList;
    }

    public HashMap<ClaimSetting, Boolean> getClaimSettings() {
        return claimSettings;
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.addMember(connection, id, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.removeMember(connection, id, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addBlacklist(UUID uuid) {
        this.blackList.add(uuid);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                BlackListDAO.addBlacklistedPlayer(connection, id, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeBlackList(UUID uuid) {
        this.blackList.remove(uuid);
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                BlackListDAO.removeBlacklistedPlayer(connection, id, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        SchedulerUtils.runAsync(() -> {
            try (Connection connection = DatabaseManager.getConnection()) {
                ClaimDAO.addClaim(connection, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public boolean overlaps(Claim claim) {
        if (this.origin.getWorld() != claim.origin.getWorld()) return false;

        Rectangle rectangle1 = new Rectangle(this.origin, new Location(this.origin.getWorld(), this.origin.getBlockX() + this.xLength, this.origin.getBlockY(), this.origin.getBlockZ() + this.zLength));
        Rectangle rectangle2 = new Rectangle(claim.getOrigin(), new Location(claim.origin.getWorld(), claim.origin.getBlockX() + claim.xLength, claim.origin.getBlockY(), claim.origin.getBlockZ() + claim.zLength));

//        if (rectangle1.getLeftLocation().getBlockX() == rectangle1.getRightLocation().getBlockX() || rectangle1.getLeftLocation().getBlockZ() == rectangle1.getRightLocation().getBlockZ() || rectangle2.getLeftLocation().getBlockX() == rectangle2.getRightLocation().getBlockX() || rectangle2.getLeftLocation().getBlockZ() == rectangle2.getRightLocation().getBlockZ()) return false;
//
//        if (rectangle1.getLeftLocation().getBlockX() >= rectangle2.getRightLocation().getBlockX() || rectangle2.getLeftLocation().getBlockX() >= rectangle1.getRightLocation().getBlockX()) return false;
//
//        if (rectangle1.getLeftLocation().getBlockZ() >= rectangle2.getRightLocation().getBlockZ() || rectangle2.getLeftLocation().getBlockZ() >= rectangle1.getRightLocation().getBlockZ()) return false;

        boolean xOverlap = false;
        if (rectangle1.getLeftLocation().getBlockX() <= rectangle2.getLeftLocation().getBlockX() && rectangle1.getRightLocation().getBlockX() >= rectangle2.getLeftLocation().getBlockX()) xOverlap = true;
        if (rectangle2.getRightLocation().getBlockX() >= rectangle1.getLeftLocation().getBlockX() && rectangle1.getRightLocation().getBlockX() >= rectangle2.getLeftLocation().getBlockX()) xOverlap = true;

        if (rectangle1.getBottomLocation().getBlockZ() <= rectangle2.getBottomLocation().getBlockZ() && rectangle1.getTopLocation().getBlockZ() >= rectangle2.getBottomLocation().getBlockZ() && xOverlap) return true;
        if (rectangle2.getTopLocation().getBlockZ() >= rectangle1.getBottomLocation().getBlockZ() && rectangle1.getTopLocation().getBlockZ() >= rectangle2.getBottomLocation().getBlockZ() && xOverlap) return true;

        return false;
    }

    public static boolean inClaim(Claim claim, Location location) {
        Rectangle rectangle = new Rectangle(claim.getOrigin(), new Location(claim.origin.getWorld(), claim.origin.getBlockX() + claim.xLength, claim.origin.getBlockY(), claim.origin.getBlockZ() + claim.zLength));
        //if (location.getBlockX() >= rectangle.getLeftLocation().getBlockX() && location.getBlockX() <= rectangle.getRightLocation().getBlockX() && location.getBlockZ() >= rectangle.getLeftLocation().getBlockZ() && location.getBlockZ() <= rectangle.getRightLocation().getBlockZ()) return true;
        boolean xOverlap = false;
        if (location.getBlockX() >= rectangle.getLeftLocation().getBlockX() && location.getBlockX() <= rectangle.getRightLocation().getBlockX()) xOverlap = true;

        if (location.getBlockZ() >= rectangle.getBottomLocation().getBlockZ() && location.getBlockZ() <= rectangle.getTopLocation().getBlockZ() && xOverlap) return true;
        return false;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }
}
