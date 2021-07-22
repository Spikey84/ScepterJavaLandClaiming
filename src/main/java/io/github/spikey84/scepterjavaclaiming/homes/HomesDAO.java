package io.github.spikey84.scepterjavaclaiming.homes;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.utils.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HomesDAO {
    public static List<Home> getHomes(Connection connection) {
        PreparedStatement statement = null;
        List<Home> homes = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT * FROM claim_homes;";

            statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Location location = new Location(Bukkit.getWorld(resultSet.getString("world_name")), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
                int id = resultSet.getInt("claim_id");
                homes.add(new Home(location, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return homes;
    }

    public static void removeHome(Connection connection, int id) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM claim_homes WHERE claim_id=?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHome(Connection connection, Home home) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO claim_homes (claim_id, x, y, z, world_name) \
                    VALUES\
                    (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE x=? y=? z=? world_name=?;
                    """;
            statement = connection.prepareStatement(query);

            statement.setInt(1, home.getClaimId());
            statement.setInt(2, home.getLocation().getBlockX());
            statement.setInt(3, home.getLocation().getBlockY());
            statement.setInt(4, home.getLocation().getBlockZ());
            statement.setString(5, home.getLocation().getWorld().getName());
            statement.setInt(6, home.getLocation().getBlockX());
            statement.setInt(7, home.getLocation().getBlockY());
            statement.setInt(8, home.getLocation().getBlockZ());
            statement.setString(9, home.getLocation().getWorld().getName());

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
