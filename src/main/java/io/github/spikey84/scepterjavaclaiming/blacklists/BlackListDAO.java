package io.github.spikey84.scepterjavaclaiming.blacklists;

import com.google.common.collect.Lists;
import io.github.spikey84.scepterjavaclaiming.utils.UUIDUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public class BlackListDAO {
    public static List<UUID> getBlacklist(Connection connection, int id) {
        PreparedStatement statement = null;
        List<UUID> blacklist = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            String query = "SELECT uuid FROM claim_blacklist WHERE claim_id=?;";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                UUID uuid = UUIDUtils.build(resultSet.getString("uuid"));
                blacklist.add(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blacklist;
    }

    public static void removeBlacklistedPlayer(Connection connection, int id, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    DELETE FROM claim_blacklist WHERE claim_id=? AND uuid=?;
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

    public static void addBlacklistedPlayer(Connection connection, int id, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            String query = """
                    INSERT INTO claim_blacklist (id, uuid) \
                    VALUES\
                    (?, ?) ON DUPLICATE KEY UPDATE id=? uuid=?;
                    """;
            statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            statement.setString(2, UUIDUtils.strip(uuid));
            statement.setInt(3, id);
            statement.setString(4, UUIDUtils.strip(uuid));

            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
