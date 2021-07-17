package io.github.spikey84.scepterjavaclaiming;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.utils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClaimDAO {

    public static List<Claim> getClaims(Connection connection) {
        Statement statement = null;
        List<Claim> claims = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM claims;";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                UUID owner = UUIDUtils.build(resultSet.getString("owner"));
                int xL = resultSet.getInt("x_length");
                int zL = resultSet.getInt("z_length");
                int xO = resultSet.getInt("origin_x");
                int zO = resultSet.getInt("origin_z");
                byte[] settingsBytes = resultSet.getBytes("settings");
                HashMap<ClaimSetting, Boolean> settings = new HashMap<ClaimSetting, Boolean>();
                for (int i = 0; i < settingsBytes.length; i++) {
                    byte b = settingsBytes[i];
                    if (b == (byte) 1) settings.put(ClaimSetting.getFromID((byte) i), true); else settings.put(ClaimSetting.getFromID((byte) i), false);
                }
                String worldName = resultSet.getString("world_name");
                String serverName = resultSet.getString("server_name");

                claims.add(new Claim(new Location(Bukkit.getWorld(worldName), xO, 255, zO), xL, zL, owner, getMembers(connection, id), settings));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return claims;
    }

    public static void delClaim(Connection connection, int id) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection.setAutoCommit(false);

            String query = """
                    DELETE FROM claims WHERE id=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();

            statement.close();

            query = """
                    DELETE FROM claim_members WHERE claim_id=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addClaim(Connection connection, Claim claim) {
        PreparedStatement statement = null;

        List<Byte> settings = Lists.newArrayList();
        for (Map.Entry<ClaimSetting, Boolean> entry : claim.getClaimSettings().entrySet()) {
            if (entry.getValue()) settings.add((byte) 1); else settings.add((byte) 0);
        }
        byte[] bytes = new byte[]{};
        for (byte b : settings) {
            bytes[bytes.length] = b;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            String query;

            if (claim.getId() != -1) query = """
                    INSERT INTO active_multipliers (owner, settings, x_length, z_length, origin_x, origin_z, world_name, server_name)\
                    VALUES\
                    (?, ?, ?, ?, ?, ?, ?, ?);
                    """; else query = """
                    INSERT INTO active_multipliers (id, owner, settings, x_length, z_length, origin_x, origin_z, world_name, server_name)\
                    VALUES\
                    (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE \
                    SET id=?, owner=?, settings=?, x_length=?, z_length=?, origin_x=?, origin_z=?, world_name=?, server_name=?;
                    """;
            statement = connection.prepareStatement(query);

            if (claim.getId() != -1) {
                statement.setInt(1, claim.getId());
                statement.setString(2, UUIDUtils.strip(claim.getOwner()));
                statement.setBytes(3, bytes);
                statement.setInt(4, claim.getXLength());
                statement.setInt(5, claim.getZLength());
                statement.setInt(6, claim.getOrigin().getBlockX());
                statement.setInt(7, claim.getOrigin().getBlockZ());
                statement.setString(8, claim.getOrigin().getWorld().getName());
                statement.setString(9, Bukkit.getServer().getName());
                statement.setInt(10, claim.getId());
                statement.setString(11, UUIDUtils.strip(claim.getOwner()));
                statement.setBytes(12, bytes);
                statement.setInt(13, claim.getXLength());
                statement.setInt(14, claim.getZLength());
                statement.setInt(15, claim.getOrigin().getBlockX());
                statement.setInt(16, claim.getOrigin().getBlockZ());
                statement.setString(17, claim.getOrigin().getWorld().getName());
                statement.setString(18, Bukkit.getServer().getName());
            } else {
                statement.setString(1, UUIDUtils.strip(claim.getOwner()));
                statement.setBytes(2, bytes);
                statement.setInt(3, claim.getXLength());
                statement.setInt(4, claim.getZLength());
                statement.setInt(5, claim.getOrigin().getBlockX());
                statement.setInt(6, claim.getOrigin().getBlockZ());
                statement.setString(7, claim.getOrigin().getWorld().getName());
                statement.setString(8, Bukkit.getServer().getName());


            }


            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<UUID> getMembers(Connection connection, int id) {
        PreparedStatement statement = null;
        List<UUID> members = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT uuid FROM claim_members WHERE claim_id=?;";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                UUID uuid = UUIDUtils.build(resultSet.getString("uuid"));
                members.add(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return members;
    }

    public static void removeMember(Connection connection, int id, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM claim_members WHERE claim_id=? AND uuid=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, UUIDUtils.strip(uuid));
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMember(Connection connection, int id, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO claim_members (id, uuid) \
                    VALUES\
                    (?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            statement.setString(2, UUIDUtils.strip(uuid));

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}