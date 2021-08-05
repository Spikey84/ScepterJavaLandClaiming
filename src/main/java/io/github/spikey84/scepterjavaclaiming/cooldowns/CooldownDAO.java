package io.github.spikey84.scepterjavaclaiming.cooldowns;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.spikey84.scepterjavaclaiming.ConfigManager;
import io.github.spikey84.scepterjavaclaiming.homes.Home;
import io.github.spikey84.scepterjavaclaiming.utils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CooldownDAO {

    public static HashMap<UUID, HashMap<Byte, Timestamp>> getCooldowns(Connection connection) {
        PreparedStatement statement = null;
        HashMap<UUID, HashMap<Byte, Timestamp>> cooldowns = Maps.newHashMap();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM cooldowns;";

            statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                byte id = (byte) resultSet.getInt("id");
                UUID uuid = UUIDUtils.build(resultSet.getString("uuid"));
                Timestamp time = resultSet.getTimestamp("lastused");
                cooldowns.putIfAbsent(uuid, Maps.newHashMap());
                cooldowns.get(uuid).put(id, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cooldowns;
    }

    public static void removeCooldown(Connection connection, int id, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM cooldowns WHERE id=? AND uuid=?;
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

    public static void updateCooldown(Connection connection, UUID uuid, byte id, Timestamp timestamp) {
        PreparedStatement statement = null;

        try {
            removeCooldown(connection, id, uuid);

            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO cooldowns (uuid, id, lastused) \
                    VALUES\
                    (?, ?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, UUIDUtils.strip(uuid));
            statement.setInt(2, id);
            statement.setTimestamp(3, timestamp);

            statement.execute();
            statement.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
